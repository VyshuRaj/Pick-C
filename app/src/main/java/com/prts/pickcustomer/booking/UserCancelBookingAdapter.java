package com.prts.pickcustomer.booking;

import android.content.Context;
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
import com.prts.pickcustomer.userrating.Feedback;

import java.util.List;

/**
 * Created by LOGICON on 02-01-2018.
 */

public class UserCancelBookingAdapter  extends BaseAdapter {
    private Context mContext;
    private List<Feedback> mFeedbackList;

    public UserCancelBookingAdapter(Context context,List<Feedback> feedbacks){
        mContext = context;
        mFeedbackList = feedbacks;
    }
    @Override
    public int getCount() {
        return mFeedbackList.size();
    }

    @Override
    public Object getItem(int i) {
        return mFeedbackList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        View view1 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_cancel_remark, viewGroup, false);
        PickCCustomTextVIew cancelRemarksTV = (PickCCustomTextVIew) view1.findViewById(R.id.cancel_remarks_tv);
        RelativeLayout cancelLL = (RelativeLayout) view1.findViewById(R.id.cancel_remarks_ll);
        CheckBox checkBox=view1.findViewById(R.id.checkBox);

        int colorValue=0;
        if(mFeedbackList.get(position).isSelected()){
            checkBox.setChecked(true);
            colorValue=mContext.getResources().getColor(R.color.appThemeYellow);
        }else{
            checkBox.setChecked(false);
          //  colorValue=mContext.getResources().getColor(R.color.appThemeBgColorDark);
        }

        cancelLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean is=mFeedbackList.get(position).isSelected();
                mFeedbackList.get(position).setSelected(!is);
                notifyDataSetChanged();
            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                boolean is=mFeedbackList.get(position).isSelected();
                mFeedbackList.get(position).setSelected(!is);
                notifyDataSetChanged();
            }
        });

        cancelRemarksTV.setText(mFeedbackList.get(position).getText());
        return view1;
    }
}