package com.terminatingcode.android.migrainetree.amazonaws.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.terminatingcode.android.migrainetree.R;
import com.terminatingcode.android.migrainetree.amazonaws.AWSMobileClient;
import com.terminatingcode.android.migrainetree.amazonaws.user.IdentityManager;

 public class IdentityDemoFragment extends DemoFragmentBase {
    /** Logging tag for this class. */
    private static final String LOG_TAG = IdentityDemoFragment.class.getSimpleName();

    /** The identity manager used to keep track of the current user account. */
    private IdentityManager identityManager;

    /** This fragment's view. */
    private View mFragmentView;

    /** Text view for showing the user identity. */
    private TextView userIdTextView;

    /** Text view for showing the user name. */
    private TextView userNameTextView;

    /** Image view for showing the user image. */
    private ImageView userImageView;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mFragmentView = inflater.inflate(R.layout.fragment_demo_identity, container, false);
        userNameTextView = (TextView) mFragmentView.findViewById(R.id.textView_demoIdentityUserName);
        userIdTextView = (TextView) mFragmentView.findViewById(R.id.textView_demoIdentityUserID);
        userImageView = (ImageView)mFragmentView.findViewById(R.id.imageView_demoIdentityUserImage);

        // Obtain a reference to the identity manager.
        identityManager = AWSMobileClient.defaultMobileClient()
            .getIdentityManager();
        fetchUserIdentity();
        return mFragmentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    /**
     * Fetches the user identity safely on the background thread.  It may make a network call.
     */
    private void fetchUserIdentity() {
        Log.d(LOG_TAG, "fetchUserIdentity");

        // Pre-fetched to avoid race condition where fragment is no longer active.
        final String unknownUserIdentityText =
                getString(R.string.identity_demo_unknown_identity_text);

        AWSMobileClient.defaultMobileClient()
                .getIdentityManager()
                .getUserID(new IdentityManager.IdentityHandler() {

            @Override
            public void handleIdentityID(String identityId) {

                // We have successfully retrieved the user's identity. You can use the
                // user identity value to uniquely identify the user. For demonstration
                // purposes here, we will display the value in a text view.
                userIdTextView.setText(identityId);
            }

            @Override
            public void handleError(Exception exception) {

                // We failed to retrieve the user's identity. Set unknown user identifier
                // in text view.
                userIdTextView.setText(unknownUserIdentityText);

                final Context context = getActivity();

                if (context != null && isAdded()) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle(R.string.identity_demo_error_dialog_title)
                            .setMessage(getString(R.string.identity_demo_error_message_failed_get_identity)
                                    + exception.getMessage())
                            .setNegativeButton(R.string.identity_demo_dialog_dismiss_text, null)
                            .create()
                            .show();
                }
            }
        });
    }
}
