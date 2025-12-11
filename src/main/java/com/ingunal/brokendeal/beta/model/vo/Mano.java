/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ingunal.brokendeal.beta.model.vo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andre
 */
public class Mano {
   private List<Carta> cartas;

    // Constructor vacío
    public Mano() {
        this.cartas = new ArrayList<>();
    }

    // Constructor con lista
    public Mano(List<Carta> cartas) {
        this.cartas = cartas;
    }

    // Métodos de manejo
    public void agregarCarta(Carta carta) {
        cartas.add(carta);
    }

    public void eliminarCarta(Carta carta) {
        cartas.remove(carta);
    }

    // Getters y Setters
    public List<Carta> getCartas() {
        return cartas;
    }

    public void setCartas(List<Carta> cartas) {
        this.cartas = cartas;
    } 
    
    public void limpiar() {
        cartas.clear();
    }
}
