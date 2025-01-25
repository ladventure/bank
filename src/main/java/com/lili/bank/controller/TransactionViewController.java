package com.lili.bank.controller;

import com.lili.bank.application.TransactionAppService;
import com.lili.bank.client.ModifyTransactionDTO;
import com.lili.bank.client.Page;
import com.lili.bank.client.TransactionDTO;
import com.lili.bank.client.TransactionQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Controller

public class TransactionViewController {

    @Autowired
    TransactionController transactionController;

    @GetMapping("/transactions")
    public String showTransactionsPage(Model model) {

        ResponseEntity<Page<TransactionDTO>> res=transactionController.listTransactions(new TransactionQuery());
        List<TransactionDTO> transactions= Objects.requireNonNull(res.getBody()).getResult();
        model.addAttribute("transactions", transactions);
        return "transactions"; // 这里的名称对应 transactions.html，Thymeleaf 会自动查找对应的模板文件
    }
}
