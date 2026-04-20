# 🔐 Configuración CORS - Backend To-Do List

## ¿Qué es CORS?

**CORS** (Cross-Origin Resource Sharing) es un mecanismo de seguridad que controla desde qué dominios se puede acceder a tu API.

### Problema sin CORS:
```
Access to XMLHttpRequest at 'https://backend-list-task.onrender.com/tasks' 
from origin 'https://mi-app.vercel.app' has been blocked by CORS policy
```

---

## ✅ CORS YA ESTÁ CONFIGURADO

**Archivo creado:** `src/main/java/com/todolist/backend/config/CorsConfig.java`

### Orígenes permitidos en Desarrollo:
- ✅ `http://localhost:5173` (Vite)
- ✅ `http://localhost:3000` (React/Next.js)

### Orígenes en Producción:
- ⚠️ Pendiente agregar URL de Vercel

---

## 🔧 Pasos para Completar

### Paso 1: Recompilar el Backend

```bash
mvnw clean install
```

### Paso 2: Probar Localmente

Inicia el backend:
```bash
mvnw spring-boot:run
```

Desde el navegador (F12):
```javascript
fetch('http://localhost:8080/tasks')
  .then(r => r.json())
  .then(data => console.log('✅ CORS funciona:', data))
```

### Paso 3: Cuando el Frontend esté en Vercel

Obtén la URL de Vercel (ej: `https://mi-app.vercel.app`)

Edita: `src/main/java/com/todolist/backend/config/CorsConfig.java`

Busca:
```java
// .allowedOrigins("https://tu-proyecto.vercel.app")
```

Reemplaza con:
```java
.allowedOrigins(
    "http://localhost:5173",           
    "http://localhost:3000",           
    "https://mi-app.vercel.app"        // Tu URL de Vercel
)
```

### Paso 4: Recompila y Deploy

```bash
mvnw clean package
```

Sube a Git para que Render lo despliegue automáticamente.

### Paso 5: Verifica desde Vercel

Abre F12 en tu app de Vercel:

```javascript
fetch('https://backend-list-task.onrender.com/tasks')
  .then(r => r.json())
  .then(data => console.log('✅ Listo:', data))
  .catch(e => console.error('❌ Error:', e))
```

Si ves los datos, ¡CORS funciona! 🎉

---

## 📋 Checklist

- [x] CorsConfig.java creado
- [ ] Backend recompilado
- [ ] Probado localmente
- [ ] Deploy en Render
- [ ] Frontend en Vercel
- [ ] URL de Vercel agregada a CORS
- [ ] Deploy final en Render
- [ ] Verificado desde Vercel

