package com.kulcloud.signage.tenant.ui.component.player;

/**
 * DevicePlayListener
 */
public interface DevicePlayListener {

    public void play();
    public void pause();
    public void stop();
    public void changeVolume(double volume);
    public void changeMute(boolean mute);

}