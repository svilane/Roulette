# Roulette

Roulette is a Java app 

## Installation

Use the package manager in your IDE and create a runnable jar from the files below.
 
```bash
Open these files in an IDE
1 DrawBean.java
2 PlayerDetailBetsBean.java
3 PlayerSumaryDrawBean.java
4 Roulette.java - with the Main method
And Run the "Roulette.java"
```

## Usage

```Java
Edit The Application.properties according to your specification
Application.properties

inputfile=C:\\Dev\\Projects\\Test\\Roulette\\src\\main\\resources\\Players.txt
bet.highest.number=36
bet.maximum=100
win.even.or.odd=2
win.number=36
draw.frequency=30
```

### inputfile
This is a directory to place the input file


### bet.highest.number
This is the highest number that can be drawn in a draw
This is very helpful when testing - if you limit this number to say 3 the lickylywould to draw a number is good 

### bet.maximum
This is the highest bet allowed per draw

### win.even.or.odd
This variable specifies the multiples of bet a player wins if he chose an even or odd type of number and that type of number wins

### win.number
This variable specifies the multiples of bet a player wins if he chose the winning number

### draw.frequency
This variable specifies the the time in seconds between draws/ every x seconds the system makes a draw


## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

