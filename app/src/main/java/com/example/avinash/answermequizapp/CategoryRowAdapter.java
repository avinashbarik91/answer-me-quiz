package com.example.avinash.answermequizapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class CategoryRowAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Category> data;
    private static LayoutInflater inflater = null;

    public CategoryRowAdapter(ArrayList<Category> data, Context context) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.activity_category_row_adapter, null);

        TextView text = (TextView) vi.findViewById(R.id.categoryNameTextView);
        text.setText(data.get(position).getName());

        ImageView moviePoster = (ImageView) vi.findViewById(R.id.categoryImageView);
        moviePoster.setImageDrawable(data.get(position).getImageName());

        return vi;
    }
}
