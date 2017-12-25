package cn.yzhg.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

/**
 * 作者 涛雷科技 , 时间 2017/12/25.
 * <p>
 * 警句:真英雄大勇无谓,好风景总在险峰
 * 概括(Android 6.0动态申请权限工具类)
 * <p>
 * <p>
 * Builder设计模式:
 * 需要构造的目标 和 嵌套在该类内部的Builder类,通过两步操作完成目标类的构造
 */

public class PermissionUtils {

    public static final int READ_CONTACTS_CODE = 10001;
    public static final int CALL_PHONE_CODE = 10002;
    public static final int READ_CALENDAR_CODE = 10003;
    public static final int CAMERA_CODE = 10004;
    public static final int BODY_SENSORS_CODE = 10005;
    public static final int ACCESS_FINE_LOCATION_CODE = 10006;
    public static final int WRITE_EXTERNAL_STORAGE_CODE = 10007;
    public static final int RECORD_AUDIO_CODE = 10008;
    public static final int SEND_SMS_CODE = 10009;
    public static final String READ_CONTACTS = Manifest.permission.READ_CONTACTS;  //允许程序访问联系人通讯录信息
    public static final String CALL_PHONE = Manifest.permission.CALL_PHONE;  //允许程序从非系统拨号器里拨打电话
    public static final String READ_CALENDAR = Manifest.permission.READ_CALENDAR;  //允许程序读取用户的日志信息
    public static final String CAMERA = Manifest.permission.CAMERA;  //允许程序访问摄像头进行拍照
    public static final String BODY_SENSORS = Manifest.permission.BODY_SENSORS;  //感应器权限
    public static final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    // 允许程序通过GPS芯片接收卫星的定位信息
    public static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    //SD卡读写权限
    public static final String RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;  //
    // 允许程序录制声音通过手机或耳机的麦克
    public static final String SEND_SMS = Manifest.permission.SEND_SMS;   //允许程序发送短信

    public static class Builder {
        private String title = "权限申请";
        private String message = "您还没有申请该权限";
        private String negative = "取消";
        private String positive = "去设置";
        private PermissionListener pListener;

        /*设置弹出框标题*/
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /*设置弹出框显示的信息*/
        Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /*设置弹出框,确定按钮显示的字体,默认为去设置*/
        Builder setNegative(String negative) {
            this.negative = negative;
            return this;
        }

        /*设置弹出框取消字体显示,默认为取消*/
        Builder setPositive(String positive) {
            this.positive = positive;
            return this;
        }

        /*去检查单个权限是否已经申请*/
        void examinePermission(Activity context, String permission, int ResultCode) {
            int resultCode = ContextCompat.checkSelfPermission(context, permission);
            if (resultCode == PackageManager.PERMISSION_DENIED) {  /*如果该权限还没有申请就返回-1*/
                if (rejectPermission(context, permission)) {
                    /*用户拒绝过此权限*/
                    showPermissionDialog(context, permission, ResultCode, title, message, negative, positive);
                } else {
                    /*用户没有拒绝过此权限*/
                    applyPermission(context, permission, ResultCode);
                }
            } else if (resultCode == PackageManager.PERMISSION_GRANTED) {
                /*已经申请过该权限*/
                if (pListener != null) {
                    pListener.possessPermission();
                }
            }
        }

        /*检查单个权限是否被用户拒绝过,返回true说明用户拒绝过此权限*/
        private boolean rejectPermission(Activity context, String permission) {
            return ActivityCompat.shouldShowRequestPermissionRationale(context, permission);
        }

        /*去申请单个权限*/
        private void applyPermission(Activity context, String permission, int RequestCode) {
            ActivityCompat.requestPermissions(context, new String[]{permission},
                    RequestCode);
        }

        /*设计Dialog弹出框给用户解释为什么需要这个权限*/
        private void showPermissionDialog(
                Activity context, String permission, int ResultCode,
                String title, String message, String negative, String positive) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setNegativeButton(negative, (dialog, which) -> {
            /*用户点击了取消按钮,*/
                if (pListener != null) pListener.cancelPermission();
            });
            builder.setPositiveButton(positive, (dialog, which) -> {
            /*用户点击了确定按钮,再次去申请权限*/
                applyPermission(context, permission, ResultCode);
            });
            builder.create();
            builder.show();
        }

        /*用户点击了不再询问后,需要用户跳转到手机设置页面,手动打开权限*/
        public void settingDialog( Activity context){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setNegativeButton(negative, (dialog, which) -> {
            /*用户点击了取消按钮,*/
                if (pListener != null) pListener.cancelPermission();
            });
            builder.setPositiveButton(positive, (dialog, which) -> {
            /*用户点击了确定按钮,再次去申请权限*/
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", context.getApplicationContext().getPackageName(), null);
                intent.setData(uri);
                context.startActivity(intent);
            });
            builder.create();
            builder.show();

        }


        Builder setPermissionListener(PermissionListener pListener) {
            this.pListener = pListener;
            return this;
        }

