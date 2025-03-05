package za.co.enl.acc_num_gen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import za.co.enl.acc_num_gen.models.AccountNumber;
import za.co.enl.acc_num_gen.models.responses.AvailableAccNumResponses;

import java.util.List;

@Repository
public interface AccountNumberGenRepo extends JpaRepository<AccountNumber, Long> {

    @Query("SELECT Count(*) FROM AccountNumber a WHERE a.isAvailable=true")
    int getCountOfAllAvailableAccountNumbers();

    @Query("SELECT Count(*) FROM AccountNumber a WHERE a.isAvailable=true and a.accountType=:accountType")
    int getAvailableAccountNumbersByAccountType(@Param("accountType") String accountType);

    @Query(value= "select account_type as accountType, account_sub_type as accountSubType, Count(*) as availableAccountNumbers, 0 as minAccNumToBeReserved from enl_accounts_db.account_number where is_available=1 group by account_type, account_sub_type",
            nativeQuery = true)
    List<AvailableAccNumResponses> getAvailableAccountNumbers();
}