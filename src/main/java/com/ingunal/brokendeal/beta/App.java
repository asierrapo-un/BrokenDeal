package com.ingunal.brokendeal.beta;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {
    
    @Override
    public void start(Stage stage) {
        // Solo crear el SceneManager
        // Él se encargará de crear los objetos cuando el usuario elija el juego
        SceneManager sceneManager = new SceneManager(stage);
        
        // Mostrar menú inicial
        sceneManager.mostrarMenu();
    }

    public static void main(String[] args) {
        launch();
    }
}