import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GrafoListaAdyacenciaGenericoTest {
    private GrafoListaAdyacenciaGenerico<String> grafo;

    @Before
    public void setUp() {
        grafo = new GrafoListaAdyacenciaGenerico<>();
    }

    @Test
    public void testAgregarNodoYArco() {
        grafo.agregarNodo("A");
        grafo.agregarNodo("B");
        grafo.agregarNodo("C");
        grafo.agregarArista("A", "B");

        assertTrue(grafo.existeArista("A", "B"));
        assertFalse(grafo.existeArista("A", "C"));
    }

    @Test
    public void testEliminarNodoYArco() {
        grafo.agregarNodo("X");
        grafo.agregarNodo("Y");
        grafo.agregarNodo("Z");
        grafo.agregarArista("X", "Y");

        grafo.eliminarNodo("X");

        assertFalse(grafo.existeArista("X", "Y"));
    }
}
