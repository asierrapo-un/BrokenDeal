/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ingunal.brokendeal.beta.dao;

import com.ingunal.brokendeal.beta.dao.dto.CartaJSON;
import com.ingunal.brokendeal.beta.model.vo.Carta;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStreamReader;
import java.util.*;

/**
 *
 * @author andre
 */
public class CartaDAO {
    public List<Carta> cargarCartas() {

        try {
            InputStreamReader reader = new InputStreamReader(
                getClass().getResourceAsStream("/data/Cartas.json")
            );

            // Estructura: Map<Palo, Map<ValorTexto, CartaJSON>>
            Map<String, Map<String, CartaJSON>> data =
                    new Gson().fromJson(reader,
                    new TypeToken<Map<String, Map<String, CartaJSON>>>(){}.getType());

            List<Carta> cartas = new ArrayList<>();

            for (String palo : data.keySet()) {

                Map<String, CartaJSON> valores = data.get(palo);

                for (String valorTexto : valores.keySet()) {

                    CartaJSON jsonCarta = valores.get(valorTexto);

                    // Nuevo: usamos "palo" tal cual, y el valor es el primer char
                    String simbolo = palo;
                    char valorChar = valorTexto.charAt(0);
                    int valorNumerico = jsonCarta.getValue();
                    String ruta = jsonCarta.getImgRoute();

                    cartas.add(new Carta(simbolo, valorChar, valorNumerico, ruta));
                }
            }

            return cartas;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error cargando Cartas.json: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
