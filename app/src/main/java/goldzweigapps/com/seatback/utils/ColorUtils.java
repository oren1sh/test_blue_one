package goldzweigapps.com.seatback.utils;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.Size;
import android.util.Log;

import java.util.Locale;

@SuppressWarnings({"unused"})

public class ColorUtils {
       private static final String TAG = ColorUtils.class.getSimpleName();
    /**
     * Color utils all material design colors.
     * fast usage
     * each color is separated by region for its color set.
     */
    //region red
    @ColorInt
    public static final int RED_50 = 0xFFFFEBEE;
    @ColorInt
    public static final int RED_100 = 0xFFFFCDD2;
    @ColorInt
    public static final int RED_200 = 0xFFEF9A9A;
    @ColorInt
    public static final int RED_300 = 0xFFE57373;
    @ColorInt
    public static final int RED_400 = 0xFFEF5350;
    @ColorInt
    public static final int RED_500 = 0xFFF44336;
    @ColorInt
    public static final int RED_600 = 0xFFE53935;
    @ColorInt
    public static final int RED_700 = 0xFFD32F2F;
    @ColorInt
    public static final int RED_800 = 0xFFC62828;
    @ColorInt
    public static final int RED_900 = 0xFFB71C1C;
    @ColorInt
    public static final int RED_A_100 = 0xFFFF8A80;
    @ColorInt
    public static final int RED_A_200 = 0xFFFF5252;
    @ColorInt
    public static final int RED_A_400 = 0xFFFF1744;
    @ColorInt
    public static final int RED_A_700 = 0xFFD50000;
    //endregion red
    //region pink
    @ColorInt
    public static final int PINK_50 = 0xFFFCE4EC;
    @ColorInt
    public static final int PINK_100 = 0xFFF8BBD0;
    @ColorInt
    public static final int PINK_200 = 0xFFF48FB1;
    @ColorInt
    public static final int PINK_300 = 0xFFF06292;
    @ColorInt
    public static final int PINK_400 = 0xFFEC407A;
    @ColorInt
    public static final int PINK_500 = 0xFFE91E63;
    @ColorInt
    public static final int PINK_600 = 0xFFD81B60;
    @ColorInt
    public static final int PINK_700 = 0xFFC2185B;
    @ColorInt
    public static final int PINK_800 = 0xFFAD1457;
    @ColorInt
    public static final int PINK_900 = 0xFF880E4F;
    @ColorInt
    public static final int PINK_A_100 = 0xFFFF80AB;
    @ColorInt
    public static final int PINK_A_200 = 0xFFFF4081;
    @ColorInt
    public static final int PINK_A_400 = 0xFFF50057;
    @ColorInt
    public static final int PINK_A_700 = 0xFFC51162;
    //endregion pink
    //region purple
    @ColorInt
    public static final int PURPLE_50 = 0xFFF3E5F5;
    @ColorInt
    public static final int PURPLE_100 = 0xFFE1BEE7;
    @ColorInt
    public static final int PURPLE_200 = 0xFFCE93D8;
    @ColorInt
    public static final int PURPLE_300 = 0xFFBA68C8;
    @ColorInt
    public static final int PURPLE_400 = 0xFFAB47BC;
    @ColorInt
    public static final int PURPLE_500 = 0xFF9C27B0;
    @ColorInt
    public static final int PURPLE_600 = 0xFF8E24AA;
    @ColorInt
    public static final int PURPLE_700 = 0xFF7B1FA2;
    @ColorInt
    public static final int PURPLE_800 = 0xFF6A1B9A;
    @ColorInt
    public static final int PURPLE_900 = 0xFF4A148C;
    @ColorInt
    public static final int PURPLE_A_100 = 0xFFEA80FC;
    @ColorInt
    public static final int PURPLE_A_200 = 0xFFE040FB;
    @ColorInt
    public static final int PURPLE_A_400 = 0xFFD500F9;
    @ColorInt
    public static final int PURPLE_A_700 = 0xFFAA00FF;
    //endregion purple
    //region deep purple
    @ColorInt
    public static final int DEEP_PURPLE_50 = 0xFFEDE7F6;
    @ColorInt
    public static final int DEEP_PURPLE_100 = 0xFFD1C4E9;
    @ColorInt
    public static final int DEEP_PURPLE_200 = 0xFFB39DDB;
    @ColorInt
    public static final int DEEP_PURPLE_300 = 0xFF9575CD;
    @ColorInt
    public static final int DEEP_PURPLE_400 = 0xFF7E57C2;
    @ColorInt
    public static final int DEEP_PURPLE_500 = 0xFF673AB7;
    @ColorInt
    public static final int DEEP_PURPLE_600 = 0xFF5E35B1;
    @ColorInt
    public static final int DEEP_PURPLE_700 = 0xFF512DA8;
    @ColorInt
    public static final int DEEP_PURPLE_800 = 0xFF4527A0;
    @ColorInt
    public static final int DEEP_PURPLE_900 = 0xFF311B92;
    @ColorInt
    public static final int DEEP_PURPLE_A_100 = 0xFFB388FF;
    @ColorInt
    public static final int DEEP_PURPLE_A_200 = 0xFF7C4DFF;
    @ColorInt
    public static final int DEEP_PURPLE_A_400 = 0xFF651FFF;
    @ColorInt
    public static final int DEEP_PURPLE_A_700 = 0xFF6200EA;
    //endregion deep purple
    //region indigo
    @ColorInt
    public static final int INDIGO_50 = 0xFFE8EAF6;
    @ColorInt
    public static final int INDIGO_100 = 0xFFC5CAE9;
    @ColorInt
    public static final int INDIGO_200 = 0xFF9FA8DA;
    @ColorInt
    public static final int INDIGO_300 = 0xFF7986CB;
    @ColorInt
    public static final int INDIGO_400 = 0xFF5C6BC0;
    @ColorInt
    public static final int INDIGO_500 = 0xFF3F51B5;
    @ColorInt
    public static final int INDIGO_600 = 0xFF3949AB;
    @ColorInt
    public static final int INDIGO_700 = 0xFF303F9F;
    @ColorInt
    public static final int INDIGO_800 = 0xFF283593;
    @ColorInt
    public static final int INDIGO_900 = 0xFF1A237E;
    @ColorInt
    public static final int INDIGO_A_100 = 0xFF8C9EFF;
    @ColorInt
    public static final int INDIGO_A_200 = 0xFF536DFE;
    @ColorInt
    public static final int INDIGO_A_400 = 0xFF3D5AFE;
    @ColorInt
    public static final int INDIGO_A_700 = 0xFF304FFE;
    //endregion indigo
    //region blue
    @ColorInt
    public static final int BLUE_50 = 0xFFE3F2FD;
    @ColorInt
    public static final int BLUE_100 = 0xFFBBDEFB;
    @ColorInt
    public static final int BLUE_200 = 0xFF90CAF9;
    @ColorInt
    public static final int BLUE_300 = 0xFF64B5F6;
    @ColorInt
    public static final int BLUE_400 = 0xFF42A5F5;
    @ColorInt
    public static final int BLUE_500 = 0xFF2196F3;
    @ColorInt
    public static final int BLUE_600 = 0xFF1E88E5;
    @ColorInt
    public static final int BLUE_700 = 0xFF1976D2;
    @ColorInt
    public static final int BLUE_800 = 0xFF1565C0;
    @ColorInt
    public static final int BLUE_900 = 0xFF0D47A1;
    @ColorInt
    public static final int BLUE_A_100 = 0xFF82B1FF;
    @ColorInt
    public static final int BLUE_A_200 = 0xFF448AFF;
    @ColorInt
    public static final int BLUE_A_400 = 0xFF2979FF;
    @ColorInt
    public static final int BLUE_A_700 = 0xFF2962FF;
    //endregion blue
    //region light blue
    @ColorInt
    public static final int LIGHT_BLUE_50 = 0xFFE1F5FE;
    @ColorInt
    public static final int LIGHT_BLUE_100 = 0xFFB3E5FC;
    @ColorInt
    public static final int LIGHT_BLUE_200 = 0xFF81D4FA;
    @ColorInt
    public static final int LIGHT_BLUE_300 = 0xFF4FC3F7;
    @ColorInt
    public static final int LIGHT_BLUE_400 = 0xFF29B6F6;
    @ColorInt
    public static final int LIGHT_BLUE_500 = 0xFF03A9F4;
    @ColorInt
    public static final int LIGHT_BLUE_600 = 0xFF039BE5;
    @ColorInt
    public static final int LIGHT_BLUE_700 = 0xFF0288D1;
    @ColorInt
    public static final int LIGHT_BLUE_800 = 0xFF0277BD;
    @ColorInt
    public static final int LIGHT_BLUE_900 = 0xFF01579B;
    @ColorInt
    public static final int LIGHT_BLUE_A_100 = 0xFF80D8FF;
    @ColorInt
    public static final int LIGHT_BLUE_A_200 = 0xFF40C4FF;
    @ColorInt
    public static final int LIGHT_BLUE_A_400 = 0xFF00B0FF;
    @ColorInt
    public static final int LIGHT_BLUE_A_700 = 0xFF0091EA;
    //endregion light blue
    //region cyan
    @ColorInt
    public static final int CYAN_50 = 0xFFE0F7FA;
    @ColorInt
    public static final int CYAN_100 = 0xFFB2EBF2;
    @ColorInt
    public static final int CYAN_200 = 0xFF80DEEA;
    @ColorInt
    public static final int CYAN_300 = 0xFF4DD0E1;
    @ColorInt
    public static final int CYAN_400 = 0xFF26C6DA;
    @ColorInt
    public static final int CYAN_500 = 0xFF00BCD4;
    @ColorInt
    public static final int CYAN_600 = 0xFF00ACC1;
    @ColorInt
    public static final int CYAN_700 = 0xFF0097A7;
    @ColorInt
    public static final int CYAN_800 = 0xFF00838F;
    @ColorInt
    public static final int CYAN_900 = 0xFF006064;
    @ColorInt
    public static final int CYAN_A_100 = 0xFF84FFFF;
    @ColorInt
    public static final int CYAN_A_200 = 0xFF18FFFF;
    @ColorInt
    public static final int CYAN_A_400 = 0xFF00E5FF;
    @ColorInt
    public static final int CYAN_A_700 = 0xFF00B8D4;

