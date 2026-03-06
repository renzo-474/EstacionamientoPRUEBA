/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */




const API_URL = 'http://localhost:8080/api';


const loginForm = document.getElementById('loginForm');
const errorDiv = document.createElement('div');
errorDiv.className = 'error-message';
loginForm.parentNode.insertBefore(errorDiv, loginForm);


function validateInput(input) {
    const pattern = /^[a-zA-Z0-9]+$/;
    return pattern.test(input);
}


loginForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value;
    const submitButton = loginForm.querySelector('button[type="submit"]');
    

    if (!validateInput(username)) {
        showError('El usuario solo puede contener letras y números');
        return;
    }
    

    submitButton.classList.add('loading');
    submitButton.disabled = true;
    hideError();
    
    try {
        const response = await fetch(`${API_URL}/auth/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                idEmpleado: username,
                password: password
            })
        });
        
        const data = await response.json();
        
        if (response.ok && data.success) {
         
            localStorage.setItem('token', data.token);
            localStorage.setItem('userId', data.idEmpleado);
            localStorage.setItem('userName', data.nombre);
            localStorage.setItem('userRole', data.rol);
            
 
            window.location.href = 'dashboard.html';
        } else {
            showError(data.details || 'Credenciales inválidas');
        }
    } catch (error) {
        console.error('Error:', error);
        showError('Error de conexión. Por favor, intente nuevamente.');
    } finally {
        submitButton.classList.remove('loading');
        submitButton.disabled = false;
    }
});


function showError(message) {
    errorDiv.textContent = message;
    errorDiv.classList.add('show');
}

// Ocultar mensaje de error
function hideError() {
    errorDiv.classList.remove('show');
}

function checkAuth() {
    const token = localStorage.getItem('token');
    if (token) {
     
        verifyToken(token);
    }
}


async function verifyToken(token) {
    try {
        const response = await fetch(`${API_URL}/auth/verify`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        
        const data = await response.json();
        
        if (data.valid) {
           
            window.location.href = 'dashboard.html';
        } else {
            
            localStorage.clear();
        }
    } catch (error) {
        console.error('Error verificando token:', error);
        localStorage.clear();
    }
}


document.addEventListener('DOMContentLoaded', () => {
    checkAuth();
    

    document.getElementById('username').focus();
});