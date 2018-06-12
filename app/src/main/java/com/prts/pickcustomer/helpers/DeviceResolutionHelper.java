package com.prts.pickcustomer.helpers;

import android.app.Activity;
import android.graphics.Point;
import android.view.Display;

/**
 * Created by LOGICON on 22-12-2017.
 */

public class DeviceResolutionHelper {

    public static Point getDeviceWidth(Activity context){
        Display display = context.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }
}
