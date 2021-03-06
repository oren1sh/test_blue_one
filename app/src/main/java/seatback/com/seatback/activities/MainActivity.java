package seatback.com.seatback.activities;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.crashlytics.android.Crashlytics;
import com.devpaul.bluetoothutillib.SimpleBluetooth;
import com.devpaul.bluetoothutillib.dialogs.DeviceDialog;
import com.devpaul.bluetoothutillib.utils.BluetoothUtility;
import com.devpaul.bluetoothutillib.utils.SimpleBluetoothListener;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import goldzweigapps.tabs.Builder.EasyTabsBuilder;
import goldzweigapps.tabs.Items.TabItem;
import goldzweigapps.tabs.View.EasyTabs;
import seatback.com.seatback.R;
import seatback.com.seatback.application.SeatBackApplication;
import seatback.com.seatback.fragments.HomeFragment;
import seatback.com.seatback.fragments.StatisticFragment;
import seatback.com.seatback.fragments.TipsFragment;
import seatback.com.seatback.fragments.WorkoutFragment;
import seatback.com.seatback.services.TimeService;
import seatback.com.seatback.utils.ColorUtils;
import seatback.com.seatback.utils.Utils;

public class MainActivity extends AppCompatActivity implements WorkoutFragment.OnWorkoutFragmentCreated, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 2001;

    //region variables
    //region time service
    TimeService mTimeService;
    ServiceConnection timeServiceConnection;
    //endregion time service

    //region permission
    private String[] permissions = new String[]{Manifest.permission.BLUETOOTH_ADMIN};
    //endregion permission

    //region bluetooth

    //random int
    private final int BLUETOOTH_PERMISSION_REQUEST_CODE = 399;
    private final int REQUEST_CODE_EMAIL = 450;

    private SimpleBluetooth simpleBluetooth;
    private static final int BLUETOOTH_SCAN_REQUEST = 119;
    private static final int CHOOSE_SERVER_REQUEST = 120;
    private boolean isDeviceConnected = false;
    public boolean isLoggedIn = false;
    protected int refreshHomeViewPeriod = 5000; // how often to refresh the home view, in ms.
    private long connectedDuration = 0;
    private boolean isServiceBound = false;
    private boolean isBluetoothInitized = false;
    private boolean packetStarted = false;
    private CharSequence startChar = "*";
    private CharSequence endChar = "#";
//    ArrayList<Integer> integerDataFull = new ArrayList<>();
    private String dataFull = "", currentBuffer = "";
    private Menu menu = null;
    SeatBackApplication helper = SeatBackApplication.getInstance();
    private static CountDownTimer myCountdownTimer;
    public static boolean didShowError = false;
    //endregion bluetooth

    //region views
    EasyTabs mEasyTabs;
    Toolbar mToolBar;
    //endregion views

    //region fragments
