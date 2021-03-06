package ru.binarysimple.ui;

import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Calendar;

import ru.binarysimple.ui.dialogs.Dlg_DelOrg;
import ru.binarysimple.ui.dialogs.Dlg_Org;

public class Main extends AppCompatActivity {

    //    FloatingActionButton fab;
    public static final String LOG_TAG = "fc_log";
    public static final String TABLE_NAME = "info";
    public static final String TABLE_NAME_C = "comp_req";
    public static final String TABLE_ILL_DAY = "ill_day";
    public static final String TABLE_HOLY_DAY = "holy_day";
    public static final String TABLE_RESULTS = "results";
    public final static String FAB_ORG_TAG_SAVE = "fab_org_tag_save";
    public final static String FAB_ORG_TAG_CALC = "fab_org_tag_calc";
    final static String KEY_MENU_MAIN = "KEY_MENU_MAIN";
    final static String KEY_MENU_ORG = "KEY_MENU_ORG";
    final static String KEY_MENU_PERS = "KEY_MENU_PERS";
    final static String KEY_MENU_ADDPERS = "KEY_MENU_ADDPERS";
    final static Integer RESULTCODE_PERS_ADDED = 101;
    final static Integer RESULTCODE_PERS_EDITED = 102;
    final static Integer START_ACT_FOR_EDIT = 102;
    final static String FAB_TAG_CALC = "fab_tag_edit";
    final static String FAB_TAG_SAVE = "fab_tag_save";
    final static String FAB_PARAM_TAG_EDIT = "fab_tag_edit";
    final static String FAB_PARAM_TAG_SAVE = "fab_tag_save";
    final static String RESULTS_REQUEST_CALC = "REQUEST";
    final static String RESULTS_LOAD = "LOAD";
    final static String RESULTS_CALC = "CALC";
    final static String RESULTS_REQUEST_MONTH = "request_month";
    final static String RESULTS_REQUEST_YEAR = "request_year";

    private SharedPreferences sPref;
    //  DBHelper dbHelper;
    private TextView etCompName;
    private Menu menu;
    private Boolean savedMenuPersIsVisible;
    private Boolean savedMenuOrgIsVisible;
    Boolean savedMenuMainIsVisible;
    private Boolean savedMenuAddPersIsVisible;
    private FragmentPers fragmentPers;
    private ArrayAdapter<String> spAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;


    public Context getMainContext() {
        return Main.this;
    }

    private void setFragmentPers(FragmentPers fragmentPers) {
        this.fragmentPers = fragmentPers;
    }

    public View getOrgView() {
        return this.mViewPager.findViewWithTag("org");
    }

    private void saveOrgToDB(String name) {
        //Cursor c = null;
        ContentValues cv = new ContentValues();
        //dbHelper = new DBHelper(this);
        //SQLiteDatabase db = dbHelper.getWritableDatabase();

        cv.put("name", name);
        cv.put("test", 1);
        cv.put("ndfl", getResources().getString(R.string.par_ndfl_hint));
        cv.put("pfr", getResources().getString(R.string.par_pfr_hint));
        cv.put("fss", getResources().getString(R.string.par_fss_hint));
        cv.put("ffoms", getResources().getString(R.string.par_ffoms_hint));
        cv.put("year", "" + Calendar.getInstance().get(Calendar.YEAR));
        cv.put("month", Calendar.getInstance().get(Calendar.MONTH));
        WorkDB workDB = new WorkDB();
        workDB.insertRecord(this, TABLE_NAME_C, cv); //new insert to db
        //Cursor c = db.rawQuery("select * from " + Main.TABLE_NAME_C + "", null);
        Cursor c = workDB.getData(this, "select * from " + Main.TABLE_NAME_C + "", null);
        c.moveToLast();
        setSpinner(getOrgView(), -2);
        Spinner spinner = (Spinner) findViewById(R.id.spOrgs);
        String cn = c.getString(c.getColumnIndex("name"));
        Integer c_id = c.getInt(c.getColumnIndex("_id"));
        sPref = getSharedPreferences("mPref", MODE_PRIVATE); //get preferences object
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString("cn", cn); //put company name
        ed.putInt("c_id", c_id); // put company id
        ed.putInt("sp_id", spinner.getSelectedItemPosition());
        ed.putInt("month", Calendar.getInstance().get(Calendar.MONTH));
        ed.putString("year", "" + Calendar.getInstance().get(Calendar.YEAR));
        ed.putString("ndfl", getResources().getString(R.string.par_ndfl_hint));
        ed.putString("pfr", getResources().getString(R.string.par_pfr_hint));
        ed.putString("fss", getResources().getString(R.string.par_fss_hint));
        ed.putString("ffoms", getResources().getString(R.string.par_ffoms_hint));
        ed.apply(); // save pref
        c.close();
        //  dbHelper.close();
    }

