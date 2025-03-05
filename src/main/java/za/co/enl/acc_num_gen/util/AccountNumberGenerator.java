package za.co.enl.acc_num_gen.util;

import java.util.Random;

/**
 * Account number generator.
 * @author Brian Mutsikamahwe
 */
public class AccountNumberGenerator {

    private final String weightings;
    private final Integer modulus;

    private Random random = new Random(System.currentTimeMillis());

    public AccountNumberGenerator(String weightings, Integer modulus) {
        this.weightings = weightings;
        this.modulus = modulus;
    }

    /**
     * Generates a random valid account number
     *
     * @param prefix The prefix, a set digits at the start of the account number.
     * @return A randomly generated, valid, account number.
     */
    public String generate(String prefix) {
        int length = this.modulus;
        // The number of random digits that we need to generate is equal to the
        // total length of the card number minus the start digits given by the
        // user, minus the check digit at the end.
        int randomNumberLength = length - (prefix.length() + 1);
        StringBuilder builder = new StringBuilder(prefix);
        for (int i = 0; i < randomNumberLength; i++) {
            int digit = this.random.nextInt(10);
            builder.append(digit);
        }
        // Do the weighting algorithm to generate the check digit.
        int checkDigit = this.getCheckDigit(builder.toString());
        builder.append(checkDigit);

        return builder.toString();
    }

    /**
     * Generates the check digit required to make the given account number
     * valid
     *
     * @param number The account number for which to generate the check digit.
     * @return The check digit required to make the given account number
     * valid.
     */
    public int getCheckDigit(String number) {
        int sum = 0;
        for (int i = 0; i < number.length(); i++) {
            // Get the digit at the current position.
            int digit = Integer.parseInt(number.substring(i, (i + 1)));
            int weight = Integer.parseInt(weightings.substring(i, (i + 1)));
            digit = digit * weight;
            if (digit > 9) {
                digit = (digit / 10) + (digit % 10);
            }
            sum += digit;
        }
        int mod = sum % this.modulus;
        return ((mod == 0) ? 0 : this.modulus - mod);
    }
}