# Guia: instalar DBeaver y conectar PostgreSQL de Render

Esta guia te muestra como instalar **DBeaver Community** en Windows y conectarlo a tu base PostgreSQL en Render para ver tablas y datos.

## Checklist rapido

- [ ] Descargar e instalar DBeaver Community
- [ ] Copiar datos de conexion desde Render (External Database URL)
- [ ] Crear conexion PostgreSQL en DBeaver
- [ ] Activar SSL (require)
- [ ] Probar conexion
- [ ] Abrir tabla `task` y consultar datos

---

## 1) Requisitos

- Tener tu base PostgreSQL creada en Render.
- Tener estos datos disponibles (desde Render DB):
  - `External Database URL`
  - `Username`
  - `Password`
- Internet habilitado (conexion externa a la DB).

> Importante: para apps fuera de Render (como DBeaver en tu PC), usa **External Database URL**.

---

## 2) Descargar DBeaver (Windows)

1. Ve al sitio oficial de DBeaver.
2. Descarga **DBeaver Community** para Windows.
3. Ejecuta el instalador y deja las opciones por defecto.
4. Abre DBeaver.

---

## 3) Sacar datos correctos desde Render

En tu base PostgreSQL de Render, ubica:

- `External Database URL`
- `Username`
- `Password`

La URL externa suele verse asi:

```text
postgres://USER:PASSWORD@HOST:5432/DATABASE
```

Para DBeaver puedes usar:

- host: `HOST`
- puerto: `5432`
- database: `DATABASE`
- user: `USER`
- password: `PASSWORD`

---

## 4) Crear la conexion en DBeaver

1. Click en **Database** -> **New Database Connection**.
2. Selecciona **PostgreSQL**.
3. Completa los campos:
   - **Host**: el host del External URL
   - **Port**: `5432`
   - **Database**: nombre de tu base (ej. `task_list_0hyi`)
   - **Username**: tu usuario (ej. `task_list_0hyi_user`)
   - **Password**: tu password
4. Marca **Save password locally** si quieres evitar escribirla cada vez.

---

## 5) Configurar SSL

En la ventana de conexion:

1. Abre la pestaña **SSL**.
2. Activa SSL.
3. Usa modo **require** (si aparece selector de modo).

> Esto coincide con tu backend, que usa `sslmode=require`.

---

## 6) Probar conexion

1. Click en **Test Connection**.
2. Si pide descargar driver, acepta.
3. Si todo sale bien, click en **Finish**.

Si falla, revisa:

- Que usaste **External** (no Internal) URL.
- Usuario/password correctos.
- Puerto `5432`.
- SSL activo.

---

## 7) Ver la tabla `task`

1. En el panel izquierdo, abre:
   - tu conexion -> **Schemas** -> `public` -> **Tables**
2. Busca la tabla `task`.
3. Click derecho -> **View Data** -> **All rows**.

---

## 8) Consultas SQL utiles

Abre SQL Editor y ejecuta:

```sql
SELECT * FROM task;
```

```sql
SELECT COUNT(*) FROM task;
```

```sql
SELECT id, title, completed
FROM task
ORDER BY id DESC
LIMIT 20;
```

---

## 9) Troubleshooting rapido

### Error: connection timed out

- Confirma que usas `External Database URL`.
- Verifica internet/firewall local.

### Error: password authentication failed

- Revisa usuario/password exactos.
- Si cambiaste password en Render, actualiza DBeaver.

### Error SSL

- Activa SSL y modo `require`.

---

## 10) Seguridad (importante)

- No compartas credenciales en chats/publico.
- Si alguna se expuso, rota password en Render.
- Usa un gestor de contrasenas para guardar secretos.

---

## Resultado esperado

Si todo esta bien, podras:

- Ver la tabla `task` creada por tu backend.
- Confirmar que las tareas del frontend se guardan en PostgreSQL.
- Ejecutar consultas SQL para validar datos en tiempo real.

