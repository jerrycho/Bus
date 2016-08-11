package com.jerry.bus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.jerry.bus.adapter.PagerAdapter;
import com.jerry.bus.R;
import com.jerry.bus.domain.BackEvent;
import com.jerry.bus.domain.BusComeBack;
import com.jerry.bus.domain.BusRouteResult;
import com.jerry.bus.domain.BusStop;
import com.jerry.bus.domain.BusStopResult;
import com.jerry.bus.domain.MyBusCallParam;
import com.jerry.bus.domain.OutEvent;
import com.jerry.bus.utils.GsonUtil;
import com.pixplicity.easyprefs.library.Prefs;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.UUID;

import okhttp3.Call;
import pl.coreorb.selectiondialogs.data.SelectableIcon;
import pl.coreorb.selectiondialogs.dialogs.IconSelectDialog;


public class MainActivity extends AppCompatActivity {

    private MenuItem mSearchMenuItem;
    private FrameLayout flLoading;
    private String searchBusNo;
    private String busStopNameEN;
    private String busStopNameTC;
    private String selectedBound;
    private int selectTab=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        flLoading = (FrameLayout) findViewById(R.id.flLoading);
        flLoading.setVisibility(View.INVISIBLE);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("OUT/出去"));
        tabLayout.addTab(tabLayout.newTab().setText("BACK/返黎"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                //Toast.makeText(MainActivity.this,"tab"+tab.getPosition(),Toast.LENGTH_LONG).show();
                selectTab = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("DEBUG", "onQueryTextSubmit " + query);
                //showDialog();
                doSearchByBusNumber(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    private static final String TAG_SELECT_ICON_DIALOG = "TAG_SELECT_ICON_DIALOG";

    private void doSearchByBusNumber(String busNo){
        this.searchBusNo = busNo.toUpperCase();
        OkHttpUtils
                .get()
                .url("http://www.lwb.hk/ajax/getRoute_info.php")
                        .addParams("t", UUID.randomUUID().toString())
                        .addParams("field9", searchBusNo)
                .build()
                .connTimeOut(1000)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Server error / no internet",
                                Toast.LENGTH_SHORT);

                        toast.show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        BusRouteResult busRouteResult = GsonUtil.fromJson(response, BusRouteResult.class);
                        if (busRouteResult != null && busRouteResult.valid && busRouteResult.bus_arr != null) {
                            ArrayList<SelectableIcon> items = new ArrayList<SelectableIcon>(2);
                            int i = 1;
                            for (BusComeBack bcb : busRouteResult.bus_arr) {
                                SelectableIcon icon = new SelectableIcon();
                                icon.setId(String.valueOf(i));
                                icon.setName(bcb.origin + "-->" + bcb.destination
                                                + "\n" +
                                                bcb.origin_chi + "-->" + bcb.destination_chi
                                );

                                items.add(icon);
                                i++;
                            }
                            showRouteDialog(items);
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Invalid Number",
                                    Toast.LENGTH_SHORT);

                            toast.show();
                        }
                    }
                });
    }

    /*
    example : dialog
                From A to B
                From B to A
     */
    public void showRouteDialog(ArrayList<SelectableIcon> items){
        new IconSelectDialog.Builder(MainActivity.this)
                .setTitle("Choose")
                .setIcons(items)
                .setOnIconSelectedListener(new IconSelectDialog.OnIconSelectedListener() {
                    @Override
                    public void onIconSelected(SelectableIcon selectedItem) {
                        selectedBound = selectedItem.getId();
                        OkHttpUtils
                                .get()
                                .url("http://www.lwb.hk/ajax/getRoute_info.php")
                                .addParams("t", UUID.randomUUID().toString())
                                .addParams("field9", searchBusNo)
                                .addParams("chkroutebound", "true")
                                .addParams("routebound", selectedBound)
                                .build()
                                .connTimeOut(1000)
                                .execute(new StringCallback() {

                                    @Override
                                    public void onError(Call call, Exception e, int id) {
                                        Toast toast = Toast.makeText(getApplicationContext(),
                                                "Server error / no internet",
                                                Toast.LENGTH_SHORT);

                                        toast.show();
                                    }

                                    @Override
                                    public void onResponse(String response, int id) {
                                        BusStopResult busStopResult = GsonUtil.fromJson(response, BusStopResult.class);
                                        ArrayList<SelectableIcon> items = new ArrayList<SelectableIcon>();
                                        for (int i = 0; i < busStopResult.bus_arr.size(); i++) {
                                            BusStop bs = busStopResult.bus_arr.get(i);

                                            SelectableIcon icon = new SelectableIcon();
                                            icon.setId(i + "|" + bs.STOP_CODE + "|" + bs.STOP_NAME_ENG + "|" + bs.STOP_NAME_CHI);
                                            icon.setName(bs.STOP_NAME_ENG
                                                            + "\n" +
                                                            bs.STOP_NAME_CHI
                                            );
                                            items.add(icon);
                                        }
                                        showBusStationDialog(items);
                                    }
                                });
                    }
                })
                .build().show(MainActivity.this.getSupportFragmentManager(), "1");
    }

    public void showBusStationDialog(ArrayList<SelectableIcon> items){
        new IconSelectDialog.Builder(MainActivity.this)
                .setTitle("Choose")
                .setIcons(items)
                .setOnIconSelectedListener(new IconSelectDialog.OnIconSelectedListener() {
                    @Override
                    public void onIconSelected(SelectableIcon selectedItem) {
                        String[] strs = selectedItem.getId().split("\\|");
                        MyBusCallParam param = new MyBusCallParam();
                        param.searchBusNo = searchBusNo;
                        param.bound = selectedBound;
                        param.stopCode = strs[1].replaceAll("-", "");
                        param.stopSeq =  strs[0];
                        param.busStopNameEN = strs[2];
                        param.busStopNameTC = strs[3];
                        if (selectTab == 0) {
                            param.direction = "out";
                            param.save();
                            EventBus.getDefault().post(new OutEvent(param));
                        } else {
                            param.direction = "back";
                            param.save();
                            EventBus.getDefault().post(new BackEvent(param));
                        }




                        //Pass to fragment

//                        OkHttpUtils
//                                .get()
//                                .url("http://etav2.kmb.hk/")
//                                .addParams("t", UUID.randomUUID().toString())
//                                .addParams("action", "geteta")
//                                .addParams("lang", "tc")
//                                .addParams("route", searchBusNo)
//                                .addParams("bound",selectedBound)
//                                .addParams("stop", strs[1].replaceAll("-", ""))
//                                .addParams("stop_seq", strs[0])
//                                .build()
//                                .connTimeOut(1000)
//                                .execute(new StringCallback() {
//
//                                    @Override
//                                    public void onError(Call call, Exception e, int id) {
//                                        Toast toast = Toast.makeText(getApplicationContext(),
//                                                "Server error / no internet",
//                                                Toast.LENGTH_SHORT);
//                                        toast.show();
//                                    }
//
//                                    @Override
//                                    public void onResponse(String response, int id) {
//                                        Log.d("DEBUG", "response>>>" + response);
//                                    }
//                                });
                    }
                })
                .build().show(MainActivity.this.getSupportFragmentManager(), "2");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_en:
//                Intent intent = new Intent(this, SettingsActivity.class);
//                startActivity(intent);
                Prefs.putString("lang", "EN");
//                Toast.makeText(MainActivity.this,"Hihi",Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_tc:
//                Intent shareIntent = new Intent();
//                shareIntent.setAction(Intent.ACTION_SEND);
//                shareIntent.setType("text/plain");
//                shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.message_share_text));
//                startActivity(Intent.createChooser(shareIntent, getString(R.string.message_share_title)));
                Prefs.putString("lang","TC");
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}