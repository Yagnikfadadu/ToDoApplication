package com.tasklist.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Calendar;

public class MainAdapter extends BaseAdapter
{
    Context context;
    LayoutInflater inflater;
    ArrayList<String> title;
    ArrayList<String> time;
    ArrayList<String> status;


    public MainAdapter(Context context,ArrayList<String> title,ArrayList<String> time,ArrayList<String> status)
    {
        this.context = context;
        this.title = title;
        this.time = time;
        this.status = status;
    }


    @Override
    public int getCount() {
        return title.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @SuppressLint({"ViewHolder", "InflateParams", "SetTextI18n"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.activity_custom_list_view2,null,true);
        TextView vTitle,vTime;
        RadioButton radioButton;
        vTitle = convertView.findViewById(R.id.list_title);
        vTime = convertView.findViewById(R.id.list_time);
        MaterialCardView materialCardView = convertView.findViewById(R.id.card_view);
        radioButton = convertView.findViewById(R.id.list_radio);
        vTitle.setText(title.get(position).substring(0,1).toUpperCase()+title.get(position).substring(1));
        vTime.setText(time.get(position));
        String isCompleted = status.get(position);
        isCompleted = isCompleted.toLowerCase();
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        String stringTime = time.get(position);
        String[] dateTime = stringTime.split(" ");
        String[] pureDate = dateTime[0].split("/");
        String[] pureTime = dateTime[1].split(":");
        int y = Integer.parseInt(pureDate[0]);
        int m = Integer.parseInt(pureDate[1]);
        int d = Integer.parseInt(pureDate[2]);

        int hr = Integer.parseInt(pureTime[0]);
        int min = Integer.parseInt(pureTime[1]);

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int mouth = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DATE);
        int hour = cal.get(Calendar.HOUR);
        int minute = cal.get(Calendar.MINUTE);

        if(isCompleted.equals("true"))
        {
            radioButton.setSelected(true);
            vTitle.setTextColor(Color.parseColor("#FF397A"));
            vTime.setTextColor(Color.parseColor("#FF397A"));
            radioButton.setChecked(true);
            @SuppressLint("UseCompatLoadingForDrawables") Drawable drawable = context.getResources().getDrawable(R.drawable.ic_baseline_verified_24);
            vTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,drawable,null);

        }

        radioButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v)
            {
                boolean isSelected = radioButton.isSelected();
                if (isSelected)
                {
                    dataBaseHelper.setPending(title.get(position));
                    radioButton.setSelected(false);
                    radioButton.setChecked(false);
                }
                else
                {
                    dataBaseHelper.setCompleted(title.get(position));
                    radioButton.setSelected(true);
                    radioButton.setChecked(true);
                }
                if (context instanceof MainActivity) {
                    ((MainActivity)context).fetchCustomData();
                }
                return true;
            }
        });
        radioButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               boolean isSelected = radioButton.isSelected();
               if (isSelected)
               {
                   dataBaseHelper.setPending(title.get(position));
                   radioButton.setSelected(false);
                   radioButton.setChecked(false);
               }
               else
               {
                   dataBaseHelper.setCompleted(title.get(position));
                   radioButton.setSelected(true);
                   radioButton.setChecked(true);
               }
                if (context instanceof MainActivity) {
                    ((MainActivity)context).fetchCustomData();
                }
            }
        });

        materialCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(context);
                materialAlertDialogBuilder.setTitle("Select Operation");
                materialAlertDialogBuilder.setMessage(title.get(position).substring(0,1).toUpperCase()+title.get(position).substring(1));
                materialAlertDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataBaseHelper dataBaseHelper1 = new DataBaseHelper(context);
                        dataBaseHelper1.deleteTask(title.get(position));
                        if (context instanceof MainActivity) {
                            ((MainActivity)context).fetchCustomData();
                        }
                    }
                });

                materialAlertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                materialAlertDialogBuilder.show();

                return true;
            }
        });


        return convertView;
    }
}
