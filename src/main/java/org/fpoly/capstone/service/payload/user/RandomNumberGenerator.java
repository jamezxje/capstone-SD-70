package org.fpoly.capstone.service.payload.user;

import org.apache.commons.lang3.RandomStringUtils;
import java.util.Random;

public class RandomNumberGenerator {
    public  int generateRandom6DigitNumber() {

        Random random = new Random();
        int minRange = 100000;
        int maxRange = 999999;
        return random.nextInt(maxRange - minRange + 1) + minRange;
    }
    public  String randomPassword() {
        String password =  RandomStringUtils.random(9,true,true);

        return password;
    }
}
