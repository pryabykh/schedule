package com.pryabykh.authserver.controllers;

import com.pryabykh.authserver.dtos.request.RefreshTokenDto;
import com.pryabykh.authserver.dtos.request.UserCredentialsDto;
import com.pryabykh.authserver.dtos.response.TokenAndRefreshTokenDto;
import com.pryabykh.authserver.exceptions.BadCredentialsException;
import com.pryabykh.authserver.exceptions.InvalidTokenException;
import com.pryabykh.authserver.exceptions.TokenDoesNotExistException;
import com.pryabykh.authserver.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    ResponseEntity<TokenAndRefreshTokenDto> login(@RequestBody UserCredentialsDto userCredentialsDto) {
        return ResponseEntity.ok(authService.login(userCredentialsDto));
    }

    @PostMapping("/refresh")
    ResponseEntity<TokenAndRefreshTokenDto> refresh(@RequestBody RefreshTokenDto refreshTokenDto) {
        return ResponseEntity.ok(authService.refresh(refreshTokenDto));
    }

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleConstraintViolationException() {
        return new ResponseEntity<>("Bad request", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({BadCredentialsException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handleBadCredentialsException() {
        return new ResponseEntity<>("Forbidden", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({TokenDoesNotExistException.class, InvalidTokenException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> handleInvalidToken() {
        return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({Exception.class, RuntimeException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleException(Throwable throwable) {
        throwable.printStackTrace();
        return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
