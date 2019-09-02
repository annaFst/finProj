package com.example.bt.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bt.BuildConfig;
import com.example.bt.EventsActivity;
import com.example.bt.R;
import com.example.bt.app.CurrentUserAccount;
import com.example.bt.app.LoginPersistanceManager;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import java.util.Collections;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private static final int RC_SIGN_IN = 101;
    //private EditText mUserPhone;
    //private EditText mUserName;
    private Button mBtnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //mUserPhone = findViewById(R.id.input_phone_number);
        //mUserName = findViewById(R.id.input_full_name);
        mBtnLogin = findViewById(R.id.btn_login);

        mBtnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                login();


            }
        });
    }

    private void login() {

        Intent intent = AuthUI.getInstance().createSignInIntentBuilder()
                    .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                    .setAvailableProviders(Collections.singletonList(
                            new AuthUI.IdpConfig.PhoneBuilder().build()))
                    .setLogo(R.mipmap.ic_launcher)
                    .build();

        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {

            IdpResponse idpResponse = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                user.getIdToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                    @Override
                    public void onSuccess(GetTokenResult result) {
                        String idToken = result.getToken();
                        LoginPersistanceManager.writeToFile(getBaseContext(), idToken);

                        Log.d(TAG, "GetTokenResult result = " + idToken);
                    }
                });

                try {
                    CurrentUserAccount.getInstance().InitCurrentUser(user);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                showAlertDialog(user);

                Intent intent  = new Intent(this, EventsActivity.class);
                startActivity(intent);

            } else {
                /**
                 *   Sign in failed. If response is null the user canceled the
                 *   sign-in flow using the back button. Otherwise check
                 *   response.getError().getErrorCode() and handle the error.
                 */
                Toast.makeText(getBaseContext(), "Phone Auth Failed", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void showAlertDialog(FirebaseUser user) {
        AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(
                LoginActivity.this);

        // Set Title
        mAlertDialog.setTitle("Successfully Signed In");

        // Set Message
        mAlertDialog.setMessage(" Phone Number is " + user.getPhoneNumber());

        mAlertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        mAlertDialog.create();

        // Showing Alert Message
        mAlertDialog.show();
    }
}
