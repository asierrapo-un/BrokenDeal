/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ingunal.brokendeal.beta.model.vo.dialogo;

/**
 *
 * @author andre
 */
public class Efecto {
    private int dañoVida;
    private int perdidaCordura;

    // Constructor vacío
    public Efecto() {
    }

    // Constructor completo
    public Efecto(int dañoVida, int perdidaCordura) {
        this.dañoVida = dañoVida;
        this.perdidaCordura = perdidaCordura;
    }

    // Getters y Setters
    public int getDañoVida() {
        return dañoVida;
    }

    public void setDañoVida(int dañoVida) {
        this.dañoVida = dañoVida;
    }

    public int getPerdidaCordura() {
        return perdidaCordura;
    }

    public void setPerdidaCordura(int perdidaCordura) {
        this.perdidaCordura = perdidaCordura;
    }
}
