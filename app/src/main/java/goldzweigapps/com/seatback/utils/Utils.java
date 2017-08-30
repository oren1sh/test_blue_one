package goldzweigapps.com.seatback.utils;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import goldzweigapps.com.seatback.BuildConfig;
import goldzweigapps.com.seatback.R;

/**
 * Created by gilgoldzweig on 24/03/2017.
 */

public class Utils {
    private static final String TAG = Utils.class.getSimpleName();
    private static String connectedMAC = "";
    private static final String[] postureNames = {"standing", "good", "bending", "slouching", "leg", "leg", "unknown"};

    public static String getPostureName(int postureIndex){
        if( postureIndex >= postureNames.length)
            return "Unknown";
        else
            return postureNames[postureIndex];
    }

    public static String getServerURL(){
if(!BuildConfig.DEBUG)
        return  "http://52.51.116.134:10010";
else
//    return  "http://52.51.116.134:10010";
        return  "http://10.0.0.5:10010";
    }

    public static String getConnectecMAC(){
        return connectedMAC;
    }

    public static void setConnectecMAC(String MAC){
        connectedMAC = MAC;
    }

    public static String getAdpaterAddress() {
        String address = BluetoothAdapter.getDefaultAdapter().getAddress();
        if( address.length() == 0)
            address = android.os.Build.SERIAL;

        return address;
    }
    /**
     *
     * @param dp
     * @return value id px
     */
    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     *
     * @param clicked
     * @param leftSide
     * @param centerOne
     * @param centerTwo
     * @param Right
     * check witch button was clicked and changes the background to be in selected colors
     */
    public static void singleSelectionButtons(Button clicked, Button leftSide, Button centerOne,
                                              Button centerTwo,
                                              Button Right) {
        if (clicked.equals(leftSide)) {
            leftSide.setBackgroundResource(R.drawable.button_side_left_selected);
            centerOne.setBackgroundResource(R.drawable.button_center);
            centerTwo.setBackgroundResource(R.drawable.button_center);
            Right.setBackgroundResource(R.drawable.button_side_right);
            leftSide.setTextColor(ColorUtils.WHITE);
            centerOne.setTextColor(ColorUtils.BLACK);
            centerTwo.setTextColor(ColorUtils.BLACK);
            Right.setTextColor(ColorUtils.BLACK);
        } else if (clicked.equals(centerOne)) {
            leftSide.setBackgroundResource(R.drawable.button_side_left);
            centerOne.setBackgroundResource(R.drawable.button_center_selected);
            centerTwo.setBackgroundResource(R.drawable.button_center);
            Right.setBackgroundResource(R.drawable.button_side_right);
            leftSide.setTextColor(ColorUtils.BLACK);
            centerOne.setTextColor(ColorUtils.WHITE);
            centerTwo.setTextColor(ColorUtils.BLACK);
            Right.setTextColor(ColorUtils.BLACK);
        } else if (clicked.equals(centerTwo)) {
            leftSide.setBackgroundResource(R.drawable.button_side_left);
            centerOne.setBackgroundResource(R.drawable.button_center);
            centerTwo.setBackgroundResource(R.drawable.button_center_selected);
            Right.setBackgroundResource(R.drawable.button_side_right);
            leftSide.setTextColor(ColorUtils.BLACK);
            centerOne.setTextColor(ColorUtils.BLACK);
            centerTwo.setTextColor(ColorUtils.WHITE);
            Right.setTextColor(ColorUtils.BLACK);
        } else if (clicked.equals(Right)) {
            leftSide.setBackgroundResource(R.drawable.button_side_left);
            centerOne.setBackgroundResource(R.drawable.button_center);
            centerTwo.setBackgroundResource(R.drawable.button_center);
            Right.setBackgroundResource(R.drawable.button_side_right_selected);
            leftSide.setTextColor(ColorUtils.BLACK);
            centerOne.setTextColor(ColorUtils.BLACK);
            centerTwo.setTextColor(ColorUtils.BLACK);
            Right.setTextColor(ColorUtils.WHITE);
        } else {
            leftSide.setBackgroundResource(R.drawable.button_side_left_selected);
            centerOne.setBackgroundResource(R.drawable.button_center);
            centerTwo.setBackgroundResource(R.drawable.button_center);
            Right.setBackgroundResource(R.drawable.button_side_right);
            leftSide.setTextColor(ColorUtils.WHITE);
            centerOne.setTextColor(ColorUtils.BLACK);
            centerTwo.setTextColor(ColorUtils.BLACK);
            Right.setTextColor(ColorUtils.BLACK);
        }
    }


