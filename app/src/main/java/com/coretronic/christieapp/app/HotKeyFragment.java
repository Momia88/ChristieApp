package com.coretronic.christieapp.app;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class HotKeyFragment extends Fragment {

    private static final String TAG = HotKeyFragment.class.getSimpleName();
    private Context mContext;
    private float density;
    private TableRow.LayoutParams btnParams;
    private Button btnValue;
    private Resources rs;
    private int btnHeight = 70;
    private int rowMax = 3;
    private int colMax = 3;
    private int txtSize = 36;

    private TextView hotkeyValue;
    private TextView keyUp;
    private TextView keyLeft;
    private TextView keyEnter;
    private TextView keyRight;
    private TextView keyDown;
    private LinearLayout buttonGroup;
    private TextView keyInfo;
    private View line;
    private TextView keyExit;
    private SlidingDrawer slidingDrawer;
    private TableLayout tableLayout;
    private ImageView imageViewIcon;

    private void assignViews(View v) {
        hotkeyValue = (TextView) v.findViewById(R.id.hotkeyValue);
        keyUp = (TextView) v.findViewById(R.id.key_up);
        keyLeft = (TextView) v.findViewById(R.id.key_left);
        keyEnter = (TextView) v.findViewById(R.id.key_enter);
        keyRight = (TextView) v.findViewById(R.id.key_right);
        keyDown = (TextView) v.findViewById(R.id.key_down);
        buttonGroup = (LinearLayout) v.findViewById(R.id.buttonGroup);
        keyInfo = (TextView) v.findViewById(R.id.key_info);
        line = v.findViewById(R.id.line);
        keyExit = (TextView) v.findViewById(R.id.key_exit);
        slidingDrawer = (SlidingDrawer) v.findViewById(R.id.sliding);
        tableLayout = (TableLayout) v.findViewById(R.id.keyBtn);
        imageViewIcon = (ImageView) v.findViewById(R.id.imageViewIcon);

        // table layout
        addNumKeyBtn(btnParams);

        // slidedraw
        int tableHeight = (colMax + 1) * btnHeight + colMax * 18;
        LinearLayout.LayoutParams tableParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (tableHeight * density));
        slidingDrawer.setLayoutParams(tableParams);
        slidingDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener()// 開抽屜
        {
            @Override
            public void onDrawerOpened() {
                imageViewIcon.setImageResource(R.drawable.ic_up);// 響應開抽屜事件, change icon
            }
        });
        slidingDrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {
                imageViewIcon.setImageResource(R.drawable.ic_down);// 響應關抽屜事件, change icon
                hotkeyValue.setText("HOTKEY: " + btnValue.getText().toString());
            }
        });

        keyEnter.setOnClickListener(keyListener);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        rs = getActivity().getResources();
        density = AppUtils.getDensity(getActivity());
        btnParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, (int) (btnHeight * density));
        btnParams.setMargins(3, 3, 3, 3);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hot_key, container, false);
        //get view
        assignViews(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private void addNumKeyBtn(TableRow.LayoutParams params) {

        for (int row = 0; row < rowMax; row++) {
            TableRow tableRow = new TableRow(getActivity());
            for (int col = 0; col < colMax; col++) {
                Button btn = new Button(getActivity());
                btn.setLayoutParams(params);
                final int num = row * colMax + col + 1;
                Log.d(TAG, "id: " + num);
                btn.setId(AppConfig.ids[num]);
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
        valueParams.setMargins(3, 3, 3, 3);
        btnValue = new Button(getActivity());
        btnValue.setLayoutParams(valueParams);
        btnValue.setTextColor(rs.getColor(R.color.white));
        btnValue.setTextSize(txtSize);
        btnValue.setBackgroundResource(R.drawable.btn_style_1);
        tableRow.addView(btnValue);

        // add to tablelayout
        tableLayout.addView(tableRow);
    }


    View.OnClickListener keyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int value = Integer.valueOf(btnValue.getText().toString());
            final String keyValue = KeyMap.keyMap.get(AppConfig.ids[value]);
            Log.d(TAG, "keyValue: " + keyValue);
            TelnetService.sendCommand(mContext, keyValue);
        }
    };

}
