package com.nickdbush.mywbgs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.nickdbush.mywbgs.models.Event;
import com.nickdbush.mywbgs.models.Lesson;
import com.nickdbush.mywbgs.tasks.GetHomeworkTask;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;
import io.realm.Realm;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity {

    @State
    String username;
    @State
    String password;

    @BindView(R.id.txtl_username)
    TextInputLayout txtlUsername;
    @BindView(R.id.txtl_password)
    TextInputLayout txtlPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        if (username != null) txtlUsername.getEditText().setText(username);
        if (password != null) txtlPassword.getEditText().setText(password);
    }

    public void login(View view) {
        String username = txtlUsername.getEditText().getText().toString().trim();
        String password = txtlPassword.getEditText().getText().toString();

        // TODO: 26/10/2016 Validation!

        GetHomeworkTask getHomeworkTask = new GetHomeworkTask(new GetHomeworkTask.OnHomeworkReturned() {
            @Override
            public void onHomeworkReturned(List<Lesson> lessons) {
                if (lessons == null || lessons.size() == 0) {
                    ConnectivityManager cm = (ConnectivityManager) getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                    if (!isConnected)
                        Toast.makeText(getBaseContext(), "No connection", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getBaseContext(), "Incorrect username or password", Toast.LENGTH_LONG).show();
                    btnLogin.setEnabled(true);
                    return;
                }
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                for (Lesson lesson : lessons) {
                    realm.copyToRealm(lesson);
                }
                try {
                    realm.createAllFromJson(Event.class, getAssets().open("calendar.json"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                realm.commitTransaction();


                SharedPreferences.Editor editor = getSharedPreferences("com.nickdbush.mywbgs", MODE_PRIVATE).edit();
                editor.putBoolean("nodata", false);
                editor.commit();

                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        bundle.putString("password", password);
        btnLogin.setEnabled(false);
        getHomeworkTask.execute(bundle);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        username = txtlUsername.getEditText().toString();
        password = txtlPassword.getEditText().toString();
        Icepick.saveInstanceState(this, outState);
    }
}
