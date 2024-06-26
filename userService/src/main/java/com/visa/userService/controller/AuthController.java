
package com.visa.userService.controller;

import com.visa.lib.DTO.ApiErrorResponse;
import com.visa.lib.entity.Auth.UserAccount;
import com.visa.userService.helper.JwtHelper;
import com.visa.userService.model.dto.LoginAttemptResponse;
import com.visa.userService.model.dto.LoginRequest;
import com.visa.userService.model.dto.LoginResponse;
import com.visa.userService.model.dto.SignupRequest;
import com.visa.userService.service.LoginService;
import com.visa.userService.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.security.auth.message.AuthException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    private final AuthenticationManager authenticationManager;

    @Autowired
    private UserService userServiceImpl;

    @Autowired
    private LoginService loginServiceImpl;


    public AuthController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;

    }

    @Operation(summary = "Signup user")
    @ApiResponse(responseCode = "201")
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequest requestDto) {
        UserAccount user = userServiceImpl.signup(requestDto);
        loginServiceImpl.addLoginAttempt(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Authenticate user and return token")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = LoginResponse.class)))
    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
            if (authentication.isAuthenticated()) {
                String token = JwtHelper.generateToken(authentication);
                return ResponseEntity.ok(new LoginResponse(request.username(), token));
            } else {
                throw new AuthException("unauthenticated");
            }
        } catch (BadCredentialsException e) {
            throw e;
        } catch (AuthException e) {
            throw new RuntimeException(e);
        }


    }

    @Operation(summary = "Logs out the authenticated user.")
    @ApiResponse(responseCode = "200", description = "Logged out successfully", content = @Content(schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        userServiceImpl.logout();
        return ResponseEntity.status(HttpStatus.OK).build();

    }


    @Operation(summary = "Get recent login attempts")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = LoginResponse.class)))
    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @GetMapping(value = "/loginAttempts")
    public ResponseEntity<LoginAttemptResponse> loginAttempts(@RequestHeader("Authorization") String token) {
        String email = JwtHelper.extractUsername(token.replace("Bearer ", ""));

        return ResponseEntity.ok(loginServiceImpl.findRecentLoginAttempts(email));
    }


}