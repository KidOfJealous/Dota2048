package com.example.yuqiaohe.Dota2048.Skills;

import android.content.Context;

import com.example.yuqiaohe.Dota2048.Activity.MainActivity;

public class SkillFactory {

    public static Skill CreateSkill(Context context, int id,MainActivity ma)
    {
        switch (id)
        {
            case 1:return new RequiemOfSouls(context,ma);
            case 2:return new TimeLapse(context,ma);
            case 3:return new LagunaBlade(context,ma);
        }
        return new Skill(context,"default",ma);
    }
}
