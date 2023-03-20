package it.petretiandrea.poc.springhazelcast.configuration

import com.hazelcast.config.Config
import com.hazelcast.config.MapStoreConfig
import com.hazelcast.config.SerializerConfig
import com.hazelcast.core.Hazelcast
import com.hazelcast.core.HazelcastInstance
import it.petretiandrea.poc.springhazelcast.paymentinstrument.domain.PaymentInstrument
import it.petretiandrea.poc.springhazelcast.paymentinstrument.infra.HazelcastPaymentInstrumentRepository
import it.petretiandrea.poc.springhazelcast.paymentinstrument.infra.PaymentInstrumentMapStore
import it.petretiandrea.poc.springhazelcast.paymentinstrument.infra.PaymentInstrumentMongoRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MemberConfiguration {


    @Bean
    fun paymentInstrumentMapConfig(repo: PaymentInstrumentMongoRepository) : MapStoreConfig {
        val mapStore = PaymentInstrumentMapStore(repo)
        return MapStoreConfig().apply {
            isEnabled = true
            implementation = mapStore
            initialLoadMode = MapStoreConfig.InitialLoadMode.EAGER
            writeDelaySeconds = 0 // write throug enabled
        }
    }

    @Bean
    @Qualifier("server")
    fun member(paymentInstrumentMapConfig: MapStoreConfig): HazelcastInstance {
        val config = Config().apply {
            getMapConfig("payment-instrument").apply {
                mapStoreConfig = paymentInstrumentMapConfig
            }
            networkConfig.join.multicastConfig.isEnabled = true
            serializationConfig.addSerializerConfig(
                SerializerConfig()
                    .setImplementation(HazelcastPaymentInstrumentRepository.serializer())
                    .setTypeClass(PaymentInstrument::class.java))
        }

        return Hazelcast.newHazelcastInstance(config)
    }
}