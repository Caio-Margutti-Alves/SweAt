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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.beardedhen.androidbootstrap.AwesomeTextView;

import java.io.InputStream;

import usp.each.si.ach2006.codesport.R;
import usp.each.si.ach2006.codesport.SessionManager;
import usp.each.si.ach2006.codesport.codeUtils.LoadProfileImage;
import usp.each.si.ach2006.codesport.codeUtils.Util;
import usp.each.si.ach2006.codesport.models.user.User;
import yalantis.com.sidemenu.interfaces.ScreenShotable;

public class ProfileFragment extends Fragment implements ScreenShotable{

    private Activity activity;

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

    private ImageView imgvProfile;
    private static final int ACTION_TAKE_PHOTO = 1;

    private View loginFormView;
    private View loginStatusView;

    private AwesomeTextView edtFirstName;
    private AwesomeTextView edtLastName;
    private AwesomeTextView edtEmail;
    private AwesomeTextView edtAge;
    private AwesomeTextView edtGender;

    public static ProfileFragment newInstance(String text){
        ProfileFragment mFragment = new ProfileFragment();
        Bundle mBundle = new Bundle();
        mBundle.putString(Util.TEXT_FRAGMENT, text);
        mFragment.setArguments(mBundle);
        return mFragment;
    }
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
		super.onCreate(savedInstanceState);

        edtFirstName = (AwesomeTextView) rootView.findViewById(R.id.firstName);
        edtLastName = (AwesomeTextView) rootView.findViewById(R.id.lastName);
        edtEmail = (AwesomeTextView) rootView.findViewById(R.id.email);
        edtAge= (AwesomeTextView) rootView.findViewById(R.id.edtAge);
        edtGender = (AwesomeTextView) rootView.findViewById(R.id.edtGender);

        loginFormView = rootView.findViewById(R.id.login_form);
        loginStatusView = rootView.findViewById(R.id.login_status);

        imgvProfile = (ImageView) rootView.findViewById(R.id.img_profile);

		try{
            User user = ((SessionManager) getActivity().getApplication()).getCurrentUser();

            if(user.getFacebookId()!= null){
                new LoadProfileImage(imgvProfile).execute(user.getFacebookPictureUrl());
            }else{
                new LoadProfileImage(imgvProfile).execute(user.getGooglePlusPictureUrl());
            }

            edtFirstName.setText(user.getFirstName());
            edtLastName.setText(user.getLastName());
            edtEmail.setText(user.getEmail());
            edtAge.setText(String.valueOf(user.getAge()) + " years");
            edtGender.setText(user.getGender());
			
		}catch(Exception ex){
            Log.e("ProfileFragment", ex.getMessage());
            ex.printStackTrace();
            //edtFirstName.setText("Default");
			//edtLastName.setText("Name");
			//edtAge.setText("Default Age");
			//edtEmail.setText("Default Email");
			//profilePicture.setProfileId(User.getFacebookId());
		}

		rootView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		return rootView;
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
}

