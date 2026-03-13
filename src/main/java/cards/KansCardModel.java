package cards;

import player.PlayerController;

public class KansCardModel implements Card{

    String beschrijving;
    Integer locatie;
    int value;


    public KansCardModel(String beschrijving, int value, Integer locatie){
        this.beschrijving = beschrijving;
        this.value = value;
        this.locatie = locatie;
    }
    @Override
    public String getBeschrijving() {
        return beschrijving;
    }

    @Override
    public int getValue() {
        return value;
    }

    public Integer getLocation(){
        return locatie;
    }
}
