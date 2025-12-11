package com.ingunal.brokendeal.beta;

import javafx.application.Application;
import javafx.stage.Stage;

import com.ingunal.brokendeal.beta.controller.*;
import com.ingunal.brokendeal.beta.dao.DialogoDAO;
import com.ingunal.brokendeal.beta.model.vo.Baraja;
import com.ingunal.brokendeal.beta.model.vo.juego.Juego;
import com.ingunal.brokendeal.beta.model.vo.juego.Poker;
import com.ingunal.brokendeal.beta.model.vo.personajes.Dealer;
import com.ingunal.brokendeal.beta.model.vo.personajes.Jugador;

/**
 * JavaFX App
 */
public class App extends Application {
    
    @Override
    public void start(Stage stage) {

        // ----------- Inicializar backend (tu lógica) ------------
        Baraja baraja = new Baraja();
        Jugador jugador = new Jugador();
        Dealer dealer = new Dealer();
        Juego juego = new Poker(jugador, dealer, baraja, 1); // o Blackjack luego

        ControladorCartas ctrlCartas = new ControladorCartas(baraja);
        ControladorDialogo ctrlDialogo = new ControladorDialogo();
        DialogoDAO dialogoDAO = new DialogoDAO();

        ControladorJuego controladorJuego =
            new ControladorJuego(juego, ctrlCartas, ctrlDialogo, dialogoDAO);

        // ----------- Scene Manager ------------------------------
        SceneManager sceneManager = new SceneManager(stage, controladorJuego);

        // ----------- Mostrar menú inicial ------------------------
        sceneManager.mostrarMenu();
    }

    public static void main(String[] args) {
        launch();
    }
}