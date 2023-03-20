package it.petretiandrea.poc.springhazelcast.paymentinstrument.infra.mongo

import it.petretiandrea.poc.springhazelcast.paymentinstrument.domain.PaymentInstrument
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface PaymentInstrumentMongoRepository : ReactiveMongoRepository<PaymentInstrument, String>