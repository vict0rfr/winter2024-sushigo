public class Card{
    private Type type;
    private boolean wasabiEffect;

    public Card(Type type){
        this.type = type;
        this.wasabiEffect = false;
    }

    public String toString(){
        return this.type + " ";
    }

    public Type getType(){
        return this.type;
    }

    public void setWasabiEffect(boolean wasabiEffect){
        this.wasabiEffect = wasabiEffect;
    }

    public boolean getWasabiEffect(){
        return this.wasabiEffect;
    }
}