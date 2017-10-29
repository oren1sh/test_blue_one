package seatback.com.seatback.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.provider.Settings;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import seatback.com.seatback.BuildConfig;
import seatback.com.seatback.R;

/**
 * Created by gilgoldzweig on 24/03/2017.
 */

public class Utils {
    private static final String TAG = Utils.class.getSimpleName();
    private static String connectedMAC = "", connectedName = "", apiTokenID = "p4Mk3kEJwA5ZMXVxwNCRDUy3KKvdV2t4vSiU39Y2";
    private static final String[] postureNames = {"standing", "good", "bending forward", "slump", "tilt left", "tilt right", "nope 6", "nope 7", "unknown"};

    public static String getPostureName(int postureIndex){
// 0=standing // 1=good // 2=banding forward // 3= slump // 4= left  //5= right left// 6= nope //7= nope //8=Unknown
        if( postureIndex > postureNames.length)
            return "Unknown";
        else
            return postureNames[postureIndex];
    }

    @Nullable
    @DrawableRes

    /*
      @param int the posture index as sent by the board
     * function returns the proper image based on the received posture index
     */
    public static Integer getImageAfterComparing(int posture) {
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
                return R.drawable.lean_left_2good;
            case 5:
                return R.drawable.lean_right_2good;
            case 6:
            default:
                break;
        }
        return null;
    }

    public static String getServerURL(){
if(!BuildConfig.DEBUG)
        return  "http://52.51.116.134:10010";
else
    return  "http://52.51.116.134:10010";
//        return  "http:" + "//10.0.0.11:10010";
//        return  "http://192.168.0.94:10020";
    }

    public static String getAPITokenId(){
        return apiTokenID;
    }

    public static void setAPITokenId(String token){
        apiTokenID = token;
    }
    public static String getConnectecMAC(){
        return connectedMAC;
    }

    public static void setConnectecMAC(String MAC){
        connectedMAC = MAC;
    }

    public static void setConnectecName(String Name){ connectedName = Name;}

    public static String getConnectecName(){
        return connectedName;
    }

    public static String getUserID(Context context) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if( currentUser != null) {
            String userID = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            return userID;
        }
        else
        {
            return "nope@seatback.co";
        }
    }
    public static String getDeviceID(Context context) {
        String device_unique_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        return device_unique_id;
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

}