package cacharrito.controlador;

import static cacharrito.util.Respuestas.error;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cacharrito.modelo.Administrador;
import cacharrito.modelo.Alquiler;
import cacharrito.modelo.Auditoria;
import cacharrito.modelo.Usuario;
import cacharrito.modelo.Vehiculo;
import cacharrito.repositorio.administrador;
import cacharrito.repositorio.alquiler;
import cacharrito.repositorio.auditoria;
import cacharrito.repositorio.usuario;
import cacharrito.repositorio.vehiculo;
import cacharrito.util.GeneradorPdf;

@RestController
@RequestMapping("/alquileres/al")
@CrossOrigin(origins = "*")
public class ControladoraAlquiler {

	private static final String PENDIENTE = "pendiente de entrega";

	@Autowired
	private alquiler repoAlquiler;

	@Autowired
	private usuario repoUsuario;

	@Autowired
	private vehiculo repoVehiculo;

	@Autowired
	private administrador repoAdministrador;

	@Autowired
	private auditoria repoAuditoria;

	@GetMapping("/listarTodo")
	public List<Alquiler> mostrarTodos() {
		return repoAlquiler.findAll(Sort.by(Sort.Direction.DESC, "id"));
	}

	// El usuario selecciona el vehiculo y solicita el alquiler
	@PostMapping("/guardarAlquiler")
	public ResponseEntity<?> guardar(@RequestBody Alquiler a) {
		if (a.getUsuario() == null || a.getUsuario().getId() == null || a.getVehiculo() == null
				|| a.getVehiculo().getId() == null || a.getFechaInicio() == null
				|| a.getFechaEntregaPactada() == null) {
			return error(HttpStatus.BAD_REQUEST, "Faltan datos para solicitar el alquiler");
		}

		Usuario u = repoUsuario.findById(a.getUsuario().getId()).orElse(null);
		Vehiculo v = repoVehiculo.findById(a.getVehiculo().getId()).orElse(null);
		if (u == null || v == null) {
			return error(HttpStatus.NOT_FOUND, "No se encontró el usuario o el vehículo");
		}
		if (!"disponible".equals(v.getEstado())) {
			return error(HttpStatus.CONFLICT, "Este vehículo ya no está disponible. Elige otro del catálogo.");
		}
		if (a.getFechaInicio().isBefore(LocalDate.now())) {
			return error(HttpStatus.BAD_REQUEST, "La fecha de inicio no puede ser anterior a hoy");
		}
		if (a.getFechaEntregaPactada().isBefore(a.getFechaInicio())) {
			return error(HttpStatus.BAD_REQUEST, "La fecha de entrega no puede ser anterior a la de inicio");
		}
		// La licencia debe cubrir todo el alquiler y habilitar para el tipo de vehiculo
		if (u.getVigenciaLicencia() == null || u.getVigenciaLicencia().isBefore(a.getFechaEntregaPactada())) {
			return error(HttpStatus.BAD_REQUEST,
					"Tu licencia vence antes de la fecha de entrega. Elige fechas dentro de su vigencia.");
		}
		if (!licenciaHabilita(u.getCategoriaLicencia(), v.getTipoVehiculo())) {
			return error(HttpStatus.BAD_REQUEST, "Tu licencia categoría " + u.getCategoriaLicencia()
					+ " no te habilita para conducir este tipo de vehículo");
		}

		long dias = ChronoUnit.DAYS.between(a.getFechaInicio(), a.getFechaEntregaPactada());
		if (dias <= 0) {
			dias = 1;
		}
		BigDecimal valorTotal = v.getValorDia().multiply(BigDecimal.valueOf(dias));

		String numeroAlquiler = "ALQ-" + System.currentTimeMillis();

		Alquiler alquilerNuevo = new Alquiler(numeroAlquiler, u, v, a.getFechaInicio(), a.getFechaEntregaPactada(),
				valorTotal, PENDIENTE);

		repoAlquiler.save(alquilerNuevo);

		v.setEstado("alquilado");
		repoVehiculo.save(v);

		return ResponseEntity.ok(alquilerNuevo);
	}

	// El usuario puede cancelar su alquiler mientras el vehiculo no haya sido entregado
	@PostMapping("/cancelarAlquiler")
	public ResponseEntity<?> cancelar(@RequestParam("numeroAlquiler") String numeroAlquiler) {
		Alquiler a = repoAlquiler.findByNumeroAlquiler(numeroAlquiler);
		if (a == null) {
			return error(HttpStatus.NOT_FOUND, "No existe el alquiler " + numeroAlquiler);
		}
		if (!PENDIENTE.equals(a.getEstado())) {
			return error(HttpStatus.CONFLICT, "Solo se puede cancelar un alquiler que aún está pendiente de entrega");
		}

		a.setEstado("cancelado");
		repoAlquiler.save(a);

		Vehiculo v = a.getVehiculo();
		v.setEstado("disponible");
		repoVehiculo.save(v);

		Auditoria registro = new Auditoria(null, a, "CANCELACION", LocalDateTime.now(),
				"El usuario " + a.getUsuario().getNumeroIdentificacion() + " cancelo el alquiler " + numeroAlquiler);
		repoAuditoria.save(registro);

		return ResponseEntity.ok(a);
	}

	@PostMapping("/buscarPorNumero")
	public Alquiler buscarPorNumero(@RequestParam("numeroAlquiler") String numeroAlquiler) {
		return repoAlquiler.findByNumeroAlquiler(numeroAlquiler.trim());
	}

