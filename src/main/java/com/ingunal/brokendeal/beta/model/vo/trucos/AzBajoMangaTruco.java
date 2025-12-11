/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ingunal.brokendeal.beta.model.vo.trucos;

import com.ingunal.brokendeal.beta.model.vo.Carta;
import com.ingunal.brokendeal.beta.model.vo.personajes.Jugador;
import com.ingunal.brokendeal.beta.model.vo.juego.Juego;

/**
 *
 * @author andre
 */
public class AzBajoMangaTruco extends Truco {

    private Carta cartaGuardada;

    public AzBajoMangaTruco(Carta cartaInicial) {
        super(1, 0.5); // 1 uso, 50% base de ser descubierto
        this.cartaGuardada = cartaInicial;
    }

    public Carta getCartaGuardada() {
        return cartaGuardada;
    }
    
    // ← NUEVO: Setter para asignar la carta después
    public void setCartaGuardada(Carta carta) {
        this.cartaGuardada = carta;
    }

    @Override
    public boolean activar(Jugador jugador, Juego juego) {

        if (usosRestantes <= 0) return false;
        
        // Validar que tengamos una carta guardada
        if (cartaGuardada == null) {
            System.err.println("ERROR: No hay carta guardada en el truco");
            return false;
        }

        // Recalcular probabilidad según vida
        recalcularProbabilidad(jugador);

        boolean descubierto = Math.random() < probabilidadDescubierto;

        if (descubierto) {
            jugador.perderVida(1);          // castigo por ser atrapado
            usosRestantes = 0;
            jugador.resetIndiceCartaSeleccionada(); // seguridad
            return false;
        }

        // Índice elegido por la vista
        int indice = jugador.getIndiceCartaSeleccionadaParaCambiar();

        if (indice < 0 || indice >= jugador.getMano().getCartas().size()) {
            return false; // inválido → no consume uso
        }

        // Reemplazo de carta real
        intercambiarConMano(jugador, indice);

        usosRestantes--;

        jugador.resetIndiceCartaSeleccionada(); // siempre limpiar

        return true;
    }

    /**
     * Reemplaza la carta del jugador por la carta guardada.
     */
    private void intercambiarConMano(Jugador jugador, int indice) {
        jugador.getMano().getCartas().set(indice, cartaGuardada);
    }

    /**
     * Ajusta la probabilidad de ser descubierto según vida.
     */
    public void recalcularProbabilidad(Jugador jugador) {
        int vida = jugador.getVida();

        if (vida >= 60)       probabilidadDescubierto = 0.50;
        else if (vida >= 30)  probabilidadDescubierto = 0.60;
        else                  probabilidadDescubierto = 0.75;
    }
}