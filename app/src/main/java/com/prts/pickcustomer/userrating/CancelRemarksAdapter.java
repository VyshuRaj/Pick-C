package com.prts.pickcustomer.userrating;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.prts.pickcustomer.R;
import com.prts.pickcustomer.customviews.PickCCustomTextVIew;
import com.prts.pickcustomer.trucks.OnItemClick;

import java.util.List;

/**
 * Created by satya on 30-Dec-17.
 */

class CancelRemarksAdapter extends BaseAdapter {

    private List<Feedback> mFeedback;
    private OnItemClick mListener;
    private Context mContext;

    CancelRemarksAdapter(List<Feedback> feedback, OnItemClick onItemClick, Context context) {
        mFeedback = feedback;
        mListener=onItemClick;
        mContext=context;
    }

    @Override
    public int getCount() {
        return mFeedback.size();
    }

    @Override
    public Object getItem(int i) {
        return mFeedback.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder")
        View view1 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cancel_remarks_sub_layout, viewGroup, false);
        PickCCustomTextVIew cancelRemarksTV =  view1.findViewById(R.id.cancel_remarks_tv);
        CheckBox checkBox=view1.findViewById(R.id.checkBox);
        final RelativeLayout linearLayout=view1.findViewById(R.id.mainLL);
        cancelRemarksTV.setText(mFeedback.get(position).getText());


        if(mFeedback.get(position).isSelected){
            checkBox.setChecked(true);
           // cancelRemarksTV.setBackgroundColor(ContextCompat.getColor(mContext,R.color.appThemeYellow));
        }else{
            checkBox.setChecked(false);
            //cancelRemarksTV.setBackgroundColor(ContextCompat.getColor(mContext,R.color.appThemeBgColorDark));
        }

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean fee=!mFeedback.get(position).isSelected();
                mFeedback.get(position).setSelected(fee);
                notifyDataSetChanged();
                mListener.onItemClick(position,linearLayout);
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean fee=!mFeedback.get(position).isSelected();
                mFeedback.get(position).setSelected(fee);
                notifyDataSetChanged();
                mListener.onItemClick(position,linearLayout);
            }
        });

        return view1;
    }
}