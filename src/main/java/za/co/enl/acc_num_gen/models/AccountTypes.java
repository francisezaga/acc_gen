package za.co.enl.acc_num_gen.models;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AccountTypes {
    savings,
    current,
    loan,
    partners,
    other;


    @JsonCreator
    public static AccountTypes fromValue(String value) {
        for (AccountTypes accTypes : AccountTypes.values()) {
            if (accTypes.name().equalsIgnoreCase(value)) {
                return accTypes;
            }
        }
        throw new IllegalArgumentException("Invalid acc type: " + value);
    }

    @JsonValue
    public String toValue() {
        return name();
    }
}

