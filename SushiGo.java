import java.util.Scanner;
public class SushiGo {
    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);

        //Create our deck for the game
        Deck tableDeck = new Deck();
        
        System.out.println(startGame());

        //Run the game loop
        boolean playAgain = true;
        while (playAgain) {
            playGame(reader, tableDeck);
            playAgain = askPlayAgain(reader);
        }
    }

    public static void playGame(Scanner reader, Deck tableDeck) {
        Player[] players = createPlayers(reader);
        tableDeck.shuffle();
        tableDeck.dealCards(players);
        runGameLoop(players, reader);
    }

    public static boolean askPlayAgain(Scanner reader) {
        System.out.println("Game Over, Want to play again? (Y/N)");
        String response = reader.nextLine();
        if (!response.equalsIgnoreCase("Y")) {
            System.out.println("Goodbye!");
            return false;
        }
        return true;
    }

    public static Player[] createPlayers(Scanner reader){
        int numOfPlayers = getNumOfPlayers(reader);
        return buildPlayers(numOfPlayers);
    }
    
    public static int getNumOfPlayers(Scanner reader) {
        int numOfPlayers = 0;
        boolean validInput = false;
        do {
            System.out.println("How many players? (Minimum 2, Maximum 5) ");
            try {
                numOfPlayers = Integer.parseInt(reader.nextLine());
                if(numOfPlayers >= 2 && numOfPlayers <= 5) {
                    validInput = true;
                } else {
                    System.out.println("\u001B[31mPlease enter a number between 2 and 5.\u001B[0m");
                }
            } catch (NumberFormatException e) {
                System.out.println("\u001B[31mINVALID INPUT. PLEASE ENTER AN INTEGER\u001B[0m");
            }
        } while (!validInput);
        return numOfPlayers;
    }
    
    public static Player[] buildPlayers(int numOfPlayers) {
        Player[] players = new Player[numOfPlayers];
        for(int i = 0; i < numOfPlayers; i++){
            players[i] = new Player(i, numOfPlayers);
        }
        return players;
    }

    public static void runGameLoop(Player[] players, Scanner reader){
        int rounds = 0;
        while (rounds < 3) {
            for(int ID = 0; ID < players.length; ID++){
                int cardIndex = 0;
                System.out.println(printPlayerTurn(ID, players[ID]));
                boolean validInput = false;
                do {
                    System.out.print(players[ID].printPlayerHand() + "\u001B[0m\n\u001B[34mSelect a card to play:\u001B[0m");
                try {
                    //Offset by 1, to match the index of the card
                    cardIndex = Integer.parseInt(reader.nextLine())-1;
                    if(cardIndex >= 0 && cardIndex < players[ID].handLength()){
                        validInput = true;
                    } else {
                        System.out.println("\u001B[31mInvalid card index. Please enter a number between 1 and " + players[ID].handLength() + "\u001B[0m");
                    }
                    } catch (NumberFormatException e) {
                    System.out.println("\u001B[31mINVALID INPUT. PLEASE ENTER AN INTEGER\u001B[0m");
                    }
                } while (!validInput);
                players[ID].checkWasabi(cardIndex);           
                players[ID].manipulateHand(cardIndex);
                System.out.println(players[ID].printPlayerHand());
            }
            moveDecks(players);
            rounds++;
        }
        System.out.println(calculateWinner(players));
    }
    
    //Do calculation and determine winner
    public static String calculateWinner(Player[] players) {
        String builder = "";
        builder += "Calculating points...\n";
        int maxPoints = 0;
        int winnerIndex = 0;
        for (int i = 0; i < players.length; i++) {
            int points = calculatePointsPerPlayer(players, i);
            builder += "Player " + (i + 1) + " has " + points + " points\n";
            if (points > maxPoints) {
                maxPoints = points;
                winnerIndex = i;
            }
        }
        builder += "\u001B[32mPlayer " + (winnerIndex + 1) + " wins with " + maxPoints + " points!\u001B[0m";
        return builder;
        }


    public static void moveDecks(Player[] players) {
        // Array to hold the deep copies of the players' hands
        DynamicCardArray[] copiedHands = new DynamicCardArray[players.length];
        
        // Deep copy the players' hands
        for (int i = 0; i < players.length; i++) {
            copiedHands[i] = players[i].getHand();
        }

        // Shift the hands to the next player
        for (int i = 0; i < players.length; i++) {
            if (i == players.length - 1) {
            players[0].setHand(copiedHands[i]);
            } else {
            players[i + 1].setHand(copiedHands[i]);
            }
        }
    }

        public static String startGame(){
        return "\u001B[32m" + "WELCOME TO SUSHI GO" + "\u001B[0m";    
    }

    public static String printPlayerTurn(int ID, Player player){
        String builder = "";
        //Offset by 1, to match the player ID, not the index
        ID++;
        builder+="\u001B[36m*****************************************" + "\n" + 
        player + "\u001B[36m\n*****************************************\u001B[0m";
        return builder;
    }


    //Order: countSushiRoll, countTempura, countSashimi, countDumpling,
    // countSalmonNigiri, countSquidNigiri, countEggNigiri, countPudding
    public static int calculatePointsPerPlayer(Player[] players, int playerIndex){
        int points = 0;
        int[] cards = players[playerIndex].countCardsPerPlayer();
        // Sushi Rolls
        points += calculateSushiRollPoints(players, playerIndex);
        // Tempura
        points += calcTempura(cards);
        // Sashimi 
        points += calcSashimiPoints(cards);
        // Dumpling
        points += calcDumpling(cards);
        // Salmon Nigiri
        points += cards[4];
        // Squid Nigiri
        points += cards[5];
        // Egg Nigiri
        points += cards[6];
        // Pudding
        points += calculatePuddingPoints(players, playerIndex);

        return points;
    }

    public static int calcSashimiPoints(int[] cards){
        int points = 0;
        if (cards[2] >= 3) {
            int setsOfThree = cards[2] / 3;
            points += setsOfThree * 10;
        }
        return points;
    }

    public static int calcTempura(int[] cards){
        int points = 0;
        if (cards[1] >= 2) {
            int setsOfTwo = cards[1] / 2;
            points += setsOfTwo * 5;
        }
        return points;
    }

    public static int calcDumpling(int[] cards){
        int points = 0;
        switch (cards[3]) {
            case 1:
                points += 1;
                break;
            case 2:
                points += 3;
                break;
            case 3:
                points += 6;
                break;
            case 4:
                points += 10;
                break;
            case 5:
                points += 15;
                break;
        }
        return points;
    }

    public static int calculateSushiRollPoints(Player[] players, int playerIndex) {
        int maxSushiRollCards = 0;
        int secSushiRollCards = 0;
        boolean hasMax = false;
        boolean hasSec = false;
        boolean isTie = false;
        int[] otherPlayerCards = new int[Type.values().length - 3]; // -3 because we are not counting all 3 sushi rolls and wasabi
        int[] allSushiRollCards = new int[players.length];
        
        for (int i = 0; i < players.length; i++) {
            otherPlayerCards = players[i].countCardsPerPlayer();
            allSushiRollCards[i] = otherPlayerCards[0];

            if (allSushiRollCards[i] > maxSushiRollCards) {
                secSushiRollCards = maxSushiRollCards;
                maxSushiRollCards = allSushiRollCards[i];
            } else if (allSushiRollCards[i] > secSushiRollCards) {
                secSushiRollCards = allSushiRollCards[i];
            }
        }

        int playerSushiRollCards = players[playerIndex].countCardsPerPlayer()[0];
        if (playerSushiRollCards == 0)
            return 0;

        
        if (playerSushiRollCards == maxSushiRollCards)
            hasMax = true;
        if (playerSushiRollCards == secSushiRollCards)
            hasSec = true;

        int countMax = 0;
        int countSec = 0;
        for (int i = 0; i < allSushiRollCards.length; i++) {
            if (maxSushiRollCards == allSushiRollCards[i])
                countMax++;
            if(secSushiRollCards == allSushiRollCards[i])
                countSec++;
        }
        if (countMax > 1) 
            isTie = true;
        if (countSec > 1)
            isTie = true;
        
        int points = 0;
        if (hasMax) {
            points += 6;
        } else if (hasSec) {
            points += 3;
        } 
        if ((isTie && hasSec) || (isTie && hasMax)) // if tie, everyone gets half points
            points /= (countMax > 1) ? countMax : (countSec > 1) ? countSec : 1;
        return points;
    }

    public static int calculatePuddingPoints(Player[] players, int playerIndex) {
        int maxPuddingCards = 0;
        int minPuddingCards = Type.PUDDING.getCount(); //max amount of pudding cards, ie 10
        boolean hasMax = false;
        boolean hasMin = false;
        boolean isTie = false;
        int[] otherPlayerCards = new int[Type.values().length - 3];
        int[] allPuddingCards = new int[players.length];
        
        for (int i = 0; i < players.length; i++) {
            otherPlayerCards = players[i].countCardsPerPlayer();
            allPuddingCards[i] = otherPlayerCards[7];

            if (allPuddingCards[i] > maxPuddingCards) 
            maxPuddingCards = allPuddingCards[i];
            if (allPuddingCards[i] < minPuddingCards)
            minPuddingCards = allPuddingCards[i];
        }

        int playerPuddingCards = players[playerIndex].countCardsPerPlayer()[7];

        if (playerPuddingCards == maxPuddingCards)
            hasMax = true;
        if (playerPuddingCards == minPuddingCards)
            hasMin = true;

        int countMax = 0;
        int countMin = 0;
        for (int i = 0; i < allPuddingCards.length; i++) {
            if (maxPuddingCards == allPuddingCards[i])
                countMax++;
            if (minPuddingCards == allPuddingCards[i])
                countMin++;
        }
        if (countMax > 1)
            isTie = true;
        if (countMin > 1)
            isTie = true;

        int points = 0;
        if (hasMax) {
            points += 6;
        } else if (hasMin && players.length > 2){
            points -= 6;
        } 
        if ((isTie && hasMin) || (isTie && hasMax)) //if theres a tie, everyone gets half the points
            points /= (countMax > 1) ? countMax : (countMin > 1) ? countMin : 1;
        
        return points;
    }

}
