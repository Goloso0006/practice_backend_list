# 🚀 Guía: Desplegar en Render desde GitHub Actions

Ahora que tienes tu CI/CD configurado en GitHub Actions, es momento de conectarlo con **Render** para desplegar automáticamente.

---

## 📋 Opciones de Integración

### Opción A: Render se conecta a GitHub (RECOMENDADO - MÁS SIMPLE)
```
Tu Código en GitHub
         ↓
    [Workflow CI/CD]
         ↓
    Validación ✅
         ↓
    Render monitorea GitHub
         ↓
    Render hace su propio build
         ↓
    Despliegue en Render 🎉
```

**Ventajas:**
- ✅ Muy simple de configurar
- ✅ No necesitas secrets en GitHub
- ✅ Render maneja todo

**Desventajas:**
- ❌ Render compila todo nuevamente (lento)
- ❌ Si hay error en Render, no lo saben en GitHub

---

### Opción B: GitHub Actions genera artifacts + Render los descarga
```
Tu Código en GitHub
         ↓
    [Workflow CI/CD]
         ↓
    ✅ Validación OK
         ↓
    🎁 Genera artifacts
       (JAR + dist)
         ↓
    Render descarga artifacts
         ↓
    Render despliega 🎉
```

**Ventajas:**
- ✅ Más rápido (no recompila)
- ✅ GitHub valida antes de enviar a Render

**Desventajas:**
- ❌ Requiere configuración de webhook
- ❌ Más complejo

---

## 🎯 Implementar Opción A (Simple)

### Paso 1: Crear Servicio en Render

