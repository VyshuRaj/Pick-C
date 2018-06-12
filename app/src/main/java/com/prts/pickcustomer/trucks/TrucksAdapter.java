package com.prts.pickcustomer.trucks;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class TrucksAdapter extends RecyclerView.Adapter<TrucksAdapter.ViewHolder> {
    private Context mContext;
    private List<VehicleGroupType> mVehicleTypes;
    private OnItemClick mListener;

    public TrucksAdapter(Context context, List<VehicleGroupType> vehicleTypes, OnItemClick truckCateogeoriesFragment) {
        mContext = context;
        mVehicleTypes = vehicleTypes;
        mVehicleTypes.get(0).setSelected(true);
        mVehicleTypes.get(0).setOpenImages(R.drawable.mini_opened_truck);
        mListener = truckCateogeoriesFragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.trucks_row_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder,final int position) {
        holder.mType.setText(mVehicleTypes.get(position).getLookupCode());
        holder.mImageView.setImageResource(mVehicleTypes.get(position).getOpenImages());
        holder.mTypeDesc.setText(mVehicleTypes.get(position).getDefaultTime());
        holder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(position,holder.mRelativeLayout);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mVehicleTypes==null?0: mVehicleTypes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.miniType)
        TextView mType;
        @BindView(R.id.loadingImageView)
        ImageView mImageView;
        @BindView(R.id.timer)
        TextView mTypeDesc;
        @BindView(R.id.relativeLayout)
        RelativeLayout mRelativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
