package ru.binarysimple.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class Main extends AppCompatActivity {

//    FloatingActionButton fab;
    public static final String LOG_TAG = "fc_log";
    public static final String TABLE_NAME = "info";
    public static final String TABLE_NAME_C = "comp_req";
    public static final String TABLE_ILL_DAY = "ill_day";
    public static final String TABLE_HOLY_DAY = "holy_day";
    public static final String TABLE_RESULTS = "results";
    final static String KEY_MENU_MAIN = "KEY_MENU_MAIN";
    final static String KEY_MENU_ORG = "KEY_MENU_ORG";
    final static String KEY_MENU_PERS = "KEY_MENU_PERS";
    final static String KEY_MENU_ADDPERS = "KEY_MENU_ADDPERS";
    final static Integer RESULTCODE_PERS_ADDED = 101;
    final static Integer RESULTCODE_PERS_EDITED = 102;
    final static Integer START_ACT_FOR_EDIT = 102;
    final static String FAB_TAG_CALC = "fab_tag_edit";
    final static String FAB_TAG_SAVE = "fab_tag_save";
    final static Integer RESULTS_CALC = 103;
    SharedPreferences sPref;
    DBHelper dbHelper;
    EditText etCompName;
    //DialogFragment dlg2;
    Menu menu;
    Boolean savedMenuPersIsVisible;
    Boolean savedMenuOrgIsVisible;
    Boolean savedMenuMainIsVisible;
    Boolean savedMenuAddPersIsVisible;
    //String saved_fab_tag;
    FragmentPers fragmentPers;
    ArrayAdapter<String> spAdapter;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    public Context getMainContext(){
        return Main.this;
    }

    public void  setFragmentPers (FragmentPers fragmentPers){
        this.fragmentPers=fragmentPers;
    }

    public View getOrgView(){
        return this.mViewPager.findViewWithTag("org");
    }

    public void saveOrgToDB(String name){
        //Cursor c = null;
        ContentValues cv = new ContentValues();
        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        cv.put("name", name);
        cv.put("test", 1);
        WorkDB workDB = new WorkDB();
        workDB.insertRecord(this, TABLE_NAME_C, cv); //new insert to db
        Cursor c = db.rawQuery("select * from " + Main.TABLE_NAME_C + "", null);
        c.moveToLast();
        setSpinner(getOrgView(), -2);
        Spinner spinner = (Spinner) findViewById(R.id.spOrgs);
        String cn = c.getString(c.getColumnIndex("name"));
        Integer c_id = c.getInt(c.getColumnIndex("_id"));
        sPref = getSharedPreferences("mPref", MODE_PRIVATE); //get preferences object
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString("cn", cn); //put company name
        ed.putInt("c_id", c_id); // put company id
        ed.putInt("sp_id",spinner.getSelectedItemPosition());
        ed.apply(); // save pref
        c.close();
        dbHelper.close();
    }

    public void updateOrgInDB (String name){
        //Cursor c = null;
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("test", 1);
        sPref = getSharedPreferences("mPref", MODE_PRIVATE); //get preferences object
        Integer sp_id = sPref.getInt("sp_id", -1);
        Integer c_id = sPref.getInt("c_id", -1);
        WorkDB workDB = new WorkDB();
        workDB.updateRecord(this, TABLE_NAME_C, cv, c_id.toString());
        setSpinner(getOrgView(), sp_id);
    }

    public String getName(){
        etCompName = (EditText) findViewById(R.id.etCompName); //find object edittext
        return etCompName.getText().toString();
    }


    public void setSpinner(View rootView, Integer pos){
        //ишем Все организации в базе, если есть, то заполняем спиннер, если нет, то  хинт
        try{
            WorkDB workDB = new WorkDB();
            Cursor c = workDB.getData(this, "select * from "+TABLE_NAME_C, null);
            if (c.getCount()>0){ //if found some
                if (spAdapter ==null){
                    spAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
                    spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                }
                Spinner spinner = (Spinner) rootView.findViewById(R.id.spOrgs);
                spinner.setAdapter(spAdapter);
                spAdapter.clear();
                if(c.moveToFirst()){
                    do{
                        spAdapter.add(c.getString(c.getColumnIndex("name")));
                        Log.d(LOG_TAG, "fill spinner "+c.getString(c.getColumnIndex("name")));
                    } while(c.moveToNext());
                }
                c.close();
            if (pos>-1) {
                spinner.setSelection(pos);
                fragmentPers.loadPersonsListFromDB();
            }
                else if (pos == -2){
                spinner.setSelection(spinner.getCount()-1);
            }

            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // извлекаем данные о видимости пункта меню
        if (savedInstanceState != null) {
            savedMenuPersIsVisible = savedInstanceState.getBoolean(KEY_MENU_PERS,true);
            savedMenuOrgIsVisible = savedInstanceState.getBoolean(KEY_MENU_ORG,true);
            savedMenuAddPersIsVisible = savedInstanceState.getBoolean(KEY_MENU_ADDPERS,true);
            savedMenuMainIsVisible = savedInstanceState.getBoolean(KEY_MENU_MAIN,true);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the spAdapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container); //container - viewPager from activity_main
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

      tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

          @Override
          public void onTabSelected(TabLayout.Tab tab) {
              Log.d(LOG_TAG, tab.getPosition() + " onTabSelected");
              if (menu != null) {
                  switch (tab.getPosition()) {
                      case 0:
                          //menu.setGroupVisible(R.id.grMain, false);
                          menu.setGroupVisible(R.id.grOrg, true);
                          menu.setGroupVisible(R.id.grPersAdd, false);
                          menu.setGroupVisible(R.id.grPers, false);
                          mViewPager.setCurrentItem(tab.getPosition(), true);
                          break;
                      case 1:
                          //menu.setGroupVisible(R.id.grMain, false);
                          menu.setGroupVisible(R.id.grOrg, false);
                          menu.setGroupVisible(R.id.grPersAdd, false);
                          menu.setGroupVisible(R.id.grPers, false);
                          mViewPager.setCurrentItem(tab.getPosition(), true);
                          break;
                      case 2:
                         // menu.setGroupVisible(R.id.grMain, false);
                          menu.setGroupVisible(R.id.grOrg, false);
                          menu.setGroupVisible(R.id.grPersAdd, false);
                          menu.setGroupVisible(R.id.grPers, true);
                          mViewPager.setCurrentItem(tab.getPosition(), true);
                          break;
                  }
                  savedMenuPersIsVisible = menu.findItem(R.id.action_editPers).isVisible();
                  savedMenuAddPersIsVisible = menu.findItem(R.id.action_savePers).isVisible();
                  savedMenuOrgIsVisible = menu.findItem(R.id.action_newOrg).isVisible();
              }
          }

          @Override
          public void onTabUnselected(TabLayout.Tab tab) {
              Log.d(LOG_TAG, tab.getPosition() + " onTabUnselected");
              if (tab.getPosition() == 1) {
                  EditText etYear = (EditText) findViewById(R.id.etYear);
                  Spinner spMonth = (Spinner) findViewById(R.id.spMonth);
                  EditText etNdfl = (EditText) findViewById(R.id.etNdfl);
                  EditText etPfr = (EditText) findViewById(R.id.edPfr);
                  EditText etFfoms = (EditText) findViewById(R.id.edFfoms);
                  EditText etFss = (EditText) findViewById(R.id.edFss);
                  sPref = getSharedPreferences("mPref", MODE_PRIVATE); // get preferences
                  SharedPreferences.Editor ed = sPref.edit();
                  ed.putString("year", etYear.getText().toString());
                  ed.putInt("month", spMonth.getSelectedItemPosition());
                  ed.putString("ndfl", etNdfl.getText().toString());
                  ed.putString("pfr", etPfr.getText().toString());
                  ed.putString("ffoms", etFfoms.getText().toString());
                  ed.putString("fss", etFss.getText().toString());
                  ed.apply();
              }
          }

          @Override
          public void onTabReselected(TabLayout.Tab tab) {
              Log.d(LOG_TAG, tab.getPosition() + " onTabReselected");
          }
      });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //menu.setGroupVisible(R.id.grMain,false);
        menu.setGroupVisible(R.id.grOrg,true);
        menu.setGroupVisible(R.id.grPersAdd,false);
        menu.setGroupVisible(R.id.grPers, false);

        this.menu = menu; //set menu variable
        return true;
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(LOG_TAG, "onRestoreInstanceState");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LOG_TAG, "onSaveInstanceState");
        if (menu != null) {
            //сохраняем видимость меню по признаку видимости отдельного пункта из каждой группы
            outState.putBoolean(KEY_MENU_ADDPERS,  menu.findItem(R.id.action_savePers).isVisible());
            outState.putBoolean(KEY_MENU_PERS,  menu.findItem(R.id.action_editPers).isVisible());
            outState.putBoolean(KEY_MENU_ORG,  menu.findItem(R.id.action_newOrg).isVisible());
        }

        if (fragmentPers!= null) {
            fragmentPers.onSaveState();
        }

    }

    // обновление меню
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (savedMenuPersIsVisible != null) menu.setGroupVisible(R.id.grPers,savedMenuPersIsVisible);
        if (savedMenuAddPersIsVisible != null) menu.setGroupVisible(R.id.grPersAdd,savedMenuAddPersIsVisible);
        if (savedMenuOrgIsVisible != null) menu.setGroupVisible(R.id.grOrg,savedMenuOrgIsVisible);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.action_addPers) {
            Log.d(LOG_TAG, "menu add_pers pressed");
            Intent intent = new Intent(this, PersActivity.class);
            startActivityForResult(intent, 1);
            menu.setGroupVisible(R.id.grOrg, false);
            menu.setGroupVisible(R.id.grPers, true);
            menu.setGroupVisible(R.id.grPersAdd, false);
        }

        if (item.getItemId()==R.id.action_editPers) {

            Log.d(LOG_TAG, "menu edit pressed");
            fragmentPers.editPerson();
            //menu.setGroupVisible(R.id.grMain, false);
            menu.setGroupVisible(R.id.grOrg, false);
            menu.setGroupVisible(R.id.grPers, true);
            menu.setGroupVisible(R.id.grPersAdd, false);
        }

        if (item.getItemId()==R.id.action_newOrg) {
            Log.d(LOG_TAG, "menu newOrg pressed");
            EditText etCompName = (EditText) findViewById(R.id.etCompName);
            etCompName.setEnabled(true);
            etCompName.setText("");
            try {
                Spinner spinner = (Spinner) findViewById(R.id.spOrgs);
                spAdapter.add("");
                spinner.setSelection(spinner.getCount()-1);
            }
            catch(Exception e) {
                return super.onOptionsItemSelected(item);
            }
        }
        if (item.getItemId()==R.id.action_cancelOrg) {
            Log.d(LOG_TAG, "menu cancelOrg pressed");
            EditText etCompName = (EditText) findViewById(R.id.etCompName);
            if (!etCompName.isEnabled()) return super.onOptionsItemSelected(item);
            sPref = getSharedPreferences("mPref", MODE_PRIVATE); // get preferences
            etCompName.setText(sPref.getString("cn",""));
            setSpinner(this.getOrgView(),sPref.getInt("c_id",0)-1);
            etCompName.setEnabled(false);
        }
        if (item.getItemId()==R.id.action_saveOrg) {
            Log.d(LOG_TAG, "menu saveOrg pressed");
            EditText etCompName = (EditText) findViewById(R.id.etCompName);
            if (!etCompName.isEnabled()) return super.onOptionsItemSelected(item);
            if (etCompName.getText().toString().equals(""))return super.onOptionsItemSelected(item);
            Spinner spinner = (Spinner) findViewById(R.id.spOrgs);
            etCompName.setEnabled(false);

            if (spinner.getCount()>0) {
                if (spinner.getSelectedItem().toString().equals("")) {
                    saveOrgToDB(etCompName.getText().toString());
                }
                else updateOrgInDB(etCompName.getText().toString());
            }
            else saveOrgToDB(etCompName.getText().toString());

        }
        if (item.getItemId()==R.id.action_editOrg) {
            try{
                Spinner spinner = (Spinner) findViewById(R.id.spOrgs);
                spinner.getSelectedItem().toString().equals("");}
            catch(Exception e) {
                return super.onOptionsItemSelected(item);
            }

            Log.d(LOG_TAG, "menu editOrg pressed");
            EditText etCompName = (EditText) findViewById(R.id.etCompName);
            etCompName.setEnabled(true);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;
        Log.d(LOG_TAG, "Activity result " + resultCode);
        if (resultCode == RESULTCODE_PERS_ADDED) {
            fragmentPers.addPers(data);
            fragmentPers.fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_save));
            fragmentPers.fab.setTag(Main.FAB_TAG_SAVE);
            fragmentPers.fab.show();
            }

        if (resultCode == RESULTCODE_PERS_EDITED) {
            fragmentPers.saveEditedPers(data);
            fragmentPers.fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_save));
            fragmentPers.fab.setTag(Main.FAB_TAG_SAVE);
        }

    }

    /**
     * A placeholder fragment containing a simple view.
     */
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

    public static class FragmentParam extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        String[] data = {"январь", "февраль", "март", "апрель", "май", "июнь", "июль", "август", "сентябрь", "октябрь", "ноябрь", "декабрь"};
        SharedPreferences sPref;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static FragmentParam newInstance(int sectionNumber) {
            FragmentParam fragment = new FragmentParam();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_params, container, false);
            rootView.setTag("param");
            Spinner spinner = (Spinner) rootView.findViewById(R.id.spMonth);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_item, data);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            sPref = getActivity().getSharedPreferences("mPref", MODE_PRIVATE); // get preferences
            EditText etYear = (EditText) rootView.findViewById(R.id.etYear);
            EditText etNdfl = (EditText) rootView.findViewById(R.id.etNdfl);
            EditText etPfr = (EditText) rootView.findViewById(R.id.edPfr);
            EditText etFfoms = (EditText) rootView.findViewById(R.id.edFfoms);
            EditText etFss = (EditText) rootView.findViewById(R.id.edFss);
            String year = sPref.getString("year", "-1");
                       if (!year.equals("-1")) {
                        etYear.setText(year);
                        }
                        else {
                           etYear.setText("" + Calendar.getInstance().get(Calendar.YEAR));
                       }
            spinner.setSelection(sPref.getInt("month",Calendar.getInstance().get(Calendar.MONTH)));
            if (!sPref.getString("ndfl","-1").equals("-1")){
                etNdfl.setText(sPref.getString("ndfl","-1"));
            }
            if (!sPref.getString("pfr","-1").equals("-1")){
                etPfr.setText(sPref.getString("pfr","-1"));
            }
            if (!sPref.getString("ffoms","-1").equals("-1")){
                etFfoms.setText(sPref.getString("ffoms","-1"));
            }
            if (!sPref.getString("fss","-1").equals("-1")){
                etFss.setText(sPref.getString("fss", "-1"));
            }
            return rootView;
        }
    }

    public static class FragmentOrg extends Fragment {
        //public static final String TABLE_NAME = "info";
        public static final String TABLE_NAME_C = "comp_req";
        public static final String LOG_TAG = "fc_log";
        private static final String ARG_SECTION_NUMBER = "section_number";
        //DBHelper dbHelper;
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        SharedPreferences sPref;
        //SQLiteDatabase db;
        EditText etCompName;
        Spinner spOrgs;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static FragmentOrg newInstance(int sectionNumber) {
            FragmentOrg fragment = new FragmentOrg();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_org, container, false);
            rootView.setTag("org");
            String name;
            /**
             * fill spinner
             */
            sPref = getActivity().getSharedPreferences("mPref", MODE_PRIVATE); // get preferences
            Main main= ((Main) getActivity());
            main.setSpinner(rootView,sPref.getInt("sp_id",-1));

            /**
             * fill etCompName from Preferences or set it to default value
             */
            etCompName = (EditText) rootView.findViewById(R.id.etCompName); //find object edittext
            spOrgs = (Spinner) rootView.findViewById(R.id.spOrgs); //find spinner orgs

            name = sPref.getString("cn","-1");
            if (name.equals("-1")){
                etCompName.setHint("Название");
            }
            else etCompName.setText(name); //set hint from pref or "comp name"
            Log.d(LOG_TAG, " set name "+name);

            /**
             * set spinner select item listener
             */
            //Spinner spinner = (Spinner) rootView.findViewById(R.id.spOrgs);
            spOrgs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    Log.d(LOG_TAG, " spinner select "+position);
                    sPref = getActivity().getSharedPreferences("mPref", MODE_PRIVATE); // get preferences
                    Integer c_id = sPref.getInt("c_id", -1);
                    String name = parent.getSelectedItem().toString();
                    etCompName.setText(name);
                    try {
                        WorkDB workDB = new WorkDB();
                        Cursor c = workDB.getData(getActivity(),
                                "select * from "+TABLE_NAME_C+" where name="+"\""+name+"\"", null);
                        if (c.getCount()>0) {
                            c.moveToFirst();
                            if (c_id != c.getInt(c.getColumnIndex("_id"))) {
                                ((Main) getActivity()).fragmentPers.persons.clear();//clear persons array if organisation was changed
                            }

                            c_id=c.getInt(c.getColumnIndex("_id"));
                        }
                        c.close();
                        }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                    SharedPreferences.Editor ed = sPref.edit();
                    ed.putString("cn", name); //put company name
                    ed.putInt("c_id", c_id); // put company id
                    ed.putInt("sp_id",position);
                    ed.apply(); // save pref
                 //   dbHelper.close();
                 }
                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
            return rootView;
        }

    }

    public static class FragmentPers extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        SharedPreferences sPref;
        FloatingActionButton fab;
        ArrayList<Person> persons = new ArrayList<Person>();
        MySimpleArrayAdapter myAdapter;
        ListView lvMain;
        SaveFragment saveFragment;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static FragmentPers newInstance(int sectionNumber) {
            FragmentPers fragment = new FragmentPers();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        /**
         * public methods
         */

        public void onSaveState(){
            Log.d(LOG_TAG, "Fragment PERS custom SaveState");

         //   saveFragment.setData(persons);

        }

        public void addPers(Intent data){
            Log.d(LOG_TAG, "Add person");
            View rootView = this.getView();
            if (data==null){return;}
            try{
                sPref = getActivity().getSharedPreferences("mPref", MODE_PRIVATE); // get preferences
                Integer c_id = sPref.getInt("c_id", -1);
                lvMain = (ListView) rootView.findViewById(R.id.lvMain);
                persons.add(new Person( data.getStringExtra("name"),
                                    data.getStringExtra("position"),
                                    data.getStringExtra("salary"),
                                    data.getIntExtra("id", -1),
                                    data.getIntExtra("comp_id", c_id)));

                myAdapter.notifyDataSetChanged();
                lvMain.setAdapter(myAdapter);}
                catch (Exception e){
                Log.d(LOG_TAG, " addPers exception");
            }
        }

        public void saveEditedPers (Intent data){
            Log.d(LOG_TAG, "Save edited person");
            //View rootView = this.getView();
            if (data==null)return;
            sPref = getActivity().getSharedPreferences("mPref", MODE_PRIVATE); // get preferences
            Integer c_id = sPref.getInt("c_id", -1);
            int i = lvMain.getCheckedItemPosition();
            persons.set(i, new Person(data.getStringExtra("name"),
                                     data.getStringExtra("position"),
                                     data.getStringExtra("salary"),
                                     data.getIntExtra("id", -1),
                                     data.getIntExtra("comp_id", c_id)));
            myAdapter.notifyDataSetChanged();
            lvMain.setAdapter(myAdapter);

        }

        public void savePersonsListToDB(){
            Log.d(LOG_TAG, "myAdapter records count = " + myAdapter.getCount());
            Log.d(LOG_TAG, "persons records count = " + persons.size());

            for (int i=0;i<persons.size();i++){
                Log.d(LOG_TAG, "persons record index = " +i);
                Log.d(LOG_TAG, "name = " +persons.get(i).name);
                Log.d(LOG_TAG, "id = " +persons.get(i).id);

                ContentValues cv = new ContentValues();
                cv.put("comp_id", persons.get(i).comp_id);
                cv.put("name", persons.get(i).name);
                cv.put("pos", persons.get(i).position);
                cv.put("sal", persons.get(i).salary);
                WorkDB workDB = new WorkDB();

                if (persons.get(i).id<0){
                    workDB.insertRecord(getActivity(), TABLE_NAME, cv); //new insert to db*/
                }
                else {
                    workDB.updateRecord(getActivity(),TABLE_NAME,cv,persons.get(i).id.toString());

                }
            }
        }

        public void loadPersonsListFromDB(){
            if (!persons.isEmpty()) return;
            sPref = getActivity().getSharedPreferences("mPref", MODE_PRIVATE); // get preferences
            Integer c_id = sPref.getInt("c_id", -1);
            //WorkDB workDB = new WorkDB();
            //Cursor c = workDB.getData(getActivity(), "select * from "+TABLE_NAME+" WHERE c_id = ?", new String[] {c_id.toString()});
            WorkDB workDB = new WorkDB();
            Cursor c = workDB.getData(getActivity(),
                    "select * from "+TABLE_NAME+" where comp_id="+c_id, null);
            if (c != null){
                if (c.getCount()>0){
                    c.moveToFirst();
                    for (int i=0; i<c.getCount();i++){

                    Person person = new Person(c.getString(c.getColumnIndex("name")),
                                               c.getString(c.getColumnIndex("pos")),
                                               c.getString(c.getColumnIndex("sal")),
                                               c.getInt(c.getColumnIndex("_id")),
                                               c.getInt(c.getColumnIndex("comp_id")));
                    persons.add(i,person);
                    c.move(1);
                    }
                    fab.show();
                }
                else fab.hide();
                c.close();
            }
            else fab.hide();

        }

        public void editPerson(){
            if (lvMain.getCount()>0){
                int i = lvMain.getCheckedItemPosition();
                if ( i<0 ) return;
                Log.d(LOG_TAG, "selected item id in lvMain = " + i);
                Intent intent = new Intent(getActivity(), PersActivity.class);
                intent.putExtra("ed_name", persons.get(i).name);
                intent.putExtra("ed_position", persons.get(i).position);
                intent.putExtra("ed_salary", persons.get(i).salary);
                intent.putExtra("ed_id", persons.get(i).id);
                intent.putExtra("ed_comp_id", persons.get(i).comp_id);
                startActivityForResult(intent, Main.START_ACT_FOR_EDIT);
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_pers, container, false);
            rootView.setTag("pers");

            saveFragment = (SaveFragment) getFragmentManager().findFragmentByTag("SAVE_FRAGMENT");
            fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
            if (saveFragment != null) {
                persons = saveFragment.getData();
                fab.setTag(saveFragment.getFab_save_tag().toString());
                if (fab.getTag().toString() == Main.FAB_TAG_SAVE) fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_save));
            }
            else {
                Log.d(LOG_TAG, "onCreate new fragment");
                saveFragment = new SaveFragment();
                getFragmentManager().beginTransaction()
                        .add(saveFragment, "SAVE_FRAGMENT")
                        .commit();
                fab.setTag(Main.FAB_TAG_CALC);
            }

            lvMain = (ListView) rootView.findViewById(R.id.lvMain);
            myAdapter = new MySimpleArrayAdapter(rootView.getContext(), persons);
            lvMain.setAdapter(myAdapter);


            fab.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    //TODO results here!!!
                    if (lvMain.getCount()>0){
                        if (fab.getTag()==Main.FAB_TAG_SAVE){
                            savePersonsListToDB();
                            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_calc));
                            fab.setTag(Main.FAB_TAG_CALC);
                        }
                        else {

                        Intent intent = new Intent(getActivity(), PersonListActivity.class);
                        intent.putExtra("request",Main.RESULTS_CALC);
                        startActivityForResult(intent, Main.RESULTS_CALC);
                        }
                    }
                }
            });

            loadPersonsListFromDB();

            return rootView;
        }
        /**
         * LOGS
         *
         */

        public void onResume() {
            super.onResume();
            Main main = (Main) getActivity();
            main.setFragmentPers(this);
            Log.d(LOG_TAG, "Fragment Pers onResume save fragmentPers");
        }

        public void onPause() {
            super.onPause();
            Log.d(LOG_TAG, "onPause saveFragment.setData(products);");
            saveFragment.setData(persons);
            saveFragment.setFab_save_tag(fab.getTag().toString());
            super.onPause();
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == 1) {
                return FragmentParam.newInstance(position + 1);
            } else if (position == 0) {
                return FragmentOrg.newInstance(position + 1);
            } else if (position==2){
                fragmentPers = FragmentPers.newInstance(position+1);
                return fragmentPers; //FragmentPers.newInstance(position+1);
            }
            else return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.section1);
                case 1:
                    return getResources().getString(R.string.section2);
                case 2:
                    return getResources().getString(R.string.section3);
            }
            return null;
        }
    }
}