    //endregion cyan
    //region teal
    @ColorInt
    public static final int TEAL_50 = 0xFFE0F2F1;
    @ColorInt
    public static final int TEAL_100 = 0xFFB2DFDB;
    @ColorInt
    public static final int TEAL_200 = 0xFF80CBC4;
    @ColorInt
    public static final int TEAL_300 = 0xFF4DB6AC;
    @ColorInt
    public static final int TEAL_400 = 0xFF26A69A;
    @ColorInt
    public static final int TEAL_500 = 0xFF009688;
    @ColorInt
    public static final int TEAL_600 = 0xFF00897B;
    @ColorInt
    public static final int TEAL_700 = 0xFF00796B;
    @ColorInt
    public static final int TEAL_800 = 0xFF00695C;
    @ColorInt
    public static final int TEAL_900 = 0xFF004D40;
    @ColorInt
    public static final int TEAL_A_100 = 0xFFA7FFEB;
    @ColorInt
    public static final int TEAL_A_200 = 0xFF64FFDA;
    @ColorInt
    public static final int TEAL_A_400 = 0xFF1DE9B6;
    @ColorInt
    public static final int TEAL_A_700 = 0xFF00BFA5;

    //endregion teal
    //region green
    @ColorInt
    public static final int GREEN_50 = 0xFFE8F5E9;
    @ColorInt
    public static final int GREEN_100 = 0xFFC8E6C9;
    @ColorInt
    public static final int GREEN_200 = 0xFFA5D6A7;
    @ColorInt
    public static final int GREEN_300 = 0xFF81C784;
    @ColorInt
    public static final int GREEN_400 = 0xFF66BB6A;
    @ColorInt
    public static final int GREEN_500 = 0xFF4CAF50;
    @ColorInt
    public static final int GREEN_600 = 0xFF43A047;
    @ColorInt
    public static final int GREEN_700 = 0xFF388E3C;
    @ColorInt
    public static final int GREEN_800 = 0xFF2E7D32;
    @ColorInt
    public static final int GREEN_900 = 0xFF1B5E20;
    @ColorInt
    public static final int GREEN_A_100 = 0xFFB9F6CA;
    @ColorInt
    public static final int GREEN_A_200 = 0xFF69F0AE;
    @ColorInt
    public static final int GREEN_A_400 = 0xFF00E676;
    @ColorInt
    public static final int GREEN_A_700 = 0xFF00C853;

