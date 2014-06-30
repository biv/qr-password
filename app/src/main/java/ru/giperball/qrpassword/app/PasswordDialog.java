package ru.giperball.qrpassword.app;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import ru.giperball.qrpassword.app.exceptions.ExceptionHandler;

/**
 * Dialog for setting password for reading data from qr codes
 */
public class PasswordDialog extends Dialog {
    private PasswordDialogSubmitListener dialogSubmitListener;
    private QrPasswordMode mode;

    public PasswordDialog(Context context,
                          QrPasswordMode mode,
                          PasswordDialogSubmitListener dialogSubmitListener) {
        super(context);
        this.dialogSubmitListener = dialogSubmitListener;
        this.mode = mode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_dialog);
        final EditText editText = (EditText)findViewById(R.id.password_dialog_edit_text);
        CheckBox checkBox = (CheckBox)findViewById(R.id.password_dialog_checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText.setInputType(InputType.TYPE_CLASS_TEXT);
                } else {
                    editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
        Button button = (Button)findViewById(R.id.password_dialog_ok_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String password = editText.getText().toString();
                    PasswordChecker.checkPassword(password);
                    PasswordHolder passwordHolder = mode.getPasswordHolder();
                    passwordHolder.setPassword(password);
                    PasswordDialog.this.dismiss();
                    if (dialogSubmitListener != null) {
                        dialogSubmitListener.onDialogSubmit();
                    }
                } catch (Exception e) {
                    ExceptionHandler.handleException(getContext(), e);
                }
            }
        });
    }
}
