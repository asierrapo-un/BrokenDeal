/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ingunal.brokendeal.beta.model.vo.juego;

import com.ingunal.brokendeal.beta.model.vo.personajes.Dealer;
import com.ingunal.brokendeal.beta.model.vo.personajes.Jugador;
import com.ingunal.brokendeal.beta.model.vo.Baraja;
import com.ingunal.brokendeal.beta.model.vo.trucos.Truco;
import com.ingunal.brokendeal.beta.model.vo.personajes.Personaje;

/**
 *
 * @author andre
 */
public abstract class Juego {
    protected Jugador jugador;       // Composición
    protected Dealer dealer;         // Composición
    protected Baraja baraja;         // Asociación
    protected int rondasVision;
    protected int numeroRonda;
    protected EstadoJuego estado;

    // ---------------- CONSTRUCTORES ---------------- //

    public Juego() {
        this.jugador = new Jugador();
        this.dealer = new Dealer();
        this.numeroRonda = 1;
        this.estado = EstadoJuego.INICIO;
    }

    public Juego(Jugador jugador, Dealer dealer, Baraja baraja) {
        this.jugador = jugador;
        this.dealer = dealer;
        this.baraja = baraja;
        this.numeroRonda = 1;
        this.estado = EstadoJuego.INICIO;
    }

    // ---------------- LÓGICA BASE DE JUEGO ---------------- //

    public void iniciarRonda() {
        estado = EstadoJuego.TURNO_JUGADOR;

        jugador.getMano().limpiar();
        dealer.getMano().limpiar();
    }

    public void finalizarRonda() {
        Personaje ganador = evaluarGanador();

        // 1. Empate → nada cambia
        if (ganador == null) {
            avanzarRonda();
            return;
        }

        // 2. Ganó jugador → dealer pierde vida
        if (ganador == jugador) {
            dealer.perderVida(5);
        }
        // 3. Ganó dealer → jugador pierde vida y cordura
        else {
            jugador.perderVida(5);
            jugador.perderCordura(3);
        }

        // 4. Se acabó el juego
        if (!jugador.estaVivo() || !dealer.estaVivo()) {
            estado = EstadoJuego.FINALIZADO;
            return;
        }

        // 5. Avanzar a la siguiente ronda
        avanzarRonda();
    }

    public void avanzarRonda() {
        numeroRonda++;
        estado = EstadoJuego.ENTRE_RONDAS;
    }

    public boolean esFinDelJuego() {
        return !jugador.estaVivo() || !dealer.estaVivo();
    }

    public void cambiarEstado(EstadoJuego nuevo) {
        this.estado = nuevo;
    }

    // ---------------- TRUCOS Y EFECTOS ---------------- //

    public boolean permitirUsoTruco(Truco truco) {
        return truco.getUsosRestantes() > 0;
    }

    public void aplicarTruco(Truco truco) {
        truco.activar(jugador, this);
    }

    // ---------------- MÉTODO ABSTRACTO ---------------- //

    protected abstract Personaje evaluarGanador();

    // ---------------- GETTERS Y SETTERS ---------------- //
    
    public int getRondasVision() { return rondasVision; }
    public void setRondasVision(int rondasVision) {
        this.rondasVision = rondasVision;
    }

    public Jugador getJugador() { return jugador; }
    public Dealer getDealer() { return dealer; }
    public Baraja getBaraja() { return baraja; }
    public int getNumeroRonda() { return numeroRonda; }
    public EstadoJuego getEstado() { return estado; }
}
