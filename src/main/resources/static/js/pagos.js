"use strict";

const MES  = document.getElementById("mesSel").value;
const ANIO = document.getElementById("anioSel").value;

async function registrarPago(btn) {
    const fila         = btn.closest("tr");
    const idEstudiante = parseInt(fila.dataset.idEstudiante);

    btn.disabled = true;
    btn.textContent = "Guardando...";

    try {
        const res = await fetch("/api/pagos/registrar", {
            method:  "POST",
            headers: { "Content-Type": "application/json" },
            body:    JSON.stringify({ idEstudiante, mes: parseInt(MES), anio: parseInt(ANIO) })
        });

        if (!res.ok) throw new Error();

        const data = await res.json();

        // Actualizar fila sin recargar
        fila.dataset.idPago  = data.idPago;
        fila.dataset.estado  = "pagado";
        fila.cells[2].innerHTML = '<span class="badge badge-pagado">pagado</span>';
        fila.cells[3].textContent = data.fechaPago;
        fila.cells[4].textContent = "$4.00";
        fila.cells[5].innerHTML = `
            <button class="btn btn-anular" data-id-pago="${data.idPago}" onclick="anularPago(this)">
                ✕ Anular
            </button>`;

        actualizarResumen();
        mostrarToast("Pago registrado correctamente", "ok");

    } catch {
        btn.disabled = false;
        btn.textContent = "✔ Registrar pago";
        mostrarToast("No se pudo registrar el pago", "error");
    }
}

async function anularPago(btn) {
    const idPago = btn.dataset.idPago;
    const fila   = btn.closest("tr");

    if (!confirm("¿Anular este pago?")) return;

    try {
        const res = await fetch(`/api/pagos/${idPago}`, { method: "DELETE" });
        if (!res.ok) throw new Error();

        fila.dataset.idPago = "";
        fila.dataset.estado = "sin registro";
        fila.cells[2].innerHTML = '<span class="badge badge-sin">sin registro</span>';
        fila.cells[3].textContent = "—";
        fila.cells[4].textContent = "—";
        fila.cells[5].innerHTML = `
            <button class="btn btn-pagar" data-id-estudiante="${fila.dataset.idEstudiante}" onclick="registrarPago(this)">
                ✔ Registrar pago
            </button>`;

        actualizarResumen();
        mostrarToast("Pago anulado", "ok");

    } catch {
        mostrarToast("No se pudo anular el pago", "error");
    }
}

function actualizarResumen() {
    const filas     = document.querySelectorAll("tbody tr[data-id-estudiante]");
    let pagados     = 0;
    let pendientes  = 0;
    let total       = 0;

    filas.forEach(f => {
        if (f.dataset.estado === "pagado") { pagados++; total += 4; }
        else if (f.dataset.estado === "pendiente") pendientes++;
    });

    document.querySelectorAll(".resumen-num")[0].textContent = pagados;
    document.querySelectorAll(".resumen-num")[1].textContent = pendientes;
    document.querySelectorAll(".resumen-num")[2].textContent = "$" + total.toFixed(2);
}

function mostrarToast(msg, tipo = "ok") {
    const t = document.getElementById("toast");
    t.textContent   = msg;
    t.className     = `toast toast-${tipo}`;
    t.style.display = "block";
    setTimeout(() => t.style.display = "none", 3000);
}