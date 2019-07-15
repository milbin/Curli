package com.fitness.curli;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapter extends BaseAdapter {
    // Declare Variables

    Context mContext;
    LayoutInflater inflater;
    private List<SearchResult> namesList = null;
    private ArrayList<SearchResult> arraylist;

    public ListViewAdapter(Context context, List<SearchResult> animalNamesList) {
        mContext = context;
        this.namesList = animalNamesList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<SearchResult>();
        this.arraylist.addAll(animalNamesList);
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
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.list_view_items, null);
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(namesList.get(position).getName());
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
