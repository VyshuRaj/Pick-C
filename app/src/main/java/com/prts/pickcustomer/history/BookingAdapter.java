package com.prts.pickcustomer.history;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.prts.pickcustomer.R;
import com.prts.pickcustomer.trucks.OnItemClick;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;
import static com.prts.pickcustomer.helpers.Constants.STATUS_CANCELLED;
import static com.prts.pickcustomer.helpers.Constants.STATUS_COMPLETED;
import static com.prts.pickcustomer.helpers.Constants.STATUS_CONFIRMED;
import static com.prts.pickcustomer.helpers.Constants.STATUS_NOT_SPECIFIED;
import static com.prts.pickcustomer.helpers.Constants.STATUS_PENDING;
import static com.prts.pickcustomer.helpers.Constants.VEHICLE_TYPE_CLOSED;
import static com.prts.pickcustomer.helpers.Constants.VEHICLE_TYPE_OPEN;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {
    private List<HistoryData> mBokingHistoryData;
    private Context mContext;
    private String vehicleGroupText;
    private String vehicleTypeText;
    private OnItemClick mListener;

    BookingAdapter(Context context, List<HistoryData> bookData, OnItemClick onItemClick) {
        mBokingHistoryData = bookData;
        mContext = context;
        mListener = onItemClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final HistoryData bookingData = mBokingHistoryData.get(position);
        holder.dateAndTimeTV.setText(bookingData.getBookingDate());


        if (bookingData.isComplete()) {
            bindStatusToTV(STATUS_COMPLETED, holder.statusTV, holder.completedStatusIV);
        } else if (bookingData.isCancel()) {
            bindStatusToTV(STATUS_CANCELLED, holder.statusTV, holder.completedStatusIV);
        } else if (bookingData.isConfirm()) {
            bindStatusToTV(STATUS_CONFIRMED, holder.statusTV, holder.completedStatusIV);
        } else {
            bindStatusToTV(STATUS_NOT_SPECIFIED, holder.statusTV, holder.completedStatusIV);
            // Todo date and Time
            long currentTimeInmilliseconds = Calendar.getInstance().getTimeInMillis();
            long requiredBookingDateAndTimeInLiiseconds = getDateAndTimeInMilliSeconds(bookingData.getRequiredDate());

            if (requiredBookingDateAndTimeInLiiseconds <= currentTimeInmilliseconds) {
                holder.dateAndTimeTV.setText(bookingData.getBookingDate());
                holder.cancelBookingTV.setVisibility(View.GONE);
            } else {
                bindStatusToTV(STATUS_PENDING, holder.statusTV, holder.completedStatusIV);
                holder.dateAndTimeTV.setText(bookingData.getRequiredDate());
                holder.cancelBookingTV.setVisibility(View.VISIBLE);
            }
        }

        holder.dateAndTimeTV.setText(changeDateAndTimeFormat(holder.dateAndTimeTV.getText().toString()));
        try {
            try {
                Log.e(TAG, "AvgRating" + bookingData.getAvgDriverRating());
            } catch (Exception e) {
                e.printStackTrace();
            }

            String rating = String.valueOf(bookingData.getDriverRating());
            holder.mRattingValue.setText(rating);
            holder.ratingBar.setRating(Float.parseFloat(rating));
            holder.ratingBar.setOnTouchListener(null);
            holder.ratingBar.setOnClickListener(null);
            holder.ratingBar.setOnRatingBarChangeListener(null);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        holder.bookingNoTV.setText(bookingData.getBookingNo());
        holder.tripAmountTV.setText(String.valueOf("₹ " + bookingData.getTripAmount()));
        holder.mCargoTypes.setText(bookingData.getCargoDescription());
        String pick = bookingData.getLocationFrom();
        String drop = bookingData.getLocationTo();
        holder.fromLocationTV.setText(pick);
        holder.toLocationTV.setText(drop);
        bindVehicleImageToIVandCargoDescTV(holder.vehicleDesTV, bookingData.getVehicleGroup(), bookingData.getVehicleType(), holder.vehicleIV);

        Log.e(TAG, "IsPaid" + mBokingHistoryData.get(position).isPaid());

        if (mBokingHistoryData.get(position).isPaid())
            holder.completedStatusIV.setImageResource(R.drawable.paid_stamp);
        else {
            if (mBokingHistoryData.get(position).isCancel())
                holder.completedStatusIV.setImageResource(R.drawable.cancelled_stamp);
            else {
                holder.completedStatusIV.setVisibility(View.GONE);
            }
        }

        holder.cancelBookingTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(position, holder.cancelBookingTV);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mListener.onItemClick(position, holder.itemView);
            }
        });
    }

    private void bindVehicleImageToIVandCargoDescTV(TextView vehicleDescTV, int vehicleGroup, int vehicleType, ImageView vehicleImageView) {
        vehicleImageView.setImageResource(getTruckSelectedIcon(vehicleGroup, vehicleType));
        vehicleDescTV.setText(String.format("%s %s Truck", vehicleGroupText, vehicleTypeText));
    }

    private String changeDateAndTimeFormat(String dateAndTime) {

        dateAndTime = dateAndTime.replace("T", " ");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        SimpleDateFormat sdf1 = new SimpleDateFormat("EEE MMM dd HH:mm a yyyy");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf1 = new SimpleDateFormat("      EEE, MMM dd, yyyy HH:mm a");
        try {
            Date mDate = sdf.parse(dateAndTime);
            dateAndTime = sdf1.format(mDate);
            System.out.println(dateAndTime + " Date in milli :: ");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateAndTime;
    }

    private int getTruckSelectedIcon(int vehicleGroupId, int vehicleTypeId) {
        switch (vehicleGroupId) {
            case 1000:
                vehicleGroupText = "700kgs - Mini -";
                switch (vehicleTypeId) {
                    case VEHICLE_TYPE_OPEN:
                        vehicleTypeText = "Open";
                        return R.drawable.mini_opened_truck;
                    case VEHICLE_TYPE_CLOSED:
                        vehicleTypeText = "Closed";
                        return R.drawable.mini_closed_truck;
                }
                return R.drawable.mini_opened_truck;
            case 1001:
                vehicleGroupText = "1030kgs - Small -";
                switch (vehicleTypeId) {
                    case VEHICLE_TYPE_OPEN:
                        vehicleTypeText = "Open";
                        return R.drawable.small_open_truck;
                    case VEHICLE_TYPE_CLOSED:
                        vehicleTypeText = "Closed";
                        return R.drawable.small_closed_truck;
                }
                return R.drawable.small_open_truck;
            case 1002:
                vehicleGroupText = "1250kgs - Medium -";
                switch (vehicleTypeId) {
                    case VEHICLE_TYPE_OPEN:
                        vehicleTypeText = "Open";
                        return R.drawable.meduim_open_truck;
                    case VEHICLE_TYPE_CLOSED:
                        vehicleTypeText = "Closed";
                        return R.drawable.medium_closed_truck;
                }
                return R.drawable.meduim_open_truck;
            case 1003:
                vehicleGroupText = "2700kgs - Large -";
                switch (vehicleTypeId) {
                    case VEHICLE_TYPE_OPEN:
                        vehicleTypeText = "Open";
                        return R.drawable.large_open_truck;
                    case VEHICLE_TYPE_CLOSED:
                        vehicleTypeText = "Closed";
                        return R.drawable.large_closed_truck;
                }
                return R.drawable.large_open_truck;
        }
        return R.drawable.small_open_truck;
    }

    private void bindStatusToTV(String status, TextView statusTextView, ImageView completedStatusIV) {
        statusTextView.setVisibility(View.GONE);

        switch (status) {
            case STATUS_COMPLETED:
                statusTextView.setText(STATUS_COMPLETED);
                //statusTextView.setVisibility(View.VISIBLE);
                statusTextView.setTextColor(ContextCompat.getColor(mContext, R.color.colorCompletedStatus));
                break;
            case STATUS_CANCELLED:
                statusTextView.setText(STATUS_CANCELLED);
                // statusTextView.setVisibility(View.VISIBLE);
                completedStatusIV.setImageResource(R.drawable.cancelled_stamp);
                statusTextView.setTextColor(ContextCompat.getColor(mContext, R.color.colorCancelledStatus));
                break;
            case STATUS_CONFIRMED:
                statusTextView.setText(STATUS_CONFIRMED);
                //statusTextView.setVisibility(View.VISIBLE);
                statusTextView.setTextColor(ContextCompat.getColor(mContext, R.color.colorConfirmedStatus));
                break;
            case STATUS_PENDING:
                statusTextView.setText(STATUS_PENDING);
                statusTextView.setTextColor(ContextCompat.getColor(mContext, R.color.colorPendingStatus));
                break;
            default:
                statusTextView.setVisibility(View.GONE);
        }
    }

    private long getDateAndTimeInMilliSeconds(String dateAndTime) {
        dateAndTime = dateAndTime.replace("T", " ");
        long timeInMilliseconds = 0;

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date mDate = sdf.parse(dateAndTime);
            timeInMilliseconds = mDate.getTime();
            System.out.println(dateAndTime + " Date in milli :: " + timeInMilliseconds);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeInMilliseconds;
    }

    @Override
    public int getItemCount() {
        return mBokingHistoryData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.vehicle_image_IV)
        ImageView vehicleIV;
        @BindView(R.id.driverIconIV)
        ImageView driverIV;
        @BindView(R.id.completedStatusIV)
        ImageView completedStatusIV;
        @BindView(R.id.rating_bar)
        RatingBar ratingBar;
        @BindView(R.id.ratingText)
        TextView mRattingValue;
        @BindView(R.id.booking_status_TV)
        TextView statusTV;
        @BindView(R.id.booking_date_time_TV)
        TextView dateAndTimeTV;
        @BindView(R.id.vehicle_group_type_TV)
        TextView vehicleDesTV;
        @BindView(R.id.booking_no_TV)
        TextView bookingNoTV;
        @BindView(R.id.from_location_address_TV)
        TextView fromLocationTV;
        @BindView(R.id.to_location_address_TV)
        TextView toLocationTV;
        @BindView(R.id.total_fare_TV)
        TextView fareTV;
        @BindView(R.id.tripAmountTV)
        TextView tripAmountTV;
        @BindView(R.id.cancel_booking_TV)
        TextView cancelBookingTV;
        @BindView(R.id.cargo_types)
        TextView mCargoTypes;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
