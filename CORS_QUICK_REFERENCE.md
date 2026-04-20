# 📝 CORS - Referencia Rápida

## Estado: ✅ COMPLETAMENTE CONFIGURADO

### Archivo de Configuración
```
src/main/java/com/todolist/backend/config/CorsConfig.java
```

---

## ✅ EN DESARROLLO

El backend ya permite solicitudes desde:
- `http://localhost:5173` ← Vite default
- `http://localhost:3000` ← React default
- `http://127.0.0.1:5173` ← localhost alternativo
- `http://127.0.0.1:3000` ← localhost alternativo

### Test rápido:
```javascript
// En consola del navegador (F12)
fetch('http://localhost:8080/tasks')
  .then(r => r.json())
  .then(console.log)
```

Sin errores CORS = ✅ Funciona

---

## ⚠️ EN PRODUCCIÓN (Después de Vercel)

### 1. Obtén tu URL de Vercel
Ejemplo: `https://mi-todolist.vercel.app`

### 2. Edita CorsConfig.java

Cambia esto:
```java
.allowedOrigins(
    "http://localhost:5173",
    "http://localhost:3000",
    "http://127.0.0.1:5173",
    "http://127.0.0.1:3000"
    // .allowedOrigins("https://tu-proyecto.vercel.app")  ← Descomentar
)
```

Por esto:
```java
.allowedOrigins(
    "http://localhost:5173",
    "http://localhost:3000",
    "http://127.0.0.1:5173",
    "http://127.0.0.1:3000",
    "https://mi-todolist.vercel.app"  ← Agregar tu URL
)
```

### 3. Recompila y Deploy
```bash
mvnw clean package
# Sube a Git, Render deploya automáticamente
```

### 4. Verifica desde Vercel
```javascript
fetch('https://backend-list-task.onrender.com/tasks')
  .then(r => r.json())
  .then(console.log)
```

Si ves datos = ✅ CORS funciona en producción

---

## 🔍 Troubleshooting CORS

| Error | Causa | Solución |
|-------|-------|----------|
| `CORS policy blocked` | Dominio no está en allowedOrigins | Agregar a CorsConfig.java |
| `No Access-Control-Allow-Origin header` | CORS no configurado | ✅ Ya está configurado |
| `Credentials mode 'include' is not allowed with wildcard` | Conflicto de config | No usar `allowedOrigins("*")` con `allowCredentials(true)` |

---

## 📞 Resumen

✅ **CORS funciona en desarrollo**

⏳ **Después del deploy en Vercel:**
1. Obtén URL de Vercel
2. Edita CorsConfig.java
3. Agrega la URL
4. Recompila
5. Deploy en Render
6. ¡Listo!

---

**URL del Backend:** `https://backend-list-task.onrender.com`

**Documentación completa:** Ver `FRONTEND_INTEGRATION_GUIDE.md`

