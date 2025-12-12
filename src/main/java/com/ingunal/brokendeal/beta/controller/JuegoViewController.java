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
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.geometry.Pos;

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
    @FXML private StackPane rootStackPane; // Contenedor raíz para el diálogo
    
    // ========== DEPENDENCIAS ==========
    private SceneManager sceneManager;
    private ControladorJuego controladorJuego;
    private String tipoJuego;
    
    // ========== ESTADO VISUAL ==========
    private List<ImageView> cartasJugadorVisual;
    private List<ImageView> cartasDealerVisual;
    
    // ========== CONTROL DE SELECCIÓN DE CARTAS ==========
    private List<Integer> cartasSeleccionadas;
    private int cartaSeleccionadaUnica = -1;
    
    // ========== CONTROL DE TURNOS ==========
    private boolean jugadorSePlanto = false;
    private boolean dealerSePlanto = false;
    
    // ========== SISTEMA DE DIÁLOGOS ==========
    private VBox dialogoBox;
    private Label dialogoLabel;
    private boolean dialogoActivo = false;
    
    @FXML
    private void initialize() {
        cartasJugadorVisual = new ArrayList<>();
        cartasDealerVisual = new ArrayList<>();
        cartasSeleccionadas = new ArrayList<>();
        
        // NO crear el diálogo aquí - se creará después cuando tengamos acceso al StackPane
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
        // CRÍTICO: Crear el diálogo DESPUÉS de que la vista esté cargada
        crearSistemaDialogo();
        
        actualizarFondo();
        actualizarDealerGif();
        actualizarBarraVida();
        cargarBotonesSegunJuego();
        
        mostrarCartasJugador();
        mostrarCartasDealer();
        
        aplicarEfectoTurno();
        
        System.out.println("✓ Vista inicializada - Es turno del jugador");
    }
    
    // ========== SISTEMA DE DIÁLOGO ==========
    
    private void crearSistemaDialogo() {
        // Crear fondo semi-transparente con bordes redondeados
        Rectangle fondo = new Rectangle(600, 200);
        fondo.setFill(Color.rgb(123, 72, 54, 0.50));
        fondo.setArcWidth(20);
        fondo.setArcHeight(20);
        
        // Crear label para el texto
        dialogoLabel = new Label("Haz clic para continuar a la siguiente ronda");
        dialogoLabel.setTextFill(Color.WHITE);
        dialogoLabel.setWrapText(true);
        dialogoLabel.setMaxWidth(550);
        dialogoLabel.setAlignment(Pos.CENTER);
        dialogoLabel.setStyle("-fx-font-size: 20px; -fx-padding: 30;");
        
        // Contenedor del diálogo con el fondo y el texto superpuestos
        dialogoBox = new VBox();
        dialogoBox.setAlignment(Pos.CENTER);
        dialogoBox.getChildren().addAll(fondo, dialogoLabel);
        dialogoBox.setVisible(false);
        StackPane.setAlignment(dialogoBox, Pos.CENTER);
        
        // Evento de clic para cerrar diálogo
        dialogoBox.setOnMouseClicked(e -> cerrarDialogo());
        dialogoBox.setStyle("-fx-cursor: hand;");
        
        // CRÍTICO: Agregar el dialogoBox al StackPane raíz
        if (rootStackPane != null) {
            rootStackPane.getChildren().add(dialogoBox);
            System.out.println("✓ Sistema de diálogo creado y agregado al StackPane");
        } else {
            System.err.println("ERROR: rootStackPane es null - no se puede agregar el diálogo");
        }
    }
    
    private void mostrarDialogo(String texto) {
        dialogoLabel.setText(texto);
        dialogoBox.setVisible(true);
        dialogoActivo = true;
        
        // Animación de aparición
        FadeTransition fade = new FadeTransition(Duration.seconds(0.3), dialogoBox);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }
    
    private void cerrarDialogo() {
        if (!dialogoActivo) return;
        
        // Animación de desaparición
        FadeTransition fade = new FadeTransition(Duration.seconds(0.3), dialogoBox);
        fade.setFromValue(1);
        fade.setToValue(0);
        fade.setOnFinished(e -> {
            dialogoBox.setVisible(false);
            dialogoActivo = false;
            iniciarNuevaRonda();
        });
        fade.play();
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
        
        System.out.println("DEBUG: Mostrando " + cartas.size() + " cartas del jugador");
        
        for (int i = 0; i < cartas.size(); i++) {
            Carta carta = cartas.get(i);
            
            StackPane contenedor = new StackPane();
            ImageView cartaView = crearCartaView(carta, "jugador");
            contenedor.getChildren().add(cartaView);
            
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
        
        System.out.println("DEBUG: Mostrando " + cartas.size() + " cartas del dealer");
        
        for (Carta carta : cartas) {
            ImageView cartaView = crearCartaView(carta, "dealer");
            
            if (!mostrarCartas) {
                cartaView.setVisible(false);
            }
            
            cartasDealerVisual.add(cartaView);
            cartasDealerBox.getChildren().add(cartaView);
        }
    }
    
    private ImageView crearCartaView(Carta carta, String personaje) {
        ImageView imgView = new ImageView();
        
        String ruta = "/" + carta.getImgRuta();
        cargarImagen(imgView, ruta);
        
        if(personaje.equals("dealer")){
            imgView.setFitWidth(50);
            imgView.setFitHeight(80);
        } else {
            imgView.setFitWidth(90);
            imgView.setFitHeight(150);
        }
        imgView.setPreserveRatio(true);
        imgView.setSmooth(false);
        
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
            cartasSeleccionadas.remove(Integer.valueOf(indice));
            contenedor.setStyle("-fx-cursor: hand; -fx-border-color: transparent;");
        } else {
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
        if (cartaSeleccionadaUnica >= 0 && cartaSeleccionadaUnica < cartasJugadorBox.getChildren().size()) {
            StackPane anteriorContenedor = (StackPane) cartasJugadorBox.getChildren().get(cartaSeleccionadaUnica);
            anteriorContenedor.setStyle("-fx-cursor: hand; -fx-border-color: transparent;");
        }
        
        if (cartaSeleccionadaUnica == indice) {
            cartaSeleccionadaUnica = -1;
            contenedor.setStyle("-fx-cursor: hand; -fx-border-color: transparent;");
        } else {
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
        
        boolean exito = controladorJuego.cambiarCartasJugador(cartasSeleccionadas);
        
        if (exito) {
            jugadorSePlanto = false;
            actualizarVista();
            
            ejecutarTurnoDealer();
        }
    }
    
    // ========== LÓGICA DE BLACKJACK ==========
    
    private void pedirCartaBlackjack() {
        System.out.println("→ Pidiendo carta (BlackJack)");
        
        BlackJack bj = (BlackJack) controladorJuego.getJuego();
        Jugador jugador = bj.getJugador();
        
        bj.pedirCarta(jugador);
        
        jugadorSePlanto = false;
        actualizarVista();
        
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
        
        ejecutarTurnoDealer();
    }
    
    // ========== TURNO DEL DEALER ==========
    
    private void ejecutarTurnoDealer() {
        System.out.println("\n=== TURNO DEL DEALER ===");
        
        controladorJuego.getJuego().cambiarEstado(EstadoJuego.TURNO_DEALER);
        aplicarEfectoTurno();
        
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
        
        controladorJuego.procesarTurnoDealer();
        
        dealerSePlanto = true;
        actualizarVista();
        
        PauseTransition pausa = new PauseTransition(Duration.seconds(1.0));
        pausa.setOnFinished(e -> finalizarRonda());
        pausa.play();
    }
    
    private void ejecutarTurnoDealerBlackjack() {
        BlackJack bj = (BlackJack) controladorJuego.getJuego();
        Dealer dealer = bj.getDealer();
        
        int valorDealer = bj.calcularValorMano(dealer.getMano());
        System.out.println("Valor actual del dealer: " + valorDealer);
        
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
            
            volverATurnoJugador();
            
        } else {
            System.out.println("→ Dealer se planta");
            dealerSePlanto = true;
            
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
        
        actualizarBarraVida();
        actualizarFondo();
        actualizarDealerGif();
        
        if (!jugador.estaVivo() || !dealer.estaVivo()) {
            finalizarJuego();
            return;
        }
        
        // Mostrar diálogo entre rondas
        PauseTransition pausa = new PauseTransition(Duration.seconds(2.0));
        pausa.setOnFinished(e -> mostrarDialogo("Haz clic para continuar a la siguiente ronda"));
        pausa.play();
    }
    
    private void iniciarNuevaRonda() {
        System.out.println("\n========== NUEVA RONDA ==========");
        
        jugadorSePlanto = false;
        dealerSePlanto = false;
        
        // CRÍTICO: Restaurar la baraja antes de iniciar la nueva ronda
        System.out.println("→ Restaurando baraja (cartas restantes: " + 
                         controladorJuego.getJuego().getBaraja().cartasRestantes() + ")");
        controladorJuego.getJuego().getBaraja().reiniciarBaraja();
        
        // Iniciar nueva ronda con el controlador
        controladorJuego.iniciarRonda();
        
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