package com.satyendra.crud_demo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.satyendra.constants.Constants;

/**
 * Created by Satyendra on 01/04/17.
 */
public class DeleteDialogBox extends DialogFragment {

    public interface DeleteDialogBoxListener {
        void deleteData(int rollno);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        Bundle bundle = this.getArguments();
        final int rollno = bundle.getInt(Constants.ROLLNO);

        builder.setTitle(String.valueOf(rollno));
        builder.setMessage("Confirm Deletion ?");
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DeleteDialogBoxListener mainActivity = (DeleteDialogBoxListener) getActivity();
                mainActivity.deleteData(rollno);
                DeleteDialogBox.this.getDialog().dismiss();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DeleteDialogBox.this.getDialog().cancel();
            }
        });
        return builder.create();
    }
}
