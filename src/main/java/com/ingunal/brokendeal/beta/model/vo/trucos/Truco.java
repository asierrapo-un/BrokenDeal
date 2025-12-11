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
public abstract class Truco {
    protected int usosRestantes;
    protected double probabilidadDescubierto;

    public Truco(int usosRestantes, double probabilidadDescubierto) {
        this.usosRestantes = usosRestantes;
        this.probabilidadDescubierto = probabilidadDescubierto;
    }

    public int getUsosRestantes() {
        return usosRestantes;
    }

    public double getProbabilidadDescubierto() {
        return probabilidadDescubierto;
    }
    
    /**
     * Cada truco implementa su propio comportamiento
     * Retorna true si se activó correctamente
     * Retorna false si falló o el jugador fue descubierto
     */
    public abstract boolean activar(Jugador jugador, Juego juego);
}
