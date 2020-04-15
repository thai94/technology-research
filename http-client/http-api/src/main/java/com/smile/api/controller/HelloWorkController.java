package com.smile.api.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class HelloWorkController {

    @PostMapping("/hello-post")
    public String helloWorldPost(@RequestBody HelloEntity helloEntity) {
        String message = String.format("Hello: %s", helloEntity.name);
        return message;
    }

    @GetMapping("/hello-get")
    public String helloWorldGet(@RequestParam String name) throws InterruptedException {
        String message = String.format("Hello: %s", name);
        return message;
    }
}
