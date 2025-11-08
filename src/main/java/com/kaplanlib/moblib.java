package com.kaplanlib;

import net.fabricmc.api.ModInitializer;

import com.kaplanlib.api.scheduler.ServerScheduler;


public class moblib implements ModInitializer {

    @Override
    public void onInitialize() {
        ServerScheduler.init();
    }
}
