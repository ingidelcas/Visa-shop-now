package com.visa.userService.controller;

import com.visa.lib.entity.auth.UserAccount;
import com.visa.lib.exceptions.NotFoundException;
import com.visa.userService.header.HeaderGenerator;
import com.visa.userService.model.dto.UserAccountDto;
import com.visa.userService.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/manager")
public class UserManager {

    @Autowired
    private UserService userService;

    @Autowired
    private HeaderGenerator headerGenerator;

    private final ModelMapper modelMapper;

    public UserManager(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    @GetMapping("/user/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') and principal.id == #id")
    public ResponseEntity<?> getUserById(@PathVariable("id") Integer id) {
        Optional<UserAccount> user = Optional.ofNullable(userService.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with: " + id)));
        return (user.isPresent())
                ? new ResponseEntity<>(user.get(), headerGenerator.getHeadersForSuccessGetMethod(), HttpStatus.OK)
                : new ResponseEntity<>(null, headerGenerator.getHeadersForError(), HttpStatus.NOT_FOUND);
    }
}
