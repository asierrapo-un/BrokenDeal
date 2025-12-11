/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ingunal.brokendeal.beta.model.vo.personajes;

import com.ingunal.brokendeal.beta.model.vo.Carta;
import com.ingunal.brokendeal.beta.model.vo.trucos.Truco;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andre
 */
public class Jugador extends Personaje {
    private int cordura;
    private List<Truco> trucos;   // Composición 1..*
    private int indiceCartaSeleccionadaParaCambiar = -1;

    // Constructor vacío
    public Jugador() {
        super();
        this.cordura = 100; // por defecto (puedes modificarlo más adelante)
        this.trucos = new ArrayList<>();
    }

    // Constructor completo
    public Jugador(String nombre, int vida, int cordura) {
        super(nombre, vida);
        this.cordura = cordura;
        this.trucos = new ArrayList<>();
    }

    // Añade una carta directamente a la mano
    public void recibirCarta(Carta carta) {
        this.mano.agregarCarta(carta);
    }

    public void perderCordura(int valor) {
        this.cordura -= valor;
        if (this.cordura < 0) this.cordura = 0;
    }
    
    public void ganarCordura(int valor) {
        this.cordura += valor;
        if (this.cordura < 0) this.cordura = 0;
    }

    // Getters y setters
    public int getCordura() {
        return cordura;
    }

    public void setCordura(int cordura) {
        this.cordura = cordura;
    }

    public List<Truco> getTrucos() {
        return trucos;
    }

    public void setTrucos(List<Truco> trucos) {
        this.trucos = trucos;
    }

    public void agregarTruco(Truco truco) {
        this.trucos.add(truco);
    }
    
    public int getIndiceCartaSeleccionadaParaCambiar() {
        return indiceCartaSeleccionadaParaCambiar;
    }

    public void setIndiceCartaSeleccionadaParaCambiar(int indice) {
        this.indiceCartaSeleccionadaParaCambiar = indice;
    }

    // útil: limpiar después de usar
    public void resetIndiceCartaSeleccionada() {
        this.indiceCartaSeleccionadaParaCambiar = -1;
    }
}
