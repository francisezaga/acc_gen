package za.co.enl.acc_num_gen.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name="acc_numbers_reserve",
        uniqueConstraints = {
                @jakarta.persistence.UniqueConstraint(columnNames = {"accountNumber"})
        }
)

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AccountNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String accountNumber;
    @Column(nullable = false)
    private String accountType;
	@Column(nullable = false)
	private String accountSubType;
    @Column(nullable = false)
    private boolean isAvailable;


}
