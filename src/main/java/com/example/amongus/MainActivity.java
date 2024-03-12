package com.example.amongus;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    GameSurface gameSurface;
    public int count = 0;
    public MediaPlayer background;
    public SoundPool soundpool;
    public int killSound;
    public int soundP;
    public int time = 60;
    public int points = 0;
    public String t = "";
    volatile boolean running;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_main);
        gameSurface = new GameSurface(this);
        setContentView(gameSurface);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundpool = new SoundPool.Builder()
                    .setMaxStreams(6)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundpool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
        }

        killSound = soundpool.load(this, R.raw.killsound, 1);



        gameSurface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count == 0){
                    count = 1;
                } else if(count == 1){
                    count = 0;
                }
            }
        });

        background = MediaPlayer.create(MainActivity.this, R.raw.amongustheme);

        background.setLooping(true);
        background.start();




        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                t = ("0:"+checkDigit(time));
                time--;

            }

            public void onFinish() {
                t = ("Finished");
                running = false;

                setContentView(R.layout.activity_end);
                TextView scoree = findViewById(R.id.score);
                TextView timee = findViewById(R.id.time);

                scoree.setText("Score: "+points);
                timee.setText("Time: "+t);

                //Intents showing black screen so wasn't working

                //Intent intent = new Intent(MainActivity.this, EndActivity.class);
                //intent.putExtra("time", t);
                //intent.putExtra("score", points);
                //intent.addCategory(Intent.CATEGORY_HOME);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //startActivity(intent);
                //cancel();
                //finish();

            }

        }.start();



    }

    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }



    @Override
    protected void onPause(){
        super.onPause();
        gameSurface.pause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        gameSurface.resume();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "onTouchEvent");
        return super.onTouchEvent(event);
    }

    public class GameSurface extends SurfaceView implements Runnable, SensorEventListener{


        volatile float gyroScope;
        volatile boolean end;
        volatile float temp;



        Thread gameThread;
        SurfaceHolder holder;

        Bitmap crewmate, knife;
        int crewX = 0;
        int knifeY = 0;
        int knifeX = 0;
        int crewYY = 0;
        int crewXX = 0;

        int checker = 0;

        Paint paintProperty;

        int screenWidth;
        int screenHeight;

        double distanceVectorX = 0;
        double distanceVectorY = 0;
        double distance = 0;


        public GameSurface(Context context) {


            super(context);

            running = false;
            SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            Sensor gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            sensorManager.registerListener(this, gyroscope, sensorManager.SENSOR_DELAY_FASTEST);




            holder = getHolder();

            knife = BitmapFactory.decodeResource(getResources(), R.drawable.knife);
            crewmate = BitmapFactory.decodeResource(getResources(), R.drawable.crewmate);

            Display screenDisplay = getWindowManager().getDefaultDisplay();
            Point sizeOfScreen = new Point();
            screenDisplay.getSize(sizeOfScreen);

            screenWidth = sizeOfScreen.x;
            screenHeight = sizeOfScreen.y;

            paintProperty = new Paint();

            System.out.println(screenWidth);
            System.out.println(screenHeight);
            System.out.println(crewmate.getWidth());
            System.out.println(knife.getWidth());

        }




        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

        gyroScope = sensorEvent.values[1];
        temp += gyroScope;

            if(crewX > 500){
                crewX = 490;
            }

            if(crewX < -500){
                crewX = -490;
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }

        @Override
        public void run() {
            int flip = 1;
            while(running){
                if(holder.getSurface().isValid() == false){
                    continue;
                }
                Canvas canvas = holder.lockCanvas();

                //gameSurface.setBackground(getResources().getDrawable(R.drawable.background));
                canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.mainbackground), 0, 0, null);
                canvas.drawBitmap(crewmate, (screenWidth/2)-crewmate.getWidth()/2+crewX, (screenHeight/2)-crewmate.getHeight(), null);
                //canvas.drawText(String.valueOf(points), 800, 800, paintProperty);

                Paint paint = new Paint();
                paint.setColor(Color.WHITE);
                paint.setStyle(Paint.Style.FILL);
                paint.setTextSize(50);
                canvas.drawText("Score: " + String.valueOf(points), 100, 200, paint);

                canvas.drawText("Time is "+ t, 620, 200, paint);


                 //Normal Mode:
                if(count==0) {
                    canvas.drawBitmap(knife, knifeX, knifeY - 100, null);
                    if (knifeY > 1800) {
                        knifeY = -50;
                        knifeX = (int) (Math.random() * 1000);
                    }
                }

                //Impossible Mode:
                if(count > 0) {
                    knifeX = (int) (Math.random() * 1000);
                    canvas.drawBitmap(knife, knifeX, knifeY - 100, null);

                    if (knifeY > 1800) {
                        knifeY = -50;
                    }
                }

                crewYY = screenHeight/2;
                crewXX = (screenWidth/2)- crewmate.getWidth()/2+crewX;

                distanceVectorX = Math.abs(knifeX - crewXX);
                System.out.println("\n\n");
                System.out.println("knife x: "+ knifeX);
                System.out.println("Crew X: "+ crewXX);
                System.out.println("Vector x: "+distanceVectorX);
                distanceVectorY = Math.abs(knifeY - crewYY);
                System.out.println();
                System.out.println("Crew Y: "+crewYY);
                System.out.println("Knife Y: "+knifeY);
                System.out.println("Vector y: "+distanceVectorY);
                System.out.println();


                if(distanceVectorY < 50 && distanceVectorX < 50) {

                    crewmate = BitmapFactory.decodeResource(getResources(), R.drawable.crewmatedead);
                    checker = 1;
                    soundpool.play(killSound, 1, 1, 0, 0, 1);

                }


               System.out.println("Points: "+points);
               System.out.println("Checker: "+checker);


                if(knifeY > 1700) {
                    crewmate = BitmapFactory.decodeResource(getResources(), R.drawable.crewmate);
                    if(checker == 1){
                        points--;
                        checker = 0;

                    } else if(checker == 0){
                        points++;
                    }

                }



                crewX += temp;
                knifeY += 70;
                holder.unlockCanvasAndPost(canvas);
            }


        }



        public void resume(){
            running = true;
            gameThread = new Thread((this));
            gameThread.start();
        }
        public void pause(){
            running = false;
            while(true){
                try{
                    gameThread.join();
                }catch (InterruptedException e){

                }
            }
        }


    }



}