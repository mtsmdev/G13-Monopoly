package ui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import property.PropertyController;

import java.util.stream.Collectors;

import static utils.Music.playMusic;
import static utils.Music.playSoundEffect;

public class DialogView {
    private DialogView() {}

    private static void showLayer(AnchorPane board) {
        getLayer(board).setVisible(true);
    }

    private static void hideLayer(AnchorPane board) {
        getLayer(board).setVisible(false);
    }

    private static AnchorPane getLayer(AnchorPane board) {
        var stack = (StackPane) board.getParent();
        return (AnchorPane) stack.getChildren().stream().filter(p -> p.getId().equals("dialogLayer")).collect(Collectors.toList()).get(0);
    }

    public static void createKaartDialog(AnchorPane board, String kaartTitle, String msg, Runnable acceptFn){
        playSoundEffect("showDialog.wav");
        showLayer(board);
        VBox dialog = new VBox();
        dialog.setLayoutX(160);
        dialog.setLayoutY(100);

        Label title = new Label(kaartTitle);

        Text bericht;
        var maxLineLength = 35;
        if (msg.length() <= maxLineLength){
            bericht = new Text(msg+"\n\n");
        }else{
            if (msg.length() <= maxLineLength * 2){
                bericht = new Text(msg+"\n");
            }else {
                bericht = new Text(msg);
            }
        }

        title.setTranslateY(-50);
        bericht.setWrappingWidth(400);
        bericht.setTranslateX(10);

        var buttonAccept = new Button("Accepteren");
        buttonAccept.getStyleClass().add("button_accept");
        buttonAccept.applyCss();

        buttonAccept.setPrefWidth(150);


        dialog.getChildren().addAll(title, bericht, buttonAccept);

        buttonAccept.setOnMouseClicked(mouseEvent -> {
            playSoundEffect("GameButton.wav");
            acceptFn.run();
            hideLayer(board);
            getLayer(board).getChildren().clear();
        });


        dialog.getStyleClass().add("dialog");
        dialog.applyCss();
        title.getStyleClass().add("kaartTitle");
        title.applyCss();
        bericht.getStyleClass().add("kaartMsg");
        bericht.applyCss();

        getLayer(board).getChildren().addAll(dialog);
    }


