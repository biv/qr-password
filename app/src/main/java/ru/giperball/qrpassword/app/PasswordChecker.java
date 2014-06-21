package ru.giperball.qrpassword.app;

/**
 * Check password with some rules
 */
public class PasswordChecker {
    private static final int MIN_PASSWORD_LENGTH = 10;

    public static void checkPassword(String password) throws BadPasswordException {
        if (password == null) {
            throw new BadPasswordException("password is null");
        }
        if (password.isEmpty()) {
            throw new BadPasswordException("password is empty");
        }
        if (password.length() < MIN_PASSWORD_LENGTH) {
            throw new BadPasswordException("password have to have at least " +
                    MIN_PASSWORD_LENGTH + " symbols");
        }
    }
}
