package com.haphazrd.movblox.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
/**
 * Created by brittanystubbs on 7/16/15.
 */
public class GoogleServicesHelper extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,
                                            GoogleApiClient.OnConnectionFailedListener{

    public interface GoogleServicesListener{
        public void onConnected();
        public void onDisconnected();
    }

    private static final int REQUEST_CODE_RESOLUTION = -100;
    private static final int REQUEST_CODE_AVAILABILITY = -101;
    private static final String DIALOG_ERROR = "dialog_error";

    private boolean mResolvingError = false;

    private  Activity activity;
    private GoogleServicesListener listener;
    private GoogleApiClient apiClient;

    public GoogleServicesHelper(Activity activity, GoogleServicesListener listener){
        this.activity = activity;
        this.listener = listener;
        this.apiClient = new GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API)
                .addScope(Games.SCOPE_GAMES)
                .build();
    }

    public GoogleApiClient getApi(){
        return this.apiClient;
    }

    public void connect(){
        if(isGooglePlayServicesAvailable()) {
            apiClient.connect();
        }else {
            listener.onDisconnected();
        }
    }

    public void disconnect(){
        if(isGooglePlayServicesAvailable()) {
            apiClient.disconnect();
        } else {
            listener.onDisconnected();
        }
    }

    private boolean isGooglePlayServicesAvailable(){
        int availability = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        switch (availability){
            case ConnectionResult.SUCCESS:
                return true;
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
            case ConnectionResult.SERVICE_DISABLED:
            case ConnectionResult.SERVICE_INVALID:
                GooglePlayServicesUtil.getErrorDialog(availability, activity, REQUEST_CODE_AVAILABILITY).show();
                return false;
            default:
                return false;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(activity, "connected api", Toast.LENGTH_LONG).show();
        listener.onConnected();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(activity, "suspended", Toast.LENGTH_LONG).show();
        listener.onDisconnected();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if(mResolvingError){
            return;
        } else if(connectionResult.hasResolution()){
            try {
                mResolvingError = true;
                Log.e(activity.getClass().getSimpleName(), connectionResult + "");
                connectionResult.startResolutionForResult(activity, REQUEST_CODE_RESOLUTION);
            } catch(IntentSender.SendIntentException e){
                apiClient.connect();
            }
        } else {
            showErrorDialog(connectionResult.getErrorCode());
            mResolvingError = true;
            listener.onDisconnected();
        }
    }

    /* Creates a dialog for an error message */
    private void showErrorDialog(int errorCode) {
        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), "errordialog");
    }

    /* Called from ErrorDialogFragment when the dialog is dismissed. */
    public void onDialogDismissed() {
        mResolvingError = false;
    }

    /* A fragment to display an error dialog */
    public static class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() { }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GooglePlayServicesUtil.getErrorDialog(errorCode,
                    this.getActivity(), REQUEST_CODE_RESOLUTION);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            ((GoogleServicesHelper)getActivity()).onDialogDismissed();
        }
    }

    public void handleActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_CODE_RESOLUTION || requestCode == REQUEST_CODE_AVAILABILITY){
            mResolvingError = false;
            if(resultCode == Activity.RESULT_OK) {
                if(!apiClient.isConnecting() && !apiClient.isConnected()) {
                    apiClient.connect();
                }
            } else {
                listener.onDisconnected();
            }
        }
    }
}
