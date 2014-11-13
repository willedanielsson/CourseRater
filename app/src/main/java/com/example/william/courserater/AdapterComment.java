package com.example.william.courserater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by William on 2014-11-10.
 */
public class AdapterComment extends ArrayAdapter<ItemComment>{
    private final Context context;
    private final ArrayList<ItemComment> itemCommentArrayList;


    public AdapterComment(Context context, ArrayList<ItemComment> itemCommentArrayList) {

        super(context, R.layout.listview_comment_row, itemCommentArrayList);
        this.context = context;
        this.itemCommentArrayList=itemCommentArrayList;
    }

    @Override
    public boolean isEnabled(int position) {
        // TODO Auto-generated method stub

        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rootView = inflater.inflate(R.layout.listview_comment_row, parent, false);

        TextView yearView = (TextView) rootView.findViewById(R.id.comment_year_text_view);
        TextView commentView = (TextView) rootView.findViewById(R.id.comment_comment_text_view);

        yearView.setText(itemCommentArrayList.get(position).getYear());
        commentView.setText(itemCommentArrayList.get(position).getComment());

        return rootView;
    }
}
