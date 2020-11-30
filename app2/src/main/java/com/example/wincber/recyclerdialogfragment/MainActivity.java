package com.example.wincber.recyclerdialogfragment;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button mFragment = (Button) findViewById(R.id.fragment_bt);

        final MyDialogFragment fragment = new MyDialogFragment();
        mFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.show(getFragmentManager(),"dialogFragment");
            }
        });
    }

}
