package com.lili.bank;

import com.lili.bank.common.utils.LRUCache;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Set;

/**
 * bank system config properties。
 * In real scene，maybe use configuration center. For test purpose，use default value or application.properties
 * @date 2023/12/11 15:49
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "bank")
public class BankProperties {
    // to limit the max, avoid out of memory
    private int maxRecords=10000;

    private long lockTimeout=0;

    private int cacheCapacity=100;

    /**
     * to cache record accountId and ids, accelerate find records by accountId。
     * can use spring cache。 But I choose develop a simple cache for test。
     *
     */
    private LRUCache<String, Set<String>> accountIdToIdsCache;

    @PostConstruct
    public void afterPropertiesSet() {
        accountIdToIdsCache=new LRUCache<>(cacheCapacity);
    }
}
