package cn.yzhg.permission;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new PermissionUtils.builder().setTitle("需要的权限")
                .setMessage("这个权限必须申请,如果不申请就不让你玩了")
                .setNegative("取消")
                .setPositive("确定")
                .examinePermission(this, PermissionUtils.CAMERA, PermissionUtils.CAMERA_CODE)
                .setPermissionListener(new PermissionUtils.builder.PermissionListener() {
                    @Override
                    public void possessPermission() {
                        /*已经申请过该权限,可以去做想做的事情*/
                    }

                    @Override
                    public void cancelPermission() {
                        /*用户点击了,解释时取消按钮*/
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtils.CAMERA_CODE) {
            if (useLoop(grantResults, PermissionChecker.PERMISSION_GRANTED)) {
                Toast.makeText(this, "被授权了", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "没有授权", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static boolean useLoop(int[] grantResults, int resultCode) {
        for (int grantResult : grantResults) {
            if (grantResult == resultCode) return true;
        }
        return false;
    }
}


















