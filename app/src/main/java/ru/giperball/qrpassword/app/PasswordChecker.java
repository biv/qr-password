package ru.giperball.qrpassword.app;


import ru.giperball.qrpassword.app.exceptions.VisibleException;

/**
 * Check password with some rules
 */
public class PasswordChecker {
    private static final int MIN_PASSWORD_LENGTH = 10;

    public static void checkPassword(String password) throws Exception {
        if (password == null) {
            throw new Exception("Password is null");
        }
        if (password.length() < MIN_PASSWORD_LENGTH) {
            throw new VisibleException(R.string.PASSWORD_LENGTH_EXCEPTION, MIN_PASSWORD_LENGTH);
        }
    }
}
