package it.petretiandrea.poc.springhazelcast.paymentinstrument.infra.hazelcast

import com.hazelcast.map.MapStore
import com.hazelcast.spring.context.SpringAware
import it.petretiandrea.poc.springhazelcast.paymentinstrument.domain.PaymentInstrument
import it.petretiandrea.poc.springhazelcast.paymentinstrument.infra.mongo.PaymentInstrumentMongoRepository

@SpringAware
class PaymentInstrumentMapStore(
    private val paymentInstrumentMongoRepository: PaymentInstrumentMongoRepository
) : MapStore<String, PaymentInstrument> {

    override fun load(key: String): PaymentInstrument? {
        return paymentInstrumentMongoRepository
            .findById(key)
            .block()
    }

    override fun loadAll(keys: MutableCollection<String>): MutableMap<String, PaymentInstrument> {
        val map = mutableMapOf<String, PaymentInstrument>()
        for (id in keys) {
            load(id)?.let { map[id] = it }
        }

        return map;
    }

    override fun loadAllKeys(): MutableIterable<String> {
        return paymentInstrumentMongoRepository.findAll()
            .map { it.id }
            .toIterable()
    }

    override fun deleteAll(keys: MutableCollection<String>?) {
        keys?.forEach { paymentInstrumentMongoRepository.deleteById(it).block() }
    }

    override fun delete(key: String?) {
        key?.let { paymentInstrumentMongoRepository.deleteById(it) }
    }

    override fun storeAll(map: MutableMap<String, PaymentInstrument>) {
        val stored = paymentInstrumentMongoRepository.saveAll(map.values)
            .count()
            .block()
        println("Store $stored entries")
    }

    override fun store(key: String?, value: PaymentInstrument?) {
        paymentInstrumentMongoRepository.save(value!!).block()
    }
}