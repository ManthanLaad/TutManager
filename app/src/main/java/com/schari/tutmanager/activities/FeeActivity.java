package com.schari.tutmanager.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Toast;

import com.schari.tutmanager.MainActivity;
import com.schari.tutmanager.R;

public class FeeActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) !=
                PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.SEND_SMS}, 1);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void sendMsg(View view) {
        String no = "7038832844";
        String msg = "Your tuition fee payment confirmed for the month of September";

        Intent intent=new Intent(getApplicationContext(), FeeActivity.class);
        PendingIntent pi= PendingIntent.getActivity(getApplicationContext(),
                0, intent,0);

        //Get the SmsManager instance and call the sendTextMessage method to send message
        SmsManager sms= SmsManager.getDefault();
        sms.sendTextMessage(no, null, msg, pi,null);

        Toast.makeText(this, "SMS Sent!", Toast.LENGTH_SHORT).show();
    }
}