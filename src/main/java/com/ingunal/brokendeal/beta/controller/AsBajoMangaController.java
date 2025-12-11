/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ingunal.brokendeal.beta.controller;

import com.ingunal.brokendeal.beta.SceneManager;
import com.ingunal.brokendeal.beta.model.vo.Baraja;
import com.ingunal.brokendeal.beta.model.vo.Carta;
import com.ingunal.brokendeal.beta.model.vo.personajes.Jugador;
import com.ingunal.brokendeal.beta.model.vo.trucos.AzBajoMangaTruco;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

/**
 *
 * @author andre
 */
public class AsBajoMangaController {

    @FXML private Label tituloLabel;
    @FXML private HBox cartasBox;
    @FXML private Label clickLabel;

    private SceneManager sceneManager;
    private Jugador jugador;
    private Baraja baraja;

    private List<Carta> cartasDisponibles;
    private int indiceSeleccionado = -1;
    private Carta cartaSeleccionada;

    private List<StackPane> cartasVisuales = new ArrayList<>();
    
    protected String typeGame;
    

    // ------------------ Inyección ------------------ //

    public void setSceneManager(SceneManager sm) { this.sceneManager = sm; }
    public void setJugador(Jugador j) { this.jugador = j; }
    public void setBaraja(Baraja b) { this.baraja = b; }

    // ------------------ Inicialización ------------------ //

    public void inicializar(String game) {

        aplicarFuentePixelada();

        cartasDisponibles = seleccionarCartasAleatorias(5);
        mostrarCartas();
        
        typeGame = game;

        aplicarAnimacionClickLabel();
        clickLabel.setVisible(false);
    }

    // ------------------ Fuente pixel ------------------ //

    private void aplicarFuentePixelada() {
        try {
            Font fontPixel = Font.loadFont(
                    Objects.requireNonNull(getClass().getResourceAsStream("/fuentes/PixerLetters.ttf")),
                    32
            );
            tituloLabel.setFont(fontPixel);
            clickLabel.setFont(fontPixel);
        } catch (Exception e) {
            System.err.println("No se pudo cargar la fuente pixelada.");
        }
    }

    // ------------------ Selección aleatoria ------------------ //

    private List<Carta> seleccionarCartasAleatorias(int cantidad) {
        List<Carta> copia = new ArrayList<>(baraja.getCartas());
        Collections.shuffle(copia);
        return copia.subList(0, cantidad);
    }

    // ------------------ Mostrar cartas ------------------ //

    private void mostrarCartas() {
        cartasBox.getChildren().clear();
        cartasVisuales.clear();

        for (int i = 0; i < cartasDisponibles.size(); i++) {
            Carta carta = cartasDisponibles.get(i);

            Image img = cargarImagen(carta.getImgRuta());

            ImageView imgView = new ImageView(img);
            imgView.setFitWidth(140);
            imgView.setPreserveRatio(true);
            imgView.setSmooth(false);

            StackPane cont = new StackPane(imgView);
            cont.setStyle(bordeNormal());
            cont.setPadding(new Insets(10));
            cont.setCursor(Cursor.HAND);

            final int idx = i;
            cont.setOnMouseClicked(e -> seleccionarCarta(idx));
            cont.setOnMouseEntered(e -> aplicarHover(cont, idx));
            cont.setOnMouseExited(e -> quitarHover(cont, idx));

            cartasVisuales.add(cont);
            cartasBox.getChildren().add(cont);
        }
    }

    private Image cargarImagen(String ruta) {
        try {
            return new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream("/" + ruta)
                ),
                0, 0, true, false
            );
        } catch (Exception e) {
            System.err.println("Error cargando imagen: " + ruta);
            return null;
        }
    }

    // ------------------ Estilos ------------------ //

    private String bordeNormal() {
        return "-fx-border-color: white; -fx-border-width: 0;";
    }

    private String bordeHover() {
        return "-fx-border-color: yellow; -fx-border-width: 1;";
    }

    private String bordeSeleccionado() {
        return "-fx-border-color: white; -fx-border-width: 1;";
    }

    private void aplicarHover(StackPane cont, int idx) {
        if (idx != indiceSeleccionado)
            cont.setStyle(bordeHover());
    }

    private void quitarHover(StackPane cont, int idx) {
        if (idx != indiceSeleccionado)
            cont.setStyle(bordeNormal());
    }

    // ------------------ Selección ------------------ //

    private void seleccionarCarta(int indice) {

        // Desmarcar anterior
        if (indiceSeleccionado >= 0) {
            cartasVisuales.get(indiceSeleccionado).setStyle(bordeNormal());
        }

        indiceSeleccionado = indice;
        cartaSeleccionada = cartasDisponibles.get(indice);

        StackPane cont = cartasVisuales.get(indice);
        cont.setStyle(bordeSeleccionado());

        clickLabel.setVisible(true);
        tituloLabel.setText("Carta seleccionada");

        // Fade-out suave
        FadeTransition fade = new FadeTransition(Duration.seconds(0.8), cont);
        fade.setToValue(0.15);
        fade.play();

        clickLabel.getScene().setOnMouseClicked(this::continuarAlJuego);
    }

    // ------------------ Animación del texto inferior ------------------ //

    private void aplicarAnimacionClickLabel() {
        FadeTransition ft = new FadeTransition(Duration.seconds(1.2), clickLabel);
        ft.setFromValue(0.4);
        ft.setToValue(1.0);
        ft.setCycleCount(FadeTransition.INDEFINITE);
        ft.setAutoReverse(true);
        ft.play();
    }

    // ------------------ Continuar al juego ------------------ //

    private void continuarAlJuego(MouseEvent event) {
        if (cartaSeleccionada == null) return;
        
        // Asignar la carta al truco del jugador
        for (var truco : jugador.getTrucos()) {
            if (truco instanceof AzBajoMangaTruco) {
                AzBajoMangaTruco azTruco = (AzBajoMangaTruco) truco;
                
                // Asignar la carta usando el setter
                azTruco.setCartaGuardada(cartaSeleccionada);
                
                System.out.println("✓ As Bajo la Manga configurado: " + 
                                 obtenerNombreCarta(cartaSeleccionada));
                
                break;
            }
        }
        
        System.out.println("Cargando vista del juego...");
        sceneManager.mostrarVistaJuego(typeGame);
    }
    
    private String obtenerNombreCarta(Carta carta) {
        String palo = obtenerNombrePalo(carta.getSimbolo());
        String valor = String.valueOf(carta.getValor());
        
        // Traducir valores especiales
        switch (carta.getValor()) {
            case 'A': valor = "As"; break;
            case 'J': valor = "Jota"; break;
            case 'Q': valor = "Reina"; break;
            case 'K': valor = "Rey"; break;
        }
        
        return valor + " de " + palo;
    }

    /**
     * Traduce el símbolo del palo a texto legible
     */
    private String obtenerNombrePalo(String simbolo) {
        switch (simbolo) {
            case "Hearts": return "Corazones";
            case "Diamonds": return "Diamantes";
            case "Spades": return "Picas";
            case "Clubs": return "Tréboles";
            default: return simbolo;
        }
    }
}
