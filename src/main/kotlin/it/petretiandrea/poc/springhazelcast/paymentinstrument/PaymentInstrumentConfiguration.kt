package it.petretiandrea.poc.springhazelcast.paymentinstrument

import it.petretiandrea.poc.springhazelcast.paymentinstrument.domain.PaymentInstrumentRepository
import it.petretiandrea.poc.springhazelcast.paymentinstrument.infra.ignite.IgnitePaymentInstrumentRepository
import org.apache.ignite.Ignite
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PaymentInstrumentConfiguration {

//    @Bean
//    fun paymentInstrumentRepository(
//        //@Qualifier("server") hazelcastInstance: HazelcastInstance
//    ): PaymentInstrumentRepository {
//        return object:  PaymentInstrumentRepository {
//            override suspend fun findById(id: PaymentInstrumentId): PaymentInstrument? {
//                TODO("Not yet implemented")
//            }
//
//            override suspend fun save(paymentInstrument: PaymentInstrument): Result<PaymentInstrument> {
//                TODO("Not yet implemented")
//            }
//
//        }
//        //return HazelcastPaymentInstrumentRepository(hazelcastInstance)
//    }

    @Bean
    fun paymentInstrumentRepository(
            ignite: Ignite
    ): PaymentInstrumentRepository {
        return IgnitePaymentInstrumentRepository(ignite)
    }
}