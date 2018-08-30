package com.Dota2048.Activity;

import android.content.SharedPreferences;
import android.graphics.Point;
import android.util.Log;

import com.Dota2048.Data.BestScore;
import com.Dota2048.Data.Gold;
import com.Dota2048.Skills.Skill;
import com.Dota2048.Skills.SkillFactory;
import com.Game.yuqiaohe.test.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

public class LogicModule {
    /*int num;
    MainActivity parent;
    private int bestScores;
    private int[][] value=new int[4][4];
    private boolean[][] shines = new boolean[4][4];
    private boolean battling;
    private boolean move;
    private boolean merge;
    private Skill mySkill;
    private int score;
    private String[] levels = {"先锋", "卫士", "中军", "统帅", "传奇", "万古流芳", "超凡入圣", "冠绝一世"};
    private List<Point> points = new ArrayList<>();

    public void check(){}
    public int DestroyBlock(int x, int y, int damage) {
        //Log.v("lagunaBlade",x+":"+y);
        if (x >= 4 || y >= 4) return -1;
        int s = value[x][y];
        if (s > damage) {
            //showTextToast("方块过大，无法摧毁");
            return -1;
        }
        if (s == 0) {
            //showTextToast("这是一个空格子");
            return -2;
        }
        int p = s;
        int ss = 0;
        while ((p >>= 1) > 1) {
            ss += s;
        }
        num--;
        value[x][y] = 0;
        addScore(ss);
        //showTextToast("摧毁成功，获得" + ss + "分");
        for (int i = 0; i < 4; ++i)
            for (int j = 0; j < 4; ++j)
                shines[i][j] = false;
        shines[x][y] = true;
        check();
        return ss;
    }
    public int ExchangeBlock(Point p1, Point p2, int distance) {
        int x1 = p1.x;
        int x2 = p2.x;
        int y1 = p1.y;
        int y2 = p2.y;
        int v1 = value[x1][y1];
        int v2 = value[x2][y2];
        if (v1 <= 0 || v2 <= 0) {
            //showTextToast("不能和空格交换");
            return 1;
        }
        if (x1 == x2 && y1 == y2) {
            //showTextToast("不能和自己交换");
            return 2;
        }
        if (Math.abs(x1 - x2) + Math.abs(y1 - y2) > distance) {
            //showTextToast("超出距离限制");
            return 3;
        }
        value[x1][y1] = v2;
        value[x2][y2] = v1;
        for (int i = 0; i < 4; ++i)
            for (int j = 0; j < 4; ++j)
                shines[i][j] = false;
        shines[x1][y1] = true;
        shines[x2][y2] = true;
        return 0;
    }
    public void Restart() {
        num = 0;
        battling=false;
        move = true;
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                value[x][y] = 0;
                shines[x][y] = false;
            }
        }
        clearScore();
        addRandomNum();
        addRandomNum();
        mySkill.init();
        store();
    }
    public void ReBegin() {
        load();
        calculateGold();
        Restart();
        store();
    }

    public int[][] Export() {
        int[][] values = new int[5][4];
        for (int i = 0; i < 4; ++i)
            for (int j = 0; j < 4; ++j)
                values[i][j] = value[i][j];
        values[4][0] = bestScores;
        values[4][1] = score;
        return values;
    }

    public void Load(int[][] values) {
        num=0;
        for (int i = 0; i < 4; ++i)
            for (int j = 0; j < 4; ++j) {
                Log.d(values[i][j] + "", value[i][j] + "");
                shines[i][j] = (values[i][j] > 0 || value[i][j] > 0);
                value[i][j] = values[i][j];
                if(value[i][j]>0)num++;
            }

        bestScores = values[4][0];
        score = values[4][1];
        store();
        check();
    }

    private void addRandomNum() {
        points.clear();
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if (value[x][y] <= 0) {
                    points.add(new Point(x, y));//给List存放控制卡片用的指针（通过坐标轴来控制）
                }
            }
        }
        Point p = points.remove((int) (Math.random() * points.size()));
        value[p.x][p.y] = (Math.random() > 0.1 ? 2 : 4);
        num++;
        store();
    }

    private boolean swipeLeft() {
        merge=false;
//      Toast.makeText(getContext(), "向左滑动了", 0).show();
        //以下两行for循环实现了一行一行的遍历，在向左滑动的时候
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {

                for (int x1 = x + 1; x1 < 4; x1++) {
                    //animes[x1][y].setDuration(20);
                    //这是在水平上固定了一个格子之后再继续在水平上遍历别的格子，且当格子有数的时候进行以下的操作
                    if (value[x1][y] > 0) {
                        //现在判断被固定的格子有没有数字，如果没有数字就进行以下的操作
                        if (value[x][y] <= 0) {
                            //把与被固定的格子的同一水平上的格子的数字赋给被固定的格子
                            value[x][y] = value[x1][y];
                            //cardsMap[x1][y].moveLine(cardsMap[x1][y].getX(),cardsMap[x][y].getX(),cardsMap[x1][y].getY(),cardsMap[x][y].getY());
                            //把值赋给被固定的格子后自己归零
                            value[x1][y] = 0;
                            parent.setAnimes(x1, y, x, y);
                            //第二层循环，即同一层的不同列退一格继续循环，这样做的原因是为了继续固定这个格子而去检查与它同一水平上的别的格子的内容，因为其他格子是什么个情况还需要继续在第二层进行判断
                            x--;
                            //只要有移动就要加随机数
                            merge = true;
                            //animes[x1][y]=new TranslateAnimation(cardsMap[x1][y].getXp(),cardsMap[x1][y].getYp(),cardsMap[x][y].getXp(),cardsMap[x][y].getYp());
                            //animes[x1][y].setDuration(100);

                        } else if (value[x][y] == value[x1][y]) {//这层判断是判断相邻两个数相同的情况
                            value[x][y] *= 2;
                            value[x1][y] = 0;
                            double m = mySkill.getMultiple();
                            addScore((int) (value[x][y] * m));
                            parent.setAnimes(x1, y, x, y);
                            shines[x][y] = m > 2;
                            num--;
                            merge = true;
                        }
                        break;
                    }
                }
            }
        }
        return merge;
    }

    private boolean swipeUp() {
        merge=false;
//      Toast.makeText(getContext(), "向上滑动了", 0).show();
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {

                for (int y1 = y + 1; y1 < 4; y1++) {
                    if (value[x][y1] > 0) {

                        if (value[x][y] <= 0) {
                            value[x][y] = value[x][y1];
                            value[x][y1] = 0;
                            parent.setAnimes(x, y1, x, y);
                            y--;
                            //只要有移动就要加随机数
                            merge = true;
                        } else if (value[x][y] == value[x][y1]) {
                            value[x][y] *= 2;
                            value[x][y1] = 0;
                            double m = mySkill.getMultiple();
                            addScore((int) (value[x][y] * m));
                            parent.setAnimes(x, y1, x, y);
                            shines[x][y] = m > 2;
                            merge = true;
                            num--;
                        }
                        break;
                    }
                }
            }
        }
        return merge;
    }

    private boolean swipeDown() {
        merge=false;
        //Toast.makeText(getContext(), "向下滑动了", 0).show();
        for (int x = 0; x < 4; x++) {
            for (int y = 4 - 1; y >= 0; y--) {

                for (int y1 = y - 1; y1 >= 0; y1--) {
                    if (value[x][y1] > 0) {

                        if (value[x][y] <= 0) {
                            value[x][y] = value[x][y1];
                            value[x][y1] = 0;
                            parent.setAnimes(x, y1, x, y);

                            y++;
                            //只要有移动就要加随机数
                            merge = true;
                        } else if (value[x][y] == value[x][y1]) {
                            value[x][y] *= 2;
                            value[x][y1] = 0;
                            double m = mySkill.getMultiple();
                            addScore((int) (value[x][y] * m));
                            merge = true;
                            parent.setAnimes(x, y1, x, y);
                            shines[x][y] = m > 2;
                            num--;
                        }
                        break;

                    }
                }
            }
        }
        return merge;
    }

    private boolean swipeRight() {
        merge=false;
//      Toast.makeText(getContext(), "向右滑动了", 0).show();
        for (int y = 0; y < 4; y++) {
            for (int x = 4 - 1; x >= 0; x--) {

                for (int x1 = x - 1; x1 >= 0; x1--) {
                    if (value[x1][y] > 0) {

                        if (value[x][y] <= 0) {
                            value[x][y] = value[x1][y];
                            value[x1][y] = 0;
                            parent.setAnimes(x1, y, x, y);
                            x++;
                            merge = true;
                        } else if (value[x][y] == value[x1][y]) {
                            value[x][y] *= 2;
                            value[x1][y] = 0;
                            double m = mySkill.getMultiple();
                            addScore((int) (value[x][y] * m));
                            parent.setAnimes(x1, y, x, y);
                            shines[x][y] = m > 2;
                            merge = true;
                            num--;
                        }
                        break;

                    }
                }
            }
        }
        return merge;
    }

    private void calculateGold() {
        Gold g = new Gold(parent);
        if (score <= 3000) g.addGold(score / 750);
        else if (score <= 6000) g.addGold(score / 750 + 5);
        else if (score <= 12000) g.addGold(score / 750 + 10);
        else if (score <= 24000) g.addGold(score / 750 + 15);
        else if (score <= 40000) g.addGold(score / 725 + 20);
        else if (score <= 60000) g.addGold(score / 725 + 25);
        else g.addGold(score / 700 + 30);
    }

    public void Cool() {
        mySkill.Cool();
    }

    public void store() {
        SharedPreferences s = parent.getSharedPreferences("currentPosition", parent.MODE_PRIVATE);
        SharedPreferences.Editor editor = s.edit();
        for (int i = 0; i < 4; ++i)
            for (int j = 0; j < 4; ++j) {
                editor.putInt("currentBlock" + i + ":" + j, value[i][j]);
            }
        editor.apply();
        s = parent.getSharedPreferences("Score", parent.MODE_PRIVATE);
        editor = s.edit();
        editor.putInt("currentScore", score);
        editor.apply();
        new BestScore(parent).setBestScode(bestScores);
    }

    public void load() {
        SharedPreferences s = this.getSharedPreferences("currentPosition", this.MODE_PRIVATE);
        for (int i = 0; i < 4; ++i)
            for (int j = 0; j < 4; ++j) {
                value[i][j] = s.getInt("currentBlock" + i + ":" + j, 0);
            }
        mySkill = SkillFactory.CreateSkill(this, s.getInt("SkillUsing", 0), this);
        s = this.getSharedPreferences("Score", this.MODE_PRIVATE);
        score = s.getInt("currentScore", 100);
    }

    public void startGame() {
        load();
        num = 0;
        battling=false;
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                if (value[i][j] > 0) num++;
        if (num == 0) Restart();
        check();
    }

    public int getScore() {
        return score;
    }

    public void clearScore() {
        score = 0;
    }

    public void addScore(int s) {
        score += s;
        Log.d("hi", String.valueOf(bestScores));
        showScore();
        Log.d("score", String.valueOf(score));
        if (score > bestScores) {
            Log.d("Highesr", "best");
            bestScores = score;
            BestScore bs = new BestScore(parent);
            bs.setBestScode(bestScores);
        }
    }

    public String getLevel(int s) {
        if (s <= 500) return levels[0];
        if (s <= 2000) return levels[1];
        if (s <= 5000) return levels[2];
        if (s <= 12000) return levels[3];
        if (s <= 25000) return levels[4];
        if (s <= 40000) return levels[5];
        if (s <= 60000) return levels[6];
        return levels[7];
    }
    private void clearHigh()
    {
        bestScores = 0;
        new BestScore(parent).setBestScode(0);
    }*/
}
