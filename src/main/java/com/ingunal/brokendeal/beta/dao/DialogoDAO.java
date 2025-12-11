/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ingunal.brokendeal.beta.dao;

import com.google.gson.*;
import java.util.*;

import com.ingunal.brokendeal.beta.model.vo.dialogo.EscenaDialogo;
import com.ingunal.brokendeal.beta.model.vo.dialogo.OpcionRespuesta;
import com.ingunal.brokendeal.beta.model.vo.dialogo.RespuestaDealer;
import com.ingunal.brokendeal.beta.model.vo.dialogo.Efecto;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 *
 * @author andre
 */
public class DialogoDAO {
    private static final String RUTA_JSON = "/data/Historia.json";

    private Map<String, EscenaDialogo> escenasRondas = new HashMap<>();
    private EscenaDialogo introduccion;
    private EscenaDialogo finalBueno;
    private EscenaDialogo finalMalo;

    public DialogoDAO() {
        cargarEscenas();
    }

    private void cargarEscenas() {

        try (InputStream is = getClass().getResourceAsStream(RUTA_JSON)) {

            if (is == null) {
                System.out.println("ERROR: No se encontró " + RUTA_JSON);
                return;
            }

            InputStreamReader reader = new InputStreamReader(is);

            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();

            // ------------ INTRODUCCIÓN ------------
            if (root.has("introducción")) {
                JsonObject introJson = root.getAsJsonObject("introducción");

                List<String> texto = convertirALista(introJson.get("text-secuence"));

                introduccion = new EscenaDialogo(
                        "intro",
                        texto,
                        new Efecto(0, 0),
                        List.of()
                );
            }

            // ------------ RONDAS ------------
            JsonObject rondasJson = root.getAsJsonObject("rondas");

            for (String key : rondasJson.keySet()) {

                JsonObject escenaJson = rondasJson.getAsJsonObject(key);

                JsonObject enemyJson = escenaJson.getAsJsonObject("enemy_dialogue");

                List<String> textoDealer = convertirALista(enemyJson.get("text"));
                int efectoDealerValor = enemyJson.get("effect").getAsInt();

                Efecto efectoDealer = new Efecto(0, efectoDealerValor);

                List<OpcionRespuesta> opciones = new ArrayList<>();
                JsonArray choicesJson = escenaJson.getAsJsonArray("player_choices");

                for (JsonElement elem : choicesJson) {

                    JsonObject opcionJson = elem.getAsJsonObject();

                    // textoJugador
                    List<String> textoJugador;
                    if (opcionJson.has("text"))
                        textoJugador = List.of(opcionJson.get("text").getAsString());
                    else
                        textoJugador = convertirALista(opcionJson.get("text-secuence"));

                    // respuesta dealer
                    List<String> respuestaDealerLista =
                            convertirALista(opcionJson.get("enemy_response"));

                    RespuestaDealer respuestaDealer =
                            new RespuestaDealer(respuestaDealerLista);

                    // efecto cordura
                    int efectoValor = opcionJson.get("effect").getAsInt();
                    Efecto efecto = new Efecto(0, efectoValor);

                    opciones.add(new OpcionRespuesta(textoJugador, respuestaDealer, efecto));
                }

                escenasRondas.put(
                        key,
                        new EscenaDialogo(key, textoDealer, efectoDealer, opciones)
                );
            }

            // ------------ FINALES ------------
            if (root.has("Final 1")) {
                JsonObject fin1 = root.getAsJsonObject("Final 1");
                List<String> texto = convertirALista(fin1.get("text-secuence"));

                finalBueno = new EscenaDialogo(
                        "finalBueno",
                        texto,
                        new Efecto(0, 0),
                        List.of()
                );
            }

            if (root.has("Final 2")) {
                JsonObject fin2 = root.getAsJsonObject("Final 2");
                List<String> texto = convertirALista(fin2.get("text-secuence"));

                finalMalo = new EscenaDialogo(
                        "finalMalo",
                        texto,
                        new Efecto(0, 0),
                        List.of()
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------- GETTERS ----------
    public EscenaDialogo obtenerEscena(int ronda) {
        return escenasRondas.get(String.valueOf(ronda));
    }

    public EscenaDialogo obtenerIntroduccion() {
        return introduccion;
    }

    public EscenaDialogo obtenerFinalBueno() {
        return finalBueno;
    }

    public EscenaDialogo obtenerFinalMalo() {
        return finalMalo;
    }

    // ---------- AUXILIAR ----------
    private List<String> convertirALista(JsonElement elemento) {
        List<String> lista = new ArrayList<>();

        if (elemento == null || elemento.isJsonNull())
            return lista;

        if (elemento.isJsonArray()) {
            for (JsonElement e : elemento.getAsJsonArray()) {
                lista.add(e.getAsString());
            }
        } else {
            lista.add(elemento.getAsString());
        }

        return lista;
    }
}