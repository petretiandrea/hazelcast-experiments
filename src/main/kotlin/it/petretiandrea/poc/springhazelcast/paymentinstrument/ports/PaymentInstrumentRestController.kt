package it.petretiandrea.poc.springhazelcast.paymentinstrument.ports

import it.petretiandrea.poc.springhazelcast.paymentinstrument.domain.PaymentInstrument
import it.petretiandrea.poc.springhazelcast.paymentinstrument.domain.PaymentInstrumentRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("payment-instrument")
class PaymentInstrumentRestController(
    private val repository: PaymentInstrumentRepository
) {

    @PutMapping("/")
    suspend fun addInstrument(@RequestBody request: PaymentInstrumentCreateDto) {
        val paymentInstrument = PaymentInstrument(
            request.id,
            request.tokens.orEmpty(),
            request.par
        )
        repository.save(paymentInstrument)
    }

    @GetMapping("/{id}")
    suspend fun getInstruments(@PathVariable("id") id: String): PaymentInstrument? {
        return repository.findById(id)
    }


}