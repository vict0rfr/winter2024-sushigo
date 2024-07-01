import java.util.Random;
public class Deck {
    private DynamicCardArray initialCards;
    private Random rng;
    
    public Deck(){
        this.rng = new Random();
        this.initialCards = new DynamicCardArray();
        for(Type type : Type.values()){
            for(int i = 0; i < type.getCount(); i++){
                this.initialCards.addCard(new Card(type));
            }
        }
    }

    public Card drawTopCard(){
        int pos = this.initialCards.length()-1;
        Card cardRemoved = initialCards.getCardAtIndex(pos);
        initialCards.removeCardAtIndex(pos);
        return cardRemoved;
    }

    //Deal cards to players based on the number of players
    public void dealCards(Player[] players){
        for(int i = 0; i < players.length; i++){
            for(int j = 0; j < players[i].cardPerPlayer(); j++){
                players[i].addCardToHand(this.drawTopCard());
            }
        }
    }

    //Shuffle the deck
    public void shuffle(){
        for(int i = 0; i < this.initialCards.length(); i++){
            //Find random position to swap with
            int pos = this.rng.nextInt(this.initialCards.length() - 1);
            //Temp card to swap
            Card temp = this.initialCards.getCardAtIndex(i);
            //Swap
            this.initialCards.setCardAtIndex(i, this.initialCards.getCardAtIndex(pos));
            //Set the random position to the temp card
            this.initialCards.setCardAtIndex(pos, temp);            
        }
    }
}
