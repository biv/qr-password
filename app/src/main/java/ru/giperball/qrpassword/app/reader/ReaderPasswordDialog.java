package ru.giperball.qrpassword.app.reader;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import ru.giperball.qrpassword.app.BadPasswordException;
import ru.giperball.qrpassword.app.PasswordDialogSubmitListener;
import ru.giperball.qrpassword.app.PasswordHolder;
import ru.giperball.qrpassword.app.R;

/**
 * Dialog for setting password for reading data from qr codes
 */
public class ReaderPasswordDialog extends Dialog {
    private PasswordDialogSubmitListener dialogSubmitListener;

    public ReaderPasswordDialog(Context context, PasswordDialogSubmitListener dialogSubmitListener) {
        super(context);
        this.dialogSubmitListener = dialogSubmitListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reader_password_dialog);
        final EditText editText = (EditText)findViewById(R.id.reader_password_dialog_edit_text);
        CheckBox checkBox = (CheckBox)findViewById(R.id.reader_password_dialog_checkbox);
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
        Button button = (Button)findViewById(R.id.reader_password_dialog_ok_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PasswordHolder.setReaderPassword(editText.getText().toString());
                    ReaderPasswordDialog.this.dismiss();
                    if (dialogSubmitListener != null) {
                        dialogSubmitListener.onDialogSubmit();
                    }
                } catch (BadPasswordException e) {
                    Toast.makeText(getContext(), R.string.incorrect_password, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
