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
import android.support.v7.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.nickdbush.mywbgs.components.HomeworkNotificationManager;
import com.nickdbush.mywbgs.models.Event;
import com.nickdbush.mywbgs.models.Lesson;
import com.nickdbush.mywbgs.tasks.GetHomeworkTask;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;
import io.realm.Realm;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity {

    @State
    String username = "";
    @State
    String password = "";

    @BindView(R.id.txtl_username)
    TextInputLayout txtlUsername;
    @BindView(R.id.txtl_password)
    TextInputLayout txtlPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;

    private SharedPreferences sharedPreferences;

    private Realm realm;

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
        realm = Realm.getDefaultInstance();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        txtlUsername.getEditText().setText(username);
        txtlPassword.getEditText().setText(password);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    public void login(View view) {
        username = txtlUsername.getEditText().getText().toString().trim().toLowerCase();
        String password = txtlPassword.getEditText().getText().toString();

        // TODO: 26/10/2016 Validation!

        GetHomeworkTask getHomeworkTask = new GetHomeworkTask(new GetHomeworkTask.OnHomeworkReturned() {
            @Override
            public void onHomeworkReturned(GetHomeworkTask.Result result) {
                if (result.exception instanceof UnknownHostException) {
                    ConnectivityManager cm = (ConnectivityManager) getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                    if (!isConnected) {
                        Toast.makeText(getBaseContext(), "Please connect to the internet and try again", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getBaseContext(), "The Watford Boys appears to be down. Please try again later", Toast.LENGTH_LONG).show();
                    }
                    btnLogin.setEnabled(true);
                    return;
                }
                if (result.exception instanceof SocketTimeoutException) {
                    Toast.makeText(getBaseContext(), "The connection timed out. Please try again later", Toast.LENGTH_LONG).show();
                    btnLogin.setEnabled(true);
                    return;
                } else if (result.exception != null) {
                    Toast.makeText(getBaseContext(), result.exception.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    btnLogin.setEnabled(true);
                    return;
                } else if (result.lessons == null) {
                    Toast.makeText(getBaseContext(), "Incorrect username or password", Toast.LENGTH_LONG).show();
                    btnLogin.setEnabled(true);
                    return;
                }

                realm.beginTransaction();
                for (Lesson lesson : result.lessons) {
                    realm.copyToRealm(lesson);
                }
                try {
                    realm.createAllFromJson(Event.class, getAssets().open("calendar.json"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                realm.commitTransaction();

                HomeworkNotificationManager.setEnabled(getBaseContext(), true);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("version", 1);
                editor.putString("username", username);
                editor.putBoolean("init.timetable", true);
                editor.putBoolean("init.notification", true);
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
        username = txtlUsername.getEditText().getText().toString();
        password = txtlPassword.getEditText().getText().toString();
        Icepick.saveInstanceState(this, outState);
    }
}
