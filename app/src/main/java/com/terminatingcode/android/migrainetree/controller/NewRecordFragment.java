package com.terminatingcode.android.migrainetree.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.terminatingcode.android.migrainetree.R;
import com.terminatingcode.android.migrainetree.model.amazonaws.AWSMobileClient;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewRecordFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewRecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewRecordFragment extends Fragment {
    private FragmentListener mListener;

    public NewRecordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NewRecordFragment.
     */
    public static NewRecordFragment newInstance() {
        NewRecordFragment fragment = new NewRecordFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_record, container, false);
        Button newRecordButton = (Button) rootView.findViewById(R.id.record_new_migraine_button);
        newRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean isUserSignedIn = AWSMobileClient.defaultMobileClient()
                        .getIdentityManager()
                        .isUserSignedIn();
                if (isUserSignedIn && mListener != null) {
                    mListener.onNewRecordButtonClicked();
                }else{
                    promptSignin();
                }
            }
        });
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentListener) {
            mListener = (FragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void promptSignin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setTitle(
                R.string.prompt_sign_in_title)
                .setNegativeButton(android.R.string.cancel, null);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                if(mListener != null) mListener.onPromptForSignin();
            }
        });
        builder.setMessage(R.string.prompt_signin_message);
        builder.show();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener extends FragmentListener {
        void onNewRecordButtonClicked();
        void onPromptForSignin();
    }
}
