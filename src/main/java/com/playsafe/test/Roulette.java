package com.playsafe.test;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

public class Roulette {

    private static List<DrawBean>               drawBeansList = new ArrayList<DrawBean>();
    private static List<PlayerSumaryDrawBean>   playerSumaryBeanList = new ArrayList<PlayerSumaryDrawBean>();
    private static List<PlayerDetailBetsBean>   playerDetailBetsBeanList = new ArrayList<PlayerDetailBetsBean>();
    private static short currentDrawWinningNumber = 0;
    private static int round = 0;
    private String inputFile;
    private static int highestNumber;
    private static int maximumBet;
    private static int drawFrequency;
    private static int winByEvenOrOdd;
    private static int winByNumber;

    public static void main(String[] args) throws InterruptedException, IOException {

        Roulette roulette = new Roulette();        
        roulette.getPropValues();
        playerSumaryBeanList = roulette.readInputFile();

        Thread thread = new Thread(new DrawThread());
        thread.start();

        Scanner input = new Scanner(System.in);
        boolean stopGameNow = true;

        do {
            round++;
            for (PlayerSumaryDrawBean player : playerSumaryBeanList) {
                int  noOfBetsThisRound =0;
                if (player.isStillPlaying()) {
                    boolean validInput = true;
                    System.out.println("Round " + round +", your turn " + player.getName().toUpperCase() +", GoodLuck..., please make one or more bets or enter 'q' to cash-out");
                    System.out.print(player.getName() + "  ");

                    String betLine = input.nextLine();
                    betLine = betLine.trim().replaceAll(" +", " ");
                    String[] betLineTokens = betLine.split(" ");

                    List<String> errorList = validateInput(betLineTokens);
                    while  (errorList.size() > 0){
                        System.out.println(errorList.get(0));
                        System.out.println("Please try again " + player.getName().toUpperCase() +", GoodLuck..., Round " + round + ", Please make one or more bets or enter 'q' to cash-out");
                        System.out.print(player.getName() + "  ");

                        betLine = input.nextLine();
                        betLine = betLine.trim().replaceAll(" +", " ");
                        betLineTokens = betLine.split(" ");
                        if (betLineTokens.length==1 && betLineTokens[0].equalsIgnoreCase("q")) {
                            player.setStillPlaying(false);
                            break;
                        }
                        errorList.clear();
                        errorList = validateInput(betLineTokens);
                    }
                    if (betLineTokens.length== 1 && betLineTokens[0].equalsIgnoreCase("q")){
                        player.setStillPlaying(false);
                    }

                    if(player.isStillPlaying()&& betLineTokens.length > 1) {
                        processBets(player.getName(), round,  betLineTokens);
                        noOfBetsThisRound = betLineTokens.length / 2;
                    }
                }
                if(player.isStillPlaying() ) {
                   System.out.println(player.getName().toUpperCase() + ", you have " + noOfBetsThisRound + ", bets in this round. (Round " + round + ") ");
                }else{
                   System.out.println(player.getName().toUpperCase() + ", you have  quit playing in this round (Round " + round + ")");
                }
                System.out.println( "  ");
            }
            stopGameNow = stopGame();
            if (!stopGameNow) {
                System.out.println("All bets are in, now waiting for a draw.." );
                System.out.println("Round " + round + ", draw ..." );
                int winningNumber = getWiningNumber();
                System.out.println(winningNumber);
                processWinnings(winningNumber, round);
                System.out.println("    " );
                System.out.println("    " );
            }else{
                System.out.println("ALl players have quit, the game is now ending..., thanks for Playing");
            }
            stopGameNow = stopGame();
        }  while (!stopGameNow);

        thread.stop();
    }

    public static void processBets(String playerName, int drawNumber, String[] userInputArray){

        int betNumber = 1;
        for (int i = 0; i < userInputArray.length; i += 2) {

            float betsAgainstBet =0;
            try {
                betsAgainstBet = Float.parseFloat(userInputArray[i + 1]);
            }catch(NumberFormatException e){
                System.out.println("Invalid Input "+ e.getMessage() );
            }

            PlayerDetailBetsBean playerDetailBetsBean = new PlayerDetailBetsBean();
            playerDetailBetsBean.setPlayerName(playerName);
            playerDetailBetsBean.setDrawNumber(drawNumber);
            playerDetailBetsBean.setBetNumber(betNumber);
            playerDetailBetsBean.setBet(userInputArray[i]);
            playerDetailBetsBean.setNumberofBets(betsAgainstBet);
            playerDetailBetsBean.setWinningsforBet(0);

            playerDetailBetsBeanList.add(playerDetailBetsBean);

            betNumber++;
          }
    }

