package cn.yzhg.permission;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
                .setPermissionListener(new PermissionUtils.Builder.PermissionListener() {
                    @Override
                    public void possessPermission() {
                        /*全部已经授权*/
                        Log.i("动态权限是申请", "possessPermission: 全部已经授权");
                        Intent intent = new Intent();
                        intent.setClass(LaunchActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void cancelPermission() {

                    }
                })
                .morePermission(this, PermissionUtils.permission);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Toast.makeText(this, "权限回调于此", Toast.LENGTH_SHORT).show();
        if (requestCode == PermissionUtils.ALL_PERMISSION_CODE) {
            Intent intent = new Intent();
            intent.setClass(this, MainActivity.class);
            startActivity(intent);
        }
    }

}
