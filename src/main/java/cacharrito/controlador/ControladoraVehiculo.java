package cacharrito.controlador;

import static cacharrito.util.Respuestas.error;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cacharrito.modelo.Vehiculo;
import cacharrito.repositorio.vehiculo;

@RestController
@RequestMapping("/vehiculos/v")
@CrossOrigin(origins = "*")
public class ControladoraVehiculo {
	
	

	@Autowired
	private vehiculo repoVehiculo;

	@GetMapping("/listarTodo")
	public List<Vehiculo> mostrarTodos() {
		return repoVehiculo.findAll();
	}

	// Todos los vehiculos que se pueden alquilar ahora mismo (catalogo del usuario)
	@GetMapping("/listarDisponibles")
	public List<Vehiculo> listarDisponibles() {
		return repoVehiculo.findByEstado("disponible");
	}

	// Vehiculos disponibles de un tipo especifico (automovil, camioneta, campero, microbus, motocicleta)
	@PostMapping("/buscarPorTipo")
	public List<Vehiculo> buscarPorTipo(@RequestParam("tipo") String tipo) {
		return repoVehiculo.findByTipoVehiculoAndEstado(tipo, "disponible");
	}

	@PostMapping("/buscarPorPlaca")
	public Vehiculo buscarPorPlaca(@RequestParam("placa") String placa) {
		return repoVehiculo.findByPlaca(placa.trim().toUpperCase());
	}

	@PostMapping("/guardarVehiculo")
	public ResponseEntity<?> guardar(@RequestBody Vehiculo v) {
		if (v.getPlaca() == null || v.getPlaca().isBlank() || v.getTipoVehiculo() == null
				|| v.getTipoVehiculo().isBlank() || v.getValorDia() == null
				|| v.getValorDia().compareTo(BigDecimal.ZERO) <= 0) {
			return error(HttpStatus.BAD_REQUEST, "La placa, el tipo y un valor por día mayor a cero son obligatorios");
		}
		v.setPlaca(v.getPlaca().trim().toUpperCase());
		if (repoVehiculo.findByPlaca(v.getPlaca()) != null) {
			return error(HttpStatus.CONFLICT, "Ya existe un vehículo con la placa " + v.getPlaca());
		}
		if (v.getEstado() == null || v.getEstado().isBlank()) {
			v.setEstado("disponible");
		}
		v.setId(null);
		repoVehiculo.save(v);
		return ResponseEntity.ok(v);
	}

	@PostMapping("/actualizarVehiculo")
	public ResponseEntity<?> actualizar(@RequestBody Vehiculo v) {
		Vehiculo existente = v.getId() == null ? null : repoVehiculo.findById(v.getId()).orElse(null);
		if (existente == null) {
			return error(HttpStatus.NOT_FOUND, "El vehículo no existe");
		}
		String placa = v.getPlaca() == null ? "" : v.getPlaca().trim().toUpperCase();
		Vehiculo conMismaPlaca = repoVehiculo.findByPlaca(placa);
		if (conMismaPlaca != null && !conMismaPlaca.getId().equals(existente.getId())) {
			return error(HttpStatus.CONFLICT, "Ya existe otro vehículo con la placa " + placa);
		}
		existente.setPlaca(placa);
		existente.setTipoVehiculo(v.getTipoVehiculo());
		existente.setColor(v.getColor());
		existente.setValorDia(v.getValorDia());
		existente.setEstado(v.getEstado());
		existente.setMarca(v.getMarca());
		existente.setModelo(v.getModelo());
		existente.setAnio(v.getAnio());
		existente.setPasajeros(v.getPasajeros());
		existente.setTransmision(v.getTransmision());
		existente.setImagenUrl(v.getImagenUrl());
		repoVehiculo.save(existente);
		return ResponseEntity.ok(existente);
	}

	@PostMapping("/eliminarVehiculo")
	public ResponseEntity<?> eliminar(@RequestBody Long id) {
		try {
			repoVehiculo.deleteById(id);
			return ResponseEntity.ok().build();
		} catch (DataIntegrityViolationException e) {
			return error(HttpStatus.CONFLICT,
					"No se puede eliminar: el vehículo tiene alquileres registrados. Póngalo en mantenimiento en su lugar.");
		}
	}

	// El administrador sube la foto de un vehiculo; se devuelve la ruta para guardarla en imagenUrl
	@PostMapping("/subirImagen")
	public ResponseEntity<?> subirImagen(@RequestParam("archivo") MultipartFile archivo) {
		String tipo = archivo.getContentType();
		String extension = null;
		if ("image/jpeg".equals(tipo)) {
			extension = ".jpg";
		} else if ("image/png".equals(tipo)) {
			extension = ".png";
		} else if ("image/webp".equals(tipo)) {
			extension = ".webp";
		}
		if (archivo.isEmpty() || extension == null) {
			return error(HttpStatus.BAD_REQUEST, "La foto debe ser una imagen JPG, PNG o WEBP");
		}
		try {
			Path carpeta = Paths.get("uploads", "vehiculos");
			Files.createDirectories(carpeta);
			String nombre = UUID.randomUUID() + extension;
			Files.copy(archivo.getInputStream(), carpeta.resolve(nombre), StandardCopyOption.REPLACE_EXISTING);
			return ResponseEntity.ok(Map.of("url", "/uploads/vehiculos/" + nombre));
		} catch (IOException e) {
			return error(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo guardar la foto");
		}
	}
}
