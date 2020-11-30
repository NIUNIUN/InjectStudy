package com.qinglianyun.testapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meituan.robust.patch.annotaion.Add;
import com.meituan.robust.patch.annotaion.Modify;
import com.qinglianyun.testapplication.R;
import com.qinglianyun.testapplication.bean.DeviceBean;

import java.util.List;

/**
 * Created by tang_xqing on 2020/6/18.
 */
public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {

    private Context mContext;
    private List<DeviceBean.SubdevsBeanX> mDeviceList;

    public DeviceAdapter(Context mContext, List<DeviceBean.SubdevsBeanX> mDeviceList) {
        this.mContext = mContext;
        this.mDeviceList = mDeviceList;
    }

    @NonNull
    @Override
    public DeviceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_device_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DeviceAdapter.ViewHolder holder, int position) {
        DeviceBean.SubdevsBeanX deviceBean = mDeviceList.get(position);

        holder.mListView.setLayoutManager(new LinearLayoutManager(mContext));
        List<DeviceBean.SubdevsBeanX.DatapointsBean> datapoints = deviceBean.getDatapoints();
        DataPointAdapter adapter = new DataPointAdapter(mContext, datapoints);
        holder.mListView.setAdapter(adapter);

//        View listItem = holder.mListView.getChildAt(0);
//        if (null != listItem) {
//            listItem.measure(0, 0);
//            int totalHei = (listItem.getMeasuredHeight()) * 3;
//            holder.mListView.getLayoutParams().height = totalHei;
//        }

//        int totalHei = 50 * 3;
//        holder.mListView.getLayoutParams().height = totalHei;


        final String sub_device_name = deviceBean.getSub_device_name();
//        holder.mTvSubDeviceId.setText(sub_device_name);

        holder.mIvState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                holder.mTvSubDeviceId.setText(getStr());
                Object tag = holder.mIvState.getTag();
                if (null == tag || (Integer) tag == Integer.valueOf(0)) {
                    holder.mListView.setVisibility(View.GONE);
                    holder.mIvState.setTag(1);
                } else {
                    holder.mListView.setVisibility(View.VISIBLE);
                    holder.mIvState.setTag(0);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        int i = null == mDeviceList ? 0 : mDeviceList.size();
        return i;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mLl;
        private ImageView mIvOnLine;
        private TextView mTvSubDeviceId;
        private RecyclerView mListView;
        private ImageView mIvState;

        public ViewHolder(View view) {
            super(view);
            mLl = view.findViewById(R.id.item_ll);
            mListView = view.findViewById(R.id.item_lv_sub);
            mIvOnLine = view.findViewById(R.id.item_iv_online);
            mIvState = view.findViewById(R.id.item_iv_state);
            mTvSubDeviceId = view.findViewById(R.id.item_tv_subdeviceid);
        }
    }
}
