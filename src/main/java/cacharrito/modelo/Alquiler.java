package cacharrito.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "alquileres")
public class Alquiler {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "numero_alquiler", length = 30, nullable = false, unique = true)
	private String numeroAlquiler;

	@ManyToOne
	@JoinColumn(name = "usuario_id", referencedColumnName = "id")
	private Usuario usuario;

	@ManyToOne
	@JoinColumn(name = "vehiculo_id", referencedColumnName = "id")
	private Vehiculo vehiculo;

	@Column(name = "fecha_inicio")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate fechaInicio;

	@Column(name = "fecha_entrega_pactada")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate fechaEntregaPactada;

	@Column(name = "fecha_entrega_real")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate fechaEntregaReal;

	@Column(name = "valor_total", precision = 10, scale = 2)
	private BigDecimal valorTotal;

	// pendiente de entrega, entregado, disponible (finalizado), cancelado
	@Column(name = "estado", length = 30, nullable = false)
	private String estado;

	public Alquiler() {
	}

	public Alquiler(String numeroAlquiler, Usuario usuario, Vehiculo vehiculo, LocalDate fechaInicio,
			LocalDate fechaEntregaPactada, BigDecimal valorTotal, String estado) {
		this.numeroAlquiler = numeroAlquiler;
		this.usuario = usuario;
		this.vehiculo = vehiculo;
		this.fechaInicio = fechaInicio;
		this.fechaEntregaPactada = fechaEntregaPactada;
		this.valorTotal = valorTotal;
		this.estado = estado;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumeroAlquiler() {
		return numeroAlquiler;
	}

	public void setNumeroAlquiler(String numeroAlquiler) {
		this.numeroAlquiler = numeroAlquiler;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Vehiculo getVehiculo() {
		return vehiculo;
	}

	public void setVehiculo(Vehiculo vehiculo) {
		this.vehiculo = vehiculo;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDate getFechaEntregaPactada() {
		return fechaEntregaPactada;
	}

	public void setFechaEntregaPactada(LocalDate fechaEntregaPactada) {
		this.fechaEntregaPactada = fechaEntregaPactada;
	}

	public LocalDate getFechaEntregaReal() {
		return fechaEntregaReal;
	}

	public void setFechaEntregaReal(LocalDate fechaEntregaReal) {
		this.fechaEntregaReal = fechaEntregaReal;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
}
