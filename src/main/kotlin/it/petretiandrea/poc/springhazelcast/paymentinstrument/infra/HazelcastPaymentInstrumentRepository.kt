package it.petretiandrea.poc.springhazelcast.paymentinstrument.infra

import it.petretiandrea.poc.springhazelcast.paymentinstrument.domain.PaymentInstrument
import it.petretiandrea.poc.springhazelcast.paymentinstrument.domain.PaymentInstrumentId
import it.petretiandrea.poc.springhazelcast.paymentinstrument.domain.PaymentInstrumentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono

class HazelcastPaymentInstrumentRepository(
    private val paymentInstrumentMongoRepository: PaymentInstrumentMongoRepository
) : PaymentInstrumentRepository {


    override suspend fun findById(id: PaymentInstrumentId): PaymentInstrument? {
        return withContext(Dispatchers.IO) {
            paymentInstrumentMongoRepository.findById(id)
                .toMono()
                .block()
        }
    }

    override suspend fun save(paymentInstrument: PaymentInstrument): Result<PaymentInstrument> {
        return withContext(Dispatchers.IO) {
            val wrote = paymentInstrumentMongoRepository.save(paymentInstrument).block()
            wrote?.let { Result.success(it) } ?: Result.failure(Error("Failed to save"))
        }
    }
}