1. Ve a [render.com](https://render.com)
2. Haz login o crea cuenta
3. Haz clic en **"New +"** → **"Web Service"**

### Paso 2: Conectar con GitHub

1. Selecciona **"Public Git repository"**
2. Pega la URL de tu repo: `https://github.com/tu-usuario/backend`
3. Haz clic en **"Connect"**
4. Autoriza a Render acceder a tu GitHub

### Paso 3: Configurar el Servicio

```
Name: to-do-list-backend
Environment: Docker
Branch: main
Repository: backend (tu repo)
```

### Paso 4: Build Command

Para un proyecto Java + Frontend híbrido, usa:

```bash
# Build del Backend (Java)
./mvnw clean package -DskipTests

# Build del Frontend (Node)
cd my-app && npm ci && npm run build && cd ..

# Copiar dist al JAR si es necesario
# (opcional, depende de tu setup)
```

### Paso 5: Start Command

```bash
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

### Paso 6: Configurar Servidor Java

1. **Environment:** Selecciona `Docker`
2. **Plan:** Selecciona plan (Free está bien para empezar)
3. **Add Environment Variable** (si necesitas):
   - `JAVA_VERSION=25`
   - `MAVEN_OPTS=-Xmx512m`

### Paso 7: Crear Dockerfile

Crea un archivo `Dockerfile` en la raíz de tu repo:

```dockerfile
# Stage 1: Build Backend
FROM maven:3.9-eclipse-temurin-25 AS backend-builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Stage 2: Build Frontend
FROM node:20-alpine AS frontend-builder
WORKDIR /app/my-app
COPY my-app/package*.json ./
RUN npm ci
COPY my-app ./
RUN npm run build

# Stage 3: Runtime
FROM eclipse-temurin:25-jdk
WORKDIR /app
COPY --from=backend-builder /app/target/*.jar app.jar
COPY --from=frontend-builder /app/my-app/dist ./static
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
```

Este Dockerfile:
1. Compila el backend Java
2. Compila el frontend React
3. Copia ambos al JAR final
4. Ejecuta el backend que sirve el frontend estático

### Paso 8: Deploy

1. Haz clic en **"Deploy"**
2. Espera a que compile (~15-20 minutos la primera vez)
3. ¡Listo! Tu app está en vivo en `https://tu-app.onrender.com`

---

## 🔄 Flujo Automático después

Después de configurar:

```
Tu haces: git push a main
           ↓
GitHub Actions se ejecuta
           ↓
✅ Backend Validation
✅ Frontend Validation
✅ Backend Build → JAR artifact
✅ Frontend Build → dist artifact
           ↓
Render detecta nuevo commit
           ↓
Render ejecuta Build Command
           ↓
Render ejecuta Start Command
           ↓
Tu app actualizada en vivo 🎉
```

---

## 🎯 Implementar Opción B (Avanzado)

Si quieres que GitHub Actions despliegue directamente a Render:

### Paso 1: Obtener API Key de Render

1. Ve a Render Dashboard
2. Account Settings → API Keys
3. Copia tu API key

### Paso 2: Agregar Secret a GitHub

1. Ve a GitHub → Settings → Secrets and variables → Actions
2. Haz clic en "New repository secret"
3. Nombre: `RENDER_API_KEY`
4. Valor: Pega tu API key de Render

### Paso 3: Agregar Deploy Job al Workflow

Edita `.github/workflows/ci.yml` y agrega al final:

```yaml
  deploy-render:
    name: 🚀 Deploy a Render
    runs-on: ubuntu-latest
    needs: [backend-build, frontend-build]
    if: github.ref == 'refs/heads/main' && github.event_name == 'push'
    # Solo despliega en push a main (no en PRs)
    
    steps:
      - name: Trigger Render Deploy
        run: |
          curl --request POST \
            --url https://api.render.com/v1/services/srv-XXXXX/deploys \
            --header "authorization: Bearer ${{ secrets.RENDER_API_KEY }}" \
            --header "content-type: application/json"
        # Reemplaza srv-XXXXX con tu Service ID de Render
```

**Cómo obtener tu Service ID:**
1. Ve a tu servicio en Render
2. Ve a Settings
3. Busca "Service ID"

### Paso 4: Deploy automático

Ahora:
```bash
git push origin main
# GitHub Actions se ejecuta
# Si todo ✅, dispara deploy a Render automáticamente
# Si algo ❌, no despliega (protege tu producción)
```

---

## 🐛 Troubleshooting

### Problema: Render no ve mi repositorio
**Solución:**
1. Ve a Render → Account → Connected Services
2. Reconecta GitHub
3. Autoriza acceso a tu repositorio

### Problema: Build falla en Render pero funciona localmente
**Solución común:** Diferencia de versiones
```bash
# Verificar qué tienes localmente
java -version
node -v
npm -v
mvn -v

# Configurar en Render las mismas versiones
# Environment Variables en Render
```

### Problema: Puerto en uso
**Solución:** Spring Boot usa puerto 8080 por defecto. Render lo expone automáticamente.
Si quieres cambiar:
```bash
# En application.properties
server.port=8080
```

### Problema: Timeout en Render
**Síntoma:** "Build took too long"
**Solución:**
```bash
# En tu Dockerfile o Build Command, agrega:
-Dmaven.wagon.http.retryHandler.class=standard
-Dmaven.wagon.http.retryHandler.count=10
```

---

## 📊 Costo en Render

| Plan | Precio | Uso Recomendado |
|------|--------|-----------------|
| Free | $0/mes | Desarrollo, testing |
| Starter | $7/mes | Producción pequeña |
| Standard | $25/mes | Producción mediana |

**Nota:** Con el plan Free, tu app se detiene después de 15 min de inactividad.

---

## ✅ Checklist de Despliegue

- [ ] Cuenta en Render.com creada
- [ ] Repositorio conectado a Render
- [ ] Build Command configurado
- [ ] Start Command configurado
- [ ] Variables de entorno configuradas
- [ ] Dockerfile creado (si usas Docker)
- [ ] Despliegue inicial exitoso
- [ ] Configurar dominio personalizado (opcional)
- [ ] Configurar CI/CD de Render (si quieres auto-deploy)
- [ ] Tests en producción (manual)

---

## 🎓 Resumen de la Arquitectura

```
┌─────────────────────────────────────────────────────────┐
│                    GitHub Codespace                      │
│  Tu máquina local desarrollando la app                   │
└──────────────────────┬──────────────────────────────────┘
                       │ git push
                       ↓
┌─────────────────────────────────────────────────────────┐
│                  GitHub Repository                       │
│  main, develop branches                                  │
└──────────────────────┬──────────────────────────────────┘
                       │ Trigger
                       ↓
┌─────────────────────────────────────────────────────────┐
│               GitHub Actions (CI/CD)                     │
│  ✓ backend-validation (Java + Maven)                    │
│  ✓ frontend-validation (TypeScript + ESLint)            │
│  ✓ backend-build (JAR)                                  │
│  ✓ frontend-build (dist)                                │
│  ✓ summary (Resumen)                                    │
│  ✓ deploy-render (Opcional - desplegar)                │
└──────────────────────┬──────────────────────────────────┘
                       │
              ┌────────┴────────┐
              ↓                 ↓
    [Artifacts]         [Webhook/API]
    ├─ backend-jar      Render API
    ├─ frontend-build   Deploy trigger
    └─ coverage
              │                 │
              └────────┬────────┘
                       ↓
┌─────────────────────────────────────────────────────────┐
│                   Render Deployment                      │
│  https://tu-app.onrender.com                            │
│  ├─ Backend (Spring Boot) :8080                         │
│  ├─ Frontend (React) :3000 (served by backend)          │
│  └─ Database (si lo configuras)                         │
└──────────────────────────────────────────────────────────┘
```

---

## 🎉 Resultado Final

Después de todo esto, tienes:

✅ **Validación automática** de código en cada push
✅ **Compilación** de Backend (Java) y Frontend (TypeScript)
✅ **Tests automáticos** ejecutándose
✅ **Artifacts** generados y guardados 30 días
✅ **Despliegue automático** a Render (opcional)
✅ **Monitoreo** en GitHub Actions Dashboard
✅ **Seguridad** - código validado antes de desplegar

**Esto es lo que hacen empresas profesionales como Google, Netflix, Microsoft. ¡Felicidades! 🚀**

---

## 📞 Próximas Lecciones (Opcionales)

1. Agregar monitoring y alertas
2. Configurar variables de entorno por ambiente
3. Agregar análisis de seguridad
4. Configurar base de datos en Render
5. Agregar backup automático
6. Configurar CDN para el frontend
7. Tests de integración E2E
8. Rollback automático si falla

---

**¡Tu pipeline CI/CD profesional está listo para producción! 🎊**

