package com.nelyan_live.HELPER;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;



/*
 * Created by 123 on 26-09-2017.
 */


public class GoogleHelper implements GoogleApiClient.OnConnectionFailedListener {

    public interface GoogleHelperCallback {
        void onSuccessGoogle(GoogleSignInAccount account);
        void onErrorGoogle();
    }

    public static final String TAG = GoogleHelper.class.getSimpleName();

    public static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    private FragmentActivity context;
    private GoogleHelperCallback callback;

    public GoogleHelper(FragmentActivity context, GoogleHelperCallback callback) {
        this.context = context;
        this.callback = callback;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .enableAutoManage(context /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    public void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        context.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    public void onResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    public void  stopApiClient(){
      /*  mGoogleApiClient.stopAutoManage(context);
        mGoogleApiClient.disconnect();*/
         if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage((FragmentActivity) context);
            mGoogleApiClient.disconnect();
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount account = result.getSignInAccount();
            // text for dob

            callback.onSuccessGoogle(account);
        } else {
            callback.onErrorGoogle();
        }
    }
}