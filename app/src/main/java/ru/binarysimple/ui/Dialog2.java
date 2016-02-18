package ru.binarysimple.ui;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.content.SharedPreferences;

import static ru.binarysimple.ui.R.layout.fragment_org;

public class Dialog2 extends DialogFragment implements OnClickListener {

    final String LOG_TAG = "fc_log";
    DBHelper dbHelper;
    SharedPreferences sPref;
    View rootView;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.dialog_2).setPositiveButton(R.string.yes, this)
                .setNegativeButton(R.string.no, this)
                /*.setNeutralButton(R.string.maybe, this)*/
                .setMessage(R.string.dialog_2_text);
        return adb.create();
    }

    public void saveData(){

        ///////////////save company name to db and pref
        //work with DB
        Main main= ((Main) getActivity());
            dbHelper = new DBHelper(getActivity());
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor c = null;
            ContentValues cv = new ContentValues();
        //    Log.d(LOG_TAG, "--- Insert in comp_req: ---");
            cv.put("name", main.getName());
            cv.put("test", 1);
            //long rowID = db.insert(Main.TABLE_NAME_C, null, cv); // insert new record to db
            WorkDB workDB = new WorkDB();
            workDB.insertRecord(getActivity(),Main.TABLE_NAME_C,cv); //new insert to db
            c = db.rawQuery("select * from " + Main.TABLE_NAME_C + "", null);
            c.moveToLast();
            String name = c.getString(c.getColumnIndex("name"));
            Integer c_id = c.getInt(c.getColumnIndex("_id"));
            sPref = main.getMainContext().getSharedPreferences("mPref", main.getMainContext().MODE_PRIVATE); //get preferences object
            SharedPreferences.Editor ed = sPref.edit();
        ed.putString("cn", name); //put company name
        ed.putInt("c_id", c_id); // put company id
        ed.apply(); // save pref
        c.close();
        if (main.getOrgView()!=null){
            main.setSpinner(main.getOrgView(), -2);
        }
        dbHelper.close();
    }

    public  void loadDef(){
    }

    public void onClick(DialogInterface dialog, int which) {
        int i = 0;
        switch (which) {
            case Dialog.BUTTON_POSITIVE:
                // save org to DB
                saveData();
                i = R.string.yes;
                break;
            case Dialog.BUTTON_NEGATIVE:
                // load prev. data from preferences
                loadDef();
                i = R.string.no;
                break;
/*            case Dialog.BUTTON_NEUTRAL:
                // back to orgs fragment
                //new Main().gotoFrag(1);
                i = R.string.maybe;
                break;*/
        }
        if (i > 0)
            Log.d(LOG_TAG, "Dialog 2: " + getResources().getString(i));
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d(LOG_TAG, "Dialog 2: onDismiss");
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d(LOG_TAG, "Dialog 2: onCancel");
    }
}
