package ru.vtb.msa.detr.tavrida.core.util;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import org.apache.hc.client5.http.utils.Hex;

@Component
public class TavridaUtils {

    private final Random random = new Random();

    public String generateCode() {
        // Случайная заглавная буква от 'A' до 'Z'
        char letter = (char) ('A' + random.nextInt(26));
        // Случайное 4-значное число от 0000 до 9999
        int number = random.nextInt(10000);
        // Форматируем число с ведущими нулями
        return String.format("%c%04d", letter, number);
    }


    public Instant parseInstant(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        return OffsetDateTime.parse(dateString, formatter).toInstant();
    }

    public String getHash(String someString){
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] someStringBytes = someString.getBytes();
        String hexString = Hex.encodeHexString(messageDigest.digest(someStringBytes));
        return hexString.toUpperCase();
    }

}
