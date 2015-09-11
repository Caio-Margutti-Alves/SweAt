
package usp.each.si.ach2006.codesport.drawer.navigation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import usp.each.si.ach2006.codesport.R;
import usp.each.si.ach2006.codesport.drawer.adapter.NavigationAdapter;
import usp.each.si.ach2006.codesport.drawer.fragment.MapFragment;
import usp.each.si.ach2006.codesport.drawer.fragment.ProfileFragment;
import usp.each.si.ach2006.codesport.drawer.utils.Constant;
import usp.each.si.ach2006.codesport.drawer.utils.Menus;
import usp.each.si.ach2006.codesport.drawer.utils.Utils;
import usp.each.si.ach2006.codesport.models.user.User;

public class NavigationMain extends ActionBarActivity implements ProfileFragment.ProfileFragmentInterface {

    private int mLastPosition = 0;
	private ListView mListDrawer;    

	private DrawerLayout mLayoutDrawer;
	private RelativeLayout mUserDrawer;
	private RelativeLayout mRelativeDrawer;

    private TextView txtv_first_name;
    private TextView txtv_last_name;

	private FragmentManager mFragmentManager;
	private NavigationAdapter mNavigationAdapter;
	private ActionBarDrawerToggleCompat mDrawerToggle;

    private static final String TAG_QUIZ_COLLECTION = "QuizCollectionFragment";
    private static final String TAG_CREATE_QUIZ = "CreateQuizFragment";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		getSupportActionBar().setIcon(R.drawable.ic_launcher);
		
