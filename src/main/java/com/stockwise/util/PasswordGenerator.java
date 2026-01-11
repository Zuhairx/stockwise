package com.stockwise.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordGenerator {

    public static void main(String[] args) {
        String plainPassword = "admin123";
        String hashed = BCrypt.hashpw(plainPassword, BCrypt.gensalt());

        System.out.println("Plain : " + plainPassword);
        System.out.println("Hash  : " + hashed);
    }
}
