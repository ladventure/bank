package com.example.lili.bank.controller;

import com.lili.bank.BankApplication;
import com.lili.bank.client.ModifyTransactionDTO;
import com.lili.bank.client.TransactionDTO;
import com.lili.bank.client.TransactionQuery;
import com.lili.bank.controller.TransactionController;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Fork(value = 1, jvmArgs = {"-Xms2G", "-Xmx2G"})
@Warmup(iterations = 1)
@Measurement(iterations = 2)
public class SpringBootBenchmark {

    private ConfigurableApplicationContext context;

    @Setup
    public void setup() {

        context = SpringApplication.run(BankApplication.class);

//        prepare data，in real scenes ，There are many different cases.
        TransactionController controller = context.getBean(TransactionController.class);
        controller.createTransaction(new TransactionDTO("Benchmark-99999","123", BigDecimal.valueOf(100.0), LocalDateTime.now(),"WITHDRAWAL"));

    }

    @TearDown
    public void tearDown() {

        context.close();
    }

    @Benchmark
    public void testCreateTransactions() {

        TransactionController controller = context.getBean(TransactionController.class);
        controller.createTransaction(new TransactionDTO(null,"123", BigDecimal.valueOf(100.0), LocalDateTime.now(),"WITHDRAWAL"));
    }
//
//    @Benchmark
    public void testModifyTransactions() {

        TransactionController controller = context.getBean(TransactionController.class);
        controller.modifyTransaction(new ModifyTransactionDTO("Benchmark-99999","123", BigDecimal.valueOf(200.0)));
    }
//
//    @Benchmark
    public void testDeleteTransactions() {
        TransactionController controller = context.getBean(TransactionController.class);
        ResponseEntity<String> res= controller.createTransaction(new TransactionDTO(null,"123", BigDecimal.valueOf(100.0), LocalDateTime.now(),"WITHDRAWAL"));
        if(!Objects.requireNonNull(res.getBody()).isEmpty()){
            controller = context.getBean(TransactionController.class);
            controller.deleteTransaction(res.getBody());
        }

    }

//    @Benchmark
    public void testListAllTransactions() {

        TransactionController controller = context.getBean(TransactionController.class);
        controller.listTransactions(new TransactionQuery());
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(SpringBootBenchmark.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }
}