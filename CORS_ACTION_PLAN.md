# 🚀 CORS - PLAN DE ACCIÓN PARA EL EQUIPO

## 📋 Estado Actual

✅ **CORS está completamente configurado en el backend**

El archivo `CorsConfig.java` está en producción permitiendo conexiones desde:
- `http://localhost:5173` (Vite - Desarrollo local)
- `http://localhost:3000` (React - Desarrollo local)

---

## 📍 PASO 1: FRONTEND EN DESARROLLO LOCAL (Ahora)

### Para el Equipo Frontend:

1. **Clone el repo del backend localmente**
2. **Inicie el backend:**
```bash
mvnw spring-boot:run
```

3. **En su proyecto Vite/React, use esta URL base:**
```javascript
const API_URL = 'http://localhost:8080';

// Ejemplo
fetch(`${API_URL}/tasks`)
  .then(r => r.json())
  .then(data => console.log('✅ Funciona:', data))
```

4. **Si ve los datos sin errores CORS → ✅ Está listo**

---

## 📍 PASO 2: DEPLOY BACKEND EN RENDER (Ya hecho)

✅ **El backend ya está deployado:**
```
https://backend-list-task.onrender.com
```

**Verificar que funciona:**
```bash
curl https://backend-list-task.onrender.com/tasks
```

---

## 📍 PASO 3: FRONTEND EN VERCEL (Próximo)

### Cuando desplieguen el frontend en Vercel:

1. **Vercel les dará una URL** como:
```
https://mi-app.vercel.app
```

2. **Avísennos esa URL exacta**

3. **Nosotros actualizamos el backend** editando `CorsConfig.java`:

```java
// Cambiar esta línea:
.allowedOrigins(
    "http://localhost:5173",
    "http://localhost:3000",
    "http://127.0.0.1:5173",
    "http://127.0.0.1:3000"
)

// Por esto:
.allowedOrigins(
    "http://localhost:5173",
    "http://localhost:3000",
    "http://127.0.0.1:5173",
    "http://127.0.0.1:3000",
    "https://mi-app.vercel.app"  // ← AGREGAR AQUÍ
)
```

4. **Recompilamos y deployamos**:
```bash
mvnw clean package
git push  # Render auto-deploya
```

5. **Vercel y Render sincronizados** ✅

---

## 🎯 Timeline

| Fase | Responsable | Estado | Duración |
|------|-------------|--------|----------|
| 1. Dev Local | Frontend | ⏳ En progreso | Semana 1-2 |
| 2. Backend Render | Backend | ✅ Completado | - |
| 3. Frontend Vercel | Frontend | ⏳ Por hacer | Semana 2-3 |
| 4. CORS Update | Backend | ⏳ Pendiente | 5 min |
| 5. Testing | Todos | ⏳ Pendiente | 1 día |

---

## 🧪 Validación por Fase

### Fase 1: Verificar CORS Local

```bash
# Terminal 1: Inicia backend
mvnw spring-boot:run

# Terminal 2: Test desde Vite/React
# Desde consola del navegador (F12):
```

```javascript
fetch('http://localhost:8080/tasks')
  .then(r => r.json())
  .then(data => console.log('✅ OK:', data.length, 'tareas'))
  .catch(e => console.error('❌ Error CORS:', e))
```

**Esperado:**
- Sin errores CORS
- Array de tareas (vacío o con datos)

---

### Fase 2: Verificar API en Render

```bash
# Cualquier navegador
curl https://backend-list-task.onrender.com/tasks
```

**Esperado:**
- Respuesta JSON
- Code 200

---

### Fase 3: Verificar CORS en Vercel

```javascript
// Desde F12 en https://mi-app.vercel.app
fetch('https://backend-list-task.onrender.com/tasks')
  .then(r => r.json())
  .then(data => console.log('✅ CORS Vercel OK:', data))
```

**Esperado:**
- Sin errores CORS
- Datos del backend

---

## 📞 Comunicación Recomendada

### Al Frontend (Ahora):

