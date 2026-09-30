package cacharrito.modelo;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "vehiculos")
public class Vehiculo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "placa", length = 10, nullable = false, unique = true)
	private String placa;

	// automovil, camioneta, campero, microbus, motocicleta
	@Column(name = "tipo_vehiculo", length = 30, nullable = false)
	private String tipoVehiculo;

	@Column(name = "color", length = 20)
	private String color;

	@Column(name = "valor_dia", precision = 10, scale = 2, nullable = false)
	private BigDecimal valorDia;

	// disponible, alquilado, mantenimiento
	@Column(name = "estado", length = 20, nullable = false)
	private String estado;

	@Column(name = "marca", length = 40)
	private String marca;

	@Column(name = "modelo", length = 60)
	private String modelo;

	@Column(name = "anio")
	private Integer anio;

	@Column(name = "pasajeros")
	private Integer pasajeros;

	// Manual, Automatica
	@Column(name = "transmision", length = 20)
	private String transmision;

	// Ruta de la foto (/uploads/vehiculos/xxx.jpg) o URL externa. Si es null el frontend muestra una ilustracion
	@Column(name = "imagen_url", length = 500)
	private String imagenUrl;

	public Vehiculo() {
	}

	public Vehiculo(String placa, String tipoVehiculo, String color, BigDecimal valorDia, String estado) {
		this.placa = placa;
		this.tipoVehiculo = tipoVehiculo;
		this.color = color;
		this.valorDia = valorDia;
		this.estado = estado;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public String getTipoVehiculo() {
		return tipoVehiculo;
	}

	public void setTipoVehiculo(String tipoVehiculo) {
		this.tipoVehiculo = tipoVehiculo;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public BigDecimal getValorDia() {
		return valorDia;
	}

	public void setValorDia(BigDecimal valorDia) {
		this.valorDia = valorDia;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	public Integer getPasajeros() {
		return pasajeros;
	}

	public void setPasajeros(Integer pasajeros) {
		this.pasajeros = pasajeros;
	}

	public String getTransmision() {
		return transmision;
	}

	public void setTransmision(String transmision) {
		this.transmision = transmision;
	}

	public String getImagenUrl() {
		return imagenUrl;
	}

	public void setImagenUrl(String imagenUrl) {
		this.imagenUrl = imagenUrl;
	}
}
