package com.example.pantryparserbackend.Services;

import com.example.pantryparserbackend.Passwords.OTP;
import com.example.pantryparserbackend.Passwords.OTPRepository;
import com.example.pantryparserbackend.Users.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.List;

@Service
@Api(value = "Password Service", description = "Password and OTP handling and management")
public class PasswordService {

    private static final Logger logger = LoggerFactory.getLogger(PasswordService.class);
    @Autowired
    private OTPRepository otpRepo;

    /**
     * generates an OTP of a specified length
     * @param length length of OTP
     * @return new OTP
     */
    @ApiOperation(value = "Generates an OTP of the provided length")
    public String generateOTP(int length, User user) {
        logger.info("generating an OTP");
        SecureRandom random = new SecureRandom();
        String password = "";
        for(int i = 0; i < length; i++){
            //generates a random int that's less than 10
            password += random.nextInt(10) + "";
        }

        OTP otp = new OTP(password.trim(), user);

        try {
            otpRepo.save(otp);
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }

        return password;
    }

    /**
     * This function "uses" an OTP - verifies it and then removes it from the database
     * @param password input OTP
     * @param user user attempting to use OTP
     * @return good OTP or not
     */
    @ApiOperation(value = "Operation that handles the comparing and deleting of OTPs - \"using\" OTPs")
    public boolean useOTP(String password, User user) {
        logger.info("User " + user.getId() + " is attempting to use an OTP");
        List<OTP> otps = otpRepo.findByUser(user);

        for (int i = 0; i < otps.size(); i++) {
            OTP otp = otps.get(i);
            if (otp.outOfDate()) {
                logger.info("User " + user.getId() + " had an out of date OTP");
                otpRepo.delete(otp);
            } else if (otp.verify(password)){
                logger.info("User " + user.getId() + " sent a good OTP");
                otpRepo.delete(otp);
                return true;
            }
        }
        return false;
    }

    /**
     * Compares a hash with the hash of a password
     * @param password input password
     * @param hash input hash
     * @return equal or not
     */
    @ApiOperation(value = "An operation that compares a password to a provided hash")
    public static boolean comparePasswords(String password, String hash) {
        String[] parts = hash.split(":");
        byte[] salt = PasswordService.fromHex(parts[1]);
        String passwordToTest = PasswordService.hash(password, salt);

        return hash.equals(passwordToTest);
    }

    /**
     * specifically for creating / changing a password, this method generates a new salt and calls the hash function
     * @param password input string for the new password
     * @return hashed value for the new password
     */
    @ApiOperation(value = "Generates a new hash from an inputted password")
    public static String newHash(String password) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return PasswordService.hash(password, salt);
    }

    /**
     * hashes the inputted password with the provided salt
     * @param password input password string
     * @param salt input random salt
     * @return hexed value of hashed password
     */
    private static String hash(String password, byte[] salt) {
        int iterations = 65536;
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, 512);
        try{
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return iterations + ":" + PasswordService.toHex(salt) + ":" + PasswordService.toHex(factory.generateSecret(spec).getEncoded());
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
