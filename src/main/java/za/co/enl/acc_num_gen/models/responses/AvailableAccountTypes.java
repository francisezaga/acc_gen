package za.co.enl.acc_num_gen.models.responses;

import java.util.List;

public class AvailableAccountTypes {

    private String accType;
    private List<AvailableAccNumResponses> accTypes;

    public AvailableAccountTypes(){}

    public AvailableAccountTypes(String accType, List<AvailableAccNumResponses> accTypes) {
        this.accType = accType;
        this.accTypes = accTypes;
    }

    public String getAccType() {
        return accType;
    }

    public void setAccType(String accType) {
        this.accType = accType;
    }

    public List<AvailableAccNumResponses> getAccTypes() {
        return accTypes;
    }

    public void setAccTypes(List<AvailableAccNumResponses> accTypes) {
        this.accTypes = accTypes;
    }
}
