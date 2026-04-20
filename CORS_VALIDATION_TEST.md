# 🧪 Validación y Testing CORS

## Métodos para Verificar que CORS Está Funcionando

### Método 1: Test desde la Consola del Navegador (Más Fácil)

#### A. En Desarrollo Local

1. Inicia el backend:
```bash
mvnw spring-boot:run
```

2. Abre el navegador en `http://localhost:5173` (tu frontend Vite)

3. Abre la consola (F12 → Pestaña "Console")

4. Pega este código:
```javascript
fetch('http://localhost:8080/tasks')
  .then(response => {
    console.log('Status:', response.status);
    console.log('Headers:', response.headers.get('Access-Control-Allow-Origin'));
    return response.json();
  })
  .then(data => {
    console.log('✅ CORS Funciona. Datos recibidos:');
    console.table(data);
  })
  .catch(error => {
    console.error('❌ Error CORS:');
    console.error(error);
  });
```

5. Si ves los datos en la consola = ✅ CORS funciona

#### B. En Producción (Render + Vercel)

1. Abre tu app en Vercel: `https://tu-app.vercel.app`

2. Consola (F12)

3. Pega:
```javascript
fetch('https://backend-list-task.onrender.com/tasks')
  .then(response => {
    console.log('Status:', response.status);
    console.log('CORS Origin:', response.headers.get('Access-Control-Allow-Origin'));
    return response.json();
  })
  .then(data => {
    console.log('✅ CORS en Producción Funciona:');
    console.table(data);
  })
  .catch(error => {
    console.error('❌ Error:');
    console.error(error.message);
  });
```

---

### Método 2: Con CURL (Desde Terminal)

#### Test Preflight Request (OPTIONS)

```bash
# Windows PowerShell
$headers = @{
    "Origin" = "http://localhost:5173"
    "Access-Control-Request-Method" = "POST"
}
Invoke-WebRequest -Uri "http://localhost:8080/tasks" -Method OPTIONS -Headers $headers -Verbose
```

```bash
# Linux/Mac/Git Bash
curl -i -X OPTIONS http://localhost:8080/tasks \
  -H "Origin: http://localhost:5173" \
  -H "Access-Control-Request-Method: POST"
```

**Respuesta esperada:**
```
HTTP/1.1 200
Access-Control-Allow-Origin: http://localhost:5173
Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS
Access-Control-Allow-Headers: *
Access-Control-Max-Age: 3600
```

#### Test GET Simple

```bash
# Windows PowerShell
Invoke-WebRequest -Uri "http://localhost:8080/tasks" `
  -Headers @{"Origin" = "http://localhost:5173"} | Format-List
```

```bash
# Linux/Mac
curl -i http://localhost:8080/tasks \
  -H "Origin: http://localhost:5173"
```

---

### Método 3: Con Postman

1. Abre Postman

2. Crea nueva solicitud GET

3. URL: `http://localhost:8080/tasks`

4. Headers:
   - Key: `Origin`
   - Value: `http://localhost:5173`

5. Envía (Send)

6. En Response → Headers, busca `Access-Control-Allow-Origin`

7. Si ves que contiene tu origen = ✅ CORS funciona

---

### Método 4: Test Automatizado (JavaScript)

Crea un archivo `test-cors.js`:

```javascript
/**
 * Script para validar CORS en múltiples orígenes
 */

const API_URL = 'http://localhost:8080'; // Cambiar a producción si es necesario

const origins = [
  'http://localhost:5173',
  'http://localhost:3000',
  'http://127.0.0.1:5173',
  'https://backend-list-task.onrender.com'
];

async function testCorsForOrigin(origin) {
  console.log(`\n🔍 Testeando origin: ${origin}`);
  
  try {
    const response = await fetch(`${API_URL}/tasks`, {
      method: 'GET',
      headers: {
        'Origin': origin,
        'Accept': 'application/json'
      }
    });
    
    const corsOrigin = response.headers.get('Access-Control-Allow-Origin');
    const corsMethods = response.headers.get('Access-Control-Allow-Methods');
    const corsHeaders = response.headers.get('Access-Control-Allow-Headers');
    const corsCredentials = response.headers.get('Access-Control-Allow-Credentials');
    
    console.log(`  Status: ${response.status}`);
    console.log(`  Access-Control-Allow-Origin: ${corsOrigin || 'NO CONFIGURADO'}`);
    console.log(`  Access-Control-Allow-Methods: ${corsMethods}`);
    console.log(`  Access-Control-Allow-Headers: ${corsHeaders}`);
    console.log(`  Access-Control-Allow-Credentials: ${corsCredentials}`);
    
    if (corsOrigin === origin || corsOrigin === '*') {
      console.log(`  ✅ CORS OK para ${origin}`);
    } else {
      console.log(`  ❌ CORS FALLIDO para ${origin}`);
    }
    
  } catch (error) {
    console.error(`  ❌ Error: ${error.message}`);
  }
}

// Ejecutar tests
async function runAllTests() {
  console.log('=== CORS Testing Suite ===\n');
  
  for (const origin of origins) {
    await testCorsForOrigin(origin);
  }
  
  console.log('\n=== Testing Complete ===');
}

// Ejecutar
runAllTests();
```

