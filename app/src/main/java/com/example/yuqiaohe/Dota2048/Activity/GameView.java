package com.example.yuqiaohe.Dota2048.Activity;

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
    //我们需要定义一个二维数组来记录GameView初始化时生成的16个卡片类
    private Card[][] cardsMap = new Card[4][4];
    private Card[][] backs = new Card[4][4];
    public Skill mySkill;
    private GridLayout BackGround;
    private GridLayout FrontGround;
    LayoutParams lp = new LayoutParams(-1,-1);  //创建个布局，填充满整个父局容器
    private static GameView gameView = null;

    public static GameView getGameView() {
        return gameView;
    }

    private List<Point> points = new ArrayList<Point>();


    private Animation[][] animes = new TranslateAnimation[4][4];
    private boolean[][] shines = new boolean[4][4];

    private int[][] value = new int[4][4];
    private boolean move = true;
    public GameView(Context context) {
        super(context);
        initGameView();
        gameView = this;
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGameView();
        gameView = this;
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initGameView();
        gameView = this;
    }
    private void Clear()
    {
        for(int i=0;i<4;++i)for(int j=0;j<4;++j)shines[i][j]=false;
        animes=new Animation[4][4];
    }
    private void setAnimes(final int x,final int y,final int x1,final int y1)
    {
        int w = getCardWitch();
        TranslateAnimation animation = new TranslateAnimation(0,(x1-x)*w,0, (y1-y)*w);
        animation.setDuration(120);
        animation.setFillAfter(true);
        synchronized (this)
        {
            if (move) {
                move = false;
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        setAll();
                        addRandomNum();
                        check();
                        move = true;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }
        }
        //as.addAnimation(sa);
        //as.setFillAfter(true);
        animes[x][y]=animation;
    }
    private void StartAnimes()
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
        mySkill = new RequiemOfSouls(getContext());
        FrontGround = new GridLayout(getContext());
        BackGround = new GridLayout(getContext());
        BackGround.setColumnCount(4);
        FrontGround.setColumnCount(4);
        BackGround.setBackgroundColor(Color.BLACK);
        addCards(getCardWitch(),getCardWitch());
        FrontGround.setClipChildren(false);
        FrontGround.setClipToPadding(false);
        startGame();
        setOnTouchListener(new OnTouchListener() {
            private float startX,startY;//初始的位置
            private float offsetX,offsetY; //偏移的值

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(!move)return true;
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        startX  = motionEvent.getX();
                        startY = motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        offsetX = motionEvent.getX()-startX;
                        offsetY = motionEvent.getY()-startY;
                        Clear();
                        if(Math.abs(offsetX)>Math.abs(offsetY)) {//这个是防止斜着化
                            if (offsetX < -80) {
                                Log.d("move","left");
                                swipeLeft();

                            } else if (offsetX > 80) {
                                Log.d("move","right");
                                swipeRight();
                            }
                        }else {
                            if (offsetY < -80){
                                Log.d("move","up");
                                swipeUp();
                            }else if (offsetY>80){
                                Log.d("move", "down ");
                                swipeDown();
                            }
                        }
                        break;
                }
                MainActivity.getMainActivity().StoreScore();
                return true;
            }
        });
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

    /**
     * 获取屏幕的宽度
     * @return
     */
    private void setAll()
    {
        for(int x =0;x<4;++x)
            for(int y =0;y<4;++y)
            {
                if(shines[x][y])cardsMap[x][y].Shine(value[x][y],R.drawable.im_re);
                else cardsMap[x][y].setNum(value[x][y]);
                cardsMap[x][y].Reset();
            }
    }
    private int getCardWitch(){
        //Log.d("233","5");
        DisplayMetrics displayMetrics;
        displayMetrics = getResources().getDisplayMetrics();

        int carWitch;
        carWitch = displayMetrics.widthPixels;

        return (carWitch-10)/4;
    }
    public void store()
    {
        SharedPreferences s = getContext().getSharedPreferences("currentPosition",getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = s.edit();
        for(int i= 0;i<4;++i)
            for(int j=0;j<4;++j)
            {
                editor.putInt("currentBlock"+i+":"+j,value[i][j]);
            }
        editor.apply();
    }
    public void load()
    {
        SharedPreferences s = getContext().getSharedPreferences("currentPosition",getContext().MODE_PRIVATE);
        for(int i= 0;i<4;++i)
            for(int j=0;j<4;++j)
            {
                value[i][j]=s.getInt("currentBlock"+i+":"+j,0);
            }
        mySkill = SkillFactory.CreateSkill(getContext(),s.getInt("SkillUsing",1));
        check();
    }

    public void Restart()
    {
        move = true;
        for (int y = 0;y<4;y++){
            for (int x = 0;x < 4;x++) {
                value[x][y]=0;
                cardsMap[x][y].setNum(value[x][y]=0);
            }
        }
        MainActivity.getMainActivity().clearScore();
        MainActivity.getMainActivity().showGold();
        addRandomNum();
        addRandomNum();
        setAll();
        store();
        MainActivity.getMainActivity().StoreScore();
    }
    public void startGame(){
        load();
        int s = 0;
        for (int y = 0;y<4;y++){
            for (int x = 0;x < 4;x++) {
                if(value[x][y]>0)s++;
            }
        }
        if(s<2)Restart();
        store();
        setAll();
    }

    private void addRandomNum(){
        //把这个point清空，每次调用添加随机数时就清空之前所控制的指针
        points.clear();

        //对所有的位置进行遍历：即为每个卡片加上了可以控制的指针
        for(int y = 0;y<4;y++){
            for (int x=0; x<4;x++) {
                //Log.d("value"+x+"-"+y,String.valueOf(value[x][y]));
                if(value[x][y]<=0){
                    points.add(new Point(x,y));//给List存放控制卡片用的指针（通过坐标轴来控制）
                }
            }
        }
        Point p = points.remove((int)(Math.random()*points.size()));
        cardsMap[p.x][p.y].setNum(value[p.x][p.y]=(Math.random()>0.1?2:4));
        store();
        //通过point对象来充当下标的角色来控制存放card的二维数组cardsMap，然后随机给定位到的card对象赋值
    }

    private void swipeLeft(){
        boolean merge = false;//控制每滑动一次画面就加一个随机数到画面，也就是在下面所有for循环之后
//      Toast.makeText(getContext(), "向左滑动了", 0).show();
        //以下两行for循环实现了一行一行的遍历，在向左滑动的时候
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {

                for (int x1 = x+1; x1 < 4; x1++) {
                    //animes[x1][y].setDuration(20);
                    //这是在水平上固定了一个格子之后再继续在水平上遍历别的格子，且当格子有数的时候进行以下的操作
                    if (value[x1][y]>0) {
                        //现在判断被固定的格子有没有数字，如果没有数字就进行以下的操作
                        if (value[x][y]<=0) {
                            //把与被固定的格子的同一水平上的格子的数字赋给被固定的格子
                            value[x][y]=value[x1][y];
                            //cardsMap[x1][y].moveLine(cardsMap[x1][y].getX(),cardsMap[x][y].getX(),cardsMap[x1][y].getY(),cardsMap[x][y].getY());
                            //把值赋给被固定的格子后自己归零
                            value[x1][y]=0;
                            setAnimes(x1,y,x,y);
                            //第二层循环，即同一层的不同列退一格继续循环，这样做的原因是为了继续固定这个格子而去检查与它同一水平上的别的格子的内容，因为其他格子是什么个情况还需要继续在第二层进行判断
                            x--;
                            //只要有移动就要加随机数
                            merge = true;
                            //animes[x1][y]=new TranslateAnimation(cardsMap[x1][y].getXp(),cardsMap[x1][y].getYp(),cardsMap[x][y].getXp(),cardsMap[x][y].getYp());
                            //animes[x1][y].setDuration(100);

                        }else if (value[x][y]==value[x1][y]) {//这层判断是判断相邻两个数相同的情况
                            value[x][y]*=2;
                            value[x1][y]=0;
                            Double m = mySkill.getMultiple();
                            MainActivity.getMainActivity().addScore((int)(value[x][y]*m));
                            setAnimes(x1,y,x,y);
                            shines[x][y]=m>2;
                            merge = true;
                        }
                        break;
                    }
                }
            }
        }
        if (merge) {
            mySkill.Cool();
            StartAnimes();
        }
    }
    private void swipeRight(){
        boolean merge = false;//控制每滑动一次画面就加一个随机数到画面，也就是在下面所有for循环之后
//      Toast.makeText(getContext(), "向右滑动了", 0).show();
        for (int y = 0; y < 4; y++) {
            for (int x = 4-1; x >=0; x--) {

                for (int x1 = x-1; x1 >=0; x1--) {
                    if (value[x1][y]>0) {

                        if (value[x][y]<=0) {
                            value[x][y]=value[x1][y];
                            value[x1][y]=0;
                            setAnimes(x1,y,x,y);
                            x++;
                            merge = true;
                        }else if (value[x][y]==value[x1][y]) {
                            value[x][y]*=2;
                            value[x1][y]=0;
                            Double m = mySkill.getMultiple();
                            MainActivity.getMainActivity().addScore((int)(value[x][y]*m));
                            setAnimes(x1,y,x,y);
                            shines[x][y]=m>2;
                            merge = true;
                        }
                        break;

                    }
                }
            }
        }
        if (merge) {
            mySkill.Cool();
            StartAnimes();
        }
    }
    private void swipeUp(){
        boolean merge = false;//控制每滑动一次画面就加一个随机数到画面，也就是在下面所有for循环之后
//      Toast.makeText(getContext(), "向上滑动了", 0).show();
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {

                for (int y1 = y+1; y1 < 4; y1++) {
                    if (value[x][y1]>0) {

                        if (value[x][y]<=0) {
                            value[x][y]=value[x][y1];
                            value[x][y1]=0;
                            setAnimes(x,y1,x,y);
                            y--;
                            //只要有移动就要加随机数
                            merge = true;
                        }else if (value[x][y]==value[x][y1]) {
                            value[x][y]*=2;
                            value[x][y1]=0;
                            Double m = mySkill.getMultiple();
                            MainActivity.getMainActivity().addScore((int)(value[x][y]*m));
                            setAnimes(x,y1,x,y);
                            shines[x][y]=m>2;
                            merge = true;
                        }
                        break;
                    }
                }
            }
        }
        if (merge) {
            mySkill.Cool();
            StartAnimes();
        }
    }
    private void swipeDown(){
        boolean merge = false;//控制每滑动一次画面就加一个随机数到画面，也就是在下面所有for循环之后
        //Toast.makeText(getContext(), "向下滑动了", 0).show();
        for (int x = 0; x < 4; x++) {
            for (int y = 4-1; y >=0; y--) {

                for (int y1 = y-1; y1 >=0; y1--) {
                    if (value[x][y1]>0) {

                        if (value[x][y]<=0) {
                            value[x][y]=value[x][y1];
                            value[x][y1]=0;
                            setAnimes(x,y1,x,y);

                            y++;
                            //只要有移动就要加随机数
                            merge = true;
                        }else if (value[x][y]==value[x][y1]) {
                            value[x][y]*=2;
                            value[x][y1]=0;
                            Double m = mySkill.getMultiple();
                            MainActivity.getMainActivity().addScore((int)(value[x][y]*m));
                            merge = true;
                            setAnimes(x,y1,x,y);
                            shines[x][y]=m>2;
                        }
                        break;

                    }
                }
            }
        }
        if (merge) {
            mySkill.Cool();
            StartAnimes();
        }
    }
    public int[][] Export()
    {
        int [][] values = new int[4][4];
        for(int i=0;i<4;++i)
            for(int j=0;j<4;++j)
                values[i][j]=value[i][j];
        return values;
    }
    public void Load(int [][] values)
    {
        for(int i=0;i<4;++i)
            for(int j=0;j<4;++j)
                value[i][j]=values[i][j];
        SetAll();
    }
    public void SetAll()
    {
        for(int i=0;i<4;++i)
            for(int j=0;j<4;++j)
                cardsMap[i][j].Shine(value[i][j],R.drawable.im_re);
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

    /**
     * 判断游戏结束的
     * 界面格子全满了且相邻的格子没有相同的数字
     */
    private void check(){
        boolean complete = true;
        for(int y = 0;y <4;y++){
            for(int x = 0;x<4;x++){
                if (value[x][y]<=0
                        ||(x>0&&value[x][y]==value[x-1][y])||
                        (x<3&&value[x][y]==(value[x+1][y]))||
                        (y>0&&value[x][y]==value[x][y-1])||
                        (y<3&&value[x][y]==value[x][y+1])) {
                    return;
                }
            }
        }
        if (complete){
            Gold g = new Gold(getContext());
            int score = MainActivity.getMainActivity().getScore();
            if(score<750);
            else if(score<=3000)g.addGold(score/750);
            else if(score<=6000)g.addGold(score/750+5);
            else if(score<=12000)g.addGold(score/750+10);
            else if(score<=24000)g.addGold(score/750+15);
            else if(score<=40000)g.addGold(score/725+20);
            else if(score<=60000)g.addGold(score/725+25);
            else g.addGold(score/700+30);
            new AlertDialog.Builder(getContext()).setTitle("比赛结束").setMessage("你失败了").setPositiveButton("重新匹配", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Restart();
                }
            }).show();
        }
    }
}