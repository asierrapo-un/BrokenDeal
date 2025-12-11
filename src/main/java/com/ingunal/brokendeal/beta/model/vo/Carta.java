/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ingunal.brokendeal.beta.model.vo;

/**
 *
 * @author andre
 */
public class Carta {
    private String simbolo;
    private char valor;
    private int valorNumerico;
    private String imgRuta;

    // Constructor vacío (requerido por JSON)
    public Carta() {
    }

    // Constructor completo
    public Carta(String simbolo, char valor, int valorNumerico, String imgRuta) {
        this.simbolo = simbolo;
        this.valor = valor;
        this.valorNumerico = valorNumerico;
        this.imgRuta = imgRuta;
    }

    // Getters y Setters
    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public char getValor() {
        return valor;
    }

    public void setValor(char valor) {
        this.valor = valor;
    }

    public int getValorNumerico() {
        return valorNumerico;
    }

    public void setValorNumerico(int valorNumerico) {
        this.valorNumerico = valorNumerico;
    }

    public String getImgRuta() {
        return imgRuta;
    }

    public void setImgRuta(String imgRuta) {
        this.imgRuta = imgRuta;
    }
}
