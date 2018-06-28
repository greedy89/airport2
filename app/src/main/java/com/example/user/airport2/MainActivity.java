package com.example.user.airport2;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;

import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private BeaconManager beaconManager;
    ToggleButton btnToggle;
    Switch sw1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        beaconManager = new BeaconManager(getApplicationContext());
        btnToggle = (ToggleButton)findViewById(R.id.toggleButton);
        sw1 = (Switch)findViewById(R.id.switch1);
        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
                        @Override
                        public void onServiceReady() {
                            long scanPeriod = 5*1000;
                            long waitTime = 5*1000;
                            beaconManager.setBackgroundScanPeriod(scanPeriod,waitTime);
                            beaconManager.startMonitoring(new BeaconRegion(
                                    "region1", UUID.fromString("b9407f30-f5f8-466e-aff9-25556b57fe6d"),39255,27376
                            ));

                        }
                    });
                }else{
                    beaconManager.disconnect();
                    Toast.makeText(MainActivity.this, "off service", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        btnToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
//                        @Override
//                        public void onServiceReady() {
//                            long scanPeriod = 10*1000;
//                            long waitTime = 5*1000;
//                            beaconManager.setBackgroundScanPeriod(scanPeriod,waitTime);
//                            beaconManager.startMonitoring(new BeaconRegion(
//                                    "region1", UUID.fromString("b9407f30-f5f8-466e-aff9-25556b57fe6d"),39255,27376
//                            ));
//
//                        }
//                    });
//                }else{
//                    beaconManager.disconnect();
//                    Toast.makeText(MainActivity.this, "off service", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });


        beaconManager.setMonitoringListener(new BeaconManager.BeaconMonitoringListener() {
            @Override
            public void onEnteredRegion(BeaconRegion beaconRegion, List<Beacon> beacons) {
                shownotification(
                        "Your gate closes in 47 minutes.",
                        "Current security wait time is 15 minutes, "
                                + "and it's a 5 minute walk from security to the gate. "
                                + "Looks like you've got plenty of time!");
            }

            @Override
            public void onExitedRegion(BeaconRegion beaconRegion) {
                shownotification(
                        "Airport",
                        "you exit airport");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
    }

    public void shownotification(String title,String message){
        Intent notifiIntent = new Intent(this, MainActivity.class);
        notifiIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[] { notifiIntent }, PendingIntent.FLAG_UPDATE_CURRENT);


        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .build();


        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }
}
