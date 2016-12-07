package com.example.wangweimin.customerview.activity;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.wangweimin.customerview.R;
import com.example.wangweimin.customerview.service.HackDialogService;

/**
 * Created by wangweimin on 16/12/7.
 */

public class HackDialogServiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hack_dialog);

        Button start = (Button) findViewById(R.id.start_btn);
        if (start != null)
            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkSystemWindowPermission() && checkUsagePermission()) {
                        Intent intent = new Intent(getApplicationContext(), HackDialogService.class);
                        startService(intent);
                        Log.e(HackDialogServiceActivity.class.getSimpleName(), "start service");
                    }
                }
            });

    }

    private boolean checkSystemWindowPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 1);
            return false;
        }
        return true;
    }

    private boolean checkUsagePermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int mode = 0;
            mode = appOps.checkOpNoThrow("android:get_usage_stats", android.os.Process.myUid(), getPackageName());
            boolean granted = mode == AppOpsManager.MODE_ALLOWED;
            if(!granted){
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                startActivityForResult(intent, 2);
                return false;
            }
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(Settings.canDrawOverlays(this)){
                if(checkUsagePermission()){
                    Intent intent = new Intent(this, HackDialogService.class);
                    startService(intent);
                }
            }
        }else if(requestCode == 2){
            AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int mode = 0;
            mode = appOps.checkOpNoThrow("android:get_usage_stats", android.os.Process.myUid(), getPackageName());
            boolean granted = mode == AppOpsManager.MODE_ALLOWED;
            if(!granted)
                Toast.makeText(this, "请开启该权限", Toast.LENGTH_SHORT).show();
        }
    }
}
