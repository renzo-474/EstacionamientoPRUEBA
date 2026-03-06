/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */


if (!requireAuth()) {
    throw new Error('No autenticado');
}

updateUserInfo();

let entradasActivas = [];
let salidas = [];
let clientes = [];
let currentTab = 'activos';
const TARIFAS = {
    1: 5.00, 
    2: 3.00,  
    3: 7.00   
};


async function loadInitialData() {
    try {
       
        await loadClientes();
        
       
        await loadEntradasActivas();
        
     
        await loadSalidas();
        
     
        setDefaultDateTime();
        
    } catch (error) {
        console.error('Error cargando datos:', error);
        showNotification('Error al cargar los datos', 'error');
    }
}

async function loadClientes() {
    try {
        const response = await fetchAuth(`${API_URL}/clientes`);
        
        if (!response.ok) {
            throw new Error('Error al cargar clientes');
        }
        
        clientes = await response.json();
        
    
        const select = document.getElementById('idCliente2');
        select.innerHTML = '<option value="">Mismo de la entrada</option>';
        
        clientes.forEach(cliente => {
            const option = document.createElement('option');
            option.value = cliente.idCliente;
            option.textContent = `${cliente.nombres} ${cliente.apellidos}`;
            select.appendChild(option);
        });
        
    } catch (error) {
        console.error('Error cargando clientes:', error);
    }
}


async function loadEntradasActivas() {
    try {
        const response = await fetchAuth(`${API_URL}/entradas/activas`);
        
        if (!response.ok) {
            throw new Error('Error al cargar entradas activas');
        }
        
        entradasActivas = await response.json();
        
    
        fillEntradasSelect();
        
      
        renderActivosTable();
        
    } catch (error) {
        console.error('Error:', error);
        showNotification('Error al cargar entradas activas', 'error');
    }
}


function fillEntradasSelect() {
    const select = document.getElementById('idEntrada');
    select.innerHTML = '<option value="">Seleccione...</option>';
    
    entradasActivas.forEach(entrada => {
        const option = document.createElement('option');
        option.value = entrada.idEntrada;
        option.textContent = `${entrada.idEntrada} - ${entrada.nroPlaca || 'Sin placa'} - ${entrada.nombreCliente || 'Sin cliente'}`;
        select.appendChild(option);
    });
}


async function loadSalidas() {
    try {
        const response = await fetchAuth(`${API_URL}/salidas`);
        
        if (!response.ok) {
            throw new Error('Error al cargar salidas');
        }
        
        salidas = await response.json();
        renderSalidasTable();
        
    } catch (error) {
        console.error('Error:', error);
        showNotification('Error al cargar las salidas', 'error');
    }
}


function renderActivosTable() {
    const tbody = document.getElementById('activosTableBody');
    
    if (entradasActivas.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="7" style="text-align: center; padding: 40px;">
                    No hay vehículos activos en el estacionamiento
                </td>
            </tr>
        `;
        return;
    }
    
    tbody.innerHTML = entradasActivas.map(entrada => {
        const tiempoEstacionado = calcularTiempoEstacionado(entrada.fechaIngreso, entrada.horaIngreso);
        
        return `
            <tr>
                <td><strong>${entrada.idEntrada}</strong></td>
                <td>${entrada.nroPlaca || '-'}</td>
                <td>${entrada.nombreCliente || '-'}</td>
                <td>${formatDateTime(entrada.fechaIngreso, entrada.horaIngreso)}</td>
                <td>${tiempoEstacionado}</td>
                <td>Nivel ${entrada.nivel} - ${entrada.zona || '-'}</td>
                <td>
                    <button class="btn btn-sm btn-primary" onclick="registrarSalidaRapida('${entrada.idEntrada}')">
                        Registrar Salida
                    </button>
                </td>
            </tr>
        `;
    }).join('');
}


function renderSalidasTable() {
    const tbody = document.getElementById('salidasTableBody');
    
    if (salidas.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="8" style="text-align: center; padding: 40px;">
                    No hay salidas registradas
                </td>
            </tr>
        `;
        return;
    }
    
    tbody.innerHTML = salidas.map(salida => {
        const tiempoTotal = salida.registroEntrada ? 
            calcularTiempoEstacionado(salida.registroEntrada.fechaIngreso, salida.registroEntrada.horaIngreso, salida.fechaSalida, salida.horaSalida) : 
            '-';
        
        return `
            <tr>
                <td><strong>${salida.idSalida}</strong></td>
                <td>${salida.registroEntrada?.nroPlaca || '-'}</td>
                <td>${salida.nombreCliente || '-'}</td>
                <td>${salida.registroEntrada ? formatDateTime(salida.registroEntrada.fechaIngreso, salida.registroEntrada.horaIngreso) : '-'}</td>
                <td>${formatDateTime(salida.fechaSalida, salida.horaSalida)}</td>
                <td>${tiempoTotal}</td>
                <td>${formatCurrency(salida.montoTotal || 0)}</td>
                <td>
                    <button class="btn btn-sm btn-secondary" onclick="verDetalleSalida('${salida.idSalida}')">
                        Ver Detalle
                    </button>
                </td>
            </tr>
        `;
    }).join('');
}


