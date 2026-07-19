package rs.ac.bg.fon.accountservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.accountservice.dto.request.CreateAccountRequest;
import rs.ac.bg.fon.accountservice.dto.request.UpdateBalanceRequest;
import rs.ac.bg.fon.accountservice.dto.response.AccountResponse;
import rs.ac.bg.fon.accountservice.service.AccountService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponse> create(@RequestBody @Validated CreateAccountRequest request){
        AccountResponse response = accountService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping
    public ResponseEntity<Boolean> updateBalance(@RequestBody @Validated UpdateBalanceRequest request){
        accountService.updateBalance(request);
        return ResponseEntity.noContent().build();
    }
}
