package com.example.yuqiaohe.Dota2048.Skills;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;
import com.example.yuqiaohe.Dota2048.Activity.MainActivity;
import com.example.yuqiaohe.test.R;


public class TimeLapse extends Skill {
    private int[][][] values = new int[5][5][4];
    private int step;
    public TimeLapse(Context context,MainActivity ma)
    {
        super(context,"TimeLapse",ma);
        SkillId=2;
        step=0;
        CoolDown=70-10*SkillLevel;
        image=R.drawable.skill_2;
        ReadValues();
        Refrash();
    }
    @Override
    public void Cool() {
        super.Cool();
        Log.d("cool",String.valueOf(CurrentCool));
        //偷懒的方法，在冷却为5时计入。
        if (step==5) {
            for(int i=0;i<4;++i)
                values[i]=values[i+1];
            values[4]=parent.Export();
        }
        if (step < 5) {
            step++;
            if(step>2)values[step-1]=parent.Export();
            //write("step", step);
        }
        WriteValues();
    }
    private void WriteValues()
    {
        SharedPreferences.Editor e = s.edit();
        for(int i =0;i<5;++i)
            for(int j=0;j<4;++j)
                e.putInt("value"+i+j,values[0][i][j]);
        e.putInt("CurrentCool",CurrentCool);
        e.apply();
    }
    @Override
    public  String getSkillName(){return "时光倒流";}
    private void ReadValues()
    {
        for(int i=0;i<5;++i)
            for(int j=0;j<4;++j)
                values[0][i][j]=s.getInt("value"+i+j,0);
        CurrentCool=s.getInt("CurrentCool",CoolDown);
    }
    @Override
    public void ClickOn(){
        if(!clickable)return;
        if(CurrentCool==0){
            CurrentCool=CoolDown;
            parent.Load(values[0]);
            WriteValues();
        }
        else{
            Toast.makeText(parent, "技能还在冷却，剩余"+CurrentCool+"次移动",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void init()
    {
        step=0;
        values[0]=parent.Export();
        CurrentCool=0;
    }
    @Override
    protected void Refrash()
    {
        initPriceString();
        initIntroString();
        clickable=SkillLevel!=0;
    }
    @Override
    protected void initIntroString()
    {
        if(SkillLevel==0)IntroString ="你没有学习此技能";
        else IntroString="使你回到5步前的状态。当前等级为"+SkillLevel+"，冷却时间为"+CoolDown+"步。";
    }
}
