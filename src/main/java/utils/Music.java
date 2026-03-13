package utils;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Music {
    private Music() {}

    static MediaPlayer player;
    static MediaPlayer soundEffect;

    public static void playMusic(String songName) {
        try{
            String filename = Music.class.getResource("/music/"+songName).toString();
            player = new MediaPlayer(new Media(filename));
            player.setVolume(0.08);
            player.play();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void playSoundEffect(String songName){
        try{
            String filename2 = Music.class.getResource("/music/"+songName).toString();
            soundEffect = new MediaPlayer(new Media(filename2));
            soundEffect.setVolume(1);
            soundEffect.play();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void stopMusic(){
        player.stop();
    }

}
