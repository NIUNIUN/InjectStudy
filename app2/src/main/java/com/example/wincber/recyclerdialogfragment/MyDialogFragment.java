package com.example.wincber.recyclerdialogfragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by wincber on 9/23/2016.
 */

public class MyDialogFragment extends DialogFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        dialog.show();
        Window window = dialog.getWindow();

        // 设置底部弹框
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        attributes.gravity = Gravity.BOTTOM;
//        attributes.windowAnimations = R.style.AnimUp;
        window.setAttributes(attributes);
//        window.setBackgroundDrawable(new ColorDrawable());
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        View  view = inflater.inflate(R.layout.fragment_list,container);
        RecyclerView mRecyclerView = (RecyclerView)view.findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new MyRecyclerAdapter());
        return view;
    }
}
