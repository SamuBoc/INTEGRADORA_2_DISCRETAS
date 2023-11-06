import java.util.ArrayList;
import java.util.List;

public class GrafoListaAdyacenciaGenerico<T> {
    private List<NodoGenerico<T>> nodos;

    public GrafoListaAdyacenciaGenerico() {
        nodos = new ArrayList<>();
    }

    public void agregarNodo(T valor) {
        nodos.add(new NodoGenerico<>(valor));
    }

    public void agregarArista(T valorNodo1, T valorNodo2) {
        NodoGenerico<T> nodo1 = buscarNodo(valorNodo1);
        NodoGenerico<T> nodo2 = buscarNodo(valorNodo2);

        if (nodo1 != null && nodo2 != null) {
            nodo1.agregarAdyacente(nodo2);
            nodo2.agregarAdyacente(nodo1);
        }
    }

    public void eliminarNodo(T valor) {
        NodoGenerico<T> nodoEliminar = buscarNodo(valor);

        if (nodoEliminar != null) {
            nodos.remove(nodoEliminar);

            for (NodoGenerico<T> nodo : nodos) {
                nodo.eliminarAdyacente(nodoEliminar);
            }
        }
    }

    public void eliminarArista(T valorNodo1, T valorNodo2) {
        NodoGenerico<T> nodo1 = buscarNodo(valorNodo1);
        NodoGenerico<T> nodo2 = buscarNodo(valorNodo2);

        if (nodo1 != null && nodo2 != null) {
            nodo1.eliminarAdyacente(nodo2);
            nodo2.eliminarAdyacente(nodo1);
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
        NodoGenerico<T> nodo1 = buscarNodo(valorNodo1);
        NodoGenerico<T> nodo2 = buscarNodo(valorNodo2);

        if (nodo1 != null && nodo2 != null) {
            return nodo1.getAdyacentes().contains(nodo2);
        }

        return false;
    }

}
