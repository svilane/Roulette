package com.playsafe.test;
class DrawBean{

    private int drawNumber;
    private long drawTime;
    private short winningNumber;

    public DrawBean(int drawNumber, long drawTime, short winningNumber) {
        this.drawNumber = drawNumber;
        this.drawTime = drawTime;
        this.winningNumber = winningNumber;
    }

    public int getDrawNumber() {
        return drawNumber;
    }

    public void setDrawNumber(int drawNumber) {
        this.drawNumber = drawNumber;
    }

    public long getDrawTime() {
        return drawTime;
    }

    public void setDrawTime(long drawTime) {
        this.drawTime = drawTime;
    }

    public short getWinningNumber() {
        return winningNumber;
    }

    public void setWinningNumber(short winningNumber) {
        this.winningNumber = winningNumber;
    }
}