//    HomeFragment homeFragment;
    WorkoutFragment workoutFragment;
    StatisticFragment statisticFragment;
    //endregion fragments
    //endregion variables

    private Bundle savedNotificationBundle = null;

    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    static private boolean didLanchGoogleActivity = false;

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

    private void drawHomeFragment(float percentage, long connectedDuration){
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        HomeFragment homeFragment = null;
        for(Fragment f: fragments){
            if( f instanceof HomeFragment)
                homeFragment = (HomeFragment)f;
        }
        if( homeFragment != null){
            homeFragment.drawPieChart(percentage, connectedDuration);
        }

    }
    @Override
    public void run() {
        Log.d(TAG, "timerRunnable");

        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        long lastConnectedAt = sharedPreferences.getLong("LAST_CONNECTED_TIME", new Date().getTime());
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(new Date()); // compute start of the day for the timestamp
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long from_date = cal.getTimeInMillis() / 1000;
        // in case the last time we have connected is the previous day, remove any existing drawing from the app.
        if( lastConnectedAt < from_date){
            connectedDuration = 0;
            drawHomeFragment(0f, 0);
        }

        if( !isLoggedIn){
            timerHandler.postDelayed(this, refreshHomeViewPeriod );
            return;
        }

        // refresh the data from the server only in case the chair is connected.
        if( isDeviceConnected){
            try{
                connectedDuration = sharedPreferences.getLong("CONNECTED_DURATION", 0);
            } catch(Exception ex){
                sharedPreferences.edit().remove("CONNECTED_DURATION").apply();
            }
            connectedDuration = sharedPreferences.getLong("CONNECTED_DURATION", 0);
            connectedDuration += refreshHomeViewPeriod;
            sharedPreferences.edit().putLong("CONNECTED_DURATION", connectedDuration).apply();
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 999);
            long to_date = cal.getTimeInMillis()/1000;

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("seatback_id", Utils.getConnectecMAC());
                jsonObject.put("user_id", Utils.getUserID(MainActivity.this));
                jsonObject.put("from_time", Long.toString(from_date));
                jsonObject.put("to_time", Long.toString(to_date));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Instantiate the RequestQueue.

            // Request a string response from the provided URL.
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Utils.getServerURL() + "/getData", jsonObject,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d(TAG, "response length="+response.length());
                            String[] badFields = {"bending_forward", "slump", "tilt_left", "tilt_right"};
                            String[] goodFields = {"good"};
                            float percentage = 0;
                            for(int i= 0; i< response.length(); i++){
                                int total = 100;
                                int good = 0, bad = 0;
                                try {
                                    JSONObject obj = response.getJSONObject(i);

                                    total = obj.getInt("total");
                                    for(int index = 0; index < badFields.length; index++){
                                        try {
                                            bad += obj.getInt(badFields[index]);
                                        } catch (JSONException e){
                                        }
                                    }
                                    for(int index = 0; index < goodFields.length; index++){
                                        try {
                                            good += obj.getInt(goodFields[index]);
                                        } catch (JSONException e){
                                        }
                                    }
                                    percentage = (total > 0 ? ((float) (bad) / (float) total) : 0f);
                                }
                                catch (JSONException e){
                                }
                            }
                            drawHomeFragment(percentage, connectedDuration);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if( error.toString().contains("AuthFailureError")) {
                        Log.d(TAG, "VolleyError Auth Error");
                        refreshToken();
                    }
                    else
                        Log.d(TAG, error.toString());
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("api_key", Utils.getAPITokenId());
                    //..add other headers
                    return params;
                }
            };

            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonArrayRequest.setRetryPolicy(policy);
            helper.add(jsonArrayRequest);
        }
        else
            // in case the device is not connected, show a message in the pie chart center location
            // but still show any existing drawing we have there.
            drawHomeFragment(0f, connectedDuration);

        timerHandler.postDelayed(this, refreshHomeViewPeriod );
    }
};

    //region main running place
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(seatback.com.seatback.R.layout.activity_main);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // saving the bundle for later use when all fragments will be created.
            savedNotificationBundle = bundle;
        }

        mAuth = FirebaseAuth.getInstance();

// Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("821587219419-ko36a6tpv91psb5rtvnv6k6eeaf3oijo.apps.googleusercontent.com")
                .requestEmail()
                .build();
        // Build a GoogleApiClient with access to the Google Sign-In API and the
// options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
//        refreshToken();
        //force layout to be ltr
        ViewCompat.setLayoutDirection(getWindow().getDecorView(), ViewCompat.LAYOUT_DIRECTION_LTR);

        //adding first data
//        addFirstEmptyDataToFullSensorsArray();

        //region permission
        //asks for the user bluetooth permission goes in runtime
        askPermission();
        //endregion permission

        //region view inflating
        mToolBar = (Toolbar) findViewById(R.id.toolbar_main);
        mEasyTabs = (EasyTabs) findViewById(R.id.easy_tabs);
        //endregion view inflating

        FirebaseMessaging.getInstance().subscribeToTopic("news");

        //region fragment creation
        //create the fragments
        workoutFragment = new WorkoutFragment();
        statisticFragment = new StatisticFragment();
        //endregion fragment creation

        //region toolbar
        setSupportActionBar(mToolBar);
        mToolBar.setTitleTextColor(ColorUtils.WHITE);
        //endregion toolbar

