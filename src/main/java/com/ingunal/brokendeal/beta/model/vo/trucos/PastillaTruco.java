/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ingunal.brokendeal.beta.model.vo.trucos;

import com.ingunal.brokendeal.beta.model.vo.personajes.Jugador;
import com.ingunal.brokendeal.beta.model.vo.juego.Juego;

/**
 *
 * @author andre
 */
public class PastillaTruco extends Truco{
    private int rondasRestantes;

    public PastillaTruco() {
        super(1, 0.0); // no tiene probabilidad de ser descubierto
        this.rondasRestantes = 3; // efecto dura 3 rondas
    }

    @Override
    public boolean activar(Jugador jugador, Juego juego) {

        if (usosRestantes <= 0) return false;

        activarEfecto(jugador, juego);
        usosRestantes--;

        return true;
    }

    private void activarEfecto(Jugador jugador, Juego juego) {
        // Subir cordura +25
        jugador.ganarCordura(25);

        // Hacer visibles las cartas del dealer por 3 rondas
        juego.setRondasVision(3);
    }

    /**
     * Llamado al final de cada ronda desde ControladorJuego
     */
    public void reducirDuracion(Juego juego) {
        if (juego.getRondasVision() > 0) {
            juego.setRondasVision(juego.getRondasVision() - 1);
        }
    }
}
