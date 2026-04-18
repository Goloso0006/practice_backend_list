# 🚀 Quick Reference - Workflow de CI/CD

## 📍 Archivo Creado
```
.github/
  └── workflows/
      └── ci.yml          ← Este archivo contiene todo el workflow
```

## 🎯 ¿Qué Hace Este Workflow?

```
Tu código en GitHub
       ↓
   [TRIGGER]
   • push a main/develop
   • PR hacia main/develop
   • Ejecución manual
       ↓
   [VALIDACIÓN PARALELA]
   ┌─────────────────┐  ┌──────────────────┐
   │  Backend Tests  │  │  Frontend Tests  │
   │ (Java + Maven)  │  │(TypeScript+ESLint)
   │ ✓ Compila Java  │  │ ✓ Compila TS    │
   │ ✓ Tests Maven   │  │ ✓ Lint (ESLint) │
   └────────┬────────┘  └────────┬─────────┘
            │                    │
       ✅ o ❌              ✅ o ❌
            │                    │
       [BUILD]                [BUILD]
       │                      │
       ├─→ JAR             ├─→ dist/
       │   artifact           artifact
       │                      │
       └────────────┬─────────┘
                    ↓
              [SUMMARY]
              Resumen visual
              de todo
```

---

## 🔑 5 Jobs Principales

| # | Job | Objetivo | Tiempo | Depende de |
|---|-----|----------|--------|-----------|
| 1️⃣ | `backend-validation` | Compilar Java + Tests | 2-3 min | Nada |
| 2️⃣ | `frontend-validation` | TypeScript + Lint | 1-2 min | Nada |
| 3️⃣ | `backend-build` | Generar JAR | 1-2 min | Job 1 ✅ |
| 4️⃣ | `frontend-build` | Generar dist/ | 1 min | Job 2 ✅ |
| 5️⃣ | `summary` | Resumen visual | <1 min | Siempre |
| | **TOTAL** | | **5-8 min** | - |

---

## 📂 Artifacts Generados

### ✅ Si Todo Exitoso:
```
Artifacts disponibles por 30 días:

1. backend-jar
   └── target/backend-0.0.1-SNAPSHOT.jar
       (JAR ejecutable del Spring Boot)

2. frontend-build
   └── my-app/dist/
       (HTML, CSS, JS optimizado)
```

### ❌ Si Falla:
```
backend-validation falló
    ↓
backend-build no se ejecuta
    ↓
Workflow completo: FAILED ❌
```

---

## 🎯 Triggers (Cuándo Se Ejecuta)

### ✅ Se ejecuta en:
- `git push` a `main`
- `git push` a `develop`
- Pull Request hacia `main` o `develop`
- Ejecución manual (GitHub UI)

### ❌ NO se ejecuta en:
- Push a otras ramas (feature/, hotfix/, etc.)
- Cambios solo en documentación (README.md, etc.) - a menos que hagas push
- Commits vacíos

---

## 🔧 Cómo Probar el Workflow

### Opción 1: Ejecución Manual (Más Rápido)
```bash
1. Ve a GitHub → Actions
2. Haz clic en "CI - To-Do List App"
3. Click "Run workflow" (botón verde)
4. Selecciona rama (main o develop)
5. Espera resultados
```

### Opción 2: Push a GitHub (Automático)
```bash
git add .
git commit -m "Testing CI workflow"
git push origin main
```

### Opción 3: Pull Request
```bash
git checkout -b feature/test
git push origin feature/test
# Abre PR en GitHub hacia main
# El workflow se ejecuta automáticamente
```

---

## 📊 Cache (Acelerar Ejecuciones)

### Maven Cache (.m2)
```yaml
cache: maven
```
- **Primera ejecución:** Descarga ~500MB de dependencias
- **Siguientes:** Usa el cache (~1 segundo)
- **Ahorro:** 2-3 minutos por ejecución

### NPM Cache
```yaml
cache: npm
```
- **Primera ejecución:** Descarga ~200MB
- **Siguientes:** Usa cache (~5 segundos)
- **Ahorro:** 1-2 minutos por ejecución

---

## ⚙️ Comandos Ejecutados

### Backend
```bash
./mvnw clean compile           # Compila Java
./mvnw test                    # Ejecuta tests (JUnit, etc.)
./mvnw clean package -DskipTests  # Genera JAR
```

### Frontend
```bash
npm ci                         # Instala dependencias exactas
npm run build                  # Compila TypeScript + Vite
npm run lint                   # Valida con ESLint
```

---

## 🐛 Debug: ¿Qué Hacer Si Falla?

### Si falla `backend-validation`:
1. Haz click en el job en GitHub Actions
2. Lee el log (busca "ERROR")
3. Errores comunes:
   - Sintaxis Java incorrecta
   - Test fallando
   - Java version mismatch

