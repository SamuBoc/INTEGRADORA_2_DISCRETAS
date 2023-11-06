import java.util.ArrayList;
import java.util.List;

public class NodoGenerico<T> {
    private T valor;
    private List<NodoGenerico<T>> adyacentes;

    public NodoGenerico(T valor) {
        this.valor = valor;
        adyacentes = new ArrayList<>();
    }

    public T getValor() {
        return valor;
    }

    public void agregarAdyacente(NodoGenerico<T> nodo) {
        adyacentes.add(nodo);
    }

    public void eliminarAdyacente(NodoGenerico<T> nodo) {
        adyacentes.remove(nodo);
    }

    public List<NodoGenerico<T>> getAdyacentes() {
        return adyacentes;
    }
}
