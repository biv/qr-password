package ru.giperball.qrpassword.app;

/**
 * Exception from password checker, if password is not valid
 */
public class BadPasswordException extends Exception {

    public BadPasswordException(String detailMessage) {
        super(detailMessage);
    }
}
