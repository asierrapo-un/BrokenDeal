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
    private List<Carta> cartasOriginales; // NUEVO: Guardar copia de las cartas originales

    // Constructor vacío
    public Baraja() {
        this.cartas = new ArrayList<>();
        this.cartasOriginales = new ArrayList<>();
    }

    // Constructor con lista
    public Baraja(List<Carta> cartas) {
        this.cartas = new ArrayList<>(cartas); // Crear copia
        this.cartasOriginales = new ArrayList<>(cartas); // Guardar originales
        System.out.println("✓ Baraja creada con " + this.cartasOriginales.size() + " cartas");
    }

    public void barajar() {
        Collections.shuffle(cartas);
    }

    public Carta repartirCarta() {
        if (cartas.isEmpty()) {
            System.err.println("ERROR: No hay cartas en la baraja para repartir");
            return null;
        }
        return cartas.remove(0);
    }

    public void eliminarCarta(Carta carta) {
        cartas.remove(carta);
    }

    /**
     * Reinicia la baraja a su estado original con todas las cartas
     */
    public void reiniciarBaraja() {
        cartas.clear();
        cartas.addAll(cartasOriginales); // Restaurar desde las originales
        barajar();
        System.out.println("✓ Baraja reiniciada con " + cartas.size() + " cartas");
    }
    
    /**
     * Método legacy - mantener por compatibilidad
     */
    public void reiniciarBaraja(List<Carta> cartasOriginales) {
        cartas.clear();
        cartas.addAll(this.cartasOriginales); // Usar las originales guardadas
        barajar();
    }

    public Carta darCartaAzBajoManga() {
        if (cartas.isEmpty()) return null;
        return cartas.get(0);
    }

    // Getters y Setters
    public List<Carta> getCartas() {
        return cartas;
    }

    public void setCartas(List<Carta> cartas) {
        this.cartas = cartas;
    }
    
    public int cartasRestantes() {
        return cartas.size();
    }
}