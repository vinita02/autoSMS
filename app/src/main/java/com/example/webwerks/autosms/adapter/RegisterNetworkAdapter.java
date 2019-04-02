package com.example.webwerks.autosms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.webwerks.autosms.R;
import com.example.webwerks.autosms.model.response.NetworkResponse;

import java.util.ArrayList;

public class RegisterNetworkAdapter extends BaseAdapter {
    LayoutInflater inflator;
    ArrayList<NetworkResponse.Operators> operators;


    public RegisterNetworkAdapter(Context context, ArrayList<NetworkResponse.Operators> networkList) {
        inflator = LayoutInflater.from(context);
        this.operators = networkList;
    }

    @Override
    public int getCount()
    {
        return operators.size();
    }

    @Override
    public NetworkResponse.Operators getItem(int position)
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
        convertView = inflator.inflate(R.layout.spinner_network, null);
        TextView tv = (TextView) convertView.findViewById(R.id.tvNetwork);
        tv.setText(operators.get(position).getOperator_name());
        return convertView;
    }
}
