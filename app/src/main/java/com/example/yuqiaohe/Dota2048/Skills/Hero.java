package com.example.yuqiaohe.Dota2048.Skills;

import android.content.Context;
import android.content.SharedPreferences;

public class Hero {
    public  SharedPreferences s;
    public Hero(Context context)
    {
        s=context.getSharedPreferences("Heroes",Context.MODE_PRIVATE);
    }
    public  int getPrice(int id)
    {
        if(id==0)return 0;
        return 100*(id+1)*(id+1);
    }
    public  void bought(int id)
    {
        SharedPreferences.Editor e= s.edit();
        e.putInt("Bought"+id,1);
        e.apply();
    }
    public  boolean hasNone()
    {
        return (s.getInt("Bought1",0)+s.getInt("Bought2",0)+s.getInt("Bought3",0))==0;
    }
    public  boolean isbought(int id) {
        return s.getInt("Bought"+id, 0) == 1;
    }
}
