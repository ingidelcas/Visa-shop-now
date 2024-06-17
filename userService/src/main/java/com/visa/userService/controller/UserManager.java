package com.visa.userService.controller;


import com.visa.lib.DTO.ApiErrorResponse;
import com.visa.lib.entity.Auth.UserAccount;
import com.visa.userService.header.HeaderGenerator;
import com.visa.userService.model.dto.ChangePasswordRequest;
import com.visa.userService.model.dto.LoginResponse;
import com.visa.userService.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/api/manager")
public class UserManager {

    @Autowired
    private UserService userService;

    @Autowired
    private HeaderGenerator headerGenerator;


    @GetMapping("/user/id/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') and principal.id == #id")
    public ResponseEntity<?> getUserById(@PathVariable("id") Integer id) {
        Optional<UserAccount> user = userService.findById(id);
        return (user.isPresent())
                ? new ResponseEntity<>(user.get(), headerGenerator.getHeadersForSuccessGetMethod(), HttpStatus.OK)
                : new ResponseEntity<>(null, headerGenerator.getHeadersForError(), HttpStatus.NOT_FOUND);
    }

    @Hidden
    @GetMapping("/user/name/{name}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') and principal.id == #id")
    public UserAccount getUserByName(@PathVariable("name") String name) {
        UserAccount user = userService.loadUserByUsername(name);
        return user;
    }



    @Operation(summary = "Change user password")
    @ApiResponse(responseCode = "200",description = "Password changed successfully", content = @Content(schema = @Schema(implementation = String.class)))
    @PutMapping("/change-password")
    @PreAuthorize("isAuthenticated() and hasAuthority('USER')")
    public String changePassword(@RequestBody ChangePasswordRequest request) {
        return userService.changePassword(request);
    }
}
