FROM openjdk:17-jdk-slim

# Definir la variable JAR_FILE para el nombre del archivo JAR
ARG JAR_FILE=target/app_list_task-0.0.1.jar

# Copiar el archivo JAR al contenedor
COPY ${JAR_FILE} app_list.jar

# Exponer el puerto 8080
EXPOSE 8080

# Configurar el contenedor para ejecutar el JAR
ENTRYPOINT ["java", "-jar", "app_list.jar"]