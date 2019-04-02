package com.example.webwerks.autosms.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.webwerks.autosms.R;
import com.example.webwerks.autosms.model.response.NetworkResponse;
import com.example.webwerks.autosms.model.response.ViewProfileResponse;

import java.util.ArrayList;

public class ProfileNetworkAdapter extends BaseAdapter {
    LayoutInflater inflator;
    ArrayList<ViewProfileResponse.Operators> operators;


    public ProfileNetworkAdapter(Context context, ArrayList<ViewProfileResponse.Operators> networkList) {
        inflator = LayoutInflater.from(context);
        this.operators = networkList;
    }

    @Override
    public int getCount()
    {
        return operators.size();
    }

    @Override
    public ViewProfileResponse.Operators getItem(int position)
    {
        return operators.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Log.d("TAGAA", String.valueOf(position));
        convertView = inflator.inflate(R.layout.spinner_network, null);
        TextView tv = (TextView) convertView.findViewById(R.id.tvNetwork);
        tv.setText(operators.get(position).getOperator_name());
        return convertView;
    }
}
