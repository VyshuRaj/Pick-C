package com.prts.pickcustomer.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.prts.pickcustomer.R;
import com.prts.pickcustomer.helpers.SocketManager;

import java.net.Socket;

import io.reactivex.Emitter;

public class SocketSample extends AppCompatActivity {
    TextView mTextView;
    String TAG = "Socket";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket_sample);

        mTextView = findViewById(R.id.receive);
    }

    public void send(View view) {

    }
}
