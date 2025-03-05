package za.co.enl.acc_num_gen.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.enl.acc_num_gen.models.APIResponse;
import za.co.enl.acc_num_gen.models.AccountNumberDTO;
import za.co.enl.acc_num_gen.services.AccountNumberGeneratorService;

import java.time.Instant;

@RestController
@RequestMapping(path = "/enl/account-num/generator")
public class AccountNumberGeneratorController {

    @Autowired
    private final AccountNumberGeneratorService accountNumberGeneratorService;

    public AccountNumberGeneratorController(AccountNumberGeneratorService accountNumberGeneratorService) {
       this.accountNumberGeneratorService = accountNumberGeneratorService;
    }


   @GetMapping(value = {"availableAccountNumbersCount"})
    public ResponseEntity<APIResponse> getCountOfAvailableNumbers()  {
        return accountNumberGeneratorService.getAvailableAccountNumbers();
    }

    @PostMapping(value = {"generateAccountNumbers"})
    public ResponseEntity<APIResponse> generateAccountNumbers()  {
        return accountNumberGeneratorService.generateAccountNumbers();
    }

    @PostMapping(value = {"generateAccountNumber"})
    public ResponseEntity<APIResponse> generateAccountNumber(@RequestBody AccountNumberDTO accountNumberDTO)  {
        return accountNumberGeneratorService.generateAccountNumber(accountNumberDTO);
    }

}
