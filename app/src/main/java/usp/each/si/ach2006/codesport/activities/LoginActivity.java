package usp.each.si.ach2006.codesport.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import usp.each.si.ach2006.codesport.R;
import usp.each.si.ach2006.codesport.SessionManager;
import usp.each.si.ach2006.codesport.models.user.User;

public class LoginActivity extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener {

    private String TAG = "SweAt";

    protected void successfulLogin(){
        Intent intent = new Intent(getApplicationContext(),AdActivity.class);
        startActivity(intent);
        finish();
    }

    //------------------Facebook Sign in Methods---------------

    private CallbackManager callbackManager;

    FacebookCallback<LoginResult> facebookCallback =  new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            // App code
            Profile profile = Profile.getCurrentProfile();
            Log.d("Facebook Login", "----------------------------------");
            Log.d("onSuccess", "--------" + loginResult.getAccessToken());
            Log.d("Token", "--------" + loginResult.getAccessToken().getToken());
            Log.d("Permision", "--------" + loginResult.getRecentlyGrantedPermissions());
            Log.d("ProfileDataNameF", "--" + profile.getFirstName());
            Log.d("ProfileDataNameL", "--" + profile.getLastName());
            Log.d("Image URI", "--" + profile.getLinkUri());
            Log.d("OnGraph", "------------------------");

            User currentUser = new User();

            currentUser.setFirstName(profile.getFirstName());
            currentUser.setLastName(profile.getLastName());
            currentUser.setFacebookId(profile.getId());
            currentUser.setFacebookPictureUrl();
            currentUser.setFacebookMobileToken(loginResult.getAccessToken().getToken());

            ((SessionManager) getApplication()).setCurrentUser(currentUser);

            GraphRequest request = GraphRequest.newMeRequest( loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            try{
                                Log.d("LoginActivity", response.toString());

                                User currentUser = ((SessionManager) getApplication()).getCurrentUser();

                                currentUser.setEmail(object.getString("email"));
                                currentUser.setDob(object.getString("birthday"));
                                currentUser.setGender(object.getString("gender"));

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }


                        }
                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender, birthday");
            request.setParameters(parameters);
            request.executeAsync();

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



    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.

            Person personProfile =  Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

            // App code
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.d("Google+ Login", "----------------------------------");
            Log.d("ID", "--------" + acct.getId());
            Log.d("ProfileDataName", "--" + acct.getDisplayName());
            Log.d("Token", "--------" + acct.getIdToken());
            Log.d("Permision", "--------" + acct.getGrantedScopes());
            Log.d("Email", "--" + acct.getEmail());
            Log.d("Image URI", "--" + acct.getPhotoUrl());
            Log.d("Name", "--" + personProfile.getDisplayName());
            Log.d("Has Birthday", "--" + personProfile.hasBirthday());
            Log.d("Birthday", "--" + personProfile.getBirthday());

            String[] names = acct.getDisplayName().split(" ");
            User currentUser = new User();

            currentUser.setFirstName(names[0]);
            currentUser.setLastName(names[names.length - 1]);
            currentUser.setEmail(acct.getEmail());
            currentUser.setGooglePlusId(acct.getId());
            currentUser.setGooglePlusPictureUrl(acct.getPhotoUrl().toString());
            currentUser.setGooglePlusMobileToken(acct.getIdToken());
            currentUser.setGender(personProfile.getBirthday());

            int gender = personProfile.getGender();

            if(Person.Gender.FEMALE ==  gender) {
                currentUser.setGender("Female");
            }else{
                currentUser.setGender("Male");
            }

            ((SessionManager) getApplication()).setCurrentUser(currentUser);

            successfulLogin();
        } else {
            // Signed out, show unauthenticated UI.
            //updateUI(false);
        }
    }

    Button.OnClickListener lstnGoogleLogin = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            signIn();
        }
    };

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
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

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    //----------------------------HIBRIDOS----------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Facebook Requires this step before starting UI
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        //

        setContentView(R.layout.activity_login);


        LoginButton facebookLoginButton = (LoginButton) findViewById(R.id.btnFacebookLogin);
        facebookLoginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_status", "user_birthday"));
        facebookLoginButton.registerCallback(callbackManager, facebookCallback);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestScopes(Plus.SCOPE_PLUS_LOGIN, Plus.SCOPE_PLUS_PROFILE)
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Plus.API)
                .build();

        SignInButton googleLoginButton = (SignInButton) findViewById(R.id.btnGoogleLogin);
        googleLoginButton.setOnClickListener(lstnGoogleLogin);
        setGooglePlusButtonText(googleLoginButton);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);

        //GOOGLE SIGN IN
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        //FACEBOOK SIGN IN
        else{
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

}
