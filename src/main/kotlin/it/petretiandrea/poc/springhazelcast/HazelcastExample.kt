package it.petretiandrea.poc.springhazelcast

import com.hazelcast.client.HazelcastClient
import com.hazelcast.client.config.ClientConfig
import com.hazelcast.config.SerializerConfig
import it.petretiandrea.poc.springhazelcast.customer.domain.Customer
import it.petretiandrea.poc.springhazelcast.customer.infra.HazelcastCustomerRepository
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) {
    //runApplication<SpringHazelcastApplication>(*args)
    val config = ClientConfig().apply {
        networkConfig.addAddress("localhost:5701")
        instanceName = "example"
        serializationConfig.addSerializerConfig(
            SerializerConfig()
                .setImplementation(HazelcastCustomerRepository.customSerializer())
                .setTypeClass(Customer::class.java)
        )
    }
    val hazelcast = HazelcastClient.getOrCreateHazelcastClient(config)
    println(hazelcast.name)
    val repository = HazelcastCustomerRepository(hazelcast)

    // example of lost update issue
    runBlocking {
        repository.save(Customer("1", "ciao", 20))
        val a = repository.findById("1")
        val b = repository.findById("1")
        a?.incrementAge()
        b?.decrementAge()
        repository.save(a!!)
        repository.save(b!!)
        // expected age must be 22
        repository.findById("1")?.let {
            println("Saved age ${it.age}. Should be 20, but 19 due to lost update of second read")
        }
    }

    // example with version optimistic lockin
    runBlocking {
        repository.save(Customer("1", "ciao", 20))
        val a = repository.findById("1")
        val oldA = a?.copy()
        val b = repository.findById("1")
        val oldB = b?.copy()
        a?.incrementAge()
        b?.incrementAge()
        repository.optimisticSave(a!!, oldA!!).let {
            println("Update A: $it")
        }
        repository.optimisticSave(b!!, oldB!!).let {
            println("Update B: ${it}. Should be false due optimistic lock")
        }
    }
}
