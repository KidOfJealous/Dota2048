package com.Dota2048.Data;

import android.content.Context;
import android.content.SharedPreferences;

public class BestScore {
    private SharedPreferences s;
    public BestScore(Context context){
        s = context.getSharedPreferences("bestscore",context.MODE_PRIVATE);
    }

    public int getBestScode(){
        return s.getInt("bestscore",0);
    }
    public void setBestScode(int bestScode){
        SharedPreferences.Editor editor = s.edit();
        editor.putInt("bestscore",bestScode);
        editor.apply();
    }
}
