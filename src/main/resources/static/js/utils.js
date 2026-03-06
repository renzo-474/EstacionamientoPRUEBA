/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */


const API_URL = 'http://localhost:8080/api';


function requireAuth() {
    const token = localStorage.getItem('token');
    if (!token) {
        window.location.href = 'login.html';
        return false;
    }
    return true;
}


async function fetchAuth(url, options = {}) {
    const token = localStorage.getItem('token');
    
    const defaultHeaders = {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
    };
    
    const config = {
        ...options,
        headers: {
            ...defaultHeaders,
            ...(options.headers || {})
        }
    };
    
    const response = await fetch(url, config);
    
   
    if (response.status === 401) {
        localStorage.clear();
        window.location.href = 'login.html';
        throw new Error('Sesión expirada');
    }
    
    return response;
}


function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString('es-PE', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    });
}


function formatCurrency(amount) {
    return new Intl.NumberFormat('es-PE', {
        style: 'currency',
        currency: 'PEN'
    }).format(amount);
}

function showNotification(message, type = 'success') {
    const notification = document.createElement('div');
    notification.className = `notification ${type}`;
    notification.textContent = message;
    
    document.body.appendChild(notification);
    
    setTimeout(() => {
        notification.classList.add('show');
    }, 100);
    
    setTimeout(() => {
        notification.classList.remove('show');
        setTimeout(() => {
            notification.remove();
        }, 300);
    }, 3000);
}

function logout() {
    if (confirm('¿Está seguro que desea cerrar sesión?')) {
        localStorage.clear();
        window.location.href = 'login.html';
    }
}

function updateUserInfo() {
    const userName = localStorage.getItem('userName');
    const userRole = localStorage.getItem('userRole');
    
    const userNameElements = document.querySelectorAll('#userName, .user-info');
    userNameElements.forEach(el => {
        if (el.id === 'userName' || el.classList.contains('user-name')) {
            el.textContent = userName || 'Usuario';
        }
    });
    
    const userRoleElements = document.querySelectorAll('.user-role');
    userRoleElements.forEach(el => {
        el.textContent = userRole || 'EMPLEADO';
    });
}


function createModal(title, content) {
    const modal = document.createElement('div');
    modal.className = 'modal';
    modal.innerHTML = `
        <div class="modal-content">
            <div class="modal-header">
                <h2>${title}</h2>
                <button class="close-btn" onclick="closeModal(this)">&times;</button>
            </div>
            <div class="modal-body">
                ${content}
            </div>
        </div>
    `;
    
    document.body.appendChild(modal);
    setTimeout(() => modal.classList.add('show'), 10);
    
    return modal;
}


function closeModal(button) {
    const modal = button.closest('.modal');
    modal.classList.remove('show');
    setTimeout(() => modal.remove(), 300);
}


function createSearchableSelect(selectElement, placeholder = 'Buscar...') {
    const wrapper = document.createElement('div');
    wrapper.className = 'searchable-select';
    
    const searchInput = document.createElement('input');
    searchInput.type = 'text';
    searchInput.className = 'form-control search-input';
    searchInput.placeholder = placeholder;
    
    const optionsList = document.createElement('div');
    optionsList.className = 'options-list';
    
   
    const options = Array.from(selectElement.options).map(opt => ({
        value: opt.value,
        text: opt.text
    }));
    
   
    function showOptions(filter = '') {
        const filtered = options.filter(opt => 
            opt.text.toLowerCase().includes(filter.toLowerCase())
        );
        
        optionsList.innerHTML = filtered.map(opt => `
            <div class="option-item" data-value="${opt.value}">${opt.text}</div>
        `).join('');
        
        optionsList.style.display = 'block';
    }
    
  
    searchInput.addEventListener('input', (e) => {
        showOptions(e.target.value);
    });
    
    searchInput.addEventListener('focus', () => {
        showOptions();
    });
    
    document.addEventListener('click', (e) => {
        if (!wrapper.contains(e.target)) {
            optionsList.style.display = 'none';
        }
    });
    
    optionsList.addEventListener('click', (e) => {
        if (e.target.classList.contains('option-item')) {
            const value = e.target.dataset.value;
            const text = e.target.textContent;
            
            selectElement.value = value;
            searchInput.value = text;
            optionsList.style.display = 'none';
            
          
            selectElement.dispatchEvent(new Event('change'));
        }
    });
    
   
    selectElement.style.display = 'none';
    wrapper.appendChild(searchInput);
    wrapper.appendChild(optionsList);
    selectElement.parentNode.insertBefore(wrapper, selectElement);
    
   
    if (selectElement.value) {
        const selectedOption = options.find(opt => opt.value === selectElement.value);
        if (selectedOption) {
            searchInput.value = selectedOption.text;
        }
    }
}


