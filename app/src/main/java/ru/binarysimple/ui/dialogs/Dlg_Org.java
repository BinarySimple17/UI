package ru.binarysimple.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.TextView;

import ru.binarysimple.ui.Main;
import ru.binarysimple.ui.R;

public class Dlg_Org extends DialogFragment {
    SharedPreferences sPref;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.dlg_org_caption)
                .setView(inflater.inflate(R.layout.dlg_org, null))
                .setPositiveButton(R.string.dlg_org_OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText etComp = (EditText) getDialog().findViewById(R.id.etDlgCompName);
                        Main main = (Main) getActivity();
                        TextView etCompName = (TextView) main.getOrgView().findViewById(R.id.etCompName);
                        etCompName.setText(etComp.getText());
                        FloatingActionButton fab = (FloatingActionButton) main.getOrgView().findViewById(R.id.fab_org);
                        fab.setTag(Main.FAB_ORG_TAG_SAVE);
                        fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_save));
                        main.saveORG(main.getOrgView());
                    }
                })
                .setNegativeButton(R.string.dlg_org_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Main main = (Main) getActivity();
                        TextView etCompName = (TextView) main.getOrgView().findViewById(R.id.etCompName);
                        sPref = main.getMainContext().getSharedPreferences("mPref", main.getMainContext().MODE_PRIVATE);
                        etCompName.setText(sPref.getString("cn", ""));
                        main.setSpinner(main.getOrgView(), sPref.getInt("c_id", 0) - 1);
                        main.setSpinner(main.getOrgView(), sPref.getInt("c_id", 0) - 1);
                    }
                });
        return adb.create();
    }
}
