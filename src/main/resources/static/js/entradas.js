/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */
if (!requireAuth()) {
    throw new Error('No autenticado');
}

updateUserInfo();

let entradas = [];
let clientes = [];
let vehiculos = [];
let isEditMode = false;
let currentEntradaId = null;

loadInitialData();

async function loadInitialData() {
    try {
        await loadClientes();
        await loadVehiculos();
        await loadEntradas();
        setDefaultDateTime();
    } catch (error) {
        console.error('Error cargando datos:', error);
        showNotification('Error al cargar los datos', 'error');
    }
}

async function loadClientes() {
    try {
        const response = await fetchAuth(`${API_URL}/clientes`);
        if (!response.ok) throw new Error('Error al cargar clientes');

        clientes = await response.json();
        const select = document.getElementById('idCliente');
        select.innerHTML = '<option value="">Seleccione...</option>';

        clientes.forEach(cliente => {
            const option = document.createElement('option');
            option.value = cliente.idCliente;
            option.textContent = `${cliente.nombres} ${cliente.apellidos} - ${cliente.numDoc || 'Sin doc'}`;
            select.appendChild(option);
        });

        if (clientes.length > 20) {
            createSearchableSelect(select, 'Buscar cliente...');
        }
    } catch (error) {
        console.error('Error clientes:', error);
    }
}

async function loadVehiculos() {
    try {
        const response = await fetchAuth(`${API_URL}/vehiculos`);
        if (!response.ok) throw new Error('Error al cargar vehículos');

        vehiculos = await response.json();

        const select = document.getElementById('nroPlaca');
        if (select && select.tagName === 'SELECT') {
            select.innerHTML = '<option value="">Seleccione...</option>';

            vehiculos.forEach(vehiculo => {
                const option = document.createElement('option');
                option.value = vehiculo.nroPlaca;
                option.textContent = vehiculo.nroPlaca;
                select.appendChild(option);
            });

            if (vehiculos.length > 20) {
                createSearchableSelect(select, 'Buscar placa...');
            }
        }

    } catch (error) {
        console.error('Error vehículos:', error);
    }
}

async function loadEntradas() {
    try {
        const response = await fetchAuth(`${API_URL}/entradas`);
        if (!response.ok) throw new Error('Error al cargar entradas');

        entradas = await response.json();
        renderEntradas(entradas);
    } catch (error) {
        console.error('Error entradas:', error);
        showNotification('Error al cargar las entradas', 'error');
    }
}

