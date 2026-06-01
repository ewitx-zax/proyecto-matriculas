function mostrarToast(mensaje, tipo) {
    let toast = document.getElementById("toast");
    toast.innerHTML = mensaje;
    toast.className = "toast show " + tipo;
    
    setTimeout(function() {
        toast.className = "toast";
    }, 5000); // 5 segundos
}

/* MODAL EDITAR */
function abrirModalEditar(button) {
    let id = button.getAttribute("data-id");
    let username = button.getAttribute("data-username");
    
    document.getElementById("editId").value = id;
    document.getElementById("editUsername").value = username;
    document.getElementById("modalEditar").style.display = "block";
}

function cerrarModalEditar() {
    document.getElementById("modalEditar").style.display = "none";
}

function validarEditar() {
    let pass1 = document.getElementById("editPassword").value;
    let pass2 = document.getElementById("editConfirmPassword").value;
    
    if (pass1 !== pass2) {
        mostrarToast("Las contraseñas no coinciden", "error");
        return false;
    }
    return true;
}

/* MODAL AGREGAR */
function abrirModalAgregar() {
    document.getElementById("modalAgregar").style.display = "block";
}

function cerrarModalAgregar() {
    document.getElementById("modalAgregar").style.display = "none";
}

function validarAgregar() {
    let pass1 = document.getElementById("newPassword").value;
    let pass2 = document.getElementById("confirmNewPassword").value;
    
    if (pass1 !== pass2) {
        mostrarToast("Las contraseñas no coinciden", "error");
        return false;
    }
    return true;
}


