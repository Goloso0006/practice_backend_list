# -----------------------------------------------------------------------------
# ETAPA 1 (builder)
# -----------------------------------------------------------------------------
# Usamos una imagen que ya trae Maven + JDK 17 para compilar la aplicación.
# El alias "builder" permite referirnos a esta etapa más adelante.
FROM maven:3.9.9-eclipse-temurin-17 AS builder

# Directorio de trabajo dentro del contenedor para todas las instrucciones
# siguientes (equivale a hacer "cd /app").
WORKDIR /app

# Copiamos primero Maven Wrapper y su configuración para que Docker pueda
# cachear dependencias entre builds cuando no cambian los fuentes.
COPY mvnw mvnw
COPY .mvn .mvn
COPY pom.xml pom.xml

# Damos permisos de ejecución al wrapper en entorno Linux.
RUN chmod +x mvnw

# Copiamos el código fuente de la aplicación.
COPY src src

# Compilamos y empaquetamos el proyecto en un JAR dentro del contenedor.
# - clean: limpia artefactos previos.
# - package: genera el .jar en /app/target.
# -DskipTests: omite tests para acelerar el build de imagen.
RUN ./mvnw clean package -DskipTests

# -----------------------------------------------------------------------------
# ETAPA 2 (runtime)
# -----------------------------------------------------------------------------
# Imagen más ligera solo para ejecutar la app (sin Maven).
FROM amazoncorretto:17-alpine-jdk

# Directorio de trabajo de la imagen final.
WORKDIR /app

# Copiamos el JAR generado en la etapa "builder".
# Esto evita depender de /target local o de artefactos subidos al repositorio.
COPY --from=builder /app/target/*.jar app_list.jar

# Documentamos el puerto en el que escucha Spring Boot.
EXPOSE 8080

# Comando de arranque del contenedor.
ENTRYPOINT ["java", "-jar", "app_list.jar"]