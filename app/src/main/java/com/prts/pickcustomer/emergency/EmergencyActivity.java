package com.prts.pickcustomer.emergency;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.prts.pickcustomer.R;
import com.prts.pickcustomer.helpers.PermissionChecker;
import com.prts.pickcustomer.helpers.ToastHelper;

import static com.prts.pickcustomer.helpers.PermissionChecker.CALL_PERMISSIONS;

public class EmergencyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        if (!PermissionChecker.checkPermission(EmergencyActivity.this, CALL_PERMISSIONS))
            PermissionChecker.reqPermissions(EmergencyActivity.this, CALL_PERMISSIONS);

    }

    private String getName(String number) {
        switch (number) {
            case "100":
                return "Police";
            case "101":
                return "Fire Dept";
            default:
                return "Ambulance";
        }
    }

    private void showConfirmDialog(final String number) {

        try {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(EmergencyActivity.this);
            alertDialog.setTitle("Call confirmation");
            alertDialog.setMessage("Do you want to make a call to " + getName(number) + "?");
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            final AlertDialog alertDialog1 = alertDialog.create();
            alertDialog1.show();
            alertDialog1.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog1.cancel();
                    makeCall(number);
                }
            });

            alertDialog1.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog1.cancel();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void makeCallToPolice(View view) {
        showConfirmDialog("100");
    }

    public void makeCallToFireDept(View view) {
        showConfirmDialog("101");

    }

    public void makeCallToAmbulance(View view) {
        showConfirmDialog("108");
    }

    private void makeCall(String number) {

        try {

            if (!PermissionChecker.checkPermission(EmergencyActivity.this, CALL_PERMISSIONS))
                PermissionChecker.reqPermissions(EmergencyActivity.this, CALL_PERMISSIONS);

            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
            intent.setData(Uri.parse("tel:" + number));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

            ToastHelper.showToastLenShort(EmergencyActivity.this, "Please give permission to make calls");
        }
    }
}
