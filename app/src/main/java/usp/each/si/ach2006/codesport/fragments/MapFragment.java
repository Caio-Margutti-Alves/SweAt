package usp.each.si.ach2006.codesport.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import usp.each.si.ach2006.codesport.R;
import usp.each.si.ach2006.codesport.SessionManager;
import usp.each.si.ach2006.codesport.codeUtils.Util;
import usp.each.si.ach2006.codesport.codeUtils.dialog.ArrayAdapterWithIcon;
import usp.each.si.ach2006.codesport.codeUtils.dialog.DateDialog;
import usp.each.si.ach2006.codesport.codeUtils.dialog.EventDialog;
import usp.each.si.ach2006.codesport.codeUtils.dialog.TimeDialog;
import usp.each.si.ach2006.codesport.codeUtils.gps.Constants;
import usp.each.si.ach2006.codesport.codeUtils.gps.FetchAddressIntentService;
import usp.each.si.ach2006.codesport.models.event.Event;
import usp.each.si.ach2006.codesport.models.user.User;
import yalantis.com.sidemenu.interfaces.ScreenShotable;

/**
 * Created by caioa_000 on 21/02/2015.
 */

public class MapFragment extends Fragment implements ScreenShotable,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    protected static final String TAG = "main-activity";

    protected static final String ADDRESS_REQUESTED_KEY = "address-request-pending";
    protected static final String LOCATION_ADDRESS_KEY = "location-address";

    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    protected boolean mAddressRequested;
    protected String mAddressOutput;
    //private AddressResultReceiver mResultReceiver;
    protected TextView mLocationAddressTextView;

    private MapView mapView;
    private BootstrapCircleThumbnail imgb_mark;
    private BootstrapCircleThumbnail imgb_maps_style;
    private ProgressBar mProgressBar;

    EventDialog event_dialog;
    DateDialog date_dialog;
   // TimeDialog time_dialog;

    protected double lat = 0;
    protected double lng = 0;

    private String[] options;
    private String[] description;
    private Integer[] icons;


    ImageButton.OnClickListener lst_create_mark = new Button.OnClickListener() {
        public void onClick(View view) {

            ListAdapter adapter = new ArrayAdapterWithIcon(getActivity(), options, icons);

            final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

            builder.setTitle(getResources().getString(R.string.dialog_mark));
            builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                   Marker marker = mapView.getMap().addMarker(new MarkerOptions()
                           .position(new LatLng(lat, lng))
                           .title(String.valueOf(options[which]))
                           .snippet(String.valueOf(which)));

                    switch (which) {
                        case Util.MENU_SOCCER:
                            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_soccer));
                            break;
                        case Util.MENU_BASKET:
                            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_basket));
                            break;
                        case Util.MENU_VOLLEY:
                            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_volley));
                            break;
                        case Util.MENU_TENNIS:
                            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_tennis));
                            break;
                        case Util.MENU_BASEBALL:
                            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_baseball));
                            break;
                        case Util.MENU_FOOTBALL:
                            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_football));
                            break;
                    }

                    date_dialog.showDialog(getActivity(), marker);
                }
            });
            builder.show();
        }
    };


    GoogleMap.OnMarkerClickListener lst_mark = new GoogleMap.OnMarkerClickListener() {

        @Override
        public boolean onMarkerClick(Marker marker) {

            User user = ((SessionManager) getActivity().getApplication()).getCurrentUser();

            event_dialog.showDialog(getActivity(), marker , user, "Just a sample for testing the dialog!");
            return true;
        }
    };

    ImageButton.OnClickListener lstnMapsStyle = new Button.OnClickListener() {
        public void onClick(View view) {
            final String[] options = getResources().getStringArray(R.array.maps_styles);

            final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

            builder.setTitle(getResources().getString(R.string.dialog_maps_styles));
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case Util.MENU_NORMAL:
                            mapView.getMap().setMapType(GoogleMap.MAP_TYPE_NORMAL);
                            break;
                        case Util.MENU_HYBRID:
                            mapView.getMap().setMapType(GoogleMap.MAP_TYPE_HYBRID);
                            break;
                        case Util.MENU_SATELLITE:
                            mapView.getMap().setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                            break;
                        case Util.MENU_TERRAIN:
                            mapView.getMap().setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                            break;
                    }
                }
            });
            builder.show();
        }
    };



    public static MapFragment newInstance(String text){
        MapFragment mFragment = new MapFragment();
        Bundle mBundle = new Bundle();
        mBundle.putString(Util.TEXT_FRAGMENT, text);
        mFragment.setArguments(mBundle);
        return mFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        options = getResources().getStringArray(R.array.mark_option);
        description = getResources().getStringArray(R.array.mark_descriptions);
        icons = new Integer[] {R.drawable.icon_soccer, R.drawable.icon_basket,
                R.drawable.icon_volley, R.drawable.icon_tennis, R.drawable.icon_baseball, R.drawable.icon_football };

        event_dialog = new EventDialog();
        date_dialog = new DateDialog();
        //time_dialog = new TimeDialog();

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMap().setMyLocationEnabled(true);

        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        imgb_mark = (BootstrapCircleThumbnail) view.findViewById(R.id.imgb_mark);
        imgb_mark.setOnClickListener(lst_create_mark);

        imgb_maps_style = (BootstrapCircleThumbnail) view.findViewById(R.id.imgb_maps_style);
        imgb_maps_style.setOnClickListener(lstnMapsStyle);

        //mResultReceiver = new AddressResultReceiver(new Handler());

        // Set defaults, then update using values stored in the Bundle.
        mAddressRequested = false;
        mAddressOutput = "";
        updateValuesFromBundle(savedInstanceState);

        updateUIWidgets();
        buildGoogleApiClient();

        mapView.getMap().setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mapView.getMap().getUiSettings().setCompassEnabled(true);

        MapsInitializer.initialize(getActivity());

        setUpCapitalsMarkers();

        mapView.getMap().setOnMarkerClickListener(lst_mark);

        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    public void fetchAddressButtonHandler(View view) {
        // We only start the service to fetch the address if GoogleApiClient is connected.
        if (mGoogleApiClient.isConnected() && mLastLocation != null) {
            startIntentService();
        }
        // If GoogleApiClient isn't connected, we process the user's request by setting
        // mAddressRequested to true. Later, when GoogleApiClient connects, we launch the service to
        // fetch the address. As far as the user is concerned, pressing the Fetch Address button
        // immediately kicks off the process of getting the address.
        mAddressRequested = true;
        updateUIWidgets();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    /*
     * Called when the Activity is going into the background. Parts of the UI
     * may be visible, but the Activity is inactive.
     */
    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    /*
     * Called when the Activity is restarted, even before it becomes visible.
     */
    @Override
    public void onStart() {
        super.onStart();

        mGoogleApiClient.connect();

    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    /**
     * Updates fields based on data stored in the bundle.
     */
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Check savedInstanceState to see if the address was previously requested.
            if (savedInstanceState.keySet().contains(ADDRESS_REQUESTED_KEY)) {
                mAddressRequested = savedInstanceState.getBoolean(ADDRESS_REQUESTED_KEY);
            }
            // Check savedInstanceState to see if the location address string was previously found
            // and stored in the Bundle. If it was found, display the address string in the UI.
            if (savedInstanceState.keySet().contains(LOCATION_ADDRESS_KEY)) {
                mAddressOutput = savedInstanceState.getString(LOCATION_ADDRESS_KEY);
                displayAddressOutput();
            }
        }
    }

    /**
     * Builds a GoogleApiClient. Uses {@code #addApi} to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        // Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            lat = mLastLocation.getLatitude();
            lng = mLastLocation.getLongitude();
            if (!Geocoder.isPresent()) {
                Toast.makeText(getActivity(), R.string.no_geocoder_available, Toast.LENGTH_LONG).show();
                return;
            }
            // It is possible that the user presses the button to get the address before the
            // GoogleApiClient object successfully connects. In such a case, mAddressRequested
            // is set to true, but no attempt is made to fetch the address (see
            // fetchAddressButtonHandler()) . Instead, we start the intent service here if the
            // user has requested an address, since we now have a connection to GoogleApiClient.
            if (mAddressRequested) {
                startIntentService();
            }
        }
    }

    /**
     * Creates an intent, adds location data to it as an extra, and starts the intent service for
     * fetching an address.
     */
    protected void startIntentService() {
        // Create an intent for passing to the intent service responsible for fetching the address.
        Intent intent = new Intent(getActivity(), FetchAddressIntentService.class);

        // Pass the result receiver as an extra to the service.
        //intent.putExtra(Constants.RECEIVER, mResultReceiver);

        // Pass the location data as an extra to the service.
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);

        // Start the service. If the service isn't already running, it is instantiated and started
        // (creating a process for it if needed); if it is running then it remains running. The
        // service kills itself automatically once all intents are processed.
        getActivity().startService(intent);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    /**
     * Updates the address in the UI.
     */
    protected void displayAddressOutput() {
        mLocationAddressTextView.setText(mAddressOutput);
    }

    /**
     * Toggles the visibility of the progress bar. Enables or disables the Fetch Address button.
     */
    private void updateUIWidgets() {
        if (mAddressRequested) {
            mProgressBar.setVisibility(ProgressBar.VISIBLE);
            imgb_mark.setEnabled(false);
        } else {
            mProgressBar.setVisibility(ProgressBar.GONE);
            imgb_mark.setEnabled(true);
        }
    }

    /**
     * Shows a toast with the given text.
     */
    protected void showToast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save whether the address has been requested.
        savedInstanceState.putBoolean(ADDRESS_REQUESTED_KEY, mAddressRequested);

        // Save the address string.
        savedInstanceState.putString(LOCATION_ADDRESS_KEY, mAddressOutput);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void takeScreenShot() {

    }

    @Override
    public Bitmap getBitmap() {
        return null;
    }

    /**
     * Receiver for data sent from FetchAddressIntentService.
     */

    /*
    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }


        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string or an error message sent from the intent service.
            mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
            displayAddressOutput();

            // Show a toast message if an address was found.
            if (resultCode == Constants.SUCCESS_RESULT) {
                showToast(getString(R.string.address_found));
            }

            // Reset. Enable the Fetch Address button and stop showing the progress bar.
            mAddressRequested = false;
            updateUIWidgets();
        }
    }*/


    // requires list of format (lat, lon, lat, lon, lat, lon....)
    private void setUpCapitalsMarkers() {
        String[] capitals = getResources().getStringArray(R.array.capitals);
        ArrayList<Event> events = ((SessionManager) getActivity().getApplication()).getUpcomingEvents();

        int max = getResources().getStringArray(R.array.mark_option).length;
        int min = 0;
        Random random = new Random();

        for(int i = 0; i < capitals.length; i++){

            String[] coord = capitals[i].trim().split(" ");

            int randomNum = random.nextInt((max - min));

            Bitmap bitmap;

            switch (randomNum) {
                case Util.MENU_SOCCER:
                    bitmap = BitmapFactory.decodeResource(getResources(),(R.drawable.icon_soccer));
                    break;
                case Util.MENU_BASKET:
                    bitmap = BitmapFactory.decodeResource(getResources(), (R.drawable.icon_basket));
                    break;
                case Util.MENU_VOLLEY:
                    bitmap = BitmapFactory.decodeResource(getResources(), (R.drawable.icon_volley));
                    break;
                case Util.MENU_TENNIS:
                    bitmap = BitmapFactory.decodeResource(getResources(), (R.drawable.icon_tennis));
                    break;
                case Util.MENU_BASEBALL:
                    bitmap = BitmapFactory.decodeResource(getResources(), (R.drawable.icon_baseball));
                    break;
                case Util.MENU_FOOTBALL:
                    bitmap = BitmapFactory.decodeResource(getResources(), (R.drawable.icon_football));
                    break;
                default:
                    bitmap = BitmapFactory.decodeResource(getResources(), (R.drawable.map_marker));
            }

            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/2,bitmap.getHeight()/2, false);

            Marker marker = mapView.getMap().addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(coord[0]), Double.parseDouble(coord[1])))
                    .title(options[randomNum])
                    .snippet(String.valueOf(randomNum))
                    .icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap)));

            events.get(i).setMarker(marker);

        }

    }

}
