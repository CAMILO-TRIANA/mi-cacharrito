package cacharrito.config;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 1. Configuración de imágenes/archivos subidos
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registro) {
        try {
            Path carpeta = Paths.get("uploads");
            Files.createDirectories(carpeta);
            String ruta = carpeta.toAbsolutePath().toUri().toString();
            if (!ruta.endsWith("/")) {
                ruta += "/";
            }
            registro.addResourceHandler("/uploads/**").addResourceLocations(ruta);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo preparar la carpeta de imagenes", e);
        }
    }

    // 2. Configuración Global de CORS para permitir peticiones desde Vercel
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(false);
    }
}