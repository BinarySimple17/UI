package ru.binarysimple.ui;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MySimpleArrayAdapter extends ArrayAdapter<Person> {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Person> objects;

    MySimpleArrayAdapter(Context context, ArrayList<Person> products) {
        super (context,R.layout.item,products);
        ctx = context;
        objects = products;
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
            view = lInflater.inflate(R.layout.item, parent, false);
        }

        Person p = getProduct(position);

        ((TextView) view.findViewById(R.id.tvName)).setText(p.name);
        ((TextView) view.findViewById(R.id.tvPosition)).setText(p.position);
        ((TextView) view.findViewById(R.id.tvSalary)).setText(p.salary);
        return view;
    }


    Person getProduct(int position) {
        return getItem(position);
    }

}