    //endregion green
    //region light green
    @ColorInt
    public static final int LIGHT_GREEN_50 = 0xFFF1F8E9;
    @ColorInt
    public static final int LIGHT_GREEN_100 = 0xFFDCEDC8;
    @ColorInt
    public static final int LIGHT_GREEN_200 = 0xFFC5E1A5;
    @ColorInt
    public static final int LIGHT_GREEN_300 = 0xFFAED581;
    @ColorInt
    public static final int LIGHT_GREEN_400 = 0xFF9CCC65;
    @ColorInt
    public static final int LIGHT_GREEN_500 = 0xFF8BC34A;
    @ColorInt
    public static final int LIGHT_GREEN_600 = 0xFF7CB342;
    @ColorInt
    public static final int LIGHT_GREEN_700 = 0xFF689F38;
    @ColorInt
    public static final int LIGHT_GREEN_800 = 0xFF558B2F;
    @ColorInt
    public static final int LIGHT_GREEN_900 = 0xFF33691E;
    @ColorInt
    public static final int LIGHT_GREEN_A_100 = 0xFFCCFF90;
    @ColorInt
    public static final int LIGHT_GREEN_A_200 = 0xFFB2FF59;
    @ColorInt
    public static final int LIGHT_GREEN_A_400 = 0xFF76FF03;
    @ColorInt
    public static final int LIGHT_GREEN_A_700 = 0xFF64DD17;
    //endregion light green
    //region lime
    @ColorInt
    public static final int LIME_50 = 0xFFF9FBE7;
    @ColorInt
    public static final int LIME_100 = 0xFFF0F4C3;
    @ColorInt
    public static final int LIME_200 = 0xFFE6EE9C;
    @ColorInt
    public static final int LIME_300 = 0xFFDCE775;
    @ColorInt
    public static final int LIME_400 = 0xFFD4E157;
    @ColorInt
    public static final int LIME_500 = 0xFFCDDC39;
    @ColorInt
    public static final int LIME_600 = 0xFFC0CA33;
    @ColorInt
    public static final int LIME_700 = 0xFFAFB42B;
    @ColorInt
    public static final int LIME_800 = 0xFF9E9D24;
    @ColorInt
    public static final int LIME_900 = 0xFF827717;
    @ColorInt
    public static final int LIME_A_100 = 0xFFF4FF81;
    @ColorInt
    public static final int LIME_A_200 = 0xFFEEFF41;
    @ColorInt
    public static final int LIME_A_400 = 0xFFC6FF00;
    @ColorInt
    public static final int LIME_A_700 = 0xFFAEEA00;
    //endregion lime
    //region yellow
    @ColorInt
    public static final int YELLOW_50 = 0xFFFFFDE7;
    @ColorInt
    public static final int YELLOW_100 = 0xFFFFF9C4;
    @ColorInt
    public static final int YELLOW_200 = 0xFFFFF59D;
    @ColorInt
    public static final int YELLOW_300 = 0xFFFFF176;
    @ColorInt
    public static final int YELLOW_400 = 0xFFFFEE58;
    @ColorInt
    public static final int YELLOW_500 = 0xFFFFEB3B;
    @ColorInt
    public static final int YELLOW_600 = 0xFFFDD835;
    @ColorInt
    public static final int YELLOW_700 = 0xFFFBC02D;
    @ColorInt
    public static final int YELLOW_800 = 0xFFF9A825;
    @ColorInt
    public static final int YELLOW_900 = 0xFFF57F17;
    @ColorInt
    public static final int YELLOW_A_100 = 0xFFFFFF8D;
    @ColorInt
    public static final int YELLOW_A_200 = 0xFFFFFF00;
    @ColorInt
    public static final int YELLOW_A_400 = 0xFFFFEA00;
    @ColorInt
    public static final int YELLOW_A_700 = 0xFFFFD600;
    //endregion yellow
    //region amber
    @ColorInt
    public static final int AMBER_50 = 0xFFFFF8E1;
    @ColorInt
    public static final int AMBER_100 = 0xFFFFECB3;
    @ColorInt
    public static final int AMBER_200 = 0xFFFFE082;
    @ColorInt
    public static final int AMBER_300 = 0xFFFFD54F;
    @ColorInt
    public static final int AMBER_400 = 0xFFFFCA28;
    @ColorInt
    public static final int AMBER_500 = 0xFFFFC107;
    @ColorInt
    public static final int AMBER_600 = 0xFFFFB300;
    @ColorInt
    public static final int AMBER_700 = 0xFFFFA000;
    @ColorInt
    public static final int AMBER_800 = 0xFFFF8F00;
    @ColorInt
    public static final int AMBER_900 = 0xFFFF6F00;
    @ColorInt
    public static final int AMBER_A_100 = 0xFFFFE57F;
    @ColorInt
    public static final int AMBER_A_200 = 0xFFFFD740;
    @ColorInt
    public static final int AMBER_A_400 = 0xFFFFC400;
    @ColorInt
    public static final int AMBER_A_700 = 0xFFFFAB00;

