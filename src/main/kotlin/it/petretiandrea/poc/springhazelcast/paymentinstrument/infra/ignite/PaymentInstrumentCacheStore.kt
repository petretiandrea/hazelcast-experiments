package it.petretiandrea.poc.springhazelcast.paymentinstrument.infra.ignite

import it.petretiandrea.poc.springhazelcast.paymentinstrument.domain.PaymentInstrument
import it.petretiandrea.poc.springhazelcast.paymentinstrument.infra.mongo.PaymentInstrumentMongoRepository
import org.apache.ignite.cache.store.CacheStoreAdapter
import org.apache.ignite.lang.IgniteBiInClosure
import javax.cache.Cache

class PaymentInstrumentCacheStore(
        private val paymentInstrumentMongoRepository: PaymentInstrumentMongoRepository
) : CacheStoreAdapter<String, PaymentInstrument>() {


    override fun load(key: String?): PaymentInstrument? {
        return paymentInstrumentMongoRepository
                .findById(key!!)
                .block()
    }

    override fun write(entry: Cache.Entry<out String, out PaymentInstrument>?) {
        paymentInstrumentMongoRepository.save(entry?.value!!).block()
    }

    override fun delete(key: Any?) {
        key?.let { paymentInstrumentMongoRepository.deleteById(it.toString()) }
    }

    override fun loadCache(clo: IgniteBiInClosure<String, PaymentInstrument>?, vararg args: Any?) {
        (args as Array<Any>).forEach { println("ARG ${it}") }
        super.loadCache(clo, *args)
    }
}