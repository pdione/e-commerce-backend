package com.embarkx.firstspring;

import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {

    @GetMapping("/hello/{name}")
    public HelloResponse sayHello(@PathVariable String name) {
        return new HelloResponse("Hello World! " + name);
    }

    @PostMapping("/hello")
    public String helloPost(@RequestBody String name) {
        return "Hello " + name + "!";
    }
}
