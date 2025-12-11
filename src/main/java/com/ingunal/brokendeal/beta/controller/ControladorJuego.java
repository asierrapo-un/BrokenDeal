/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ingunal.brokendeal.beta.controller;

import com.ingunal.brokendeal.beta.model.vo.personajes.Dealer;
import com.ingunal.brokendeal.beta.model.vo.trucos.Truco;
import com.ingunal.brokendeal.beta.model.vo.trucos.PastillaTruco;
import com.ingunal.brokendeal.beta.model.vo.trucos.AzBajoMangaTruco;
import com.ingunal.brokendeal.beta.model.vo.dialogo.EscenaDialogo;
import com.ingunal.brokendeal.beta.model.vo.juego.Juego;
import com.ingunal.brokendeal.beta.model.vo.juego.Poker;
import com.ingunal.brokendeal.beta.model.vo.juego.ResultadoPoker;
import com.ingunal.brokendeal.beta.model.vo.juego.BlackJack;
import com.ingunal.brokendeal.beta.model.vo.juego.EstadoJuego;
import com.ingunal.brokendeal.beta.model.vo.Mano;
import com.ingunal.brokendeal.beta.model.vo.Carta;
import com.ingunal.brokendeal.beta.model.vo.personajes.Jugador;
import com.ingunal.brokendeal.beta.dao.DialogoDAO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author andre
 */
public class ControladorJuego {
    private Juego juego;
    private ControladorCartas controladorCartas;
    private ControladorDialogo controladorDialogo;
    private DialogoDAO dialogoDAO;

    // ---------------- CONSTRUCTOR ---------------- //

    public ControladorJuego(Juego juego,
                            ControladorCartas controladorCartas,
                            ControladorDialogo controladorDialogo,
                            DialogoDAO dialogoDAO) {

        this.juego = juego;
        this.controladorCartas = controladorCartas;
        this.controladorDialogo = controladorDialogo;
        this.dialogoDAO = dialogoDAO;
    }

    // ---------------- INICIO DEL JUEGO ---------------- //

    public void iniciarJuego() {

        juego.getBaraja().barajar();
        juego.cambiarEstado(EstadoJuego.INICIO);

        EscenaDialogo intro = dialogoDAO.obtenerIntroduccion();
        controladorDialogo.iniciarDialogo(intro);

        iniciarRonda();
    }

    // ---------------- RONDAS ---------------- //

    public void iniciarRonda() {

        juego.iniciarRonda();

        if (juego instanceof Poker) {
            controladorCartas.repartirCartas(juego.getJugador().getMano(), 5);
            controladorCartas.repartirCartas(juego.getDealer().getMano(), 5);
        }

        if (juego instanceof BlackJack) {
            controladorCartas.repartirCartas(juego.getJugador().getMano(), 2);
            controladorCartas.repartirCartas(juego.getDealer().getMano(), 2);
        }

        juego.cambiarEstado(EstadoJuego.TURNO_JUGADOR);
    }

    public void finalizarRonda() {

        // Reducir efecto pastillas
        for (Truco t : juego.getJugador().getTrucos()) {
            if (t instanceof PastillaTruco) {
                PastillaTruco p = (PastillaTruco) t;
                p.reducirDuracion(juego);
            }
        }

        juego.finalizarRonda();

        if (juego.esFinDelJuego()) {

            EscenaDialogo finalEscena;

            if (!juego.getDealer().estaVivo()) {
                finalEscena = dialogoDAO.obtenerFinalBueno();
            } else {
                finalEscena = dialogoDAO.obtenerFinalMalo();
            }

            controladorDialogo.iniciarDialogo(finalEscena);
            juego.cambiarEstado(EstadoJuego.FINALIZADO);
            return;
        }

        ejecutarDialogo();
        iniciarRonda();
    }

    // ---------------- DIÁLOGOS ---------------- //

    public void ejecutarDialogo() {

        int ronda = juego.getNumeroRonda();
        EscenaDialogo escena = dialogoDAO.obtenerEscena(ronda);

        if (escena != null) {
            controladorDialogo.iniciarDialogo(escena);
        }

        juego.cambiarEstado(EstadoJuego.ENTRE_RONDAS);
    }

