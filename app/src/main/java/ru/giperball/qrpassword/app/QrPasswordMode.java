package ru.giperball.qrpassword.app;

import android.app.Activity;

import ru.giperball.qrpassword.app.reader.ReaderActivity;
import ru.giperball.qrpassword.app.writer.WriterActivity;

/**
 * Enum with application modes
 */
public enum QrPasswordMode {
    READER_PASSWORD(ReaderActivity.class, PasswordHolder.getReaderPasswordHolder()),
    WRITER_PASSWORD(WriterActivity.class, PasswordHolder.getWriterPasswordHolder());

    private Class<? extends Activity> activityClass;
    private PasswordHolder passwordHolder;

    private QrPasswordMode(Class<? extends Activity> activityClass, PasswordHolder passwordHolder) {
        this.activityClass = activityClass;
        this.passwordHolder = passwordHolder;
    }

    public Class<? extends Activity> getActivityClass() {
        return activityClass;
    }

    public PasswordHolder getPasswordHolder() {
        return passwordHolder;
    }
}
