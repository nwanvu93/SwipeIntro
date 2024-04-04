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
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String appPackageName = appItemAdapter.getData().get(position).getAppId();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri
                    .parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException e) {
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
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    AppItem app = new AppItem();
                    JSONObject obj = jsonArray.getJSONObject(i);
                    if (!obj.isNull("name"))
                        app.setAppName(obj.getString("name"));
                    if (!obj.isNull("id"))
                        app.setAppId(obj.getString("id"));
                    if (!obj.isNull("icon"))
                        app.setAppIcon(obj.getString("icon"));
                    if (!obj.isNull("rating"))
                        app.setAppRating(obj.getDouble("rating"));
                    if (!obj.isNull("author"))
                        app.setAppAuthor(obj.getString("author"));

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
            if (appItemsResult == null || appItemsResult.isEmpty()) {
                return;
            }
            appItemAdapter.setData(appItemsResult);
            appItemAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
