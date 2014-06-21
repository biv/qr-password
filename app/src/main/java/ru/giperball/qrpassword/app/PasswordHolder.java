package ru.giperball.qrpassword.app;


/**
 * Hold base password for use in all activities
 */
public class PasswordHolder {
    private PasswordHolder() {}
    private static String readerPassword = null;
    private static String writerPassword = null;

    synchronized public static String getReaderPassword() {
        return readerPassword;
    }

    synchronized public static String getWriterPassword() {
        return writerPassword;
    }

    synchronized public static void setReaderPassword(String readerPassword) throws BadPasswordException {
        PasswordChecker.checkPassword(readerPassword);
        PasswordHolder.readerPassword = readerPassword;
    }

    synchronized public static void setWriterPassword(String writerPassword) throws BadPasswordException {
        PasswordChecker.checkPassword(writerPassword);
        PasswordHolder.writerPassword = writerPassword;
    }

    synchronized public static boolean isReaderPasswordSet() {
        return readerPassword != null;
    }

    synchronized public static boolean isWriterPasswordSet() {
        return writerPassword != null;
    }
}
