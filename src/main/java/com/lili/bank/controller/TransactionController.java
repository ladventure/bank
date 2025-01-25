package com.lili.bank.controller;

import com.lili.bank.application.TransactionAppService;
import com.lili.bank.client.ModifyTransactionDTO;
import com.lili.bank.client.Page;
import com.lili.bank.client.TransactionDTO;
import com.lili.bank.client.TransactionQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/transactions")
@Tag(name = "账号操作记录管理 API", description = "主要记录账号的资金操作情况")
public class TransactionController {
    @Autowired
    TransactionAppService transactionAppService;

    @PostMapping("createTransaction")
    @Operation(summary = "创建一天资金操作记录", description = "返回一个操作记录ID")
    public ResponseEntity<String> createTransaction(@RequestBody @NotNull @Valid TransactionDTO transactionDto) {
        String id = transactionAppService.createTransaction(transactionDto);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    /**
     * in real scene, ResponseEntity more fields
     * @param id
     * @return
     */
    @Operation(summary = "删除一天资金操作记录", description = "返回操作是否成功")
    @DeleteMapping("deleteTransaction/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable String id) {
        transactionAppService.deleteTransaction(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "修改一条资金操作记录", description = "返回操作是否成功")
    @PutMapping("modifyTransaction")
    public ResponseEntity<Void> modifyTransaction(@RequestBody @NotNull @Valid ModifyTransactionDTO transactionDto) {
        transactionAppService.modifyTransaction(transactionDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "删除一条资金操作记录", description = "返回操作是否成功")
    @GetMapping("listAllTransactions")
    public ResponseEntity<Page<TransactionDTO>> listTransactions(@RequestBody @Valid TransactionQuery transactionQuery) {
        Page<TransactionDTO> transactionDtos = transactionAppService.listTransactions(transactionQuery);
        return new ResponseEntity<>(transactionDtos, HttpStatus.OK);
    }
}
