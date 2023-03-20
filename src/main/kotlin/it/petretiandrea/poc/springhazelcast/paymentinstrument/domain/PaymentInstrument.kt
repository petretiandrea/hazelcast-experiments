package it.petretiandrea.poc.springhazelcast.paymentinstrument.domain

typealias PaymentInstrumentId = String
typealias PaymentToken = String
typealias PAR = String

data class PaymentInstrument(
    val id: PaymentInstrumentId,
    var tokens: List<PaymentToken>,
    var par: PAR?
){
    fun addToken(token: PaymentToken) {
        this.tokens += token
    }

    fun addTokens(tokens: Iterable<PaymentToken>) {
        this.tokens += tokens
    }

    fun associatePAR(par: PAR) {
        this.par = par
    }
}