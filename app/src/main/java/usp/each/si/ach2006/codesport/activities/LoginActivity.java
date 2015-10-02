package usp.each.si.ach2006.codesport.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;

import java.util.Arrays;

import usp.each.si.ach2006.codesport.R;
import usp.each.si.ach2006.codesport.fragments.FacebookLoginFragment;

public class LoginActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener{

	private String TAG = "SweAt";

    //------------Facebook Variables----------------
    private CallbackManager callbackManager;
    private FacebookLoginFragment facebookLoginFragment;

    //------------Google Variables----------------

    /* RequestCode for resolutions involving sign-in */
    private static final int RC_SIGN_IN = 9001;

    /* Keys for persisting instance variables in savedInstanceState */
    private static final String KEY_IS_RESOLVING = "is_resolving";
    private static final String KEY_SHOULD_RESOLVE = "should_resolve";

    /* Client for accessing Google APIs */
    private GoogleApiClient mGoogleApiClient;

    /* View to display current status (signed-in, signed-out, disconnected, etc) */
    private TextView mStatus;

    /* Is there a ConnectionResult resolution in progress? */
    private boolean mIsResolving = false;

    /* Should we automatically resolve ConnectionResults when possible? */
    private boolean mShouldResolve = false;

    Button.OnClickListener lstnGoogleLogin = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            onSignInClicked();
        }
    };

    Button.OnClickListener lstnFacebookLogin = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            onSignInClicked();
        }
    };

	private void onSignInClicked() {
		// User clicked the sign-in button, so begin the sign-in process and automatically
		// attempt to resolve any errors that occur.
		mShouldResolve = true;
		mGoogleApiClient.connect();

		// Show a message to the user that we are signing in.
		//mStatusTextView.setText(R.string.signing_in);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        //Facebook Requires this step before starting UI
		FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        //

		setContentView(R.layout.activity_login);

        if (savedInstanceState != null) {
            mIsResolving = savedInstanceState.getBoolean(KEY_IS_RESOLVING);
            mShouldResolve = savedInstanceState.getBoolean(KEY_SHOULD_RESOLVE);
        }

		// Build GoogleApiClient with access to basic profile
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                .build();

        SignInButton googleLoginButton = (SignInButton) findViewById(R.id.btnGoogleLogin);
        setGooglePlusButtonText(googleLoginButton);
        googleLoginButton.setOnClickListener(lstnGoogleLogin);

        LoginButton facebookLoginButton = (LoginButton) findViewById(R.id.btnFacebookLogin);
        facebookLoginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_status", "user_birthday"));
        facebookLoginButton.registerCallback(callbackManager, facebookCallback);

    }


	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

		Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);

        //GOOGLE SIGN IN
		if (requestCode == RC_SIGN_IN) {
			// If the error resolution was not successful we should not resolve further.
			if (resultCode != RESULT_OK) {
				mShouldResolve = false;
			}

			mIsResolving = false;
			mGoogleApiClient.connect();
        }
        //FACEBOOK SIGN IN
        else{
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

	}

	@Override
	protected void onStart() {
		super.onStart();
		mGoogleApiClient.connect();
	}

	@Override
	protected void onStop() {
		super.onStop();
		mGoogleApiClient.disconnect();
	}

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_IS_RESOLVING, mIsResolving);
        outState.putBoolean(KEY_SHOULD_RESOLVE, mShouldResolve);
    }

	protected void successfulLogin(){
		Intent intent = new Intent(getApplicationContext(),AdActivity.class);
		startActivity(intent);
		finish();
	}

	//------------------Facebook Sign in Methods---------------
    FacebookCallback<LoginResult> facebookCallback =  new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            successfulLogin();
        }

        @Override
        public void onCancel() {
            // App code
        }

        @Override
        public void onError(FacebookException exception) {
            // App code
        }
    };


	//------------------Google Sign in Methods-----------------

	@Override
	public void onConnected(Bundle bundle) {
		// onConnected indicates that an account was selected on the device, that the selected
		// account has granted any requested permissions to our app and that we were able to
		// establish a service connection to Google Play services.
		Log.d(TAG, "onConnected:" + bundle);
		mShouldResolve = false;

		// Show the signed-in UI
		successfulLogin();
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		// Could not connect to Google Play Services.  The user needs to select an account,
		// grant permissions or resolve an error in order to sign in. Refer to the javadoc for
		// ConnectionResult to see possible error codes.
		Log.d(TAG, "onConnectionFailed:" + connectionResult);

		if (!mIsResolving && mShouldResolve) {
			if (connectionResult.hasResolution()) {
				try {
					connectionResult.startResolutionForResult(this, RC_SIGN_IN);
					mIsResolving = true;
				} catch (IntentSender.SendIntentException e) {
					Log.e(TAG, "Could not resolve ConnectionResult.", e);
					mIsResolving = false;
					mGoogleApiClient.connect();
				}
			} else {
				// Could not resolve the connection result, show the user an
				// error dialog.
				showErrorDialog(connectionResult);
			}
		} else {
			// Show the signed-out UI
			//showSignedOutUI();
            showErrorDialog(connectionResult);
		}
	}

	@Override
	public void onConnectionSuspended(int i) {
		// The connection to Google Play services was lost. The GoogleApiClient will automatically
		// attempt to re-connect. Any UI elements that depend on connection to Google APIs should
		// be hidden or disabled until onConnected is called again.
		Log.w(TAG, "onConnectionSuspended:" + i);
	}

	private void setGooglePlusButtonText(SignInButton signInButton) {
		// Search all the views inside SignInButton for TextView
		for (int i = 0; i < signInButton.getChildCount(); i++) {
			View v = signInButton.getChildAt(i);

			// if the view is instance of TextView then change the text SignInButton
			if (v instanceof TextView) {
				TextView tv = (TextView) v;
				tv.setText(getResources().getString(R.string.login_google));
				return;
			}
		}
	}

    private void showErrorDialog(ConnectionResult connectionResult) {
        int errorCode = connectionResult.getErrorCode();

        if (GooglePlayServicesUtil.isUserRecoverableError(errorCode)) {
            // Show the default Google Play services error dialog which may still start an intent
            // on our behalf if the user can resolve the issue.
            GooglePlayServicesUtil.getErrorDialog(errorCode, this, RC_SIGN_IN,
                    new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            mShouldResolve = false;
                            //updateUI(false);
                        }
                    }).show();
        } else {
            // No default Google Play Services error, display a message to the user.
            String errorString = "No default Google Play Services error";
            Toast.makeText(this, errorString, Toast.LENGTH_SHORT).show();

            mShouldResolve = false;
            //updateUI(false);
        }
    }


}
