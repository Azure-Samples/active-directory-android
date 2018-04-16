package com.azuresamples.azureadsampleapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;

import com.microsoft.aad.adal.*;


public class MainActivity extends AppCompatActivity {

    /* UI & Debugging Variables */
    private static final String TAG = MainActivity.class.getSimpleName();
    Button callGraphButton;
    Button signOutButton;

    /* Azure AD Constants */
    private static final String AUTHORITY = "https://login.microsoftonline.com/common";

    /* Azure AD Variables */
    private AuthenticationContext mAuthContext;
    private AuthenticationResult authResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        callGraphButton = (Button) findViewById(R.id.callGraph);
        signOutButton = (Button) findViewById(R.id.clearCache);

        callGraphButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onCallGraphClicked();
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onSignOutClicked();
            }
        });

        mAuthContext = new AuthenticationContext(MainActivity.this, AUTHORITY, false);


        /* TODO: Attempt an acquireTokenSilent call to see if we're signed in  */
    }

    //
    // Core Auth methods used by ADAL
    // ==================================
    // onActivityResult() - handles redirect from System browser
    // onCallGraphClicked() - attempts to get tokens for graph, if it succeeds calls graph & updates UI
    // callGraphAPI() - called on successful token acquisition which makes an HTTP request to graph
    // onSignOutClicked() - Signs user out of the app & updates UI
    //

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mAuthContext.onActivityResult(requestCode, resultCode, data);
    }

    /*
     * End user clicked call Graph API button, time for Auth
     * Use ADAL to get an Access token for the Microsoft Graph API
     */
    private void onCallGraphClicked() {
        // Don't need to call ATS, did it onCreate.
        // Call AT()
        // Callback will call graph, update UI
    }

    private void callGraphAPI() {
        // Grab AccessToken
        // Call the /me endpoint of MS Graph API
        // Update UI for Auth'd user

        // TODO: Use volley to call graph, paint data via updateGraphUI(...)
    }

    private void onSignOutClicked() {
        // End user has clicked the Sign Out button
        // Kill the token cache
        // Optionally call the signout endpoint to fully sign out the user account
    }

    //
    // UI Helper methods
    // ================================
    // updateGraphUI() - Sets graph response in UI
    // updateSuccessUI() - Updates UI when token acquisition succeeds
    // updateSignedOutUI() - Updates UI when app sign out succeeds
    //

    private void updateGraphUI() {
        // Called on success from /me endpoint
        // Writes graph data to the UI
    }

    private void updateSuccessUI() {
        // Called on success from /me endpoint
        // Removed call Graph API button and paint Sign out

    }

    private void updateSignedOutUI() {
        // TODO: Hide data, sign out button, show un-authenticated UI
    }

    //
    // ADAL Callbacks
    // ======================
    // getActivity() - returns activity so we can acquireToken within a callback
    // getAuthSilentCallback() - callback defined to handle acquireTokenSilent() case
    // getAuthInteractiveCallback() - callback defined to handle acquireToken() case
    //

    public Activity getActivity() {
        return this;
    }

    /* Callback used in for silent acquireToken calls.
     * Looks if tokens are in the cache (refreshes if necessary and if we don't forceRefresh)
     * else errors that we need to do an interactive request.
     */
    private AuthenticationCallback getAuthSilentCallback() {
        return new AuthenticationCallback<AuthenticationResult>() {
            @Override
            public void onSuccess(AuthenticationResult authenticationResult) {
                /* Successfully got a token, call graph now */
                Log.d(TAG, "Successfully authenticated");

                /* Store the authResult */
                authResult = authenticationResult;

                /* call graph */
                callGraphAPI();

                /* update the UI to post call graph state */
                updateSuccessUI();
            }

            @Override
            public void onError(Exception exception) {
                /* Failed to acquireToken */
                Log.d(TAG, "Authentication failed: " + exception.toString());

                if (exception instanceof AuthenticationException) {
                    /* TODO: Implement error handling described in doc https://docs.microsoft.com/en-us/azure/active-directory/develop/active-directory-devhowto-adal-error-handling#acquiretokensilent */
                    /* TODO: Make sure user cancel error is covered */
                } else {
                    /* Tokens expired or no session, retry with interactive */

                }
            }
        };
    }

    /* Callback used for interactive request.  If succeeds we use the access
     * token to call the Microsoft Graph. Does not check cache
     */
    private AuthenticationCallback getAuthInteractiveCallback() {
        return new AuthenticationCallback<AuthenticationResult>() {
            @Override
            public void onSuccess(AuthenticationResult authenticationResult) {
                /* Successfully got a token, call graph now */
                Log.d(TAG, "Successfully authenticated");
                Log.d(TAG, "ID Token: " + authenticationResult.getIdToken());

                /* Store the auth result */
                authResult = authenticationResult;

                /* call graph */
                callGraphAPI();

                /* update the UI to post call graph state */
                updateSuccessUI();
            }

            @Override
            public void onError(Exception exception) {
                /* Failed to acquireToken */
                Log.d(TAG, "Authentication failed: " + exception.toString());

                if (exception instanceof AuthenticationException) {
                    /* TODO: Implement error handling described in doc https://docs.microsoft.com/en-us/azure/active-directory/develop/active-directory-devhowto-adal-error-handling */
                    /* TODO: Make sure user cancel error is covered */
                } else {
                    /* Tokens expired or no session, retry with interactive */

                }
            }
        };
    }

}