function calcularTiempoEstacionado(fechaIngreso, horaIngreso, fechaSalida = null, horaSalida = null) {
    try {
    
        let fechaEntrada = new Date(fechaIngreso);
        if (horaIngreso) {
            if (Array.isArray(horaIngreso) && horaIngreso.length >= 2) {
                fechaEntrada.setHours(horaIngreso[0], horaIngreso[1], 0);
            }
        }
        
    
        let fechaSalidaObj = fechaSalida ? new Date(fechaSalida) : new Date();
        if (fechaSalida && horaSalida) {
            if (Array.isArray(horaSalida) && horaSalida.length >= 2) {
                fechaSalidaObj.setHours(horaSalida[0], horaSalida[1], 0);
            }
        }
        
    
        const diff = fechaSalidaObj - fechaEntrada;
        const horas = Math.floor(diff / (1000 * 60 * 60));
        const minutos = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));
        
        return `${horas}h ${minutos}m`;
        
    } catch (error) {
        return '-';
    }
}


function formatDateTime(fecha, hora) {
    if (!fecha) return '-';
    
    const fechaObj = new Date(fecha);
    const fechaStr = fechaObj.toLocaleDateString('es-PE');
    
    let horaStr = '-';
    if (hora) {
        if (Array.isArray(hora) && hora.length >= 2) {
            horaStr = `${hora[0].toString().padStart(2, '0')}:${hora[1].toString().padStart(2, '0')}`;
        } else if (typeof hora === 'string') {
            horaStr = hora.substring(0, 5);
        }
    }
    
    return `${fechaStr} ${horaStr}`;
}


function showTab(tab) {
    currentTab = tab;
    
  
    document.getElementById('tabActivos').classList.toggle('active', tab === 'activos');
    document.getElementById('tabSalidas').classList.toggle('active', tab === 'salidas');
    
   
    document.getElementById('activosContent').style.display = tab === 'activos' ? 'block' : 'none';
    document.getElementById('salidasContent').style.display = tab === 'salidas' ? 'block' : 'none';
}


document.getElementById('searchInput').addEventListener('input', (e) => {
    const searchTerm = e.target.value.toLowerCase();
    
    if (currentTab === 'activos') {
        const filtered = entradasActivas.filter(entrada => 
            (entrada.nroPlaca && entrada.nroPlaca.toLowerCase().includes(searchTerm)) ||
            (entrada.nombreCliente && entrada.nombreCliente.toLowerCase().includes(searchTerm))
        );
        renderActivosTable(filtered);
    } else {
        const filtered = salidas.filter(salida => 
            (salida.registroEntrada?.nroPlaca && salida.registroEntrada.nroPlaca.toLowerCase().includes(searchTerm)) ||
            (salida.nombreCliente && salida.nombreCliente.toLowerCase().includes(searchTerm))
        );
        renderSalidasTable(filtered);
    }
});


