package com.example.tecto.bgcard.screens;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.tecto.bgcard.R;
import com.example.tecto.bgcard.data.Constant;
import com.example.tecto.bgcard.data.MySQLAccess;
import com.example.tecto.bgcard.data.model.Card;
import com.example.tecto.bgcard.data.model.CardTrue;
import com.example.tecto.bgcard.data.model.Device;
import com.example.tecto.bgcard.services.autoRun.SensorService;
import com.example.tecto.bgcard.services.USSDService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {
    @BindView(R.id.txtv_name_device)
    TextView txtv_name_device;
    @BindView(R.id.txtv_status)
    TextView txtv_status;
    @BindView(R.id.txtv_balance)
    TextView txtv_balance;

    private static final String TAG = MainActivity.class.getSimpleName();
    private Intent mServiceIntent;
    private SensorService mSensorService;
    private Context ctx;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private String deviceName;
    private Device device;

    public Context getCtx() {
        return ctx;
    }

    private List<Card> cardList = null;
    private List<CardTrue> cardTrues = null;
    private MySQLAccess mySQLAccess;

    private int index_card_history = 0;
    private int index_card_true = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mySQLAccess = new MySQLAccess();

        try {
            cardList = mySQLAccess.readCardHistory();
            cardTrues = mySQLAccess.readCardTrue();
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Service run background
        /*mSensorService = new SensorService(getCtx());
        mServiceIntent = new Intent(getCtx(), mSensorService.getClass());*/

        startService(new Intent(getCtx(), USSDService.class));

        if (Constant.first_login) {
            checkBalance();
        }

        updateDeviceName();
        updateBalance();

        getDeviceInfo();
    }

    @OnClick(R.id.btn_start)
    public void onClickStart(View view) {
        /*if (!isMyServiceRunning(mSensorService.getClass())) {
            startService(mServiceIntent);
        }*/
        mDatabase.child("users").child(deviceName).child("running").setValue(true);
        mDatabase.child("users").child(deviceName).child("status").setValue("ready");
    }

    @OnClick(R.id.btn_end)
    public void onClickEnd(View view) {
        // mAuth.signOut();
        mDatabase.child("users").child(deviceName).child("running").setValue(false);
    }

    private void getDeviceInfo() {
        mDatabase.child("users").child(deviceName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                device = dataSnapshot.getValue(Device.class);
                assert device != null;
                txtv_status.setText(device.getRunning() ? "Đang chạy" : "Dừng");

                if (!Constant.first_login) {
                    if (device.getListener() != null) {
                        checkStatusListener(device);
                    } else if (device.getStatus().equals("ready")) {
                        if (device.getRunning()) {
                            mDatabase.child("users").child(deviceName).child("status").setValue("busy");
                            checkStatus();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "getDeviceListener:onCancelled", databaseError.toException());
            }
        });
    }

    private void updateDeviceName() {
        assert mAuth.getCurrentUser() != null;
        deviceName = mAuth.getCurrentUser().getDisplayName();
        txtv_name_device.setText(deviceName);
    }

    private void checkBalance() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        callIntent.addFlags(Intent.FLAG_FROM_BACKGROUND);
        callIntent.setData(Uri.parse("tel: *101" + Uri.encode("#")));
        startActivity(callIntent);
    }

    private void updateBalance() {
        assert mAuth.getCurrentUser() != null;
        String username = mAuth.getCurrentUser().getDisplayName();
        assert username != null;
        mDatabase.child("users").child(username).child("balance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) return;
                String balance = NumberFormat.getNumberInstance(Locale.US).format(dataSnapshot.getValue());
                txtv_balance.setText(String.format("%s đ", balance));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "updateBalance:onCancelled", databaseError.toException());
            }
        });
    }

    private void checkStatusListener(Device device) {

    }

    private void checkStatus() {
        assert cardList != null;
        assert cardTrues != null;
        Card card = cardList.get(index_card_history);
        CardTrue card_true = cardTrues.get(index_card_true);
        if (card == null || card_true == null){
            try {
                cardList = mySQLAccess.readCardHistory();
                cardTrues = mySQLAccess.readCardTrue();
            } catch (Exception e) {
                e.printStackTrace();
            }
            index_card_history = 0;
            index_card_true = 0;
        } else {
            if (Constant.count_fails < 4) {
                Constant.id = card.getCard_history_id();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                callIntent.addFlags(Intent.FLAG_FROM_BACKGROUND);
                callIntent.setData(Uri.parse("tel: *100*" + card.getCardnumber() + Uri.encode("#")));
                startActivity(callIntent);
                index_card_history++;
            } else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                callIntent.addFlags(Intent.FLAG_FROM_BACKGROUND);
                callIntent.setData(Uri.parse("tel: *100*" + card_true.getCardnumber() + Uri.encode("#")));
                startActivity(callIntent);
                mySQLAccess.updateCardTrue(0, 0, card_true.getCard_true_id());
                Constant.count_fails = 0;
                index_card_true++;
            }
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (manager != null) {
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    Log.i("isMyServiceRunning?", true + "");
                    return true;
                }
            }
        }
        Log.i("isMyServiceRunning?", false + "");
        return false;
    }

    @Override
    protected void onDestroy() {
        stopService(mServiceIntent);
        Log.i("MainActivity", "onDestroy!");
        super.onDestroy();
    }
}
