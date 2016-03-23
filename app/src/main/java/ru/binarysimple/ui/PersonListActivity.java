package ru.binarysimple.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import ru.binarysimple.ui.content.PersonContent;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

/**
 * An activity representing a list of Persons. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link PersonDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class PersonListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private SharedPreferences sPref;
    private ArrayList<Result> results = new ArrayList<Result>();
    // --Commented out by Inspection (17.03.2016 15:51):SaveFragmentPersonList saveFragmentPersonList;

    private ArrayList<Result> calcResults() { //load c_id, year and month from sPref
        ArrayList<Result> results = new ArrayList<Result>();
        sPref = getSharedPreferences("mPref", MODE_PRIVATE); // get preferences
        String comp_id = Integer.toString(sPref.getInt("c_id", -1));
        String year = sPref.getString("year", "-1");
        String month = Integer.toString(sPref.getInt("month", -1));//int
        String ndfl = sPref.getString("ndfl", getResources().getString(R.string.par_ndfl_hint));//
        String ffoms = sPref.getString("ffoms", getResources().getString(R.string.par_ffoms_hint));
        String pfr = sPref.getString("pfr", getResources().getString(R.string.par_pfr_hint));
        String fss = sPref.getString("fss", getResources().getString(R.string.par_fss_hint));

        Currency curr = Currency.getInstance(Locale.getDefault());
        WorkDB workDB = new WorkDB();
        Cursor c = workDB.getData(this, "select * from " + Main.TABLE_NAME + " WHERE comp_id = ?",
                new String[]{comp_id.toString()});
        if (c == null) return null;
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            Result result = new Result.ResultBuilder()
                    .set_id(i)
                    .setId_person(c.getLong(c.getColumnIndex("_id")))
                    .setMonth(Integer.parseInt(month))
                    .setYear(Integer.parseInt(year))
                    .setNdfl(CurrOps.mult(curr, ndfl, c.getString(c.getColumnIndex("sal"))))
                    .setFfoms(CurrOps.mult(curr, ffoms, c.getString(c.getColumnIndex("sal"))))
                    .setPfr(CurrOps.mult(curr, pfr, c.getString(c.getColumnIndex("sal"))))
                    .setFss(CurrOps.mult(curr, fss, c.getString(c.getColumnIndex("sal"))))
                    .setName(c.getString(c.getColumnIndex("name")))
                    .setPosition(c.getString(c.getColumnIndex("pos")))
                    .setSalary(c.getString(c.getColumnIndex("sal")))
                    .setComp_id(comp_id.toString())
                    .build();
            results.add(i, result);
            c.moveToNext();
        }
        return results;
    }

    private ArrayList<Result> loadResults(String comp_id, String year, String month) { // get c_id, year, month from params
        ArrayList<Result> results = new ArrayList<Result>();
        sPref = getSharedPreferences("mPref", MODE_PRIVATE); // get preferences

        WorkDB workDB = new WorkDB();
        Cursor c = workDB.getData(this, "select * from " + Main.TABLE_RESULTS + " WHERE comp_id = ? and " +
                "month = ? and year = ?", new String[]{comp_id, month, year});
        if (c == null) return null;
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            Result result = new Result.ResultBuilder()
                    .set_id(i)
                    .setId_person(c.getLong(c.getColumnIndex("id_person")))
                    .setMonth(Integer.parseInt(month))
                    .setYear(Integer.parseInt(year))
                    .setNdfl(c.getString(c.getColumnIndex("ndfl")))
                    .setFfoms(c.getString(c.getColumnIndex("ffoms")))
                    .setPfr(c.getString(c.getColumnIndex("pfr")))
                    .setFss(c.getString(c.getColumnIndex("fss")))
                    .setName(c.getString(c.getColumnIndex("name")))
                    .setPosition(c.getString(c.getColumnIndex("position")))
                    .setSalary(c.getString(c.getColumnIndex("salary")))
                    .setComp_id(comp_id)
                    .build();
            results.add(i, result);
            c.moveToNext();
        }
        return results;
    }

    private boolean startedForCalc() {
        sPref = getSharedPreferences("mPref", MODE_PRIVATE); // get preferences
        String request = sPref.getString(Main.RESULTS_REQUEST_CALC, Main.RESULTS_CALC);
        return request.equals(Main.RESULTS_CALC);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        //** ADS
        //TODO close ad by click
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        View recyclerView = findViewById(R.id.person_list);
        assert recyclerView != null;

        boolean startedForCalc = startedForCalc();

        if (startedForCalc) {
            results = calcResults();
        } else {
            //started for load results from DB
            //TODO load from db. remove loading from sPref in future
            //TODO change getting month and year to RESULTS_REQUEST_YEAR and RESULTS_REQUEST_MONTH
            sPref = getSharedPreferences("mPref", MODE_PRIVATE); // get preferences
            String comp_id = Integer.toString(sPref.getInt("c_id", -1));
            String year = sPref.getString(Main.RESULTS_REQUEST_YEAR, "-1");
            String month = sPref.getString(Main.RESULTS_REQUEST_MONTH, "-1");//int
            this.setTitle(R.string.title_saved_person_list);
            results = loadResults(comp_id, year, month);
        }
        setupRecyclerViewArray((RecyclerView) recyclerView, results);

        if (findViewById(R.id.person_detail_container) != null) {
            mTwoPane = true;
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (results.size() < 1) {
            fab.hide();
            Snackbar.make(recyclerView, getResources().getString(R.string.no_data), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        if (startedForCalc) { //started for calc new results. Button onClick - save results to DB
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (results.size() < 1) return;
                    WorkDB workDB_res = new WorkDB();
                    workDB_res.delResults(view.getContext(), results.get(0).getComp_id(),
                            results.get(0).getMonth().toString(), results.get(0).getYear().toString());

                    ContentValues cv = new ContentValues();
                    for (int i = 0; i < results.size(); i++) {
                        cv.put("id_person", results.get(i).getId_person());
                        cv.put("month", results.get(i).getMonth());
                        cv.put("year", results.get(i).getYear());
                        cv.put("ndfl", results.get(i).getNdfl());
                        cv.put("ffoms", results.get(i).getFfoms());
                        cv.put("pfr", results.get(i).getPfr());
                        cv.put("fss", results.get(i).getFss());
                        cv.put("name", results.get(i).getName());
                        cv.put("position", results.get(i).getPosition());
                        cv.put("salary", results.get(i).getSalary());
                        cv.put("comp_id", results.get(i).getComp_id());
                        WorkDB workDB = new WorkDB();
                        workDB.insertRecordOnConflict(view.getContext(), Main.TABLE_RESULTS, cv);
                        cv.clear();
                    }
                    Snackbar.make(view,  getResources().getString(R.string.results_is_saved), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        } else {
            // if pressed for load results. Button - clear results from DB
            /*fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Результаты должны быть очищены?", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });*/
            fab.hide();
        }
    }

