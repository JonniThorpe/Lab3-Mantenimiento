package org.mps.ronqi2;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mps.dispositivo.Dispositivo;
import java.util.ArrayList;
import java.util.stream.Stream;

public class RonQI2SilverTest {

    RonQI2 ronqi2;
    Dispositivo dispositivo;

    @BeforeEach
    void setup() {
        ronqi2 = new RonQI2Silver();
        dispositivo = mock(Dispositivo.class);
    }

    /*
     * Analiza con los caminos base qué pruebas se han de realizar para comprobar
     * que al inicializar funciona como debe ser.
     * El funcionamiento correcto es que si es posible conectar ambos sensores y
     * configurarlos,
     * el método inicializar de ronQI2 o sus subclases,
     * debería devolver true. En cualquier otro caso false. Se deja programado un
     * ejemplo.
     */
    @Test
    public void inicializar_conAmbosDispositivosConectadosYConf_shouldReturnTrue() {
        // Define
        when(dispositivo.conectarSensorPresion()).thenReturn(true);
        when(dispositivo.conectarSensorSonido()).thenReturn(true);
        when(dispositivo.configurarSensorPresion()).thenReturn(true);
        when(dispositivo.configurarSensorSonido()).thenReturn(true);

        // Execute
        ronqi2.anyadirDispositivo(dispositivo);
        boolean result = ronqi2.inicializar();

        // Verify

        assertTrue(result);
        verify(dispositivo).conectarSensorPresion();
        verify(dispositivo).configurarSensorPresion();
        verify(dispositivo).conectarSensorSonido();
        verify(dispositivo).configurarSensorSonido();

    }

    @Test
    public void inicializar_conDispositivoPresionConectadoYConf_shouldReturnFalse() {
        // Define
        when(dispositivo.conectarSensorPresion()).thenReturn(true);
        when(dispositivo.configurarSensorPresion()).thenReturn(true);
        when(dispositivo.conectarSensorSonido()).thenReturn(false);
        when(dispositivo.configurarSensorSonido()).thenReturn(false);

        // Execute

        ronqi2.anyadirDispositivo(dispositivo);
        boolean result = ronqi2.inicializar();

        // Verify

        assertFalse(result);
        verify(dispositivo).conectarSensorPresion();
        verify(dispositivo).configurarSensorPresion();

    }

    @Test
    public void inicializar_conDispositivoSonidoConectadoYConf_shouldReturnFalse() {

        // Define
        when(dispositivo.conectarSensorPresion()).thenReturn(false);
        when(dispositivo.configurarSensorPresion()).thenReturn(false);
        when(dispositivo.conectarSensorSonido()).thenReturn(true);
        when(dispositivo.configurarSensorSonido()).thenReturn(true);

        // Execute

        ronqi2.anyadirDispositivo(dispositivo);
        boolean result = ronqi2.inicializar();

        // Verify

        assertFalse(result);
        verify(dispositivo, never()).conectarSensorSonido();
        verify(dispositivo, never()).configurarSensorSonido();

    }

    @Test
    public void inicializar_conDispositivoSonidoConectadoYNoConf_shouldReturnFalse() {
        // Define
        when(dispositivo.conectarSensorPresion()).thenReturn(true);
        when(dispositivo.configurarSensorPresion()).thenReturn(false);
        when(dispositivo.conectarSensorSonido()).thenReturn(true);
        when(dispositivo.configurarSensorSonido()).thenReturn(true);

        // Execute

        ronqi2.anyadirDispositivo(dispositivo);
        boolean result = ronqi2.inicializar();

        // Verify
        assertFalse(result);
        verify(dispositivo).conectarSensorPresion();
        verify(dispositivo).configurarSensorPresion();
        verify(dispositivo).conectarSensorSonido();
        verify(dispositivo).configurarSensorSonido();

    }

    @Test
    public void inicializar_conDispositivoPresionConectadoYNoConf_shouldReturnFalse() {
        // Define
        when(dispositivo.conectarSensorPresion()).thenReturn(true);
        when(dispositivo.configurarSensorPresion()).thenReturn(true);
        when(dispositivo.conectarSensorSonido()).thenReturn(true);
        when(dispositivo.configurarSensorSonido()).thenReturn(false);

        // Execute

        ronqi2.anyadirDispositivo(dispositivo);
        boolean result = ronqi2.inicializar();

        // Verify
        assertFalse(result);
        verify(dispositivo).conectarSensorPresion();
        verify(dispositivo).configurarSensorPresion();
        verify(dispositivo).conectarSensorSonido();
        verify(dispositivo).configurarSensorSonido();

    }

