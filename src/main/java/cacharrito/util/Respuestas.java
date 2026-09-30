package cacharrito.util;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Respuestas {

	// Respuesta de error uniforme: {"mensaje": "..."} para que el frontend pueda mostrarla
	public static ResponseEntity<Map<String, String>> error(HttpStatus estado, String mensaje) {
		return ResponseEntity.status(estado).body(Map.of("mensaje", mensaje));
	}
}
