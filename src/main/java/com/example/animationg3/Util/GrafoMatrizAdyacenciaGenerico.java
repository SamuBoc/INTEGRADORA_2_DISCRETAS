package com.example.animationg3.Util;

import java.util.ArrayList;

public class GrafoMatrizAdyacenciaGenerico<T> {
    private ArrayList<NodoGenerico<T>> nodos;
    private boolean[][] matrizAdyacencia;

    public GrafoMatrizAdyacenciaGenerico(int numNodos) {
        nodos = new ArrayList<>(numNodos);
        matrizAdyacencia = new boolean[numNodos][numNodos];
    }

    public void agregarNodo(T valor) {
        nodos.add(new NodoGenerico<>(valor));
    }

    public void agregarArista(T valorNodo1, T valorNodo2) {
        int indiceNodo1 = nodos.indexOf(buscarNodo(valorNodo1));
        int indiceNodo2 = nodos.indexOf(buscarNodo(valorNodo2));

        if (indiceNodo1 != -1 && indiceNodo2 != -1) {
            matrizAdyacencia[indiceNodo1][indiceNodo2] = true;
            matrizAdyacencia[indiceNodo2][indiceNodo1] = true;
        }
    }

    public void eliminarNodo(T valor) {
        NodoGenerico<T> nodoEliminar = buscarNodo(valor);
        int indiceNodoEliminar = nodos.indexOf(nodoEliminar);

        if (nodoEliminar != null) {
            nodos.remove(indiceNodoEliminar);

            for (int i = 0; i < nodos.size(); i++) {
                matrizAdyacencia[i][indiceNodoEliminar] = false;
                matrizAdyacencia[indiceNodoEliminar][i] = false;
            }
        }
    }

    public void eliminarArista(T valorNodo1, T valorNodo2) {
        int indiceNodo1 = nodos.indexOf(buscarNodo(valorNodo1));
        int indiceNodo2 = nodos.indexOf(buscarNodo(valorNodo2));

        if (indiceNodo1 != -1 && indiceNodo2 != -1) {
            matrizAdyacencia[indiceNodo1][indiceNodo2] = false;
            matrizAdyacencia[indiceNodo2][indiceNodo1] = false;
        }
    }

    private NodoGenerico<T> buscarNodo(T valor) {
        for (NodoGenerico<T> nodo : nodos) {
            if (nodo.getValor().equals(valor)) {
                return nodo;
            }
        }
        return null;
    }

    public boolean existeArista(T valorNodo1, T valorNodo2) {
        int indiceNodo1 = nodos.indexOf(buscarNodo(valorNodo1));
        int indiceNodo2 = nodos.indexOf(buscarNodo(valorNodo2));

        if (indiceNodo1 != -1 && indiceNodo2 != -1) {
            return matrizAdyacencia[indiceNodo1][indiceNodo2];
        }

        return false;
    }

}
