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
            // IMPORTANTE: Guardar en sesión que el admin fue verificado
            await fetch("/api/guardar-verificacion-admin", {
                method: "POST"
            });
            
            // Mostrar modal de nuevo usuario
            ocultarModal("modalCredenciales");
            document.getElementById("nuevo-user").value = "";
            document.getElementById("nuevo-pass").value = "";
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
    const errorDiv = document.getElementById("error-crear");

    if (!username || !password) {
        errorDiv.textContent = "Completa todos los campos.";
        errorDiv.style.display = "block";
        return;
    }

    try {
        // Usar el nuevo endpoint que NO requiere sesión
        const params = new URLSearchParams({ username, password });
        const res = await fetch("/crear-usuario-desde-login", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: params.toString()
        });

        if (res.ok) {
            cerrarTodo();
            mostrarToast("Usuario creado correctamente ✓", "ok");
            // Opcional: recargar la página o actualizar lista de usuarios
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
    overlay.style.display = "flex";
    overlay.onclick = (e) => { if (e.target === overlay) cerrarTodo(); };
}

function ocultarModal(id) {
    document.getElementById(id).style.display = "none";
}

function cerrarTodo() {
    ["modalVerificar", "modalCredenciales", "modalNuevoUsuario"].forEach(ocultarModal);
}

/* ---- TOAST ---- */
function mostrarToast(msg, tipo = "ok") {
    const t = document.getElementById("toast");
    t.textContent = msg;
    t.className = `toast toast-${tipo}`;
    t.style.display = "block";
    setTimeout(() => t.style.display = "none", 3500);
}

/* ---- ESC cierra modales ---- */
document.addEventListener("keydown", e => {
    if (e.key === "Escape") cerrarTodo();
});