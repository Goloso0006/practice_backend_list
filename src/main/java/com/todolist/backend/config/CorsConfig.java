package com.todolist.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración de CORS (Cross-Origin Resource Sharing)
 * Permite que el frontend (en diferentes dominios) acceda a los endpoints del backend
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                // Aplicar CORS a todos los endpoints (/tasks/**)
                .addMapping("/tasks/**")
                // Permitir origen local en desarrollo (Vite se ejecuta en puerto 5173)
                .allowedOrigins(
                        "http://localhost:5173",      // Frontend local (Vite)
                        "http://localhost:3000",      // Frontend local (alternativo)
                        "http://127.0.0.1:5173",      // localhost alternativo
                        "http://127.0.0.1:3000"       // localhost alternativo
                )
                // URL del frontend en producción (cambiar cuando sepas el dominio exacto)
                // IMPORTANTE: Actualizar después del deploy en Vercel
                // .allowedOrigins("https://tu-proyecto.vercel.app")
                
                // Métodos HTTP permitidos
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                
                // Headers permitidos
                .allowedHeaders("*")
                
                // Credenciales (cookies, authorization headers, etc.)
                .allowCredentials(true)
                
                // Tiempo en segundos que el navegador cachea la respuesta preflight
                .maxAge(3600);
        
        // Opcional: configuración adicional para otros endpoints si es necesario
        // registry.addMapping("/public/**")
        //         .allowedOrigins("*")
        //         .allowedMethods("GET");
    }
}