//        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
//                new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE},
//                false, null, null, null, null);
//
//        try {
//            startActivityForResult(intent, REQUEST_CODE_EMAIL);
//        } catch (ActivityNotFoundException e) {
//            // This device may not have Google Play Services installed.
//            // TODO: do something else
//        }

        //region tabs
        //initializing the tabs library
        EasyTabsBuilder
                .with(mEasyTabs)
                .addTabs(new TabItem(new HomeFragment(), "Home"),
                        new TabItem(workoutFragment, "Workout"),
                        new TabItem(new TipsFragment(), "Tips"),
                        new TabItem(statisticFragment, "Statistic"))
                .addIcons(R.drawable.ic_home_white_24dp,
                        seatback.com.seatback.R.drawable.ic_workout_white_24dp,
                        R.drawable.ic_lightbulb_outline_white_24dp,
                        R.drawable.ic_statistic_white_24dp)
                .setTabsBackgroundColor(ContextCompat.getColor(this, R.color.primary))
                .setIndicatorColor(ColorUtils.WHITE)
                .hideAllTitles(true)
                .setIconFading(true)
                .Build();
        mEasyTabs.getTabLayout().setElevation(Utils.dpToPx(4));
        //endregion tabs

        //region bluetooth

        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);

        //trying to connect automatic to bluetooth device
        setupBluetoothConnection();
        //initializing the bluetooth library

        SimpleBluetoothListener simpleBluetoothListener = new SimpleBluetoothListener() {
            @Override
            public void onBluetoothDataReceived(byte[] bytes, String data) {
                super.onBluetoothDataReceived(bytes, data);
//                Log.d(TAG, "onBluetoothDataReceived: " + data);
                currentBuffer += data;
                while( currentBuffer.contains(endChar)){
                    Log.d(TAG, "currentBuffer=" + currentBuffer);
                    currentBuffer = currentBuffer.replaceAll("[\\\r|\\\n]", "");
                    String[] parts = currentBuffer.split(endChar.toString());
                    dataFull = parts[0];
                    currentBuffer = "";
                    for(int index = 1; index < parts.length; index++){
                        currentBuffer += parts[index];
                    }
                    Log.d(TAG, "datafull="+dataFull);
                    parseDataFromString(dataFull);
                }
//                if( !packetStarted){
//                    if (!data.contains(startChar)) return;
//                    // need to escape regular expression special characters, such as *.
//                    // in case the starChar will change to something which will be different from
//                    // a special characters, than the below line should be change to remove the escape sequence.
//                    String[] parts1 = data.split("\\"+startChar.toString());
//                    if( parts.length > 0)
//                        data = parts[0];
//                    if( parts.length == 2)
//                        data = parts[1];
//                    packetStarted = true;
//                }
//                if (data.contains(startChar))
//                    packetStarted = true;
//                if (!data.contains(endChar)) {
//                    dataFull += data;
//                } else {
//                    //changing the data from the string in order to update the ui
////                    parseDataFromString(dataFull);
//                    data = data.replaceAll("[\\\r|\\\n]", "");
//                    String[] parts1 = data.split(endChar.toString());
//                    if( parts.length > 0) {
//                        dataFull += parts[0] + endChar;
//                    }
//                    else
//                        dataFull += data;
////                    integerDataFull =
//                    Log.d(TAG, "data size: " + dataFull);
//                    parseDataFromString(dataFull);
//                    if( parts.length == 2) {
//                        dataFull = parts[1];
//                    }
//                    else
//                        dataFull = "";
//                    packetStarted = false;
//                }

//                Log.d(TAG, "data size: " + dataFull);

//                    integerDataFull.clear();
            }

            @Override
            public void onDeviceConnected(BluetoothDevice device) {
                super.onDeviceConnected(device);
                //showing a message that device had disconnected
                Snackbar.make(getWindow().getDecorView(),
                        "Connected to " + device.getName(),
                        Snackbar.LENGTH_LONG)
                        .show();
                isDeviceConnected = true;
                packetStarted = false;

                UpdateBluetoothMenuTask myTask = new UpdateBluetoothMenuTask();
                myTask.execute();

                SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("LAST_MAC", device.getAddress());

                long lastConnectedAt = sharedPreferences.getLong("LAST_CONNECTED_TIME", new Date().getTime());
                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                cal.setTime(new Date()); // compute start of the day for the timestamp
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                long from_date = cal.getTimeInMillis();
                if( lastConnectedAt < from_date){
                    editor.putLong("CONNECTED_DURATION", 0);
                }
                editor.putLong("LAST_CONNECTED_TIME", new Date().getTime());
                editor.apply();

                Utils.setConnectecMAC(device.getAddress());
                Utils.setConnectecName(device.getName());

                //running the timer service when the device is connected

                //region timer service
//                final Intent serviceIntent = new Intent(MainActivity.this, TimeService.class);
//                MainActivity.this.startService(serviceIntent);
//
//                timeServiceConnection = new ServiceConnection() {
//                    @Override
//                    public void onServiceConnected(ComponentName name, IBinder service) {
//                        TimeService.TimeBinder timeBinder = (TimeService.TimeBinder) service;
//                        mTimeService = timeBinder.getService();
//                        mTimeService.startService(serviceIntent);
//                    }
//
//                    @Override
//                    public void onServiceDisconnected(ComponentName name) {
//                    }
//                };
//                isServiceBound = getApplicationContext().bindService(serviceIntent, timeServiceConnection, BIND_AUTO_CREATE);
//                Log.d(TAG, "MainActivity isServiceBound="+isServiceBound);
//                //endregion timer service
            }

            @Override
            public void onDeviceDisconnected(BluetoothDevice device) {
                super.onDeviceDisconnected(device);
                //showing a message that device had disconnected
                Snackbar.make(getWindow().getDecorView(),
                        "Disconnected from " + device.getName(),
                        Snackbar.LENGTH_LONG)
                        .show();
                isDeviceConnected = false;

                UpdateBluetoothMenuTask myTask = new UpdateBluetoothMenuTask();
                myTask.execute();
            }

            @Override
            public void onDiscoveryStarted() {
                super.onDiscoveryStarted();
                isDeviceConnected = false;
                UpdateBluetoothMenuTask myTask = new UpdateBluetoothMenuTask();
                myTask.execute();
            }

            @Override
            public void onDiscoveryFinished() {
                super.onDiscoveryFinished();
                isDeviceConnected = false;
                UpdateBluetoothMenuTask myTask = new UpdateBluetoothMenuTask();
                myTask.execute();
            }

            @Override
            public void onDevicePaired(BluetoothDevice device) {
                super.onDevicePaired(device);
            }

            @Override
            public void onDeviceUnpaired(BluetoothDevice device) {
                super.onDeviceUnpaired(device);
            }
        };
        //adds listener to the bluetooth library
        simpleBluetooth.setSimpleBluetoothListener(simpleBluetoothListener);
        //endregion bluetooth
