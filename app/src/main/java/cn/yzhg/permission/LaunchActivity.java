package cn.yzhg.permission;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * 作者 涛雷科技 , 时间 2017/12/27.
 * <p>
 * 警句:真英雄大勇无谓,好风景总在险峰
 * 概括(一句话总结该类用法)
 */

public class LaunchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new PermissionUtils.Builder()
                .morePermission(this, PermissionUtils.permission, 1000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1000){
            Intent intent = new Intent();
            intent.setClass(this,MainActivity.class);
            startActivity(intent);
        }
        Toast.makeText(this, "点了一下", Toast.LENGTH_SHORT).show();
    }

}
