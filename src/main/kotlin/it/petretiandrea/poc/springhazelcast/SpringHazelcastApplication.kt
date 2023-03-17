package it.petretiandrea.poc.springhazelcast

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class SpringHazelcastApplication

fun main(args: Array<String>) {
    runApplication<SpringHazelcastApplication>(*args)
}
