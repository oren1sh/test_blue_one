package goldzweigapps.com.seatback.activities;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.devpaul.bluetoothutillib.SimpleBluetooth;
import com.devpaul.bluetoothutillib.dialogs.DeviceDialog;
import com.devpaul.bluetoothutillib.utils.SimpleBluetoothListener;
import com.devpaul.bluetoothutillib.utils.BluetoothUtility;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.VolleyError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import goldzweigapps.com.seatback.R;
import goldzweigapps.com.seatback.application.SeatBackApplication;
import goldzweigapps.com.seatback.fragments.HomeFragment;
import goldzweigapps.com.seatback.fragments.StatisticFragment;
import goldzweigapps.com.seatback.fragments.TipsFragment;
import goldzweigapps.com.seatback.fragments.WorkoutFragment;
import goldzweigapps.com.seatback.services.TimeService;
import goldzweigapps.com.seatback.utils.ColorUtils;
import goldzweigapps.com.seatback.utils.Utils;
import goldzweigapps.tabs.Builder.EasyTabsBuilder;
import goldzweigapps.tabs.Items.TabItem;
import goldzweigapps.tabs.View.EasyTabs;

public class MainActivity extends AppCompatActivity{
    private static final String TAG = MainActivity.class.getSimpleName();

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

    private SimpleBluetooth simpleBluetooth;
    private static final int BLUETOOTH_SCAN_REQUEST = 119;
    private static final int CHOOSE_SERVER_REQUEST = 120;
    private boolean isDeviceConnected = false;
    private boolean isServiceBound = false;
    private boolean isBluetoothInitized = false;
    private String connectedMAC = "";
    private CharSequence startChar = "*";
    private CharSequence endChar = "#";
    ArrayList<Integer> integerDataFull = new ArrayList<>();
    String dataFull = "";
    private Menu menu = null;
    SeatBackApplication helper = SeatBackApplication.getInstance();
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

    //region main running place
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //force layout to be ltr
        ViewCompat.setLayoutDirection(getWindow().getDecorView(), ViewCompat.LAYOUT_DIRECTION_LTR);

        //adding first data
        addFirstEmptyDataToFullSensorsArray();

        //region permission
        //asks for the user bluetooth permission goes in runtime
        askPermission();
        //endregion permission

        //region view inflating
        mToolBar = (Toolbar) findViewById(R.id.toolbar_main);
        mEasyTabs = (EasyTabs) findViewById(R.id.easy_tabs);
        //endregion view inflating

        //region fragment creation
        //create the fragments
//        homeFragment = new HomeFragment();
        workoutFragment = new WorkoutFragment();
        statisticFragment = new StatisticFragment();
        //endregion fragment creation

        //region place data in pressure map
//        updatePressureMapValues(integerDataFull);
        //endregion place data in pressure map

        //region toolbar
        setSupportActionBar(mToolBar);
        mToolBar.setTitleTextColor(ColorUtils.WHITE);
        //endregion toolbar