function createDatePicker(inputElement) {
    // Usar el datepicker nativo de HTML5
    inputElement.type = 'datetime-local';
    inputElement.className = 'form-control';
    

    const now = new Date();
    now.setMinutes(now.getMinutes() - now.getTimezoneOffset());
    inputElement.value = now.toISOString().slice(0, 16);
}

function validateForm(formElement) {
    const inputs = formElement.querySelectorAll('input[required], select[required]');
    let isValid = true;
    
    inputs.forEach(input => {
        if (!input.value.trim()) {
            input.classList.add('error');
            isValid = false;
        } else {
            input.classList.remove('error');
        }
        
  
        if (input.type === 'email' && input.value) {
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailRegex.test(input.value)) {
                input.classList.add('error');
                isValid = false;
            }
        }
        
        if (input.pattern && input.value) {
            const regex = new RegExp(input.pattern);
            if (!regex.test(input.value)) {
                input.classList.add('error');
                isValid = false;
            }
        }
    });
    
    return isValid;
}
function syncSearchableSelects(formElement) {
    const selects = formElement.querySelectorAll('select');

    selects.forEach(select => {
        const wrapper = select.previousElementSibling;
        if (!wrapper || !wrapper.classList.contains('searchable-select')) return;

        const searchInput = wrapper.querySelector('.search-input');
        const searchValue = searchInput?.value?.trim().toLowerCase();

        const matchedOption = Array.from(select.options).find(opt =>
            opt.text.trim().toLowerCase() === searchValue
        );

        if (matchedOption) {
            select.value = matchedOption.value;
        } else {
            console.warn(`No se encontró coincidencia exacta para "${searchValue}" en select con name="${select.name}"`);
            select.value = ''; // puedes poner null si tu backend lo acepta
        }
    });
}
function syncSearchableSelects(formElement) {
    const selects = formElement.querySelectorAll('select');

    selects.forEach(select => {
        const wrapper = select.previousElementSibling;
        if (!wrapper || !wrapper.classList.contains('searchable-select')) return;

        const searchInput = wrapper.querySelector('.search-input');
        const searchValue = searchInput?.value?.trim().toLowerCase();

        const matchedOption = Array.from(select.options).find(opt =>
            opt.text.trim().toLowerCase() === searchValue
        );

        if (matchedOption) {
            select.value = matchedOption.value;
        } else {
            console.warn(`No se encontró coincidencia exacta para "${searchValue}" en select con name="${select.name}"`);
            select.value = '';
        }
    });
}



const utilStyles = `
<style>
.notification {
    position: fixed;
    top: 20px;
    right: 20px;
    padding: 16px 24px;
    border-radius: 8px;
    color: white;
    font-weight: 500;
    z-index: 3000;
    transform: translateX(400px);
    transition: transform 0.3s ease;
}

.notification.show {
    transform: translateX(0);
}

.notification.success {
    background-color: #4caf50;
}

.notification.error {
    background-color: #f44336;
}

.notification.warning {
    background-color: #ff9800;
}

.notification.info {
    background-color: #2196f3;
}

.searchable-select {
    position: relative;
}

.search-input {
    width: 100%;
}

.options-list {
    position: absolute;
    top: 100%;
    left: 0;
    right: 0;
    max-height: 200px;
    overflow-y: auto;
    background: white;
    border: 1px solid #e0e0e0;
    border-radius: 0 0 8px 8px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    display: none;
    z-index: 100;
}

.option-item {
    padding: 10px 14px;
    cursor: pointer;
    transition: background-color 0.2s;
}

.option-item:hover {
    background-color: #f5f5f5;
}

.form-control.error {
    border-color: #f44336;
}

.form-control.error:focus {
    box-shadow: 0 0 0 3px rgba(244, 67, 54, 0.2);
}
</style>
`;

document.head.insertAdjacentHTML('beforeend', utilStyles);

