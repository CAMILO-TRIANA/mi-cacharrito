package cacharrito.controlador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cacharrito.modelo.Administrador;
import cacharrito.repositorio.administrador;

@RestController
@RequestMapping("/administradores/a")
@CrossOrigin(origins = "*")
public class ControladoraAdministrador {

	@Autowired
	private administrador repoAdministrador;

	@GetMapping("/listarTodo")
	public List<Administrador> mostrarTodos() {
		return repoAdministrador.findAll();
	}

	@PostMapping("/guardarAdministrador")
	public ResponseEntity<Administrador> guardar(@RequestBody Administrador a) {
		repoAdministrador.save(a);
		return ResponseEntity.ok(a);
	}

	@PostMapping("/eliminarAdministrador")
	public ResponseEntity<Void> eliminar(@RequestBody Long id) {
		repoAdministrador.deleteById(id);
		return ResponseEntity.ok().build();
	}

	// Login del administrador: usuario + password
	@PostMapping("/login")
	public Administrador login(@RequestParam("usuario") String usuario, @RequestParam("password") String password) {
		return repoAdministrador.findByUsuarioAndPassword(usuario, password);
	}
}