**Solución:**
```bash
# Ejecuta localmente
./mvnw clean compile
./mvnw test
```

### Si falla `frontend-validation`:
1. Revisa el log
2. Errores comunes:
   - Error en TypeScript
   - Regla ESLint violada
   - Dependencia faltante

**Solución:**
```bash
cd my-app
npm ci
npm run build
npm run lint
```

---

## 🌍 Despliegue en Render

### Opción A: Render hace su propio build (Recomendado)
1. Conecta GitHub a Render
2. Selecciona rama `main`
3. Render automáticamente:
   - Descarga código de GitHub
   - Ejecuta `./mvnw clean package` (Backend)
   - Ejecuta `npm run build` (Frontend)
   - Despliega todo

**Ventaja:** Simple, no necesitas este workflow
**Desventaja:** Más lento, no hay validación previa

### Opción B: Usar artifacts de GitHub Actions
1. Tu GitHub Actions genera JAR + dist
2. Render descarga los artifacts
3. Render despliega directamente

**Ventaja:** Más rápido, código ya validado
**Desventaja:** Configuración más compleja

**Para ahora:** Usa **Opción A**, es más simple.

---

## 📝 Anatomía del Workflow YAML

```yaml
name: 🚀 CI - To-Do List App
# Nombre que ves en GitHub Actions

on:
  push:
    branches: [main, develop]
  pull_request:
    branches: [main, develop]
  workflow_dispatch:
# Triggers (cuándo ejecutar)

concurrency:
  group: ci-${{ github.ref }}
  cancel-in-progress: true
# Si hay múltiples ejecuciones, cancela la anterior

jobs:
  backend-validation:      # Job ID (identificador)
    name: 🔍 Validación    # Nombre legible en la UI
    runs-on: ubuntu-latest # Sistema operativo
    permissions:
      contents: read       # Permisos (solo lectura)
    
    steps:                 # Pasos a ejecutar
      - name: 📥 Checkout
        uses: actions/checkout@v4
        # Usa una acción predefinida
      
      - name: 📦 Compilar
        run: ./mvnw clean compile
        # Ejecuta comando shell
```

---

## ✅ Checklist de Configuración

- [x] Workflow YAML creado en `.github/workflows/ci.yml`
- [x] Job de validación backend (Maven)
- [x] Job de validación frontend (npm)
- [x] Job de build backend (JAR)
- [x] Job de build frontend (dist)
- [x] Job de summary
- [x] Cache de dependencias
- [x] Artifacts configurados
- [ ] Conectar con Render (próximo paso)
- [ ] Configurar variables de entorno (si necesario)

---

## 🎓 Conceptos Aprendidos

Después de implementar esto, ahora entiendes:

✅ Qué es CI/CD
✅ Cómo funcionan los workflows de GitHub Actions
✅ Validación automática de código
✅ Tests en la nube
✅ Artifacts y descargas
✅ Parallelización de tareas
✅ Dependencias entre jobs
✅ Caché para optimizar
✅ Integración con Render

---

## 🔗 Comandos Útiles

```bash
# Ver estado del workflow localmente
./mvnw clean compile     # Backend
cd my-app && npm run build  # Frontend

# Verificar qué hace cada comando
cat pom.xml              # Ver configuración Maven
cat my-app/package.json  # Ver scripts disponibles

# Si haces cambios
git add .
git commit -m "Update workflow"
git push origin main
# GitHub Actions se ejecuta automáticamente
```

---

## 💡 Tips & Tricks

1. **Usar GitHub Actions CLI para debug local:**
   ```bash
   # Instalar: https://cli.github.com/
   gh workflow list
   gh run list
   gh run view <run-id> --log
   ```

2. **Crear ramas de prueba:**
   ```bash
   git checkout -b test/ci-workflow
   # Haz cambios al workflow
   git push origin test/ci-workflow
   # Ve a GitHub y abre PR para ver el workflow en acción
   ```

3. **Monitorear recursos:**
   - GitHub Actions tiene 2,000 minutos/mes GRATIS
   - Este workflow consume ~5-8 minutos por ejecución
   - Con 10 pushes diarios: ~60-80 minutos/mes (✅ Dentro del límite)

---

## 📞 Próximos Pasos

1. ✅ Push tu código a GitHub (rama main/develop)
2. ⏳ Espera a que el workflow se ejecute (~5-8 min)
3. 📊 Ve a GitHub Actions y verifica los resultados
4. 🎯 Descarga los artifacts para probar
5. 🚀 Configura despliegue automático en Render
6. 🎓 Estudia el archivo `.github/workflows/ci.yml` para entender cada paso

---

**¡Ahora tienes un pipeline profesional de CI/CD! 🎉**

Para cualquier duda, consulta el archivo `CI_CD_ANALYSIS.md` para explicaciones detalladas.

