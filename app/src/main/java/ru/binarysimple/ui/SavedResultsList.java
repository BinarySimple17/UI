package ru.binarysimple.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class SavedResultsList extends AppCompatActivity {

    Integer comp_id;
    String month;
    String year;
    ListView lvPeriods;
    ArrayList<Period> periods = new ArrayList<Period>();
    MySimpleArrayAdapterPeriods myAdapter;
    SharedPreferences sPref;
    final String[] monthName = {"январь", "февраль", "март", "апрель", "май", "июнь", "июль", "август", "сентябрь", "октябрь", "ноябрь", "декабрь"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_results_list);
        sPref = getSharedPreferences("mPref", MODE_PRIVATE);
        comp_id = sPref.getInt("c_id", -1);
        lvPeriods = (ListView) findViewById(R.id.lvPeriods);
        myAdapter = new MySimpleArrayAdapterPeriods(this, periods);
        lvPeriods.setAdapter(myAdapter);
        loadPeriods();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_resulst_list);
        toolbar.setTitle(getResources().getString(R.string.title_saved_results) +" "+ sPref.getString("cn",""));

        lvPeriods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences.Editor ed = sPref.edit();
                ed.putString(Main.RESULTS_REQUEST_CALC, Main.RESULTS_LOAD); //results request
                ed.putString(Main.RESULTS_REQUEST_MONTH,periods.get(i).monthIndex);
                ed.putString(Main.RESULTS_REQUEST_YEAR,periods.get(i).yearIndex);
                ed.apply();
                Intent intent = new Intent(getApplication(), PersonListActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadPeriods() {
        if (!periods.isEmpty()) return;
        WorkDB workDB = new WorkDB();
        Cursor c = workDB.getData(this,
                "select comp_id, month, year from " + Main.TABLE_RESULTS +
                        " where comp_id = " + comp_id +
                        " group by month, year order by year, month", null);
        if (c != null) {
            if (c.getCount() > 0) {
                c.moveToFirst();
                for (int i = 0; i < c.getCount(); i++) {
                    Period period = new Period(c.getString(c.getColumnIndex("comp_id")),
                            monthName[Integer.parseInt( c.getString(c.getColumnIndex("month")))],
                            c.getString(c.getColumnIndex("month")),
                            c.getString(c.getColumnIndex("year"))+ " г.",
                            c.getString(c.getColumnIndex("year")));
                    periods.add(i, period);
                    c.move(1);
                }
            }
            c.close();
        }
    }
}
