package it.petretiandrea.poc.springhazelcast.customer.domain

interface CustomerRepository {
    suspend fun findById(customerId: String): Customer?
    suspend fun save(customer: Customer)
    suspend fun optimisticSave(customer: Customer, old: Customer): Boolean
}