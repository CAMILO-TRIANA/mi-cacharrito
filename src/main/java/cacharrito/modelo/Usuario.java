package cacharrito.modelo;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "numero_identificacion", length = 20, nullable = false, unique = true)
	private String numeroIdentificacion;

	@Column(name = "nombre_completo", length = 100, nullable = false)
	private String nombreCompleto;

	@Column(name = "fecha_expedicion_licencia")
	private LocalDate fechaExpedicionLicencia;

	@Column(name = "categoria_licencia", length = 10)
	private String categoriaLicencia;

	@Column(name = "vigencia_licencia")
	private LocalDate vigenciaLicencia;

	@Column(name = "correo", length = 100)
	private String correo;

	@Column(name = "telefono", length = 20)
	private String telefono;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "password", length = 255)
	private String password;

	public Usuario() {
	}

	public Usuario(String numeroIdentificacion, String nombreCompleto, LocalDate fechaExpedicionLicencia,
			String categoriaLicencia, LocalDate vigenciaLicencia, String correo, String telefono, String password) {
		this.numeroIdentificacion = numeroIdentificacion;
		this.nombreCompleto = nombreCompleto;
		this.fechaExpedicionLicencia = fechaExpedicionLicencia;
		this.categoriaLicencia = categoriaLicencia;
		this.vigenciaLicencia = vigenciaLicencia;
		this.correo = correo;
		this.telefono = telefono;
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}

	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}

	public LocalDate getFechaExpedicionLicencia() {
		return fechaExpedicionLicencia;
	}

	public void setFechaExpedicionLicencia(LocalDate fechaExpedicionLicencia) {
		this.fechaExpedicionLicencia = fechaExpedicionLicencia;
	}

	public String getCategoriaLicencia() {
		return categoriaLicencia;
	}

	public void setCategoriaLicencia(String categoriaLicencia) {
		this.categoriaLicencia = categoriaLicencia;
	}

	public LocalDate getVigenciaLicencia() {
		return vigenciaLicencia;
	}

	public void setVigenciaLicencia(LocalDate vigenciaLicencia) {
		this.vigenciaLicencia = vigenciaLicencia;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
