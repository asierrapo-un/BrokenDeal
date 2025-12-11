/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ingunal.brokendeal.beta;

import com.ingunal.brokendeal.beta.controller.AsBajoMangaController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.ingunal.brokendeal.beta.controller.MenuController;
import com.ingunal.brokendeal.beta.controller.IntroController;
import com.ingunal.brokendeal.beta.controller.ControladorJuego;
import com.ingunal.brokendeal.beta.model.vo.Baraja;
import com.ingunal.brokendeal.beta.model.vo.juego.Juego;
import com.ingunal.brokendeal.beta.model.vo.personajes.Dealer;
import com.ingunal.brokendeal.beta.model.vo.personajes.Jugador;

/**
 *
 * @author andre
 */
public class SceneManager {
    private Stage stage;
    private ControladorJuego controladorJuego;

    public SceneManager(Stage stage, ControladorJuego controladorJuego) {
        this.stage = stage;
        this.controladorJuego = controladorJuego;
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

    // -------------------- ESCENAS --------------------

    /** Carga menú principal */
    public void mostrarMenu() {
        MenuController controller =
            cargarVista("/view/menu_view.fxml", 1024, 720);

        if (controller != null) {
            controller.setSceneManager(this);       // ← enlazamos
            controller.setControladorJuego(controladorJuego); // ← inyección
        }
    }

    /** Cargar Poker */
    public void mostrarPoker() {
        // futuro FXML del juego Poker
        System.out.println("→ Cambiar escena: Poker");

        controladorJuego.iniciarJuego();  // Aquí entra tu lógica real
        // cargar la vista de juego cuando esté lista
    }

    /** Cargar Blackjack */
    public void mostrarBlackjack() {
        System.out.println("→ Cambiar escena: Blackjack");

        controladorJuego.iniciarJuego();
        // misma idea: se cargará FXML del juego cuando esté lista la vista
    }

    /** Cerrar juego */
    public void salir() {
        stage.close();
    }
    
    
    
    public void mostrarIntro(String game) {
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
            controller.startIntro(game);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    
    public void mostrarAsBajoManga() {
        
    }
}
