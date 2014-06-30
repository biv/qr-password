package ru.giperball.qrpassword.app.exceptions;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import ru.giperball.qrpassword.app.R;

/**
 * Handle exception, that received in activities.
 * Exception, that visible for user, is shown by toast.
 *
 */
public class ExceptionHandler {
    public static void handleException(Context context, Exception e) {
        if (e instanceof VisibleException) {
            VisibleException exception = (VisibleException)e;
            handleVisibleException(context, exception);
        } else {
            Log.e(context.getClass().getSimpleName(), e.getMessage());
            Toast.makeText(context, context.getString(R.string.SOME_ERROR), Toast.LENGTH_LONG).show();
        }
    }

    private static void handleVisibleException(Context context, VisibleException e) {
        Object[] args = e.getArgs();
        String message;
        if (args != null) {
            message = context.getString(e.getMessageId(), args);
        } else {
            message = context.getString(e.getMessageId());
        }
        Log.e(context.getClass().getSimpleName(), message);
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
