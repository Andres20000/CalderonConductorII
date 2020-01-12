package calderonconductor.tactoapps.com.calderonconductor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import calderonconductor.tactoapps.com.calderonconductor.Adapter.TabsPagerAdapter;


public class PresentacionUnicaVez extends FragmentActivity
{

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    TextView skip;
    Button bt1,bt2,bt3,bt4,bt5;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_presentacion_unica_vez);

        if (savedInstanceState != null){
            Intent i = new Intent(getApplicationContext(), Splash.class);
            startActivity(i);
            finish();
            return;
        }
        bt1=(Button) findViewById(R.id.btn1);
        bt2=(Button) findViewById(R.id.btn2);
        bt3=(Button) findViewById(R.id.btn3);
        bt4=(Button) findViewById(R.id.btn4);
        bt5=(Button) findViewById(R.id.btn5);

        viewPager = (ViewPager) findViewById(R.id.pager);

        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {

                btnAction(position);

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    private void btnAction(int action){
        switch(action){
            case 0:
                setButton(bt1,4,15,R.drawable.circulo_gris_claro_post_border_style);
                setButton(bt2,4,15,R.drawable.circulo_blanco_style);
                setButton(bt3,4,15,R.drawable.circulo_gris_claro_post_border_style);
                setButton(bt4,4,15,R.drawable.circulo_gris_claro_post_border_style);
                setButton(bt5,4,15,R.drawable.circulo_gris_claro_post_border_style);

                break;

            case 1:

                setButton(bt1,4,15,R.drawable.circulo_blanco_style);
                setButton(bt2,4,15,R.drawable.circulo_gris_claro_post_border_style);
                setButton(bt3,4,15,R.drawable.circulo_gris_claro_post_border_style);
                setButton(bt4,4,15,R.drawable.circulo_gris_claro_post_border_style);
                setButton(bt5,4,15,R.drawable.circulo_gris_claro_post_border_style);


                break;

            case 2:
                setButton(bt1,4,15,R.drawable.circulo_gris_claro_post_border_style);
                setButton(bt2,4,15,R.drawable.circulo_gris_claro_post_border_style);
                setButton(bt3,4,15,R.drawable.circulo_blanco_style);
                setButton(bt4,4,15,R.drawable.circulo_gris_claro_post_border_style);
                setButton(bt5,4,15,R.drawable.circulo_gris_claro_post_border_style);

                break;

            case 3:
                setButton(bt1,4,15,R.drawable.circulo_gris_claro_post_border_style);
                setButton(bt2,4,15,R.drawable.circulo_gris_claro_post_border_style);
                setButton(bt3,4,15,R.drawable.circulo_gris_claro_post_border_style);
                setButton(bt4,4,15,R.drawable.circulo_blanco_style);
                setButton(bt5,4,15,R.drawable.circulo_gris_claro_post_border_style);

                break;

            case 4:
                setButton(bt1,4,15,R.drawable.circulo_gris_claro_post_border_style);
                setButton(bt2,4,15,R.drawable.circulo_gris_claro_post_border_style);
                setButton(bt3,4,15,R.drawable.circulo_gris_claro_post_border_style);
                setButton(bt4,4,15,R.drawable.circulo_gris_claro_post_border_style);
                setButton(bt5,4,15,R.drawable.circulo_blanco_style);

                break;
        }
    }
    private void setButton(Button btn,int w,int h,int c){

        btn.setWidth(w);
        btn.setHeight(h);
        btn.setBackgroundResource(c);
    }


    //bhtn_atras_hadware
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == event.KEYCODE_BACK) {
            Log.v("atras","atras");
            //atras2();
        }
        return false;
    }


}