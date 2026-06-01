// =============================================
// JS para Login
// =============================================

document.addEventListener('DOMContentLoaded', function() {
    
    console.log('✅ Página de Login cargada correctamente');
    
    // Puedes agregar validaciones adicionales aquí en el futuro
    // Ejemplo: mostrar/ocultar contraseña, etc.
    
});

// Opcional: Validación en el lado del cliente antes de enviar
function validarLogin() {
    const username = document.querySelector('input[th\\:field="*{username}"]').value;
    const password = document.querySelector('input[th\\:field="*{password}"]').value;
    
    if (!username || !password) {
        alert("Por favor complete todos los campos");
        return false;
    }
    return true;
}


