package com.bigkiller.miaihelper;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class helper implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam)  {
        try {
            if (loadPackageParam.packageName.equals("com.miui.voiceassist") || loadPackageParam.packageName.equals("com.miui.voiceassisu")) {
                XposedHookHelper.getInstances(loadPackageParam).Build.MODEL("Mi 10");
                XposedHookHelper.getInstances(loadPackageParam).Build.DEVICE("umi");
            }
        } catch (Throwable throwable) {
        }
    }
}
