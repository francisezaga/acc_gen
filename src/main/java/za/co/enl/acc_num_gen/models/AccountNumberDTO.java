package za.co.enl.acc_num_gen.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AccountNumberDTO {

    private String accNumPrefix;

    private AccountTypes accountType;

    private AccountNames accountSubType;

}
