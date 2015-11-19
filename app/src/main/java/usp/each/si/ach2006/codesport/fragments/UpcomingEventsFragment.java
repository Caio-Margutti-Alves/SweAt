package usp.each.si.ach2006.codesport.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;

import usp.each.si.ach2006.codesport.R;
import usp.each.si.ach2006.codesport.SessionManager;
import usp.each.si.ach2006.codesport.codeUtils.LoadProfileImage;
import usp.each.si.ach2006.codesport.codeUtils.Util;
import usp.each.si.ach2006.codesport.models.event.Event;
import usp.each.si.ach2006.codesport.models.user.User;
import yalantis.com.sidemenu.interfaces.ScreenShotable;

/**
 * Created by caioa_000 on 18/11/2015.
 */
public class UpcomingEventsFragment extends ListFragment implements ScreenShotable{

    private Activity activity;

    private ListArrayAdpater adapter = null;
    private LinearLayout layoutList = null;
   // private View loadQuizStatusView;

    private ArrayList<Event> upcomingEvents;

    private static final String TAG = "UpcomingEvents";

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id) {
        Toast.makeText(this.getActivity(), pos, Toast.LENGTH_LONG).show();
    }

    public static UpcomingEventsFragment newInstance(String text){
        UpcomingEventsFragment mFragment = new UpcomingEventsFragment();
        Bundle mBundle = new Bundle();
        mBundle.putString(Util.TEXT_FRAGMENT, text);
        mFragment.setArguments(mBundle);
        return mFragment;
    }

     public void loadUpcomingEvents() {

         upcomingEvents = ((SessionManager) getActivity().getApplication()).getUpcomingEvents();

         String[] values = new String[upcomingEvents.size()];
         String[] descriptions = new String[upcomingEvents.size()];

         int i = 0;
         for(Event event : upcomingEvents){
             values[i] = "Sport: " + event.getMarker().getTitle();
             descriptions[i++] = String.valueOf(event.getDate());
         }

         adapter = new ListArrayAdpater(this.getActivity(), values, descriptions);

         setListAdapter(adapter);
    }

    /*public void refreshList() {
        int i = 0;
        String[] values = new String[quizCollection.getQuizzes().size()];
        String[] descriptions = new String[quizCollection.getQuizzes().size()];

        for(Quiz quiz : quizCollection.getQuizzes()){
            values[i] = "Quiz " + quiz.getId();
            descriptions[i++] = quiz.getDuration() + " seconds";
        }

        adapter = new ListArrayAdpater(this.getActivity(), values, descriptions);
        setListAdapter(adapter);
    }*/

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_upcoming_events, container, false);
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

       // SessioquizCollection = new QuizCollection();

        layoutList = (LinearLayout) rootView.findViewById(R.id.layout_list);
        //SloadQuizStatusView = rootView.findViewById(R.id.layout_load_status);

        loadUpcomingEvents();

        rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putSerializable(TAG_QUIZ_COLLECTION, quizCollection);
    }


    /*public void attemptQuizRetrieve(){
        showProgress(true);
       // authTask = new QuizzesLoadingTask();
       // authTask.execute((Void) null);
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            loadQuizStatusView.setVisibility(View.VISIBLE);
            loadQuizStatusView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            loadQuizStatusView.setVisibility(show ? View.VISIBLE
                                    : View.GONE);
                        }
                    });

            layoutList.setVisibility(View.VISIBLE);
            layoutList.animate().setDuration(shortAnimTime)
                    .alpha(show ? 0 : 1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            layoutList.setVisibility(show ? View.GONE
                                    : View.VISIBLE);
                        }
                    });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            loadQuizStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
            layoutList.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }*/

    /*public class QuizzesLoadingTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                quizCollection.loadAllQuizzes();
            } catch (Exception e) {
                return false;
            }

            if (quizCollection.getQuizzes()== null)return false;
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            authTask = null;
            showProgress(false);

            if (success) {
                refreshList();
            } else {
                new AlertDialog.Builder(getActivity())
                        .setMessage(R.string.dialog_message)
                        .setTitle(R.string.dialog_title)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        }).show();
            }
        }

        @Override
        protected void onCancelled() {
            authTask = null;
            showProgress(false);
        }
    }*/

    public class ListArrayAdpater extends ArrayAdapter<String> {
        private final Context context;
        private final String[] values;
        private final String[] descriptions;

        public ListArrayAdpater(Context context, String[] values, String[] descriptions) {
            super(context, R.layout.list_item_layout, values);
            this.context = context;
            this.values = values;
            this.descriptions = descriptions;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.list_item_layout, parent,false);

            TextView txtTitle = (TextView) rowView.findViewById(R.id.firstLine);
            TextView txtDescription = (TextView) rowView.findViewById(R.id.secondLine);
            ImageView imgvIcon = (ImageView) rowView.findViewById(R.id.icon);

            User user = ((SessionManager) getActivity().getApplication()).getCurrentUser();

            if(user.getFacebookId()!= null){
                new LoadProfileImage(imgvIcon).execute(user.getFacebookPictureUrl());
            }else{
                new LoadProfileImage(imgvIcon).execute(user.getGooglePlusPictureUrl());
            }

            txtTitle.setText(values[position]);
            txtDescription.setText(descriptions[position]);

            return rowView;
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
