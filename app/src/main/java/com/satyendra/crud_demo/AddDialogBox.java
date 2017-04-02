package com.satyendra.crud_demo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.satyendra.bean.Student;

/**
 * Created by Satyendra on 01/04/17.
 */
public class AddDialogBox extends DialogFragment {

    public interface AddDialogBoxListener {
        void createData(Student student);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View addDialogView = inflater.inflate(R.layout.add_dialog_layout, null);
        final EditText addedName = (EditText) addDialogView.findViewById(R.id.added_name);
        final EditText addedRollno = (EditText) addDialogView.findViewById(R.id.added_rollno);

        builder.setTitle("Add Student");
        builder.setView(addDialogView);
        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = addedName.getText().toString().trim();
                Integer rollno = Integer.parseInt(addedRollno.getText().toString().trim());

                AddDialogBoxListener mainActivity = (AddDialogBoxListener) getActivity();
                mainActivity.createData(new Student(rollno, name));
                AddDialogBox.this.getDialog().dismiss();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AddDialogBox.this.getDialog().cancel();
            }
        });
        return builder.create();
    }
}