    // ---------------- TRUCOS ---------------- //
    public boolean usarTruco(int indiceOpcion) {
        Jugador jugador = juego.getJugador();

        if (jugador.getTrucos() == null || jugador.getTrucos().isEmpty())
            return false;

        if (indiceOpcion < 0 || indiceOpcion >= jugador.getTrucos().size())
            return false;

        Truco truco = jugador.getTrucos().get(indiceOpcion);

        if (!juego.permitirUsoTruco(truco))
            return false;

        // --- AZ BAJO LA MANGA --- 
        if (truco instanceof AzBajoMangaTruco) {

            int indice = jugador.getIndiceCartaSeleccionadaParaCambiar();

            if (indice < 0) {
                System.out.println("ERROR: La vista no estableció la carta a cambiar.");
                return false;
            }

            boolean exito = truco.activar(jugador, juego);

            jugador.resetIndiceCartaSeleccionada();

            if (!exito) {
                System.out.println("→ El dealer te descubrió usando un truco.");
            }

            if (truco.getUsosRestantes() <= 0)
                jugador.getTrucos().remove(truco);

            return exito;
        }

        // --- PASTILLA ---
        if (truco instanceof PastillaTruco) {

            boolean exito = truco.activar(jugador, juego);

            if (exito)
                System.out.println("→ Pastilla ingerida. Visión restante: "
                        + juego.getRondasVision());

            if (truco.getUsosRestantes() <= 0)
                jugador.getTrucos().remove(truco);

            return exito;
        }

        // Otros futuros
        return truco.activar(jugador, juego);
    }

    // ---------------- TURNOS ---------------- //

    public void procesarTurnJugador() {
        juego.cambiarEstado(EstadoJuego.TURNO_JUGADOR);
    }

    public void procesarTurnoDealer() {

        juego.cambiarEstado(EstadoJuego.TURNO_DEALER);

        if (juego instanceof Poker) {
            cambiarCartasDealer();
            return;
        }

        Dealer d = juego.getDealer();

        if (juego instanceof BlackJack) {
            BlackJack bj = (BlackJack) juego;
            int suma = bj.calcularValorMano(d.getMano());

            if (suma <= 16) {
                controladorCartas.repartirCarta(d.getMano());
            }
        }
    }

    // --------------- CAMBIO DE CARTAS ------------------ //

    public boolean cambiarCartasJugador(List<Integer> indices) {

        if (!(juego instanceof Poker)) return false;

        Poker poker = (Poker) juego;

        if (!poker.permitirCambios(juego.getJugador())) {
            System.out.println("No quedan cambios permitidos.");
            return false;
        }

        controladorCartas.cambiarCartasPorIndices(juego.getJugador().getMano(), indices);
        poker.setCambiosPermitidos(poker.getCambiosPermitidos() - 1);

        return true;
    }

    public void cambiarCartasDealer() {

        if (!(juego instanceof Poker)) return;

        Poker poker = (Poker) juego;
        Mano manoDealer = juego.getDealer().getMano();
        ResultadoPoker resultado = poker.evaluarManoPoker(manoDealer);

        List<Integer> indicesCambiar = new ArrayList<>();

        switch (resultado.getCategoria()) {

            case 0: // carta alta
                indicesCambiar = Arrays.asList(2, 3, 4);
                break;

            case 1: // pareja
                int par = resultado.getDesempate().get(0);
                for (int i = 0; i < manoDealer.getCartas().size(); i++) {
                    if (manoDealer.getCartas().get(i).getValorNumerico() != par)
                        indicesCambiar.add(i);
                }
                break;

            case 2: // doble pareja
                Map<Integer, Long> freq = manoDealer.getCartas().stream()
                        .collect(Collectors.groupingBy(Carta::getValorNumerico,
                                Collectors.counting()));

                for (int i = 0; i < manoDealer.getCartas().size(); i++) {
                    if (freq.get(manoDealer.getCartas().get(i).getValorNumerico()) == 1)
                        indicesCambiar.add(i);
                }
                break;

            case 3: // trío
                int trio = resultado.getDesempate().get(0);
                for (int i = 0; i < manoDealer.getCartas().size(); i++) {
                    if (manoDealer.getCartas().get(i).getValorNumerico() != trio)
                        indicesCambiar.add(i);
                }
                break;

            default:
                return; // jugadas fuertes → no cambiar
        }

        if (!indicesCambiar.isEmpty() && poker.getCambiosPermitidos() > 0) {
            controladorCartas.cambiarCartasPorIndices(manoDealer, indicesCambiar);
            poker.setCambiosPermitidos(poker.getCambiosPermitidos() - 1);
        }
    }

    // ---------------- GETTERS ---------------- //

    public Juego getJuego() {
        return juego;
    }
}
