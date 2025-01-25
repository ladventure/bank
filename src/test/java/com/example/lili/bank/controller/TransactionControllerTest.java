package com.example.lili.bank.controller;


import com.lili.bank.BankApplication;
import com.lili.bank.client.TransactionDTO;
import com.lili.bank.controller.TransactionController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.alibaba.fastjson.JSON;
import static org.hamcrest.Matchers.is;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@SpringBootTest(classes = BankApplication.class)
@AutoConfigureMockMvc
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private TransactionDTO getTransactionDTO() {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAccountId("123");
        transactionDTO.setAmount(BigDecimal.valueOf(100.0));
        transactionDTO.setType("WITHDRAWAL");
        transactionDTO.setTimestamp(LocalDateTime.now());
        return transactionDTO;
    };
    // test case for createTransaction, test normal case, black accountId, illegal type etc. Because time is limited, other scenes are omitted。
    @Test
    public void testCreateTransactionNormal() throws Exception {
        // test data
        TransactionDTO transactionDto = getTransactionDTO();


        mockMvc.perform(MockMvcRequestBuilders.post("/transactions/createTransaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(transactionDto)))
                        .andExpect(MockMvcResultMatchers.status().isCreated());
    }
    @Test
    public void testCreateTransactionBlackAccountId() throws Exception {
        // test data
        TransactionDTO transactionDto = getTransactionDTO();
        transactionDto.setAccountId("");


        mockMvc.perform(MockMvcRequestBuilders.post("/transactions/createTransaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(transactionDto)))
                .andExpect(MockMvcResultMatchers.status().is5xxServerError());
    }
    @Test
    public void testCreateTransactionIllegalType() throws Exception {
        // test data
        TransactionDTO transactionDto =getTransactionDTO();
        transactionDto.setType("xxx");


        mockMvc.perform(MockMvcRequestBuilders.post("/transactions/createTransaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(transactionDto)))
                .andExpect(MockMvcResultMatchers.status().is5xxServerError());
    }

//    other function tests just normal case, other scenes to do

    @Test
    public void testDeleteTransaction() throws Exception {
        TransactionDTO transactionDto =getTransactionDTO();
        transactionDto.setId("99999");
//        prepare test data
        mockMvc.perform(MockMvcRequestBuilders.post("/transactions/createTransaction")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(transactionDto)));

        mockMvc.perform(MockMvcRequestBuilders.delete("/transactions/deleteTransaction/{id}", transactionDto.getId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testModifyTransaction() throws Exception {
        TransactionDTO transactionDto =getTransactionDTO();
        transactionDto.setId("99999");
//        prepare test data
        mockMvc.perform(MockMvcRequestBuilders.post("/transactions/createTransaction")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON.toJSONString(transactionDto)));


        transactionDto.setAmount(BigDecimal.valueOf(200.0));
        mockMvc.perform(MockMvcRequestBuilders.put("/transactions/modifyTransaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(transactionDto)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testListTransactions() throws Exception {
        // 执行 GET 请求
        mockMvc.perform(MockMvcRequestBuilders.get("/transactions/listAllTransactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
    )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}