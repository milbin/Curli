package com.fitness.curli;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapter extends BaseAdapter {
    // Declare Variables

    Context mContext;
    LayoutInflater inflater;
    private List<SearchResult> namesList = null;
    private ArrayList<SearchResult> arraylist;

    public ListViewAdapter(Context context, List<SearchResult> namesList) {
        mContext = context;
        this.namesList = namesList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<SearchResult>();
        this.arraylist.addAll(namesList);
    }

    public class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() {
        return namesList.size();
    }

    @Override
    public SearchResult getItem(int position) {
        return namesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        SQLData data = new SQLData();
        final ViewHolder holder;
        final ViewHolder holder2;
        if (view == null) {
            holder = new ViewHolder();
            holder2 = new ViewHolder();
            view = inflater.inflate(R.layout.list_view_items, null);
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.name);

            holder2.name = (TextView) view.findViewById(R.id.nameLabel);

            view.setTag(R.id.name, holder);
            view.setTag(R.id.nameLabel, holder2);

        } else {
            holder = (ViewHolder) view.getTag(R.id.name);
            holder2 = (ViewHolder) view.getTag(R.id.nameLabel);
        }
        // Set the results into TextViews
        holder.name.setText(namesList.get(position).getName());
        String name = namesList.get(position).getName();

        if (data.GROUPS.contains(name)){
            holder2.name.setText("Group : ");
            //System.out.println("GROUP -----> "+name);
        }
        else if (data.MUSCLES.contains(name)){
            holder2.name.setText("Muscle : ");
            //System.out.println("MUSCLE -----> "+name);
        }
        else if (data.EXERCISES.contains(name)){
            holder2.name.setText("Exercise : ");
            //System.out.println("EXERCISE --> "+name);
        }

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        namesList.clear();
        if (charText.length() == 0) {
            namesList.addAll(arraylist);
        } else {
            for (SearchResult wp : arraylist) {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    namesList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}