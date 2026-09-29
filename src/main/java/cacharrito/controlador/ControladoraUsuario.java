package cacharrito.controlador;

import static cacharrito.util.Respuestas.error;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cacharrito.modelo.Usuario;
import cacharrito.repositorio.usuario;

@RestController
@RequestMapping("/usuarios/u")
@CrossOrigin(origins = "http://localhost:4200")
public class ControladoraUsuario {

	@Autowired
	private usuario repoUsuario;

	@GetMapping("/listarTodo")
	public List<Usuario> mostrarTodos() {
		return repoUsuario.findAll();
	}

	// Registro de usuario
	@PostMapping("/guardarUsuario")
	public ResponseEntity<?> guardar(@RequestBody Usuario u) {
		if (vacio(u.getNumeroIdentificacion()) || vacio(u.getNombreCompleto()) || vacio(u.getPassword())
				|| vacio(u.getCategoriaLicencia())) {
			return error(HttpStatus.BAD_REQUEST, "Complete los datos obligatorios del registro");
		}
		u.setNumeroIdentificacion(u.getNumeroIdentificacion().trim());
		if (repoUsuario.findByNumeroIdentificacion(u.getNumeroIdentificacion()) != null) {
			return error(HttpStatus.CONFLICT, "Ya existe un usuario registrado con ese número de identificación");
		}
		if (u.getVigenciaLicencia() == null || u.getVigenciaLicencia().isBefore(LocalDate.now())) {
			return error(HttpStatus.BAD_REQUEST, "La licencia de conducción debe estar vigente");
		}
		u.setId(null);
		repoUsuario.save(u);
		return ResponseEntity.ok(u);
	}

	@PostMapping("/actualizarUsuario")
	public ResponseEntity<?> actualizar(@RequestBody Usuario u) {
		Usuario existente = u.getId() == null ? null : repoUsuario.findById(u.getId()).orElse(null);
		if (existente == null) {
			return error(HttpStatus.NOT_FOUND, "El usuario no existe");
		}
		existente.setNombreCompleto(u.getNombreCompleto());
		existente.setFechaExpedicionLicencia(u.getFechaExpedicionLicencia());
		existente.setCategoriaLicencia(u.getCategoriaLicencia());
		existente.setVigenciaLicencia(u.getVigenciaLicencia());
		existente.setCorreo(u.getCorreo());
		existente.setTelefono(u.getTelefono());
		// La contraseña ya no viaja hacia el frontend, asi que solo se cambia si llega una nueva
		if (!vacio(u.getPassword())) {
			existente.setPassword(u.getPassword());
		}
		repoUsuario.save(existente);
		return ResponseEntity.ok(existente);
	}

	@PostMapping("/eliminarUsuario")
	public ResponseEntity<Void> eliminar(@RequestBody Long id) {
		repoUsuario.deleteById(id);
		return ResponseEntity.ok().build();
	}

	// Login del usuario: numero de identificacion + password
	@PostMapping("/login")
	public Usuario login(@RequestParam("identificacion") String identificacion,
			@RequestParam("password") String password) {
		return repoUsuario.findByNumeroIdentificacionAndPassword(identificacion.trim(), password);
	}

	@PostMapping("/buscarPorIdentificacion")
	public Usuario buscarPorIdentificacion(@RequestParam("identificacion") String identificacion) {
		return repoUsuario.findByNumeroIdentificacion(identificacion);
	}

	private boolean vacio(String s) {
		return s == null || s.isBlank();
	}
}
