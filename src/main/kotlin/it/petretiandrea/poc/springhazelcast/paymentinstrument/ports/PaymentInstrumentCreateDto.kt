package it.petretiandrea.poc.springhazelcast.paymentinstrument.ports

data class PaymentInstrumentCreateDto(
    val id: String,
    val tokens: List<String>? = emptyList(),
    val par: String?
)
