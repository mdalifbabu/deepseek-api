package com.example.deepseek.service;

import com.example.deepseek.config.DeepSeekConfig;
import com.example.deepseek.dto.DeepSeekRequest;
import com.example.deepseek.dto.DeepSeekResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class DeepSeekService {

    private final RestTemplate restTemplate;
    private final DeepSeekConfig config;

    public String getChatResponse(String prompt) {
        // Prepare headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        System.out.println(config.getApiKey());
        headers.set("Authorization", "Bearer " + config.getApiKey());

        // Prepare request body
        DeepSeekRequest request = new DeepSeekRequest();
        request.setModel(config.getModel());
        request.setMessages(Collections.singletonList(
                new DeepSeekRequest.Message("user", prompt)
        ));

        // Create HTTP entity
        HttpEntity<DeepSeekRequest> entity = new HttpEntity<>(request, headers);

        // Make the API call
        ResponseEntity<DeepSeekResponse> response = restTemplate.exchange(
                config.getApiUrl(),
                HttpMethod.POST,
                entity,
                DeepSeekResponse.class
        );

        // Process and return the response
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody().getChoices().get(0).getMessage().getContent();
        } else {
            throw new RuntimeException("Failed to get response from DeepSeek API");
        }
    }

    @Async
    public CompletableFuture<String> getChatResponseAsync(String prompt) {
        String response = getChatResponse(prompt);
        return CompletableFuture.completedFuture(response);
    }

    @Cacheable(value = "deepseekResponses", key = "#prompt")
    public String getChatResponseCaching(String prompt) {
        // existing implementation
        return "";
    }
}