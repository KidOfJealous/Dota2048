package com.example.yuqiaohe.Dota2048.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.example.yuqiaohe.Dota2048.Data.Gold;
import com.example.yuqiaohe.Dota2048.Skills.RequiemOfSouls;
import com.example.yuqiaohe.Dota2048.Skills.Skill;
import com.example.yuqiaohe.Dota2048.Skills.SkillFactory;
import com.example.yuqiaohe.test.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hasee on 2018/3/13.
 * 游戏界面类
 */

public class GameView extends FrameLayout {

    private Card[][] cardsMap = new Card[4][4];
    private int[][]value;
    private GridLayout BackGround;
    private GridLayout FrontGround;
    private MainActivity parent;
    LayoutParams lp = new LayoutParams(-1,-1);
    public void set(MainActivity p,int[][] v,boolean[][] s) {
        parent = p;
        value = v;
        shines = s;
        for(int i=0;i<4;++i)
            for(int j =0;j<4;++j)
                cardsMap[i][j].setParent(p);
    }

    private boolean[][] shines;
    public GameView(Context context) {
        super(context);
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initGameView();
    }
    public void startAnimation(Animation[][] animations)
    {
        for(int i=0;i<4;++i)
            for(int j=0;j<4;++j)
            {
                cardsMap[i][j].startAnimation(animations[i][j]);
            }
    }
    public void StartAnimes(Animation[][] animes)
    {
        for(int x =0;x<4;++x)
            for(int y=0;y<4;++y)
                if(animes[x][y]!=null)
                    if(cardsMap[x][y].getNum()>0)cardsMap[x][y].startAnimation(animes[x][y]);
    }
    /**
     * 初始化界面
     */
    private void setDouble(int x,int y,int x1,int y1)
    {
        cardsMap[x][y].setNum(value[x][y]);
        cardsMap[x1][y1].setNum(value[x1][y1]);
    }
    private void initGameView(){
        //mySkill = new RequiemOfSouls(getContext());
        FrontGround = new GridLayout(getContext());
        BackGround = new GridLayout(getContext());
        BackGround.setColumnCount(4);
        FrontGround.setColumnCount(4);
        BackGround.setBackgroundColor(Color.BLACK);
        addCards(getCardWitch(),getCardWitch());
        FrontGround.setClipChildren(false);
        FrontGround.setClipToPadding(false);
        addView(BackGround);
        addView(FrontGround);
    }

    /**
     * 布局里面加入卡片
     * @param cardWidth
     * @param cardHeight
     */
    private void addCards(int cardWidth,int cardHeight){
        Card c;
        lp.setMargins(8,8,8,8);
        for(int y = 0;y< 4;y++){
            for(int x = 0;x < 4;x++){
                c = new Card(getContext());
                c.setNum(0);
                //Log.d("233","3");
                FrameLayout fr = new FrameLayout(getContext());
                ImageView im = new ImageView(getContext());
                im.setColorFilter(Color.parseColor("#11ffffff"));
                im.setImageResource(R.drawable.lg_0);
                fr.addView(im,lp);
                BackGround.addView(fr,cardWidth,cardHeight);
                FrontGround.addView(c,cardWidth,cardHeight);
                //Log.d("233","4");
                cardsMap[x][y] = c;
            }
        }
    }
    
    public void setAll()
    {
        for(int x =0;x<4;++x)
            for(int y =0;y<4;++y)
            {
                if(shines[x][y])cardsMap[x][y].Shine(value[x][y],R.drawable.im_re);
                else cardsMap[x][y].setNum(value[x][y]);
                cardsMap[x][y].Reset();
            }
    }
    public int getCardWitch(){
        //Log.d("233","5");
        DisplayMetrics displayMetrics;
        displayMetrics = getResources().getDisplayMetrics();
        int carWitch;
        carWitch = displayMetrics.widthPixels;

        return (carWitch-10)/4;
    }

    public void SetAll(int im)
    {
        for(int i=0;i<4;++i)
            for(int j=0;j<4;++j)
                if(shines[i][j])cardsMap[i][j].Shine(value[i][j],im);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 宽模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        // 宽大小
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        // 高大小
        int heightSize;
        // 只有宽的值是精确的才对高做精确的比例校对
        heightSize = widthSize;
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    public Point getBlock(float x, float y)
    {
        int w = getCardWitch();
        int xx = ((int)(x/w));
        int yy = ((int)(y/w));
        return new Point(xx,yy);
    }

}