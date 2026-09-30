package cacharrito.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cacharrito.modelo.Usuario;

@Repository
public interface usuario extends JpaRepository<Usuario, Long> {

	public Usuario findByNumeroIdentificacionAndPassword(String numeroIdentificacion, String password);

	public Usuario findByNumeroIdentificacion(String numeroIdentificacion);
}