    public static boolean isPermissionGranted(Context context, String... permissions) {
        boolean granted = false;
        for (String permission : permissions) {
            granted = false;
            granted = ContextCompat.checkSelfPermission(context,
                    permission) == PackageManager.PERMISSION_GRANTED;
            if (!granted) {
                try {
                    throw new Throwable("Permission denied: " + permission);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                return false;
            }
        }
        return granted;
    }

    @Nullable
    @DrawableRes

    /*
      @param ArrayList sensors values
     * function take the 72 sensors check their values and return the proper image
     */
    public static Integer getImageAfterComparing(List<Integer> sensors) {
        int posture = calculatePosture(sensors);
        switch(posture) {
            case 0:
                return R.drawable.standing;
            case 1:
                return R.drawable.good_posture;
            case 2:
                return R.drawable.banding;
            case 3:
                return R.drawable.slouching;
            case 4:
                return R.drawable.leg;
            case 5:
                return R.drawable.leg;
            case 6:
            default:
                break;
        }
        return null;
    }

    // 0=standing // 1=good // 2=banding forward // 3= slump // 4= unequal right //5= unequal left //6=Unknown
    public static int calculatePosture(List<Integer> sensors){
        int BackLU = 0;
        int BottomLU = 0;
        int BackRU = 0;
        int BottomRU = 0;
        int BackLD = 0;
        int BottomLD = 0;
        int BackRD = 0;
        int BottomRD = 0;
        if (sensors.size() != 72) {
            Log.d(TAG, "getImageAfterComparing: not 72 but: " + sensors.size());
            return 6;
            //TODO connect to crashlytics
        }

        BackLU = sum(sensors, 0, 2) + sum(sensors, 6, 8) + sum(sensors, 12, 14);
        BackRU = sum(sensors, 3, 5) + sum(sensors, 9, 11) + sum(sensors, 15, 17);
        BackLD = sum(sensors, 18, 20) + sum(sensors, 24, 26) + sum(sensors, 30, 32);
        BackRD = sum(sensors, 21, 23) + sum(sensors, 27, 29) + sum(sensors, 33, 35);
        BottomLU = sum(sensors, 36, 38) + sum(sensors, 42, 44) + sum(sensors, 48, 50);
        BottomRU = sum(sensors, 39, 41) + sum(sensors, 45, 47) + sum(sensors, 51, 53);
        BottomLD = sum(sensors, 54, 56) + sum(sensors, 60, 62) + sum(sensors, 66, 68);
        BottomRD = sum(sensors, 57, 59) + sum(sensors, 63, 65) + sum(sensors, 69, 71);
        int BottomLeft = BottomLU + BottomLD;
        int BottomRight = BottomRU + BottomRD;
        int BottomDown = BottomRD + BottomLD;
        int BottomUp = BottomRU + BottomLU;
        int BackUp = BackLU + BackRU;
        int BackDown = BackLD + BackRD;

        Log.d(TAG, String.format(
                "ButtomLeft: %d \nButtomRight: %d\nButtomDown: %d\nButtomUp: %d\nBackUp: %d\nBackDown: %d",
                BottomLeft, BottomRight, BottomDown, BottomUp, BackUp, BackDown));
        //standing
        if (BottomDown + BottomUp < 6000) {
            Log.d(TAG, "getImageAfterComparing: standing");
            return 0;

        }  else if (BackUp < 2100) {
            Log.d(TAG, "getImageAfterComparing: banding");
            return 2;

        } else if (BackUp > 2100 && BottomUp + BackDown > 4200) {
            Log.d(TAG, "getImageAfterComparing: good posture");
            return 1;

        }  else if (BottomRight - BottomLeft > 1200 || BottomLeft - BottomRight > 1200) {
            Log.d(TAG, "getImageAfterComparing: leg");
            return 4;

        } else if (BottomDown > 3500 && BackUp > 2200 && (BottomUp + BackDown < 4200)) {
            Log.d(TAG, "getImageAfterComparing: slouching");
            return 3;

        } else {
            Log.d(TAG, "getImageAfterComparing: else good");
            return 6;
        }
    }

    public static int sum(List<Integer> sensorsToSum, int Start, int End) {
        int SumPart = 0;
        for (int i = Start; i <= End; i++) {
            SumPart += sensorsToSum.get(i);
        }
        return SumPart;
    }
}