function showAddSalidaModal() {
    document.getElementById('modalTitle').textContent = 'Registrar Salida';
    document.getElementById('salidaForm').reset();
    document.getElementById('entryInfo').style.display = 'none';
    setDefaultDateTime();
    document.getElementById('salidaModal').classList.add('show');
}

function registrarSalidaRapida(idEntrada) {
    showAddSalidaModal();
    document.getElementById('idEntrada').value = idEntrada;
    loadEntryInfo();
}

function closeSalidaModal() {
    document.getElementById('salidaModal').classList.remove('show');
    document.getElementById('salidaForm').reset();
    document.getElementById('entryInfo').style.display = 'none';
}


function setDefaultDateTime() {
    const now = new Date();
    
 
    const year = now.getFullYear();
    const month = (now.getMonth() + 1).toString().padStart(2, '0');
    const day = now.getDate().toString().padStart(2, '0');
    document.getElementById('fechaSalida').value = `${year}-${month}-${day}`;
    
 
    const hours = now.getHours().toString().padStart(2, '0');
    const minutes = now.getMinutes().toString().padStart(2, '0');
    document.getElementById('horaSalida').value = `${hours}:${minutes}`;
}

function loadEntryInfo() {
    const idEntrada = document.getElementById('idEntrada').value;
    
    if (!idEntrada) {
        document.getElementById('entryInfo').style.display = 'none';
        return;
    }
    
    const entrada = entradasActivas.find(e => e.idEntrada === idEntrada);
    
    if (entrada) {
        document.getElementById('infoPlaca').textContent = entrada.nroPlaca || '-';
        document.getElementById('infoCliente').textContent = entrada.nombreCliente || '-';
        document.getElementById('infoIngreso').textContent = formatDateTime(entrada.fechaIngreso, entrada.horaIngreso);
        document.getElementById('infoUbicacion').textContent = `Nivel ${entrada.nivel} - ${entrada.zona || '-'}`;
        document.getElementById('entryInfo').style.display = 'block';

     
        document.getElementById('idCliente2').value = "";

        calculateAmount();
    }
}


function calculateAmount() {
    const idEntrada = document.getElementById('idEntrada').value;
    const codTipoVhc = document.getElementById('codTipoVhc').value;
    
    if (!idEntrada || !codTipoVhc) {
        document.getElementById('totalAmount').textContent = 'Total a pagar: S/. 0.00';
        return;
    }
    
    const entrada = entradasActivas.find(e => e.idEntrada === idEntrada);
    if (!entrada) return;
    

    const fechaEntrada = new Date(entrada.fechaIngreso);
    if (entrada.horaIngreso && Array.isArray(entrada.horaIngreso)) {
        fechaEntrada.setHours(entrada.horaIngreso[0], entrada.horaIngreso[1], 0);
    }
    
    const fechaSalida = new Date(document.getElementById('fechaSalida').value);
    const horaSalida = document.getElementById('horaSalida').value.split(':');
    fechaSalida.setHours(parseInt(horaSalida[0]), parseInt(horaSalida[1]), 0);
    
    const diff = fechaSalida - fechaEntrada;
    const horas = Math.ceil(diff / (1000 * 60 * 60)); 
    
    const tarifa = TARIFAS[codTipoVhc] || 5.00;
    const total = horas * tarifa;
    
    document.getElementById('totalAmount').textContent = `Total a pagar: ${formatCurrency(total)}`;
}


