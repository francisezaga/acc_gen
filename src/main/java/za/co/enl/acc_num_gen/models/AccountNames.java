package za.co.enl.acc_num_gen.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AccountNames {
    mobi,
    ezi,
    biz,
    sevi,
    loan,
    shembe,
    doe,
    other;

    @JsonCreator
    public static AccountNames fromValue(String value) {
        for (AccountNames accNames : AccountNames.values()) {
            if (accNames.name().equalsIgnoreCase(value)) {
                return accNames;
            }
        }
        throw new IllegalArgumentException("Invalid acc name: " + value);
    }

    @JsonValue
    public String toValue() {
        return name();
    }
}
