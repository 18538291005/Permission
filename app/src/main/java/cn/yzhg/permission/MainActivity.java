package cn.yzhg.permission;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button but_permission = findViewById(R.id.but_permission);
        but_permission.setOnClickListener(v -> {
            new PermissionUtils.Builder()
                    .setTitle("需要的权限")
                    .setMessage("这个权限必须申请,如果不申请就不让你玩了")
                    .setNegative("取消")
                    .setPositive("确定")
                    .setPermissionListener(new PermissionUtils.Builder.PermissionListener() {
                        @Override
                        public void possessPermission() {
                        /*已经申请过该权限,可以去做想做的事情*/
                            Log.i("MainActivity", "possessPermission: 该权限已经被申请");
                        }

                        @Override
                        public void cancelPermission() {
                        /*用户点击了,解释时取消按钮*/
                            Log.i("MainActivity", "possessPermission: 用户点击了取消按钮");
                        }
                    })
                    .examinePermission(this, PermissionUtils.CAMERA, PermissionUtils.CAMERA_CODE);
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionUtils.CAMERA_CODE:
                for (int i = 0; i < grantResults.length; i++) {
                    /*判断当前权限,如果权限返回码为 -1 ,并且判断是否需要给用户解释该权限,如果不需要.这是就是弹出框提示用户打开设置页面进行时授权 */
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,permissions[i])) {
                            new PermissionUtils.Builder()
                                    .setTitle("权限申请")
                                    .setMessage("缺少相机权限,请前往设置页面手动开启")
                                    .settingDialog(this);
                        }
                    }
                }
                break;
        }
    }

    public static boolean useLoop(int[] grantResults, int resultCode) {
        for (int grantResult : grantResults) {
            if (grantResult == resultCode) return true;



            /*
            * 提交演示
            *
            * */
        }
        return false;
    }
}


















