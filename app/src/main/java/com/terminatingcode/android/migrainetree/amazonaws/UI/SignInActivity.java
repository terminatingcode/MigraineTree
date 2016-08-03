package com.terminatingcode.android.migrainetree.amazonaws.UI;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.terminatingcode.android.migrainetree.amazonaws.AWSMobileClient;
import com.terminatingcode.android.migrainetree.amazonaws.user.IdentityManager;
import com.terminatingcode.android.migrainetree.amazonaws.user.IdentityProvider;
import com.terminatingcode.android.migrainetree.amazonaws.user.signin.GoogleSignInProvider;
import com.terminatingcode.android.migrainetree.amazonaws.user.signin.SignInManager;
import com.terminatingcode.android.migrainetree.R;

public class SignInActivity extends Activity {
    private final static String LOG_TAG = SignInActivity.class.getSimpleName();
    private SignInManager signInManager;

    /** Permission Request Code (Must be < 256). */
    private static final int GET_ACCOUNTS_PERMISSION_REQUEST_CODE = 93;

    /** The Google OnClick listener, since we must override it to get permissions on Marshmallow and above. */
    private View.OnClickListener googleOnClickListener;

    /**
     * SignInResultsHandler handles the final result from sign in. Making it static is a best
     * practice since it may outlive the SplashActivity's life span.
     */
    private class SignInResultsHandler implements IdentityManager.SignInResultsHandler {
        /**
         * Receives the successful sign-in result and starts the main activity.
         * @param provider the identity provider used for sign-in.
         */
        @Override
        public void onSuccess(final IdentityProvider provider) {
            Log.d(LOG_TAG, String.format("User sign-in with %s succeeded",
                provider.getDisplayName()));

            // The sign-in manager is no longer needed once signed in.
            SignInManager.dispose();

            Toast.makeText(SignInActivity.this, String.format("Sign-in with %s succeeded.",
                provider.getDisplayName()), Toast.LENGTH_LONG).show();

            // Load user name and image.
            AWSMobileClient.defaultMobileClient()
                .getIdentityManager().loadUserInfoAndImage(provider, new Runnable() {
                @Override
                public void run() {
                    Log.d(LOG_TAG, "Launching Main Activity...");
                    startActivity(new Intent(SignInActivity.this, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    // finish should always be called on the main thread.
                    finish();
                }
            });
        }

        /**
         * Recieves the sign-in result indicating the user canceled and shows a toast.
         * @param provider the identity provider with which the user attempted sign-in.
         */
        @Override
        public void onCancel(final IdentityProvider provider) {
            Log.d(LOG_TAG, String.format("User sign-in with %s canceled.",
                provider.getDisplayName()));

            Toast.makeText(SignInActivity.this, String.format("Sign-in with %s canceled.",
                provider.getDisplayName()), Toast.LENGTH_LONG).show();
        }

        /**
         * Receives the sign-in result that an error occurred signing in and shows a toast.
         * @param provider the identity provider with which the user attempted sign-in.
         * @param ex the exception that occurred.
         */
        @Override
        public void onError(final IdentityProvider provider, final Exception ex) {
            Log.e(LOG_TAG, String.format("User Sign-in failed for %s : %s",
                provider.getDisplayName(), ex.getMessage()), ex);

            final AlertDialog.Builder errorDialogBuilder = new AlertDialog.Builder(SignInActivity.this);
            errorDialogBuilder.setTitle("Sign-In Error");
            errorDialogBuilder.setMessage(
                String.format("Sign-in with %s failed.\n%s", provider.getDisplayName(), ex.getMessage()));
            errorDialogBuilder.setNeutralButton("Ok", null);
            errorDialogBuilder.show();
        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        signInManager = SignInManager.getInstance(this);

        signInManager.setResultsHandler(this, new SignInResultsHandler());

        // Initialize sign-in buttons.
        googleOnClickListener =
            signInManager.initializeSignInButton(GoogleSignInProvider.class, findViewById(R.id.g_login_button));

        if (googleOnClickListener != null) {
            // if the onClick listener was null, initializeSignInButton will have removed the view.
            this.findViewById(R.id.g_login_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    final Activity thisActivity = SignInActivity.this;
                    if (ContextCompat.checkSelfPermission(thisActivity,
                        Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(SignInActivity.this,
                            new String[]{Manifest.permission.GET_ACCOUNTS},
                            GET_ACCOUNTS_PERMISSION_REQUEST_CODE);
                        return;
                    }

                    // call the Google onClick listener.
                    googleOnClickListener.onClick(view);
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode,
                                           final String permissions[], final int[] grantResults) {
        if (requestCode == GET_ACCOUNTS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                this.findViewById(R.id.g_login_button).callOnClick();
            } else {
                Log.i(LOG_TAG, "Permissions not granted for Google sign-in. :(");
            }
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        signInManager.handleActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // pause/resume Mobile Analytics collection
        AWSMobileClient.defaultMobileClient().handleOnResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // pause/resume Mobile Analytics collection
        AWSMobileClient.defaultMobileClient().handleOnPause();
    }

}
