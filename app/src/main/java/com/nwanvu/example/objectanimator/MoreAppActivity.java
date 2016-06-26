package com.nwanvu.example.objectanimator;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.nwanvu.example.objectanimator.adapter.AppItemAdapter;
import com.nwanvu.example.objectanimator.classes.Util;
import com.nwanvu.example.objectanimator.entities.AppItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;


public class MoreAppActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private View pbloading;
    private AppItemAdapter appItemAdapter;
    private LoadAppList loadAppList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_app);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        pbloading = findViewById(R.id.pbloading);
        appItemAdapter = new AppItemAdapter(this, new ArrayList<AppItem>());
        StickyListHeadersListView stickyList = (StickyListHeadersListView) findViewById(R.id.appListView);
        stickyList.setOnItemClickListener(this);
        stickyList.setEmptyView(findViewById(R.id.emptyView));
        stickyList.setDrawingListUnderStickyHeader(true);
        stickyList.setAreHeadersSticky(true);
        stickyList.setAdapter(appItemAdapter);

        loadAppList = new LoadAppList();
        loadAppList.execute();
        try {
            final AdView adView = (AdView) findViewById(R.id.adViewMore);
            AdRequest.Builder adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
            String[] stringArray = getResources().getStringArray(R.array.device_ids);
            for (String id : stringArray) {
                adRequest.addTestDevice(id);
            }
            adView.loadAd(adRequest.build());
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    adView.setVisibility(View.GONE);
                    super.onAdFailedToLoad(errorCode);
                }

                @Override
                public void onAdLeftApplication() {
                    super.onAdLeftApplication();
                }

                @Override
                public void onAdOpened() {
                    super.onAdOpened();
                }

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    adView.setVisibility(View.VISIBLE);
                }
            });
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String appPackageName = appItemAdapter.getData().get(position).getAppId();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri
                    .parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id="
                            + appPackageName)));
        }
    }

    @Override
    public void onRefresh() {
        loadAppList.cancel(true);
        loadAppList = new LoadAppList();
        loadAppList.execute();
    }

    class LoadAppList extends AsyncTask<Void, Void, ArrayList<AppItem>> {

        @Override
        protected ArrayList<AppItem> doInBackground(Void... params) {
            try {
                String json = Util.callGetApi(getString(R.string.more_app_url));
                ArrayList<AppItem> appItems = new ArrayList<>();
                JSONObject appsObj = new JSONObject(json.replace("\t", ""));
                JSONArray jsonArray = appsObj.getJSONArray("apps");
                for (int i = 0; i < jsonArray.length(); i++) {
                    AppItem app = new AppItem();
                    JSONObject obj = jsonArray.getJSONObject(i);
                    if (!obj.isNull("appname"))
                        app.setAppName(obj.getString("appname"));
                    if (!obj.isNull("appid"))
                        app.setAppId(obj.getString("appid"));
                    if (!obj.isNull("appicon"))
                        app.setAppIcon(obj.getString("appicon"));
                    if (!obj.isNull("apprating"))
                        app.setAppRating(obj.getDouble("apprating"));
                    if (!obj.isNull("appauthor"))
                        app.setAppAuthor(obj.getString("appauthor"));

                    if (!app.getAppId().equals(getPackageName()))
                        appItems.add(app);
                }
                return appItems;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<AppItem> appItemsResult) {
            super.onPostExecute(appItemsResult);
            pbloading.setVisibility(View.GONE);
            if (appItemsResult == null || appItemsResult.size() == 0) {
                return;
            }
            appItemAdapter.setData(appItemsResult);
            appItemAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
