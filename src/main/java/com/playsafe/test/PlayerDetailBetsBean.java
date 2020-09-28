package com.playsafe.test;

class PlayerDetailBetsBean {
    private String  playerName;
    private int     drawNumber;
    private int     betNumber;
    private String  bet;
    private float   numberofBets;
    private float   winningsforBet;
    private boolean isStillPlaying;

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getDrawNumber() {
        return drawNumber;
    }

    public void setDrawNumber(int drawNumber) {
        this.drawNumber = drawNumber;
    }

    public int getBetNumber() {
        return betNumber;
    }

    public void setBetNumber(int betNumber) {
        this.betNumber = betNumber;
    }

    public String getBet() {
        return bet;
    }

    public void setBet(String bet) {
        this.bet = bet;
    }

    public float getNumberofBets() {
        return numberofBets;
    }

    public void setNumberofBets(float numberofBets) {
        this.numberofBets = numberofBets;
    }

    public float getWinningsforBet() {
        return winningsforBet;
    }

    public void setWinningsforBet(float winningsforBet) {
        this.winningsforBet = winningsforBet;
    }

    public boolean isStillPlaying() {
        return isStillPlaying;
    }

    public void setStillPlaying(boolean stillPlaying) {
        isStillPlaying = stillPlaying;
    }
}