    public static void createPropertyDialog(AnchorPane board, PropertyController property, Runnable acceptFn, Runnable abortFn){
        playSoundEffect("showDialog.wav");
        showLayer(board);
        HBox dialog = new HBox();
        dialog.getStyleClass().add("dialog");
        dialog.applyCss();
        dialog.setLayoutX(160);
        dialog.setLayoutY(100);

        VBox rechter_gedeelte = new VBox();
        VBox linker_gedeelte = new VBox();
        VBox part1_linker_gedeelte = new VBox();
        VBox part2_linker_gedeelte = new VBox();

        Button accept = new Button("Kopen");
        Button annuleer = new Button("Annuleren");

        accept.setOnAction(event -> {
            playSoundEffect("money.wav");
            acceptFn.run();
            getLayer(board).getChildren().clear();
            hideLayer(board);
        });

        annuleer.setOnAction(event -> {
            playSoundEffect("GameButton.wav");
            abortFn.run();
            getLayer(board).getChildren().clear();
            hideLayer(board);
        });

        accept.getStyleClass().add("button_accept");
        accept.applyCss();
        annuleer.getStyleClass().add("button_accept");
        accept.applyCss();

        accept.setTranslateY(20);
        annuleer.setTranslateY(40);

        var image_logo = new Image(DialogView.class.getResource("/images/login/duopoly_logo.png").toString(), 200, 100, true, true);
        var imageView_logo = new ImageView(image_logo);
        imageView_logo.prefWidth(100);
        imageView_logo.prefHeight(100);

        imageView_logo.setTranslateY(-5);
        imageView_logo.setTranslateX(18);

        rechter_gedeelte.getChildren().addAll(imageView_logo, accept, annuleer);


        Label straatname = new Label(property.getName());
        Label straatNetPrijs = new Label("€ "+ property.getPrice());
        linker_gedeelte.getStyleClass().add("straat");
        linker_gedeelte.applyCss();
        part1_linker_gedeelte.getChildren().addAll(straatname, straatNetPrijs);
        part1_linker_gedeelte.getStyleClass().add("headStreet");
        part1_linker_gedeelte.applyCss();
        part2_linker_gedeelte.getStyleClass().add("bodyStreet");
        straatname.getStyleClass().add("straatNaam");
        straatname.applyCss();
        straatNetPrijs.getStyleClass().add("prijsStraat");
        straatNetPrijs.applyCss();

        Paint paint = property.getColor();
        CornerRadii radii = new CornerRadii(4);
        BackgroundFill backgroundFill = new BackgroundFill(paint, radii, Insets.EMPTY);

        
        Background background = new Background(backgroundFill);
        part1_linker_gedeelte.setBackground(background);

        int huisKostenRekenen = property.getRent();

        var geenHuisImage = new Image(DialogView.class.getResource("/images/kaarten/Huis_Hotel/no_book.png").toString(), 28, 30, true, true);
        var huisImage = new Image(DialogView.class.getResource("/images/kaarten/Huis_Hotel/green_book.png").toString(), 28, 30, true, true);
        var hotelImage = new Image(DialogView.class.getResource("/images/kaarten/Huis_Hotel/red_book.png").toString(), 28, 30, true, true);


        HBox straatPrijs_ = new HBox();
        var geenHuis = new ImageView(geenHuisImage);
        Label prijs = new Label("€ "+property.getRent());
        straatPrijs_.getChildren().addAll(geenHuis, prijs);

        HBox huis1 = new HBox();
        huisKostenRekenen+= property.getRent();
        var huisSympol1 = new ImageView(huisImage);
        Label huis1_prijs = new Label("€ "+huisKostenRekenen);
        huis1.getChildren().addAll(huisSympol1, huis1_prijs);

        HBox huis2 = new HBox();
        huisKostenRekenen+= property.getRent();
        var huisSympol2 = new ImageView(huisImage);
        Label huis2_prijs = new Label("€ "+huisKostenRekenen);
        huis2.getChildren().addAll(huisSympol2, huis2_prijs);

        HBox huis3 = new HBox();
        huisKostenRekenen+= property.getRent();
        var huisSympol3 = new ImageView(huisImage);
        Label huis3_prijs = new Label("€ "+huisKostenRekenen);
        huis3.getChildren().addAll(huisSympol3, huis3_prijs);

        HBox huis4 = new HBox();
        huisKostenRekenen+= property.getRent();
        var huisSympol4 = new ImageView(huisImage);
        Label huis4_prijs = new Label("€ "+huisKostenRekenen);
        huis4.getChildren().addAll(huisSympol4, huis4_prijs);

        HBox huis5 = new HBox();
        huisKostenRekenen+= property.getRent();
        var huisSympol5 = new ImageView(hotelImage);
        Label huis5_prijs = new Label("€ "+huisKostenRekenen);
        huis5.getChildren().addAll(huisSympol5, huis5_prijs);


        prijs.getStyleClass().add("prijs");
        huis1_prijs.getStyleClass().add("prijs");
        huis2_prijs.getStyleClass().add("prijs");
        huis3_prijs.getStyleClass().add("prijs");
        huis4_prijs.getStyleClass().add("prijs");
        huis5_prijs.getStyleClass().add("prijs");
        prijs.applyCss();
        huis1_prijs.applyCss();
        huis2_prijs.applyCss();
        huis3_prijs.applyCss();
        huis4_prijs.applyCss();
        huis5_prijs.applyCss();

        part2_linker_gedeelte.getChildren().addAll(straatPrijs_, huis1, huis2, huis3, huis4, huis5);
        linker_gedeelte.getChildren().addAll(part1_linker_gedeelte, part2_linker_gedeelte);


        dialog.getChildren().addAll(linker_gedeelte, rechter_gedeelte);

        getLayer(board).getChildren().add(dialog);

    }

