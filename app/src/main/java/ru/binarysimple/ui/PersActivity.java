package ru.binarysimple.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PersActivity extends AppCompatActivity {

    FragmentPersInfo fragmentPersInfo;
    Menu menu;
    Person person;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    public Person getPersonData(){
        String name="";
        String position="";
        String salary="";
        Long id=Long.parseLong("-1");
        Integer comp_id=-1;
        Person person = new Person(name,position,salary,id,comp_id);
        try{
            Intent intent = getIntent();
            person.name=intent.getStringExtra("ed_name");
            person.position=intent.getStringExtra("ed_position");
            person.salary=intent.getStringExtra("ed_salary");
            person.id =intent.getLongExtra("ed_id",-1);
            person.comp_id =intent.getIntExtra("ed_comp_id",-1);
        }
        catch (Exception e){

        }
        return person;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pers);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

// Create the spAdapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections spAdapter.
        mViewPager = (ViewPager) findViewById(R.id.containerPers); //container - viewPager from activity_main
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabsPers);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.setGroupVisible(R.id.grOrg, false);
        menu.setGroupVisible(R.id.grPers, false);
        menu.setGroupVisible(R.id.grPersAdd, true);
        this.menu = menu; //set menu variable
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();
        //noinspection SimplifiableIfStatement

        if (item.getItemId()==R.id.action_savePers){
            Log.d(Main.LOG_TAG, "menu save_pers pressed from pers activity");
            TextView etFio = (TextView) findViewById(R.id.etFIO);
            TextView etPosition = (TextView) findViewById(R.id.etPosition);
            TextView etSalary = (TextView) findViewById(R.id.etSalary);

            if (etFio.getText().toString().equals("")
                    || etPosition.getText().toString().equals("")
                    || etSalary.getText().toString().equals("")){
                Toast.makeText(getBaseContext(), R.string.toastAddPers, Toast.LENGTH_SHORT).show();
                return true;
            }

            Intent intent = new Intent();
            intent.putExtra("name", etFio.getText().toString());
            intent.putExtra("position",etPosition.getText().toString());
            intent.putExtra("salary",etSalary.getText().toString());
            //put here id of person from DB or -1 if new person
            intent.putExtra("id", this.person.id);
            if (this.person.id > -1) {
                setResult(Main.RESULTCODE_PERS_EDITED, intent);
            }
            else setResult(Main.RESULTCODE_PERS_ADDED, intent);

            finish();
        }

        return super.onOptionsItemSelected(item);
    }
   //**********************************************************************************************

    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

//****************************** FRAGMENT CLASSES**************************************************
    public static class FragmentPersInfo extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    public static FragmentPersInfo newInstance(int sectionNumber) {
        FragmentPersInfo fragment = new FragmentPersInfo();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pers_info, container, false);

        PersActivity persActivity = (PersActivity) this.getActivity();
        persActivity.person = persActivity.getPersonData();
        EditText etFio = (EditText) rootView.findViewById(R.id.etFIO);
        EditText etPosition = (EditText) rootView.findViewById(R.id.etPosition);
        EditText etSalary = (EditText) rootView.findViewById(R.id.etSalary);
        etFio.setText(persActivity.person.name);
        etPosition.setText(persActivity.person.position);
        etSalary.setText(persActivity.person.salary);

        return rootView;
    }

}

    public static class FragmentPersIllDays extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public static FragmentPersIllDays newInstance(int sectionNumber) {
            FragmentPersIllDays fragment = new FragmentPersIllDays();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_pers_illdays, container, false);
            return rootView;
        }

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            if (position == 0) {
                fragmentPersInfo = FragmentPersInfo.newInstance(position + 1);
                return fragmentPersInfo;
            } else if (position == 1) {
                return FragmentPersIllDays.newInstance(position + 1);
            }
            else return PlaceholderFragment.newInstance(position + 1);
          //  return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.sectionP1);
            }
            return null;
        }
    }

}
