package com.lili.bank.domain.transaction;

import com.lili.bank.BankProperties;
import com.lili.bank.client.Page;
import com.lili.bank.client.TransactionQuery;
import com.lili.bank.common.exception.BankException;
import com.lili.bank.common.exception.BankExceptionEnum;
import com.lili.bank.common.utils.LockUtils;
import com.lili.bank.common.utils.ObjectUtils;
import com.lili.bank.infrastructure.TransactionDAO;
import com.lili.bank.infrastructure.TransactionDO;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * domain layer,
 * Primary business logic
 */
@Service
public class TransactionManager {

    @Autowired
    TransactionDAO transactionDao;

    @Autowired
    BankProperties bankProperties;

    public String createTransaction(TransactionEntity transaction) throws BankException {
//        support id generate
        if(Strings.isBlank(transaction.getId())){
            //       need use id generate algorithm
            String id = UUID.randomUUID().toString();
            transaction.setId(id);
        }
        transactionDao.findById(transaction.getId()).ifPresent(objDo->{
            throw new BankException(BankExceptionEnum.DUPLICATE_ERROR);
        });
//       Although use memory save, no concurrent problem. But for simulating real scenes，use lock to avoid concurrent problem, in real scenes use redis lock and db repeat table
        boolean lockRes=LockUtils.lock(transaction.getAccountId(),bankProperties.getLockTimeout());
//        handle lock false
        if (!lockRes){
            throw new BankException(BankExceptionEnum.CONCURRENT_ERROR);
        }
        try {
            transactionDao.save(ObjectUtils.convertObject(transaction,TransactionDO.class));
            //        clear cache
            bankProperties.getAccountIdToIdsCache().remove(transaction.getAccountId());

            return transaction.getId();

        }finally {
            LockUtils.unlock(transaction.getAccountId());
        }

    }


    public void deleteTransaction(String id) throws BankException {
        Optional<TransactionDO> optional= transactionDao.findById(id);
        if(optional.isEmpty()){
            throw new BankException(BankExceptionEnum.NOT_FOUND_ERROR);
        }
        try {
            boolean lockRes=LockUtils.lock(optional.get().getAccountId(),bankProperties.getLockTimeout());
//         handle lock false
            if (!lockRes){
                throw new BankException(BankExceptionEnum.CONCURRENT_ERROR);
            }
            transactionDao.deleteById(id);

            //        clear cache
            bankProperties.getAccountIdToIdsCache().remove(optional.get().getAccountId());

        }finally {
            LockUtils.unlock(optional.get().getAccountId());
        }

    }


    public void modifyTransaction(TransactionEntity transaction) {
        Optional<TransactionDO> optional= transactionDao.findById(transaction.getId());
        if(optional.isEmpty() || !optional.get().getAccountId().equals(transaction.getAccountId())){
            throw new BankException(BankExceptionEnum.NOT_FOUND_ERROR);
        }
        try {
            boolean lockRes=LockUtils.lock(optional.get().getAccountId(),bankProperties.getLockTimeout());
//         handle lock false
            if (!lockRes){
                throw new BankException(BankExceptionEnum.CONCURRENT_ERROR);
            }
            optional.get().setAmount(transaction.getAmount());
            transactionDao.update(optional.get());

            //        clear cache
            bankProperties.getAccountIdToIdsCache().remove(transaction.getAccountId());
        }finally {
            LockUtils.unlock(optional.get().getAccountId());
        }


    }


    public Page<TransactionEntity> listTransactions(TransactionQuery query) {
        TransactionDO transactionDo=new TransactionDO();
        transactionDo.setAccountId(query.getAccountId());
        transactionDo.setAmount(query.getAmount());
        transactionDo.setType(query.getType());
        transactionDo.setId(query.getId());

        if (query.getId() != null) {
            Optional<TransactionDO> optional= transactionDao.findById(query.getId());
            return new Page<>(optional.isEmpty() ? 0:1, optional.map(transactionDO -> List.of(ObjectUtils.convertObject(transactionDO, TransactionEntity.class))).orElseGet(List::of));
        }
//      for test just this case use cache
        if (query.getAccountId() != null && bankProperties.getAccountIdToIdsCache().get(query.getAccountId())!=null) {
            List<TransactionDO> dos=new ArrayList<>();
            bankProperties.getAccountIdToIdsCache().get(query.getAccountId()).forEach(id ->transactionDao.findById(id).ifPresent(dos::add));
            dos.sort(Comparator.comparing(TransactionDO::getId));
            return new Page<>(dos.size(),dos.stream().map(objDo->ObjectUtils.convertObject(objDo,TransactionEntity.class)).toList());
        }
        Page<TransactionDO> pageDos= transactionDao.find(transactionDo,query.getPageNumber(),query.getPageSize());


        // update cache
        pageDos.getResult().forEach(objDo-> {
            Set<String> ids = bankProperties.getAccountIdToIdsCache().get(objDo.getAccountId());
            if (ids == null) {
                ids = new HashSet<>();
            }
            ids.add(objDo.getId());
            bankProperties.getAccountIdToIdsCache().put(objDo.getAccountId(), ids);
        });
        return new Page<>(pageDos.getTotalCount(),pageDos.getResult().stream().map(objDo->ObjectUtils.convertObject(objDo,TransactionEntity.class)).toList());
    }
}
