package com.stockwise.util;

import com.stockwise.model.User;

public class UserSession {
    private static User currentUser;

    public static void set(User user) {
        currentUser = user;
    }

    public static User get() {
        return currentUser;
    }
}
