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
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
    
    private Carta cartaSeleccionada;
    private List<Carta> cartasDisponibles; // 5 cartas aleatorias
    
    // Referencias visuales
    private List<VBox> cartasVisual;
    private int indiceSeleccionado = -1;

    /**
     * Configura las dependencias necesarias
     */
    public void setSceneManager(SceneManager sm) {
        this.sceneManager = sm;
    }
    
    public void setJugador(Jugador j) {
        this.jugador = j;
    }
    
    public void setBaraja(Baraja b) {
        this.baraja = b;
    }

    /**
     * Inicializa la vista: selecciona 5 cartas aleatorias y las muestra
     */
    public void inicializar() {
        // Seleccionar 5 cartas aleatorias de la baraja
        cartasDisponibles = seleccionarCartasAleatorias(5);
        
        if (cartasDisponibles.size() != 5) {
            System.err.println("ERROR: No se pudieron seleccionar 5 cartas de la baraja");
            return;
        }
        
        // Crear las visualizaciones de las cartas
        cartasVisual = new ArrayList<>();
        mostrarCartas();
        
        // Animación del texto "haz clic para continuar"
        FadeTransition fade = new FadeTransition(Duration.seconds(1.2), clickLabel);
        fade.setFromValue(0.3);
        fade.setToValue(1.0);
        fade.setCycleCount(FadeTransition.INDEFINITE);
        fade.setAutoReverse(true);
        fade.play();
        
        // Inicialmente ocultar el texto de continuar
        clickLabel.setVisible(false);
    }

    /**
     * Selecciona N cartas aleatorias de la baraja (sin eliminarlas)
     */
    private List<Carta> seleccionarCartasAleatorias(int cantidad) {
        List<Carta> cartasBaraja = new ArrayList<>(baraja.getCartas());
        Collections.shuffle(cartasBaraja);
        
        List<Carta> seleccionadas = new ArrayList<>();
        for (int i = 0; i < Math.min(cantidad, cartasBaraja.size()); i++) {
            seleccionadas.add(cartasBaraja.get(i));
        }
        
        return seleccionadas;
    }

    /**
     * Muestra las 5 cartas en el HBox
     */
    private void mostrarCartas() {
        cartasBox.getChildren().clear();
        
        for (int i = 0; i < cartasDisponibles.size(); i++) {
            Carta carta = cartasDisponibles.get(i);
            
            // Crear contenedor para cada carta
            VBox contenedor = new VBox(10);
            contenedor.setAlignment(Pos.CENTER);
            contenedor.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); " +
                                "-fx-border-color: white; " +
                                "-fx-border-width: 2; " +
                                "-fx-padding: 15; " +
                                "-fx-cursor: hand;");
            
            // Cargar imagen de la carta
            ImageView imgView = new ImageView();
            try {
                String ruta = "/" + carta.getImgRuta();
                Image img = new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream(ruta)
                ));
                imgView.setImage(img);
                imgView.setFitWidth(120);
                imgView.setFitHeight(180);
                imgView.setPreserveRatio(true);
                imgView.setSmooth(false); // Pixel art
            } catch (Exception e) {
                System.err.println("Error cargando imagen: " + carta.getImgRuta());
            }
            
            // Label con el nombre
            Label nombre = new Label(obtenerNombreCarta(carta));
            nombre.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
            
            contenedor.getChildren().addAll(imgView, nombre);
            
            // Guardar referencia
            cartasVisual.add(contenedor);
            
            // Evento de clic
            final int indice = i;
            contenedor.setOnMouseClicked(e -> seleccionarCarta(indice));
            
            // Hover effect
            contenedor.setOnMouseEntered(e -> {
                if (indiceSeleccionado != indice) {
                    contenedor.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2); " +
                                      "-fx-border-color: yellow; " +
                                      "-fx-border-width: 3; " +
                                      "-fx-padding: 15; " +
                                      "-fx-cursor: hand;");
                }
            });
            
            contenedor.setOnMouseExited(e -> {
                if (indiceSeleccionado != indice) {
                    contenedor.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); " +
                                      "-fx-border-color: white; " +
                                      "-fx-border-width: 2; " +
                                      "-fx-padding: 15; " +
                                      "-fx-cursor: hand;");
                }
            });
            
            cartasBox.getChildren().add(contenedor);
        }
    }

    /**
     * Maneja la selección de una carta
     */
    private void seleccionarCarta(int indice) {
        // Desmarcar la anterior
        if (indiceSeleccionado >= 0) {
            VBox anterior = cartasVisual.get(indiceSeleccionado);
            anterior.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); " +
                            "-fx-border-color: white; " +
                            "-fx-border-width: 2; " +
                            "-fx-padding: 15; " +
                            "-fx-cursor: hand;");
        }
        
        // Marcar la nueva
        indiceSeleccionado = indice;
        cartaSeleccionada = cartasDisponibles.get(indice);
        
        VBox seleccionado = cartasVisual.get(indice);
        seleccionado.setStyle("-fx-background-color: rgba(100, 255, 100, 0.3); " +
                            "-fx-border-color: lime; " +
                            "-fx-border-width: 4; " +
                            "-fx-padding: 15; " +
                            "-fx-cursor: hand;");
        
        // Mostrar texto de continuar
        clickLabel.setVisible(true);
        tituloLabel.setText("Carta seleccionada: " + obtenerNombreCarta(cartaSeleccionada));
        
        // Permitir clic en cualquier parte para continuar
        clickLabel.getScene().setOnMouseClicked(this::continuarAlJuego);
    }

    /**
     * Continúa al juego después de seleccionar la carta
     */
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
        
        // NO eliminamos la carta de la baraja (puede aparecer en el juego)
        
        // Ir al juego correspondiente
        String tipoJuego = sceneManager.getTipoJuego();
        if (tipoJuego.equals("poker")) {
            sceneManager.mostrarPoker();
        } else {
            sceneManager.mostrarBlackjack();
        }
    }

    /**
     * Obtiene el nombre legible de una carta
     */
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