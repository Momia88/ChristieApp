package com.coretronic.christieapp.app.Common;

import com.coretronic.christieapp.app.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Morris on 15/1/21.
 */
public class KeyMap {

    public static final Map<Integer,String> keyMap;
    static
    {
        keyMap = new HashMap<Integer, String>();
        keyMap.put(R.id.key_1,"(KEY 26)");
        keyMap.put(R.id.key_2,"(KEY 27)");
        keyMap.put(R.id.key_3,"(KEY 28)");
        keyMap.put(R.id.key_4,"(KEY 29)");
        keyMap.put(R.id.key_5,"(KEY 30)");
        keyMap.put(R.id.key_6,"(KEY 31)");
        keyMap.put(R.id.key_7,"(KEY 32)");
        keyMap.put(R.id.key_8,"(KEY 33)");
        keyMap.put(R.id.key_9,"(KEY 34)");
        keyMap.put(R.id.key_0,"(KEY 36)");

        keyMap.put(R.id.key_pow_on,"(PWR 1)");
        keyMap.put(R.id.key_pow_off,"(PWR 0)");
        keyMap.put(R.id.key_info,"(KEY 19)");
        keyMap.put(R.id.key_exit,"(KEY 20)");
        keyMap.put(R.id.key_info,"(KEY 48)");
        keyMap.put(R.id.key_exit,"(KEY 47)");
        keyMap.put(R.id.key_info,"(KEY 66)");
        keyMap.put(R.id.key_up,"(KEY 38)");
        keyMap.put(R.id.key_down,"(KEY 42)");
        keyMap.put(R.id.key_left,"(KEY 39)");
        keyMap.put(R.id.key_right,"(KEY 41)");
        keyMap.put(R.id.key_enter,"(KEY 40)");

        keyMap.put(R.id.key_focus_far,"(FCS p)");
        keyMap.put(R.id.key_focus_near,"(FCS n)");
        keyMap.put(R.id.key_zoom_in,"(ZOM n)");
        keyMap.put(R.id.key_zoom_out,"(ZOM p)");
        keyMap.put(R.id.key_lens_shift_up,"(LVO p)");
        keyMap.put(R.id.key_lens_shift_down,"(LVO n)");
        keyMap.put(R.id.key_lens_shift_left,"(LHO p)");
        keyMap.put(R.id.key_lens_shift_right,"(LHO n)");

    };


}
