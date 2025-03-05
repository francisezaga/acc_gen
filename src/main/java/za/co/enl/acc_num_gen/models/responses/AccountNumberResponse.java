package za.co.enl.acc_num_gen.models.responses;

import jakarta.persistence.Column;

public class AccountNumberResponse {

    private String accountNumber;
    private String accountType;
    private String accountSubType;

    public AccountNumberResponse(){}

    public AccountNumberResponse(String accountNumber, String accountType, String accountSubType) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.accountSubType = accountSubType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountSubType() {
        return accountSubType;
    }

    public void setAccountSubType(String accountSubType) {
        this.accountSubType = accountSubType;
    }
}
