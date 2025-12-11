package com.ingunal.brokendeal.beta.controller;

import com.ingunal.brokendeal.beta.SceneManager;
import com.ingunal.brokendeal.beta.model.vo.Carta;
import com.ingunal.brokendeal.beta.model.vo.juego.EstadoJuego;
import com.ingunal.brokendeal.beta.model.vo.personajes.Dealer;
import com.ingunal.brokendeal.beta.model.vo.personajes.Jugador;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

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
        
        // Aplicar oscurecimiento inicial si no es turno del jugador
        aplicarEfectoTurno();
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
     * Actualiza las barras de vida del jugador y dealer.
     * AJUSTE: Redondea al múltiplo de 2 más cercano hacia ARRIBA.
     */
    private void actualizarBarraVida() {
        Jugador jugador = controladorJuego.getJuego().getJugador();
        Dealer dealer = controladorJuego.getJuego().getDealer();
        
        // Redondear al múltiplo de 2 más cercano hacia arriba
        int estadoVidaJugador = redondearHaciaArribaDivisor2(jugador.getVida());
        int estadoVidaDealer = redondearHaciaArribaDivisor2(dealer.getVida());
        
        String rutaVidaJugador = "/img/Vida/Jugador/player_health-bar_" + estadoVidaJugador + ".png";
        String rutaVidaDealer = "/img/Vida/Dealer/boss_health-bar_" + estadoVidaDealer + ".png";
        
        cargarImagen(vidaJugadorImageView, rutaVidaJugador);
        cargarImagen(vidaDealerImageView, rutaVidaDealer);
    }
    
    /**
     * Redondea un valor al múltiplo de 2 más cercano hacia ARRIBA.
     * Ejemplo: 55 -> 56, 54 -> 54, 57 -> 58
     */
    private int redondearHaciaArribaDivisor2(int valor) {
        if (valor % 2 == 0) {
            return valor; // Ya es par
        } else {
            return valor + 1; // Redondear hacia arriba
        }
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
            ImageView cartaView = crearCartaView(carta, "jugador");
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
            ImageView cartaView = crearCartaView(carta, "dealer");
            
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
    private ImageView crearCartaView(Carta carta, String personaje) {
        ImageView imgView = new ImageView();
        
        String ruta = "/" + carta.getImgRuta();
        cargarImagen(imgView, ruta);
        
        if(personaje=="dealer"){
            imgView.setFitWidth(50);  // Ajusta aquí el ancho de las cartas del dealer
            imgView.setFitHeight(80); // Ajusta aquí el alto de las cartas del dealer
        } else {
            imgView.setFitWidth(90);  // Ajusta aquí el ancho de las cartas del jugador
            imgView.setFitHeight(150); // Ajusta aquí el alto de las cartas del jugador
        }
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
    
    /**
     * Aplica efecto de oscurecimiento a las cartas del jugador cuando NO es su turno
     */
    private void aplicarEfectoTurno() {
        EstadoJuego estado = controladorJuego.getJuego().getEstado();
        
        if (estado == EstadoJuego.TURNO_JUGADOR) {
            // Es turno del jugador: cartas normales
            restaurarBrilloCartas();
        } else {
            // No es turno del jugador: oscurecer cartas
            oscurecerCartasJugador();
        }
    }
    
    /**
     * Oscurece las cartas del jugador con una animación suave
     */
    private void oscurecerCartasJugador() {
        for (ImageView cartaView : cartasJugadorVisual) {
            FadeTransition fade = new FadeTransition(Duration.seconds(0.5), cartaView);
            fade.setToValue(0.4); // Opacidad reducida
            fade.play();
        }
    }
    
    /**
     * Restaura el brillo normal de las cartas del jugador
     */
    private void restaurarBrilloCartas() {
        for (ImageView cartaView : cartasJugadorVisual) {
            FadeTransition fade = new FadeTransition(Duration.seconds(0.5), cartaView);
            fade.setToValue(1.0); // Opacidad completa
            fade.play();
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
        aplicarEfectoTurno(); // NUEVO: Actualizar efecto de turno
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