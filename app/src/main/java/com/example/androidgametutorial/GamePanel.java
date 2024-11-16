package com.example.androidgametutorial;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    private Paint redPaint = new Paint();
    private SurfaceHolder holder;
//    private float x, y;
    private ArrayList<RndSquare> squares = new ArrayList<>();
    private Random rand = new Random();
    private GameLoop gameLoop;

    public GamePanel(Context context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
        redPaint.setColor(Color.RED);
        gameLoop = new GameLoop(this);
    }

    public void render(){
        Canvas c = holder.lockCanvas();
        c.drawColor(Color.BLACK);

        synchronized (squares) {
            for (RndSquare square : squares)
                square.draw(c);
        }

        holder.unlockCanvasAndPost(c);
    }
//    double delta
    public void update(double delta){

        synchronized (squares){
            for(RndSquare square: squares)
                square.move(delta);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

//        x = event.getX();
//        y = event.getY();
        if(event.getAction() == MotionEvent.ACTION_DOWN) {

            PointF pos = new PointF(event.getX(), event.getY());
            int color = Color.rgb(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
            int size = 25 + rand.nextInt(101);

            synchronized (squares){
                squares.add(new RndSquare(pos, color, size));
            }
//            render();
//            update();
        }
        return true;
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        gameLoop.setGameLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    private class RndSquare{
        private PointF pos;
        private int size;
        private Paint paint;
        private int xDir = 1, yDir = 1;
        public RndSquare(PointF pos, int color, int size){
            this.pos = pos;
            this.size = size;
            paint = new Paint();
            paint.setColor(color);
        }

        //1080 * 1920
        public void move(double delta){
            pos.x += xDir * delta * 6;
            if(pos.x >= 960 || pos.x <= 0)
                xDir *= -1;

            pos.y += yDir * delta * 6;
            if(pos.y >= 2240 || pos.y <= 0)
                yDir *= -1;
        }


        public void draw(Canvas c){
            c.drawRect(pos.x, pos.y, pos.x+size, pos.y+size, paint);
        }
    }
}
