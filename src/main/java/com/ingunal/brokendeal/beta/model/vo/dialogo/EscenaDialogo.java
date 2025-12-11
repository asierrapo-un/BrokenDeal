/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ingunal.brokendeal.beta.model.vo.dialogo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andre
 */
public class EscenaDialogo {
    private String idEscena;                      // "0", "1", "14", etc.
    private List<String> textoDealer;              // líneas de diálogo del dealer
    private Efecto efectoDealer;                   // effect dentro de enemy_dialogue
    private List<OpcionRespuesta> opciones;        // respuestas del jugador

    // Constructor vacío
    public EscenaDialogo() {
        this.idEscena = "";
        this.textoDealer = new ArrayList<>();
        this.efectoDealer = new Efecto(0,0);
        this.opciones = new ArrayList<>();
    }

    // Constructor completo (el que usa el DAO)
    public EscenaDialogo(
            String idEscena,
            List<String> textoDealer,
            Efecto efectoDealer,
            List<OpcionRespuesta> opciones
    ) {
        this.idEscena = idEscena;
        this.textoDealer = textoDealer;
        this.efectoDealer = efectoDealer;
        this.opciones = opciones;
    }

    // Getters
    public String getIdEscena() {
        return idEscena;
    }

    public List<String> getTextoDealer() {
        return textoDealer;
    }

    public Efecto getEfectoDealer() {
        return efectoDealer;
    }

    public List<OpcionRespuesta> getOpciones() {
        return opciones;
    }

    // Setters (opcionales)
    public void setIdEscena(String idEscena) {
        this.idEscena = idEscena;
    }

    public void setTextoDealer(List<String> textoDealer) {
        this.textoDealer = textoDealer;
    }

    public void setEfectoDealer(Efecto efectoDealer) {
        this.efectoDealer = efectoDealer;
    }

    public void setOpciones(List<OpcionRespuesta> opciones) {
        this.opciones = opciones;
    }
}
