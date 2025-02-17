package org.fpoly.capstone.common;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Log4j2
public class CommonUtils {

    private CommonUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() != null) {
            return authentication.getName();
        }
        return null;
    }

}
