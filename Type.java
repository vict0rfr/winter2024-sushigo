public enum Type {
    ONE_SUSHI_ROLL(6),
    TWO_SUSHI_ROLL(12),
    THREE_SUSHI_ROLL(8),
    TEMPURA(14),
    SASHIMI(14),
    DUMPLING(14),
    SALMON_NIGIRI(10),
    SQUID_NIGIRI(5),
    EGG_NIGIRI(5),
    WASABI(6),
    PUDDING(10);

    private int countOfCards;
    private Type(int countOfCards){
        this.countOfCards = countOfCards;
    }

    public int getCount(){
        return countOfCards;
    }
}