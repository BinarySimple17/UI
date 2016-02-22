package ru.binarysimple.ui;

/**
 * Created by voffka on 11.10.2015.
 */
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import java.util.ArrayList;

/**
 * Created by voffka on 26.06.2015.
 */
public class SaveFragment extends Fragment {

    // data object we want to retain
    //private MyDataObject data;
    ArrayList<Person> savePerson = new ArrayList<Person>();
    String fab_save_tag = Main.FAB_TAG_CALC;

    // this method is only called once for this fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
        ///fab_save_tag = Main.FAB_TAG_CALC;
    }

    public void setData(ArrayList<Person> data) {
        savePerson = data;
    }

    public void setFab_save_tag (String tag){
        fab_save_tag = tag;
    }

    public ArrayList<Person> getData() {
        return savePerson;
    }

    public String getFab_save_tag(){
        return fab_save_tag;
    }
}