    public static void processWinnings(int winningNumber, int drawNumber) throws FileNotFoundException {

        System.out.println("" );
        System.out.println("" );
        System.out.println("Number " + drawNumber  );
        System.out.println("Player                   Bet                  Outcome          Winnings ");
        System.out.println("-----");

        for (PlayerDetailBetsBean playerDetailBetsBean : playerDetailBetsBeanList ) {
            if (playerDetailBetsBean.getDrawNumber() == drawNumber) {
                String outcome= "";

                float amountWon = getAmountWon(playerDetailBetsBean.getBet(), playerDetailBetsBean.getNumberofBets(),  winningNumber );
                BigDecimal winningAmountFormated = new BigDecimal(amountWon);
                winningAmountFormated =winningAmountFormated.setScale(1, BigDecimal.ROUND_HALF_EVEN);

                if (amountWon> 0) {
                    outcome=  "WIN";
                }else{
                    outcome=  "LOSE";
                }

                String playername = padRight(playerDetailBetsBean.getPlayerName(),25 );
                String betOn = padRight(playerDetailBetsBean.getBet(),22 );
                String outcomePadded = padRight(outcome,20 );

                System.out.print(playername);
                System.out.print(betOn);
                System.out.print(outcomePadded);
                System.out.println(winningAmountFormated);

                playerDetailBetsBean.setWinningsforBet(amountWon);
            }
        }
        updateTextFile();
    }

    public static float getAmountWon(String bet, float betsAgainstBet, int winningNumber){
        float amountWon = 0;
        boolean evenDraw = true;

        if (winningNumber % 2 == 0){
            evenDraw = true;
        }else{
            evenDraw = false;
        }

        if (!isNumeric(bet.trim())) {
            if (bet.trim().equalsIgnoreCase("even")) {
                if (evenDraw) {
                    amountWon = betsAgainstBet * winByEvenOrOdd;
                }
            }
            if (bet.trim().equalsIgnoreCase("odd")) {
                if (!evenDraw) {
                    amountWon = betsAgainstBet * winByEvenOrOdd;
                }
            }
        }else{
            int betOnNumber = Integer.parseInt(bet.trim());
            if (betOnNumber == winningNumber){
                amountWon = betsAgainstBet  * winByNumber;
            }
        }
        return amountWon;
    }

    public static void updateTextFile() throws FileNotFoundException {
        String oldFileName = "Players.txt";
        String tmpFileName = "tmp_Players.txt";

        BufferedReader br = null ;
        BufferedWriter bw = null;
        try {
            br = new BufferedReader(new FileReader(oldFileName));
            bw = new BufferedWriter(new FileWriter(tmpFileName));
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim().replaceAll(" +", " ");
                String[] stringTokens = line.split(",");
                String playerNameFromFile   = stringTokens[0].trim();

                float totalWinsFromFile     = Float.parseFloat(stringTokens[1].trim());
                float totalBetsFromFile     = Float.parseFloat(stringTokens[2].trim());

                for(int i= 0; i< playerDetailBetsBeanList.size() ; i++){
                    if (playerNameFromFile.equalsIgnoreCase(playerDetailBetsBeanList.get(i).getPlayerName().trim())){

                        totalWinsFromFile =  totalWinsFromFile +  playerDetailBetsBeanList.get(i).getWinningsforBet();
                        totalBetsFromFile =  totalBetsFromFile + playerDetailBetsBeanList.get(i).getNumberofBets();

                        line = line.replace(stringTokens[1], ""+totalWinsFromFile);
                        line = line.replace(stringTokens[2], ""+totalBetsFromFile);
                    }
                }

                bw.write(line+"\n");
            }
        } catch (Exception e) {
            return;
        } finally {
            try {
                if(br != null)
                    br.close();
            } catch (IOException e) {
                //
            }
            try {
                if(bw != null)
                    bw.close();
            } catch (IOException e) {
                //
            }
        }
        // Once everything is complete, delete old file..
        File oldFile = new File(oldFileName);
        oldFile.delete();