		setContentView(R.layout.navigation_main);		
		
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);		
        
        mListDrawer = (ListView) findViewById(R.id.listDrawer);        
		mRelativeDrawer = (RelativeLayout) findViewById(R.id.relativeDrawer);		
		mLayoutDrawer = (DrawerLayout) findViewById(R.id.layoutDrawer);

        txtv_first_name = (TextView) findViewById(R.id.txt_user_name_drawer);
        txtv_last_name = (TextView) findViewById(R.id.txt_user_lastname_drawer);

        txtv_first_name.setText(User.getFirstName());
        txtv_last_name.setText(User.getLastName());


		mUserDrawer = (RelativeLayout) findViewById(R.id.userDrawer);
		mUserDrawer.setOnClickListener(userOnClick);
		
		if (mListDrawer != null) {

			// All header menus should be informed here
			// listHeader.add(MENU POSITION)			
			List<Integer> mListHeader = new ArrayList<Integer>();

			// All menus which will contain an accountant should be informed here
			// Counter.put ("POSITION MENU", "VALUE COUNTER");			
			SparseIntArray  mCounter = new SparseIntArray();			
			mCounter.put(Constant.MENU_MAPS,6);
			mNavigationAdapter = new NavigationAdapter(this, NavigationList.getNavigationAdapter(this, mListHeader, mCounter, null));
		}
		
		mListDrawer.setAdapter(mNavigationAdapter);
		mListDrawer.setOnItemClickListener(new DrawerItemClickListener());

		mDrawerToggle = new ActionBarDrawerToggleCompat(this, mLayoutDrawer);		
		mLayoutDrawer.setDrawerListener(mDrawerToggle);
		
		if (savedInstanceState != null) { 			
			setLastPosition(savedInstanceState.getInt(Constant.LAST_POSITION));
			
			setTitleFragments(mLastPosition);			
			mNavigationAdapter.resetarCheck();		
			mNavigationAdapter.setChecked(mLastPosition, true);							
	    }else{
	    	setLastPosition(mLastPosition); 
	    	setFragmentList(mLastPosition);
	    }

	}

    private void setFragmentList(int posicao){

        Fragment mFragment = null;
        mFragmentManager = getSupportFragmentManager();

        switch (posicao) {
            case Constant.MENU_MAPS:
                mFragment = new MapFragment().newInstance(Utils.getTitleItem(NavigationMain.this, Constant.MENU_MAPS));
                break;
            case Constant.MENU_PROFILE:
                mFragment = new ProfileFragment().newInstance(Utils.getTitleItem(NavigationMain.this, Constant.MENU_PROFILE));
                break;
            case Constant.MENU_SETTINGS:
                //mFragment = new SettingsFragment().newInstance(Utils.getTitleItem(NavigationMain.this, Constant.MENU_SETTINGS));
                break;
            case Constant.MENU_INVITE:
                invite();
                break;
        }

        if (mFragment != null){
            setTitleFragments(mLastPosition);
            mNavigationAdapter.resetarCheck();
            mNavigationAdapter.setChecked(posicao, true);
            mFragmentManager.beginTransaction().replace(R.id.content_frame, mFragment).commit();
        }
    }


    private void hideMenus(Menu menu, int posicao) {
        boolean drawerOpen = mLayoutDrawer.isDrawerOpen(mRelativeDrawer);

        switch (posicao) {
            case R.id.action_settings:
                menu.getItem(R.id.action_new).setVisible(false);
                break;
            default:
                //menu.getItem(R.id.action_new).setVisible(true);
                break;
		}  
    }	

	private void setTitleFragments(int position){	
		setIconActionBar(Utils.iconNavigation[position]);
		setSubtitleActionBar(Utils.getTitleItem(NavigationMain.this, position));				
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub		
		super.onSaveInstanceState(outState);		
		outState.putInt(Constant.LAST_POSITION, mLastPosition);					
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case Menus.HOME:
			if (mLayoutDrawer.isDrawerOpen(mRelativeDrawer)) {
				mLayoutDrawer.closeDrawer(mRelativeDrawer);
			} else {
				mLayoutDrawer.openDrawer(mRelativeDrawer);
			}
			return true;
		default:

	        if (mDrawerToggle.onOptionsItemSelected(item)) {
	            return true;
	        }

			return super.onOptionsItemSelected(item);
		}		             
    }
		
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	hideMenus(menu, mLastPosition);
        return super.onPrepareOptionsMenu(menu);  
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);	     
	    mDrawerToggle.syncState();	
	 }	
	
	public void setTitleActionBar(CharSequence informacao) {    	
    	getSupportActionBar().setTitle(informacao);
    }	
	
	public void setSubtitleActionBar(CharSequence informacao) {    	
    	getSupportActionBar().setSubtitle(informacao);
    }	

	public void setIconActionBar(int icon) {    	
    	getSupportActionBar().setIcon(icon);
    }	
	
	public void setLastPosition(int posicao){		
		this.mLastPosition = posicao;
	}	
		
	private class ActionBarDrawerToggleCompat extends ActionBarDrawerToggle {

		public ActionBarDrawerToggleCompat(Activity mActivity, DrawerLayout mDrawerLayout){
			super(
			    mActivity,
			    mDrawerLayout, 
  			    R.drawable.ic_action_navigation_drawer, 
				R.string.drawer_open,
				R.string.drawer_close);
		}
		
		@Override
		public void onDrawerClosed(View view) {			
			supportInvalidateOptionsMenu();				
		}

		@Override
		public void onDrawerOpened(View drawerView) {	
			mNavigationAdapter.notifyDataSetChanged();			
			supportInvalidateOptionsMenu();			
		}		
	}
		  
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);		
	}
	
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int posicao, long id) {          	        	
	    	setLastPosition(posicao);        	
	    	setFragmentList(mLastPosition);	    	
	    	mLayoutDrawer.closeDrawer(mRelativeDrawer);	 
        }
    }	
    
	private OnClickListener userOnClick = new OnClickListener() {		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mLayoutDrawer.closeDrawer(mRelativeDrawer);
		}
	};	
	
	  public void invite() {

			Resources resources = getResources();

			Intent emailIntent = new Intent();
			emailIntent.setAction(Intent.ACTION_SEND);

			emailIntent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.invite_message));
			emailIntent.putExtra(Intent.EXTRA_SUBJECT,resources.getString(R.string.subject));
			emailIntent.setType("message/rfc822");

			PackageManager pm = getPackageManager();
			Intent sendIntent = new Intent(Intent.ACTION_SEND);
			sendIntent.setType("text/plain");

			Intent openInChooser = Intent.createChooser(emailIntent,resources.getString(R.string.title_activity_invite));

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
					intent.setComponent(new ComponentName(packageName,ri.activityInfo.name));
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
						intent.putExtra(Intent.EXTRA_SUBJECT,resources.getString(R.string.subject));
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

    public void onUserUpdated(){
        Fragment mFragment = new ProfileFragment().newInstance(Utils.getTitleItem(NavigationMain.this, Constant.MENU_MAPS));
        mNavigationAdapter.resetarCheck();
        //mNavigationAdapter.setChecked(posicao, true);

        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.content_frame, mFragment).addToBackStack(TAG_QUIZ_COLLECTION).commit();

        new AlertDialog.Builder(this)
                .setMessage(R.string.user_updated)
                .setTitle(R.string.user_updated_title)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                }).show();
    }


}
