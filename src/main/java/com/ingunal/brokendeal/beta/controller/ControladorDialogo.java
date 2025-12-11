/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ingunal.brokendeal.beta.controller;


import java.util.List;

import com.ingunal.brokendeal.beta.model.vo.dialogo.EscenaDialogo;
import com.ingunal.brokendeal.beta.model.vo.dialogo.OpcionRespuesta;
import com.ingunal.brokendeal.beta.model.vo.dialogo.RespuestaDealer;
import com.ingunal.brokendeal.beta.model.vo.dialogo.Efecto;

/**
 *
 * @author andre
 */
public class ControladorDialogo {
    private EscenaDialogo escenaActual;

    private int indiceDealer = 0;        // para avanzar en textoDealer
    private int indiceJugador = 0;       // para text-secuence
    private int indiceRespuesta = 0;     // para enemy_response

    private OpcionRespuesta opcionElegida = null;

    private EstadoDialogo estado = EstadoDialogo.IDLE;

    public enum EstadoDialogo {
        IDLE,
        MOSTRANDO_DEALER,
        ESPERANDO_RESPUESTA,
        MOSTRANDO_JUGADOR,
        MOSTRANDO_RESPUESTA_ENEMIGO,
        TERMINADO
    }

    // -----------------------------------------------------------
    // INICIAR EL DIALOGO
    // -----------------------------------------------------------
    public void iniciarDialogo(EscenaDialogo escena) {
        this.escenaActual = escena;

        this.indiceDealer = 0;
        this.indiceJugador = 0;
        this.indiceRespuesta = 0;
        this.opcionElegida = null;

        this.estado = EstadoDialogo.MOSTRANDO_DEALER;
    }

    // -----------------------------------------------------------
    // AVANZA UNA LÍNEA DEL DEALER
    // -----------------------------------------------------------
    public String mostrarDialogoDealer() {

        if (estado != EstadoDialogo.MOSTRANDO_DEALER) {
            return null;
        }

        List<String> lineas = escenaActual.getTextoDealer();

        if (indiceDealer < lineas.size()) {
            return lineas.get(indiceDealer++);
        }

        // Dealer terminó → ahora se muestran opciones
        estado = EstadoDialogo.ESPERANDO_RESPUESTA;
        return null;
    }

    // -----------------------------------------------------------
    // SELECCIONAR UNA OPCIÓN DEL JUGADOR
    // -----------------------------------------------------------
    public List<String> elegirOpcion(int indice) {

        if (estado != EstadoDialogo.ESPERANDO_RESPUESTA) {
            return null;
        }

        opcionElegida = escenaActual.getOpciones().get(indice);

        indiceJugador = 0;
        estado = EstadoDialogo.MOSTRANDO_JUGADOR;

        return opcionElegida.getTextoOpcion(); // COMPLETO, la vista lo muestra secuencial
    }

    // -----------------------------------------------------------
    // MOSTRAR SIGUIENTE LÍNEA DEL JUGADOR (si es text-sequence)
    // -----------------------------------------------------------
    public String mostrarLineaJugador() {

        if (estado != EstadoDialogo.MOSTRANDO_JUGADOR) {
            return null;
        }

        List<String> texto = opcionElegida.getTextoOpcion();

        if (indiceJugador < texto.size()) {
            return texto.get(indiceJugador++);
        }

        // Ya terminó → mostrar respuesta del dealer
        estado = EstadoDialogo.MOSTRANDO_RESPUESTA_ENEMIGO;
        return null;
    }

    // -----------------------------------------------------------
    // MOSTRAR SIGUIENTE LINEA DE ENEMY_RESPONSE
    // -----------------------------------------------------------
    public String mostrarRespuestaEnemigo() {

        if (estado != EstadoDialogo.MOSTRANDO_RESPUESTA_ENEMIGO) {
            return null;
        }

        RespuestaDealer resp = opcionElegida.getSiguiente();
        List<String> lineas = resp.getTextoRespuesta();

        if (indiceRespuesta < lineas.size()) {
            return lineas.get(indiceRespuesta++);
        }

        // Ya terminó → se finaliza el diálogo
        estado = EstadoDialogo.TERMINADO;
        return null;
    }

    // -----------------------------------------------------------
    // APLICACIÓN DEL EFECTO DE LA OPCIÓN
    // -----------------------------------------------------------
    public Efecto obtenerEfecto() {
        if (opcionElegida == null) return null;
        return opcionElegida.getEfecto();
    }

    public EstadoDialogo getEstado() {
        return estado;
    }

    // -----------------------------------------------------------
    // Saber si terminó
    // -----------------------------------------------------------
    public boolean isTerminado() {
        return estado == EstadoDialogo.TERMINADO;
    }

    // Para permitir que ControladorJuego lo reinicie
    public void finalizarDialogo() {
        this.estado = EstadoDialogo.TERMINADO;
    }
}
