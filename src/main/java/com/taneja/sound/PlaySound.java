package com.taneja.sound;

import org.springframework.stereotype.Component;

import java.applet.Applet;
import java.applet.AudioClip;
import java.nio.file.Paths;

@Component
public class PlaySound {

    public void playBeep() {

        AudioClip clip = Applet.newAudioClip(getClass().getClassLoader().getResource("beep.wav"));
        clip.play();

    }

    public void playBeep2() {

        AudioClip clip = Applet.newAudioClip(getClass().getClassLoader().getResource("beep2.wav"));
        clip.play();

    }

}
