package com.example.yuqiaohe.Dota2048.Skills;

import android.content.Context;

import com.example.yuqiaohe.test.R;

public class RequiemOfSouls extends Skill{

    public final boolean clickable = false;
    public RequiemOfSouls(Context context){
        super(context,"RequiemOfSouls");
        SkillId=1;
        CoolDown=0;
        image= R.drawable.skill_1;
    }
    @Override
    public double getMultiple()
    {
        return (SkillLevel==0||Math.random()>=0.15)?1.0:(1.2+SkillLevel*1.1);
    }
}


