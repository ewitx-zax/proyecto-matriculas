"use strict";

/* =============================================
   MODAL INICIAL
   ============================================= */
function elegir(tipo) {
    if (tipo === 'nuevo') {
        ocultarModal("modalTipo");
        document.getElementById("formNuevo").style.display = "block";
        document.getElementById("btn-volver").style.display = "block";
    } else {
        ocultarModal("modalTipo");
        mostrarModal("modalBuscar");
    }
}

function volverModal() {
    ocultarModal("modalBuscar");
    mostrarModal("modalTipo");
    document.getElementById("nie-buscar").value = "";
    document.getElementById("error-buscar").style.display = "none";
}

function volverBuscar() {
    ocultarModal("modalActualizar");
    mostrarModal("modalBuscar");
}

/* =============================================
   BUSCAR ESTUDIANTE POR NIE
   ============================================= */
async function buscarEstudiante() {
    const nie      = document.getElementById("nie-buscar").value.trim();
    const errorDiv = document.getElementById("error-buscar");

    if (!nie) {
        errorDiv.textContent = "Ingresa el NIE del estudiante.";
        errorDiv.style.display = "block";
        return;
    }

    try {
        const res = await fetch(`/api/estudiantes/nie/${encodeURIComponent(nie)}`);

        if (res.status === 404) {
            errorDiv.textContent = "No se encontró ningún estudiante con ese NIE.";
            errorDiv.style.display = "block";
            return;
        }

        if (!res.ok) throw new Error();

        const e = await res.json();
        errorDiv.style.display = "none";

        // Llenar info en modal actualizar
        document.getElementById("info-nombre").textContent =
            [e.primerNombre, e.segundoNombre, e.primerApellido, e.segundoApellido]
            .filter(Boolean).join(" ");
        document.getElementById("info-nie").textContent    = e.nie ?? "—";
        document.getElementById("info-actual").textContent =
            `${e.bachilleratoNombre ?? "—"} | ${e.gradoNombre ?? "—"} | Sección ${e.seccionNombre ?? "—"} | ${e.anioLectivo ?? "—"}`;

        // Guardar id para el PUT
        window._estudianteId = e.idEstudiante;

        ocultarModal("modalBuscar");
        mostrarModal("modalActualizar");

    } catch {
        errorDiv.textContent = "Error de conexión. Intenta de nuevo.";
        errorDiv.style.display = "block";
    }
}

/* =============================================
   ACTUALIZAR MATRÍCULA
   ============================================= */
async function actualizarMatricula() {
    const idBachillerato = document.getElementById("upd-bachillerato").value;
    const idGrado        = document.getElementById("upd-grado").value;
    const idSeccion      = document.getElementById("upd-seccion").value;
    const anioLectivo    = document.getElementById("upd-anio").value;

    if (!idBachillerato || !idGrado || !idSeccion || !anioLectivo) {
        mostrarToast("Completa todos los campos antes de guardar.", "error");
        return;
    }

    try {
        const res = await fetch(`/api/estudiantes/${window._estudianteId}/matricula`, {
            method:  "PUT",
            headers: { "Content-Type": "application/json" },
            body:    JSON.stringify({
                idBachillerato: parseInt(idBachillerato),
                idGrado:        parseInt(idGrado),
                idSeccion:      parseInt(idSeccion),
                anioLectivo:    parseInt(anioLectivo)
            })
        });

        if (!res.ok) throw new Error();

        ocultarModal("modalActualizar");
        mostrarToast("Matrícula actualizada correctamente ✓", "ok");

        setTimeout(() => window.location.href = "/gestion-matricula", 2000);

    } catch {
        mostrarToast("No se pudo actualizar. Intenta de nuevo.", "error");
    }
}

/* =============================================
   CARGA DINÁMICA GRADOS Y SECCIONES — FORMULARIO NUEVO
   ============================================= */
function cargarGrados(idBachillerato) {
    const gradoSelect   = document.getElementById("grado-select");
    const seccionSelect = document.getElementById("seccion-select");

    gradoSelect.innerHTML   = '<option value="">Seleccione grado</option>';
    seccionSelect.innerHTML = '<option value="">Primero seleccione grado</option>';
    gradoSelect.disabled    = true;
    seccionSelect.disabled  = true;

    if (!idBachillerato) return;

    const grados = GRADOS_POR_BACHILLERATO[idBachillerato] || [];
    grados.forEach(g => {
        const opt = document.createElement("option");
        opt.value       = g.id;
        opt.textContent = g.nombre;
        gradoSelect.appendChild(opt);
    });

    gradoSelect.disabled = false;
}

function cargarSecciones(idGrado) {
    const seccionSelect = document.getElementById("seccion-select");
    seccionSelect.innerHTML = '<option value="">Seleccione sección</option>';
    seccionSelect.disabled  = true;

    if (!idGrado) return;

    const secciones = SECCIONES_POR_GRADO[idGrado] || [];
    secciones.forEach(s => {
        const opt = document.createElement("option");
        opt.value       = s.id;
        opt.textContent = s.nombre;
        seccionSelect.appendChild(opt);
    });

    seccionSelect.disabled = false;
}

/* =============================================
   CARGA DINÁMICA — MODAL ACTUALIZAR
   ============================================= */
function cargarGradosModal(idBachillerato) {
    const gradoSelect   = document.getElementById("upd-grado");
    const seccionSelect = document.getElementById("upd-seccion");

    gradoSelect.innerHTML   = '<option value="">Seleccione grado</option>';
    seccionSelect.innerHTML = '<option value="">Primero seleccione grado</option>';
    gradoSelect.disabled    = true;
    seccionSelect.disabled  = true;

    if (!idBachillerato) return;

    const grados = GRADOS_POR_BACHILLERATO[idBachillerato] || [];
    grados.forEach(g => {
        const opt = document.createElement("option");
        opt.value       = g.id;
        opt.textContent = g.nombre;
        gradoSelect.appendChild(opt);
    });

    gradoSelect.disabled = false;
    gradoSelect.onchange = () => cargarSeccionesModal(gradoSelect.value);
}

function cargarSeccionesModal(idGrado) {
    const seccionSelect = document.getElementById("upd-seccion");
    seccionSelect.innerHTML = '<option value="">Seleccione sección</option>';
    seccionSelect.disabled  = true;

    if (!idGrado) return;

    const secciones = SECCIONES_POR_GRADO[idGrado] || [];
    secciones.forEach(s => {
        const opt = document.createElement("option");
        opt.value       = s.id;
        opt.textContent = s.nombre;
        seccionSelect.appendChild(opt);
    });

    seccionSelect.disabled = false;
}

/* =============================================
   CAMPO FECHA PARTO
   ============================================= */
function toggleParto(val) {
    document.getElementById("campo-parto").style.display =
        val === "true" ? "block" : "none";
}

/* =============================================
   HELPERS MODALES
   ============================================= */
function mostrarModal(id) {
    document.getElementById(id).style.display = "flex";
}

function ocultarModal(id) {
    document.getElementById(id).style.display = "none";
}

/* =============================================
   TOAST
   ============================================= */
function mostrarToast(msg, tipo = "ok") {
    const t = document.getElementById("toast");
    t.textContent   = msg;
    t.className     = `toast toast-${tipo}`;
    t.style.display = "block";
    setTimeout(() => t.style.display = "none", 3000);
}

/* ESC cierra modales secundarios */
document.addEventListener("keydown", e => {
    if (e.key === "Escape") {
        ocultarModal("modalBuscar");
        ocultarModal("modalActualizar");
    }
});