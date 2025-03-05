package za.co.enl.acc_num_gen.models.responses;

public class AccountTypeResponse {

    private String accType;
    private String message;

    public AccountTypeResponse(String accType, String message) {
        this.accType = accType;
        this.message = message;
    }

    public String getAccType() {
        return accType;
    }

    public void setAccType(String accType) {
        this.accType = accType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