        // And rename tmp file's name to old file name
        File newFile = new File(tmpFileName);
        newFile.renameTo(oldFile);

    }
    public static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    public static boolean stopGame(){
        boolean stopGame = true;
        for (PlayerSumaryDrawBean playerSumaryBean : playerSumaryBeanList) {
            if (playerSumaryBean.isStillPlaying()) {
                stopGame = false;
                break;
            }
        }
        return stopGame;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private static int getWiningNumber () throws InterruptedException {

        System.out.print("Drawing the Winnning Number for round " + round + " .." );

            int size = drawBeansList.size();
            while (drawBeansList.size() == size) {
               Thread.sleep(300);
               System.out.print("." );
           }
        System.out.print(" " );
        return currentDrawWinningNumber;
    }

   

    private static List<String> validateInput (String[] userInputArray) {
        List<String> errorList = new ArrayList<String>();
        if (userInputArray.length==1 && userInputArray[0].equalsIgnoreCase("q")) {
            return errorList;
        }

        if (userInputArray.length==1 && !userInputArray[0].equalsIgnoreCase("q")) {
            errorList.add("*** Input not valid ***, Enter 'q' to quit ");
            return errorList;
        }

        if (userInputArray.length < 2) {
            errorList.add("*** Input not valid ***, Please enter one or more bets in pairs of (bet and an amount) and should be separated by a space in one line " + userInputArray.length);
            return errorList;
        }

        if (userInputArray.length % 2 != 0) {
            errorList.add("*** Input not valid ***, multiple bets and an amounts should be in pairs, you have " + userInputArray.length + " tokens in your input ");
            return errorList;
        }

        int betnunberInRound = 1;
        for (int i = 0; i < userInputArray.length; i += 2) {
            if (isNumeric(userInputArray[i].trim())) {
                int betNumber = Integer.parseInt(userInputArray[i]);
                if (betNumber < 1) {
                    errorList.add("*** Input not valid ***, bets should between 1 and 36, you have " + userInputArray[i].trim()+ " On bet "+betnunberInRound);
                }
                if (betNumber > maximumBet) {
                    errorList.add("*** Input not valid ***, bets should between 1 and 36, you have " + userInputArray[i].trim()+ " On bet "+ betnunberInRound);
                }
            } else if (!(userInputArray[i].trim().equalsIgnoreCase("even") || userInputArray[i].trim().equalsIgnoreCase("odd"))) {
                return errorList;
            }
            if (isNumeric(userInputArray[i + 1].trim())) {
                float betAmount = Float.parseFloat(userInputArray[i + 1]);
                if (betAmount < 1) {
                    errorList.add("*** Input not valid ***, A bet multiple should not be less than 1, you have " + userInputArray[i + 1].trim());
                    return errorList;
                }
                if (betAmount > maximumBet) {
                    errorList.add("*** Input not valid ***, A bet multiple should not be more than 100, you have " + userInputArray[i + 1].trim());
                    return errorList;
                }
            }
            betnunberInRound++;
       }
       return errorList;
   }

    public List<PlayerSumaryDrawBean> readInputFile () {
        
        List<PlayerSumaryDrawBean> playerBetsBeanList = new ArrayList<PlayerSumaryDrawBean>();

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(inputFile.trim()));
            String line = reader.readLine();
            while (line != null) {
                String[] values = line.split(",");
                String name = values[0];
                float totalValueWins = Float.parseFloat(values[1]);
                float noOfGames = Float.parseFloat(values[2]);
                playerBetsBeanList.add(new PlayerSumaryDrawBean(name, -1, "-1", totalValueWins, noOfGames,true));
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Technical error, System can not read players file " + e.getMessage());
        }
        return playerBetsBeanList;
    }

   public void getPropValues() throws IOException {

        Properties prop = new Properties();
        String propFileName = "application.properties";
        InputStream  inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

        try {
            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            inputFile       = prop.getProperty("inputfile");
            drawFrequency   = Integer.parseInt(prop.getProperty("draw.frequency"));
            highestNumber   = Integer.parseInt(prop.getProperty("bet.highest.number"));
            maximumBet      = Integer.parseInt(prop.getProperty("bet.maximum"));
            winByEvenOrOdd  = Integer.parseInt(prop.getProperty("win.even.or.odd"));
            winByNumber     = Integer.parseInt(prop.getProperty("win.number"));

        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        } finally {
            inputStream.close();
        }
    }


   static class DrawThread extends Thread{
        Random random = new Random();
        static final int low = 1;
        static final int high = highestNumber;
        static int drawNumber = 0;
        public void run(){
            while(true){
                try{
                    Thread.sleep(drawFrequency * 1000);
                    currentDrawWinningNumber = (short) (random.nextInt(high-low) + low);
                    long lastDrawtime = System.currentTimeMillis(); // for Audit purposes
                    drawNumber++;
                    drawBeansList.add(new DrawBean(drawNumber,lastDrawtime,currentDrawWinningNumber));
                }catch(InterruptedException e) {
                    System.out.println(e);
                }
            }
        }
    }
}