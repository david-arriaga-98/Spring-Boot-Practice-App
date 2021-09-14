package com.isac.ecommerce.services;

import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UtilService {

    public long verifyLong(String id) throws NumberFormatException {
        return Long.parseLong(id);
    }

    public boolean verifyIfIsAnImage(@NonNull String filename) {
        String regex = "^[a-zA-Z0-9._ -]+\\.(png|jpe?g|gif|bmp|webp)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(filename);
        return m.matches();
    }

    public String getRandomHexString(int chars) {
        Random random = new Random();
        StringBuilder stringBuffer = new StringBuilder();
        while (stringBuffer.length() < chars) {
            stringBuffer.append(Integer.toHexString(random.nextInt()));
        }
        return stringBuffer.substring(0, chars);
    }

}
