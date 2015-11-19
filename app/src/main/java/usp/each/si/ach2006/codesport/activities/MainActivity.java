package usp.each.si.ach2006.codesport.activities;

import android.app.ActionBar;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;
import usp.each.si.ach2006.codesport.R;
import usp.each.si.ach2006.codesport.SessionManager;
import usp.each.si.ach2006.codesport.codeUtils.Util;
import usp.each.si.ach2006.codesport.fragments.MapFragment;
import usp.each.si.ach2006.codesport.fragments.ProfileFragment;
import usp.each.si.ach2006.codesport.fragments.UpcomingEventsFragment;
import usp.each.si.ach2006.codesport.models.event.Event;
import usp.each.si.ach2006.codesport.models.user.User;
import yalantis.com.sidemenu.interfaces.Resourceble;
import yalantis.com.sidemenu.interfaces.ScreenShotable;
import yalantis.com.sidemenu.model.SlideMenuItem;
import yalantis.com.sidemenu.util.ViewAnimator;

/**
 * Created by caioa_000 on 11/09/2015.
 */

public class MainActivity extends ActionBarActivity implements GoogleApiClient.OnConnectionFailedListener, ViewAnimator.ViewAnimatorListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private List<SlideMenuItem> list = new ArrayList<>();
    private MapFragment contentFragment;
    private ViewAnimator viewAnimator;
    private LinearLayout linearLayout;

    private BitmapDrawable profilePicture;

    private GoogleApiClient mGoogleApiClient;

    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contentFragment = new MapFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, contentFragment)
                .commit();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        linearLayout = (LinearLayout) findViewById(R.id.left_drawer);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
            }
        });

        setActionBar();
        createMenuList();
        viewAnimator = new ViewAnimator<>(this, list, contentFragment, drawerLayout, this);

        //mGoogleApiClient = ((SessionManager) this.getApplication()).getGoogleApiClient();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        populate();

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    private void createMenuList() {
        SlideMenuItem menuItem0 = new SlideMenuItem(Util.CLOSE, R.drawable.icn_close);
        list.add(menuItem0);
        SlideMenuItem menuItem1 = new SlideMenuItem(Util.MAP, R.drawable.side_menu_map);
        list.add(menuItem1);
        SlideMenuItem menuItem2 = new SlideMenuItem(Util.CALENDAR,  R.drawable.side_menu_calendar);
        list.add(menuItem2);
        SlideMenuItem menuItem3 = new SlideMenuItem(Util.PROFILE,  R.drawable.side_menu_profile);
        list.add(menuItem3);
        SlideMenuItem menuItem4 = new SlideMenuItem(Util.INVITE,  R.drawable.side_menu_share);
        list.add(menuItem4);
        SlideMenuItem menuItem5 = new SlideMenuItem(Util.LOGOFF, R.drawable.side_menu_logoff);
        list.add(menuItem5);
    }


    private void setActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        getSupportActionBar().setIcon(R.drawable.logo_text_orange);
        //getSupportActionBar().setCol

        drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                toolbar,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                linearLayout.removeAllViews();
                linearLayout.invalidate();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (slideOffset > 0.6 && linearLayout.getChildCount() == 0)
                    viewAnimator.showMenuContent();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public ScreenShotable onSwitch(Resourceble slideMenuItem, ScreenShotable fragment, int position) {
        View view = findViewById(R.id.content_frame);
        int finalRadius = Math.max(view.getWidth(), view.getHeight());

        SupportAnimator animator = ViewAnimationUtils.createCircularReveal(view, 0, position, 0, finalRadius);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.setDuration(ViewAnimator.CIRCULAR_REVEAL_ANIMATION_DURATION);

        findViewById(R.id.content_overlay).setBackgroundDrawable(new BitmapDrawable(getResources(), fragment.getBitmap()));
        animator.start();

        ScreenShotable contentFragment = fragment;

        switch (slideMenuItem.getName()) {
            case Util.CLOSE:
                break;
            case Util.MAP:
                contentFragment = MapFragment.newInstance("a");
                break;
            case Util.CALENDAR:
                contentFragment = UpcomingEventsFragment.newInstance("a");
                break;
            case Util.PROFILE:
                contentFragment = ProfileFragment.newInstance("a");
                break;
            case Util.INVITE:
                invite();
                break;
            case Util.LOGOFF:
                logout();
                break;
            default:
                contentFragment = null;
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, (Fragment) contentFragment).commit();
        return contentFragment;
      }

    @Override
    public void disableHomeButton() {
        getSupportActionBar().setHomeButtonEnabled(false);

    }

    @Override
    public void enableHomeButton() {
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerLayout.closeDrawers();

    }

    @Override
    public void addViewToContainer(View view) {
        linearLayout.addView(view);
    }

    public void invite() {

        Resources resources = getResources();

        Intent emailIntent = new Intent();
        emailIntent.setAction(Intent.ACTION_SEND);

        emailIntent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.invite_message));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.subject));
        emailIntent.setType("message/rfc822");

        PackageManager pm = getPackageManager();
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");

        Intent openInChooser = Intent.createChooser(emailIntent, resources.getString(R.string.title_activity_invite));

        List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
        List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();
        for (int i = 0; i < resInfo.size(); i++) {
            // Extract the label, append it, and repackage it in a LabeledIntent
            ResolveInfo ri = resInfo.get(i);
            String packageName = ri.activityInfo.packageName;
            if (packageName.contains("android.email")) {
                emailIntent.setPackage(packageName);
            } else if (packageName.contains("twitter")
                    || packageName.contains("facebook")
                    || packageName.contains("mms")
                    || packageName.contains("android.gm")) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                if (packageName.contains("twitter")) {
                    intent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.invite_message));
                } else if (packageName.contains("facebook")) {
                    intent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.url_invite_message));
                } else if (packageName.contains("mms")) {
                    intent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.invite_message));
                } else if (packageName.contains("android.gm")) {
                    intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(resources.getString(R.string.invite_message)));
                    intent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.subject));
                    intent.setType("message/rfc822");
                }

                intentList.add(new LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon));
            }
        }

        // convert intentList to array
        LabeledIntent[] extraIntents = intentList
                .toArray(new LabeledIntent[intentList.size()]);

        openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
        startActivity(openInChooser);
    }


    protected void logout(){
        try {
            FacebookSdk.sdkInitialize(getApplicationContext());
            LoginManager.getInstance().logOut();
            Log.d("Facebook Sign Out", "Sign out from Facebook acc.");
        }catch(Exception e){
            Log.d("Facebook Sig. Unsuccess", "Failed Signing out from Facebook acc.");
            Log.e("Error", e.toString());
        }

        try{
            if(mGoogleApiClient.isConnected()){
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                Log.d("Google Sign Out", "Sing out of Google Acc. successfull");
            }
        }catch(Exception e){
            Log.d("Google Sig. Unsuccess", "Failed Signing out from Google acc.");
            Log.e("Error", e.toString());
        }

        SessionManager.dispose();

        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
        finish();
    }

    protected void populate(){

        String[] capitals = getResources().getStringArray(R.array.capitals);
        ArrayList<Event> events = new ArrayList();
        User user = ((SessionManager) getApplication()).getCurrentUser();

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        for(int i = 0; i < capitals.length; i++){
            Event event = new Event(dateFormat.format(date).toString(), user);
            events.add(event);
        }

        ((SessionManager) getApplication()).setUpcomingEvents(events);
        //((SessionManager) getApplication()).setAllEvents(events);
    }


}
