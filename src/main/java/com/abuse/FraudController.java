package com.abuse;

import com.abuse.module.FraudRepository;
import com.abuse.types.Charge;
import com.abuse.types.Create;
import com.abuse.types.Receive;
import com.abuse.types.Send;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.abuse.rule.Utils.parsing;

@RestController
public class FraudController {
    private final FraudRepository repository;

    @Autowired
    public FraudController(FraudRepository repository) {
        this.repository = repository;
    }

    @PutMapping(value = { "create/{userId}/{account}" })
    public void create(@PathVariable Long userId, @PathVariable Long account, @RequestParam(name = "date") LocalDateTime date) {
        repository.add(userId, account, date, parsing(Create.ACCOUNT, account));
    }

    @PutMapping(value = { "charge/{userId}/{account}" })
    public void charge(@PathVariable Long userId, @PathVariable Long account
            , @RequestParam(name = "date") LocalDateTime date
            , @RequestParam(name = "amount") Long amount
            , @RequestParam(name = "bankAccount") Long bankAccount) {
        repository.add(userId, account, date
                , parsing(Charge.AMOUNT, amount
                , Charge.BANK_ACCOUNT, bankAccount));
    }

    @PutMapping(value = { "receive/{userId}/{account}" })
    public void receive(@PathVariable Long userId, @PathVariable Long account
            , @RequestParam(name = "date") LocalDateTime date
            , @RequestParam(name = "beforeBalance") Long beforeBalance
            , @RequestParam(name = "fromAccount") Long fromAccount
            , @RequestParam(name = "toId") Long toId
            , @RequestParam(name = "amount") Long amount) {
        repository.add(userId, account, date
                , parsing(Receive.BEFORE_BALANCE, beforeBalance
                , Receive.FROM_ACCOUNT, fromAccount
                , Receive.TO_ID, toId
                , Receive.AMOUNT, amount));
    }

    @PutMapping(value = { "send/{userId}/{account}" })
    public void send(@PathVariable Long userId, @PathVariable Long account
            , @RequestParam(name = "date") LocalDateTime date
            , @RequestParam(name = "fromBalance") Long fromBalance
            , @RequestParam(name = "toAccount") Long toAccount
            , @RequestParam(name = "toId") Long toId
            , @RequestParam(name = "amount") Long amount) {
        repository.add(userId, account, date
                , parsing(Send.FROM_BALANCE, fromBalance
                , Send.TO_ACCOUNT, toAccount
                , Send.TO_ID, toId
                , Send.AMOUNT, amount));
    }

    @RequestMapping(value = { "fraud/{userId}" }, method = RequestMethod.GET)
    public Fraud fraud(@PathVariable Long userId) {
        List<String> found = repository.findById(userId);
        return new Fraud(userId, found.size() > 0, found);
    }

}
