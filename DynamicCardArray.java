public class DynamicCardArray {
    private Card[] cards;
    private int numOfCards;

    public DynamicCardArray(){
        this.cards = new Card[1000];
        this.numOfCards = 0;
    }

    public int length(){
        return this.numOfCards;
    }

    public Card getCardAtIndex(int index){
        return cards[index];
    }

    public void addCard(Card card){
        this.cards[this.numOfCards] = card;
        this.numOfCards++;
    }

    public void setCardAtIndex(int index, Card card){
        this.cards[index] = card;
    }

    public void removeCardAtIndex(int index){
        for(int i = index; i < this.numOfCards; i++){
            this.cards[i] = this.cards[i+1];
        }
        this.numOfCards--;
    }

    public boolean contains(Card card){
        for(int i = 0; i < this.numOfCards; i++){
            if(this.cards[i] == card){
                return true;
            }
        }
        return false;
    }
}
