package cards;

import board.BoardController;
import player.PlayerController;
import property.PropertyController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static ui.DialogView.createKaartDialog;

public class CardController {

    public final List<AlgemeenFondsCardModel> algemeenFondsStapel;
    public final List<KansCardModel> kansStapel;
    private final Random random = new Random();

    public CardController(){
        this.algemeenFondsStapel = new ArrayList<>();
        this.kansStapel = new ArrayList<>();

        algemeenFondsStapel.add(new AlgemeenFondsCardModel("Je grootouders zijn gestorven, erf €250", 250, null));
        algemeenFondsStapel.add(new AlgemeenFondsCardModel("Betaal je zorgverzekering €115", -115, null));
        algemeenFondsStapel.add(new AlgemeenFondsCardModel("Ga naar START, ontvang €200", 200, 0));
        algemeenFondsStapel.add(new AlgemeenFondsCardModel("Je bent jarig ontvang van elke speler €25", 100, null));
        algemeenFondsStapel.add(new AlgemeenFondsCardModel("Belastingsaangifte wordt uitgekeerd, ontvang €400", 400, null));
        algemeenFondsStapel.add(new AlgemeenFondsCardModel("Belastingsaangifte wordt ingetrokken, betaal €300", -300, null));
        algemeenFondsStapel.add(new AlgemeenFondsCardModel("Je heb je TOTO-bet verloren, betaal €40", -40, null));
        algemeenFondsStapel.add(new AlgemeenFondsCardModel("Je heb je TOTO-bet gewonnen, ontvang €100", 100, null));
        algemeenFondsStapel.add(new AlgemeenFondsCardModel("Ga naar 077", 0, 23));
        algemeenFondsStapel.add(new AlgemeenFondsCardModel("Ontvang €50", 50, null));
        algemeenFondsStapel.add(new AlgemeenFondsCardModel("Je bent dom geweest en moet eigen risico betalen van €385", -385, null));
        algemeenFondsStapel.add(new AlgemeenFondsCardModel("Je krijgt een gratis vakantie,\n"+ "Ga naar schiphol en ontvang 250", 250, 4));
        algemeenFondsStapel.add(new AlgemeenFondsCardModel("Je krijgt je verloren Airpod vergoed\n"+ "van de verzekering, ontvang €115", 115, null));
        algemeenFondsStapel.add(new AlgemeenFondsCardModel("Je gaat zuipen, betaal de rekening van €75", -75, null));


        kansStapel.add(new KansCardModel("Ga verder naar START. \n" + "Ontvang €200", 200, 0));
        kansStapel.add(new KansCardModel("Ga naar Lidda. \n" + "En u ontvangt €200.", 200, 28));
        kansStapel.add(new KansCardModel("De bank betaalt u €100 dividend", 100, null));
        kansStapel.add(new KansCardModel("Ga naar Ally.\n"+ "En ontvang €200", 200, 15));
        kansStapel.add(new KansCardModel("Duo naait je, betaal €150", -150, null));
        kansStapel.add(new KansCardModel("Boete voor wildplassen tijdens het stappen\n"+"Betaal €35", -35, null));
        kansStapel.add(new KansCardModel("Tikkie van vorige maand nog niet betaald\n"+"Betaal direct €45", -45, null));
        kansStapel.add(new KansCardModel("Duo is gestort, ontvang €175", 175, null));
        kansStapel.add(new KansCardModel("Ga naar schiphol", 0, 4));
        kansStapel.add(new KansCardModel("Je wint een prijs, ontvang €150", 150, null));
        kansStapel.add(new KansCardModel("Je overnacht in Rotterdam Zuid, je verliest €350", -350, 30));
        kansStapel.add(new KansCardModel("Je MB's zijn op betaal direct €15", -15, null));
    }

    public void getKansCard(BoardController board, PlayerController player, PropertyController property, Runnable acceptFn) {
        var card = kansStapel.get(random.nextInt(kansStapel.size() - 1));
        runCard(board, player, card);
        createKaartDialog(board.getBoard(), property.getName(), card.getBeschrijving(), acceptFn);

    }

    public void getAlgemeenFonds(BoardController board, PlayerController player, PropertyController property, Runnable acceptFn) {
        var card = algemeenFondsStapel.get(random.nextInt(kansStapel.size() - 1));
        runCard(board, player, card);
        createKaartDialog(board.getBoard(), property.getName(), card.getBeschrijving(), acceptFn);
    }

    private void runCard(BoardController board, PlayerController player, Card card) {
        player.setBalance(card.getValue() + player.getBalance());

        if (card.getLocation() != null) {
            var newCurrentProp = card.getLocation();
            player.moveTo(board.getProperties().get(newCurrentProp));
        }
    }
}
