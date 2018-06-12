package com.prts.pickcustomer.trucks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.prts.pickcustomer.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LOGICON on 19-12-2017.
 */

public class TrucksGridAdapter extends BaseAdapter {
    private Context mContext;
    private List<VehicleGroupType> mVehicleTypes;
    private OnItemClick mListener;

    public TrucksGridAdapter(Context context, List<VehicleGroupType> vehicleTypes, OnItemClick onItemClick) {
        mContext = context;
        mVehicleTypes = vehicleTypes;
        mVehicleTypes.get(0).setSelected(true);
        mVehicleTypes.get(0).setOpenImages(R.drawable.mini_opened_truck);
        mListener = onItemClick;
    }


    @Override
    public int getCount() {
        return mVehicleTypes == null ? 0 : mVehicleTypes.size();
    }

    @Override
    public VehicleGroupType getItem(int position) {
        return mVehicleTypes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        final ViewHolder viewHolder;

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.trucks_row_item, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.mType.setText(mVehicleTypes.get(position).getLookupCode());
        viewHolder.mImageView.setImageResource(mVehicleTypes.get(position).getOpenImages());
        viewHolder.mTypeDesc.setText(mVehicleTypes.get(position).getDefaultTime());
        viewHolder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(position, viewHolder.mRelativeLayout);
                notifyDataSetChanged();
            }
        });

        return view;
    }

    class ViewHolder {
        @BindView(R.id.miniType)
        TextView mType;
        @BindView(R.id.loadingImageView)
        ImageView mImageView;
        @BindView(R.id.timer)
        TextView mTypeDesc;
        @BindView(R.id.relativeLayout)
        RelativeLayout mRelativeLayout;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
