package com.example.michal.app100;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class CustomListAdapter extends BaseAdapter {
    private Context context;
    private List<Report> mReportList;

    public CustomListAdapter(Context context, List<Report> mReportList) {
        this.context = context;
        this.mReportList = mReportList;
    }


    @Override
    public int getCount() {
        return mReportList.size();
    }

    @Override
    public Object getItem(int position) {
        return mReportList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.list_row_layout,null);
        TextView REvent = (TextView)v.findViewById(R.id.reportEvent);
        TextView RStatus = (TextView)v.findViewById(R.id.status);
        TextView RDate = (TextView)v.findViewById(R.id.date);
        //set text for TextView
        REvent.setText(mReportList.get(position).eventType);
        RStatus.setText("סטטוס:"  + mReportList.get(position).status);
        RDate.setText(mReportList.get(position).reportDate);
        return v;
    }
}