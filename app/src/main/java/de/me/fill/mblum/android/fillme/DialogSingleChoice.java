package de.me.fill.mblum.android.fillme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DialogSingleChoice extends DialogFragment {

    private final String title;
    private String[] choicesArray;
    private int checkedItem;
    private String identifier;

    private DialogSingleChoiceListener mListener;

    interface DialogSingleChoiceListener {
        void onPositiveButtonClicked(String[] choicesArray, int clickedPosition, String identifier);

        void onNegativeButtonClicked();
    }

    DialogSingleChoice(String title, String[] choicesArray, int checkedItem, String identifier) {
        this.title = title;
        this.choicesArray = choicesArray;
        this.checkedItem = checkedItem;
        this.identifier = identifier;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        dialogBuilder.setTitle(title)
                .setSingleChoiceItems(choicesArray, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int clickedPosition) {
                        checkedItem = clickedPosition;
                    }
                })
                .setPositiveButton(R.string.str_save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onPositiveButtonClicked(choicesArray, checkedItem, identifier);
                    }
                })
                .setNegativeButton(R.string.str_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onNegativeButtonClicked();
                    }
                });

        return dialogBuilder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (DialogSingleChoiceListener) context;
    }
}
