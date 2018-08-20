package com.example.yuqiaohe.Dota2048.Skills;

import android.content.Context;
import android.content.SharedPreferences;

public class Hero {
    private static int[] prices = {400,1200,700,2000};
    public static SharedPreferences s;
    private static int useing=0;
    public static void init(Context context){s=context.getSharedPreferences("Heroes",Context.MODE_PRIVATE);}
    public static int getPrice(int id)
    {
        return prices[id];
    }
    public static void bought(int id)
    {
        SharedPreferences.Editor e= s.edit();
        e.putInt("Bought"+id,1);
        e.apply();
    }
    public static boolean isbought(int id) {
        return s.getInt("Bought"+id, 0) == 1;
    }
    public static void setUsed(int id)
    {
        useing=id;
    }
    public int using()
    {
        return useing;
    }
}
