package com.abuse;

import com.abuse.module.FraudRepository;
import com.abuse.rule.Rulable;
import com.abuse.types.Charge;
import com.abuse.types.Create;
import com.abuse.types.Receive;
import com.abuse.types.Send;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import static com.abuse.rule.Utils.parsing;

@RestController
@RequestMapping("/v1")
public class FraudController {
    private final FraudRepository repository;

    @Autowired
    public FraudController(FraudRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = { "{userId}/{account}/status" })
    public Map<Rulable, Queue<LocalDateTime>> status(@PathVariable Long userId, @PathVariable Long account) {
        return repository.status(userId, account);
    }

    @PutMapping(value = { "{userId}/create/{account}" })
    public void create(@PathVariable Long userId, @PathVariable Long account
            , @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam(name = "date") LocalDateTime date) {
        repository.add(userId, account, date, parsing(Create.ACCOUNT, account));
    }

    @PutMapping(value = { "{userId}/charge/{account}" })
    public void charge(@PathVariable Long userId, @PathVariable Long account
            , @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam(name = "date") LocalDateTime date
            , @RequestParam(name = "amount") Long amount
            , @RequestParam(name = "bankAccount") Long bankAccount) {
        repository.add(userId, account, date
                , parsing(Charge.AMOUNT, amount
                , Charge.BANK_ACCOUNT, bankAccount));
    }

    @PutMapping(value = { "{userId}/{account}/receive" })
    public void receive(@PathVariable Long userId, @PathVariable Long account
            , @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)  @RequestParam(name = "date") LocalDateTime date
            , @RequestParam(name = "beforeBalance") Long beforeBalance
            , @RequestParam(name = "fromAccount") Long fromAccount
            , @RequestParam(name = "fromId") Long fromId
            , @RequestParam(name = "amount") Long amount) {
        repository.add(userId, account, date
                , parsing(Receive.BEFORE_BALANCE, beforeBalance
                , Receive.FROM_ACCOUNT, fromAccount
                , Receive.FROM_ID, fromId
                , Receive.AMOUNT, amount));
    }

    @PutMapping(value = { "{userId}/{account}/send" })
    public void send(@PathVariable Long userId, @PathVariable Long account
            , @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam(name = "date") LocalDateTime date
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