    public static void createHuisDialog(AnchorPane board, PropertyController property, Runnable buyFn, Runnable nextFn){
        playSoundEffect("showDialog.wav");
        showLayer(board);
        HBox dialog = new HBox();
        dialog.getStyleClass().add("dialog");
        dialog.applyCss();
        dialog.setLayoutX(160);
        dialog.setLayoutY(100);

        VBox rechter_gedeelte = new VBox();
        VBox linker_gedeelte = new VBox();
        VBox part1_linker_gedeelte = new VBox();
        VBox part2_linker_gedeelte = new VBox();

        Button accept = new Button("Koop Huis €" + property.getRent() + "!");
        Button annuleer = new Button("Volgende zet.");

        accept.setOnAction(event -> {
            playSoundEffect("addHuis.wav");
            buyFn.run();
            getLayer(board).getChildren().clear();
            hideLayer(board);
        });

        annuleer.setOnAction(event -> {
            playSoundEffect("GameButton.wav");
            nextFn.run();
            getLayer(board).getChildren().clear();
            hideLayer(board);
        });

        accept.getStyleClass().add("button_accept");
        accept.applyCss();
        annuleer.getStyleClass().add("button_accept");
        accept.applyCss();

        accept.setTranslateY(20);
        annuleer.setTranslateY(40);

        var image_logo = new Image(DialogView.class.getResource("/images/login/duopoly_logo.png").toString(), 200, 100, true, true);
        var imageView_logo = new ImageView(image_logo);
        imageView_logo.prefWidth(100);
        imageView_logo.prefHeight(100);

        imageView_logo.setTranslateY(-5);
        imageView_logo.setTranslateX(18);

        rechter_gedeelte.getChildren().addAll(imageView_logo, accept, annuleer);


        Label straatname = new Label(property.getName());
        Label straatNetPrijs = new Label("€ "+ property.getPrice());
        linker_gedeelte.getStyleClass().add("straat");
        linker_gedeelte.applyCss();
        part1_linker_gedeelte.getChildren().addAll(straatname, straatNetPrijs);
        part1_linker_gedeelte.getStyleClass().add("headStreet");
        part1_linker_gedeelte.applyCss();
        part2_linker_gedeelte.getStyleClass().add("bodyStreet");
        straatname.getStyleClass().add("straatNaam");
        straatname.applyCss();
        straatNetPrijs.getStyleClass().add("prijsStraat");
        straatNetPrijs.applyCss();

        Paint paint = property.getColor();
        CornerRadii radii = new CornerRadii(4);
        BackgroundFill backgroundFill = new BackgroundFill(paint, radii, Insets.EMPTY);


        Background background = new Background(backgroundFill);
        part1_linker_gedeelte.setBackground(background);

        var geenHuisImage = new Image(DialogView.class.getResource("/images/kaarten/Huis_Hotel/no_book.png").toString(), 28, 30, true, true);
        var huisImage = new Image(DialogView.class.getResource("/images/kaarten/Huis_Hotel/green_book.png").toString(), 28, 30, true, true);
        var hotelImage = new Image(DialogView.class.getResource("/images/kaarten/Huis_Hotel/red_book.png").toString(), 28, 30, true, true);


        HBox straatPrijs_ = new HBox();
        int huisKostenRekenen = property.getRent();
        var geenHuis = new ImageView(geenHuisImage);
        Label prijs = new Label("€ "+property.getRent());
        straatPrijs_.getChildren().addAll(geenHuis, prijs);

        HBox huis1 = new HBox();
        huisKostenRekenen += property.getRent();
        var huisSympol1 = new ImageView(huisImage);
        Label huis1_prijs = new Label("€ "+huisKostenRekenen);
        huis1.getChildren().addAll(huisSympol1, huis1_prijs);

        HBox huis2 = new HBox();
        huisKostenRekenen+= property.getRent();
        var huisSympol2 = new ImageView(huisImage);
        Label huis2_prijs = new Label("€ "+huisKostenRekenen);
        huis2.getChildren().addAll(huisSympol2, huis2_prijs);

        HBox huis3 = new HBox();
        huisKostenRekenen+= property.getRent();
        var huisSympol3 = new ImageView(huisImage);
        Label huis3_prijs = new Label("€ "+huisKostenRekenen);
        huis3.getChildren().addAll(huisSympol3, huis3_prijs);

        HBox huis4 = new HBox();
        huisKostenRekenen+= property.getRent();
        var huisSympol4 = new ImageView(huisImage);
        Label huis4_prijs = new Label("€ "+huisKostenRekenen);
        huis4.getChildren().addAll(huisSympol4, huis4_prijs);

        HBox huis5 = new HBox();
        huisKostenRekenen+= property.getRent();
        var huisSympol5 = new ImageView(hotelImage);
        Label huis5_prijs = new Label("€ "+huisKostenRekenen);
        huis5.getChildren().addAll(huisSympol5, huis5_prijs);


        prijs.getStyleClass().add("prijs");
        huis1_prijs.getStyleClass().add("prijs");
        huis2_prijs.getStyleClass().add("prijs");
        huis3_prijs.getStyleClass().add("prijs");
        huis4_prijs.getStyleClass().add("prijs");
        huis5_prijs.getStyleClass().add("prijs");
        prijs.applyCss();
        huis1_prijs.applyCss();
        huis2_prijs.applyCss();
        huis3_prijs.applyCss();
        huis4_prijs.applyCss();
        huis5_prijs.applyCss();

        part2_linker_gedeelte.getChildren().addAll(straatPrijs_, huis1, huis2, huis3, huis4, huis5);
        linker_gedeelte.getChildren().addAll(part1_linker_gedeelte, part2_linker_gedeelte);


        dialog.getChildren().addAll(linker_gedeelte, rechter_gedeelte);

        getLayer(board).getChildren().add(dialog);

    }


    public static void createBelastingDialog(AnchorPane board, Runnable acceptFn){
        playSoundEffect("showDialog.wav");
        showLayer(board);
        VBox dialog = new VBox();

        dialog.setLayoutX(160);
        dialog.setLayoutY(100);

        Text title = new Text("Belasting");
        Text bedrag = new Text("Betaal 200 euro");

        var buttonAccept = new Button("Betalen");
        buttonAccept.getStyleClass().add("button_accept");
        buttonAccept.applyCss();

        dialog.getStyleClass().add("dialog");
        dialog.applyCss();
        title.getStyleClass().add("kaartTitle");
        title.applyCss();
        bedrag.getStyleClass().add("kaartMsg");

        title.setTranslateY(-50);
        bedrag.setTranslateY(-20);
        buttonAccept.setTranslateY(20);


        dialog.getChildren().addAll(title, bedrag, buttonAccept);
        getLayer(board).getChildren().add(dialog);

        buttonAccept.setOnMouseClicked(mouseEvent -> {
            playSoundEffect("money.wav");
            acceptFn.run();
            getLayer(board).getChildren().remove(dialog);
            hideLayer(board);
        });
    }


}
