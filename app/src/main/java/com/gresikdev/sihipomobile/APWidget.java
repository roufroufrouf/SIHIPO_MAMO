package com.gresikdev.sihipomobile;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.gresikdev.sihipomobile.hotspotmanager.WifiApManager;

public class APWidget extends AppWidgetProvider {
    public static String WIFIAP_STATE_CHANGED = "android.net.wifi.WIFI_AP_STATE_CHANGED";
    private static final String STATE = "state";
    private boolean state;

    @Override
    public void onReceive(Context context, Intent intent) {
        lg("on recev");

        WifiApManager apMan = new WifiApManager(context);
        state = apMan.isWifiApEnabled();
        lg("state: " + state);

        if (WIFIAP_STATE_CHANGED.equals(intent.getAction())) {
            lg("ap changed");
        }

        if (intent.getBooleanExtra(STATE, false)) {
            state = !state;
            apMan.setWifiApEnabled(null, state);
            lg("Widget button click");
        }

        if (intent.getBooleanExtra(STATE, false) || (WIFIAP_STATE_CHANGED.equals(intent.getAction()) && !intent.getBooleanExtra(STATE, false))) {
            AppWidgetManager awm = AppWidgetManager.getInstance(context);
            ComponentName cn = new ComponentName(context, this.getClass());
            int[] ids = awm.getAppWidgetIds(cn);
            onUpdate(context, awm, ids);
        }

        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        lg("on update");
        lg("Status: " + state);
        for (int i = 0; i < appWidgetIds.length; i++) {
            int currentId = appWidgetIds[i];
            lg("id: " + currentId);
            Intent intent = new Intent(context, APWidget.class);
            intent.putExtra(STATE, true);
            PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget);
            rv.setImageViewResource(R.id.widgetButton1, toggleButton());
            rv.setOnClickPendingIntent(R.id.widgetButton1, pi);
            rv.setOnClickPendingIntent(R.id.widgetLinearLayout1, tphs(context));
            appWidgetManager.updateAppWidget(currentId, rv);
            lg("Updated");
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private PendingIntent tphs(Context c) {
        Intent intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pi = PendingIntent.getActivity(c, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pi;
    }

    private int toggleButton() {
        return state ? R.drawable.bt_hp_on : R.drawable.bt_hp_off;
    }

    private void lg(String p0) {
        Log.i("rah", p0);
    }
}
