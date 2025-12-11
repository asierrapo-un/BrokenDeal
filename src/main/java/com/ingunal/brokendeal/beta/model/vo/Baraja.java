/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ingunal.brokendeal.beta.model.vo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author andre
 */
public class Baraja {
    private List<Carta> cartas;

    // Constructor vacío
    public Baraja() {
        this.cartas = new ArrayList<>();
    }

    // Constructor con lista
    public Baraja(List<Carta> cartas) {
        this.cartas = cartas;
    }

    // MÉTODOS A IMPLEMENTAR LUEGO

    public void barajar() {
        Collections.shuffle(cartas);
    }

    public Carta repartirCarta() {
        if (cartas.isEmpty()) return null;
        return cartas.remove(0);
    }

    public void eliminarCarta(Carta carta) {
        cartas.remove(carta);
    }

    public void reiniciarBaraja(List<Carta> cartasOriginales) {
        cartas.clear();
        cartas.addAll(cartasOriginales);
        barajar();
    }

    public Carta darCartaAzBajoManga() {
        if (cartas.isEmpty()) return null;
        return cartas.get(0); // o puedes elegir una aleatoria
    }

    // Getters y Setters
    public List<Carta> getCartas() {
        return cartas;
    }

    public void setCartas(List<Carta> cartas) {
        this.cartas = cartas;
    }
}