	@PostMapping("/listarPorUsuario")
	public List<Alquiler> listarPorUsuario(@RequestParam("usuarioId") Long usuarioId) {
		Usuario u = repoUsuario.findById(usuarioId).orElse(null);
		if (u == null) {
			return List.of();
		}
		return repoAlquiler.findByUsuarioOrderByIdDesc(u);
	}

	// El administrador ve los vehiculos alquilados que aun no han sido entregados
	@GetMapping("/listarNoEntregados")
	public List<Alquiler> listarNoEntregados() {
		return repoAlquiler.findByEstado(PENDIENTE);
	}

	// El administrador busca por placa y marca el vehiculo como entregado
	@PostMapping("/marcarEntregado")
	public ResponseEntity<?> marcarEntregado(@RequestParam("placa") String placa,
			@RequestParam("adminId") Long adminId) {
		Vehiculo v = repoVehiculo.findByPlaca(placa.trim().toUpperCase());
		if (v == null) {
			return error(HttpStatus.NOT_FOUND, "No existe un vehículo con la placa " + placa.trim().toUpperCase());
		}
		Alquiler a = repoAlquiler.findByVehiculoAndEstado(v, PENDIENTE);
		if (a == null) {
			return error(HttpStatus.NOT_FOUND, "El vehículo " + v.getPlaca() + " no tiene un alquiler pendiente de entrega");
		}

		a.setEstado("entregado");
		repoAlquiler.save(a);

		Administrador admin = repoAdministrador.findById(adminId).orElse(null);
		Auditoria registro = new Auditoria(admin, a, "ENTREGA", LocalDateTime.now(),
				"Se entrego el vehiculo con placa " + v.getPlaca() + " al usuario " + a.getUsuario().getNombreCompleto());
		repoAuditoria.save(registro);

		return ResponseEntity.ok(a);
	}

	// El administrador busca por numero de alquiler y marca el vehiculo como disponible de nuevo,
	// cobrando los dias adicionales si la entrega real es posterior a la pactada
	@PostMapping("/marcarDisponible")
	public ResponseEntity<?> marcarDisponible(@RequestParam("numeroAlquiler") String numeroAlquiler,
			@RequestParam("fechaEntregaReal") String fechaEntregaReal, @RequestParam("adminId") Long adminId) {

		Alquiler a = repoAlquiler.findByNumeroAlquiler(numeroAlquiler.trim());
		if (a == null) {
			return error(HttpStatus.NOT_FOUND, "No existe el alquiler " + numeroAlquiler);
		}
		if (!"entregado".equals(a.getEstado())) {
			return error(HttpStatus.CONFLICT, "Este alquiler está en estado \"" + a.getEstado()
					+ "\". Solo se puede recibir un vehículo que ya fue entregado al usuario.");
		}

		LocalDate entregaReal;
		try {
			entregaReal = LocalDate.parse(fechaEntregaReal);
		} catch (DateTimeParseException e) {
			return error(HttpStatus.BAD_REQUEST, "La fecha real de entrega no es válida");
		}
		if (entregaReal.isBefore(a.getFechaInicio())) {
			return error(HttpStatus.BAD_REQUEST, "La fecha de devolución no puede ser anterior al inicio del alquiler");
		}

		a.setFechaEntregaReal(entregaReal);

		if (entregaReal.isAfter(a.getFechaEntregaPactada())) {
			long diasAdicionales = ChronoUnit.DAYS.between(a.getFechaEntregaPactada(), entregaReal);
			BigDecimal recargo = a.getVehiculo().getValorDia().multiply(BigDecimal.valueOf(diasAdicionales));
			a.setValorTotal(a.getValorTotal().add(recargo));
		}

		a.setEstado("finalizado");
		repoAlquiler.save(a);

		Vehiculo v = a.getVehiculo();
		v.setEstado("disponible");
		repoVehiculo.save(v);

		Administrador admin = repoAdministrador.findById(adminId).orElse(null);
		Auditoria registro = new Auditoria(admin, a, "FINALIZACION", LocalDateTime.now(),
				"Se cambio el alquiler " + numeroAlquiler + " a disponible. Valor final: " + a.getValorTotal());
		repoAuditoria.save(registro);

		return ResponseEntity.ok(a);
	}

	// Genera el PDF del alquiler solicitado
	@GetMapping("/generarPdf")
	public ResponseEntity<?> generarPdf(@RequestParam("numeroAlquiler") String numeroAlquiler) {
		Alquiler a = repoAlquiler.findByNumeroAlquiler(numeroAlquiler);
		if (a == null) {
			return error(HttpStatus.NOT_FOUND, "No existe el alquiler " + numeroAlquiler);
		}
		byte[] pdf = GeneradorPdf.generarPdfAlquiler(a);

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + numeroAlquiler + ".pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(pdf);
	}

	// Motocicleta: categorias A. Los demas tipos (automovil, camioneta, campero, microbus): B o C
	private boolean licenciaHabilita(String categoria, String tipoVehiculo) {
		if (categoria == null || categoria.isBlank()) {
			return false;
		}
		String c = categoria.trim().toUpperCase();
		if ("motocicleta".equalsIgnoreCase(tipoVehiculo)) {
			return c.startsWith("A");
		}
		return c.startsWith("B") || c.startsWith("C");
	}
}
