package com.example.pantryparserbackend.Util;

import com.example.pantryparserbackend.Passwords.OTP;
import com.example.pantryparserbackend.Passwords.OTPRepository;
import com.example.pantryparserbackend.users.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class PasswordUtil {

    private static OTPRepository otpRepository;

    @Autowired
    public void setOtpRepository(OTPRepository repo) {
        otpRepository = repo;
    }

    /**
     * generates an OTP of a specified length
     * @param length length of OTP
     * @return new OTP
     */
    public static String generateOTP(int length, User user) {
        SecureRandom random = new SecureRandom();
        String password = "";
        for(int i = 0; i < length; i++){
            //generates a random int that's less than 10
            password += random.nextInt(10);
        }
        OTP otp = new OTP(password, user);

        try {
            otpRepository.save(otp);
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }

        return password;
    }
    /**
     * Compares a hash with the hash of a password
     * @param password input password
     * @param hash input hash
     * @return
     */
    public static boolean comparePasswords(String password, String hash) {
        String[] parts = hash.split(":");
        byte[] salt = PasswordUtil.fromHex(parts[1]);

        String passwordToTest = PasswordUtil.hash(password, salt);
        return hash.equals(passwordToTest);
    }

    /**
     * specifically for creating / changing a password, this method generates a new salt and calls the hash function
     * @param password input string for the new password
     * @return hashed value for the new password
     */
    public static String newHash(String password) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return PasswordUtil.hash(password, salt);
    }

    /**
     * hashes the inputted password with the provided salt
     * @param password input password string
     * @param salt input random salt
     * @return hexed value of hashed password
     */
    public static String hash(String password, byte[] salt) {
        int iterations = 65536;
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, 512);
        try{
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return iterations + ":" + PasswordUtil.toHex(salt) + ":" + PasswordUtil.toHex(factory.generateSecret(spec).getEncoded());
        }catch(NoSuchAlgorithmException | InvalidKeySpecException e){
            //probably will never happen since these are permanently set, so shouldn't have to handle this
            return "encryption error";
        }
    }

    /**
     * converts a byte array to a hexidecimal string
     * @param array array of bytes
     * @return hexidecimal string
     */
    private static String toHex(byte[] array){
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        }else {
            return hex;
        }
    }

    /**
     * converts a hexidecimal string to a byte array
     * @param hex hexidecimal string
     * @return byte array
     */
    private static byte[] fromHex(String hex){
        byte[] bytes = new byte[hex.length()/2];
        for(int i = 0; i < bytes.length; i++){
            bytes[i] = (byte)Integer.parseInt(hex.substring(2*i, 2*i+2), 16);
        }
        return bytes;
    }
}
