package com.example.resource.web;

import com.example.resource.web.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class ItemResource {

    @GetMapping
    public ResponseEntity<String> get() {
        return ResponseEntity.ok("ok");
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody @Valid UserDto newUser) {
        return ResponseEntity.ok(newUser);
    }
}
