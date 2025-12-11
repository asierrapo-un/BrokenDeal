/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ingunal.brokendeal.beta.controller;

import java.util.Comparator;
import java.util.List;
import com.ingunal.brokendeal.beta.model.vo.Baraja;
import com.ingunal.brokendeal.beta.model.vo.Carta;
import com.ingunal.brokendeal.beta.model.vo.Mano;
import java.util.stream.Collectors;

/**
 *
 * @author andre
 */
public class ControladorCartas {
    private Baraja baraja;

    // Constructor: recibe una baraja ya inicializada
    public ControladorCartas(Baraja baraja) {
        this.baraja = baraja;
    }

    // Reparte UNA carta a la mano
    public void repartirCarta(Mano mano) {
        Carta carta = baraja.repartirCarta();
        if (carta != null) {
            mano.agregarCarta(carta);
        }
    }

    // Reparte N cartas a la mano
    public void repartirCartas(Mano mano, int cantidad) {
        for (int i = 0; i < cantidad; i++) {
            repartirCarta(mano);
        }
    }
    
    // Cambia las cartas seleccionadas por unas nuevas. Máximo 3.
    public void cambiarCartasPorIndices(Mano mano, List<Integer> indices) {
        // Validación: máximo 3 cartas por reglas del Poker
        if (indices == null || indices.isEmpty()) return;
        if (indices.size() > 3) {
            System.out.println("Solo puedes cambiar hasta 3 cartas.");
            return;
        }

        // Ordenar índices en forma descendente para evitar problemas
        // al eliminar cartas por posición
        List<Integer> sortedIndices = indices.stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        List<Carta> cartas = mano.getCartas();

        // 1) Recoger las cartas antiguas y devolverlas al fondo de la baraja
        for (int indice : sortedIndices) {
            if (indice < 0 || indice >= cartas.size()) {
                System.out.println("Índice inválido para cambiar carta: " + indice);
                continue;
            }

            Carta cartaEliminada = cartas.remove(indice);
            baraja.getCartas().add(cartaEliminada); // Se devuelven al fondo del mazo
        }

        // 2) Sacar nuevas cartas y colocarlas en las posiciones correctas
        // Aquí recorremos nuevamente los índices PERO ahora en orden ascendente
        // para reinsertar en la posición original.
        List<Integer> ascending = indices.stream()
                .sorted()
                .collect(Collectors.toList());

        for (int indice : ascending) {
            Carta nueva = baraja.repartirCarta();
            if (nueva != null) {
                cartas.add(indice, nueva);
            }
        }
    }

    // Reinicia la baraja a su estado original
    public void reiniciarBaraja() {
        baraja.reiniciarBaraja(baraja.getCartas()); 
    }
}
