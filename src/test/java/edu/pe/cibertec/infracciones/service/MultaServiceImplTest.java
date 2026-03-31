package edu.pe.cibertec.infracciones.service;

import edu.pe.cibertec.infracciones.exception.InfractorBloqueadoException;
import edu.pe.cibertec.infracciones.model.EstadoMulta;
import edu.pe.cibertec.infracciones.model.Infractor;
import edu.pe.cibertec.infracciones.model.Multa;
import edu.pe.cibertec.infracciones.model.Vehiculo;
import edu.pe.cibertec.infracciones.repository.InfractorRepository;
import edu.pe.cibertec.infracciones.repository.MultaRepository;
import edu.pe.cibertec.infracciones.repository.TipoInfraccionRepository;
import edu.pe.cibertec.infracciones.repository.VehiculoRepository;
import edu.pe.cibertec.infracciones.service.impl.MultaServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MultaServiceImplTest {

    @Mock
    private MultaRepository multaRepository;
    @Mock
    private InfractorRepository infractorRepository;
    @Mock
    private VehiculoRepository vehiculoRepository;
    @Mock
    private TipoInfraccionRepository tipoInfraccionRepository;

    @InjectMocks
    private MultaServiceImpl multaService;

    // Pregunta 3
    @Test
    void transferirMulta_condicionesValidas_asignaYGuarda() {
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setId(1L);

        Infractor infractorB = new Infractor();
        infractorB.setId(2L);
        infractorB.setBloqueado(false);
        infractorB.setVehiculos(new ArrayList<>(List.of(vehiculo)));

        Multa multa = new Multa();
        multa.setId(1L);
        multa.setEstado(EstadoMulta.PENDIENTE);
        multa.setVehiculo(vehiculo);

        when(multaRepository.findById(1L)).thenReturn(Optional.of(multa));
        when(infractorRepository.findById(2L)).thenReturn(Optional.of(infractorB));

        multaService.transferirMulta(1L, 2L);

        assertEquals(infractorB, multa.getInfractor());
        verify(multaRepository, times(1)).save(multa);
    }
    //Pregunta 4
    @Test
    void transferirMulta_infractorBloqueado_lanzaExcepcionYNoGuarda() {
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setId(1L);

        Infractor infractorA = new Infractor();
        infractorA.setId(1L);
        infractorA.setBloqueado(false);
        infractorA.setVehiculos(new ArrayList<>(List.of(vehiculo)));

        Infractor infractorB = new Infractor();
        infractorB.setId(3L);
        infractorB.setBloqueado(true);

        Multa multa = new Multa();
        multa.setId(1L);
        multa.setEstado(EstadoMulta.PENDIENTE);
        multa.setVehiculo(vehiculo);
        multa.setInfractor(infractorA);

        when(multaRepository.findById(1L)).thenReturn(Optional.of(multa));
        when(infractorRepository.findById(infractorB.getId())).thenReturn(Optional.of(infractorB));

        ArgumentCaptor<Multa> multaCaptor = ArgumentCaptor.forClass(Multa.class);

        assertThrows(InfractorBloqueadoException.class, () ->
                multaService.transferirMulta(1L, infractorB.getId())
        );

        verify(multaRepository, never()).save(multaCaptor.capture());
    }

}
