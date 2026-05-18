package com.wemeet.flowfixassistant.common.infrastructure.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {

    @Value("\${rag-engine.base-url:http://localhost:8003}")
    private lateinit var ragEngineBaseUrl: String

    @Bean
    fun ragEngineWebClient(): WebClient {
        return WebClient.builder()
            .baseUrl(ragEngineBaseUrl)
            .build()
    }
}
