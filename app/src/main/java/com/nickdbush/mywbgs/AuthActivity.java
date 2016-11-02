package com.nickdbush.mywbgs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.nickdbush.mywbgs.components.HomeworkNotificationManager;
import com.nickdbush.mywbgs.models.Event;
import com.nickdbush.mywbgs.models.Lesson;
import com.nickdbush.mywbgs.tasks.GetHomeworkTask;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.State;
import io.realm.Realm;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AuthActivity extends AppCompatActivity {

    @State
    String username;
    @State
    String password;

    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.txtl_username)
    TextInputLayout txtlUsername;
    @BindView(R.id.txtl_password)
    TextInputLayout txtlPassword;

    private SharedPreferences sharedPreferences;
    private FirebaseAuth auth;
    private Realm realm;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        sharedPreferences = getSharedPreferences(MyWBGS.SHARED_PREFERENCES_FILENAME, MODE_PRIVATE);
        auth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    public void login(View view) {
        enableButtons(false);
        username = txtlUsername.getEditText().getText().toString().trim().toLowerCase().replace("@watfordboys.org", "");
        final String email = username + "@watfordboys.org";
        password = txtlPassword.getEditText().getText().toString();
        auth.signInWithEmailAndPassword(processEmail(email), password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                HomeworkNotificationManager.setEnabled(getBaseContext(), true, false);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("schema_version", MyWBGS.SCHEMA_VERSION);
                editor.putBoolean("init.timetable", true);
                editor.putBoolean("init.notification", true);
                editor.commit();

                DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");
                database.child(authResult.getUser().getUid()).child("token").setValue(FirebaseInstanceId.getInstance().getToken());

                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(AuthActivity.this, "Username or password invalid", Toast.LENGTH_LONG).show();
                    return;
                } else if (e instanceof FirebaseAuthInvalidUserException) {
                    String error = ((FirebaseAuthInvalidUserException) e).getErrorCode();
                    if (error.equals("ERROR_USER_DISABLED")) {
                        Toast.makeText(AuthActivity.this, "Your account has been disabled", Toast.LENGTH_LONG).show();
                    } else if (error.equals("ERROR_USER_NOT_FOUND")) {
                        GetHomeworkTask getHomeworkTask = new GetHomeworkTask(new GetHomeworkTask.OnHomeworkReturned() {
                            @Override
                            public void onHomeworkReturned(final GetHomeworkTask.Result result) {
                                if (result.exception instanceof UnknownHostException) {
                                    ConnectivityManager cm = (ConnectivityManager) getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                                    boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                                    if (!isConnected) {
                                        Toast.makeText(getBaseContext(), "Please connect to the internet and try again", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getBaseContext(), "The Watford Boys website appears to be down. Please try again later", Toast.LENGTH_LONG).show();
                                    }
                                    btnLogin.setEnabled(true);
                                    return;
                                }
                                if (result.exception instanceof SocketTimeoutException) {
                                    Toast.makeText(getBaseContext(), "The connection timed out. Please try again later", Toast.LENGTH_LONG).show();
                                    enableButtons(true);
                                    return;
                                } else if (result.exception != null) {
                                    Toast.makeText(getBaseContext(), result.exception.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                    enableButtons(true);
                                    return;
                                } else if (result.lessons == null) {
                                    Toast.makeText(getBaseContext(), "Incorrect username or password", Toast.LENGTH_LONG).show();
                                    enableButtons(true);
                                    return;
                                }

                                String actualEmail = processEmail(result.meta.getString("email", ""));
                                auth.createUserWithEmailAndPassword(actualEmail, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
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

                                        HomeworkNotificationManager.setEnabled(getBaseContext(), true, false);

                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putInt("schema_version", MyWBGS.SCHEMA_VERSION);
                                        editor.putBoolean("init.timetable", true);
                                        editor.putBoolean("init.notification", true);
                                        editor.commit();

                                        DatabaseReference database = FirebaseDatabase.getInstance().getReference("users").child(authResult.getUser().getUid());
                                        database.child("token").setValue(FirebaseInstanceId.getInstance().getToken());
                                        database.child("name").setValue(result.meta.getString("name"));
                                        database.child("username").setValue(result.meta.getString("username"));
                                        database.child("email").setValue(result.meta.getString("email"));
                                        database.child("year").setValue(result.meta.getInt("year"));
                                        database.child("house").setValue(result.meta.getString("house"));

                                        // DatabaseReference timetableDatabase = FirebaseDatabase.getInstance().getReference("timetables").child(authResult.getUser().getUid());
                                        // timetableDatabase.setValue(result.lessons);

                                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AuthActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                        Log.d(AuthActivity.class.getSimpleName(), "FIREBASE AUTH ERROR: " + e.getMessage());
                                    }
                                });

                            }
                        });
                        Bundle bundle = new Bundle();
                        bundle.putString("username", username);
                        bundle.putString("password", password);
                        getHomeworkTask.execute(bundle);
                    }
                    return;
                }
            }
        });
    }

    private void enableButtons(boolean state) {
        btnLogin.setEnabled(state);
    }

    private String processEmail(String text) {
        return text.replaceAll("^\\d+", "");
    }
}
