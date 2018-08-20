package com.example.yuqiaohe.Dota2048.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import com.example.yuqiaohe.Dota2048.Data.BestScore;
import com.example.yuqiaohe.Dota2048.Skills.RequiemOfSouls;
import com.example.yuqiaohe.test.R;

public class MenuActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        setButtons();
        BestScore BS = new BestScore(this);
    }
    private void setButtons()
    {
        Button ld = findViewById(R.id.LoadGame);
        Button rs = findViewById(R.id.StartGame);
        Button hs = findViewById(R.id.HeroStore);
        if(hs!=null)
        {
            hs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setContentView(R.layout.activity_store);
                }
            });
        }
        if(ld!=null)
        {
            ld.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setContentView(R.layout.activity_main);
                }
            });
        }
        if(rs!=null)
        {
            rs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GameView.getGameView().Restart();
                    Intent intent= new Intent(getBaseContext(),MainActivity.class);
                    //new MainActivity();
                    startActivity(intent);
                }
            });
        }
    }
}
