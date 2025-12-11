/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ingunal.brokendeal.beta.controller;

import com.ingunal.brokendeal.beta.SceneManager;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.event.Event;
import java.util.Objects;
import javafx.beans.binding.Bindings;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * 
 * @author andre
 */
public class MenuController {
    @FXML private StackPane root;
    @FXML private ImageView backgroundImage;
    @FXML private HBox centerBox;
    @FXML private ImageView btnPoker;
    @FXML private ImageView btnBlackjack;
    @FXML private ImageView btnQuit;

    // Parámetros dinámicos
    private DoubleProperty centerSpacing = new SimpleDoubleProperty(20);
    private DoubleProperty bottomOffset = new SimpleDoubleProperty(35);
    private DoubleProperty centerVerticalOffset = new SimpleDoubleProperty(215);

    // Rutas de imágenes
    private static final String BG_PATH = "/img/Menu/Menu.gif";
    private static final String POKER_PATH = "/img/Menu/Boton_Menu_Poker.png";
    private static final String BJ_PATH = "/img/Menu/Boton_Menu_BlackJack.png";
    private static final String QUIT_PATH = "/img/Menu/Boton_Menu_Salir.png";
    
    // Audio
    private MediaPlayer mediaPlayer;
    private static final String MUSIC_PATH = "/audio/music/Anxiety.mp3";

    @FXML
    private void initialize() {
        // Cargar imágenes
        backgroundImage.setImage(loadImage(BG_PATH));
        btnPoker.setImage(loadImage(POKER_PATH));
        btnBlackjack.setImage(loadImage(BJ_PATH));
        btnQuit.setImage(loadImage(QUIT_PATH));

        // Pixel-Art nítido
        backgroundImage.setSmooth(false);
        btnPoker.setSmooth(false);
        btnBlackjack.setSmooth(false);
        btnQuit.setSmooth(false);

        btnPoker.setPreserveRatio(true);
        btnBlackjack.setPreserveRatio(true);
        btnQuit.setPreserveRatio(true);

        // Fondo responsivo
        backgroundImage.fitWidthProperty().bind(root.widthProperty());
        backgroundImage.fitHeightProperty().bind(root.heightProperty());

        // Escalado dinámico de botones
        root.widthProperty().addListener((obs, oldW, newW) ->
                adjustButtonSizes(newW.doubleValue(), root.getHeight())
        );
        root.heightProperty().addListener((obs, oldH, newH) ->
                adjustButtonSizes(root.getWidth(), newH.doubleValue())
        );

        // Spacing del centro
        centerBox.spacingProperty().bind(centerSpacing);

        // Bajar centro
        centerBox.styleProperty().bind(
                Bindings.concat("-fx-padding: ",
                        centerVerticalOffset.asString(), " 0 0 0;")
        );

        // Margen inferior del botón Salir
        VBox bottomVBox = (VBox) btnQuit.getParent();
        bottomVBox.styleProperty().bind(
                Bindings.concat("-fx-padding: 0 0 ", bottomOffset.asString(), " 0;")
        );
        
        iniciarMusica();
    }

    private Image loadImage(String resourcePath) {
        try {
            return new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream(resourcePath)
            ));
        } catch (Exception e) {
            System.err.println("No se pudo cargar: " + resourcePath);
            return null;
        }
    }

    private void adjustButtonSizes(double width, double height) {
        if (width <= 0 || height <= 0) return;

        double base = Math.min(width, height);
        double btnW = base * 0.22;
        double btnH = base * 0.14;

        btnPoker.setFitWidth(Math.max(110, btnW));
        btnPoker.setFitHeight(Math.max(50, btnH));

        btnBlackjack.setFitWidth(Math.max(110, btnW));
        btnBlackjack.setFitHeight(Math.max(50, btnH));

        btnQuit.setFitWidth(Math.max(70, base * 0.15));
        btnQuit.setFitHeight(Math.max(30, base * 0.07));
    }

    // ------------------ Eventos del menú ------------------
    @FXML private void onPokerClicked(Event e)   { seleccionarOpcion(0); }
    @FXML private void onBlackjackClicked(Event e){ seleccionarOpcion(1); }
    @FXML private void onQuitClicked(Event e)    { seleccionarOpcion(2); }

    public void seleccionarOpcion(int opcion) {
        fadeOutMusica(() -> {
            switch (opcion) { 
                case 0: 
                    System.out.println("→ Usuario eligió: Poker"); 
                    sceneManager.mostrarIntro("poker"); // ← Ahora crea objetos
                    break; 

                case 1: 
                    System.out.println("→ Usuario eligió: Blackjack"); 
                    sceneManager.mostrarIntro("blackjack"); // ← Ahora crea objetos
                    break; 

                case 2: 
                    System.out.println("→ Saliendo del juego..."); 
                    sceneManager.salir(); 
                    break; 
            }
        });
    }
    
    // ----------------- Música -----------------
    private void iniciarMusica() {
        try {
            Media media = new Media(Objects.requireNonNull(
                    getClass().getResource(MUSIC_PATH)
            ).toExternalForm());

            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setVolume(0.0);
            mediaPlayer.play();

            fadeInMusica();

        } catch (Exception e) {
            System.err.println("ERROR cargando música: " + e.getMessage());
        }
    }

    private void fadeInMusica() {
        if (mediaPlayer == null) return;

        new Thread(() -> {
            try {
                for (double v = 0.0; v <= 1.0; v += 0.02) {
                    double volume = v;
                    javafx.application.Platform.runLater(() -> mediaPlayer.setVolume(volume));
                    Thread.sleep(40);
                }
            } catch (InterruptedException ignored) {}
        }).start();
    }

    private void fadeOutMusica(Runnable accionDespues) {
        if (mediaPlayer == null) {
            if (accionDespues != null) accionDespues.run();
            return;
        }

        new Thread(() -> {
            try {
                for (double v = mediaPlayer.getVolume(); v >= 0.0; v -= 0.03) {
                    double volume = v;
                    javafx.application.Platform.runLater(() -> mediaPlayer.setVolume(volume));
                    Thread.sleep(40);
                }
            } catch (Exception ignored) {}

            javafx.application.Platform.runLater(() -> {
                mediaPlayer.stop();
                if (accionDespues != null)
                    accionDespues.run();
            });
        }).start();
    }

    // ------------------ Setter de SceneManager ------------------
    private SceneManager sceneManager;

    public void setSceneManager(SceneManager sm) { 
        this.sceneManager = sm; 
    }
}