        /*使用接口回调的方式,调用取消事件*/
        public interface PermissionListener {
            void possessPermission();  //已经有权限了,可以去执行逻辑

            void cancelPermission();  //给用户解释权限的时候,用户点击了取消按钮

        }
    }
}

























/*
 * Android 6.0动态权限获取步骤后:
 *  获取联系人权限
 * group:android.permission-group.CONTACTS
 * permission:android.permission.WRITE_CONTACTS  写入联系人,但不可读取
 * permission:android.permission.GET_ACCOUNTS  允许程序访问账户Gmail列表
 * permission:android.permission.READ_CONTACTS 允许程序访问联系人通讯录信息
 *
 *  电话权限
 *  group:android.permission-group.PHONE
 *  permission:android.permission.READ_CALL_LOG 读取通话记录
 *  permission:android.permission.READ_PHONE_STATE  允许程序访问电话状态
 *  permission:android.permission.CALL_PHONE  允许程序从非系统拨号器里拨打电话
 *  permission:android.permission.WRITE_CALL_LOG  允许程序写入（但是不能读）用户的联系人数据
 *  permission:android.permission.USE_SIP  允许程序使用SIP视频服务
 *  permission:android.permission.PROCESS_OUTGOING_CALLS  允许程序监视，修改或放弃播出电话
 *  permission:com.android.voicemail.permission.ADD_VOICEMAIL  允许一个应用程序添加语音邮件系统
 *
 *  读写权限
 *  group:android.permission-group.CALENDAR
 *  permission:android.permission.READ_CALENDAR  允许程序读取用户的日志信息
 *  permission:android.permission.WRITE_CALENDAR  允许程序写入日程，但不可读取
 *
 *  相机拍照权限
 *  group:android.permission-group.CAMERA
 *  permission:android.permission.CAMERA  允许程序访问摄像头进行拍照
 *
 *  感应器权限
 *  group:android.permission-group.SENSORS
 *  permission:android.permission.BODY_SENSORS
 *
 *  位置权限
 *  group:android.permission-group.LOCATION
 *  permission:android.permission.ACCESS_FINE_LOCATION  允许程序通过GPS芯片接收卫星的定位信息
 *  permission:android.permission.ACCESS_COARSE_LOCATION  允许程序通过WiFi或移动基站的方式获取用户错略的经纬度信息
 *
 *  SD卡读写权限
 *  group:android.permission-group.STORAGE
 *  程序可以读取设备外部存储空间（内置SDcard和外置SDCard）的文件，如果您的App已经添加了“WRITE_EXTERNAL_STORAGE ”权限 ，则就没
 *  必要添加读的权限了，写权限已经包含了读权限了。
 *  permission:android.permission.READ_EXTERNAL_STORAGE
 *  permission:android.permission.WRITE_EXTERNAL_STORAGE
 *
 *  group:android.permission-group.MICROPHONE
 *  permission:android.permission.RECORD_AUDIO  允许程序录制声音通过手机或耳机的麦克
 *
 *  group:android.permission-group.SMS  允许程序接收短信
 *  permission:android.permission.READ_SMS 允许程序读取短信内容
 *  permission:android.permission.RECEIVE_WAP_PUSH  允许程序接收WAP PUSH信息
 *  permission:android.permission.RECEIVE_MMS 允许程序接收彩信
 *  permission:android.permission.RECEIVE_SMS
 *  permission:android.permission.SEND_SMS  允许程序发送短信
 *  permission:android.permission.READ_CELL_BROADCASTS
 *
 *  1.检查是否已经有权限了
 *  ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
 *
 *  这个方法是,判断是否已经申请了权限,返回int值
 *  PackageManager.PERMISSION_DENIED  == -1  表示用户还没有申请权限
 *  PackageManager.PERMISSION_GRANTED  == 0  表示用户已经申请了权限
 *
 * 第二步:
 *  ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
 *  .WRITE_EXTERNAL_STORAGE)
 *
 *  这个方法是判断,用户以前是否拒绝过此权限,返回true则表示用户此前拒绝了该权限,返回false则表示没有拒绝过
 *
 *  如果为true,则需要给用户提示框解释该权限的用处
 *
 *  否则就去申请权限
 *  需要动态申请的9个权限
 *
 * permission:android.permission.READ_CONTACTS 允许程序访问联系人通讯录信息
 *
 * permission:android.permission.CALL_PHONE  允许程序从非系统拨号器里拨打电话
 *
 * permission:android.permission.READ_CALENDAR  允许程序读取用户的日志信息
 *
 * permission:android.permission.CAMERA  允许程序访问摄像头进行拍照
 *
 * permission:android.permission.BODY_SENSORS  感应器权限
 *
 * permission:android.permission.ACCESS_FINE_LOCATION  允许程序通过GPS芯片接收卫星的定位信息
 *
 * permission:android.permission.WRITE_EXTERNAL_STORAGE SD卡读写权限
 *
 * permission:android.permission.RECORD_AUDIO  允许程序录制声音通过手机或耳机的麦克
 *
 * permission:android.permission.SEND_SMS  允许程序发送短信
 */
