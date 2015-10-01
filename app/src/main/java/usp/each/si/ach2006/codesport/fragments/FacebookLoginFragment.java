package usp.each.si.ach2006.codesport.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

import usp.each.si.ach2006.codesport.models.user.User;

public class FacebookLoginFragment extends Fragment {

	private String facebookId;
	private String firstName;
	private String lastName;
	private String mobileToken;
	private String gender;
	private String email;
	private String dob;

    private AccessToken token;
    private AccessTokenTracker tokenTracker;

    CallbackManager callbackManager;

	private static final String TAG = "MainFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this.getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        tokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.
            }
        };
        // If the access token is available already assign it.
        token = AccessToken.getCurrentAccessToken();


        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        facebookUserQueryData();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tokenTracker.stopTracking();
    }

	public void newUser(String facebookId, String login, String password, String mobileToken,
			String firstName, String lastName, String email, String gender,
			String dob) {

		/*if(User.newUser(facebookId, login, password, mobileToken, firstName, lastName, email,
				String.valueOf(gender), dob)){
		
			User.getUserByFacebookId(facebookId);
		}*/
	}

    public void facebookUserQueryData(){
        GraphRequest request = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        // Application code
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,gender, birthday");

        request.setParameters(parameters);
        request.executeAsync();
    }

}
