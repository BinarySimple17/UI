package ru.binarysimple.ui;

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


import ru.binarysimple.ui.content.PersonContent;

import java.util.ArrayList;
import java.util.List;

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
    SharedPreferences sPref;

    private ArrayList<Result> calcResults() {
        ArrayList<Result> results = new ArrayList<Result>();
        //TODO calc new results
            /*√взять comp_id
            * √взять ставки налогов
            * √по comp_id отобрать сотрудников
            * √по каждому сотруднику создать экземпляр Results
            * √объеднить их в ArrayList<Results>
            * √передать в setupRecyclerView массив результатов
            */
        sPref = getSharedPreferences("mPref", MODE_PRIVATE); // get preferences
        String comp_id = Integer.toString(sPref.getInt("c_id",-1));
        String year = sPref.getString("year", "-1");//TODO add calendar current year into Reslts
        String month = Integer.toString(sPref.getInt("month", -1));//int
        String ndfl = sPref.getString("ndfl", "-1");//
        String ffoms = sPref.getString("ffoms","-1");
        String pfr = sPref.getString("pfr","-1");
        String fss = sPref.getString("fss","-1");
        WorkDB workDB = new WorkDB();
        Cursor c = workDB.getData(this, "select * from "+Main.TABLE_NAME+" WHERE comp_id = ?", new String[] {comp_id.toString()});
        if (c == null) return null;
        c.moveToFirst();
        for (int i=0;i < c.getCount();i++) {
            Result result = new Result.ResultBuilder()
                    .set_id(i)
                    .setId_person(c.getInt(c.getColumnIndex("_id")))
                    .setMonth(Integer.parseInt(month))
                    .setYear(Integer.parseInt(year))
                    .setNdfl(ndfl + " x salary")
                    .setFfoms(ffoms +" x salary")
                    .setPfr(pfr+" x salary")
                    .setFss(fss+" x salary")
                    .setName(c.getString(c.getColumnIndex("name")))
                    .setPosition(c.getString(c.getColumnIndex("pos")))
                    .setSalary(c.getString(c.getColumnIndex("sal")))
                    .setComp_id(comp_id.toString())
                    .build();
            results.add(i,result);
            c.moveToNext();
        }
        return results;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Here must be save all results action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        View recyclerView = findViewById(R.id.person_list);
        assert recyclerView != null;
        //TODO choose start for calc or start for saved
    //    Intent intent = getIntent();
        ArrayList<Result> results = new ArrayList<Result>();
       // if (intent.getStringExtra(Main.RESULTS_REQUEST_CALC).equals(Main.RESULTS_CALC)){
            results = calcResults();
       //     if (results == null) return;
            setupRecyclerViewArray((RecyclerView) recyclerView, results);
       // }
       // else {
            //TODO load from db
        //    setupRecyclerView((RecyclerView) recyclerView);
       // }


        if (findViewById(R.id.person_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        /*ArrayList pers = new ArrayList();
        pers.add("pers araylist");*/
        //PersonContent personContent = new PersonContent();
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(PersonContent.ITEMS));
     //   recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(PersonContent.ITEMS));
    }

    private void setupRecyclerViewArray(@NonNull RecyclerView recyclerView, ArrayList<Result> results) {
        /*ArrayList pers = new ArrayList();
        pers.add("pers araylist");*/
        PersonContent personContent = new PersonContent(results);
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(personContent.ITEMS));
        //   recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(PersonContent.ITEMS));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Result> mValues;

        public SimpleItemRecyclerViewAdapter(List<Result> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.person_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            Integer mId = mValues.get(position).get_id()+1;
            holder.mIdView.setText(mId.toString());
            holder.mContentView.setText(mValues.get(position).getName());

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
            public final TextView mIdView;
            public final TextView mContentView;
            public Result mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}
