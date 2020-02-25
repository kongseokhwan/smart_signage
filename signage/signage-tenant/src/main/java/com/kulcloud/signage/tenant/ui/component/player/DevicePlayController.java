package com.kulcloud.signage.tenant.ui.component.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.kulcloud.signage.commons.enums.PlayerState;
import com.kulcloud.signage.commons.utils.CommonUtils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;

/**
 * DevicePlayController
 */
public class DevicePlayController extends HorizontalLayout{

    private static final long serialVersionUID = 1L;

    private List<DevicePlayListener> listeners = Collections.synchronizedList(new ArrayList<>(1));

    private Icon playIcon;
    private Icon pauseIcon;
    private Button playButton;
    private Button stopButton;
    private Icon volumeOffIcon;
    private Icon volumeOnIcon;
    private Button volumeOffButton;
    private NumberField volumeField;

    private boolean play;
    private boolean stop;
    private boolean mute;

    public DevicePlayController() {
        super();
        setMargin(false);
        setPadding(true);
        setSpacing(true);
        setWidthFull();
        setHeight("50px");
        setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        playIcon = VaadinIcon.PLAY.create();
        pauseIcon = VaadinIcon.PAUSE.create();
        playButton = new Button();
        playButton.addClickListener(e -> setPlay(!this.play, true));

        stopButton = new Button(VaadinIcon.STOP.create());
        stopButton.addClickListener(e -> setStop(true));

        HorizontalLayout playLayout = new HorizontalLayout(playButton, stopButton);
        expand(playLayout);

        volumeOffIcon = VaadinIcon.VOLUME_OFF.create();
        volumeOnIcon = VaadinIcon.VOLUME.create();
        volumeOffButton = new Button();
        volumeOffButton.addClickListener(e -> setMute(!this.mute, true));

        volumeField = new NumberField();
        volumeField.setHasControls(true);
        volumeField.setMin(0);
        volumeField.setMax(1);
        volumeField.setStep(0.1);
        volumeField.addValueChangeListener(e -> {
        	if(e.isFromClient()) {
        		fireVolume(volumeField.getValue());
        	}
        });
        HorizontalLayout volumeLayout = new HorizontalLayout(volumeOffButton, volumeField);

        disableController();
        add(playLayout, volumeLayout);
    }

    public void setData(Map<String, Object> data) {
        if(data != null && data.size() > 0) {
            String playValue = CommonUtils.convertToString(data.get("play"));
            PlayerState play;
            if (playValue == null) {
                play = PlayerState.IDLE;
            } else {
                play = PlayerState.valueOf(playValue);
            }

            switch (play) {
            case IDLE:
                this.play = false;
                this.stop = true;
                playButton.setEnabled(false);
                playButton.setIcon(playIcon);
                stopButton.setEnabled(false);
                break;
            case PLAYING:
            case BUFFERING:
                this.play = true;
                this.stop = false;
                playButton.setEnabled(true);
                playButton.setIcon(pauseIcon);
                stopButton.setEnabled(true);
                break;
            case PAUSED:
                this.play = false;
                this.stop = false;
                playButton.setEnabled(true);
                playButton.setIcon(playIcon);
                stopButton.setEnabled(true);
                break;
            }

            if (data.get("mute") != null) {
                Boolean mute = CommonUtils.convertToBoolean(data.get("mute"));
                setMute(mute);
            } else {
                setMute(false);
            }

            if (data.get("volume") != null) {
                Double volume = CommonUtils.convertToDouble(data.get("volume"));
                setVolume(volume);
            } else {
                setVolume(0);
            }
        } else {
            disableController();
        }
    }

    private void disableController() {
        this.play = false;
        this.stop = false;
        this.mute = false;
        this.playButton.setIcon(playIcon);
        this.volumeOffButton.setIcon(volumeOnIcon);
        this.playButton.setEnabled(false);
        this.stopButton.setEnabled(false);
        this.volumeOffButton.setEnabled(false);
        this.volumeField.setEnabled(false);
    }

    public void setPlay(boolean play) {
        setPlay(play, false);
    }

    public void setPlay(boolean play, boolean fire) {
        this.play = play;
        playButton.setIcon(play ? pauseIcon : playIcon);
        stopButton.setEnabled(true);

        if (fire) {
            if (play) {
                for (DevicePlayListener listener : listeners) {
                    listener.play();
                }
            } else {
                for (DevicePlayListener listener : listeners) {
                    listener.pause();
                }
            }
        }
    }

    public void setStop(boolean fire) {
        this.play = false;
        this.stop = true;
        playButton.setIcon(playIcon);
        playButton.setEnabled(false);
        stopButton.setEnabled(false);

        if (fire) {
            for (DevicePlayListener listener : listeners) {
                listener.stop();
            }
        }
    }

    public void setVolume(double volume) {
        setVolume(volume, false);
    }

    public void setVolume(double volume, boolean fire) {
        if (!fire && volume >= volumeField.getMin() && volume <= volumeField.getMax()) {
            volumeField.setValue(volume);
        }

        if (fire) {
            fireVolume(volume);
        }
    }

    private void fireVolume(double volume) {
        for (DevicePlayListener listener : listeners) {
            listener.changeVolume(volume);
        }
    }

    public void setMute(boolean mute) {
        setMute(mute, false);
    }

    public void setMute(boolean mute, boolean fire) {
        this.mute = mute;
        
        volumeField.setEnabled(!mute);
        volumeOffButton.setEnabled(true);
        volumeOffButton.setIcon(mute ? volumeOnIcon : volumeOffIcon);
        if(fire) {
            for (DevicePlayListener listener : listeners) {
                listener.changeMute(mute);
            }
        }
    }

    public void addDevicePlayListener(DevicePlayListener listener) {
        if(!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeDevicePlayListener(DevicePlayListener listener) {
        listeners.remove(listener);
    }
}