    /*
     * Un inicializar debe configurar ambos sensores, comprueba que cuando se
     * inicializa de forma correcta (el conectar es true),
     * se llama una sola vez al configurar de cada sensor.
     */
    @Test
    public void inicializar_llamaUnaVezAConf() {
        // Define
        when(dispositivo.conectarSensorPresion()).thenReturn(true);
        when(dispositivo.conectarSensorSonido()).thenReturn(true);

        // Execute

        ronqi2.anyadirDispositivo(dispositivo);
        ronqi2.inicializar();

        // Verify
        verify(dispositivo, times(1)).configurarSensorPresion();
        verify(dispositivo, times(1)).configurarSensorSonido();

    }

    /*
     * Un reconectar, comprueba si el dispositivo desconectado, en ese caso, conecta
     * ambos y devuelve true si ambos han sido conectados.
     * Genera las pruebas que estimes oportunas para comprobar su correcto
     * funcionamiento.
     * Centrate en probar si todo va bien, o si no, y si se llama a los métodos que
     * deben ser llamados.
     */

    @Test
    public void reconectar_cuandoAmbosDispositivoSeConectan_shouldReturnTrue() {
        when(dispositivo.conectarSensorPresion()).thenReturn(true);
        when(dispositivo.conectarSensorSonido()).thenReturn(true);
        when(dispositivo.estaConectado()).thenReturn(false);

        ronqi2.anyadirDispositivo(dispositivo);
        boolean result = ronqi2.reconectar();

        assertTrue(result);
        verify(dispositivo).conectarSensorPresion();
        verify(dispositivo).conectarSensorSonido();
        verify(dispositivo).estaConectado();
    }

    @Test
    public void reconectar_cuandoDispositivoPresionNoConectadoYSonidoConectado_shouldReturnFalse() {
        when(dispositivo.conectarSensorPresion()).thenReturn(false);
        when(dispositivo.conectarSensorSonido()).thenReturn(true);
        when(dispositivo.estaConectado()).thenReturn(false);

        ronqi2.anyadirDispositivo(dispositivo);
        boolean result = ronqi2.reconectar();

        assertFalse(result);
        verify(dispositivo).conectarSensorPresion();
        verify(dispositivo, never()).conectarSensorSonido();
        verify(dispositivo).estaConectado();
    }

    @Test
    public void reconectar_cuandoDispositivoPresionConectadoYSonidoNoConectado_shouldReturnFalse() {
        when(dispositivo.conectarSensorPresion()).thenReturn(true);
        when(dispositivo.conectarSensorSonido()).thenReturn(false);
        when(dispositivo.estaConectado()).thenReturn(false);

        ronqi2.anyadirDispositivo(dispositivo);
        boolean result = ronqi2.reconectar();

        assertFalse(result);
        verify(dispositivo).conectarSensorPresion();
        verify(dispositivo).conectarSensorSonido();
        verify(dispositivo).estaConectado();
    }

    @Test
    public void reconectar_conAmbosYaContectados_shouldReturnFalse() {
        when(dispositivo.conectarSensorPresion()).thenReturn(false);
        when(dispositivo.conectarSensorSonido()).thenReturn(false);
        when(dispositivo.estaConectado()).thenReturn(true);

        ronqi2.anyadirDispositivo(dispositivo);
        boolean result = ronqi2.reconectar();

        assertFalse(result);
        verify(dispositivo, never()).conectarSensorPresion();
        verify(dispositivo, never()).conectarSensorSonido();
        verify(dispositivo).estaConectado();
    }

    @Test
    public void estaConectado_whenNotConnected_shouldReturnFalse() {
        when(dispositivo.estaConectado()).thenReturn(false);
        ronqi2.anyadirDispositivo(dispositivo);
        boolean result = ronqi2.estaConectado();

        assertFalse(result);
        verify(dispositivo).estaConectado();

    }

    @Test
    public void estaConectado_whenConnected_shouldReturnTrue() {
        when(dispositivo.estaConectado()).thenReturn(true);
        ronqi2.anyadirDispositivo(dispositivo);
        boolean result = ronqi2.estaConectado();

        assertTrue(result);
        verify(dispositivo).estaConectado();
    }

