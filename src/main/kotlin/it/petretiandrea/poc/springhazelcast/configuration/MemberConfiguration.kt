package it.petretiandrea.poc.springhazelcast.configuration

import com.hazelcast.config.MapStoreConfig
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration

@Configuration
class MemberConfiguration {

    fun mapStoreConfig() : MapStoreConfig {

    }
}