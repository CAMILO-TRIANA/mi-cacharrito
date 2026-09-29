package cacharrito.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cacharrito.modelo.Alquiler;
import cacharrito.modelo.Usuario;
import cacharrito.modelo.Vehiculo;

@Repository
public interface alquiler extends JpaRepository<Alquiler, Long> {

	public Alquiler findByNumeroAlquiler(String numeroAlquiler);

	public List<Alquiler> findByEstado(String estado);

	public List<Alquiler> findByUsuarioOrderByIdDesc(Usuario usuario);

	public Alquiler findByVehiculoAndEstado(Vehiculo vehiculo, String estado);
}
