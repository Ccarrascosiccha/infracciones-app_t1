package edu.pe.cibertec.infracciones.service;

import edu.pe.cibertec.infracciones.model.*;
import edu.pe.cibertec.infracciones.repository.*;
import edu.pe.cibertec.infracciones.service.impl.InfractorServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InfractorServiceImplTest {

    @Mock
    private InfractorRepository infractorRepository;
    @Mock
    private VehiculoRepository vehiculoRepository;
    @Mock
    private MultaRepository multaRepository;

    @InjectMocks
    private InfractorServiceImpl infractorService;

    //1
    @Test
    void calcularDeuda_conPendienteYVencida_retorna545() {
        Multa pendiente = new Multa();
        pendiente.setMonto(200.00);

        Multa vencida = new Multa();
        vencida.setMonto(300.00);

        when(multaRepository.findByInfractor_IdAndEstado(1L, EstadoMulta.PENDIENTE))
                .thenReturn(List.of(pendiente));
        when(multaRepository.findByInfractor_IdAndEstado(1L, EstadoMulta.VENCIDA))
                .thenReturn(List.of(vencida));

        double resultado = infractorService.calcularDeuda(1L);

        assertEquals(545.00, resultado, 0.01);
    }
    // Pregunta 2
    @Test
    void desasignarVehiculo_sinMultasPendientes_remuevVehiculoYGuarda() {
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setId(1L);

        Infractor infractor = new Infractor();
        infractor.setId(1L);
        infractor.setVehiculos(new ArrayList<>(List.of(vehiculo)));

        when(multaRepository.existsByInfractor_IdAndVehiculo_IdAndEstado(
                1L, 1L, EstadoMulta.PENDIENTE)).thenReturn(false);
        when(infractorRepository.findById(1L))
                .thenReturn(Optional.of(infractor));

        infractorService.desasignarVehiculo(1L, 1L);

        assertTrue(infractor.getVehiculos().isEmpty());
        verify(infractorRepository, times(1)).save(infractor);
    }


}