//        registerReceiver(timerReceiver, new IntentFilter(TimeService.COUNTDOWN_PACKAGE));
        registerReceiver(notificationReceiver, new IntentFilter("OPEN_NEW_ACTIVITY"));
        timerHandler.postDelayed(timerRunnable, 0);

    }

    void connectedToChair(String MACAddress){
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        String deviceMAC = MACAddress.length() == 0 ? sharedPreferences.getString("LAST_MAC", ""): MACAddress;
        try {
            if (deviceMAC.length() > 0 && !isDeviceConnected && isBluetoothInitized) {
                simpleBluetooth.connectToBluetoothDevice(deviceMAC);
            }
        }
        catch (IllegalStateException ex){
            Log.d(TAG, "connectedToChair IllegalStateException" + ex.getMessage());
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    private void updateServerWithEmail(){
        String endpointURL = Utils.getServerURL() + "/UpdateUserID";
        // updating the server with the data record
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device_id", Utils.getDeviceID(this.getBaseContext()));
            jsonObject.put("user_id", Utils.getUserID(this.getBaseContext()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Instantiate the RequestQueue.

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(endpointURL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if( error.toString().contains("AuthFailureError")) {
                    Log.d(TAG, "VolleyError Auth Error");
                    refreshToken();
                }
                else
                    Log.d(TAG, "VolleyError=" + error.toString());
            }
        }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("api_key",Utils.getAPITokenId());
                //..add other headers
                return params;
            }
        };
        // Add the request to the RequestQueue.
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        helper.add(jsonObjectRequest);
    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            firebaseAuthWithGoogle(acct);
            Log.d(TAG, "account display name=" + acct.getEmail());
            String token = acct.getIdToken();
            Log.d(TAG, "token="+token);

//            mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
//            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
//            updateUI(false);
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success");
                    FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
                    mUser.getToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                String idToken = task.getResult().getToken();
                                Utils.setAPITokenId(idToken);
                                updateServerWithEmail();
                                isLoggedIn = true;
                                connectedToChair(""); // Send token to your backend via HTTPS
                                // ...
                            } else {
                                // Handle error -> task.getException();
                                Crashlytics.logException(task.getException());
                                Toast.makeText(MainActivity.this, "Getting token failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.getException());
                    Crashlytics.logException(task.getException());
                    Toast.makeText(MainActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                }

                // ...
            }
        });
    }
    //endregion main running place

    private BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            DialogFragment dialog = new YesNoDialog();
            Bundle args = new Bundle();
            args.putString(YesNoDialog.ARG_TITLE, intent.getStringExtra("title"));
            args.putString(YesNoDialog.ARG_MESSAGE, intent.getStringExtra("body"));
            args.putBoolean(YesNoDialog.ARG_SHOW_CANCEL, false);
            args.putInt(YesNoDialog.ARG_INT_VALUE, Integer.parseInt(intent.getStringExtra("posture")));

            dialog.setArguments(args);
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            HomeFragment homeFragment = null;
            WorkoutFragment workoutFragment = null;
            for (Fragment f : fragments) {
                if (f instanceof WorkoutFragment)
                    workoutFragment = (WorkoutFragment) f;
            }
            mEasyTabs.getViewPager().setCurrentItem(1);

            dialog.setTargetFragment(workoutFragment, 100);
            try{
                dialog.show(getSupportFragmentManager(), "tag");
            }
            catch (IllegalStateException ignored){
            }
        }
    };

    //region timer receiver