        //region tabs
        //initializing the tabs library
        EasyTabsBuilder
                .with(mEasyTabs)
                .addTabs(new TabItem(new HomeFragment(), "Home"),
                        new TabItem(workoutFragment, "Workout"),
                        new TabItem(new TipsFragment(), "Tips"),
                        new TabItem(statisticFragment, "Statistic"))
                .addIcons(R.drawable.ic_home_white_24dp,
                        R.drawable.ic_workout_white_24dp,
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
                Log.d(TAG, "onBluetoothDataReceived: " + data);
                if (!data.contains(endChar)) {
                    if (data.contains(startChar)) {
                       dataFull += data;
                    } else {
                        dataFull += data;
                    }
                } else {
                    //changing the data from the string in order to update the ui
//                    changeDataFromString(dataFull);
                    data = data.replaceAll("[\\\r|\\\n]", "");
                    String[] parts = data.split(endChar.toString());
                    if( parts.length > 0) {
                        dataFull += parts[0] + endChar;
                    }
                    else
                        dataFull += data;
                    integerDataFull = changeDataFromString(dataFull);
                    if( parts.length == 2) {
                        dataFull = parts[1];
                    }
                    else
                        dataFull = "";
                }

                Log.d(TAG, "data size: " + dataFull);

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
                UpdateBluetoothMenuTask myTask = new UpdateBluetoothMenuTask();
                myTask.execute();

                SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("LAST_MAC", device.getAddress());
                editor.commit();
                connectedMAC = device.getAddress();

                //running the timer service when the device is connected

                //region timer service
                Intent serviceIntent = new Intent(MainActivity.this, TimeService.class);

                timeServiceConnection = new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {
                        TimeService.TimeBinder timeBinder = (TimeService.TimeBinder) service;
                        mTimeService = timeBinder.getService();
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName name) {
                    }
                };
                isServiceBound = getApplicationContext().bindService(serviceIntent, timeServiceConnection, BIND_AUTO_CREATE);
                //endregion timer service
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(timerReceiver, new IntentFilter(TimeService.COUNTDOWN_PACKAGE));
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        String deviceMAC = sharedPreferences.getString("LAST_MAC", "");
        try {
            if (deviceMAC.length() > 0 && !isDeviceConnected && isBluetoothInitized) {
                simpleBluetooth.connectToBluetoothDevice(deviceMAC);
            }
        }
        catch (IllegalStateException ex){
            Log.d(TAG, "onStart IllegalStateException" + ex.getMessage());
        }
    }
    //endregion main running place

    //region timer receiver
    private BroadcastReceiver timerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getExtras() != null) {
                long millisUntilFinished = intent.getLongExtra("countdown", 0);
                Log.d(TAG, "onReceive: " + millisUntilFinished);
                List<Fragment> fragments = getSupportFragmentManager().getFragments();
                HomeFragment homeFragment = null;
                for(Fragment f: fragments){
                    if( f instanceof HomeFragment)
                        homeFragment = (HomeFragment)f;
                }
                if( homeFragment != null)
                if (homeFragment.circleTimerView != null) {
                    homeFragment.circleTimerView.setCurrentTime((int) millisUntilFinished);
                }
            }
        }
    };
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
        getMenuInflater().inflate(R.menu.toolbar_info_menu, menu);
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
            case R.id.menu_bluetooth:
                if (isDeviceConnected) {
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
         if (requestCode == BLUETOOTH_SCAN_REQUEST || requestCode == CHOOSE_SERVER_REQUEST) {
            if (resultCode == RESULT_OK) {
                if( isBluetoothInitized == false)
                    setupBluetoothConnection();
                if( isBluetoothInitized == true) {
                    String deviceMacAddress = data.getStringExtra(DeviceDialog.DEVICE_DIALOG_DEVICE_ADDRESS_EXTRA);
                    if (requestCode == BLUETOOTH_SCAN_REQUEST) {
                        simpleBluetooth.connectToBluetoothDevice(deviceMacAddress);
                    } else {
                        simpleBluetooth.connectToBluetoothServer(deviceMacAddress);
                    }
                }
            }
        }
        if( requestCode == 1001 && resultCode == RESULT_OK){
            SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
            String deviceMAC = sharedPreferences.getString("LAST_MAC", "");
            try {
                if (deviceMAC.length() > 0 && isDeviceConnected == false && isBluetoothInitized) {
                    simpleBluetooth.connectToBluetoothDevice(deviceMAC);
                }
            }
            catch (IllegalStateException ex){
                Log.d(TAG, "onStart IllegalStateException" + ex.getMessage());
            }
        }
    }
    //endregion bluetooth manually device choosing result

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if( simpleBluetooth != null)
            simpleBluetooth.endSimpleBluetooth();
        unregisterReceiver(timerReceiver);
        if( mTimeService != null)
            mTimeService.onDestroy();
        if( timeServiceConnection != null && isServiceBound == true)
            getApplicationContext().unbindService(timeServiceConnection);
        unregisterReceiver(mReceiver);
//        unbindService();
    }


    private ArrayList<Integer> changeDataFromString(String data) {
        ArrayList<Integer> dataReady = new ArrayList<>();
        if (data == null) {
            return null;
        }
        //remove the start tag and the end tag from the full string
        data = data.replace("*", "");
        data = data.replace(",#", "");
        //splitting the full string by , to get the values
        for (String number : data.split(",")) {
            //verifying that the number in not null ir empty
            if (!(number == null || number.isEmpty())) {
                //trying to convert the string into an int
                try {
                    dataReady.add(Integer.parseInt(number.trim()));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

            }
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sensorsData", data);
            jsonObject.put("seatback_id", connectedMAC);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Instantiate the RequestQueue.
        String url ="http://10.0.0.5:10010/updateData";

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display the first 500 characters of the response string.
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        // Add the request to the RequestQueue.
        helper.add(jsonObjectRequest);

        UpdateTipsTask  myTask = new UpdateTipsTask ();
        myTask.execute(dataReady);

      return dataReady;
    }

    private void addFirstEmptyDataToFullSensorsArray() {
        for (int i = 0; i < 72; i++) {
            integerDataFull.add(0);
        }
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
                if (isDeviceConnected == true)
                    menu.getItem(0).setIcon(R.drawable.ic_bluetooth_connected_white_24dp);
                else
                    menu.getItem(0).setIcon(R.drawable.ic_bluetooth_white_24dp);
            }
        }
    }

    class UpdateTipsTask extends AsyncTask<ArrayList<Integer>, Void, ArrayList<Integer>>
    {

        @Override
        protected void onPostExecute(ArrayList<Integer> data) {
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
                    tipsFragment.bottomPressureMap.setColors(data);

                    tipsFragment.topPressureMap.setTop(true);
                    tipsFragment.topPressureMap.setColors(data);
                }

            if( homeFragment != null)
            if (homeFragment.positionImageView != null && !MainActivity.this.isDestroyed()) {
                Integer imageAfterComparing = Utils.getImageAfterComparing(data);
                if (imageAfterComparing != null) {
                    Glide.with(MainActivity.this)
                            .load(imageAfterComparing)
                            .into(homeFragment.positionImageView);
                }
            }
        }

        @Override
        protected ArrayList<Integer> doInBackground(ArrayList<Integer>... params) {
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
}

