package com.example.yuqiaohe.Dota2048.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import com.example.yuqiaohe.Dota2048.Data.BestScore;
import com.example.yuqiaohe.Dota2048.Data.Gold;
import com.example.yuqiaohe.Dota2048.Skills.Hero;
import com.example.yuqiaohe.Dota2048.Skills.RequiemOfSouls;
import com.example.yuqiaohe.Dota2048.Skills.SkillFactory;
import com.example.yuqiaohe.test.R;


public class MainActivity extends Activity {
    public TextView tvScore;//计分的
    public TextView tvBestScore;//最高分
    public TextView tvMaxLevel;
    public TextView tvLevel;
    public TextView tvIntro;
    public TextView tvPrice;
    public TextView tvGold;
    public Handler mainHandler;

    private String[] levels = {"先锋","卫士","中军","统帅","传奇","万古流芳","超凡入圣","冠绝一世"};
    private int score;
    private int bestScores;//历史最高成绩
    private RequiemOfSouls Re;

    private static MainActivity mainActivity = null;

    public MainActivity(){
        mainActivity = this;
        Log.d("mainActivity","created");
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }
    private void showDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("清除最高分记录");
        builder.setMessage("你确定要清除最高分吗");
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        bestScores = 0;
                        BestScore BS = new BestScore(getApplicationContext());
                        BS.setBestScode(0);
                        if (tvBestScore != null) tvBestScore.setText(bestScores);
                        if (tvMaxLevel != null) tvMaxLevel.setText(getLevel(bestScores));
                        dialogInterface.dismiss();
                    }
                });
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog dialog=builder.create();
        dialog.show();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = this;
        setContentView(R.layout.activity_main);
        Re=new RequiemOfSouls(this);
        setButtons();
        mainHandler=new Handler();
        BestScore BS = new BestScore(this);
        bestScores = BS.getBestScode();
        setTexts();
        loadScore();
        //Log.d("score",String.valueOf(score));
        System.out.print("Score+"+score);
        showScore();
    }

    public void showGold()
    {
        Gold g = new Gold(getApplicationContext());
        if(tvGold!=null)tvGold.setText(g.getGold()+"");
    }
    public int getScore(){return score;}

    public void clearScore(){
        score = 0;
        showScore();
    }
    public void showScore(){
        if(tvScore!=null)tvScore.setText(score+"");
        if(tvLevel!=null)tvLevel.setText(getLevel(score));
    }
    public void addScore(int s){
        score+=s;
        Log.d("hi",String.valueOf(bestScores));
        showScore();
        Log.d("score",String.valueOf(score));
        if (score > bestScores){
            Log.d("Highesr","best");
            bestScores = score;
            BestScore bs = new BestScore(this);
            bs.setBestScode(bestScores);
            if(tvBestScore!=null)tvBestScore.setText(bestScores);
            if(tvMaxLevel!=null)tvMaxLevel.setText(getLevel(score));
        }
    }
    public String getLevel(int s)
    {
        if(s<=500)return levels[0];
        if(s<=2000)return levels[1];
        if(s<=5000)return levels[2];
        if(s<=12000)return levels[3];
        if(s<=25000)return levels[4];
        if(s<=40000)return levels[5];
        if(s<=60000)return levels[6];
        return levels[7];
    }

    /**
     * 菜单、返回键响应
     */
    private long exitTime = 0;

    @SuppressLint("WrongConstant")
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(this, "再按一次返回键退出",1000).show();
                exitTime = System.currentTimeMillis();
            } else {
                StoreScore();
                GameView.getGameView().store();
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void loadScore(){
        //Log.d("233","load");
        SharedPreferences s = getApplicationContext().getSharedPreferences("Score",getApplicationContext().MODE_PRIVATE);
        score = s.getInt("currentScore",100);
       // Log.d("score",String.valueOf(score));
    }
    public void StoreScore()
    {
        SharedPreferences s = getApplicationContext().getSharedPreferences("Score",getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = s.edit();
        editor.putInt("currentScore",score);
        editor.apply();
    }
    private void setButtons()
    {
        Button bt = findViewById(R.id.bt_cx);
        Button cl = findViewById(R.id.clear);
        ImageButton us = findViewById(R.id.US);
        ImageView sk = findViewById(R.id.Skill);
        Button buy1 = findViewById(R.id.buy1);
        Button buy2 = findViewById(R.id.buy2);
        Button buy3 = findViewById(R.id.buy3);

        if(bt!=null)bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gold g = new Gold(getApplicationContext());
                if(score<750);
                else if(score<=3000)g.addGold(score/750);
                else if(score<=6000)g.addGold(score/750+5);
                else if(score<=12000)g.addGold(score/750+10);
                else if(score<=24000)g.addGold(score/750+15);
                else if(score<=40000)g.addGold(score/725+20);
                else if(score<=60000)g.addGold(score/725+25);
                else g.addGold(score/700+30);
                GameView.getGameView().Restart();
            }
        });
        if(cl!=null)cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        if(us!=null)us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gold g = new Gold(getApplicationContext());
                if(Re.getSkillLevel()<3&&g.buy(Re.getPrice()))Re.levelUp();
                showGold();
                if (tvPrice!=null)
                {
                    if(Re.getSkillLevel()!=3)tvPrice.setText("升级需要："+Re.getPrice()+"金币");
                    else tvPrice.setText("技能已升至满级");
                }
                if(tvIntro!=null)
                {
                    if(Re.getSkillLevel()==0)tvIntro.setText("你没有学习此技能");
                    else tvIntro.setText("当前等级为"+Re.getSkillLevel()+"，15%的概率获得"+String.format("%.1f",Re.getSkillLevel()*1.1+1.2)+"倍得分。");
                }
            }
        });
        if(sk!=null)
        {
            sk.setImageResource(Re.getImage());
            sk.setClickable(GameView.getGameView().mySkill.clickable);
            sk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GameView.getGameView().mySkill.ClickOn();
                }
            });
        }

        if(buy1!=null)
        {
            buy1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(new Gold(getBaseContext()).buy(Hero.getPrice(1))){Hero.bought(1);}
                    else Toast.makeText(MainActivity.this, "金钱不足",Toast.LENGTH_SHORT).show();
                }
            });
        }
        if(buy2!=null)
        {
            buy2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(new Gold(getBaseContext()).buy(Hero.getPrice(2))){Hero.bought(2);}
                    else Toast.makeText(MainActivity.this, "金钱不足",Toast.LENGTH_SHORT).show();
                }
            });
        }
        if(buy3!=null)
        {
            buy2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(new Gold(getBaseContext()).buy(Hero.getPrice(3))){Hero.bought(3);}
                    else Toast.makeText(MainActivity.this, "金钱不足",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void setTexts()
    {
        tvBestScore = findViewById(R.id.maxScore);
        tvScore = findViewById(R.id.tvScore);
        tvLevel = findViewById(R.id.level);
        tvMaxLevel = findViewById(R.id.maxlevel);
        tvIntro=findViewById(R.id.intro);
        tvPrice=findViewById(R.id.Price);
        tvGold=findViewById(R.id.Gold);
        if(tvBestScore!=null)tvBestScore.setText(bestScores+"");
        if(tvMaxLevel!=null)tvMaxLevel.setText(getLevel(bestScores));
        if(tvIntro!=null)
        {
            if(Re.getSkillLevel()==0)tvIntro.setText("你没有学习此技能");
            else tvIntro.setText("当前等级为"+Re.getSkillLevel()+"，15%的概率获得"+String.format("%.1f", Re.getSkillLevel()*1.1+1.2)+"倍得分。");
        }
        if (tvPrice!=null)
        {
            if(Re.getSkillLevel()!=3)tvPrice.setText("升级需要："+Re.getPrice()+"金币");
            else tvPrice.setText("技能已升至满级");
        }
        if(tvGold!=null)
        {
            Gold g = new Gold(getApplicationContext());
            tvGold.setText(g.getGold()+"");
        }
    }

}