package com.ingunal.brokendeal.beta.controller;

import com.ingunal.brokendeal.beta.SceneManager;
import com.ingunal.brokendeal.beta.model.vo.Carta;
import com.ingunal.brokendeal.beta.model.vo.juego.EstadoJuego;
import com.ingunal.brokendeal.beta.model.vo.juego.Poker;
import com.ingunal.brokendeal.beta.model.vo.juego.BlackJack;
import com.ingunal.brokendeal.beta.model.vo.personajes.Dealer;
import com.ingunal.brokendeal.beta.model.vo.personajes.Jugador;
import com.ingunal.brokendeal.beta.model.vo.personajes.Personaje;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
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
    
    // ========== CONTROL DE SELECCIÓN DE CARTAS ==========
    private List<Integer> cartasSeleccionadas; // Índices de cartas seleccionadas
    private int cartaSeleccionadaUnica = -1; // Para BlackJack (1 carta)
    
    // ========== CONTROL DE TURNOS ==========
    private boolean jugadorSePlanto = false;
    private boolean dealerSePlanto = false;
    
    @FXML
    private void initialize() {
        cartasJugadorVisual = new ArrayList<>();
        cartasDealerVisual = new ArrayList<>();
        cartasSeleccionadas = new ArrayList<>();
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
        
        System.out.println("✓ Vista inicializada - Es turno del jugador");
    }
    
    // ========== ACTUALIZACIÓN DE ELEMENTOS DINÁMICOS ==========
    
    private void actualizarFondo() {
        int vida = controladorJuego.getJuego().getJugador().getVida();
        int estado = calcularEstado(vida);
        
        String rutaFondo = "/img/Juego/Mesa_Estado_" + estado + ".png";
        cargarImagen(fondoImageView, rutaFondo);
    }
    
    private void actualizarDealerGif() {
        int vida = controladorJuego.getJuego().getJugador().getVida();
        int estado = calcularEstado(vida);
        
        String rutaDealer = "/img/Dealer/Dealer_Estado_" + estado + ".gif";
        cargarImagen(dealerImageView, rutaDealer);
    }
    
    private void actualizarBarraVida() {
        Jugador jugador = controladorJuego.getJuego().getJugador();
        Dealer dealer = controladorJuego.getJuego().getDealer();
        
        int estadoVidaJugador = redondearHaciaArribaDivisor2(jugador.getVida());
        int estadoVidaDealer = redondearHaciaArribaDivisor2(dealer.getVida());
        
        String rutaVidaJugador = "/img/Vida/Jugador/player_health-bar_" + estadoVidaJugador + ".png";
        String rutaVidaDealer = "/img/Vida/Dealer/boss_health-bar_" + estadoVidaDealer + ".png";
        
        cargarImagen(vidaJugadorImageView, rutaVidaJugador);
        cargarImagen(vidaDealerImageView, rutaVidaDealer);
    }
    
    private int redondearHaciaArribaDivisor2(int valor) {
        if (valor % 2 == 0) {
            return valor;
        } else {
            return valor + 1;
        }
    }
    
    private int calcularEstado(int vida) {
        if (vida > 75) return 1;
        if (vida > 50) return 2;
        if (vida > 25) return 3;
        return 4;
    }
    
    // ========== GESTIÓN DE CARTAS ==========
    
    private void mostrarCartasJugador() {
        cartasJugadorBox.getChildren().clear();
        cartasJugadorVisual.clear();
        cartasSeleccionadas.clear();
        cartaSeleccionadaUnica = -1;
        
        List<Carta> cartas = controladorJuego.getJuego().getJugador().getMano().getCartas();
        
        for (int i = 0; i < cartas.size(); i++) {
            Carta carta = cartas.get(i);
            
            // Crear contenedor para la carta
            StackPane contenedor = new StackPane();
            ImageView cartaView = crearCartaView(carta, "jugador");
            contenedor.getChildren().add(cartaView);
            
            // Agregar interactividad
            final int indice = i;
            contenedor.setOnMouseClicked(e -> seleccionarCarta(indice, contenedor));
            contenedor.setStyle("-fx-cursor: hand;");
            
            cartasJugadorVisual.add(cartaView);
            cartasJugadorBox.getChildren().add(contenedor);
        }
    }
    
    private void mostrarCartasDealer() {
        cartasDealerBox.getChildren().clear();
        cartasDealerVisual.clear();
        
        List<Carta> cartas = controladorJuego.getJuego().getDealer().getMano().getCartas();
        boolean mostrarCartas = controladorJuego.getJuego().getRondasVision() > 0;
        
        for (Carta carta : cartas) {
            ImageView cartaView = crearCartaView(carta, "dealer");
            
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
    
    // ========== SELECCIÓN DE CARTAS ==========
    
    private void seleccionarCarta(int indice, StackPane contenedor) {
        if (controladorJuego.getJuego().getEstado() != EstadoJuego.TURNO_JUGADOR) {
            System.out.println("No es tu turno");
            return;
        }
        
        if (tipoJuego.equals("poker")) {
            seleccionarCartaPoker(indice, contenedor);
        } else {
            seleccionarCartaBlackjack(indice, contenedor);
        }
    }
    
    private void seleccionarCartaPoker(int indice, StackPane contenedor) {
        if (cartasSeleccionadas.contains(indice)) {
            // Deseleccionar
            cartasSeleccionadas.remove(Integer.valueOf(indice));
            contenedor.setStyle("-fx-cursor: hand; -fx-border-color: transparent;");
        } else {
            // Seleccionar (máximo 3)
            if (cartasSeleccionadas.size() < 3) {
                cartasSeleccionadas.add(indice);
                contenedor.setStyle("-fx-cursor: hand; -fx-border-color: yellow; -fx-border-width: 3;");
            } else {
                System.out.println("Solo puedes seleccionar máximo 3 cartas");
            }
        }
        
        System.out.println("Cartas seleccionadas: " + cartasSeleccionadas);
    }
    
    private void seleccionarCartaBlackjack(int indice, StackPane contenedor) {
        // Limpiar selección anterior
        if (cartaSeleccionadaUnica >= 0 && cartaSeleccionadaUnica < cartasJugadorBox.getChildren().size()) {
            StackPane anteriorContenedor = (StackPane) cartasJugadorBox.getChildren().get(cartaSeleccionadaUnica);
            anteriorContenedor.setStyle("-fx-cursor: hand; -fx-border-color: transparent;");
        }
        
        if (cartaSeleccionadaUnica == indice) {
            // Deseleccionar
            cartaSeleccionadaUnica = -1;
            contenedor.setStyle("-fx-cursor: hand; -fx-border-color: transparent;");
        } else {
            // Seleccionar nueva
            cartaSeleccionadaUnica = indice;
            contenedor.setStyle("-fx-cursor: hand; -fx-border-color: yellow; -fx-border-width: 3;");
        }
        
        System.out.println("Carta seleccionada (BlackJack): " + cartaSeleccionadaUnica);
    }
    
    // ========== EFECTOS VISUALES ==========
    
    private void aplicarEfectoTurno() {
        EstadoJuego estado = controladorJuego.getJuego().getEstado();
        
        if (estado == EstadoJuego.TURNO_JUGADOR) {
            restaurarBrilloCartas();
        } else {
            oscurecerCartasJugador();
        }
    }
    
    private void oscurecerCartasJugador() {
        for (ImageView cartaView : cartasJugadorVisual) {
            FadeTransition fade = new FadeTransition(Duration.seconds(0.5), cartaView);
            fade.setToValue(0.4);
            fade.play();
        }
    }
    
    private void restaurarBrilloCartas() {
        for (ImageView cartaView : cartasJugadorVisual) {
            FadeTransition fade = new FadeTransition(Duration.seconds(0.5), cartaView);
            fade.setToValue(1.0);
            fade.play();
        }
    }
    
    public void toggleVisibilidadCartasDealer(boolean visible) {
        for (ImageView cartaView : cartasDealerVisual) {
            cartaView.setVisible(visible);
        }
    }
    
    // ========== BOTONES DE ACCIÓN ==========

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
        if (controladorJuego.getJuego().getEstado() != EstadoJuego.TURNO_JUGADOR) {
            System.out.println("No es tu turno");
            return;
        }
        
        if (tipoJuego.equals("poker")) {
            cambiarCartasPoker();
        } else {
            pedirCartaBlackjack();
        }
    }
    
    @FXML
    private void onBotonInferiorClicked() {
        if (controladorJuego.getJuego().getEstado() != EstadoJuego.TURNO_JUGADOR) {
            System.out.println("No es tu turno");
            return;
        }
        
        plantarse();
    }
    
    // ========== LÓGICA DE POKER ==========
    
    private void cambiarCartasPoker() {
        if (cartasSeleccionadas.isEmpty()) {
            System.out.println("No has seleccionado ninguna carta para cambiar");
            return;
        }
        
        System.out.println("→ Cambiando " + cartasSeleccionadas.size() + " cartas (Poker)");
        
        // Cambiar cartas usando el controlador
        boolean exito = controladorJuego.cambiarCartasJugador(cartasSeleccionadas);
        
        if (exito) {
            jugadorSePlanto = false;
            actualizarVista();
            
            // Pasar al turno del dealer
            ejecutarTurnoDealer();
        }
    }
    
    // ========== LÓGICA DE BLACKJACK ==========
    
    private void pedirCartaBlackjack() {
        System.out.println("→ Pidiendo carta (BlackJack)");
        
        BlackJack bj = (BlackJack) controladorJuego.getJuego();
        Jugador jugador = bj.getJugador();
        
        // Pedir una carta
        bj.pedirCarta(jugador);
        
        jugadorSePlanto = false;
        actualizarVista();
        
        // Verificar si el jugador se pasó
        int valorMano = bj.calcularValorMano(jugador.getMano());
        System.out.println("Valor actual de tu mano: " + valorMano);
        
        if (valorMano > 21) {
            System.out.println("¡Te pasaste de 21!");
            finalizarRonda();
        }
    }
    
    // ========== PLANTARSE ==========
    
    private void plantarse() {
        System.out.println("→ Te plantas");
        jugadorSePlanto = true;
        
        // Pasar al turno del dealer
        ejecutarTurnoDealer();
    }
    
    // ========== TURNO DEL DEALER ==========
    
    private void ejecutarTurnoDealer() {
        System.out.println("\n=== TURNO DEL DEALER ===");
        
        // Cambiar estado
        controladorJuego.getJuego().cambiarEstado(EstadoJuego.TURNO_DEALER);
        aplicarEfectoTurno();
        
        // Esperar 1.5 segundos antes de que el dealer actúe
        PauseTransition pausa = new PauseTransition(Duration.seconds(1.5));
        pausa.setOnFinished(e -> {
            
            if (tipoJuego.equals("poker")) {
                ejecutarTurnoDealerPoker();
            } else {
                ejecutarTurnoDealerBlackjack();
            }
            
        });
        pausa.play();
    }
    
    private void ejecutarTurnoDealerPoker() {
        System.out.println("→ Dealer decide si cambiar cartas...");
        
        // El dealer decide automáticamente según su lógica
        controladorJuego.procesarTurnoDealer();
        
        dealerSePlanto = true;
        actualizarVista();
        
        // Finalizar ronda
        PauseTransition pausa = new PauseTransition(Duration.seconds(1.0));
        pausa.setOnFinished(e -> finalizarRonda());
        pausa.play();
    }
    
    private void ejecutarTurnoDealerBlackjack() {
        BlackJack bj = (BlackJack) controladorJuego.getJuego();
        Dealer dealer = bj.getDealer();
        
        int valorDealer = bj.calcularValorMano(dealer.getMano());
        System.out.println("Valor actual del dealer: " + valorDealer);
        
        // Dealer pide carta si tiene 16 o menos
        if (valorDealer <= 16 && !jugadorSePlanto) {
            System.out.println("→ Dealer pide carta");
            bj.pedirCarta(dealer);
            actualizarVista();
            
            valorDealer = bj.calcularValorMano(dealer.getMano());
            System.out.println("Nuevo valor del dealer: " + valorDealer);
            
            if (valorDealer > 21) {
                System.out.println("¡El dealer se pasó de 21!");
                dealerSePlanto = true;
                finalizarRonda();
                return;
            }
            
            // Volver al turno del jugador
            volverATurnoJugador();
            
        } else {
            // Dealer se planta
            System.out.println("→ Dealer se planta");
            dealerSePlanto = true;
            
            // Si ambos se plantaron, finalizar ronda
            if (jugadorSePlanto && dealerSePlanto) {
                finalizarRonda();
            } else {
                volverATurnoJugador();
            }
        }
    }
    
    private void volverATurnoJugador() {
        System.out.println("\n=== TURNO DEL JUGADOR ===");
        controladorJuego.getJuego().cambiarEstado(EstadoJuego.TURNO_JUGADOR);
        aplicarEfectoTurno();
    }
    
    // ========== FINALIZAR RONDA ==========
    
    private void finalizarRonda() {
        System.out.println("\n========== FINALIZANDO RONDA ==========");
        
        Personaje ganador = controladorJuego.getJuego().evaluarGanador();
        
        Jugador jugador = controladorJuego.getJuego().getJugador();
        Dealer dealer = controladorJuego.getJuego().getDealer();
        
        if (ganador == null) {
            System.out.println("→ EMPATE - No hay cambios");
        } else if (ganador == jugador) {
            System.out.println("→ ¡GANASTE LA RONDA!");
            dealer.perderVida(2);
            System.out.println("Vida del Dealer: " + dealer.getVida());
        } else {
            System.out.println("→ PERDISTE LA RONDA");
            jugador.perderVida(2);
            System.out.println("Vida del Jugador: " + jugador.getVida());
        }
        
        // Actualizar barras de vida
        actualizarBarraVida();
        actualizarFondo();
        actualizarDealerGif();
        
        // Verificar fin del juego
        if (!jugador.estaVivo() || !dealer.estaVivo()) {
            finalizarJuego();
            return;
        }
        
        // Reiniciar para nueva ronda
        PauseTransition pausa = new PauseTransition(Duration.seconds(2.5));
        pausa.setOnFinished(e -> iniciarNuevaRonda());
        pausa.play();
    }
    
    private void iniciarNuevaRonda() {
        System.out.println("\n========== NUEVA RONDA ==========");
        
        // Resetear estados
        jugadorSePlanto = false;
        dealerSePlanto = false;
        
        // Iniciar nueva ronda
        controladorJuego.iniciarRonda();
        
        // Actualizar vista
        actualizarVista();
        
        System.out.println("✓ Nueva ronda iniciada - Es turno del jugador");
    }
    
    private void finalizarJuego() {
        System.out.println("\n========== FIN DEL JUEGO ==========");
        
        Jugador jugador = controladorJuego.getJuego().getJugador();
        Dealer dealer = controladorJuego.getJuego().getDealer();
        
        if (!jugador.estaVivo()) {
            System.out.println("→ EL DEALER HA GANADO");
            // TODO: Mostrar pantalla de Game Over
        } else {
            System.out.println("→ ¡HAS GANADO!");
            // TODO: Mostrar pantalla de Victoria
        }
    }
    
    // ========== ACTUALIZACIÓN COMPLETA DE LA VISTA ==========
    
    public void actualizarVista() {
        actualizarFondo();
        actualizarDealerGif();
        actualizarBarraVida();
        mostrarCartasJugador();
        mostrarCartasDealer();
        aplicarEfectoTurno();
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