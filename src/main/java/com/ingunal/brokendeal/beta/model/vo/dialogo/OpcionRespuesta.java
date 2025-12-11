/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ingunal.brokendeal.beta.model.vo.dialogo;

import java.util.List;

/**
 *
 * @author andre
 */
public class OpcionRespuesta {
    private List<String> textoOpcion;
    private RespuestaDealer siguiente;   // Composición
    private Efecto efecto;               // Agregación

    // Constructor vacío
    public OpcionRespuesta() {
    }

    // Constructor completo
    public OpcionRespuesta(List<String> textoOpcion, RespuestaDealer siguiente, Efecto efecto) {
        this.textoOpcion = textoOpcion;
        this.siguiente = siguiente;
        this.efecto = efecto;
    }

    // Getters y Setters
    public List<String> getTextoOpcion() {
        return textoOpcion;
    }

    public void setTextoOpcion(List<String> textoOpcion) {
        this.textoOpcion = textoOpcion;
    }

    public RespuestaDealer getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(RespuestaDealer siguiente) {
        this.siguiente = siguiente;
    }

    public Efecto getEfecto() {
        return efecto;
    }

    public void setEfecto(Efecto efecto) {
        this.efecto = efecto;
    }
}
