package com.ingunal.brokendeal.beta.controller;

import com.ingunal.brokendeal.beta.SceneManager;
import com.ingunal.brokendeal.beta.model.vo.Carta;
import com.ingunal.brokendeal.beta.model.vo.personajes.Dealer;
import com.ingunal.brokendeal.beta.model.vo.personajes.Jugador;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JuegoViewController {
    // ========== ELEMENTOS VISUALES ==========
    @FXML private ImageView fondoImageView;
    @FXML private ImageView dealerImageView;
    @FXML private ImageView vidaDealerImageView;
    @FXML private ImageView vidaJugadorImageView;
    @FXML private HBox cartasDealerBox;
    @FXML private HBox cartasJugadorBox;
    @FXML private ImageView botonSuperiorImageView;
    @FXML private ImageView botonInferiorImageView;
    
    // ========== DEPENDENCIAS ==========
    private SceneManager sceneManager;
    private ControladorJuego controladorJuego;
    private String tipoJuego; // "poker" o "blackjack"
    
    // ========== ESTADO VISUAL ==========
    private List<ImageView> cartasJugadorVisual;
    private List<ImageView> cartasDealerVisual;
    
    @FXML
    private void initialize() {
        cartasJugadorVisual = new ArrayList<>();
        cartasDealerVisual = new ArrayList<>();
    }
    
    // ========== SETTERS DE DEPENDENCIAS ==========
    
    public void setSceneManager(SceneManager sm) {
        this.sceneManager = sm;
    }
    
    public void setControladorJuego(ControladorJuego cj) {
        this.controladorJuego = cj;
    }
    
    public void setTipoJuego(String tipo) {
        this.tipoJuego = tipo;
    }
    
    // ========== INICIALIZACIÓN DE LA VISTA ==========
    
    public void inicializarVista() {
        // Cargar imágenes iniciales
        actualizarFondo();
        actualizarDealerGif();
        actualizarBarraVida();
        cargarBotonesSegunJuego();
        
        // Mostrar cartas iniciales
        mostrarCartasJugador();
        mostrarCartasDealer();
    }
    
    // ========== ACTUALIZACIÓN DE ELEMENTOS DINÁMICOS ==========
    
    /**
     * Actualiza el fondo según la vida del jugador (cada 25 puntos)
     */
    private void actualizarFondo() {
        int vida = controladorJuego.getJuego().getJugador().getVida();
        int estado = calcularEstado(vida);
        
        String rutaFondo = "/img/Juego/Mesa_Estado_" + estado + ".png";
        cargarImagen(fondoImageView, rutaFondo);
    }
    
    /**
     * Actualiza el GIF del Dealer según la vida del jugador
     */
    private void actualizarDealerGif() {
        int vida = controladorJuego.getJuego().getJugador().getVida();
        int estado = calcularEstado(vida);
        
        String rutaDealer = "/img/Dealer/Dealer_Estado_" + estado + ".gif";
        cargarImagen(dealerImageView, rutaDealer);
    }
    
    /**
     * Actualiza las barras de vida del jugador y dealer
     */
    private void actualizarBarraVida() {
        Jugador jugador = controladorJuego.getJuego().getJugador();
        Dealer dealer = controladorJuego.getJuego().getDealer();
        
        // Calcular estado de la barra (cada 2 puntos de vida)
        int estadoVidaJugador = jugador.getVida() / 2; // 0-50
        int estadoVidaDealer = dealer.getVida() / 2;   // 0-50
        
        String rutaVidaJugador = "/img/Vida/Jugador/player_health-bar_" + estadoVidaJugador + ".png";
        String rutaVidaDealer = "/img/Vida/Dealer/boss_health-bar_" + estadoVidaDealer + ".png";
        
        cargarImagen(vidaJugadorImageView, rutaVidaJugador);
        cargarImagen(vidaDealerImageView, rutaVidaDealer);
    }
    
    /**
     * Calcula el estado (1-4) según la vida (cada 25 puntos)
     */
    private int calcularEstado(int vida) {
        if (vida > 75) return 1;
        if (vida > 50) return 2;
        if (vida > 25) return 3;
        return 4;
    }
    
    // ========== GESTIÓN DE CARTAS ==========
    
    /**
     * Muestra las cartas del jugador
     */
    private void mostrarCartasJugador() {
        cartasJugadorBox.getChildren().clear();
        cartasJugadorVisual.clear();
        
        List<Carta> cartas = controladorJuego.getJuego().getJugador().getMano().getCartas();
        
        for (Carta carta : cartas) {
            ImageView cartaView = crearCartaView(carta);
            cartasJugadorVisual.add(cartaView);
            cartasJugadorBox.getChildren().add(cartaView);
        }
    }
    
    /**
     * Muestra las cartas del dealer (ocultas por defecto)
     */
    private void mostrarCartasDealer() {
        cartasDealerBox.getChildren().clear();
        cartasDealerVisual.clear();
        
        List<Carta> cartas = controladorJuego.getJuego().getDealer().getMano().getCartas();
        boolean mostrarCartas = controladorJuego.getJuego().getRondasVision() > 0;
        
        for (Carta carta : cartas) {
            ImageView cartaView = crearCartaView(carta);
            
            // Ocultar cartas si no hay visión activa
            if (!mostrarCartas) {
                cartaView.setVisible(false);
            }
            
            cartasDealerVisual.add(cartaView);
            cartasDealerBox.getChildren().add(cartaView);
        }
    }
    
    /**
     * Crea un ImageView para una carta
     */
    private ImageView crearCartaView(Carta carta) {
        ImageView imgView = new ImageView();
        
        String ruta = "/" + carta.getImgRuta();
        cargarImagen(imgView, ruta);
        
        imgView.setFitWidth(80);
        imgView.setFitHeight(120);
        imgView.setPreserveRatio(true);
        imgView.setSmooth(false); // Pixel art
        
        return imgView;
    }
    
    /**
     * Muestra/oculta las cartas del dealer
     */
    public void toggleVisibilidadCartasDealer(boolean visible) {
        for (ImageView cartaView : cartasDealerVisual) {
            cartaView.setVisible(visible);
        }
    }
    
    // ========== BOTONES DE ACCIÓN ==========
    
    /**
     * Carga los botones según el tipo de juego
     */
    private void cargarBotonesSegunJuego() {
        if (tipoJuego.equals("poker")) {
            cargarImagen(botonSuperiorImageView, "/img/Juego/Boton_cambioCarta.png");
            cargarImagen(botonInferiorImageView, "/img/Juego/Boton_plantarse.png");
        } else {
            cargarImagen(botonSuperiorImageView, "/img/Juego/Boton_pedirCarta.png");
            cargarImagen(botonInferiorImageView, "/img/Juego/Boton_plantarse.png");
        }
    }
    
    @FXML
    private void onBotonSuperiorClicked() {
        if (tipoJuego.equals("poker")) {
            // Lógica para cambiar cartas
            System.out.println("→ Cambiar cartas (Poker)");
            // TODO: Abrir diálogo de selección de cartas
        } else {
            // Lógica para pedir carta
            System.out.println("→ Pedir carta (BlackJack)");
            // TODO: Pedir carta al dealer
        }
        
        // Actualizar vista
        actualizarVista();
    }
    
    @FXML
    private void onBotonInferiorClicked() {
        System.out.println("→ Plantarse");
        // TODO: Procesar turno del dealer
        
        // Actualizar vista
        actualizarVista();
    }
    
    // ========== ACTUALIZACIÓN COMPLETA DE LA VISTA ==========
    
    /**
     * Actualiza todos los elementos visuales
     */
    public void actualizarVista() {
        actualizarFondo();
        actualizarDealerGif();
        actualizarBarraVida();
        mostrarCartasJugador();
        mostrarCartasDealer();
    }
    
    // ========== UTILIDAD: CARGA DE IMÁGENES ==========
    
    private void cargarImagen(ImageView imageView, String ruta) {
        try {
            Image img = new Image(Objects.requireNonNull(
                getClass().getResourceAsStream(ruta)
            ));
            imageView.setImage(img);
        } catch (Exception e) {
            System.err.println("Error cargando imagen: " + ruta);
            e.printStackTrace();
        }
    }
}