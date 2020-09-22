package com.example.gamethread;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameViewSurface extends SurfaceView implements SurfaceHolder.Callback {

    private GameThread gameThread;

    public GameViewSurface(Context context) {
        super(context);

        getHolder().addCallback(this);
        gameThread = new GameThread(getHolder(), context, this);

        setFocusable(true);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (!hasWindowFocus) gameThread.pause();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // gameThread.setSurfaceSize(width, height);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        gameThread.setRunning(true);
        gameThread.start();
        createGameHandler();
    }

    public void createGameHandler()
    {
//        gameData = new Game(backColor);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        gameThread.setRunning(false);
        while (retry) {
            try {
                gameThread.join();
                retry = false;
            } catch (InterruptedException e) {}
        }
    }


    //@Override
    public void doDraw(Canvas canvas) {
//        canvas.drawBitmap(gameDataGen.board, mPosX, mPosY, null);
    }

    public void playSound() {
//        if (enableSound && !sp.isPlaying())
//            sp.start();
    }

    public void update()
    {
        try {
//            if (waitTillLoad || isExit || finished)
//                return;
//            if (gameData != null) {
//                gameData.update();
//
//            }
        } catch (Exception ex) {}
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
//        viewHeight = h;
//        viewWidth = w;
    }




    @Override
    public boolean onTouchEvent(MotionEvent event) {

//        if(waitTillLoad ||finished || !isTouchable || bypassParent)
//            return super.onTouchEvent(event);
//
//        scaleDetector.onTouchEvent(event);
        boolean handled = false;
        int xTouch;
        int yTouch;
        int actionIndex = event.getActionIndex();

        // get touch event coordinates and make transparent circle from it
        switch (event.getActionMasked()) {

            case MotionEvent.ACTION_DOWN:
                Log.d("myApp", "ACTION_DOWN start");
                xTouch = (int) event.getX(0);
                yTouch = (int) event.getY(0);

//                if (scaleDetector.isInProgress()) {
//                    break;
//                }
//
//                mLastTouchX = xTouch;
//                mLastTouchY = yTouch;
//
//                this.selectedIndex = findClusterFromXY(xTouch, yTouch);
//                if (selectedIndex >= 0) {
//
//                    Log.d("myApp", "ACTION_DOWN selectedIndex >");
//
//                    gameData.setTopIndex(this.selectedIndex);
//                    gameData.clearMotionData();
//                    gameData.addMotionEvent(new Point(xTouch, yTouch));
//
//                }
//                handled = true;
                Log.d("myApp", "ACTION_DOWN end");
                break;


            case MotionEvent.ACTION_MOVE:

                xTouch = (int) event.getX();
                yTouch = (int) event.getY();
//                gameData.moveStart = true;
//                gameData.moveEnd = false;

//                gameData.setMovingClusterData(gameData.currentCluster);
//                gameData.addMotionEvent(new Point(xTouch, yTouch));
//                _previousMouseX = xTouch;
//                _previousMouseY = yTouch;

                Log.d("myApp", "ACTION_MOVE end");
//                gameData.moveEnd = true;
                break;

            case MotionEvent.ACTION_UP:
                Log.d("myApp", "ACTION_UP start");
//                handled = processActionUp(event, true);
                handled = true;
                Log.d("myApp", "ACTION_UP end");
                break;

        }

        return super.onTouchEvent(event);
    }


    protected void onComplete() {
//        if(solveFragment != null)
//        {
//            finished= true;
//            solveFragment.onComplete();
//        }
    }

    public GameThread getThread() {
        return gameThread;
    }


}