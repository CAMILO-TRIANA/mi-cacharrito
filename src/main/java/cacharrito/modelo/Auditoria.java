package cacharrito.modelo;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "auditoria")
public class Auditoria {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "administrador_id", referencedColumnName = "id")
	private Administrador administrador;

	@ManyToOne
	@JoinColumn(name = "alquiler_id", referencedColumnName = "id")
	private Alquiler alquiler;

	@Column(name = "accion", length = 50, nullable = false)
	private String accion;

	@Column(name = "fecha_accion")
	private LocalDateTime fechaAccion;

	@Column(name = "detalles", columnDefinition = "TEXT")
	private String detalles;

	public Auditoria() {
	}

	public Auditoria(Administrador administrador, Alquiler alquiler, String accion, LocalDateTime fechaAccion,
			String detalles) {
		this.administrador = administrador;
		this.alquiler = alquiler;
		this.accion = accion;
		this.fechaAccion = fechaAccion;
		this.detalles = detalles;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Administrador getAdministrador() {
		return administrador;
	}

	public void setAdministrador(Administrador administrador) {
		this.administrador = administrador;
	}

	public Alquiler getAlquiler() {
		return alquiler;
	}

	public void setAlquiler(Alquiler alquiler) {
		this.alquiler = alquiler;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public LocalDateTime getFechaAccion() {
		return fechaAccion;
	}

	public void setFechaAccion(LocalDateTime fechaAccion) {
		this.fechaAccion = fechaAccion;
	}

	public String getDetalles() {
		return detalles;
	}

	public void setDetalles(String detalles) {
		this.detalles = detalles;
	}
}
