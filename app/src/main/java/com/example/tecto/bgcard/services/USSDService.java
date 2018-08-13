package com.example.tecto.bgcard.services;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.example.tecto.bgcard.data.Constant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class USSDService extends AccessibilityService {
    public static String TAG = USSDService.class.getSimpleName();

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        AccessibilityNodeInfo source = event.getSource();
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED && !String.valueOf(event.getClassName()).contains("AlertDialog")) {
            return;
        }
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED && (source == null || !source.getClassName().equals("android.widget.TextView"))) {
            return;
        }
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED && TextUtils.isEmpty(source.getText())) {
            return;
        }

        List<CharSequence> eventText;

        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            eventText = event.getText();
        } else {
            eventText = Collections.singletonList(source.getText());
        }

        String text = processUSSDText(eventText);

        if (TextUtils.isEmpty(text)) return;

        // Close dialog
        performGlobalAction(GLOBAL_ACTION_BACK); // This works on 4.1+ only

        // Handle USSD response here
        String result = String.valueOf(event.getText());
        if (Constant.first_login) {
            onCheckBalance(result);
            Constant.first_login = false;
        } else {
            onCheckCard(result);
        }
    }

    private void onCheckBalance(String result) {
        int TK = result.indexOf("TK goc la ");
        int TK_end = result.indexOf("d.");
        if (TK > 0) {
            String balance_text = result.substring(TK, TK_end);
            int balance_value = Integer.parseInt(balance_text.substring(10));
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mAuth = FirebaseAuth.getInstance();

            assert mAuth.getCurrentUser() != null;
            String username = mAuth.getCurrentUser().getDisplayName();
            assert username != null;
            if (balance_value > 1000000) {
                mDatabase.child("users").child(username).child("running").setValue(false);
            }
            mDatabase.child("users").child(username).child("balance").setValue(balance_value);
        }
    }

    private void onCheckCard(String result) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        assert mAuth.getCurrentUser() != null;
        String username = mAuth.getCurrentUser().getDisplayName();
        assert username != null;

        int KhongHopLe = result.indexOf("khong hop le");
        int HopLe = result.indexOf("Quy khach la ");
        int HopLe_end = result.indexOf("dong");

        if (KhongHopLe > 0) {
            Log.e(TAG, "onCheckCard: " + "Khong hop le");
            // call API set status fail
        } else if (HopLe > 0) {
            Log.e(TAG, "onCheckCard: " + "Hop le");
            String balance_text = result.substring(HopLe, HopLe_end);
            int balance_value = Integer.parseInt(balance_text.substring(13));

            if (balance_value > 1000000) {
                mDatabase.child("users").child(username).child("running").setValue(false);
            }
            mDatabase.child("users").child(username).child("balance").setValue(balance_value);
            // Call API set status success and value
        } else {
            // call API set status unknowns
        }
        mDatabase.child("users").child(username).child("status").setValue("ready");
    }

    private String processUSSDText(List<CharSequence> eventText) {
        for (CharSequence s : eventText) {
            String text = String.valueOf(s);
            // Return text if text is the expected ussd response
            return text;
        }
        return null;
    }

    @Override
    public void onInterrupt() {
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.d(TAG, "onServiceConnected");
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.flags = AccessibilityServiceInfo.DEFAULT;
        info.packageNames = new String[]{"com.android.phone"};
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED | AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        setServiceInfo(info);
    }
}
