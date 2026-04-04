package com.embarkx.firstspring;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public HelloResponse sayHello() {
        return new HelloResponse("Hello World!");
    }

    @PostMapping("/hello")
    public String helloPost(@RequestBody String name){
        return "Hello " + name + "!";
    }
}
