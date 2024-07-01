public class Player {
    private DynamicCardArray hand;
    private DynamicCardArray cardOnTable;
    private int playerID;
    private int numOfPlayers;
    private boolean wasabiUsed;

    //Create the players w/ their "empty" decks
    public Player(int playerID, int numOfPlayers){
        this.hand = new DynamicCardArray();
        this.cardOnTable = new DynamicCardArray();
        this.playerID = playerID + 1;
        this.numOfPlayers = numOfPlayers;
        this.wasabiUsed = false;
    }

    //Getters and setters
    public void setWasabiUsed(boolean wasabiUse){
        this.wasabiUsed = wasabiUse;
    }

    public boolean isWasabiUsed(){
        return this.wasabiUsed;
    }

    public String toString(){
        return "\u001B[31mPLAYER " + this.playerID + "\u001B[0m";
    }

    // Deep copy of the hand, necessary?
    public DynamicCardArray getHand() {
        DynamicCardArray copyOfHand = new DynamicCardArray();
        for (int i = 0; i < this.hand.length(); i++) {
            copyOfHand.addCard(this.hand.getCardAtIndex(i));
        }
        return copyOfHand;
    }

    // Deep copy of the hand
    public void setHand(DynamicCardArray copyOfHand) {
        this.hand = new DynamicCardArray();
        for (int i = 0; i < copyOfHand.length(); i++) {
            this.hand.addCard(copyOfHand.getCardAtIndex(i));
        }
    }
    
    //Determine how many cards each player gets based on the number of players
    public int cardPerPlayer(){
        int cardPerPlayer = 10;
            if (this.numOfPlayers == 3) {
                cardPerPlayer-=1;
            } else if (this.numOfPlayers == 4) {
                cardPerPlayer-=2;
            } else if (this.numOfPlayers == 5) {
                cardPerPlayer-=3;
            }
        return cardPerPlayer;
    }

    //Adds cards to the player's hand
    public void addCardToHand(Card card){
        this.hand.addCard(card);
        }

    // Print the player's hand with numbers above each card
    public String printHand(){
        String builder = "";
        for(int i = 0; i < this.hand.length(); i++){
            builder += (i + 1) + ". " + this.hand.getCardAtIndex(i) + " ";
        }
        return builder;
    }

    public String printTable(){
        String builder = "";
        for(int i = 0; i < this.cardOnTable.length(); i++){
            builder+=this.cardOnTable.getCardAtIndex(i) + " ";
        }
        return builder;
    }

    //Card on table manipulation
    public Card getCardFromHand(int i){
        return this.hand.getCardAtIndex(i);
    }
    
    public Card getCardFromTable(int i){
        return this.cardOnTable.getCardAtIndex(i);
    }

    public void addCardToTable(Card card){
        this.cardOnTable.addCard(card);
    }

    public void removeCardAtIndex(int i){
        this.hand.removeCardAtIndex(i);
    }

    public int tableLength(){
        return this.cardOnTable.length();
    }

    public int handLength(){
        return this.hand.length();
    }

    //Add card to table and remove from hand
    public void manipulateHand(int cardIndex){
        this.addCardToTable(this.getCardFromHand(cardIndex));
        this.removeCardAtIndex(cardIndex);
    }

    public void checkWasabi(int cardIndex){
        Card card = this.getCardFromHand(cardIndex);
        if (card.getType() == Type.WASABI) {
            this.setWasabiUsed(true);
        } else if ((card.getType() == Type.SALMON_NIGIRI || card.getType() == Type.SQUID_NIGIRI || card.getType() == Type.EGG_NIGIRI) && this.isWasabiUsed()) {
            card.setWasabiEffect(true);
            this.setWasabiUsed(false);
        }
    }

    public  String printPlayerHand(){
        String builder = "";
        builder+="\u001B[35mDECK: " + this.printHand() + "\u001B[0m\n" + "\u001B[33mTABLE: "
         + this.printTable() + "\u001B[0m";
        return builder;
    }
    
    public int[] countCardsPerPlayer(){
        int[] countCards = new int[Type.values().length-3]; // -3 because we are not counting all 3 sushi rolls and wasabi
        for(int i = 0; i < this.cardOnTable.length(); i++){
            Card card = this.cardOnTable.getCardAtIndex(i);
            int multiplier = card.getWasabiEffect() ? 3 : 1;

            if(card.getType() == Type.ONE_SUSHI_ROLL){
                countCards[0]+=1;
            } else if(card.getType() == Type.TWO_SUSHI_ROLL){
                countCards[0]+=2;
            } else if(card.getType() == Type.THREE_SUSHI_ROLL){
                countCards[0]+=3;
            } else if(card.getType() == Type.TEMPURA){
                countCards[1]+=1;
            } else if(card.getType() == Type.SASHIMI){
                countCards[2]+=1;
            } else if(card.getType() == Type.DUMPLING){
                countCards[3]+=1;
            } else if(card.getType() == Type.SALMON_NIGIRI){
                countCards[4]+=2 * multiplier;
            } else if(card.getType() == Type.SQUID_NIGIRI){
                countCards[5]+=3 * multiplier;
            } else if(card.getType() == Type.EGG_NIGIRI){
                countCards[6]+=1 * multiplier;
            } else if(card.getType() == Type.PUDDING){
                countCards[7]+=1;
            } 
        }
        return countCards;
    }
}