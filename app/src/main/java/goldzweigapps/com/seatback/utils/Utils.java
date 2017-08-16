package goldzweigapps.com.seatback.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import goldzweigapps.com.seatback.R;

import static goldzweigapps.com.seatback.utils.ColorUtils.*;

/**
 * Created by gilgoldzweig on 24/03/2017.
 */

public class Utils {
    private static final String TAG = Utils.class.getSimpleName();

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
    public static Integer getImageAfterComparing(ArrayList<Integer> sensors) {
        int BackLU = 0;
        int ButtomLU = 0;
        int BackRU = 0;
        int ButtomRU = 0;
        int BackLD = 0;
        int ButtomLD = 0;
        int BackRD = 0;
        int ButtomRD = 0;
        if (sensors.size() != 72) {
            Log.d(TAG, "getImageAfterComparing: not 72 but: " + sensors.size());
            return null;
            //TODO connect to crashlytics
        }

        BackLU = sum(sensors, 0, 2) + sum(sensors, 6, 8) + sum(sensors, 12, 14);
        BackRU = sum(sensors, 3, 5) + sum(sensors, 9, 11) + sum(sensors, 15, 17);
        BackLD = sum(sensors, 18, 20) + sum(sensors, 24, 26) + sum(sensors, 30, 32);
        BackRD = sum(sensors, 21, 23) + sum(sensors, 27, 29) + sum(sensors, 33, 35);
        ButtomLU = sum(sensors, 36, 38) + sum(sensors, 42, 44) + sum(sensors, 48, 50);
        ButtomRU = sum(sensors, 39, 41) + sum(sensors, 45, 47) + sum(sensors, 51, 53);
        ButtomLD = sum(sensors, 54, 56) + sum(sensors, 60, 62) + sum(sensors, 66, 68);
        ButtomRD = sum(sensors, 57, 59) + sum(sensors, 63, 65) + sum(sensors, 69, 71);
        int ButtomLeft = ButtomLU + ButtomLD;
        int ButtomRight = ButtomRU + ButtomRD;
        int ButtomDown = ButtomRD + ButtomLD;
        int ButtomUp = ButtomRU + ButtomLU;
        int BackUp = BackLU + BackRU;
        int BackDown = BackLD + BackRD;

        Log.d(TAG, String.format(
                "ButtomLeft: %d \nButtomRight: %d\nButtomDown: %d\nButtomUp: %d\nBackUp: %d\nBackDown: %d",
                ButtomLeft, ButtomRight, ButtomDown, ButtomUp, BackUp, BackDown));
        //standing
        if (ButtomDown + ButtomUp < 6000) {
            Log.d(TAG, "getImageAfterComparing: standing");
            return R.drawable.standing;

        }  else if (BackUp < 2100) {
            Log.d(TAG, "getImageAfterComparing: banding");
            return R.drawable.banding;

        } else if (BackUp > 2100 && ButtomUp + BackDown > 4200) {
            Log.d(TAG, "getImageAfterComparing: good posture");
            return R.drawable.good_posture;

        }  else if (ButtomRight - ButtomLeft > 1200 || ButtomLeft - ButtomRight > 1200) {
            Log.d(TAG, "getImageAfterComparing: leg");
            return R.drawable.leg;

        } else if (ButtomDown > 3500 && BackUp > 2200 && (ButtomUp + BackDown < 4200)) {
            Log.d(TAG, "getImageAfterComparing: slouching");
            return R.drawable.slouching;

        } else {
            Log.d(TAG, "getImageAfterComparing: else good");
            return null;
        }
    }

    public static int sum(ArrayList<Integer> sensorsToSum, int Start, int End) {
        int SumPart = 0;
        for (int i = Start; i <= End; i++) {
            SumPart += sensorsToSum.get(i);
        }
        return SumPart;
    }
}