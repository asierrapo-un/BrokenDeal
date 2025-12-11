/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ingunal.brokendeal.beta.dao.dto;

/**
 *
 * @author andre
 */
public class CartaJSON {
    private int value;
    private String imgRoute;

    public CartaJSON() {
    }

    public CartaJSON(int value, String imgRoute) {
        this.value = value;
        this.imgRoute = imgRoute;
    }

    public int getValue() {
        return value;
    }

    public String getImgRoute() {
        return imgRoute;
    }
}
