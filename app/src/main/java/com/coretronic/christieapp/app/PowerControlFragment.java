package com.coretronic.christieapp.app;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PowerControlFragment extends Fragment {
    private String TAG = PowerControlFragment.class.getSimpleName();
    private LinearLayout powerGroup;
    private ImageButton powerIcon;
    private LinearLayout buttonGroup;
    private TextView btn1;
    private View line;
    private TextView btn2;
    private boolean powerState = false;
    private Resources rs;

    private void assignViews(View v) {
        powerGroup = (LinearLayout) v.findViewById(R.id.powerGroup);
        powerIcon = (ImageButton) v.findViewById(R.id.powerIcon);
        buttonGroup = (LinearLayout) v.findViewById(R.id.buttonGroup);
        btn1 = (TextView) v.findViewById(R.id.btnMenu);
        line = v.findViewById(R.id.line);
        btn2 = (TextView) v.findViewById(R.id.btnExit);
        powerIcon.setOnClickListener(btnListener);
        btn1.setOnClickListener(btnListener);
        btn2.setOnClickListener(btnListener);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rs = getActivity().getResources();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_power_control, container, false);
        assignViews(view);
        return view;
    }

    View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.powerIcon:
                    if(powerState){
                        btn1.setTextColor(rs.getColor(R.color.gray));
                        btn1.setEnabled(false);
                        btn2.setTextColor(rs.getColor(R.color.gray));
                        btn2.setEnabled(false);
                        buttonGroup.setBackgroundResource(R.color.black);
                        powerState = false;
                    }else{
                        btn1.setTextColor(rs.getColor(R.color.black));
                        btn1.setEnabled(true);
                        btn2.setTextColor(rs.getColor(R.color.black));
                        btn2.setEnabled(true);
                        buttonGroup.setBackgroundResource(R.color.origen);
                        powerState = true;
                    }
                    break;
                case R.id.btnMenu:
                    Log.d(TAG,"btn1" );
                    break;
                case R.id.btnExit:
                    Log.d(TAG,"btn2" );
                    break;
            }
        }
    };


}
