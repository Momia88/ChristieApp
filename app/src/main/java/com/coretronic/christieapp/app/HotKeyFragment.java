package com.coretronic.christieapp.app;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.List;

public class HotKeyFragment extends Fragment {

    private static final String TAG = HotKeyFragment.class.getSimpleName();
    // slidedraw
    private GridView gv;
    private SlidingDrawer sd;
    private ImageView iv;
    private List<ResolveInfo> apps;
    private float density;
    private TableRow.LayoutParams btnParams;
    private TableLayout tableLayout;
    private Button btnValue;
    private Resources rs;
    private TextView hotkeyValue;
    private int btnHeight = 70;
    private int rowMax = 3;
    private int colMax = 3;
    private int txtSize = 36;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rs = getActivity().getResources();
        density = AppUtils.getDensity(getActivity());
        btnParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, (int) (btnHeight * density));
        btnParams.setMargins(3,3,3,3);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hot_key, container, false);

        // table layout
        tableLayout = (TableLayout) view.findViewById(R.id.keyBtn);
        addNumKeyBtn(btnParams);

        // hotkeyValue
        hotkeyValue = (TextView) view.findViewById(R.id.hotkeyValue);

        // slidedraw
        int tableHeight = (colMax + 1) * btnHeight + colMax * 18;
        LinearLayout.LayoutParams tableParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (tableHeight * density));
        sd = (SlidingDrawer) view.findViewById(R.id.sliding);
        sd.setLayoutParams(tableParams);
        iv = (ImageView) view.findViewById(R.id.imageViewIcon);
        sd.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener()// 開抽屜
        {
            @Override
            public void onDrawerOpened() {
                iv.setImageResource(R.drawable.ic_up);// 響應開抽屜事件, change icon
            }
        });
        sd.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {
                iv.setImageResource(R.drawable.ic_down);// 響應關抽屜事件, change icon
                hotkeyValue.setText("HOTKEY: " + btnValue.getText().toString());
            }
        });
        return view;
    }

    private void addNumKeyBtn(TableRow.LayoutParams params) {

        for (int row = 0; row < rowMax; row++) {
            TableRow tableRow = new TableRow(getActivity());
            for (int col = 0; col < colMax; col++) {
                Button btn = new Button(getActivity());
                btn.setLayoutParams(params);
                final int num = row * colMax + col + 1;
                Log.d(TAG, "id: " + num);
                btn.setText(String.valueOf(num));
                btn.setTextColor(rs.getColorStateList(R.color.text_style_1));
                btn.setBackgroundResource(R.drawable.btn_style_1);
                btn.setTextSize(txtSize);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnValue.setText(String.valueOf(num));
                    }
                });
                tableRow.addView(btn);
            }
            tableLayout.addView(tableRow);
        }

        // add btn 0
        TableRow tableRow = new TableRow(getActivity());
        Button btnZero = new Button(getActivity());
        btnZero.setLayoutParams(params);
        btnZero.setText(String.valueOf(0));
        btnZero.setTextColor(rs.getColorStateList(R.color.text_style_1));
        btnZero.setBackgroundResource(R.drawable.btn_style_1);
        btnZero.setTextSize(txtSize);
        btnZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnValue.setText(String.valueOf(0));
            }
        });
        tableRow.addView(btnZero);

        // add text view
        TableRow.LayoutParams valueParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, (int) (btnHeight * density));
        valueParams.span = 2;
        valueParams.setMargins(3,3,3,3);
        btnValue = new Button(getActivity());
        btnValue.setLayoutParams(valueParams);
        btnValue.setTextColor(rs.getColor(R.color.white));
        btnValue.setTextSize(txtSize);
        btnValue.setBackgroundResource(R.drawable.btn_style_1);
        tableRow.addView(btnValue);

        // add to tablelayout
        tableLayout.addView(tableRow);
    }

    private void loadApps() {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        apps = getActivity().getPackageManager().queryIntentActivities(intent, 0);
    }

    @Override
    public void onResume() {
        super.onResume();
    }


}
