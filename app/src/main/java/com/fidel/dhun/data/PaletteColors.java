package com.fidel.dhun.data;

import android.support.v7.graphics.Palette;

/**
 * Created by fidel on 11/20/17.
 */

public class PaletteColors {
   private Palette.Swatch Vibrant;
    private Palette.Swatch DarkVibrant;
    private Palette.Swatch LightVibrant;
    private Palette.Swatch Muted;
    private Palette.Swatch DarkMuted;
    private Palette.Swatch LightMuted;

    public PaletteColors(Palette.Swatch vibrant, Palette.Swatch darkVibrant, Palette.Swatch lightVibrant, Palette.Swatch muted, Palette.Swatch darkMuted, Palette.Swatch lightMuted) {
        Vibrant = vibrant;
        DarkVibrant = darkVibrant;
        LightVibrant = lightVibrant;
        Muted = muted;
        DarkMuted = darkMuted;
        LightMuted = lightMuted;
    }

    public Palette.Swatch getVibrant() {
        return Vibrant;
    }

    public void setVibrant(Palette.Swatch vibrant) {
        Vibrant = vibrant;
    }

    public Palette.Swatch getDarkVibrant() {
        return DarkVibrant;
    }

    public void setDarkVibrant(Palette.Swatch darkVibrant) {
        DarkVibrant = darkVibrant;
    }

    public Palette.Swatch getLightVibrant() {
        return LightVibrant;
    }

    public void setLightVibrant(Palette.Swatch lightVibrant) {
        LightVibrant = lightVibrant;
    }

    public Palette.Swatch getMuted() {
        return Muted;
    }

    public void setMuted(Palette.Swatch muted) {
        Muted = muted;
    }

    public Palette.Swatch getDarkMuted() {
        return DarkMuted;
    }

    public void setDarkMuted(Palette.Swatch darkMuted) {
        DarkMuted = darkMuted;
    }

    public Palette.Swatch getLightMuted() {
        return LightMuted;
    }

    public void setLightMuted(Palette.Swatch lightMuted) {
        LightMuted = lightMuted;
    }
}