//    private BroadcastReceiver timerReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (intent.getExtras() != null ) {
//                long millisUntilFinished = intent.getLongExtra("countdown", 0);
////                Log.d(TAG, "onReceive: " + millisUntilFinished);
//                List<Fragment> fragments = getSupportFragmentManager().getFragments();
//                HomeFragment homeFragment = null;
//                for(Fragment f: fragments){
//                    if( f instanceof HomeFragment)
//                        homeFragment = (HomeFragment)f;
//                }
////                if( homeFragment != null)
////                if (homeFragment.circleTimerView != null) {
////                    homeFragment.circleTimerView.setCurrentTime((int) millisUntilFinished);
////                }
//            }
//        }
//    };
    //endregion timer receiver

    //region permission functions
    private void askPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Utils.isPermissionGranted(this, permissions)) {
                requestPermissions(permissions, BLUETOOTH_PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case BLUETOOTH_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && Utils.isPermissionGranted(MainActivity.this, permissions)) {
                    Log.d(TAG, "onRequestPermissionsResult: approved");
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                    System.exit(0);
                    // permission denied, boo! try again
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    //endregion permission functions

    //region menu inflating "icons in toolbar" bluetooth icon is up here
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(seatback.com.seatback.R.menu.toolbar_info_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_info:
                Intent informationActivityIntent = new Intent(MainActivity.this,
                        InformationActivity.class);
                startActivity(informationActivityIntent);
                break;
            case seatback.com.seatback.R.id.menu_bluetooth:
                if (isDeviceConnected) {
//                    simpleBluetooth.sendData(Long.toString(System.currentTimeMillis() / 1000));
                    item.setIcon(R.drawable.ic_bluetooth_connected_white_24dp);
                } else {
                    simpleBluetooth.scan(BLUETOOTH_SCAN_REQUEST);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion menu, inflating "icons in toolbar"

    //region bluetooth functions0
    private void setupBluetoothConnection() {
        if( simpleBluetooth == null)
            simpleBluetooth = new SimpleBluetooth(this, this);
        isBluetoothInitized = simpleBluetooth.initializeSimpleBluetooth();
        simpleBluetooth.setInputStreamType(BluetoothUtility.InputStreamType.BUFFERED);
    }
    //endregion bluetooth functions

    //region bluetooth manually device choosing result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EMAIL && resultCode == RESULT_OK) {
            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            String accountType = data.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE);
            // TODO: do something with the accountName
            return;
        }
         if (requestCode == BLUETOOTH_SCAN_REQUEST || requestCode == CHOOSE_SERVER_REQUEST) {
            if (resultCode == RESULT_OK) {
                if( isBluetoothInitized == false)
                    setupBluetoothConnection();
                if( isBluetoothInitized == true) {
                    String deviceMacAddress = data.getStringExtra(DeviceDialog.DEVICE_DIALOG_DEVICE_ADDRESS_EXTRA);
                    if (requestCode == BLUETOOTH_SCAN_REQUEST) {
                        connectedToChair(deviceMacAddress);
//                        simpleBluetooth.connectToBluetoothDevice(deviceMacAddress);
                    } else {
                        simpleBluetooth.connectToBluetoothServer(deviceMacAddress);
                    }
                }
            }
        }
        if( requestCode == 1001 && resultCode == RESULT_OK){
            connectedToChair("");
        }
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

    }
    //endregion bluetooth manually device choosing result
    @Override
    protected void onDestroy() {
        Log.d(TAG, "MainActivity onDestroy");
        super.onDestroy();
        if( simpleBluetooth != null)
            simpleBluetooth.endSimpleBluetooth();
//        unregisterReceiver(timerReceiver);
        unregisterReceiver(notificationReceiver);
//        if( mTimeService != null)
//            mTimeService.onDestroy();
//        if( timeServiceConnection != null && isServiceBound == true) {
//            getApplicationContext().unbindService(timeServiceConnection);
//            Log.d(TAG, "MainActivity onDestroy unbindservice");
//        }
        unregisterReceiver(mReceiver);
//        unbindService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshToken();
    }

    public void refreshToken(){
        if( !didLanchGoogleActivity ) {
            didLanchGoogleActivity = true;
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser == null) {
                signIn();
            } else {
                FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
                mUser.getToken(true)
                        .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                            public void onComplete(@NonNull Task<GetTokenResult> task) {
                                if (task.isSuccessful()) {
                                    String idToken = task.getResult().getToken();
                                    Utils.setAPITokenId(idToken);
                                    isLoggedIn = true;
                                    didLanchGoogleActivity = false;
                                    connectedToChair("");
                                    updateServerWithEmail();
                                    // Send token to your backend via HTTPS
                                    // ...
                                } else {
                                    didLanchGoogleActivity = false;
                                    Crashlytics.logException(task.getException());
//                                Toast.makeText(MainActivity.this, "Getting token failed.",
//                                        Toast.LENGTH_SHORT).show();
                                    // Handle error -> task.getException();
                                }
                            }
                        });
            }
        }
    }
    private void parseDataFromString(String data) {
        //remove the start tag and the end tag from the full string
        data = data.replace("*", "");
        data = data.replace("#", "");
        data = data.replace("\n", "");
        data = data.replace("\r", "");
        String command  = "";
        //splitting the full string by , to get the values
        simpleBluetooth.sendData(Long.toString(System.currentTimeMillis() / 1000));
        if( data.substring(0, 1).contains("R")) {
            if( data.length() > 3)
                processSensorsData(data.substring(2, data.length()));
        }
        if( data.substring(0, 1).contains("S")) {
            if( data.length() > 3)
                processSD_Data(data.substring(2, data.length()));
        }
    }

    // process the SD data which is an "array" where each element index corresponds
    // to the posture index and the value within that array's cell has the number of samples
    // that were measured using that posture.
    // The posture array is followed by a time stamp
    // The array data might be prefixed with the string "OnOff" which indicates that the sent
    // samples were measured during a time where the device did not have a real-time clock value.
    private void processSD_Data( String originalData) {
        try {
            boolean isOnOff = false;
            String data = originalData;

            if( data.contains("OnOff")){
                isOnOff = true;
                data = data.replaceAll("\\b(OnOff,)\\b", "");
                data = data.replaceAll("\\b(OnOff)\\b", "");
            }
            String[] values = data.split(",");

            int valueIndex = 0;
            if (values.length > 0) {
                Log.d(TAG, "data = " + data);
                Log.d(TAG, "values.length = " + values.length);

                int numberOfPossiblePostures = 0;
                try {
                    if (values[valueIndex].length() > 0)
                        numberOfPossiblePostures = Integer.parseInt(values[valueIndex++]);
                    if (numberOfPossiblePostures < 0 || numberOfPossiblePostures > 8)
                        throw new Exception("Wrong value for SD data");
                    while (values[valueIndex].equals("OnOff")) {
                        Log.d(TAG, "Found OnOff");
//                        data = data.substring(6);
                        isOnOff = true;
                        valueIndex++;
                    }
                } catch (NumberFormatException e) {
                    Crashlytics.log(4, TAG, originalData);
                    Crashlytics.logException(e);
                    e.printStackTrace();
                } catch (Exception generalEx) {
                    Crashlytics.log(4, TAG, originalData);
                    Crashlytics.logException(generalEx);
                }

                String serverData = (isOnOff ? "OnOff," : "");
                serverData += String.valueOf(numberOfPossiblePostures) + ",";

                do {
                    for (int index = 0; index < numberOfPossiblePostures; index++) {
//                Log.d(TAG, "Found for posture " + index  + " the count " + values[startOfRow + index]);
                        if( values[index].length()>0)
                            serverData += values[valueIndex++] + ",";
                    }
//            Log.d(TAG, "Found the following timestamp " + values[startOfRow + numberOfPossiblePostures + 1]);
                    try {
                        String deviceTimestampValue = values[valueIndex];
                        if (isOnOff && values[valueIndex].length()>0) {
                            long deviceTimestamp = Long.parseLong(values[valueIndex]);
                            deviceTimestamp = System.currentTimeMillis() - deviceTimestamp;
                            deviceTimestampValue = String.valueOf(deviceTimestamp / 1000);
                        }
                        serverData += deviceTimestampValue + ",";
                    } catch (NumberFormatException e) {
                        Crashlytics.log(originalData);
                        Crashlytics.logException(e);
                        e.printStackTrace();
                    } catch (Exception generalEx) {
                        Crashlytics.log(originalData);
                        Crashlytics.logException(generalEx);
                    }

                    updateServerWithData(serverData, "0", 0);
                    valueIndex++;
                    serverData = "";
                }
                while (valueIndex < (values.length - 1));
            }
        } catch(Exception ex){
            Crashlytics.log(originalData);
            Crashlytics.logException(ex);
            ex.printStackTrace();
        }
    }

    // data - the sensors string data without the star/end characters and without
    // the command "R" character
    private void processSensorsData( String data) {
        ArrayList<Integer> dataReady = new ArrayList<>();
        String postureIndex = "";

        for (String number : data.split(",")) {
            if( postureIndex.length() == 0){
                postureIndex = number;
                continue;
            }
            //verifying that the number in not null or empty
            if (!(number == null || number.isEmpty())) {
//                trying to convert the string into an int
                try {
                    dataReady.add(Integer.parseInt(number.trim()));
                } catch (NumberFormatException e) {
                    Crashlytics.log(4, TAG, data);
                    Crashlytics.logException(e);
                    e.printStackTrace();
                }
            }
        }

        if( postureIndex.length() == 0) postureIndex = "0";

        String currentTimeStamp = Long.toString(System.currentTimeMillis() / 1000);
        updateServerWithData(currentTimeStamp + "," + data, postureIndex, 1);

        UpdateTipsTask  myTask = new UpdateTipsTask ();
        SensorsData dataToUpdate = new SensorsData();
        dataToUpdate.posture = Integer.parseInt(postureIndex);
        dataToUpdate.dataReady = dataReady;
        myTask.execute(dataToUpdate);
    }

    void updateServerWithData(String data, String postureIndex, int typeOfData){
        String endpointURL = Utils.getServerURL();
        // updating the server with the data record
        JSONObject jsonObject = new JSONObject();
        try {
            if( typeOfData == 0){
                jsonObject.put("sensorsSDData", data);
                endpointURL += "/updateSDData";
            }
            if( typeOfData == 1){
                jsonObject.put("sensorsData", data);
                jsonObject.put("posture", Utils.getPostureName(Integer.parseInt(postureIndex)));
                endpointURL += "/updateSensorsData";
            }

            jsonObject.put("seatback_id", Utils.getConnectecMAC());
            jsonObject.put("seatback_name", Utils.getConnectecName());
            jsonObject.put("user_id", Utils.getUserID(this.getBaseContext()));
            jsonObject.put("device_id", Utils.getDeviceID(this.getBaseContext()));
        } catch (JSONException e) {
            Crashlytics.logException(e);
            e.printStackTrace();
        }

        // Instantiate the RequestQueue.

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(endpointURL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if( error.toString().contains("AuthFailureError")) {
                    Log.d(TAG, "VolleyError Auth Error");
                    refreshToken();
                }
                if( didShowError == false) {
                    DialogFragment dialog = new YesNoDialog();
                    Bundle args = new Bundle();
                    args.putString(YesNoDialog.ARG_TITLE, "Error");
                    String errorMsg = error.toString() + " " + Utils.getServerURL() + " " + Utils.getConnectecName();
                    args.putString(YesNoDialog.ARG_MESSAGE, errorMsg);
                    args.putBoolean(YesNoDialog.ARG_SHOW_CANCEL, true);
                    dialog.setArguments(args);
                    List<Fragment> fragments = getSupportFragmentManager().getFragments();
                    HomeFragment homeFragment = null;
                    for (Fragment f : fragments) {
                        if (f instanceof HomeFragment)
                            homeFragment = (HomeFragment) f;
                    }


                    dialog.setTargetFragment(homeFragment, 100);
//                    dialog.show(getSupportFragmentManager(), "tag");
                    didShowError = true;
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("api_key", Utils.getAPITokenId());
                //..add other headers
                return params;
            }
        };
        // Add the request to the RequestQueue.
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        helper.add(jsonObjectRequest);
    }

    @Override
    public void onWorkoutFragmentCreated() {
        if( savedNotificationBundle != null){
            Intent broadcast = new Intent();
            broadcast.putExtra("title", savedNotificationBundle.getString("title"));
            broadcast.putExtra("body", savedNotificationBundle.getString("body"));
            broadcast.putExtra("posture", savedNotificationBundle.getString("posture"));
            broadcast.setAction("OPEN_NEW_ACTIVITY");
            sendBroadcast(broadcast);

            savedNotificationBundle = null;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, connectionResult.getErrorMessage());
    }

    class UpdateBluetoothMenuTask extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if( menu != null) {
                if (isDeviceConnected == true) {
                    menu.getItem(0).setIcon(R.drawable.ic_bluetooth_connected_white_24dp);
                    new CountDownTimer(2000, 2000) {
                        public void onFinish() {
                            // When timer is finished
                            // Execute your code here
                            simpleBluetooth.sendData(Long.toString(System.currentTimeMillis() / 1000));
                        }

                        public void onTick(long millisUntilFinished) {
                            // millisUntilFinished    The amount of time until finished.
                        }
                    }.start();

                    if( myCountdownTimer != null)
                        myCountdownTimer.cancel();
                    myCountdownTimer = null;
                    SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
                    String deviceMAC = sharedPreferences.getString("LAST_MAC", "");
                    try {
//                        if (deviceMAC.length() > 0) {
//                            simpleBluetooth.sendData("1");
//                        }
                    }
                    catch (IllegalStateException ex){
                        Log.d(TAG, "onStart IllegalStateException" + ex.getMessage());
                    }

                }
                else {
                    menu.getItem(0).setIcon(R.drawable.ic_bluetooth_white_24dp);
                    // start a periodic task, trying to connect to the Seatback device
                    if( myCountdownTimer == null)
                    myCountdownTimer =    new CountDownTimer(1800000, 10000) {
                        public void onTick(long millisUntilFinished) {
                            if( isLoggedIn == true)
                                connectedToChair("");
                        }

                        public void onFinish() {
                        }
                    }.start();
                }
            }
        }
    }

    class UpdateTipsTask extends AsyncTask<SensorsData, Void, SensorsData>
    {

        @Override
        protected void onPostExecute(SensorsData data) {
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            TipsFragment tipsFragment = null;
            HomeFragment homeFragment = null;
            for(Fragment f: fragments){
                if( f instanceof TipsFragment)
                    tipsFragment = (TipsFragment)f;
                if( f instanceof HomeFragment)
                    homeFragment = (HomeFragment)f;
            }
            if (tipsFragment != null)
                if (tipsFragment.bottomPressureMap != null && tipsFragment.topPressureMap != null) {

                    tipsFragment.bottomPressureMap.setTop(false);
                    tipsFragment.bottomPressureMap.setColors(data.dataReady);

                    tipsFragment.topPressureMap.setTop(true);
                    tipsFragment.topPressureMap.setColors(data.dataReady);
                }

            if( homeFragment != null && !MainActivity.this.isDestroyed()) {
                homeFragment.setImageText(data.posture);
                if (homeFragment.positionImageView != null ) {
                    Integer imageAfterComparing = Utils.getImageAfterComparing(data.posture);
                    if (imageAfterComparing != null) {
                        Glide.with(MainActivity.this)
                                .load(imageAfterComparing)
                                .into(homeFragment.positionImageView);
                    }
                }
            }
        }

        @Override
        protected SensorsData doInBackground(SensorsData... params) {
            return params[0];
        }

    }
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        isDeviceConnected = false;
                        UpdateBluetoothMenuTask myTask = new UpdateBluetoothMenuTask();
                        myTask.execute();

                        setupBluetoothConnection();
                        Log.d(TAG,"Bluetooth off");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG,"Turning Bluetooth off...");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG,"Bluetooth on");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG,"Turning Bluetooth on...");
                        break;
                }
            }
        }
    };

    protected class SensorsData{
        public int posture;
        ArrayList<Integer> dataReady;
    }

    public static class YesNoDialog extends android.support.v4.app.DialogFragment
    {
        public static final String ARG_TITLE = "YesNoDialog.Title";
        public static final String ARG_MESSAGE = "YesNoDialog.Message";
        public static final String ARG_SHOW_CANCEL = "YesNoDialog.ShowCancel";
        public static final String ARG_INT_VALUE = "YesNoDialog.IntValue";

        public YesNoDialog()
        {
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState)
        {
            Bundle args = getArguments();
            String title = args.getString(ARG_TITLE);
            String message = args.getString(ARG_MESSAGE);
            boolean showCancel = args.getBoolean(ARG_SHOW_CANCEL);
            final int imageId = args.getInt(ARG_INT_VALUE);

            if( showCancel == true)
                return new AlertDialog.Builder(getActivity())
                        .setTitle(title)
                        .setMessage(message)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                didShowError = false;
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                getTargetFragment().onActivityResult(getTargetRequestCode(), 101, null);
                            }
                        })
                        .create();
            else
                return new AlertDialog.Builder(getActivity())
                        .setTitle(title)
                        .setMessage(message)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                getTargetFragment().onActivityResult(getTargetRequestCode(), imageId, null);
                            }
                        })
                        .create();
        }
    }
}

