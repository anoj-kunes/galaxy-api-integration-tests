package com.leisurepassgroup.galaxyapimock.integration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.mockserver.integration.ClientAndServer;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;

public abstract class BaseTests {
    static ClientAndServer mockServer;

    @BeforeAll
    public static void setupServer() {
        mockServer = startClientAndServer(9088);
    }

    @AfterAll
    public static void tearDownServer() {
        mockServer.stop();
    }

    public RestTemplate getRestTemplate() {
        var restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return restTemplate;
    }
}
