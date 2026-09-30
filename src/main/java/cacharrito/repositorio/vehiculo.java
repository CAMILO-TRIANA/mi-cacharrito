package cacharrito.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cacharrito.modelo.Vehiculo;

@Repository
public interface vehiculo extends JpaRepository<Vehiculo, Long> {

	public List<Vehiculo> findByTipoVehiculoAndEstado(String tipoVehiculo, String estado);

	public List<Vehiculo> findByEstado(String estado);

	public Vehiculo findByPlaca(String placa);
}
