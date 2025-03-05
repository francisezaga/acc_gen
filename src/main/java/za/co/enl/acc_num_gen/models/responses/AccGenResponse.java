package za.co.enl.acc_num_gen.models.responses;

import java.util.List;

public class AccGenResponse {

    private String message;
    private List<AccountTypeResponse> accountTypeResponseList;

    public AccGenResponse(String message, List<AccountTypeResponse> accountTypeResponseList) {
        this.message = message;
        this.accountTypeResponseList = accountTypeResponseList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<AccountTypeResponse> getAccountTypeResponseList() {
        return accountTypeResponseList;
    }

    public void setAccountTypeResponseList(List<AccountTypeResponse> accountTypeResponseList) {
        this.accountTypeResponseList = accountTypeResponseList;
    }
}
