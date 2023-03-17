package it.petretiandrea.poc.springhazelcast.customer.domain

data class Customer(
    val id: String,
    val name: String,
    var age: Int
) {
    fun incrementAge() {
        age += 1
    }

    fun decrementAge() {
        age -= 1
    }
}