// --Commented out by Inspection START (17.03.2016 15:51):
//    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
//        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(PersonContent.ITEMS));
//    }
// --Commented out by Inspection STOP (17.03.2016 15:51)

    private void setupRecyclerViewArray(@NonNull RecyclerView recyclerView, ArrayList<Result> results) {
        PersonContent personContent = new PersonContent(results);
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(personContent.ITEMS));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Result> mValues;

        public SimpleItemRecyclerViewAdapter(List<Result> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            /*View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.person_list_content, parent, false);*/
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_res, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
          //  Integer mId = mValues.get(position).get_id() + 1;
          //  holder.mIdView.setText(mId.toString());
          //  holder.mContentView.setText(mValues.get(position).getName());
            Currency curr = Currency.getInstance(Locale.getDefault());

            holder.resFIO.setText(mValues.get(position).getName());
            holder.resPosition.setText(mValues.get(position).getPosition());
//            CurrOps.sub(curr, mValues.get(position).getSalary(), mValues.get(position).getNdfl());
            holder.resSalaryNET.setText(CurrOps.sub(curr, mValues.get(position).getSalary(),
                                                    mValues.get(position).getNdfl()));

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(PersonDetailFragment.ARG_ITEM_ID, holder.mItem.get_id().toString());
                        PersonDetailFragment fragment = new PersonDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.person_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, PersonDetailActivity.class);
                        intent.putExtra(PersonDetailFragment.ARG_ITEM_ID, holder.mItem.get_id().toString());
                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

            public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView resFIO;
            public final TextView resPosition;
            public final TextView resSalaryNET;
            public Result mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                resFIO = (TextView) view.findViewById(R.id.resFIO);
                resPosition = (TextView) view.findViewById(R.id.resPosition);
                resSalaryNET = (TextView) view.findViewById(R.id.resSalaryNET);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + resFIO.getText() + "'";
            }
        }
    }
}
