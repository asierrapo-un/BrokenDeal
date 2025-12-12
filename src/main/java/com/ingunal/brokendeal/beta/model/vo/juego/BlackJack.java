package com.ingunal.brokendeal.beta.model.vo.juego;

import com.ingunal.brokendeal.beta.model.vo.personajes.Personaje;
import com.ingunal.brokendeal.beta.model.vo.personajes.Jugador;
import com.ingunal.brokendeal.beta.model.vo.personajes.Dealer;
import com.ingunal.brokendeal.beta.model.vo.Baraja;
import com.ingunal.brokendeal.beta.model.vo.Mano;
import com.ingunal.brokendeal.beta.model.vo.Carta;

public class BlackJack extends Juego {

    private int valorMeta;

    public BlackJack() {
        super();
        this.valorMeta = 21;
    }

    public BlackJack(Jugador jugador, Dealer dealer, Baraja baraja, int valorMeta) {
        super(jugador, dealer, baraja);
        this.valorMeta = valorMeta;
    }

    public void pedirCarta(Personaje personaje) {
        if (baraja != null && personaje != null) {
            Carta carta = baraja.repartirCarta();
            if (carta != null) {
                personaje.getMano().agregarCarta(carta);
            } else {
                System.out.println("ERROR: No hay cartas disponibles en la baraja");
            }
        }
    }

    @Override
    public Personaje evaluarGanador() {
        // VALIDACIÓN: Verificar que ambas manos tengan cartas
        if (jugador.getMano().getCartas().isEmpty() || dealer.getMano().getCartas().isEmpty()) {
            System.out.println("ERROR: Una o ambas manos están vacías. No se puede evaluar.");
            return null;
        }
        
        int valorJugador = calcularValorMano(jugador.getMano());
        int valorDealer = calcularValorMano(dealer.getMano());
        
        // DEBUG: Mostrar valores
        System.out.println("Evaluación BlackJack:");
        System.out.println("  Jugador: " + valorJugador);
        System.out.println("  Dealer:  " + valorDealer);

        // Caso 1: Ambos se pasan de 21 → EMPATE
        if (valorJugador > valorMeta && valorDealer > valorMeta) {
            System.out.println("  → Ambos se pasaron - Empate");
            return null;
        }

        // Caso 2: Solo el jugador se pasa → DEALER GANA
        if (valorJugador > valorMeta) {
            System.out.println("  → Jugador se pasó - Dealer gana");
            return dealer;
        }
        
        // Caso 3: Solo el dealer se pasa → JUGADOR GANA
        if (valorDealer > valorMeta) {
            System.out.println("  → Dealer se pasó - Jugador gana");
            return jugador;
        }

        // Caso 4: Ambos están bajo 21 → Gana el más cercano a 21
        if (valorJugador > valorDealer) {
            System.out.println("  → Jugador más cerca de 21 - Jugador gana");
            return jugador;
        } else if (valorDealer > valorJugador) {
            System.out.println("  → Dealer más cerca de 21 - Dealer gana");
            return dealer;
        } else {
            System.out.println("  → Mismo valor - Empate");
            return null;
        }
    }

    public int getValorMeta() {
        return valorMeta;
    }

    public void setValorMeta(int valorMeta) {
        this.valorMeta = valorMeta;
    }
    
    public int calcularValorMano(Mano mano) {
        int suma = 0;
        int cantidadAses = 0;

        for (Carta c : mano.getCartas()) {
            int valor = c.getValorNumerico();

            // As detectado (valor 11 por defecto)
            if (valor == 11) {
                cantidadAses++;
            }

            suma += valor;
        }

        // Aplicar regla del As: Si se pasa y hay Ases, convertir As de 11 a 1
        while (suma > valorMeta && cantidadAses > 0) {
            suma -= 10; // convertir un As de 11 a 1 (restar 10)
            cantidadAses--;
        }

        return suma;
    }
}