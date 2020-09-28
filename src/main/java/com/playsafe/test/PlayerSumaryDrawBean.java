package com.playsafe.test;

class PlayerSumaryDrawBean {
    private String name;
    private int  drawNumber;
    private String betOn;
    private float betWinnings;
    private float numberOfBetsInDraw;
    private boolean isStillPlaying;

    public PlayerSumaryDrawBean(String name, int drawNumber, String betOn, float betWinnings, float numberOfBetsInDraw, boolean isStillPlaying) {
        this.name = name;
        this.drawNumber = drawNumber;
        this.betOn = betOn;
        this.betWinnings = betWinnings;
        this.numberOfBetsInDraw = numberOfBetsInDraw;
        this.isStillPlaying = isStillPlaying;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDrawNumber() {
        return drawNumber;
    }

    public void setDrawNumber(int drawNumber) {
        this.drawNumber = drawNumber;
    }

    public String getBetOn() {
        return betOn;
    }

    public void setBetOn(String betOn) {
        this.betOn = betOn;
    }

    public float getBetWinnings() {
        return betWinnings;
    }

    public void setBetWinnings(float betWinnings) {
        this.betWinnings = betWinnings;
    }

    public float getNumberOfBetsInDraw() {
        return numberOfBetsInDraw;
    }

    public void setNumberOfBetsInDraw(float numberOfBetsInDraw) {
        this.numberOfBetsInDraw = numberOfBetsInDraw;
    }

    public boolean isStillPlaying() {
        return isStillPlaying;
    }

    public void setStillPlaying(boolean stillPlaying) {
        isStillPlaying = stillPlaying;
    }
}