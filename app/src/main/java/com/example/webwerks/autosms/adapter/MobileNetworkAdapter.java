package com.example.webwerks.autosms.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.webwerks.autosms.model.response.NetworkResponse;

import java.util.ArrayList;

public class MobileNetworkAdapter extends ArrayAdapter<NetworkResponse.Operators> {

    ArrayList<NetworkResponse.Operators> operators = new ArrayList<>();

    public MobileNetworkAdapter(Context context, int textViewResourceId, ArrayList<NetworkResponse.Operators> operators) {
        super(context, textViewResourceId, operators);
        this.operators = operators;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        String name = operators.get(position).getOperator_name();
        Log.d("names",name);
        if (view instanceof TextView) {
            ((TextView) view).setText(name);
        }
        return view;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View row = super.getView(position, convertView, parent);
        String name = operators.get(position).getOperator_name();
        Log.d("TAGA",name);
        if (row instanceof TextView) {
            ((TextView) row).setText(name);
        }
        return (row);
    }
}