> **"CORS está configurado. Pueden comenzar a integrar contra `http://localhost:8080` en desarrollo. Cuando desplieguen en Vercel, avísennos la URL exacta para actualizar la whitelist CORS."**

### Al Deploy (Después de Vercel):

> **"Recibimos que el frontend está en `https://mi-app.vercel.app`. Actualizamos CORS, recompilamos y deployamos. Pueden probar ahora."**

---

## ⚠️ Errores Posibles y Soluciones

### Error: "CORS policy blocked"
```
❌ Access to XMLHttpRequest at 'https://backend-list-task.onrender.com/tasks'
   from origin 'https://mi-app.vercel.app' has been blocked by CORS policy
```

**Solución:** No se actualizó la URL en `CorsConfig.java`
→ Verificar que la URL de Vercel esté en `allowedOrigins()`

### Error: "Cannot read property 'json'" en el fetch
```javascript
❌ Uncaught TypeError: Failed to fetch
```

**Causa más común:** No es CORS, es que la API no responde

**Verificación:**
```bash
# Prueba la URL directamente
curl https://backend-list-task.onrender.com/tasks
```

---

## 📚 Documentación Disponible

Se han creado 5 documentos para referencia:

1. **FRONTEND_INTEGRATION_GUIDE.md** ← Empezar aquí
   - Endpoints completos
   - Ejemplos de código
   - Buenas prácticas

2. **CORS_SETUP_GUIDE.md**
   - Pasos de configuración
   - Checklist

3. **CORS_QUICK_REFERENCE.md**
   - Referencia rápida
   - Cheat sheet

4. **CORS_VALIDATION_TEST.md**
   - Métodos de validación
   - Tests con curl/Postman

5. **CORS_VALIDATION_COMPLETE.md**
   - Estado final
   - Resumen ejecutivo

---

## ✅ Checklist de Implementación

### Backend (Completado)
- [x] CorsConfig.java creado
- [x] Configurado para localhost
- [x] Compilado exitosamente
- [x] Deployado en Render
- [ ] Actualizado con URL de Vercel (⏳ Pendiente)
- [ ] Re-deployado en Render (⏳ Pendiente)

### Frontend (En Progreso)
- [ ] Integración local sin CORS
- [ ] Probado GET /tasks
- [ ] Probado POST /tasks
- [ ] Probado PUT /tasks/{id}
- [ ] Probado DELETE /tasks/{id}
- [ ] Deploy en Vercel
- [ ] Validación con CORS de producción

---

## 🎬 Próxima Reunión

Agenda:
1. ✅ Confirmar CORS en desarrollo local funciona
2. ⏳ Avance de integración frontend
3. ⏳ Fecha estimada para deploy Vercel
4. ⏳ Plan de testing integrado

---

## 📧 Email/Mensaje para el Frontend

---

**Asunto: CORS Configurado - Listos para Integración**

Hola equipo frontend,

Tenemos buenas noticias. El **CORS está completamente configurado** en el backend.

**Estado Actual:**
✅ Backend en Render: `https://backend-list-task.onrender.com`
✅ CORS habilitado para desarrollo local
✅ 4 endpoints funcionales: GET, POST, PUT, DELETE

**Próximos Pasos:**

**Fase 1 (Ahora):** Integrar en desarrollo local
- Levanten el backend: `mvnw spring-boot:run`
- Conecten desde `http://localhost:5173` (o `3000`)
- Sin problemas de CORS

**Fase 2 (Cuando desplieguen en Vercel):** 
- Avísennos la URL exacta de Vercel
- Actualizamos CORS en backend
- Todo sigue funcionando

**Documentación:**
- Guía completa: `FRONTEND_INTEGRATION_GUIDE.md`
- Referencias rápidas: `CORS_QUICK_REFERENCE.md`
- Testing: `CORS_VALIDATION_TEST.md`

¿Alguna pregunta? Estamos aquí para ayudar.

Saludos,
Equipo Backend

---

Fin del documento

