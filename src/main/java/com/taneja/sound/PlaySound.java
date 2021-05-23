package com.taneja.sound;

import org.springframework.stereotype.Component;

import java.applet.Applet;
import java.applet.AudioClip;

@Component
public class PlaySound {

    public void playBeep() {

        AudioClip clip = Applet.newAudioClip(getClass().getResource("/../../../../../sound/beep.wav"));
        clip.play();

    }

    public void playBeep2() {

        AudioClip clip = Applet.newAudioClip(getClass().getResource("/../../../../../sound/beep2.wav"));
        clip.play();

    }

}
