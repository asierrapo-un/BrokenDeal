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
public class RespuestaDealer {
    private List<String> textoRespuesta;

    // Constructor vacío
    public RespuestaDealer() {
    }

    // Constructor completo
    public RespuestaDealer(List<String> textoRespuesta) {
        this.textoRespuesta = textoRespuesta;
    }

    // Getters y Setters
    public List<String> getTextoRespuesta() {
        return textoRespuesta;
    }

    public void setTextoRespuesta(List<String> textoRespuesta) {
        this.textoRespuesta = textoRespuesta;
    }
}
