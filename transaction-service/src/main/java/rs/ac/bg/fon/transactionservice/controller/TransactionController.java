package rs.ac.bg.fon.transactionservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.bg.fon.transactionservice.dto.request.CreateTransactionRequest;
import rs.ac.bg.fon.transactionservice.dto.response.CreateTransactionResponse;
import rs.ac.bg.fon.transactionservice.service.TransactionService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<CreateTransactionResponse> executeTransaction(@RequestBody CreateTransactionRequest request){
        CreateTransactionResponse response = transactionService.executeTransaction(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
