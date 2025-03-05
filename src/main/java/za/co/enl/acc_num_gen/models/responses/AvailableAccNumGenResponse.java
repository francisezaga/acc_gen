package za.co.enl.acc_num_gen.models.responses;

import java.util.List;

public class AvailableAccNumGenResponse {

    private String message;

    private List<AvailableAccountTypes> accTypes;

    public AvailableAccNumGenResponse(){}

    public AvailableAccNumGenResponse(String message, List<AvailableAccountTypes> accTypes) {
        this.message = message;
        this.accTypes = accTypes;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<AvailableAccountTypes> getAccTypes() {
        return accTypes;
    }

    public void setAccTypes(List<AvailableAccountTypes> accTypes) {
        this.accTypes = accTypes;
    }
}
