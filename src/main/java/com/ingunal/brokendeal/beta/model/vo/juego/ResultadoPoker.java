/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ingunal.brokendeal.beta.model.vo.juego;

import java.util.List;

/**
 *
 * @author andre
 */
public class ResultadoPoker implements Comparable<ResultadoPoker> {
    private int categoria;
    private List<Integer> desempate;

    public ResultadoPoker(int categoria, List<Integer> desempate) {
        this.categoria = categoria;
        this.desempate = desempate;
    }
    
    public List<Integer> getDesempate() {
        return desempate;
    }

    public int getCategoria() { return categoria; }

    @Override
    public int compareTo(ResultadoPoker otro) {
        if (this.categoria != otro.categoria)
            return Integer.compare(this.categoria, otro.categoria);

        for (int i = 0; i < this.desempate.size(); i++) {
            int cmp = Integer.compare(
                this.desempate.get(i),
                otro.desempate.get(i)
            );
            if (cmp != 0) return cmp;
        }
        return 0;
    }
}
