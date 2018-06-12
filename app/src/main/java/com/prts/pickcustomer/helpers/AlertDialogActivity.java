package com.prts.pickcustomer.helpers;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.prts.pickcustomer.R;

public class AlertDialogActivity extends AppCompatActivity {
    private static final String TITLE = "title";
    private static final String MESSAGE = "message";
    private static final String POSITIVE_BTN_TEXT = "positive_btn_text";
    private static final String NEGATIVE_BTN_TEXT = "negative_btn_text";
    private static final String TAG = "AlertDialogActivity";
    public static boolean active = false;

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        try {
            Window window = this.getWindow();
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }


        try {
            setContentView(R.layout.activity_alert_dialog);
        } catch (Exception e) {
            Log.e(TAG, "Exception "+e.getMessage());
        }
        this.setFinishOnTouchOutside(false);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Button positiveBtn = (Button) findViewById(R.id.okButton2);
        Button negativeBtn = (Button) findViewById(R.id.cancelButton3);
        TextView messageTV = (TextView) findViewById(R.id.messageTV);
        TextView titleTV = (TextView) findViewById(R.id.titleTV);

        positiveBtn.setVisibility(View.GONE);
        negativeBtn.setVisibility(View.GONE);

        try {
            Intent intent = getIntent();
            String title = intent.getStringExtra(TITLE);
            String message = intent.getStringExtra(MESSAGE);
            String positiveBtnTxt = intent.getStringExtra(POSITIVE_BTN_TEXT);
            String negativeBtnTxt = intent.getStringExtra(NEGATIVE_BTN_TEXT);

            if (!titleTV.getText().toString().equals("TextView")) {
                finish();
                showAlertDialogActivity(AlertDialogActivity.this, title, message, positiveBtnTxt, negativeBtnTxt, positiveBtnClickListener, negativeBrnClickListener);
                return;
            }
            titleTV.setText(title);
            messageTV.setText(message);

            if (positiveBtnTxt != null) {
                positiveBtn.setVisibility(View.VISIBLE);
                positiveBtn.setText(positiveBtnTxt);
                positiveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (positiveBtnClickListener != null) {
                            Log.d(TAG, "positive onClick: progress");
                            positiveBtnClickListener.onPositiveBtnClick();
                        }
                        finish();
                    }
                });
            }
            if (negativeBtnTxt != null) {
                negativeBtn.setVisibility(View.VISIBLE);
                negativeBtn.setText(negativeBtnTxt);
                negativeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (negativeBrnClickListener != null) {
                            Log.d(TAG, "negative onClick: progress");
                            negativeBrnClickListener.onNegativeBtnClick();
                        }
                        finish();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showAlertDialogActivity(Context context, String title, String message, String positiveBtnText, String negativeBtnText, OnPositiveBtnClickListener positiveBtnClickListener, OnNegativeBrnClickListener negativeBrnClickListener) {
        try {
            Intent intent = new Intent(context, AlertDialogActivity.class);
            intent.putExtra(TITLE, title);
            intent.putExtra(MESSAGE, message);
            intent.putExtra(POSITIVE_BTN_TEXT, positiveBtnText);
            intent.putExtra(NEGATIVE_BTN_TEXT, negativeBtnText);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
             AlertDialogActivity.positiveBtnClickListener = positiveBtnClickListener;
             AlertDialogActivity.negativeBrnClickListener = negativeBrnClickListener;
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
           Log.e("TAG","Exception "+e.getMessage());
        }

    }


    static OnPositiveBtnClickListener positiveBtnClickListener;

    public interface OnPositiveBtnClickListener {
        void onPositiveBtnClick();
    }

    static OnNegativeBrnClickListener negativeBrnClickListener;

    public interface OnNegativeBrnClickListener {
        void onNegativeBtnClick();
    }

    @Override
    public void onBackPressed() {

    }
}