Uso:
```bash
node test-cors.js
```

---

## ✅ Resultados Esperados

### Desarrollo (Localhost)

```
✅ Status: 200
✅ Access-Control-Allow-Origin: http://localhost:5173
✅ Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS
✅ Access-Control-Allow-Headers: *
✅ Access-Control-Allow-Credentials: true
```

### Producción (Render → Vercel)

```
✅ Status: 200
✅ Access-Control-Allow-Origin: https://tu-app.vercel.app
✅ Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS
✅ Access-Control-Allow-Headers: *
✅ Access-Control-Allow-Credentials: true
```

---

## 🚨 Errores Comunes y Diagnóstico

### Error: "No Access-Control-Allow-Origin header"

**Causa:** CORS no está configurado o la clase no es detectada

**Solución:**
```bash
# 1. Verifica que el archivo existe
ls src/main/java/com/todolist/backend/config/CorsConfig.java

# 2. Recompila
mvnw clean install

# 3. Reinicia el backend
mvnw spring-boot:run

# 4. Intenta de nuevo
```

### Error: "CORS policy: origin 'X' is not allowed"

**Causa:** El origen no está en `allowedOrigins()`

**Solución:** Edita `CorsConfig.java` y agrega el origen faltante

### Error: "method not allowed"

**Causa:** El método HTTP no está en `allowedMethods()`

**Verificación actual:**
```java
.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
```

Si necesitas otro método, agrégalo aquí.

---

## 📊 Verificación Paso a Paso

### Checklist de Validación CORS

- [ ] **Archivo existe:** `src/main/java/com/todolist/backend/config/CorsConfig.java` ✅

- [ ] **Está anotado con @Configuration:**
  ```java
  @Configuration
  public class CorsConfig implements WebMvcConfigurer
  ```

- [ ] **Implementa WebMvcConfigurer:** ✅

- [ ] **Tiene método addCorsMappings():** ✅

- [ ] **allowedOrigins incluye localhost:**
  - `http://localhost:5173` ✅
  - `http://localhost:3000` ✅

- [ ] **allowedMethods incluye necesarios:**
  - GET ✅
  - POST ✅
  - PUT ✅
  - DELETE ✅

- [ ] **allowedHeaders está configurado:** ✅

- [ ] **allowCredentials es true:** ✅

- [ ] **Backend recompilado después de crear el archivo:** [ ]

- [ ] **Backend ejecutándose en puerto 8080:** [ ]

- [ ] **Prueba GET /tasks desde consola sin errores:** [ ]

- [ ] **Frontend puede hacer POST sin errores CORS:** [ ]

---

## 🔧 Solución Rápida si Algo Falla

1. **Recompila forzadamente:**
```bash
mvnw clean compile
```

2. **Borra caché y reinstala:**
```bash
mvnw clean install
```

3. **Inicia en modo verbose:**
```bash
mvnw spring-boot:run -X
```

4. **Busca errores en los logs:**
Verifica que no haya errores al iniciar:
```
Mapping to [..]
Loading class [..]
```

5. **Si todo falla, recrea el archivo:**
```bash
# Borra
del src\main\java\com\todolist\backend\config\CorsConfig.java

# Crea nuevamente (copia el contenido)
```

---

## 📚 Recursos de Referencia

- [Spring CORS Documentation](https://spring.io/guides/gs/cors/)
- [MDN CORS Reference](https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS)
- [CORS Tester Online](https://www.test-cors.org/)

---

## 💡 Próximos Pasos

1. ✅ CORS configurado en desarrollo
2. ⏳ Probar endpoints desde localhost:5173
3. ⏳ Deploy a Render
4. ⏳ Agregar URL de Vercel a CorsConfig
5. ⏳ Deploy final a Render
6. ⏳ Verificar que funciona desde Vercel