    //endregion amber
    //region orange
    @ColorInt
    public static final int ORANGE_50 = 0xFFFFF3E0;
    @ColorInt
    public static final int ORANGE_100 = 0xFFFFE0B2;
    @ColorInt
    public static final int ORANGE_200 = 0xFFFFCC80;
    @ColorInt
    public static final int ORANGE_300 = 0xFFFFB74D;
    @ColorInt
    public static final int ORANGE_400 = 0xFFFFA726;
    @ColorInt
    public static final int ORANGE_500 = 0xFFFF9800;
    @ColorInt
    public static final int ORANGE_600 = 0xFFFB8C00;
    @ColorInt
    public static final int ORANGE_700 = 0xFFF57C00;
    @ColorInt
    public static final int ORANGE_800 = 0xFFEF6C00;
    @ColorInt
    public static final int ORANGE_900 = 0xFFE65100;
    @ColorInt
    public static final int ORANGE_A_100 = 0xFFFFD180;
    @ColorInt
    public static final int ORANGE_A_200 = 0xFFFFAB40;
    @ColorInt
    public static final int ORANGE_A_400 = 0xFFFF9100;
    @ColorInt
    public static final int ORANGE_A_700 = 0xFFFF6D00;
    //endregion orange
    //region deep orange
    @ColorInt
    public static final int DEEP_ORANGE_50 = 0xFFFBE9E7;
    @ColorInt
    public static final int DEEP_ORANGE_100 = 0xFFFFCCBC;
    @ColorInt
    public static final int DEEP_ORANGE_200 = 0xFFFFAB91;
    @ColorInt
    public static final int DEEP_ORANGE_300 = 0xFFFF8A65;
    @ColorInt
    public static final int DEEP_ORANGE_400 = 0xFFFF7043;
    @ColorInt
    public static final int DEEP_ORANGE_500 = 0xFFFF5722;
    @ColorInt
    public static final int DEEP_ORANGE_600 = 0xFFF4511E;
    @ColorInt
    public static final int DEEP_ORANGE_700 = 0xFFE64A19;
    @ColorInt
    public static final int DEEP_ORANGE_800 = 0xFFD84315;
    @ColorInt
    public static final int DEEP_ORANGE_900 = 0xFFBF360C;
    @ColorInt
    public static final int DEEP_ORANGE_A_100 = 0xFFFF9E80;
    @ColorInt
    public static final int DEEP_ORANGE_A_200 = 0xFFFF6E40;
    @ColorInt
    public static final int DEEP_ORANGE_A_400 = 0xFFFF3D00;
    @ColorInt
    public static final int DEEP_ORANGE_A_700 = 0xFFDD2C00;

