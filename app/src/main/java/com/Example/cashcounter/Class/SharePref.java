package com.Example.cashcounter.Class;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharePref {
    public static String SharePrefName = "RUNNING_SERVICE_DETAILS";
    public static String cBaseDomain = "cBaseDomain";
    public static String isRuningFirsttime = "isFirstTime";
    public static String uAddress = "uAddress";
    public static String uAppId = "uAppId";
    public static String uCity = "uCity";
    public static String uCompanyName = "uACompanyName";
    public static String uCountry = "uCountry";
    public static String uCountryCode = "uCountryCode";
    public static final String uCurrencyCode = "uCurrencyCode";
    public static final String uCurrencyName = "uCurrencyName";
    public static final String uCurrencySymbol = "uCurrencySymbol";
    public static String uEmail = "uEmail";
    public static String uImei = "uAImei";
    public static String uIsPackageExpr = "uIsPackageExpr";
    public static String uMobile = "uAMobile";
    public static String uName = "uAName";
    public static String uPackageDuration = "uPackageDuration";
    public static String uPackageExprDt = "uPackageExprDt";
    public static String uPackageId = "uPackageId";
    public static String uPackageName = "uPackageName";
    public static String uPackageStartDt = "uPackageStartDt";
    public static String uPid = "uPId";
    public static String uState = "uState";
    public static String uZipcode = "uZipcode";

    public static SharedPreferences getSharePref(Context context) {
        return context.getSharedPreferences(SharePrefName, 0);
    }

    public static void Clear(SharedPreferences sharedPreferences) {
        Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.commit();
    }
}
