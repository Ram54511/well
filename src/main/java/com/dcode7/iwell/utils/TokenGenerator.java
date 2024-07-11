package com.dcode7.iwell.utils;
import java.security.SecureRandom;
import java.util.Random;

public class TokenGenerator {

    /**
     * Generate a random token/OTP of a specific length.
     *
     * @param length the length of the token to generate.
     * @return a random token.
     */
    public static String generateToken(int length) {
        if (length < 1) throw new IllegalArgumentException("length must be a positive number");

        // Using SecureRandom for secure token generation
        Random random = new SecureRandom();
        StringBuilder token = new StringBuilder(length);

        // Define the characters that can be used in the token
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        for (int i = 0; i < length; i++) {
            token.append(chars.charAt(random.nextInt(chars.length())));
        }

        return token.toString();
    }
   
    /**
     * Generates a numeric OTP of specified length. OTP beings with non-zero digit.
     * @param length
     * @return a string representation of OTP
     */
    public static String generateNumericOtp(int length) {
        if (length < 1) throw new IllegalArgumentException("length must be a positive number");

        // Using SecureRandom for secure token generation
        Random random = new SecureRandom();
        StringBuilder otp = new StringBuilder(length);

        // Define the characters that can be used in the token
        String firstDigitPool = "123456789";
        String digitsPool = "0123456789";

		otp.append(firstDigitPool.charAt(random.nextInt(firstDigitPool.length())));
        
        for (int i = 1; i < length; i++) {
            otp.append(digitsPool.charAt(random.nextInt(digitsPool.length())));
        }

        return otp.toString();
    }
}
