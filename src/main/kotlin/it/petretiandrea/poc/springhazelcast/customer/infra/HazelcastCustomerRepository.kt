package it.petretiandrea.poc.springhazelcast.customer.infra

import com.hazelcast.core.HazelcastInstance
import com.hazelcast.nio.ObjectDataInput
import com.hazelcast.nio.ObjectDataOutput
import com.hazelcast.nio.serialization.StreamSerializer
import it.petretiandrea.poc.springhazelcast.customer.domain.Customer
import it.petretiandrea.poc.springhazelcast.customer.domain.CustomerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.withContext
import reactor.kotlin.core.publisher.toMono

class HazelcastCustomerRepository(
    hazelcast: HazelcastInstance
) : CustomerRepository {

    private val customers = hazelcast.getMap<String, Customer>("customers")

    override suspend fun findById(customerId: String): Customer? {
        return withContext(Dispatchers.IO) {
            customers.getOrDefault(customerId, null)
        }
    }

    override suspend fun save(customer: Customer) {
        withContext(Dispatchers.IO) {
            customers.putAsync(customer.id, customer)
                .toMono()
                .awaitSingle()
        }
    }

    override suspend fun optimisticSave(customer: Customer, old: Customer): Boolean {
        return withContext(Dispatchers.IO) {
            customers.replace(customer.id, old, customer)
        }
    }

    companion object {
        fun customSerializer() : StreamSerializer<Customer> {
            return object : StreamSerializer<Customer> {
                override fun getTypeId(): Int {
                    return 1
                }

                override fun read(`in`: ObjectDataInput): Customer {
                    return Customer(
                        `in`.readString()!!,
                        `in`.readString()!!,
                        `in`.readInt()
                    )
                }

                override fun write(out: ObjectDataOutput, obj: Customer) {
                    out.apply {
                        writeString(obj.id)
                        writeString(obj.name)
                        writeInt(obj.age)
                    }
                }
            }
        }
    }
}