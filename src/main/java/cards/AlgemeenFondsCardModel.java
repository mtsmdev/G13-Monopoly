package cards;
public class AlgemeenFondsCardModel implements Card {

    String beschrijving;
    int value;
    Integer locatie;

    public AlgemeenFondsCardModel(String beschrijving, Integer value, Integer locatie){ //locatie moet gehaald worden van bord controller.

        this.value = value;
        this.beschrijving = beschrijving;
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