    //endregion deep orange
    //region brown
    @ColorInt
    public static final int BROWN_50 = 0xFFEFEBE9;
    @ColorInt
    public static final int BROWN_100 = 0xFFD7CCC8;
    @ColorInt
    public static final int BROWN_200 = 0xFFBCAAA4;
    @ColorInt
    public static final int BROWN_300 = 0xFFA1887F;
    @ColorInt
    public static final int BROWN_400 = 0xFF8D6E63;
    @ColorInt
    public static final int BROWN_500 = 0xFF795548;
    @ColorInt
    public static final int BROWN_600 = 0xFF6D4C41;
    @ColorInt
    public static final int BROWN_700 = 0xFF5D4037;
    @ColorInt
    public static final int BROWN_800 = 0xFF4E342E;
    @ColorInt
    public static final int BROWN_900 = 0xFF3E2723;
    //endregion brown
    //region gray
    @ColorInt
    public static final int GREY_50 = 0xFFFAFAFA;
    @ColorInt
    public static final int GREY_100 = 0xFFF5F5F5;
    @ColorInt
    public static final int GREY_200 = 0xFFEEEEEE;
    @ColorInt
    public static final int GREY_300 = 0xFFE0E0E0;
    @ColorInt
    public static final int GREY_400 = 0xFFBDBDBD;
    @ColorInt
    public static final int GREY_500 = 0xFF9E9E9E;
    @ColorInt
    public static final int GREY_600 = 0xFF757575;
    @ColorInt
    public static final int GREY_700 = 0xFF616161;
    @ColorInt
    public static final int GREY_800 = 0xFF424242;
    @ColorInt
    public static final int GREY_900 = 0xFF212121;
    //endregion gray
    //region blue gray
    @ColorInt
    public static final int BLUE_GREY_50 = 0xFFECEFF1;
    @ColorInt
    public static final int BLUE_GREY_100 = 0xFFCFD8DC;
    @ColorInt
    public static final int BLUE_GREY_200 = 0xFFB0BEC5;
    @ColorInt
    public static final int BLUE_GREY_300 = 0xFF90A4AE;
    @ColorInt
    public static final int BLUE_GREY_400 = 0xFF78909C;
    @ColorInt
    public static final int BLUE_GREY_500 = 0xFF607D8B;
    @ColorInt
    public static final int BLUE_GREY_600 = 0xFF546E7A;
    @ColorInt
    public static final int BLUE_GREY_700 = 0xFF455A64;
    @ColorInt
    public static final int BLUE_GREY_800 = 0xFF37474F;
    @ColorInt
    public static final int BLUE_GREY_900 = 0xFF263238;
    //endregion blue gray
    //region black and white
    @ColorInt
    public static final int BLACK = 0xFF000000;
    @ColorInt
    public static final int WHITE = 0xFFFFFFFF;
    //endregion black and white

