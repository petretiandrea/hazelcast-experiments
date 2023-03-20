package it.petretiandrea.poc.springhazelcast.paymentinstrument

import com.hazelcast.core.HazelcastInstance
import it.petretiandrea.poc.springhazelcast.paymentinstrument.domain.PaymentInstrumentRepository
import it.petretiandrea.poc.springhazelcast.paymentinstrument.infra.hazelcast.HazelcastPaymentInstrumentRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PaymentInstrumentConfiguration {

    @Bean
    fun paymentInstrumentRepository(
        @Qualifier("server") hazelcastInstance: HazelcastInstance
    ): PaymentInstrumentRepository {
        return HazelcastPaymentInstrumentRepository(hazelcastInstance)
    }
}