function renderEntradas(entradasList) {
    const tbody = document.getElementById('entradasTableBody');
    
    if (entradasList.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="9" style="text-align: center; padding: 40px;">
                    No hay entradas registradas
                </td>
            </tr>
        `;
        return;
    }

    tbody.innerHTML = entradasList.map(entrada => {
        const fechaHora = formatDateTime(entrada.fechaIngreso, entrada.horaIngreso);
        const estado = entrada.tieneSalida ? 
            '<span class="badge badge-success">Completado</span>' : 
            '<span class="badge badge-warning">Activo</span>';

        return `
            <tr>
                <td><strong>${entrada.idEntrada}</strong></td>
                <td>${entrada.nroPlaca || '-'}</td>
                <td>${entrada.nombreCliente || '-'}</td>
                <td>${fechaHora}</td>
                <td>Nivel ${entrada.nivel}</td>
                <td>${entrada.zona || '-'}</td>
                <td>${entrada.nombreEmpleado || '-'}</td>
                <td>${estado}</td>
                <td class="table-actions">
                    ${!entrada.tieneSalida ? `
                        <button class="btn btn-sm btn-secondary" onclick="editEntrada('${entrada.idEntrada}')">
                            Editar
                        </button>
                        <button class="btn btn-sm btn-danger" onclick="deleteEntrada('${entrada.idEntrada}')">
                            Eliminar
                        </button>
                    ` : `
                        <button class="btn btn-sm btn-secondary" disabled>
                            Con salida
                        </button>
                    `}
                </td>
            </tr>
        `;
    }).join('');
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

document.getElementById('searchInput').addEventListener('input', (e) => {
    const searchTerm = e.target.value.toLowerCase();
    const filtered = entradas.filter(entrada =>
        (entrada.nroPlaca && entrada.nroPlaca.toLowerCase().includes(searchTerm)) ||
        (entrada.nombreCliente && entrada.nombreCliente.toLowerCase().includes(searchTerm)) ||
        (entrada.zona && entrada.zona.toLowerCase().includes(searchTerm))
    );
    applyFilters(filtered);
});

document.getElementById('filterEstado').addEventListener('change', () => applyFilters());
document.getElementById('filterNivel').addEventListener('change', () => applyFilters());

function applyFilters(baseList = entradas) {
    const estadoFilter = document.getElementById('filterEstado').value;
    const nivelFilter = document.getElementById('filterNivel').value;

    let filtered = baseList;
    if (estadoFilter === 'activo') filtered = filtered.filter(e => !e.tieneSalida);
    else if (estadoFilter === 'completado') filtered = filtered.filter(e => e.tieneSalida);
    if (nivelFilter) filtered = filtered.filter(e => e.nivel === nivelFilter);

    renderEntradas(filtered);
}

function checkVehiculo() {
    const placa = document.getElementById('nroPlaca').value.toUpperCase();
    const infoElement = document.getElementById('vehiculoInfo');

    if (!placa) {
        infoElement.textContent = '';
        return;
    }

    const vehiculo = vehiculos.find(v => v.nroPlaca === placa);
    if (vehiculo) {
        infoElement.textContent = `✓ ${vehiculo.tipoVehiculo} - ${vehiculo.color} - ${vehiculo.marcaModelo}`;
        infoElement.style.color = '#28a745';
    } else {
        infoElement.textContent = '⚠ Vehículo no registrado. Se creará automáticamente.';
        infoElement.style.color = '#ffc107';
    }
}

function showAddEntradaModal() {
    isEditMode = false;
    currentEntradaId = null;
    document.getElementById('modalTitle').textContent = 'Registrar Entrada';
    document.getElementById('entradaForm').reset();
    setDefaultDateTime();
    document.getElementById('entradaModal').classList.add('show');
}

function closeEntradaModal() {
    document.getElementById('entradaModal').classList.remove('show');
    document.getElementById('entradaForm').reset();
    document.getElementById('vehiculoInfo').textContent = '';
}

function setDefaultDateTime() {
    const now = new Date();
    const year = now.getFullYear();
    const month = (now.getMonth() + 1).toString().padStart(2, '0');
    const day = now.getDate().toString().padStart(2, '0');
    const hours = now.getHours().toString().padStart(2, '0');
    const minutes = now.getMinutes().toString().padStart(2, '0');

    document.getElementById('fechaIngreso').value = `${year}-${month}-${day}`;
    document.getElementById('horaIngreso').value = `${hours}:${minutes}`;
}

function editEntrada(idEntrada) {
    const entrada = entradas.find(e => e.idEntrada === idEntrada);
    if (!entrada) return;

    isEditMode = true;
    currentEntradaId = idEntrada;

    document.getElementById('modalTitle').textContent = 'Editar Entrada';
    document.getElementById('nroPlaca').value = entrada.nroPlaca || '';
    document.getElementById('idCliente').value = entrada.idCliente || '';
    document.getElementById('codDocPaga').value = entrada.codDocPaga || '';
    document.getElementById('nroDocumento').value = entrada.nroDocumento || '';
    document.getElementById('nivel').value = entrada.nivel || '';
    document.getElementById('zona').value = entrada.zona || '';

    if (entrada.fechaIngreso) {
        const fecha = new Date(entrada.fechaIngreso);
        document.getElementById('fechaIngreso').value = fecha.toISOString().slice(0, 10);
    }
    if (entrada.horaIngreso && Array.isArray(entrada.horaIngreso)) {
        const h = entrada.horaIngreso[0].toString().padStart(2, '0');
        const m = entrada.horaIngreso[1].toString().padStart(2, '0');
        document.getElementById('horaIngreso').value = `${h}:${m}`;
    }

    checkVehiculo();
    document.getElementById('entradaModal').classList.add('show');
}

async function deleteEntrada(idEntrada) {
    if (!confirm(`¿Eliminar entrada ${idEntrada}?`)) return;
    try {
        const response = await fetchAuth(`${API_URL}/entradas/${idEntrada}`, { method: 'DELETE' });
        if (response.ok) {
            showNotification('Entrada eliminada exitosamente', 'success');
            loadEntradas();
        } else throw new Error('Error al eliminar entrada');
    } catch (error) {
        console.error('Error:', error);
        showNotification('Error al eliminar la entrada', 'error');
    }
}

document.getElementById('entradaForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const form = e.target;
    syncSearchableSelects(form);
    if (!validateForm(form)) return;

    const formData = new FormData(form);
    const idEmpleado = localStorage.getItem('userId');
    const fecha = formData.get('fechaIngreso');
    const hora = formData.get('horaIngreso');

    const entradaData = {
        nroPlaca: formData.get('nroPlaca').toUpperCase(),
        idCliente: formData.get('idCliente'),
        idEmpleado,
        codDocPaga: formData.get('codDocPaga') || null,
        nroDocumento: formData.get('nroDocumento') || null,
        nivel: parseInt(formData.get('nivel')),
        zona: formData.get('zona'),
        fechaIngreso: `${fecha}T00:00:00`,
        horaIngreso: hora
    };

    try {
        const url = isEditMode ? `${API_URL}/entradas/${currentEntradaId}` : `${API_URL}/entradas`;
        const method = isEditMode ? 'PUT' : 'POST';

        const response = await fetchAuth(url, {
            method,
            body: JSON.stringify(entradaData)
        });

        if (response.ok) {
            showNotification(isEditMode ? 'Entrada actualizada' : 'Entrada registrada', 'success');
            closeEntradaModal();
            loadEntradas();
        } else {
            const errorMsg = await response.text();
            showNotification('Error: ' + errorMsg, 'error');
        }
    } catch (error) {
        console.error('Error:', error);
        showNotification('Error al guardar entrada', 'error');
    }
});
