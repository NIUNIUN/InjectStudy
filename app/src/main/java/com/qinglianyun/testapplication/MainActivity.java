package com.qinglianyun.testapplication;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.meituan.robust.patch.annotaion.Add;
import com.meituan.robust.patch.annotaion.Modify;
import com.qinglianyun.testapplication.adapter.DeviceAdapter;
import com.qinglianyun.testapplication.bean.DeviceBean;
import com.qinglianyun.testapplication.utils.PermissionUtils;
import com.qinglianyun.tracelib.TraceLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tang_xqing on 2020/6/17.
 */
public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecycleView;
    private DeviceAdapter mAdapter;
    private List<DeviceBean.SubdevsBeanX> mDataList;

    private static final int REQUEST_CODE_SDCARD_READ = 1;

    @Modify
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        mRecycleView = findViewById(R.id.elv_data);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));

        mDataList = new ArrayList<>();

        mAdapter = new DeviceAdapter(this, mDataList);
        mRecycleView.setAdapter(mAdapter);

        getData();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Log.d("输出", "系统版本号大于4.2");
        }

        if (17 < Build.VERSION.SDK_INT) {
            System.out.println("系统版本号大于4.2");

        }
        testPrint();
    }

    @Modify
    private void getData() {
        Toast.makeText(this, "测测测测测", Toast.LENGTH_SHORT).show();
        DeviceBean.SubdevsBeanX beanX = new DeviceBean.SubdevsBeanX();
        beanX.setSub_device_name("子设备1");
        ArrayList<DeviceBean.SubdevsBeanX.DatapointsBean> datapoints = new ArrayList<>();
        DeviceBean.SubdevsBeanX.DatapointsBean bean = new DeviceBean.SubdevsBeanX.DatapointsBean();
        bean.setDp_id("0x1001");
        bean.setName("温度");
        DeviceBean.SubdevsBeanX.DatapointsBean bean1 = new DeviceBean.SubdevsBeanX.DatapointsBean();
        bean1.setDp_id("0x1002");
        bean1.setName("湿度");
        DeviceBean.SubdevsBeanX.DatapointsBean bean2 = new DeviceBean.SubdevsBeanX.DatapointsBean();
        bean2.setDp_id("0x1003");
        bean2.setName("报警");
        DeviceBean.SubdevsBeanX.DatapointsBean bean3 = new DeviceBean.SubdevsBeanX.DatapointsBean();
        bean3.setDp_id("0x1004");
        bean3.setName("预警");
        datapoints.add(bean);
        datapoints.add(bean1);
        datapoints.add(bean2);
        datapoints.add(bean3);
        beanX.setDatapoints(datapoints);

        mDataList.add(beanX);

        DeviceBean.SubdevsBeanX beanX1 = new DeviceBean.SubdevsBeanX();
        beanX1.setSub_device_name("子设备2");
        ArrayList<DeviceBean.SubdevsBeanX.DatapointsBean> datapoints1 = new ArrayList<>();
        DeviceBean.SubdevsBeanX.DatapointsBean bean4 = new DeviceBean.SubdevsBeanX.DatapointsBean();
        bean4.setDp_id("0x2001");
        bean4.setName("温度");
        DeviceBean.SubdevsBeanX.DatapointsBean bean5 = new DeviceBean.SubdevsBeanX.DatapointsBean();
        bean5.setDp_id("0x2002");
        bean5.setName("湿度");
        DeviceBean.SubdevsBeanX.DatapointsBean bean6 = new DeviceBean.SubdevsBeanX.DatapointsBean();
        bean6.setDp_id("0x2003");
        bean6.setName("报警");
        DeviceBean.SubdevsBeanX.DatapointsBean bean7 = new DeviceBean.SubdevsBeanX.DatapointsBean();
        bean7.setDp_id("0x2004");
        bean7.setName("预警");
        datapoints1.add(bean4);
        datapoints1.add(bean5);
        datapoints1.add(bean6);
        datapoints1.add(bean7);
        beanX1.setDatapoints(datapoints1);
        mDataList.add(beanX1);

        mAdapter.notifyDataSetChanged();
    }

    @Add
    public void testPrint() {
        Toast.makeText(this, "测试添加方法", Toast.LENGTH_SHORT).show();
        Log.d("TAG", "testPrint() called  添加方法");
    }

    private void testLog() {
        System.out.println("测试输出日志");
        Log.d("日志", "测试输出日志");

        try {
            int i = 1 / 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("日志", "测试输入日志 int =" + getInt());
        Log.d("日志", "测试输入日志" + "无参数");

        Log.e("日志", "通知权限已经被打开" +
                "\n手机型号:" + android.os.Build.MODEL +
                "\nSDK版本:" + android.os.Build.VERSION.SDK +
                "\n系统版本:" + android.os.Build.VERSION.RELEASE +
                "\n软件包名:" + getPackageName());
    }

    private int getInt() {
        return 1;
    }

    private boolean isGrantSDCardReadPermission() {
        return PermissionUtils.isGrantSDCardReadPermission(this);
    }

    private void requestPermission() {
        PermissionUtils.requestSDCardReadPermission(this, REQUEST_CODE_SDCARD_READ);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_SDCARD_READ:
                handlePermissionResult();
                break;

            default:
                break;
        }
    }

    @TraceLog
    private void handlePermissionResult() {
        if (isGrantSDCardReadPermission()) {

        } else {
            Toast.makeText(this, "failure because without sd card read permission", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 美团热修复
     *   1、必须添加multid
     *   2、新增的方法，加载不成功
     *   3、在Modify方法中，新增Add方法，出现异常闪退现象。（原因：暂时没有找到）
     *   4、已打补丁的基础上，可以再次打补丁
     */
}