    private void clearPersonsList() {
        if (fragmentPers == null) return;
        fragmentPers.persons.clear();
        fragmentPers.myAdapter.notifyDataSetChanged();
    }

    private void updateOrgInDB(String name) {
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

    public void saveORG(View rootView) {
        Log.d(LOG_TAG, "menu saveOrg pressed");
        etCompName = (TextView) findViewById(R.id.etCompName);
        if (etCompName.getText().toString().equals("")) {
            Snackbar.make(rootView, R.string.org_name_error, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }
        Spinner spinner = (Spinner) rootView.findViewById(R.id.spOrgs);
        if (spinner.getCount() > 0) {
            if (spinner.getSelectedItem().toString().equals("")) {
                saveOrgToDB(etCompName.getText().toString());
                clearPersonsList();
            } else updateOrgInDB(etCompName.getText().toString());
        } else saveOrgToDB(etCompName.getText().toString());
        FloatingActionButton fab_org = (FloatingActionButton) rootView.findViewById(R.id.fab_org);
        fab_org.setImageDrawable(getResources().getDrawable(R.drawable.ic_calc));
        fab_org.setTag(Main.FAB_ORG_TAG_CALC);

    }

    public String getName() {
        etCompName = (TextView) findViewById(R.id.etCompName);
        return etCompName.getText().toString();
    }


    public void setSpinner(View rootView, Integer pos) {
        //ишем Все организации в базе, если есть, то заполняем спиннер, если нет, то  хинт
        try {
            WorkDB workDB = new WorkDB();
            Spinner spinner = (Spinner) rootView.findViewById(R.id.spOrgs);
            TextView etCompName = (TextView) rootView.findViewById(R.id.etCompName);
            if (spAdapter == null) {
                spAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
                spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            }
            Cursor c = workDB.getData(this, "select * from " + TABLE_NAME_C, null);
            if (c.getCount() > 0) { //if found some
                spinner.setAdapter(spAdapter);
                spAdapter.clear();
                if (c.moveToFirst()) {
                    do {
                        spAdapter.add(c.getString(c.getColumnIndex("name")));
                        Log.d(LOG_TAG, "fill spinner " + c.getString(c.getColumnIndex("name")));
                    } while (c.moveToNext());
                }
                c.close();
                if (pos > -1) {
                    spinner.setSelection(pos);
                    if (fragmentPers != null) fragmentPers.loadPersonsListFromDB();

                } else if (pos == -2) {
                    spinner.setSelection(spinner.getCount() - 1);
                }
            }
            else {
                spAdapter.clear();
                etCompName.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // извлекаем данные о видимости пункта меню
        if (savedInstanceState != null) {
            savedMenuPersIsVisible = savedInstanceState.getBoolean(KEY_MENU_PERS, true);
            savedMenuOrgIsVisible = savedInstanceState.getBoolean(KEY_MENU_ORG, true);
            savedMenuAddPersIsVisible = savedInstanceState.getBoolean(KEY_MENU_ADDPERS, true);
            savedMenuMainIsVisible = savedInstanceState.getBoolean(KEY_MENU_MAIN, true);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
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
                            loadParams();
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
/*                Log.d(LOG_TAG, tab.getPosition() + " onTabUnselected");
                if (tab.getPosition() == 1) {
                }*/
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //              Log.d(LOG_TAG, tab.getPosition() + " onTabReselected");
            }
        });


    }
//TODO CONTEXT MENU
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_del_person, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId())
        {
            case R.id.action_delPerson:
                if (fragmentPers != null) {
                    deleteOnePerson(fragmentPers.persons.get(info.position));
                    fragmentPers.persons.remove(info.position);
                    fragmentPers.myAdapter.notifyDataSetChanged();
                }
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }

    void deleteOnePerson (Person person){
        Log.d(LOG_TAG, "deleteOnePerson "+person.name);
        WorkDB workDB = new WorkDB();
        workDB.delResultsByIDPerson(this.getMainContext(),person.id.toString());
        workDB.delOnePersonByID(this.getMainContext(),person.id.toString());
    }

    void loadParams() {
        EditText etYear = (EditText) findViewById(R.id.etYear);
        Spinner spMonth = (Spinner) findViewById(R.id.spMonth);
        EditText etNdfl = (EditText) findViewById(R.id.etNdfl);
        EditText etPfr = (EditText) findViewById(R.id.edPfr);
        EditText etFfoms = (EditText) findViewById(R.id.edFfoms);
        EditText etFss = (EditText) findViewById(R.id.edFss);
        sPref = getSharedPreferences("mPref", MODE_PRIVATE); // get preferences
        String year = sPref.getString("year", "-1");
        if (!year.equals("-1")) {
            etYear.setText(year);
        } else {
            etYear.setText("" + Calendar.getInstance().get(Calendar.YEAR));
        }
        //  Spinner spinner = (Spinner) findViewById(R.id.spMonth);
        spMonth.setSelection(sPref.getInt("month", Calendar.getInstance().get(Calendar.MONTH)));
        if (!sPref.getString("ndfl", "-1").equals("-1")) {
            etNdfl.setText(sPref.getString("ndfl", "-1"));
        }
        if (!sPref.getString("pfr", "-1").equals("-1")) {
            etPfr.setText(sPref.getString("pfr", "-1"));
        }
        if (!sPref.getString("ffoms", "-1").equals("-1")) {
            etFfoms.setText(sPref.getString("ffoms", "-1"));
        }
        if (!sPref.getString("fss", "-1").equals("-1")) {
            etFss.setText(sPref.getString("fss", "-1"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //menu.setGroupVisible(R.id.grMain,false);
        menu.setGroupVisible(R.id.grOrg, true);
        menu.setGroupVisible(R.id.grPersAdd, false);
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
            outState.putBoolean(KEY_MENU_ADDPERS, menu.findItem(R.id.action_savePers).isVisible());
            outState.putBoolean(KEY_MENU_PERS, menu.findItem(R.id.action_editPers).isVisible());
            outState.putBoolean(KEY_MENU_ORG, menu.findItem(R.id.action_newOrg).isVisible());
        }

        if (fragmentPers != null) {
            fragmentPers.onSaveState();
        }

    }

    // обновление меню
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (savedMenuPersIsVisible != null)
            menu.setGroupVisible(R.id.grPers, savedMenuPersIsVisible);
        if (savedMenuAddPersIsVisible != null)
            menu.setGroupVisible(R.id.grPersAdd, savedMenuAddPersIsVisible);
        if (savedMenuOrgIsVisible != null) menu.setGroupVisible(R.id.grOrg, savedMenuOrgIsVisible);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_addPers) {
            Log.d(LOG_TAG, "menu add_pers pressed");
            sPref = getSharedPreferences("mPref", MODE_PRIVATE); // get preferences
            Integer c_id = sPref.getInt("c_id", -1);
            if (c_id < 0) {
                //Snackbar.make(findViewById(R.id.), "Результаты должны быть очищены?", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                return super.onOptionsItemSelected(item);
            }
            Intent intent = new Intent(this, PersActivity.class);
            startActivityForResult(intent, 1);
            menu.setGroupVisible(R.id.grOrg, false);
            menu.setGroupVisible(R.id.grPers, true);
            menu.setGroupVisible(R.id.grPersAdd, false);
        }

        if (item.getItemId() == R.id.action_editPers) {

            Log.d(LOG_TAG, "menu edit pressed");
            fragmentPers.editPerson();
            //menu.setGroupVisible(R.id.grMain, false);
            menu.setGroupVisible(R.id.grOrg, false);
            menu.setGroupVisible(R.id.grPers, true);
            menu.setGroupVisible(R.id.grPersAdd, false);
        }

        if (item.getItemId() == R.id.action_delOrg) {
            Log.d(LOG_TAG, "delete org menu item pressed");
            //deleteOrgBySavedID();
            DialogFragment dlg2 = new Dlg_DelOrg();
            dlg2.show(getFragmentManager(), "del_org");
        }

        if (item.getItemId() == R.id.action_newOrg) {
            Log.d(LOG_TAG, "menu newOrg pressed");
            TextView etCompName = (TextView) findViewById(R.id.etCompName);
            etCompName.setText("");
            try {
                Spinner spinner = (Spinner) findViewById(R.id.spOrgs);
                if (spAdapter == null) {
                    spAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
                    spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                }
                spAdapter.add("");
                spinner.setSelection(spinner.getCount() - 1);
                DialogFragment dlg1 = new Dlg_Org();
                dlg1.show(getFragmentManager(), "new_org");
            } catch (Exception e) {
                return super.onOptionsItemSelected(item);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void deleteOrgBySavedID (){
        //TODO√ получаю список людей по ид конторы - persons
        //√     удаляю все результаты по каждому ид сотрудника
        //√     удаляю всех сотрудников по ид конторы
        //√     удаляю контору по ид
        //√     очищаю сПреф
        //лоадпарамс()
        // обновляю спиннер с параметром -1

        ArrayList<Person> persons = new ArrayList<Person>();

        sPref = getSharedPreferences("mPref", MODE_PRIVATE); // get preferences
        Integer c_id = sPref.getInt("c_id", -1);
        if (c_id < 0) return;

        WorkDB workDB = new WorkDB();
        Cursor c = workDB.getData(this,
                "select * from " + TABLE_NAME + " where comp_id=" + c_id, null);
        if (c != null) {
            if (c.getCount() > 0) {
                c.moveToFirst();
                for (int i = 0; i < c.getCount(); i++) {
                    workDB.delResultsByIDPerson(this,Long.toString(c.getLong(c.getColumnIndex("_id"))));
              //      Person person = new Person(c.getString(c.getColumnIndex("name")),
              //              c.getString(c.getColumnIndex("pos")),
              //              c.getString(c.getColumnIndex("sal")),
               //             c.getLong(c.getColumnIndex("_id")),
               //             c.getInt(c.getColumnIndex("comp_id")));
               //     persons.add(i, person);
                    c.move(1);
                }
            }
            c.close();
        }
        workDB.delPersons(this, c_id.toString());
        workDB.delOneOrgByID(this, c_id.toString());

        SharedPreferences.Editor ed = sPref.edit();
        ed.putString("cn", ""); //put company name
        ed.putInt("c_id", -1); // put company id
        ed.putInt("sp_id", 0);
        ed.putInt("month", Calendar.getInstance().get(Calendar.MONTH));
        ed.putString("year", "" + Calendar.getInstance().get(Calendar.YEAR));
        ed.putString("ndfl", getResources().getString(R.string.par_ndfl_hint));
        ed.putString("pfr", getResources().getString(R.string.par_pfr_hint));
        ed.putString("fss", getResources().getString(R.string.par_fss_hint));
        ed.putString("ffoms", getResources().getString(R.string.par_ffoms_hint));
        ed.apply(); // save pref
        //loadParams();
        setSpinner(this.getOrgView(),-1);
    }

    //TODO ACTIVITY RESULT
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;
        Log.d(LOG_TAG, "Activity result " + resultCode);
        if (resultCode == RESULTCODE_PERS_ADDED) {
            fragmentPers.addPers(data);
            //fragmentPers.fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_save));
            //fragmentPers.fab.setTag(Main.FAB_TAG_SAVE);
            fragmentPers.fab.show();
            fragmentPers.savePersonsListToDB();
            fragmentPers.fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_calc));
        }

        if (resultCode == RESULTCODE_PERS_EDITED) {
            fragmentPers.saveEditedPers(data);
            //fragmentPers.fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_save));
            //fragmentPers.fab.setTag(Main.FAB_TAG_SAVE);
            fragmentPers.savePersonsListToDB();
            fragmentPers.fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_calc));
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

    //TODO FRAGMENT PARAMS
    public static class FragmentParam extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        String[] data = {"январь", "февраль", "март", "апрель", "май", "июнь", "июль", "август", "сентябрь", "октябрь", "ноябрь", "декабрь"};
        SharedPreferences sPref;
        FloatingActionButton fab_param;

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
            final View rootView = inflater.inflate(R.layout.fragment_params, container, false);
            rootView.setTag("param");

            //** ADS
            AdView mAdView = (AdView) rootView.findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);


