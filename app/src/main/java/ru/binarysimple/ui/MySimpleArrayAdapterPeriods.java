package ru.binarysimple.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class MySimpleArrayAdapterPeriods extends ArrayAdapter<Period> {
    private final Context ctx;
    private final LayoutInflater lInflater;
    private final ArrayList<Period> objects;

    MySimpleArrayAdapterPeriods(Context context, ArrayList<Period> periods) {
        super(context, R.layout.item_peroid, periods);
        ctx = context;
        objects = periods;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return objects.size();
    }

    public void delItem(int position) {
        objects.remove(position);
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item_peroid, parent, false);
        }

        Period p = getProduct(position);

        ((TextView) view.findViewById(R.id.tvMonth)).setText(p.month);
        ((TextView) view.findViewById(R.id.tvYear)).setText(p.year);
        return view;
    }


    private Period getProduct(int position) {
        return getItem(position);
    }

}
