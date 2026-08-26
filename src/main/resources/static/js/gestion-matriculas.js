"use strict";

/* -----------------------------------------------
   Lee los data-* de la fila para llenar el modal
----------------------------------------------- */
let filaActual = null;
let idAEliminar = null;

function abrirEdicion(btn) {
    const fila = btn.closest("tr");
    filaActual = fila;

    document.getElementById("edit-id").value      = fila.dataset.id;
    document.getElementById("edit-nombre").value  = fila.dataset.nombre.trim();
    document.getElementById("edit-nie").value     = fila.dataset.nie !== "null" ? fila.dataset.nie : "";
    document.getElementById("edit-estado").value  = fila.dataset.estado ?? "Activo";
    document.getElementById("edit-correo").value  = fila.dataset.correo !== "null" ? fila.dataset.correo : "";

    mostrarModal("modalEdicion");
}

async function guardarEdicion(e) {
    e.preventDefault();

    const id      = document.getElementById("edit-id").value;
    const nombre  = document.getElementById("edit-nombre").value.trim();
    const nie     = document.getElementById("edit-nie").value.trim();
    const estado  = document.getElementById("edit-estado").value;
    const correo  = document.getElementById("edit-correo").value.trim();

    // Separar nombre en primerNombre y primerApellido (simple: primer token = nombre, último = apellido)
    const partes        = nombre.split(" ").filter(Boolean);
    const primerNombre  = partes[0] ?? "";
    const primerApellido = partes.length > 1 ? partes[partes.length - 1] : "";

    const payload = {
        nie,
        primerNombre,
        primerApellido,
        estadoPersona: estado,
        correo
    };

    try {
        const res = await fetch(`/api/estudiantes/${id}`, {
            method:  "PUT",
            headers: { "Content-Type": "application/json" },
            body:    JSON.stringify(payload)
        });

        if (!res.ok) throw new Error(`HTTP ${res.status}`);

        // Actualizar la fila en pantalla sin recargar
        filaActual.dataset.nombre  = nombre;
        filaActual.dataset.nie     = nie;
        filaActual.dataset.estado  = estado;
        filaActual.dataset.correo  = correo;

        filaActual.cells[1].textContent = nie || "—";
        filaActual.cells[2].textContent = nombre;
        filaActual.cells[3].innerHTML   = badgeEstado(estado);
        filaActual.cells[4].textContent = correo || "—";

        cerrarModal();
        mostrarToast("Estudiante actualizado correctamente", "ok");

    } catch (err) {
        console.error(err);
        mostrarToast("No se pudo guardar. Intenta de nuevo.", "error");
    }
}

/* -----------------------------------------------
   ELIMINAR
----------------------------------------------- */
function pedirConfirmacion(btn) {
    const fila = btn.closest("tr");
    idAEliminar = fila.dataset.id;
    document.getElementById("nombre-a-eliminar").textContent = fila.dataset.nombre.trim();
    filaActual = fila;
    mostrarModal("modalEliminar");
}

async function confirmarEliminar() {
    try {
        const res = await fetch(`/api/estudiantes/${idAEliminar}`, {
            method: "DELETE"
        });

        if (!res.ok) throw new Error(`HTTP ${res.status}`);

        filaActual.remove();
        cerrarModalEliminar();
        mostrarToast("Estudiante eliminado", "ok");
        actualizarContadorVisible();

    } catch (err) {
        console.error(err);
        mostrarToast("No se pudo eliminar. Intenta de nuevo.", "error");
    } finally {
        idAEliminar = null;
    }
}

/* -----------------------------------------------
   BÚSQUEDA (client-side sobre las filas ya renderizadas)
----------------------------------------------- */
function filtrarTabla() {
    const q     = document.getElementById("busqueda").value.toLowerCase().trim();
    const filas = document.querySelectorAll("#tablaBody tr[data-id]");
    let visibles = 0;

    filas.forEach(fila => {
        const texto = [
            fila.dataset.nombre,
            fila.dataset.nie,
            fila.dataset.estado,
            fila.dataset.correo
        ].join(" ").toLowerCase();

        const mostrar = !q || texto.includes(q);
        fila.style.display = mostrar ? "" : "none";
        if (mostrar) visibles++;
    });

    document.getElementById("contador-resultados").textContent =
        `${visibles} de ${filas.length} registros`;
}

function actualizarContadorVisible() {
    const filas    = document.querySelectorAll("#tablaBody tr[data-id]");
    const visibles = [...filas].filter(f => f.style.display !== "none").length;
    document.getElementById("contador-resultados").textContent =
        `${visibles} de ${filas.length} registros`;
}

/* -----------------------------------------------
   HELPERS
----------------------------------------------- */
function badgeEstado(estado) {
    const mapa = { "Activo": "badge-activa", "Inactivo": "badge-inactiva", "Pendiente": "badge-pendiente" };
    return `<span class="badge ${mapa[estado] ?? 'badge-pendiente'}">${estado}</span>`;
}

function mostrarModal(id) {
    const overlay = document.getElementById(id);
    overlay.style.display = "flex";
    overlay.onclick = (e) => { if (e.target === overlay) overlay.style.display = "none"; };
}

function cerrarModal()        { document.getElementById("modalEdicion").style.display  = "none"; }
function cerrarModalEliminar(){ document.getElementById("modalEliminar").style.display = "none"; }

function mostrarToast(msg, tipo = "ok") {
    const t = document.getElementById("toast");
    t.textContent  = msg;
    t.className    = `toast toast-${tipo}`;
    t.style.display = "block";
    setTimeout(() => t.style.display = "none", 3000);
}

document.addEventListener("keydown", e => {
    if (e.key === "Escape") { cerrarModal(); cerrarModalEliminar(); }
});

// Contador inicial
document.addEventListener("DOMContentLoaded", () => {
    const total = document.querySelectorAll("#tablaBody tr[data-id]").length;
    document.getElementById("contador-resultados").textContent = `${total} registros`;
});