            Spinner spinner = (Spinner) rootView.findViewById(R.id.spMonth);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_item, data);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setEnabled(false);

            sPref = getActivity().getSharedPreferences("mPref", MODE_PRIVATE); // get preferences
            EditText etYear = (EditText) rootView.findViewById(R.id.etYear);
            EditText etNdfl = (EditText) rootView.findViewById(R.id.etNdfl);
            EditText etPfr = (EditText) rootView.findViewById(R.id.edPfr);
            EditText etFfoms = (EditText) rootView.findViewById(R.id.edFfoms);
            EditText etFss = (EditText) rootView.findViewById(R.id.edFss);
            String year = sPref.getString("year", "-1");
            if (!year.equals("-1")) {
                etYear.setText(year);
            } else {
                etYear.setText("" + Calendar.getInstance().get(Calendar.YEAR));
            }
            spinner.setSelection(sPref.getInt("month", Calendar.getInstance().get(Calendar.MONTH)));
            if (!sPref.getString("ndfl", "-1").equals("-1")) {
                etNdfl.setText(sPref.getString("ndfl", "-1"));
            }
            if (!sPref.getString("pfr", "-1").equals("-1")) {
                etPfr.setText(sPref.getString("pfr", "-1"));
            }
            if (!sPref.getString("ffoms", "-1").equals("-1")) {
                etFfoms.setText(sPref.getString("ffoms", "-1"));
            }
            if (!sPref.getString("fss", "-1").equals("-1")) {
                etFss.setText(sPref.getString("fss", "-1"));
            }

            fab_param = (FloatingActionButton) rootView.findViewById(R.id.fab_params);
            fab_param.setTag(Main.FAB_PARAM_TAG_EDIT);
            fab_param.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveParamStates(rootView);
                }
            });

            return rootView;
        }

        private void saveParams(View rootView) {   //save params to sPref and DB
            EditText etYear = (EditText) rootView.findViewById(R.id.etYear);
            Spinner spMonth = (Spinner) rootView.findViewById(R.id.spMonth);
            EditText etNdfl = (EditText) rootView.findViewById(R.id.etNdfl);
            EditText etPfr = (EditText) rootView.findViewById(R.id.edPfr);
            EditText etFfoms = (EditText) rootView.findViewById(R.id.edFfoms);
            EditText etFss = (EditText) rootView.findViewById(R.id.edFss);
            sPref = getActivity().getSharedPreferences("mPref", MODE_PRIVATE); // get preferences
            Integer comp_id = sPref.getInt("c_id", -1);
            if (comp_id < 0) return;

            SharedPreferences.Editor ed = sPref.edit();
            ed.putString("year", etYear.getText().toString());
            ed.putInt("month", spMonth.getSelectedItemPosition());
            ed.putString("ndfl", etNdfl.getText().toString());
            ed.putString("pfr", etPfr.getText().toString());
            ed.putString("ffoms", etFfoms.getText().toString());
            ed.putString("fss", etFss.getText().toString());
            ed.apply();

            WorkDB workDB = new WorkDB();
            ContentValues cv = new ContentValues();
            cv.put("test", 1);
            cv.put("ndfl", etNdfl.getText().toString());
            cv.put("pfr", etPfr.getText().toString());
            cv.put("fss", etFss.getText().toString());
            cv.put("ffoms", etFfoms.getText().toString());
            cv.put("year", etYear.getText().toString());
            cv.put("month", spMonth.getSelectedItemPosition());
            spMonth.getSelectedItemPosition();
            workDB.updateRecord(getActivity(), TABLE_NAME_C, cv, comp_id.toString());
        }

        private void saveParamStates(View rootView) {
            fab_param = (FloatingActionButton) rootView.findViewById(R.id.fab_params);
            if (fab_param.getTag().equals(Main.FAB_PARAM_TAG_SAVE)) {
                fab_param.setTag(Main.FAB_PARAM_TAG_EDIT);
                fab_param.setImageDrawable(getResources().getDrawable(R.drawable.ic_editorg));
                saveParams(rootView);
            } else {
                fab_param.setTag(Main.FAB_PARAM_TAG_SAVE);
                fab_param.setImageDrawable(getResources().getDrawable(R.drawable.ic_save));
            }
            rootView.findViewById(R.id.etYear).setEnabled(!rootView.findViewById(R.id.etYear).isEnabled());
            rootView.findViewById(R.id.spMonth).setEnabled(!rootView.findViewById(R.id.spMonth).isEnabled());
            rootView.findViewById(R.id.etNdfl).setEnabled(!rootView.findViewById(R.id.etNdfl).isEnabled());
            rootView.findViewById(R.id.edPfr).setEnabled(!rootView.findViewById(R.id.edPfr).isEnabled());
            rootView.findViewById(R.id.edFfoms).setEnabled(!rootView.findViewById(R.id.edFfoms).isEnabled());
            rootView.findViewById(R.id.edFss).setEnabled(!rootView.findViewById(R.id.edFss).isEnabled());
        }
    }

    public static class FragmentOrg extends Fragment {
        //public static final String TABLE_NAME = "info";
        public static final String TABLE_NAME_C = "comp_req";
        public static final String LOG_TAG = "fc_log";
        private static final String ARG_SECTION_NUMBER = "section_number";
        FloatingActionButton fab_org;
        //DBHelper dbHelper;
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        SharedPreferences sPref;
        //SQLiteDatabase db;
        TextView etCompName;
        Spinner spOrgs;

        /****
         * etCompName onClick listeners here
         */
        private View.OnClickListener onOnClickEvent = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dlg1 = new Dlg_Org();
                dlg1.show(getActivity().getFragmentManager(), "dlg1");

            }
        };

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        //TODO FRAGMENT ORG
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
            final View rootView = inflater.inflate(R.layout.fragment_org, container, false);
            rootView.setTag("org");
            String name;

            //** ADS
            AdView mAdView = (AdView) rootView.findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);

            /**
             * fill spinner
             */
            sPref = getActivity().getSharedPreferences("mPref", MODE_PRIVATE); // get preferences
            final Main main = ((Main) getActivity());
            main.setSpinner(rootView, sPref.getInt("sp_id", -1));


            /**
             * fill etCompName from Preferences or set it to default value
             */
            etCompName = (TextView) rootView.findViewById(R.id.etCompName); //find object edittext
            spOrgs = (Spinner) rootView.findViewById(R.id.spOrgs); //find spinner orgs
            fab_org = (FloatingActionButton) rootView.findViewById(R.id.fab_org);

            name = sPref.getString("cn", "-1");
            if (name.equals("-1")) {
                etCompName.setHint("Название");
            } else etCompName.setText(name); //set hint from pref or "comp name"
            Log.d(LOG_TAG, " set name " + name);

            /**
             * set spinner select item listener
             */
            //TODO spinner select routine
            spOrgs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    Log.d(LOG_TAG, " spinner select " + position);
                    sPref = getActivity().getSharedPreferences("mPref", MODE_PRIVATE); // get preferences
                    Integer c_id = sPref.getInt("c_id", -1);
                    String year = "" + Calendar.getInstance().get(Calendar.YEAR);
                    Integer month = Calendar.getInstance().get(Calendar.MONTH);
                    String ndfl = getResources().getString(R.string.par_ndfl_hint);
                    String pfr = getResources().getString(R.string.par_pfr_hint);
                    String ffoms = getResources().getString(R.string.par_ffoms_hint);
                    String fss = getResources().getString(R.string.par_fss_hint);
                    String name = parent.getSelectedItem().toString();
                    etCompName.setText(name);
                    try {
                        WorkDB workDB = new WorkDB();
                        Cursor c = workDB.getData(getActivity(),
                                "select * from " + TABLE_NAME_C + " where name=" + "\"" + name + "\"", null);
                        if (c.getCount() > 0) {
                            c.moveToFirst();
                            if (c_id != c.getInt(c.getColumnIndex("_id"))) {
                                if (((Main) getActivity()).fragmentPers != null)
                                    ((Main) getActivity()).fragmentPers.persons.clear();//clear persons array if organisation was changed
                            }

                            c_id = c.getInt(c.getColumnIndex("_id"));
                            year = c.getString(c.getColumnIndex("year"));
                            month = c.getInt(c.getColumnIndex("month"));
                            ndfl = c.getString(c.getColumnIndex("ndfl"));
                            pfr = c.getString(c.getColumnIndex("pfr"));
                            ffoms = c.getString(c.getColumnIndex("ffoms"));
                            fss = c.getString(c.getColumnIndex("fss"));
                        }
                        c.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    SharedPreferences.Editor ed = sPref.edit();
                    ed.putString("cn", name); //put company name
                    ed.putInt("c_id", c_id); // put company id
                    ed.putInt("sp_id", position);
                    ed.putString("year", year);
                    ed.putInt("month", month);
                    ed.putString("ndfl", ndfl);
                    ed.putString("pfr", pfr);
                    ed.putString("ffoms", ffoms);
                    ed.putString("fss", fss);
                    ed.apply(); // save pref
                    //   dbHelper.close();
                    //main.loadParams();
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });

            etCompName.setOnClickListener(onOnClickEvent);
            fab_org.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!etCompName.getText().equals("")) {
                        if (fab_org.getTag() == Main.FAB_ORG_TAG_SAVE) {
                            main.saveORG(rootView);
                        } else {
                            //Snackbar.make(rootView, "Here must be load all results action", Snackbar.LENGTH_LONG)
                            //        .setAction("Action", null).show();
                            //         Intent intent = new Intent(getActivity(), PersonListActivity.class);
                            //         intent.putExtra(Main.RESULTS_REQUEST_CALC, Main.RESULTS_LOAD);
                            //         startActivity(intent);
                            //sPref = getActivity().getSharedPreferences("mPref", MODE_PRIVATE); //get preferences object
                            //SharedPreferences.Editor ed = sPref.edit();
                            //ed.putString(Main.RESULTS_REQUEST_CALC, Main.RESULTS_LOAD); //results request
                            //ed.apply(); // save pref
                            //Intent intent = new Intent(getActivity(), PersonListActivity.class);
                            Intent intent = new Intent(getActivity(), SavedResultsList.class);
                            startActivity(intent);

                        }
                    }
                }
            });

            return rootView;
        }

    }

    //TODO FRAGMNET PERS
    public static class FragmentPers extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";
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

        public void onSaveState() {
            //Log.d(LOG_TAG, "Fragment PERS custom SaveState");
        }

        public void addPers(Intent data) {
            Log.d(LOG_TAG, "Add person");
            View rootView = this.getView();
            if (data == null) {
                return;
            }
            try {
                sPref = getActivity().getSharedPreferences("mPref", MODE_PRIVATE); // get preferences
                Integer c_id = sPref.getInt("c_id", -1);
                lvMain = (ListView) rootView.findViewById(R.id.lvMain);
                persons.add(new Person(data.getStringExtra("name"),
                        data.getStringExtra("position"),
                        data.getStringExtra("salary"),
                        data.getLongExtra("id", -1),
                        data.getIntExtra("comp_id", c_id)));
                myAdapter.notifyDataSetChanged();
                lvMain.setAdapter(myAdapter);
            } catch (Exception e) {
                Log.d(LOG_TAG, " addPers exception");
            }
        }

        public void saveEditedPers(Intent data) {
            Log.d(LOG_TAG, "Save edited person");
            //View rootView = this.getView();
            if (data == null) return;
            sPref = getActivity().getSharedPreferences("mPref", MODE_PRIVATE); // get preferences
            Integer c_id = sPref.getInt("c_id", -1);
            int i = lvMain.getCheckedItemPosition();
            persons.set(i, new Person(data.getStringExtra("name"),
                    data.getStringExtra("position"),
                    data.getStringExtra("salary"),
                    data.getLongExtra("id", -1),
                    data.getIntExtra("comp_id", c_id)));
            myAdapter.notifyDataSetChanged();
            lvMain.setAdapter(myAdapter);

        }

        public void deleteOnePerson (){
            persons.size();
            Log.d(LOG_TAG, "deleteOnePerson "+persons.get(lvMain.getCheckedItemPosition()).name);
            WorkDB workDB = new WorkDB();
            workDB.delResultsByIDPerson(this.getContext(),persons.get(lvMain.getCheckedItemPosition()).id.toString());
            workDB.delOnePersonByID(this.getContext(),persons.get(lvMain.getCheckedItemPosition()).id.toString());
        }

        public void savePersonsListToDB() {
            Log.d(LOG_TAG, "myAdapter records count = " + myAdapter.getCount());
            Log.d(LOG_TAG, "persons records count = " + persons.size());

            for (int i = 0; i < persons.size(); i++) {
                Log.d(LOG_TAG, "persons record index = " + i);
                Log.d(LOG_TAG, "name = " + persons.get(i).name);
                Log.d(LOG_TAG, "id = " + persons.get(i).id);

                ContentValues cv = new ContentValues();
                cv.put("comp_id", persons.get(i).comp_id);
                cv.put("name", persons.get(i).name);
                cv.put("pos", persons.get(i).position);
                cv.put("sal", persons.get(i).salary);
                WorkDB workDB = new WorkDB();

                if (persons.get(i).id < 0) {
                    Long _id = workDB.insertRecord(getActivity(), TABLE_NAME, cv); //new insert to db and get new _id*/
                    persons.get(i).id = _id;
                } else {
                    workDB.updateRecord(getActivity(), TABLE_NAME, cv, persons.get(i).id.toString());

                }
            }
        }

        public void loadPersonsListFromDB() {
            if (!persons.isEmpty()) return;
            sPref = getActivity().getSharedPreferences("mPref", MODE_PRIVATE); // get preferences
            Integer c_id = sPref.getInt("c_id", -1);
            //WorkDB workDB = new WorkDB();
            //Cursor c = workDB.getData(getActivity(), "select * from "+TABLE_NAME+" WHERE c_id = ?", new String[] {c_id.toString()});
            WorkDB workDB = new WorkDB();
            Cursor c = workDB.getData(getActivity(),
                    "select * from " + TABLE_NAME + " where comp_id=" + c_id, null);
            if (c != null) {
                if (c.getCount() > 0) {
                    c.moveToFirst();
                    for (int i = 0; i < c.getCount(); i++) {

                        Person person = new Person(c.getString(c.getColumnIndex("name")),
                                c.getString(c.getColumnIndex("pos")),

                                //TODO куда деваются копейки?????
                                CurrOps.convertToCurr(c.getString(c.getColumnIndex("sal"))),
                                c.getLong(c.getColumnIndex("_id")),
                                c.getInt(c.getColumnIndex("comp_id")));
                        persons.add(i, person);
                        c.move(1);
                    }
                    fab.show();
                } else fab.hide();
                c.close();
            } else fab.hide();

        }

        public void editPerson() {
            if (lvMain.getCount() > 0) {
                int i = lvMain.getCheckedItemPosition();
                if (i < 0) return;
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

            //** ADS
            //TODO close ad by click
            AdView mAdView = (AdView) rootView.findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);

            saveFragment = (SaveFragment) getFragmentManager().findFragmentByTag("SAVE_FRAGMENT");
            fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
            if (saveFragment != null) {
                persons = saveFragment.getData();
                fab.setTag(saveFragment.getFab_save_tag().toString());
                if (fab.getTag().toString() == Main.FAB_TAG_SAVE)
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_save));
            } else {
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


            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (lvMain.getCount() > 0) {
                        if (fab.getTag() == Main.FAB_TAG_SAVE) {
                            savePersonsListToDB();
                            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_calc));
                            fab.setTag(Main.FAB_TAG_CALC);
                        } else {
                            sPref = getActivity().getSharedPreferences("mPref", MODE_PRIVATE); //get preferences object
                            SharedPreferences.Editor ed = sPref.edit();
                            ed.putString(Main.RESULTS_REQUEST_CALC, Main.RESULTS_CALC); //results request
                            ed.apply(); // save pref

                            Intent intent = new Intent(getActivity(), PersonListActivity.class);
//                            intent.putExtra(Main.RESULTS_REQUEST_CALC, Main.RESULTS_CALC);
                            startActivity(intent);
                        }
                    }
                }
            });

            loadPersonsListFromDB();
            registerForContextMenu(lvMain);

            return rootView;
        }



        /**
         * LOGS
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
            } else if (position == 2) {
                fragmentPers = FragmentPers.newInstance(position + 1);
                return fragmentPers; //FragmentPers.newInstance(position+1);
            } else return PlaceholderFragment.newInstance(position + 1);
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
