package com.satyendra.crud_demo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.satyendra.bean.Student;
import com.satyendra.constants.Constants;

/**
 * Created by Satyendra on 29/03/17.
 */
public class EditDialogBox extends DialogFragment {

    private static final String TAG = "EditDialogBox";

    public interface EditDialogBoxListener {
        void updateData(Student student);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View editDialogView = inflater.inflate(R.layout.edit_dialog_layout, null);
        final EditText editedName = (EditText) editDialogView.findViewById(R.id.edited_name);

        Bundle bundle = this.getArguments();
        final int rollno = bundle.getInt(Constants.ROLLNO);
        builder.setTitle(String.valueOf(rollno));
        builder.setView(editDialogView);
        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = editedName.getText().toString().trim();
                Log.d(TAG, "name : " + name);

                EditDialogBoxListener mainActivity = (EditDialogBoxListener) getActivity();
                mainActivity.updateData(new Student(rollno, name));
                EditDialogBox.this.getDialog().dismiss();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditDialogBox.this.getDialog().cancel();
            }
        });
        return builder.create();
    }
}
