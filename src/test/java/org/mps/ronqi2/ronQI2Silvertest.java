package org.mps.ronqi2;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mps.dispositivo.Dispositivo;
import org.mps.dispositivo.DispositivoSilver;

public class RonQI2SilverTest {

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
        // Mock
        Dispositivo dispositivo = mock(Dispositivo.class);

        // Define
        when(dispositivo.conectarSensorPresion()).thenReturn(true);
        when(dispositivo.conectarSensorSonido()).thenReturn(true);
        when(dispositivo.configurarSensorPresion()).thenReturn(true);
        when(dispositivo.configurarSensorSonido()).thenReturn(true);

        // Execute
        RonQI2 ronqi2 = new RonQI2Silver();
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
        // Mock
        Dispositivo dispositivo = mock(Dispositivo.class);

        // Define
        when(dispositivo.conectarSensorPresion()).thenReturn(true);
        when(dispositivo.configurarSensorPresion()).thenReturn(true);
        when(dispositivo.conectarSensorSonido()).thenReturn(false);
        when(dispositivo.configurarSensorSonido()).thenReturn(false);

        // Execute
        RonQI2 ronqi2 = new RonQI2Silver();
        ronqi2.anyadirDispositivo(dispositivo);
        boolean result = ronqi2.inicializar();

        // Verify