document.getElementById('fechaSalida').addEventListener('change', calculateAmount);
document.getElementById('horaSalida').addEventListener('change', calculateAmount);
document.getElementById('salidaForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    if (!validateForm(e.target)) {
        return;
    }
    
    const formData = new FormData(e.target);
    
    // Obtener el ID del empleado actual
    const idEmpleado = localStorage.getItem('userId');
    
    // Formatear fecha y hora
    const fecha = formData.get('fechaSalida');
    const hora = formData.get('horaSalida');
    const fechaHora = `${fecha}T${hora}:00`;
    
    // Obtener la entrada seleccionada
    const idEntrada = formData.get('idEntrada');
    const entrada = entradasActivas.find(e => e.idEntrada === idEntrada);

    if (!entrada) {
        showNotification('No se encontró la entrada seleccionada', 'error');
        return;
    }

    // Determinar el cliente final
    const clienteSeleccionado = formData.get('idCliente2');
    let idClienteFinal;
    
    // Si se seleccionó un cliente específico, usarlo
    if (clienteSeleccionado && clienteSeleccionado !== "") {
        idClienteFinal = clienteSeleccionado;
    } 
    // Si no, usar el cliente de la entrada
    else if (entrada.idCliente) {
        idClienteFinal = entrada.idCliente;
    } 
    // Si no hay cliente, usar el primer cliente disponible como fallback
    else if (clientes.length > 0) {
        idClienteFinal = clientes[0].idCliente;
        console.warn('Usando cliente por defecto:', clientes[0].nombres);
    }
    // Si no hay clientes en el sistema, error
    else {
        showNotification('No hay clientes registrados en el sistema', 'error');
        return;
    }

    const salidaData = {
        idEntrada: formData.get('idEntrada'),
        idCliente2: idClienteFinal,
        idEmpleado: idEmpleado,
        codTipoVhc: parseInt(formData.get('codTipoVhc')),
        fechaSalida: fechaHora,
        horaSalida: hora
    };
    
    // Log para debug
    console.log('Datos de salida a enviar:', salidaData);
    
    try {
        const response = await fetchAuth(`${API_URL}/salidas`, {
            method: 'POST',
            body: JSON.stringify(salidaData)
        });
        
        if (response.ok) {
            showNotification('Salida registrada exitosamente', 'success');
            closeSalidaModal();
            
            // Recargar las tablas
            await loadEntradasActivas();
            await loadSalidas();
        } else {
            const error = await response.json();
            throw new Error(error.message || 'Error al registrar salida');
        }
        
    } catch (error) {
        console.error('Error:', error);
        showNotification(error.message || 'Error al registrar la salida', 'error');
    }
});


function verDetalleSalida(idSalida) {
    const salida = salidas.find(s => s.idSalida === idSalida);
    if (!salida) return;
    
    const content = `
        <div class="entry-info">
            <h4>Detalle de la Salida</h4>
            <div class="entry-info-grid">
                <div class="info-item">
                    <span class="info-label">ID Salida</span>
                    <span class="info-value">${salida.idSalida}</span>
                </div>
                <div class="info-item">
                    <span class="info-label">ID Entrada</span>
                    <span class="info-value">${salida.idEntrada}</span>
                </div>
                <div class="info-item">
                    <span class="info-label">Placa</span>
                    <span class="info-value">${salida.registroEntrada?.nroPlaca || '-'}</span>
                </div>
                <div class="info-item">
                    <span class="info-label">Cliente</span>
                    <span class="info-value">${salida.nombreCliente || '-'}</span>
                </div>
                <div class="info-item">
                    <span class="info-label">Fecha/Hora Entrada</span>
                    <span class="info-value">${salida.registroEntrada ? formatDateTime(salida.registroEntrada.fechaIngreso, salida.registroEntrada.horaIngreso) : '-'}</span>
                </div>
                <div class="info-item">
                    <span class="info-label">Fecha/Hora Salida</span>
                    <span class="info-value">${formatDateTime(salida.fechaSalida, salida.horaSalida)}</span>
                </div>
                <div class="info-item">
                    <span class="info-label">Empleado</span>
                    <span class="info-value">${salida.nombreEmpleado || '-'}</span>
                </div>
                <div class="info-item">
                    <span class="info-label">Total Pagado</span>
                    <span class="info-value" style="color: #28a745; font-size: 18px;">${formatCurrency(salida.montoTotal || 0)}</span>
                </div>
            </div>
        </div>
        <div style="text-align: center; margin-top: 20px;">
            <button class="btn btn-primary" onclick="closeModal(this)">Cerrar</button>
        </div>
    `;
    
    createModal('Detalle de Salida', content);
}

document.addEventListener('DOMContentLoaded', () => {
    loadInitialData();
});