    /*
     * El método evaluarApneaSuenyo, evalua las últimas 5 lecturas realizadas con
     * obtenerNuevaLectura(),
     * y si ambos sensores superan o son iguales a sus umbrales, que son thresholdP
     * = 20.0f y thresholdS = 30.0f;,
     * se considera que hay una apnea en proceso. Si hay menos de 5 lecturas también
     * debería realizar la media.
     */
    @Test
    public void EvaluarApnea_shouldReturnTrue_ifAverageIsUnder_30() {
        // add dispositivo
        ronqi2.anyadirDispositivo(dispositivo);
        // ramdom float
        ArrayList<Float> floats = new ArrayList<>();
        floats.add(12.00f);
        floats.add(20.00f);
        floats.add(10.00f);
        floats.add(31.00f);
        floats.add(31.00f);
        floats.add(31.00f);

        for (int i = 0; i < floats.size(); i++) {
            when(dispositivo.leerSensorPresion()).thenReturn(floats.get(i));
            ronqi2.obtenerNuevaLectura();
        }
        // Average is under 30, result must be false
        assertTrue(ronqi2.evaluarApneaSuenyo());
    }

    @Test
    public void EvaluarApnea_shouldReturnFalse_ifAverageIsOver_30() {
        // add dispositivo
        ronqi2.anyadirDispositivo(dispositivo);
        // ramdom float
        ArrayList<Float> floats = new ArrayList<>();
        floats.add(50.00f);
        floats.add(42.00f);
        floats.add(30.00f);
        floats.add(31.00f);

        for (int i = 0; i < floats.size(); i++) {
            when(dispositivo.leerSensorPresion()).thenReturn(floats.get(i));
            ronqi2.obtenerNuevaLectura();
        }

        // Average is over 30, result must be false
        assertFalse(ronqi2.evaluarApneaSuenyo());
    }

    /*
     * Realiza un primer test para ver que funciona bien independientemente del
     * número de lecturas.
     * Usa el ParameterizedTest para realizar un número de lecturas previas a
     * calcular si hay apnea o no (por ejemplo 4, 5 y 10 lecturas).
     * https://junit.org/junit5/docs/current/user-guide/index.html#writing-tests-
     * parameterized-tests
     */

     //Primer test
    @ParameterizedTest
    @ValueSource(floats = { 10.00f, 15.00f, 19.00f })
    public void evaluarApneaSuenyo_valoresMenoresAlThreshold(float pali) {
        ronqi2.anyadirDispositivo(dispositivo);
        when(dispositivo.leerSensorPresion()).thenReturn(pali);
        when(dispositivo.leerSensorSonido()).thenReturn(pali);
        ronqi2.obtenerNuevaLectura();

        boolean resultado = ronqi2.evaluarApneaSuenyo();

        assertTrue(resultado);
    }

    //Primer test
    @ParameterizedTest
    @ValueSource(floats = { 32.00f, 42.00f, 35.00f })
    public void evaluarApneaSuenyo_valoresMayoresAlThreshold(float pali) {
        ronqi2.anyadirDispositivo(dispositivo);
        when(dispositivo.leerSensorPresion()).thenReturn(pali);
        when(dispositivo.leerSensorSonido()).thenReturn(pali);
        ronqi2.obtenerNuevaLectura();

        boolean resultado = ronqi2.evaluarApneaSuenyo();

        assertFalse(resultado);
    }

    private static Stream<Arguments> lecturasPrevias() {
        return Stream.of(
                Arguments.of(new float[] { 10.00f, 15.00f, 19.00f }, true), // Bajo el threshold
                Arguments.of(new float[] { 20.00f, 20.00f, 20.00f, 20.00f, 20.00f }, true), // Justo en el threshold
                Arguments.of(
                        new float[] { 32.00f, 42.00f, 35.00f, 35.00f, 40.00f, 30.00f, 45.00f, 50.00f, 55.00f, 30.00f },
                        false) // Por encima del threshold
        );
    }

    @ParameterizedTest
    @MethodSource("lecturasPrevias")
    public void evaluarApneaSuenyo_valores(float[] pali, boolean expected) {
        ronqi2.anyadirDispositivo(dispositivo);
        for (float lectura : pali) {
            when(dispositivo.leerSensorPresion()).thenReturn(lectura);
            when(dispositivo.leerSensorSonido()).thenReturn(lectura);
            ronqi2.obtenerNuevaLectura();
        }

        boolean resultado = ronqi2.evaluarApneaSuenyo();
        assertEquals(expected, resultado);
    }

}
