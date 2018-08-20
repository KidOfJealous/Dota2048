package com.example.yuqiaohe.Dota2048.Skills;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.yuqiaohe.Dota2048.Activity.GameView;
import com.example.yuqiaohe.Dota2048.Activity.MainActivity;


public class TimeLapse extends Skill {
    private int[][] values = new int[4][4];
    private int step;
    public TimeLapse(Context context)
    {
        super(context,"TimeLapse");
        SkillId=2;
        step=s.getInt("step",0);
    }
    @Override
    public void Cool() {
        super.Cool();
        if (step == 5) {
            values = GameView.getGameView().Export();
            WriteValues();
        }
        if (step < 5) {
            step++;
            write("step", step);
        }
    }
    private void WriteValues()
    {
        SharedPreferences.Editor e = s.edit();
        for(int i =0;i<4;++i)
            for(int j=0;j<4;++j)
                e.putInt("value"+i+j,values[i][j]);
    }

    @Override
    public void ClickOn(){
        if(CurrentCool==0){
            CurrentCool=CoolDown;
            GameView.getGameView().Load(values);
        }
        else{
            Toast.makeText(MainActivity.getMainActivity(), "技能还在冷却，剩余"+CoolDown+"次移动",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void init()
    {
        values=GameView.getGameView().Export();
    }
}