        assertFalse(result);
        verify(dispositivo).conectarSensorPresion();
        verify(dispositivo).configurarSensorPresion();

    }

    @Test
    public void inicializar_conDispositivoSonidoConectadoYConf_shouldReturnFalse() {
        // Mock
        Dispositivo dispositivo = mock(Dispositivo.class);

        // Define
        when(dispositivo.conectarSensorPresion()).thenReturn(false);
        when(dispositivo.configurarSensorPresion()).thenReturn(false);
        when(dispositivo.conectarSensorSonido()).thenReturn(true);
        when(dispositivo.configurarSensorSonido()).thenReturn(true);

        // Execute
        RonQI2 ronqi2 = new RonQI2Silver();
        ronqi2.anyadirDispositivo(dispositivo);
        boolean result = ronqi2.inicializar();

        // Verify

        assertFalse(result);
        verify(dispositivo, never()).conectarSensorSonido();
        verify(dispositivo, never()).configurarSensorSonido();

    }

    @Test
    public void inicializar_conDispositivoSonidoConectadoYNoConf_shouldReturnFalse() {
        // Mock
        Dispositivo dispositivo = mock(Dispositivo.class);

        // Define
        when(dispositivo.conectarSensorPresion()).thenReturn(true);
        when(dispositivo.configurarSensorPresion()).thenReturn(false);
        when(dispositivo.conectarSensorSonido()).thenReturn(true);
        when(dispositivo.configurarSensorSonido()).thenReturn(true);

        // Execute
        RonQI2 ronqi2 = new RonQI2Silver();
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
        // Mock
        Dispositivo dispositivo = mock(Dispositivo.class);

        // Define
        when(dispositivo.conectarSensorPresion()).thenReturn(true);
        when(dispositivo.configurarSensorPresion()).thenReturn(true);
        when(dispositivo.conectarSensorSonido()).thenReturn(true);
        when(dispositivo.configurarSensorSonido()).thenReturn(false);

        // Execute
        RonQI2 ronqi2 = new RonQI2Silver();
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
    public void inicializar_llamaUnaVezAConf(){
        // Mock
        Dispositivo dispositivo = mock(Dispositivo.class);

        //Define
        when(dispositivo.conectarSensorPresion()).thenReturn(true);
        when(dispositivo.conectarSensorSonido()).thenReturn(true);

        //Execute
        RonQI2 ronqi2 = new RonQI2Silver();
        ronqi2.anyadirDispositivo(dispositivo);
        ronqi2.inicializar();

        //Verify
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
     public void reconectar_cuandoAmbosDispositivoSeConectan_shouldReturnTrue(){

        Dispositivo dispositivo = mock(Dispositivo.class);

        when(dispositivo.conectarSensorPresion()).thenReturn(true);
        when(dispositivo.conectarSensorSonido()).thenReturn(true);
        when(dispositivo.estaConectado()).thenReturn(false);

        RonQI2 ronqi2 = new RonQI2Silver();
        ronqi2.anyadirDispositivo(dispositivo);
        boolean result = ronqi2.reconectar();

        assertTrue(result);
        verify(dispositivo).conectarSensorPresion();
        verify(dispositivo).conectarSensorSonido();
        verify(dispositivo).estaConectado();
     }

     @Test
     public void reconectar_cuandoDispositivoPresionNoConectadoYSonidoConectado_shouldReturnFalse(){

        Dispositivo dispositivo = mock(Dispositivo.class);

        when(dispositivo.conectarSensorPresion()).thenReturn(false);
        when(dispositivo.conectarSensorSonido()).thenReturn(true);
        when(dispositivo.estaConectado()).thenReturn(false);

        RonQI2 ronqi2 = new RonQI2Silver();
        ronqi2.anyadirDispositivo(dispositivo);
        boolean result = ronqi2.reconectar();

        assertFalse(result);
        verify(dispositivo).conectarSensorPresion();
        verify(dispositivo, never()).conectarSensorSonido();
        verify(dispositivo).estaConectado();
     }

     @Test
     public void reconectar_cuandoDispositivoPresionConectadoYSonidoNoConectado_shouldReturnFalse(){

        Dispositivo dispositivo = mock(Dispositivo.class);

        when(dispositivo.conectarSensorPresion()).thenReturn(true);
        when(dispositivo.conectarSensorSonido()).thenReturn(false);
        when(dispositivo.estaConectado()).thenReturn(false);

        RonQI2 ronqi2 = new RonQI2Silver();
        ronqi2.anyadirDispositivo(dispositivo);
        boolean result = ronqi2.reconectar();

        assertFalse(result);
        verify(dispositivo).conectarSensorPresion();
        verify(dispositivo).conectarSensorSonido();
        verify(dispositivo).estaConectado();
     }

     @Test
     public void reconectar_conAmbosYaContectados_shouldReturnFalse(){

        Dispositivo dispositivo = mock(Dispositivo.class);

        when(dispositivo.conectarSensorPresion()).thenReturn(false);
        when(dispositivo.conectarSensorSonido()).thenReturn(false);
        when(dispositivo.estaConectado()).thenReturn(true);

        RonQI2 ronqi2 = new RonQI2Silver();
        ronqi2.anyadirDispositivo(dispositivo);
        boolean result = ronqi2.reconectar();

        assertFalse(result);
        verify(dispositivo, never()).conectarSensorPresion();
        verify(dispositivo, never()).conectarSensorSonido();
        verify(dispositivo).estaConectado();
     }

     @Test
     public void estaConectado_whenNotConnected_shouldReturnFalse(){
        Dispositivo dispositivo = mock(Dispositivo.class);
        when(dispositivo.estaConectado()).thenReturn(false);

        RonQI2 ronqi2 = new RonQI2Silver();
        ronqi2.anyadirDispositivo(dispositivo);
        boolean result = ronqi2.estaConectado();

        assertFalse(result);
        verify(dispositivo).estaConectado();

     }

     @Test
     public void estaConectado_whenConnected_shouldReturnTrue(){
        Dispositivo dispositivo = mock(Dispositivo.class);
        when(dispositivo.estaConectado()).thenReturn(true);

        RonQI2 ronqi2 = new RonQI2Silver();
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


    /*
     * Realiza un primer test para ver que funciona bien independientemente del
     * número de lecturas.
     * Usa el ParameterizedTest para realizar un número de lecturas previas a
     * calcular si hay apnea o no (por ejemplo 4, 5 y 10 lecturas).
     * https://junit.org/junit5/docs/current/user-guide/index.html#writing-tests-
     * parameterized-tests
     */
}
