package com.prts.pickcustomer.booking;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.prts.pickcustomer.R;
import com.prts.pickcustomer.trucks.OnItemClick;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by LOGICON on 01-01-2018.
 */

public class CargoTypeAdapter extends BaseAdapter {
  private List<CargoType> mCargoTypes;
    private Context mContext;
    OnItemClick mOnItemClick;
    public static int mSelectedPosition;

    CargoTypeAdapter(Context context, List<CargoType> cargoTypes) {
        this.mCargoTypes = cargoTypes;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mCargoTypes==null ?0:mCargoTypes.size();
    }

    @Override
    public CargoType getItem(int position) {
        return mCargoTypes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cargo_type_item, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        String url = mCargoTypes.get(position).getImage();

        if (url.length() > 0) {
            Picasso.with(mContext).load(mCargoTypes.get(position).getImage()).into(viewHolder.imageview);
        }

        viewHolder.typeRadioButton.setText(mCargoTypes.get(position).getDescription());
        viewHolder.typeRadioButton.setTag(mCargoTypes.get(position).getId());

        boolean isChecked=mCargoTypes.get(position).isChecked();
        viewHolder.typeRadioButton.setChecked(isChecked);

        int colorValue=0;

        if (isChecked) {
            colorValue = ContextCompat.getColor(mContext, R.color.appThemeYellow);
        } else {
            colorValue = ContextCompat.getColor(mContext, R.color.appThemeBgColorDark);
        }

        viewHolder.typeRadioButton.setBackgroundColor(colorValue);
        viewHolder.parentLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked=!mCargoTypes.get(position).isChecked();
                mCargoTypes.get(position).setChecked(isChecked);
                mSelectedPosition=position;
                notifyDataSetChanged();
            }
        });
        return view;

    }

    class ViewHolder {
        CheckBox typeRadioButton;
        ImageView imageview;
        LinearLayout parentLay;

        ViewHolder(View view) {
            typeRadioButton = view.findViewById(R.id.checkBox);
            imageview = view.findViewById(R.id.image_id);
            parentLay=view.findViewById(R.id.rootLay);
        }
    }

}