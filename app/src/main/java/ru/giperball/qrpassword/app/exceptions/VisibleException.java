package ru.giperball.qrpassword.app.exceptions;

/**
 * Visible for user exception.
 * It receive message id, that can be taken from R.string.*.
 */
public class VisibleException extends Exception {
    private final int messageId;
    private Object[] args = null;

    public VisibleException(int messageId, Object...args) {
        super();
        this.messageId = messageId;
        this.args = args;
    }

    public int getMessageId() {
        return messageId;
    }

    public Object[] getArgs() {
        return args;
    }
}
