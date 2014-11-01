package com.example.william.courserater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by William on 2014-11-01.
 */
public class MyAdapter extends ArrayAdapter<Item> {

    private final Context context;
    private final ArrayList<Item> itemsArrayList;

    public MyAdapter(Context context, ArrayList<Item> itemsArrayList) {

        super(context, R.layout.listview_information_row, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.listview_information_row, parent, false);
        // 3. Get the two text view from the rowView
        TextView labelView = (TextView) rowView.findViewById(R.id.informationLabel);
        TextView valueView = (TextView) rowView.findViewById(R.id.informationValue);
        // 4. Set the text for textView
        labelView.setText(itemsArrayList.get(position).getLabel());
        valueView.setText(itemsArrayList.get(position).getValue());

        return rowView;
    }
}