/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ingunal.brokendeal.beta.model.vo.personajes;

/**
 *
 * @author andre
 */
public class Dealer extends Personaje {
    private int estado;

    // Constructor vacío
    public Dealer() {
        super();
        this.estado = 0;
    }

    // Constructor completo
    public Dealer(String nombre, int vida, int estado) {
        super(nombre, vida);
        this.estado = estado;
    }

    // Ajusta el estado según vida o situación del jugador
    public void setEstado(int vida, Jugador jugador) {
        // La lógica concreta se implementará después
        // en función del comportamiento psicológico del jugador.
        this.estado = vida;

        // Ejemplo para más adelante:
        // if (jugador.getCordura() < 30) { estado++; }
    }

    // Getters y setters
    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
