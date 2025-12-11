/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ingunal.brokendeal.beta.model.vo.personajes;

import com.ingunal.brokendeal.beta.model.vo.Mano;

/**
 *
 * @author andre
 */
public abstract class Personaje {
    protected String nombre;
    protected int vida;
    protected Mano mano;   // Composición

    // Constructor vacío (útil para JSON)
    public Personaje() {
        this.mano = new Mano(); // Composición: Personaje crea su mano
    }

    // Constructor completo
    public Personaje(String nombre, int vida) {
        this.nombre = nombre;
        this.vida = vida;
        this.mano = new Mano(); // Siempre existe
    }

    public void perderVida(int valor) {
        vida -= valor;
        if (vida < 0) vida = 0;
    }

    public boolean estaVivo() {
        return vida > 0;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    public Mano getMano() {
        return mano;
    }

    public void setMano(Mano mano) {
        this.mano = mano;
    }
}
