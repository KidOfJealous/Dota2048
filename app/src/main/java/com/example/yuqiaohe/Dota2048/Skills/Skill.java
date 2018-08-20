package com.example.yuqiaohe.Dota2048.Skills;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.yuqiaohe.Dota2048.Activity.MainActivity;

public class Skill {
    protected SharedPreferences s;
    protected int SkillLevel;
    protected int Price;
    protected String SkillName;
    protected int SkillId;
    protected int CoolDown;
    protected int CurrentCool;
    protected int image;
    public final boolean clickable=false;
    public Skill(Context context,String name)
    {
        SkillName=name;
        s = context.getSharedPreferences(SkillName,context.MODE_PRIVATE);
        SkillLevel = s.getInt("skillLevel1",0);
        Price = (SkillLevel+1)*(SkillLevel+1)*100;
    }
    public void levelUp()
    {
        SkillLevel++;
        Price = (SkillLevel+1)*(SkillLevel+1)*100;
        SharedPreferences.Editor editor = s.edit();
        editor.putInt("skillLevel1",SkillLevel);
        editor.apply();
    }
    public int getSkillLevel(){return SkillLevel;}
    public int getImage() {
        return image;
    }
    public int getPrice(){return Price;}

    public double getMultiple(){return 1.0;}
    public void ClickOn(){
        if(CurrentCool==0){
            CurrentCool=CoolDown;
        }
        else Toast.makeText(MainActivity.getMainActivity(), "技能还在冷却，剩余"+CoolDown+"次移动",Toast.LENGTH_SHORT).show();
    }
    protected void write(String name,int value)
    {
        SharedPreferences.Editor editor = s.edit();
        editor.putInt(name,value);
        editor.apply();
    }
    public void init(){
    }

    public void Cool()
    {
        if(CurrentCool>0)CurrentCool--;
    }

}
