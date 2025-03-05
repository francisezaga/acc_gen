package za.co.enl.acc_num_gen.models.responses;

public class AvailableAccNumResponses {

    private String accountType;
    private String accountSubType;
    private Long availableAccountNumbers;
    private Long minAccNumToBeReserved;

    public AvailableAccNumResponses(){}

    public AvailableAccNumResponses(String accountType,String accountSubType, Long availableAccountNumbers, Long minAccNumToBeReserved) {
        this.accountType = accountType;
        this.accountSubType = accountSubType;
        this.availableAccountNumbers = availableAccountNumbers;
        this.minAccNumToBeReserved = minAccNumToBeReserved;
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

    public Long getAvailableAccountNumbers() {
        return availableAccountNumbers;
    }

    public void setAvailableAccountNumbers(Long availableAccountNumbers) {
        this.availableAccountNumbers = availableAccountNumbers;
    }

    public Long getMinAccNumToBeReserved() {
        return minAccNumToBeReserved;
    }

    public void setMinAccNumToBeReserved(Long minAccNumToBeReserved) {
        this.minAccNumToBeReserved = minAccNumToBeReserved;
    }
}
