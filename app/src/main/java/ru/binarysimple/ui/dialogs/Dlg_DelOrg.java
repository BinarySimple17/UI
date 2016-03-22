package ru.binarysimple.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

import ru.binarysimple.ui.Main;
import ru.binarysimple.ui.R;

public class Dlg_DelOrg extends DialogFragment implements OnClickListener {


    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.dlg_delorg_caption).setPositiveButton(R.string.dlg_delorg_OK, this)
                .setNegativeButton(R.string.dlg_delorg_cancel, this)
                //.setNeutralButton(R.string.maybe, this)
                .setMessage(R.string.dlg_delorg_text);
        return adb.create();
    }

    public void onClick(DialogInterface dialog, int which) {
        int i = 0;
        switch (which) {
            case Dialog.BUTTON_POSITIVE:
                //i = R.string.yes;
                Main main = (Main) getActivity();
                if (main != null) main.deleteOrgBySavedID();
                break;
            case Dialog.BUTTON_NEGATIVE:
                //i = R.string.no;
                break;
        }
    }

}