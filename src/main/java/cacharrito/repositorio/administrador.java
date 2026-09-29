package cacharrito.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cacharrito.modelo.Administrador;

@Repository
public interface administrador extends JpaRepository<Administrador, Long> {

	public Administrador findByUsuarioAndPassword(String usuario, String password);
}
