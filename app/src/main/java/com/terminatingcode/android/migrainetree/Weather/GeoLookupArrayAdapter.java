package com.terminatingcode.android.migrainetree.Weather;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom Adapter for AutoCompleteTextView
 * Created by Sarah on 6/21/2016.
 */
public class GeoLookupArrayAdapter extends ArrayAdapter<String> implements Filterable {
    private List<String> cities;
    private MFilter filter;

    public GeoLookupArrayAdapter(Context context, int textViewResourceId){
        super(context, textViewResourceId);
        cities = new ArrayList<>();
    }

    @Override
    public void add(String object){
        cities.add(object);
        notifyDataSetChanged();
    }

    @Override
    public void clear() {
        cities.clear();
    }

    @Override
    public int getCount() {
        return cities.size();
    }

    @Override
    public String getItem(int position) {
        return cities.get(position);
    }

    @Override
    public MFilter getFilter() {
        if (filter == null) {
            filter = new MFilter();
        }
        return filter;
    }

    public void callFiltering(String term) {
        filter.performFiltering(term);
    }

    private class MFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null) {
                results.values = cities;
                results.count = cities.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            if (results != null && results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }

}
