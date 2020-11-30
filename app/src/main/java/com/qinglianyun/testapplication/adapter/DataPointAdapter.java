package com.qinglianyun.testapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qinglianyun.testapplication.R;
import com.qinglianyun.testapplication.bean.DeviceBean;

import java.util.List;

/**
 * Created by tang_xqing on 2020/6/18.
 */
public class DataPointAdapter extends RecyclerView.Adapter<DataPointAdapter.ChildViewHolder> {

    private Context mContext;
    private List<DeviceBean.SubdevsBeanX.DatapointsBean> mDataPointList;

    public DataPointAdapter(Context mContext, List<DeviceBean.SubdevsBeanX.DatapointsBean> mDataPointList) {
        this.mContext = mContext;
        this.mDataPointList = mDataPointList;
    }

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.child_item_data_point_layout, parent, false);
        return new ChildViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildViewHolder holder, int position) {
        // TODO: 2020/6/18 赋值
        DeviceBean.SubdevsBeanX.DatapointsBean bean = mDataPointList.get(position);
        holder.mTvDataPoint.setText(bean.getName() + "  " + bean.getDp_id());
    }

    @Override
    public int getItemCount() {
        return null == mDataPointList ? 0 : mDataPointList.size();
    }

    public class ChildViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout mRl;
        private ImageView mIvWrite;
        private TextView mTvDataPoint;

        public ChildViewHolder(View view) {
            super(view);
            mRl = view.findViewById(R.id.child_item_rl);
            mIvWrite = view.findViewById(R.id.child_item_iv);
            mTvDataPoint = view.findViewById(R.id.child_item_tv);
        }
    }
}
