package com.example.bt.ui;

import android.content.Context;
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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.OAuthCredential;

import java.util.Collections;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private static final int RC_SIGN_IN = 101;
    private Button mBtnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        boolean isLoggedIn = checkLoggedInUser();

        if (isLoggedIn)
        {
            openEventsActivity();
        }

        mBtnLogin = findViewById(R.id.btn_login);

        mBtnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private boolean checkLoggedInUser() {
        String accessToken = LoginPersistanceManager.readFromFile(this);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null)
        {
            // User signed in
            CurrentUserAccount.getInstance().setFirebaseUser(firebaseUser);
            CurrentUserAccount.getInstance().InitCurrentUser(firebaseUser);
            return true;
        }
        else{
            // No user signed in
            return false;
        }
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

                CurrentUserAccount.getInstance().InitCurrentUser(user);

                //showAlertDialog(user);
                Toast.makeText(getBaseContext(), String.format("Phone Auth Successful: %s", idpResponse.getPhoneNumber()), Toast.LENGTH_LONG).show();

                openEventsActivity();

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

    private void openEventsActivity()
    {
        Intent intent  = new Intent(this, EventsActivity.class);
        startActivity(intent);
    }

}
