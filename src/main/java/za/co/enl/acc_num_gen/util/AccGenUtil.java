package za.co.enl.acc_num_gen.util;

import za.co.enl.acc_num_gen.models.AccountNames;
import za.co.enl.acc_num_gen.models.AccountTypes;

public class AccGenUtil {

    public static AccountTypes getAccountTypeFromAccName(String accName) {
        try {
            return AccountTypes.valueOf(accName);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static AccountNames getAccountNameFromAccName(String accName) {
        try {
            return AccountNames.valueOf(accName);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
