/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ingunal.brokendeal.beta;

import com.ingunal.brokendeal.beta.controller.*;
import com.ingunal.brokendeal.beta.dao.CartaDAO;
import com.ingunal.brokendeal.beta.dao.DialogoDAO;
import com.ingunal.brokendeal.beta.model.vo.Baraja;
import com.ingunal.brokendeal.beta.model.vo.Carta;
import com.ingunal.brokendeal.beta.model.vo.juego.Juego;
import com.ingunal.brokendeal.beta.model.vo.juego.Poker;
import com.ingunal.brokendeal.beta.model.vo.juego.BlackJack;
import com.ingunal.brokendeal.beta.model.vo.personajes.Dealer;
import com.ingunal.brokendeal.beta.model.vo.personajes.Jugador;
import com.ingunal.brokendeal.beta.model.vo.trucos.AzBajoMangaTruco;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

/**
 *
 * @author andre
 */
public class SceneManager {
    private Stage stage;
    
    // Objetos del juego (se crean cuando el usuario elige)
    private String tipoJuego; // "poker" o "blackjack"
    private Jugador jugador;
    private Dealer dealer;
    private Baraja baraja;
    private Juego juego;
    private ControladorJuego controladorJuego;
    
    // Controladores auxiliares
    private ControladorCartas controladorCartas;
    private ControladorDialogo controladorDialogo;
    private DialogoDAO dialogoDAO;

    public SceneManager(Stage stage) {
        this.stage = stage;
    }

    // -------------------- MÉTODO BASE --------------------
    private <T> T cargarVista(String rutaFXML, int width, int height) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
            Parent root = loader.load();

            Scene scene = new Scene(root, width, height);
            stage.setScene(scene);
            stage.show();

            return loader.getController();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // -------------------- INICIALIZACIÓN DEL JUEGO --------------------
    
    /**
     * Crea todos los objetos necesarios según el tipo de juego elegido
     */
    private void inicializarJuego(String tipoJuego) {
        this.tipoJuego = tipoJuego;
        
        // 1. Cargar cartas desde JSON
        CartaDAO cartaDAO = new CartaDAO();
        List<Carta> cartasCargadas = cartaDAO.cargarCartas();
        
        // 2. Crear baraja con las cartas
        baraja = new Baraja(cartasCargadas);
        
        // 3. Crear jugador y dealer
        jugador = new Jugador("Jugador", 100, 100);
        dealer = new Dealer("Dealer", 100, 0);
        
        // 4. Crear el truco "As Bajo la Manga" SIN carta (se asignará después)
        AzBajoMangaTruco azTruco = new AzBajoMangaTruco(null);
        jugador.agregarTruco(azTruco);
        
        // 5. Crear el juego según el tipo
        if (tipoJuego.equals("poker")) {
            juego = new Poker(jugador, dealer, baraja, 1);
        } else {
            juego = new BlackJack(jugador, dealer, baraja, 21);
        }
        
        // 6. Crear controladores
        controladorCartas = new ControladorCartas(baraja);
        controladorDialogo = new ControladorDialogo();
        dialogoDAO = new DialogoDAO();
        
        controladorJuego = new ControladorJuego(
            juego, 
            controladorCartas, 
            controladorDialogo, 
            dialogoDAO
        );
        
        System.out.println("✓ Juego inicializado: " + tipoJuego);
    }

    // -------------------- ESCENAS --------------------

    /** Carga menú principal */
    public void mostrarMenu() {
        MenuController controller =
            cargarVista("/view/menu_view.fxml", 1024, 720);

        if (controller != null) {
            controller.setSceneManager(this);
        }
    }

    /** Mostrar intro (AHORA recibe el tipo de juego y crea los objetos) */
    public void mostrarIntro(String tipoJuego) {
        // PRIMERO: Inicializar todos los objetos del juego
        inicializarJuego(tipoJuego);
        
        // LUEGO: Mostrar la intro
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/intro_view.fxml"));
            Parent root = loader.load();

            IntroController controller = loader.getController();
            controller.setSceneManager(this);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setWidth(1024);
            stage.setHeight(720);
            stage.show();

            // Iniciar intro (música + eventos)
            controller.startIntro(tipoJuego);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Mostrar selección de "As Bajo la Manga" */
    public void mostrarAsBajoManga() {
        AsBajoMangaController controller =
            cargarVista("/view/as_bajo_manga.fxml", 1024, 720);

        if (controller != null) {
            controller.setSceneManager(this);
            controller.setJugador(jugador);
            controller.setBaraja(baraja);
            controller.inicializar(); // Cargar las 4 cartas As
        }
    }

    /** Cargar Poker */
    public void mostrarPoker() {
        System.out.println("→ Iniciando juego de Poker...");
        controladorJuego.iniciarJuego();
        // TODO: cargar la vista de juego cuando esté lista
    }

    /** Cargar Blackjack */
    public void mostrarBlackjack() {
        System.out.println("→ Iniciando juego de Blackjack...");
        controladorJuego.iniciarJuego();
        // TODO: cargar la vista de juego cuando esté lista
    }

    /** Cerrar juego */
    public void salir() {
        stage.close();
    }
    
    // -------------------- GETTERS --------------------
    
    public String getTipoJuego() {
        return tipoJuego;
    }
    
    public ControladorJuego getControladorJuego() {
        return controladorJuego;
    }
}