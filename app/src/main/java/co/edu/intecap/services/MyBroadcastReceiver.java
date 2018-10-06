package co.edu.intecap.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver {
        private static final String TAG = "MyBroadcastReceiver";
        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)){
                Log.d(TAG, "onReceive: Power connected" );
                Toast.makeText(context, "Power connected\"", Toast.LENGTH_SHORT).show();
            }
        }
    }