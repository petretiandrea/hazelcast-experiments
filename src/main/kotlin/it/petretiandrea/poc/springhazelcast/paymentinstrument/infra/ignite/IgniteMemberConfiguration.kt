package it.petretiandrea.poc.springhazelcast.paymentinstrument.infra.ignite

import it.petretiandrea.poc.springhazelcast.paymentinstrument.domain.PaymentInstrument
import it.petretiandrea.poc.springhazelcast.paymentinstrument.infra.mongo.PaymentInstrumentMongoRepository
import org.apache.ignite.Ignite
import org.apache.ignite.IgniteCheckedException
import org.apache.ignite.IgniteException
import org.apache.ignite.IgniteSpring
import org.apache.ignite.Ignition
import org.apache.ignite.cache.CacheAtomicityMode
import org.apache.ignite.cache.CacheMode
import org.apache.ignite.cache.store.CacheStoreAdapter
import org.apache.ignite.configuration.CacheConfiguration
import org.apache.ignite.configuration.IgniteConfiguration
import org.apache.ignite.internal.IgniteComponentType
import org.apache.ignite.internal.util.spring.IgniteSpringHelper
import org.apache.ignite.resources.SpringApplicationContextResource
import org.springframework.cache.CacheManager
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.cache.configuration.Factory
import javax.cache.configuration.FactoryBuilder


@Configuration
class IgniteMemberConfiguration {

    @Bean
    fun configuration(
            cacheConfiguration: CacheConfiguration<String, PaymentInstrument>
    ): IgniteConfiguration {
        return IgniteConfiguration().apply {
            igniteInstanceName = "ignite-cluster"
            consistentId = "consistentId"
            isPeerClassLoadingEnabled = true
            setCacheConfiguration(cacheConfiguration)
        }
    }

    @Bean
    fun ignite(configuration: IgniteConfiguration, applicationContext: ApplicationContext): Ignite {
        val repo = applicationContext.getBeansOfType(PaymentInstrumentMongoRepository::class.java)
        return IgniteSpring.start(configuration, applicationContext)
    }

    @Bean
    fun cacheConfiguration(): CacheConfiguration<String, PaymentInstrument> {
        return CacheConfiguration<String, PaymentInstrument>().apply {
            name = "payment-instrument"
            cacheMode = CacheMode.PARTITIONED
            atomicityMode = CacheAtomicityMode.ATOMIC
            isReadThrough = true
            isWriteThrough = true
            isOnheapCacheEnabled = true
            setTypes(String::class.java, PaymentInstrument::class.java)
            setCacheStoreFactory(MongoFactory())
        }
    }

    class MongoFactory : Factory<CacheStoreAdapter<String, PaymentInstrument>> {

        private val serialVersionUID = 0L

        @SpringApplicationContextResource
        private var appCtx: ApplicationContext? = null

        override fun create(): CacheStoreAdapter<String, PaymentInstrument> {
            val spring: IgniteSpringHelper

            try {
                spring = IgniteComponentType.SPRING.create(false)
                val data = spring.loadBeanFromAppContext<PaymentInstrumentMongoRepository>(appCtx, "paymentInstrumentMongoRepository")
                return PaymentInstrumentCacheStore(data)
            } catch (ignored: IgniteCheckedException) {
                throw IgniteException("Failed to load bean in application context ${appCtx}")
            }
        }
    }
}