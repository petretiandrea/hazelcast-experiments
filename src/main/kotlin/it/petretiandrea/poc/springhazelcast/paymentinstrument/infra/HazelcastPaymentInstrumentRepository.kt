package it.petretiandrea.poc.springhazelcast.paymentinstrument.infra

import com.hazelcast.core.Hazelcast
import com.hazelcast.core.HazelcastInstance
import com.hazelcast.nio.ObjectDataInput
import com.hazelcast.nio.ObjectDataOutput
import com.hazelcast.nio.serialization.StreamSerializer
import it.petretiandrea.poc.springhazelcast.customer.domain.Customer
import it.petretiandrea.poc.springhazelcast.paymentinstrument.domain.PaymentInstrument
import it.petretiandrea.poc.springhazelcast.paymentinstrument.domain.PaymentInstrumentId
import it.petretiandrea.poc.springhazelcast.paymentinstrument.domain.PaymentInstrumentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.withContext
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono

class HazelcastPaymentInstrumentRepository(
    hazelcast: HazelcastInstance
) : PaymentInstrumentRepository {

    private val instruments = hazelcast.getMap<String, PaymentInstrument>("payment-instrument")

    override suspend fun findById(id: PaymentInstrumentId): PaymentInstrument? {
        return withContext(Dispatchers.IO) {
            instruments.getOrDefault(id, null)
        }
    }

    override suspend fun save(paymentInstrument: PaymentInstrument): Result<PaymentInstrument> {
        return withContext(Dispatchers.IO) {
            instruments.put(paymentInstrument.id, paymentInstrument)?.let {
                Result.success(it)
            } ?: Result.failure(Error("Error during save"))
        }
    }

    companion object {
        fun serializer() : StreamSerializer<PaymentInstrument> {
            return object : StreamSerializer<PaymentInstrument> {
                override fun getTypeId(): Int {
                    return 1
                }

                override fun read(`in`: ObjectDataInput): PaymentInstrument {
                    return PaymentInstrument(
                        `in`.readString() ?: "",
                        `in`.readStringArray()?.toList().orEmpty(),
                        `in`.readString()
                    )
                }

                override fun write(out: ObjectDataOutput, obj: PaymentInstrument) {
                    out.apply {
                        writeString(obj.id)
                        writeStringArray(obj.tokens.toTypedArray())
                        writeString(obj.par)
                    }
                }
            }
        }
    }
}