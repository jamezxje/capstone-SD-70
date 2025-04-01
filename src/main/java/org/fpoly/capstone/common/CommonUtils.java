package org.fpoly.capstone.common;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Random;

@Log4j2
public class CommonUtils {

    private CommonUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 8;

    public static String getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() != null) {
            return authentication.getName();
        }
        return null;
    }

    public static String generateProductCode() {
        Random random = new Random();
        StringBuilder productCode = new StringBuilder(CODE_LENGTH);

        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            productCode.append(CHARACTERS.charAt(index));
        }

        return productCode.toString();
    }

}
