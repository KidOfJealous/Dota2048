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
    protected String PriceString;
    protected String IntroString;
    protected MainActivity parent;
    public boolean clickable=false;
    public Skill(Context context,String name,MainActivity ma)
    {
        SkillName=name;
        s = context.getSharedPreferences(SkillName,context.MODE_PRIVATE);
        SkillLevel = s.getInt("skillLevel1",0);
        Price = (SkillLevel+1)*(SkillLevel+1)*100;
        parent=ma;
        //init();
    }
    public void levelUp()
    {
        SkillLevel++;
        Price = (SkillLevel+1)*(SkillLevel+1)*100;
        SharedPreferences.Editor editor = s.edit();
        editor.putInt("skillLevel1",SkillLevel);
        editor.apply();
        Refrash();
    }
    public String getSkillName(){return "无技能";}
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
        else Toast.makeText(parent, "技能还在冷却，剩余"+CoolDown+"次移动",Toast.LENGTH_SHORT).show();
    }
    protected void write(String name,int value)
    {
        SharedPreferences.Editor editor = s.edit();
        editor.putInt(name,value);
        editor.apply();
    }
    public void init(){
    }
    protected void Refrash()
    {
        initPriceString();
        initIntroString();
    }
    public String getPriceString()
    {
       return PriceString;
    }
    public String getIntroString()
    {
        return IntroString;
    }
    public void Cool()
    {
        if(CurrentCool>0)CurrentCool--;
    }
    protected void initPriceString()
    {
        if(SkillLevel==3)PriceString="技能已升至满级";
        else PriceString="升级需要："+Price+"金币";
    }
    protected void initIntroString()
    {
        PriceString="你还没有英雄";
    }
}
