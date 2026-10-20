"use strict";

/* =============================================
   FLUJO: Nuevo usuario con verificación admin
   Paso 1 → ¿Eres admin?
   Paso 2 → Credenciales admin (verificadas en backend)
   Paso 3 → Formulario nuevo usuario
   ============================================= */

function abrirModalAdmin() {
    mostrarModal("modalVerificar");
}

function mostrarCredenciales() {
    ocultarModal("modalVerificar");
    document.getElementById("admin-user").value = "";
    document.getElementById("admin-pass").value = "";
    document.getElementById("error-admin").style.display = "none";
    mostrarModal("modalCredenciales");
}

async function verificarAdmin() {
    const username = document.getElementById("admin-user").value.trim();
    const password = document.getElementById("admin-pass").value;
    const errorDiv = document.getElementById("error-admin");

    if (!username || !password) {
        errorDiv.textContent = "Completa todos los campos.";
        errorDiv.style.display = "block";
        return;
    }

    try {
        const res = await fetch("/api/verificar-admin", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ username, password })
        });

        if (res.ok) {
            ocultarModal("modalCredenciales");
            document.getElementById("nuevo-user").value = "";
            document.getElementById("nuevo-pass").value = "";
            const rolSelect = document.getElementById("nuevo-rol");
            if (rolSelect) {
                rolSelect.value = "docente";
            }
            document.getElementById("error-crear").style.display = "none";
            mostrarModal("modalNuevoUsuario");
        } else {
            errorDiv.textContent = "Usuario o contraseña incorrectos.";
            errorDiv.style.display = "block";
        }

    } catch (err) {
        errorDiv.textContent = "Error de conexión. Intenta de nuevo.";
        errorDiv.style.display = "block";
    }
}

async function crearUsuario(e) {
    e.preventDefault();

    const username = document.getElementById("nuevo-user").value.trim();
    const password = document.getElementById("nuevo-pass").value;
    const rolSelect = document.getElementById("nuevo-rol");
    const rol = rolSelect ? rolSelect.value : "docente";
    const errorDiv = document.getElementById("error-crear");

    if (!username || !password) {
        errorDiv.textContent = "Completa todos los campos.";
        errorDiv.style.display = "block";
        return;
    }

    try {
        const res = await fetch("/api/crear-usuario", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ username, password, rol })
        });

        if (res.ok) {
            cerrarTodo();
            mostrarToast("Usuario creado correctamente ✓", "ok");
            setTimeout(() => location.reload(), 1500);
        } else {
            const errorMsg = await res.text();
            errorDiv.textContent = errorMsg || "No se pudo crear el usuario. Intenta de nuevo.";
            errorDiv.style.display = "block";
        }

    } catch (err) {
        errorDiv.textContent = "Error de conexión. Intenta de nuevo.";
        errorDiv.style.display = "block";
    }
}

/* ---- HELPERS MODALES ---- */
function mostrarModal(id) {
    const overlay = document.getElementById(id);
    if (overlay) {
        overlay.style.display = "flex";
        overlay.onclick = (e) => { if (e.target === overlay) cerrarTodo(); };
    }
}

function ocultarModal(id) {
    const overlay = document.getElementById(id);
    if (overlay) {
        overlay.style.display = "none";
    }
}

function cerrarTodo() {
    ["modalVerificar", "modalCredenciales", "modalNuevoUsuario"].forEach(ocultarModal);
}

/* ---- TOAST ---- */
function mostrarToast(msg, tipo = "ok") {
    const t = document.getElementById("toast");
    if (t) {
        t.textContent = msg;
        t.className = `toast toast-${tipo}`;
        t.style.display = "block";
        setTimeout(() => t.style.display = "none", 3500);
    }
}

/* ---- ESC cierra modales ---- */
document.addEventListener("keydown", e => {
    if (e.key === "Escape") cerrarTodo();
});