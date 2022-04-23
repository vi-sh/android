package test.example.com.verifiersvalidator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by admin on 12/11/16.
 **/

public class SplashScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreenmaker);
        final ImageView Logo = (ImageView) findViewById(R.id.splash);
        final Animation rotate = AnimationUtils.loadAnimation(getBaseContext(),R.anim.rotate);
        final Animation antirotate = AnimationUtils.loadAnimation(getBaseContext(),R.anim.anitrotate);
        final Animation zoominimg = AnimationUtils.loadAnimation(getBaseContext(),R.anim.zoomin);
        final Animation zoomoutimg = AnimationUtils.loadAnimation(getBaseContext(),R.anim.zoomout);
        final Animation righttoleft = AnimationUtils.loadAnimation(getBaseContext(),R.anim.righttoleft);
        final Animation lefttoright = AnimationUtils.loadAnimation(getBaseContext(),R.anim.lefttoright);
        final Animation videozoomin = AnimationUtils.loadAnimation(getBaseContext(),R.anim.zoominvideo);
        final Animation videozoomout = AnimationUtils.loadAnimation(getBaseContext(),R.anim.zoomoutvideo);


        //final Animation animation_5 = AnimationUtils.loadAnimation(getBaseContext(),R.anim.righttoleft);
        //final Animation fadeout = AnimationUtils.loadAnimation(getBaseContext(),R.anim.fadeout);


        //video animation feel starts
        /*imgvideo.startAnimation(videozoomout);
        videozoomout.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {

            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                imgvideo.startAnimation(videozoomin);
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {
            }
        });


        videozoomin.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {

            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                imgvideo.startAnimation(videozoomout);

            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });


        righttoleft.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {

            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                imgvideo.startAnimation(lefttoright);
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });

        lefttoright.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {

            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                imgvideo.startAnimation(videozoomout);
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {
            }
        });*/


        //Logo animation starts
        //rotate
        Logo.startAnimation(rotate);
        rotate.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {

            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                Logo.startAnimation(antirotate);
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });

        //antirotate
        antirotate.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {

            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                Logo.startAnimation(zoomoutimg);
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });



        //zoom out
        zoomoutimg.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {

            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                Logo.startAnimation(zoominimg);

            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }

        });

        //zoomin
        zoominimg.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {

            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                finish();
                Intent i = new Intent(getBaseContext(),LoginActivity.class);
                startActivity(i);
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }

        }
        );
    }
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreenActivity.this);
        builder.setTitle("Exit");
        builder.setMessage("Do You Want to Quit ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        finish();
                        System.exit(0);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Intent i=new Intent(getApplicationContext(),SplashScreenActivity.class);
                        finish();
                        startActivity(i);
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}