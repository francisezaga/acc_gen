package za.co.enl.acc_num_gen.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AccountTypeDetails {

    private String accType;
    private String accSubType;
    private String accPrefix;
    private int accTobeReserved;

 }
