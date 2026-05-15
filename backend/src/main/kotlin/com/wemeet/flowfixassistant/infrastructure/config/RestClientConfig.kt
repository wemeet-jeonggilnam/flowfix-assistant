package com.wemeet.flowfixassistant.infrastructure.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient

@Configuration
class RestClientConfig {

    @Value("\${rag-engine.base-url:http://localhost:8003}")
    private lateinit var ragEngineBaseUrl: String

    @Bean
    fun ragEngineRestClient(): RestClient {
        return RestClient.builder()
            .baseUrl(ragEngineBaseUrl)
            .build()
    }
}
