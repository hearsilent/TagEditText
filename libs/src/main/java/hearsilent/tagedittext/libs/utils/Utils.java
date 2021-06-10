package hearsilent.tagedittext.libs.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

public class Utils {

    public static DisplayMetrics getDisplayMetrics(Context context) {
        Resources resources = context.getResources();
        return resources.getDisplayMetrics();
    }

    private static Point getDisplayDimen(Context context) {
        Point size = new Point();
        WindowManager manager = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE));
        if (manager == null) {
            return size;
        }
        Display display = manager.getDefaultDisplay();
        display.getSize(size);
        return size;
    }

    public static int getDisplayWidth(Context context) {
        return getDisplayDimen(context).x;
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device
     * density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need
     *                to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on
     * device density
     */
    public static float convertDpToPixel(float dp, Context context) {
        return dp * (getDisplayMetrics(context).densityDpi / 160f);
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device
     * density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need
     *                to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return Value multiplied by the appropriate metric and truncated to
     * integer pixels.
     */
    public static int convertDpToPixelSize(float dp, Context context) {
        float pixels = convertDpToPixel(dp, context);
        final int res = (int) (pixels + 0.5f);
        if (res != 0) {
            return res;
        } else if (pixels == 0) {
            return 0;
        } else if (pixels > 0) {
            return 1;
        }
        return -1;
    }

    /**
     * This method converts sp unit to equivalent pixels, depending on device
     * density.
     *
     * @param sp      A value in sp (scale-independent pixels) unit. Which we need
     *                to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to sp depending on
     * device density
     */
    public static int convertSpToPixel(float sp, Context context) {
        return (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getDisplayMetrics(context));
    }

    public static int getStatusBarHeightPixel(Context context) {
        int result = 0;
        int resourceId =
                context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
