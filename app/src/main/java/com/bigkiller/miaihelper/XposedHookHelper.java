package com.bigkiller.miaihelper;

import java.util.HashMap;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class XposedHookHelper {

    public BuildImpl     Build     = null;

    private XC_LoadPackage.LoadPackageParam loadPackageParam = null;

    private static XposedHookHelper instances = null;

    private XposedHookHelper(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        this.loadPackageParam = loadPackageParam;
        Build     = new BuildImpl();
    }

    public static XposedHookHelper getInstances(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if (instances == null){
            instances = new XposedHookHelper(loadPackageParam);
        }
        return instances;
    }

    public XC_LoadPackage.LoadPackageParam getLoadPackageParam() {
        return loadPackageParam;
    }


    /**
     * android.os.Build 拦截
     */
    @SuppressWarnings("WeakerAccess")
    public class BuildImpl {
        public HashMap<String, String> hashMap = new HashMap<>();

        private BuildImpl() {
            XposedBridge.hookAllMethods(XposedHelpers.findClass("android.os.SystemProperties", getLoadPackageParam().classLoader), "get", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    if (hashMap.containsKey(param.args[0].toString())) {
                        param.setResult(hashMap.get(param.args[0].toString()));
                    }
                }
            });
        }

        public void MODEL(String value) {
            XposedHelpers.setStaticObjectField(android.os.Build.class, "MODEL", value);
            hashMap.put("ro.product.model", value);
        }

        public void DEVICE(String value) {
            XposedHelpers.setStaticObjectField(android.os.Build.class, "DEVICE", value);
            hashMap.put("ro.product.device", value);
        }
    }

    private static class XC_ResultHook extends XC_MethodHook {
        private Object resultObject = null;

        XC_ResultHook(Object resultObject) {
            this.resultObject = resultObject;
        }

        @Override
        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            if (resultObject != null)
                param.setResult(resultObject);
        }
    }
}
