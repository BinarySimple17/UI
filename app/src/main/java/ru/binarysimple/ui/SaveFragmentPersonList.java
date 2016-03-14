package ru.binarysimple.ui;

//import android.app.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

public class SaveFragmentPersonList extends Fragment {

    // data object we want to retain
    //private MyDataObject data;
    //ArrayList<Person> savePerson = new ArrayList<Person>();
    Boolean startedForCalc = true;

    // this method is only called once for this fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }

/*    public void setData(ArrayList<Person> data) {
        savePerson = data;
    }*/

    public void setStartedForCalc (Boolean tag){
        startedForCalc = tag;
    }

/*    public ArrayList<Person> getData() {
        return savePerson;
    }*/

    public Boolean getStartedForCalc(){
        return startedForCalc;
    }
}
