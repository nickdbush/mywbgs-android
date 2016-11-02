package com.nickdbush.mywbgs.components;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FirebaseIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(getClass().getSimpleName(), "FIREBASE TOKEN: " + refreshedToken);
        sendToServer(refreshedToken);
    }

    private void sendToServer(String token) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) database.child(user.getUid()).child("token").setValue(token);
    }
}
