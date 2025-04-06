package org.fpoly.capstone.utils.general;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class GeneralStringCode {
    public static String generateCodeAdmin() {
        Random random = new Random();
        Integer radandomCode = 1000 + random.nextInt(9000);
        return "HD" + radandomCode;
    }
}
