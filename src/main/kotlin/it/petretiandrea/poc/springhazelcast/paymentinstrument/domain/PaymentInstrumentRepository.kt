package it.petretiandrea.poc.springhazelcast.paymentinstrument.domain

interface PaymentInstrumentRepository {

    suspend fun findById(id: PaymentInstrumentId): PaymentInstrument?

    suspend fun save(paymentInstrument: PaymentInstrument): Result<PaymentInstrument>

}