package cacharrito.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cacharrito.modelo.Auditoria;

@Repository
public interface auditoria extends JpaRepository<Auditoria, Long> {
}