    @ColorInt
    public static int parseColor(@Size(min = 1) String colorString) {
        return Color.parseColor(colorString);
    }


    public static int getColor(int progress) {
        progress /= 10 + 1;
        if (progress == 40) {
            progress = 39;
        }
        return colors()[progress];
    }



    public static int[] colors() {
        return new int[]{
                Color.parseColor("#0035E5"),
                Color.parseColor("#004CE4"),
                Color.parseColor("#0062E3"),
                Color.parseColor("#0077E2"),
                Color.parseColor("#008DE1"),
                Color.parseColor("#00A2E0"),
                Color.parseColor("#00B8DF"),
                Color.parseColor("#00CDDE"),
                Color.parseColor("#00DDD8"),
                Color.parseColor("#00DCC1"),
                Color.parseColor("#00DBAB"),
                Color.parseColor("#00DA95"),
                Color.parseColor("#00D97F"),
                Color.parseColor("#00D869"),
                Color.parseColor("#00D753"),
                Color.parseColor("#00D63E"),
                Color.parseColor("#00D528"),
                Color.parseColor("#00D413"),
                Color.parseColor("#00D300"),
                Color.parseColor("#15D200"),
                Color.parseColor("#2AD100"),
                Color.parseColor("#3ED000"),
                Color.parseColor("#52CF00"),
                Color.parseColor("#66CE00"),
                Color.parseColor("#7ACD00"),
                Color.parseColor("#8DCC00"),
                Color.parseColor("#A1CB00"),
                Color.parseColor("#B4CA00"),
                Color.parseColor("#C7C900"),
                Color.parseColor("#C8B700"),
                Color.parseColor("#C7A300"),
                Color.parseColor("#C68E00"),
                Color.parseColor("#C57A00"),
                Color.parseColor("#C46600"),
                Color.parseColor("#C35200"),
                Color.parseColor("#C23F00"),
                Color.parseColor("#C12B00"),
                Color.parseColor("#C01800"),
                Color.parseColor("#BF0500"),
                Color.parseColor("#BF000C")};
    }

}
