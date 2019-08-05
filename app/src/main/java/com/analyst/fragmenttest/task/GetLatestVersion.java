package com.analyst.fragmenttest.task;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import com.analyst.fragmenttest.Interface.GooglePlayVersion;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class GetLatestVersion extends AsyncTask<String, String, JSONObject> {
    private final String TAG = GetLatestVersion.class.getSimpleName();

    private Context mContext;
    private GooglePlayVersion googlePlayVersion = null;
    private PackageManager pm;
    private PackageInfo pInfo;
    private String currentVersion = null;
    private String latestVersion = null;

    public GetLatestVersion(Context mContext, GooglePlayVersion googlePlayVersion){
        this.mContext = mContext;
        this.googlePlayVersion = googlePlayVersion;
        this.pm = mContext.getPackageManager();
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        try {
            pInfo =  pm.getPackageInfo(mContext.getPackageName(),0);
            currentVersion = pInfo.versionName;
            Log.e(TAG, " current app version : "+currentVersion);

        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }

        try {
            //It retrieves the latest version by scraping the content of current version from play store at runtime
            Document doc = Jsoup.connect("https://play.google.com/store/apps/details?id=com.analyst.fragmenttest").get();
            latestVersion = doc.getElementsByClass("htlgb").get(6).text();
            Log.e(TAG, " current PlayStore version : "+latestVersion);

        }catch (Exception e){
            e.printStackTrace();

        }

        return new JSONObject();
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        googlePlayVersion.onReceiveGooglePlayVersion(currentVersion, latestVersion);
    }
}