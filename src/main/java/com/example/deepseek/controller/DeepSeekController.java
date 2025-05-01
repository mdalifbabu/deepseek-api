package com.example.deepseek.controller;

import com.example.deepseek.service.DeepSeekService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/deepseek")
public class DeepSeekController {

    private final DeepSeekService deepSeekService;

    public DeepSeekController(DeepSeekService deepSeekService) {
        this.deepSeekService = deepSeekService;
    }

    @PostMapping("/chat")
    public String chat(@RequestBody String prompt) {
        System.out.println(prompt);
        return deepSeekService.getChatResponse(prompt);
    }

    @GetMapping("/test")
    public String test() {
        return "DeepSeek API integration is working!";
    }
}