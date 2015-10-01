package usp.each.si.ach2006.codesport.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.beardedhen.androidbootstrap.BootstrapButton;

import java.io.InputStream;

import usp.each.si.ach2006.codesport.R;
import usp.each.si.ach2006.codesport.codeUtils.Util;
import usp.each.si.ach2006.codesport.models.user.User;
import yalantis.com.sidemenu.interfaces.ScreenShotable;

public class ProfileFragment extends Fragment implements ScreenShotable{

    private Activity activity;

    private UpdateUserTask registerTask = null;

    private static String id;
	private static String firstName;
	private static String lastName;
	private static String login;
	private static String password;
	private static String rePassword;
	private static String mobileToken;
	// private static Bitmap profilePicture;
	private static int gender;
	private static String email;
	private static String dob;

    private BootstrapButton btnTakePhoto;
    private ImageView imgvProfile;
    private static final int ACTION_TAKE_PHOTO = 1;

    private View loginFormView;
    private View loginStatusView;

    private BootstrapButton btnRegister;

    private EditText edtLogin;
    private EditText edtPassword;
    private EditText edtFirstName;
    private EditText edtLastName;
    private EditText edtEmail;
    private EditText edtRePassword;

    public static ProfileFragment newInstance(String text){
        ProfileFragment mFragment = new ProfileFragment();
        Bundle mBundle = new Bundle();
        mBundle.putString(Util.TEXT_FRAGMENT, text);
        mFragment.setArguments(mBundle);
        return mFragment;
    }

    Button.OnClickListener lstPhotoOnClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    Button.OnClickListener lstnRegister = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            attemptUpdate();
        }
    };
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
		super.onCreate(savedInstanceState);
        edtFirstName = (EditText) rootView.findViewById(R.id.firstName);
        edtLastName = (EditText) rootView.findViewById(R.id.lastName);
        edtEmail = (EditText) rootView.findViewById(R.id.email);

        loginFormView = rootView.findViewById(R.id.login_form);
        loginStatusView = rootView.findViewById(R.id.login_status);

        btnRegister.setOnClickListener(lstnRegister);

        btnTakePhoto.setOnClickListener(lstPhotoOnClickListener);
        imgvProfile = (ImageView) rootView.findViewById(R.id.imgvProfile);

		try{
            edtFirstName.setText(User.getFirstName());
			edtLastName.setText(User.getLastName());
			//edtAge.setText(String.valueOf(User.getAge()));
			edtEmail.setText(User.getEmail());
			edtLogin.setText(User.getLogin());

            if(User.getFacebookId()!= null){
                new LoadProfileImage(imgvProfile).execute(User.getFacebookPictureUrl());
            }else{
                new LoadProfileImage(imgvProfile).execute(User.getGooglePlusPictureUrl());
            }
			//profilePicture.setProfileId(User.getFacebookId());

			
		}catch(Exception ex){
            edtFirstName.setText("Default");
			edtLastName.setText("Name");
			//edtAge.setText("Default Age");
			edtEmail.setText("Default Email");
			edtLogin.setText("Default Login");
			//profilePicture.setProfileId(User.getFacebookId());
		}
		
		
		rootView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT ));		
		return rootView;
	}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }


	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate the menu items for use in the action bar
		super.onCreateOptionsMenu(menu, inflater);		
		inflater.inflate(R.menu.menu_main, menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


    public void attemptUpdate() {
        if (registerTask != null) {
            return;
        }

        // Reset errors.
        edtFirstName.setError(null);
        edtLastName.setError(null);
        edtLogin.setError(null);
        edtPassword.setError(null);
        edtEmail.setError(null);
        edtRePassword.setError(null);

        // Store values at the time of the login attempt.
        id = User.getId();
        firstName = edtFirstName.getText().toString();
        lastName = edtLastName.getText().toString();
        email = edtEmail.getText().toString();
        login = edtLogin.getText().toString();
        password = edtPassword.getText().toString();
        rePassword = edtRePassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password.
        if (TextUtils.isEmpty(password)) {
            edtPassword.setError(getString(R.string.error_field_required));
            focusView = edtPassword;
            cancel = true;
        } else if (password.length() < 8) {
            edtPassword.setError(getString(R.string.error_invalid_password));
            focusView = edtPassword;
            cancel = true;
        }

        // Check for a valid rePassword.
        if (TextUtils.isEmpty(rePassword)) {
            edtRePassword.setError(getString(R.string.error_field_required));
            focusView = edtRePassword;
            cancel = true;
        } else if (!password.equals(rePassword)) {
            edtRePassword.setError(getString(R.string.error_invalid_repassword));
            focusView = edtRePassword;
            cancel = true;
        }

        // Check for a valid login.
        if (TextUtils.isEmpty(login)) {
            edtLogin.setError(getString(R.string.error_field_required));
            focusView = edtLogin;
            cancel = true;
        } else if (login.length() < 6) {
            edtLogin.setError(getString(R.string.error_invalid_login));
            focusView = edtLogin;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            edtEmail.setError(getString(R.string.error_field_required));
            focusView = edtEmail;
            cancel = true;
        } else if (!email.contains("@")) {
            edtEmail.setError(getString(R.string.error_invalid_email));
            focusView = edtEmail;
            cancel = true;
        }

        // Check for a valid first and last name.
        if (TextUtils.isEmpty(lastName)) {
            edtLastName.setError(getString(R.string.error_field_required));
            focusView = edtLastName;
            cancel = true;
        }

        if (TextUtils.isEmpty(firstName)) {
            edtFirstName.setError(getString(R.string.error_field_required));
            focusView = edtFirstName;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            registerTask = new UpdateUserTask();
            registerTask.execute((Void) null);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(
                    android.R.integer.config_shortAnimTime);

            loginStatusView.setVisibility(View.VISIBLE);
            loginStatusView.animate().setDuration(shortAnimTime)
                    .alpha(show ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            loginStatusView.setVisibility(show ? View.VISIBLE
                                    : View.GONE);
                        }
                    });

            loginFormView.setVisibility(View.VISIBLE);
            loginFormView.animate().setDuration(shortAnimTime)
                    .alpha(show ? 0 : 1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            loginFormView.setVisibility(show ? View.GONE
                                    : View.VISIBLE);
                        }
                    });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            loginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
            loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void takeScreenShot() {

    }

    @Override
    public Bitmap getBitmap() {
        return null;
    }

    public class UpdateUserTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                //User.updateUser(id, login, password, firstName, lastName,email);
            } catch (Exception e) {
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            registerTask = null;
            showProgress(false);

            if (success) {
                ((ProfileFragmentInterface)activity).onUserUpdated();
            } else {

                //TODO
                edtPassword.setText("");
                edtPassword.setError(getString(R.string.error_incorrect_combination));
                edtPassword.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            registerTask = null;
            showProgress(false);
        }
    }

    public interface ProfileFragmentInterface {
        public void onUserUpdated();
    }

    /**
     * Background Async task to load user profile picture from url
     * */
    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}

