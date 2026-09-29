package cacharrito.util;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import cacharrito.modelo.Administrador;
import cacharrito.modelo.Usuario;
import cacharrito.modelo.Vehiculo;
import cacharrito.repositorio.administrador;
import cacharrito.repositorio.usuario;
import cacharrito.repositorio.vehiculo;

// Carga datos de ejemplo la primera vez que arranca con la base de datos vacia,
// para poder entrar al panel del administrador y ver el catalogo sin insertar nada a mano.
@Component
public class DatosIniciales implements CommandLineRunner {

	@Autowired
	private administrador repoAdministrador;

	@Autowired
	private usuario repoUsuario;

	@Autowired
	private vehiculo repoVehiculo;

	@Override
	public void run(String... args) {
		if (repoAdministrador.count() == 0) {
			// Administrador: usuario "admin" / contraseña "admin123"
			repoAdministrador.save(new Administrador("Administrador Mi Cacharrito", "admin", "admin123"));
		}

		if (repoUsuario.count() == 0) {
			// Cliente de prueba: identificacion "1000000001" / contraseña "cliente123"
			repoUsuario.save(new Usuario("1000000001", "Cliente de Prueba", LocalDate.of(2020, 3, 15), "B1",
					LocalDate.now().plusYears(4), "cliente@correo.com", "3001234567", "cliente123"));
		}

		if (repoVehiculo.count() == 0) {
			repoVehiculo.save(crear("KLM123", "automovil", "Renault", "Logan", 2022, 5, "Manual", "Blanco", 120000));
			repoVehiculo.save(crear("NPQ456", "automovil", "Chevrolet", "Spark GT", 2021, 4, "Manual", "Rojo", 95000));
			repoVehiculo.save(crear("RST789", "automovil", "Mazda", "3 Touring", 2023, 5, "Automática", "Gris", 180000));
			repoVehiculo.save(crear("UVW321", "camioneta", "Toyota", "Hilux", 2022, 5, "Automática", "Negro", 320000));
			repoVehiculo.save(crear("XYZ654", "camioneta", "Renault", "Duster", 2023, 5, "Manual", "Naranja", 210000));
			repoVehiculo.save(crear("CAL001", "campero", "Jeep", "Willys CJ-5", 1974, 4, "Manual", "Verde", 160000));
			repoVehiculo.save(crear("BDF987", "campero", "Suzuki", "Jimny", 2022, 4, "Manual", "Amarillo", 190000));
			repoVehiculo.save(crear("GHJ246", "microbus", "Hyundai", "H1", 2021, 12, "Manual", "Blanco", 350000));
			repoVehiculo.save(crear("MNB135", "microbus", "Toyota", "Hiace", 2022, 14, "Manual", "Blanco", 380000));
			repoVehiculo.save(crear("ABC12D", "motocicleta", "Yamaha", "FZ 150", 2023, 2, "Manual", "Azul", 60000));
			repoVehiculo.save(crear("DEF34E", "motocicleta", "Honda", "XR 150L", 2022, 2, "Manual", "Rojo", 55000));
			repoVehiculo.save(crear("GHI56F", "motocicleta", "AKT", "NKD 125", 2021, 2, "Manual", "Negro", 45000));
		}
	}

	private Vehiculo crear(String placa, String tipo, String marca, String modelo, int anio, int pasajeros,
			String transmision, String color, int valorDia) {
		Vehiculo v = new Vehiculo(placa, tipo, color, BigDecimal.valueOf(valorDia), "disponible");
		v.setMarca(marca);
		v.setModelo(modelo);
		v.setAnio(anio);
		v.setPasajeros(pasajeros);
		v.setTransmision(transmision);
		return v;
	}
}
