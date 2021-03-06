package com.liskovsoft.smartyoutubetv.common.helpers;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import com.liskovsoft.smartyoutubetv.common.prefs.SmartPreferences;

public class AppInfoHelpers {
    public static String getAppVersion(Context context) {
        return formatAppVersion(getAppVersionNum(context), getActivityLabel(context));
    }

    public static String getAppVersionRobust(Context context) {
        return formatAppVersion(getAppVersionNum(context), getActivityLabelRobust(context));
    }

    private static String formatAppVersion(String version, String label) {
        return String.format("%s (%s)", version, label);
    }

    public static String getActivityLabelRobust(Context context) {
        SmartPreferences prefs = SmartPreferences.instance(context);
        String name = prefs.getBootstrapActivityName();
        return name == null ? getActivityLabel(context) : getActivityLabel(context, name);
    }

    public static String getAppVersionNum(Context context) {
        String versionName = null;

        try {
            versionName = context
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0)
                    .versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionName;
    }

    private static String getActivityLabel(Context context) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            return getActivityLabel(context, activity.getComponentName());
        }

        return null;
    }

    private static String getActivityLabel(Context context, String cls) {
        if (cls != null) {
            return getActivityLabel(context, new ComponentName(context, cls));
        }

        return null;
    }

    private static String getActivityLabel(Context context, ComponentName name) {
        PackageManager pm = context.getPackageManager();

        try {
            ActivityInfo info = pm.getActivityInfo(name, 0);
            return context.getResources().getString(info.labelRes);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
