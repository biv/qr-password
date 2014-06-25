package ru.giperball.qrpassword.app;


/**
 * Hold base password for use in all activities
 */
public class PasswordHolder {
    private static PasswordHolder readerPasswordHolder = new PasswordHolder();
    private static PasswordHolder writerPasswordHolder = new PasswordHolder();
    private PasswordHolder() {}
    private String password = null;

    public static PasswordHolder getReaderPasswordHolder() {
        return readerPasswordHolder;
    }

    public static PasswordHolder getWriterPasswordHolder() {
        return writerPasswordHolder;
    }

    synchronized public String getPassword() {
        return password;
    }

    synchronized public void setPassword(String password) {
        this.password = password;
    }

    synchronized boolean isPasswordSet() {
        return password != null;
    }
}
