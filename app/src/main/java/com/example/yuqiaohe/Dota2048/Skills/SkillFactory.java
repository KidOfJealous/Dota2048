package com.example.yuqiaohe.Dota2048.Skills;

import android.content.Context;

public class SkillFactory {

    public static Skill CreateSkill(Context context,int id)
    {
        switch (id)
        {
            case 1:return new RequiemOfSouls(context);
            case 2:return new TimeLapse(context);
            case 3:return new LagunaBlade(context);
        }
        return null;
    }
}
