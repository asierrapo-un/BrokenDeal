package com.ingunal.brokendeal.beta.model.vo.juego;

import com.ingunal.brokendeal.beta.model.vo.personajes.Jugador;
import com.ingunal.brokendeal.beta.model.vo.personajes.Dealer;
import com.ingunal.brokendeal.beta.model.vo.Baraja;
import com.ingunal.brokendeal.beta.model.vo.personajes.Personaje;
import com.ingunal.brokendeal.beta.model.vo.Mano;
import com.ingunal.brokendeal.beta.model.vo.Carta;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Poker extends Juego {
    private int cambiosPermitidos;

    public Poker() {
        super();
        this.cambiosPermitidos = 1;
    }

    public Poker(Jugador jugador, Dealer dealer, Baraja baraja, int cambiosPermitidos) {
        super(jugador, dealer, baraja);
        this.cambiosPermitidos = cambiosPermitidos;
    }

    public boolean permitirCambios(Personaje personaje) {
        return cambiosPermitidos > 0;
    }

    @Override
    public Personaje evaluarGanador() {
        // VALIDACIÓN: Verificar que ambas manos tengan cartas
        if (jugador.getMano().getCartas().isEmpty() || dealer.getMano().getCartas().isEmpty()) {
            System.out.println("ERROR: Una o ambas manos están vacías. No se puede evaluar.");
            return null; // Empate por defecto
        }
        
        ResultadoPoker resJugador = evaluarManoPoker(jugador.getMano());
        ResultadoPoker resDealer = evaluarManoPoker(dealer.getMano());
        
        // DEBUG: Mostrar resultados
        System.out.println("Evaluación Poker:");
        System.out.println("  Jugador: Categoría " + resJugador.getCategoria());
        System.out.println("  Dealer:  Categoría " + resDealer.getCategoria());

        int comparacion = resJugador.compareTo(resDealer);

        if (comparacion > 0) {
            System.out.println("  → Jugador gana");
            return jugador;
        }
        if (comparacion < 0) {
            System.out.println("  → Dealer gana");
            return dealer;
        }
        
        System.out.println("  → Empate exacto");
        return null;
    }

    public ResultadoPoker evaluarManoPoker(Mano mano) {
        List<Carta> cartas = mano.getCartas().stream()
            .sorted((a, b) -> b.getValorNumerico() - a.getValorNumerico())
            .collect(Collectors.toList());

        List<Integer> valores = cartas.stream()
                .map(Carta::getValorNumerico)
                .collect(Collectors.toList());

        List<String> palos = cartas.stream()
                .map(Carta::getSimbolo)
                .collect(Collectors.toList());

        Map<Integer, Long> frecuencia = valores.stream()
                .collect(Collectors.groupingBy(v -> v, Collectors.counting()));

        boolean esColor = palos.stream()
                .distinct().count() == 1;

        boolean esEscalera = true;
        for (int i = 0; i < valores.size() - 1; i++) {
            if (valores.get(i) - 1 != valores.get(i + 1)) {
                esEscalera = false;
                break;
            }
        }

        List<Long> frecs = frecuencia.values().stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        // 8. Escalera de color
        if (esEscalera && esColor) {
            return new ResultadoPoker(8, valores);
        }

        // 7. Póker (4 iguales)
        if (frecs.equals(List.of(4L, 1L))) {
            return new ResultadoPoker(7, valoresPorFrecuencia(frecuencia, valores));
        }

        // 6. Full House (3 + 2)
        if (frecs.equals(List.of(3L, 2L))) {
            return new ResultadoPoker(6, valoresPorFrecuencia(frecuencia, valores));
        }

        // 5. Color
        if (esColor) {
            return new ResultadoPoker(5, valores);
        }

        // 4. Escalera
        if (esEscalera) {
            return new ResultadoPoker(4, valores);
        }

        // 3. Trío
        if (frecs.equals(List.of(3L, 1L, 1L))) {
            return new ResultadoPoker(3, valoresPorFrecuencia(frecuencia, valores));
        }

        // 2. Doble pareja
        if (frecs.equals(List.of(2L, 2L, 1L))) {
            return new ResultadoPoker(2, valoresPorFrecuencia(frecuencia, valores));
        }

        // 1. Pareja
        if (frecs.equals(List.of(2L, 1L, 1L, 1L))) {
            return new ResultadoPoker(1, valoresPorFrecuencia(frecuencia, valores));
        }

        // 0. Carta Alta
        return new ResultadoPoker(0, valores);
    }

    private List<Integer> valoresPorFrecuencia(Map<Integer, Long> freq, List<Integer> ordenOriginal) {
        return ordenOriginal.stream()
                .sorted((a, b) -> {
                    int cmpFreq = Long.compare(freq.get(b), freq.get(a));
                    if (cmpFreq != 0) return cmpFreq;
                    return Integer.compare(b, a);
                })
                .collect(Collectors.toList());
    }

    public int getCambiosPermitidos() {
        return cambiosPermitidos;
    }

    public void setCambiosPermitidos(int cambiosPermitidos) {
        this.cambiosPermitidos = cambiosPermitidos;
    }
}