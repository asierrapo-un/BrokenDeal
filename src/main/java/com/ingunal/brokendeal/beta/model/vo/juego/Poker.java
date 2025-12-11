/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ingunal.brokendeal.beta.model.vo.juego;

import com.ingunal.brokendeal.beta.model.vo.personajes.Jugador;
import com.ingunal.brokendeal.beta.model.vo.personajes.Dealer;
import com.ingunal.brokendeal.beta.model.vo.Baraja;
import com.ingunal.brokendeal.beta.model.vo.personajes.Personaje;
import com.ingunal.brokendeal.beta.model.vo.Mano;
import com.ingunal.brokendeal.beta.model.vo.Carta;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author andre
 */
public class Poker extends Juego {
    private int cambiosPermitidos;

    // Constructor vacío
    public Poker() {
        super();
        this.cambiosPermitidos = 1;
    }

    // Constructor completo
    public Poker(Jugador jugador, Dealer dealer, Baraja baraja, int cambiosPermitidos) {
        super(jugador, dealer, baraja);
        this.cambiosPermitidos = cambiosPermitidos;
    }

    // Control de cambios
    public boolean permitirCambios(Personaje personaje) {
        return cambiosPermitidos > 0;
    }

    // Método principal para evaluar el ganador
    @Override
    public Personaje evaluarGanador() {
        ResultadoPoker resJugador = evaluarManoPoker(jugador.getMano());
        ResultadoPoker resDealer = evaluarManoPoker(dealer.getMano());

        int comparacion = resJugador.compareTo(resDealer);

        if (comparacion > 0) return jugador;
        if (comparacion < 0) return dealer;
        return null; // empate exacto, ronda sin cambios
    }

    // Aanalizar una mano de Poker
    public ResultadoPoker evaluarManoPoker(Mano mano) {

        List<Carta> cartas = mano.getCartas().stream()
            .sorted((a, b) -> b.getValorNumerico() - a.getValorNumerico())
            .collect(Collectors.toList());

        // Lista solo con valores
        List<Integer> valores = cartas.stream()
                .map(Carta::getValorNumerico)
                .collect(Collectors.toList());

        // Lista de palos
        List<String> palos = cartas.stream()
                .map(Carta::getSimbolo)
                .collect(Collectors.toList());

        // Map con frecuencia de valores
        Map<Integer, Long> frecuencia = valores.stream()
                .collect(Collectors.groupingBy(v -> v, Collectors.counting()));

        // Detecta color
        boolean esColor = palos.stream()
                .distinct().count() == 1;

        // Detecta escalera
        boolean esEscalera = true;
        for (int i = 0; i < valores.size() - 1; i++) {
            if (valores.get(i) - 1 != valores.get(i + 1)) {
                esEscalera = false;
                break;
            }
        }

        // Lista de frecuencias ordenada (ej: [3,2], [2,2,1], [4,1], etc.)
        List<Long> frecs = frecuencia.values().stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        // Evaluar jugada según categorías oficiales
        // 8. Escalera de color
        if (esEscalera && esColor) {
            return new ResultadoPoker(8, valores);
        }

        // 7. Póker (4 iguales)
        if (frecs.equals(List.of(4L, 1L))) {
            return new ResultadoPoker(7, valoresPorFrecuencia(frecuencia, valores));
        }

        // 6. Full House (3 + 2)
        if (frecs.equals(List.of(3L, 2L))) {
            return new ResultadoPoker(6, valoresPorFrecuencia(frecuencia, valores));
        }

        // 5. Color
        if (esColor) {
            return new ResultadoPoker(5, valores);
        }

        // 4. Escalera
        if (esEscalera) {
            return new ResultadoPoker(4, valores);
        }

        // 3. Trío
        if (frecs.equals(List.of(3L, 1L, 1L))) {
            return new ResultadoPoker(3, valoresPorFrecuencia(frecuencia, valores));
        }

        // 2. Doble pareja
        if (frecs.equals(List.of(2L, 2L, 1L))) {
            return new ResultadoPoker(2, valoresPorFrecuencia(frecuencia, valores));
        }

        // 1. Pareja
        if (frecs.equals(List.of(2L, 1L, 1L, 1L))) {
            return new ResultadoPoker(1, valoresPorFrecuencia(frecuencia, valores));
        }

        // 0. Carta Alta
        return new ResultadoPoker(0, valores);
    }

    // Ayudante para desempate
    private List<Integer> valoresPorFrecuencia(Map<Integer, Long> freq, List<Integer> ordenOriginal) {
        return ordenOriginal.stream()
                .sorted((a, b) -> {
                    int cmpFreq = Long.compare(freq.get(b), freq.get(a));
                    if (cmpFreq != 0) return cmpFreq;
                    return Integer.compare(b, a);
                })
                .collect(Collectors.toList());
    }

    // Getters y Setters
    public int getCambiosPermitidos() {
        return cambiosPermitidos;
    }

    public void setCambiosPermitidos(int cambiosPermitidos) {
        this.cambiosPermitidos = cambiosPermitidos;
    }
}