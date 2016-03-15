package ru.binarysimple.ui;

import android.app.Activity;
import android.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
//import android.support.v4.app.Fragment;

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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        Toast.makeText(getActivity(), "FirstFragment.onAttach()",
                Toast.LENGTH_LONG).show();
        Log.d("Fragment 1", "onAttach");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Toast.makeText(getActivity(), "FirstFragment.onActivityCreated()",
                Toast.LENGTH_LONG).show();
        Log.d("Fragment 1", "onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Toast.makeText(getActivity(), "FirstFragment.onStart()",
                Toast.LENGTH_LONG).show();
        Log.d("Fragment 1", "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(getActivity(), "FirstFragment.onResume()",
                Toast.LENGTH_LONG).show();
        Log.d("Fragment 1", "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Toast.makeText(getActivity(), "FirstFragment.onPause()",
                Toast.LENGTH_LONG).show();
        Log.d("Fragment 1", "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Toast.makeText(getActivity(), "FirstFragment.onStop()",
                Toast.LENGTH_LONG).show();
        Log.d("Fragment 1", "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Toast.makeText(getActivity(), "FirstFragment.onDestroyView()",
                Toast.LENGTH_LONG).show();
        Log.d("Fragment 1", "onDestroyView");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getActivity(), "FirstFragment.onDestroy()",
                Toast.LENGTH_LONG).show();
        Log.d("Fragment 1", "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
        Toast.makeText(getActivity(), "FirstFragment.onDetach()",
                Toast.LENGTH_LONG).show();
        Log.d("Fragment 1", "onDetach");
    }
}