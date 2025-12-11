/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ingunal.brokendeal.beta.controller;

import com.ingunal.brokendeal.beta.SceneManager;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author andre
 */
public class IntroController {
    @FXML private Label dialogueLabel;
    @FXML private Label continueLabel;

    private SceneManager sceneManager;
    private MediaPlayer player;

    private int currentIndex = 0;

    private final List<String> lines = Arrays.asList(
            "Todo pesa distinto aquí… como si cada recuerdo fuera una piedra colgada al cuello.",
            "No sé si estoy soñando… o si este es mi verdadero rostro.",
            "Me hundí por mi propia mano, y lo peor… es que no caí solo.",
            "Quizá este sea el castigo. Quizá siempre fue el final.",
            "...",
            "¿Está aún en algún sitio? ¿O solo en los rincones que me quedan?",
            "No sé si pueda arreglar nada. No sé si exista una salida.",
            "Pero no puedo quedarme esperando... No ahora.",
            "Lo prometí...",
            "No voy a abandonarte...",
            "No esta vez.",
            ""
    );

    // Ruta del audio
    private final String MUSIC_PATH = "/audio/music/Dark_City.mp3";
    
    private Font fontPixel;

    @FXML
    private void initialize() {
        // Cargar fuente pixelada
        try {
            fontPixel = Font.loadFont(
                Objects.requireNonNull(getClass().getResourceAsStream("/fuentes/PixerLetters.ttf")),
                18 // tamaño base
            );
        } catch (Exception e) {
            System.err.println("Error cargando fuente pixelada: " + e.getMessage());
        }
        
        // Aplicarla al texto
        if (fontPixel != null) {
            dialogueLabel.setFont(fontPixel);
            continueLabel.setFont(fontPixel);
        }

        // Línea inicial
        dialogueLabel.setText(lines.get(0));
        dialogueLabel.setTextAlignment(TextAlignment.CENTER); // Alinea el texto al centro
        dialogueLabel.setWrapText(true); // Importante si el texto es largo

        // Animación suave al texto “haz clic para continuar”
        FadeTransition fade = new FadeTransition(Duration.seconds(1.2), continueLabel);
        fade.setFromValue(0.3);
        fade.setToValue(1.0);
        fade.setCycleCount(Animation.INDEFINITE);
        fade.setAutoReverse(true);
        fade.play();

        // Evento de clic en toda la pantalla
        dialogueLabel.getScene(); // se inicializa luego

    }

    // Este método es llamado por SceneManager
    public void startIntro(String game) {
        playMusic();
        // El evento debe registrarse después de que la escena está creada:
        dialogueLabel.getScene().addEventHandler(MouseEvent.MOUSE_CLICKED, e -> nextLine(game));
        
        /*switch(game){
            case "poker":
                System.out.println("Abriendo juego de Poker...");
                break;
            case "blackjack":
                System.out.println("Abriendo juego de BlackJack...");
                break;
            default:
               System.out.println("Parece que hubo un problema para determinar el tipo de juego...");
               break; 
        }*/
    }

    private void playMusic() {
        try {
            Media media = new Media(Objects.requireNonNull(
                    getClass().getResource(MUSIC_PATH)).toExternalForm());

            player = new MediaPlayer(media);
            player.setCycleCount(MediaPlayer.INDEFINITE); // Loop

            // Fade IN
            player.setVolume(0);
            player.play();

            Timeline fadeIn = new Timeline(
                    new KeyFrame(Duration.seconds(0), new KeyValue(player.volumeProperty(), 0)),
                    new KeyFrame(Duration.seconds(2), new KeyValue(player.volumeProperty(), 0.35))
            );
            fadeIn.play();

        } catch (Exception e) {
            System.err.println("Error iniciando música de intro: " + e.getMessage());
        }
    }

    private void stopMusicWithFade(Runnable afterFade) {
        if (player == null) {
            afterFade.run();
            return;
        }

        Timeline fadeOut = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(player.volumeProperty(), player.getVolume())),
                new KeyFrame(Duration.seconds(1.6), new KeyValue(player.volumeProperty(), 0))
        );

        fadeOut.setOnFinished(e -> {
            player.stop();
            afterFade.run();
        });

        fadeOut.play();
    }

    private void nextLine(String game) {

        currentIndex++;

        // Si terminamos las líneas
        if (currentIndex >= lines.size()) {

            stopMusicWithFade(() -> {
                    System.out.println("Cambiando a vista de seleccionar 'as bajo la manga' ");
                    System.out.println("El tipo de juego es " + game);
                    sceneManager.mostrarAsBajoManga();
            });

            return;
        }

        // Cambiar texto con un pequeño efecto
        FadeTransition fadeOut = new FadeTransition(Duration.millis(200), dialogueLabel);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(200), dialogueLabel);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1.0);

        fadeOut.setOnFinished(e -> {
            dialogueLabel.setText(lines.get(currentIndex));
            fadeIn.play();
        });

        fadeOut.play();
    }

    // Recibir SceneManager desde fuera
    public void setSceneManager(SceneManager sm) {
        this.sceneManager = sm;
    }
}
