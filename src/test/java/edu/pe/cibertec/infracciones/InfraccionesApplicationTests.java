package edu.pe.cibertec.infracciones;

import edu.pe.cibertec.infracciones.repository.InfractorRepository;
import edu.pe.cibertec.infracciones.repository.MultaRepository;
import edu.pe.cibertec.infracciones.repository.PagoRepository;
import edu.pe.cibertec.infracciones.repository.TipoInfraccionRepository;
import edu.pe.cibertec.infracciones.repository.VehiculoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(properties = {
		"spring.autoconfigure.exclude=org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration",
		"spring.sql.init.mode=never"
})
class InfraccionesApplicationTests {

	@MockitoBean
	InfractorRepository infractorRepository;

	@MockitoBean
	VehiculoRepository vehiculoRepository;

	@MockitoBean
	MultaRepository multaRepository;

	@MockitoBean
	PagoRepository pagoRepository;

	@MockitoBean
	TipoInfraccionRepository tipoInfraccionRepository;

	@Test
	void contextLoads() {
	}

}
