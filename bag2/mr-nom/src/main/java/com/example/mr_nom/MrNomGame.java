package com.example.mr_nom;

import com.example.framework.Screen;
import com.example.framework.impl.AndroidGame;

public class MrNomGame extends AndroidGame {
    public Screen getStartScreen() {
        return new LoadingScreen(this); 
    }
} 
