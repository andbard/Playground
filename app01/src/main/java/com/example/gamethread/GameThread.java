package com.example.gamethread;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameThread extends Thread {

    private SurfaceHolder surfaceHolder;
    private GameViewSurface surface;
    private boolean running = false;
    private Context GameContext;
    private int gameState;
    public static final int STATE_PAUSE = 1;
    public static final int STATE_RUNNING = 2;

    private int GameSurfaceWidth = 1;
    private int GameSurfaceHeight = 1;

    public GameThread(SurfaceHolder holder, Context context, GameViewSurface GameSurface) {
        surfaceHolder = holder;
        surface = GameSurface;
        GameContext = context;
    }

    public void setRunning(boolean run) {
        running = run;
    }

    public void startGame() {
        synchronized (surfaceHolder) {
            setState(STATE_RUNNING);
        }
    }

    public void pause() {
        synchronized (surfaceHolder) {
            if (gameState == STATE_RUNNING) setState(STATE_PAUSE);
        }
    }

    public synchronized void restoreState(Bundle savedState) {
        synchronized (surfaceHolder) {
            setState(STATE_PAUSE);
        }
    }

    public void setState(int stateToSet) {
        synchronized (surfaceHolder) {
            // TODO Message Handling
        }
    }

    public Bundle saveState(Bundle map) {
        synchronized (surfaceHolder) {
            if (map != null) {

            }
        }
        return map;
    }

    public void setSurfaceSize(int width, int height) {
        // synchronized to make sure these all change atomically
        synchronized (surfaceHolder) {
            GameSurfaceWidth = width;
            GameSurfaceHeight = height;
        }
    }

    public void unpause() {
        setState(STATE_RUNNING);
    }

    @Override
    public void run() {
        while (running) {
            Canvas canvas=null;
            try {

                if(!surfaceHolder.getSurface().isValid())
                    continue;

                surface.update();

                canvas = surfaceHolder.lockCanvas(null);
                synchronized (surfaceHolder) {

                    surface.doDraw(canvas);
                }
            } catch(Exception p){

                Log.d("myApp", p.getMessage());

            }finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }catch (Exception e){}
                }
            }
        }
    }
}