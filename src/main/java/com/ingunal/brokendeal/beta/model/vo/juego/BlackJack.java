/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ingunal.brokendeal.beta.model.vo.juego;

import com.ingunal.brokendeal.beta.model.vo.personajes.Personaje;
import com.ingunal.brokendeal.beta.model.vo.personajes.Jugador;
import com.ingunal.brokendeal.beta.model.vo.personajes.Dealer;
import com.ingunal.brokendeal.beta.model.vo.Baraja;
import com.ingunal.brokendeal.beta.model.vo.Mano;
import com.ingunal.brokendeal.beta.model.vo.Carta;

/**
 *
 * @author andre
 */
public class BlackJack extends Juego {

    private int valorMeta;

    // Constructor vacío
    public BlackJack() {
        super();
        this.valorMeta = 21;
    }

    // Constructor completo
    public BlackJack(Jugador jugador, Dealer dealer, Baraja baraja, int valorMeta) {
        super(jugador, dealer, baraja);
        this.valorMeta = valorMeta;
    }

    // Pide carta para cualquier personaje
    public void pedirCarta(Personaje personaje) {
        if (baraja != null && personaje != null) {
            personaje.getMano().agregarCarta(baraja.repartirCarta());
        }
    }

    // Evaluación del ganador de la ronda
    @Override
    public Personaje evaluarGanador() {
        int valorJugador = calcularValorMano(jugador.getMano());
        int valorDealer = calcularValorMano(dealer.getMano());

        // Si los dos se pasan, empate
        if (valorJugador > valorMeta && valorDealer > valorMeta) {
            return null;
        }

        // Si uno se pasa, gana el otro
        if (valorJugador > valorMeta) return dealer;
        if (valorDealer > valorMeta) return jugador;

        // Ambos dentro de 21: gana el más cercano
        int difJugador = valorMeta - valorJugador;
        int difDealer = valorMeta - valorDealer;

        if (difJugador == difDealer) {
            return null; // Empate, la ronda sigue
        }

        return difJugador < difDealer ? jugador : dealer;
    }

    public int getValorMeta() {
        return valorMeta;
    }

    public void setValorMeta(int valorMeta) {
        this.valorMeta = valorMeta;
    }
    
    public int calcularValorMano(Mano mano) {
        int suma = 0;
        int cantidadAses = 0;

        for (Carta c : mano.getCartas()) {
            int valor = c.getValorNumerico();

            // As detectado (valor 11 por defecto)
            if (valor == 11) {
                cantidadAses++;
            }

            suma += valor;
        }

        // Aplicar regla del As
        // Si se pasa y hay Ases, cada As se convierte en 1 hasta que deje de pasarse
        while (suma > 21 && cantidadAses > 0) {
            suma -= 10; // convertir un As de 11, 1 (restar 10)
            cantidadAses--;
        }

        return suma;
    }
}