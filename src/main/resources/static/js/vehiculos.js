/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */

if (!requireAuth()) {
    throw new Error('No autenticado');
}


updateUserInfo();

let vehiculos = [];
let tiposVehiculo = [];
let colores = [];
let marcasModelos = [];
let isEditMode = false;
let currentPlaca = null;


async function loadInitialData() {
    try {
     
        await loadCatalogos();
        
     
        await loadVehiculos();
        
    } catch (error) {
        console.error('Error cargando datos:', error);
        showNotification('Error al cargar los datos', 'error');
    }
}


async function loadCatalogos() {
    try {
     
        tiposVehiculo = [
            { codTipoVhc: 0, nombre: 'No-Especificado' },
            { codTipoVhc: 1, nombre: 'Sedan' },
            { codTipoVhc: 2, nombre: 'SUV' },
            { codTipoVhc: 3, nombre: 'Pickup' },
            { codTipoVhc: 4, nombre: 'Hatchback' },
            { codTipoVhc: 5, nombre: 'Convertible' },
            { codTipoVhc: 6, nombre: 'Coupé' },
            { codTipoVhc: 7, nombre: 'Furgoneta' },
            { codTipoVhc: 8, nombre: 'Camión' },
            { codTipoVhc: 9, nombre: 'Motocicleta' },
            { codTipoVhc: 10, nombre: 'Minivan' }
        ];
        
        colores = [

            { codColor: 0, nombre: 'No-Especificado' },
            { codColor: 1, nombre: 'Negro' },
            { codColor: 2, nombre: 'Gris' },
            { codColor: 3, nombre: 'Plata' },
            { codColor: 4, nombre: 'Azul' },
            { codColor: 5, nombre: 'Verde' },
            { codColor: 6, nombre: 'Rojo' },
            { codColor: 7, nombre: 'Amarillo' },
            { codColor: 8, nombre: 'Blanco' },
              { codColor: 9, nombre: 'Marrón' },
              { codColor: 10, nombre: 'Beige' },
              { codColor: 11, nombre: 'Celeste' },
              { codColor: 12, nombre: 'Turquesa' },
              { codColor: 13, nombre: 'Dorado' },
              { codColor: 14, nombre: 'Bronce' },
              { codColor: 15, nombre: 'Vino' },
              { codColor: 16, nombre: 'Anaranjado' },
              { codColor: 17, nombre: 'Lavanda' },
              { codColor: 18, nombre: 'Fucsia' },
              { codColor: 19, nombre: 'Lila' },
              { codColor: 20, nombre: 'Cian' },
              { codColor: 21, nombre: 'Magenta' },
              { codColor: 22, nombre: 'Púrpura' },
              { codColor: 23, nombre: 'Gris Oscuro' },
              { codColor: 24, nombre: 'Azul Marino' },
              { codColor: 25, nombre: 'Verde Oliva' },
              { codColor: 26, nombre: 'Negro Mate' },
              { codColor: 27, nombre: 'Blanco Perla' }
        ];
        
        marcasModelos = [
            { codMarcaModelo: 100, nombre: 'TOYOTA' },
            { codMarcaModelo: 101, nombre: 'YARIS' },
            { codMarcaModelo: 102, nombre: 'COROLLA' },
            { codMarcaModelo: 103, nombre: 'LEXUS' },
            { codMarcaModelo: 104, nombre: 'HILUX' },
            { codMarcaModelo: 105, nombre: 'AVANZA' },
            { codMarcaModelo: 106, nombre: 'RAV4' },
            { codMarcaModelo: 200, nombre: 'NISSAN' },
            { codMarcaModelo: 201, nombre: 'SENTRA' },
            { codMarcaModelo: 202, nombre: 'SUNNY' },
            { codMarcaModelo: 203, nombre: 'TIDA' },
            { codMarcaModelo: 204, nombre: 'VERSA' },
            { codMarcaModelo: 205, nombre: 'ALTIMA' },
            { codMarcaModelo: 206, nombre: 'X-TRAIL' },
            { codMarcaModelo: 300, nombre: 'HONDA' },
            { codMarcaModelo: 301, nombre: 'CIVIC' },
            { codMarcaModelo: 302, nombre: 'ACCORD' },
            { codMarcaModelo: 303, nombre: 'CR-V' },
            { codMarcaModelo: 304, nombre: 'FIT' },
            { codMarcaModelo: 305, nombre: 'HR-V' },
            { codMarcaModelo: 400, nombre: 'FORD' },
            { codMarcaModelo: 401, nombre: 'FIESTA' },
            { codMarcaModelo: 402, nombre: 'FOCUS' },
            { codMarcaModelo: 403, nombre: 'RANGER' },
            { codMarcaModelo: 404, nombre: 'EXPLORER' },
            { codMarcaModelo: 500, nombre: 'CHEVROLET' },
            { codMarcaModelo: 501, nombre: 'SPARK' },
            { codMarcaModelo: 502, nombre: 'AVEO' },
            { codMarcaModelo: 503, nombre: 'CRUZE' },
            { codMarcaModelo: 504, nombre: 'TRACKER' },
            { codMarcaModelo: 505, nombre: 'TRAILBLAZER' },
            { codMarcaModelo: 600, nombre: 'HYUNDAI' },
            { codMarcaModelo: 601, nombre: 'ELANTRA' },
            { codMarcaModelo: 602, nombre: 'TUCSON' },
            { codMarcaModelo: 603, nombre: 'SANTA FE' },
            { codMarcaModelo: 604, nombre: 'ACCENT' },
            { codMarcaModelo: 700, nombre: 'KIA' },
            { codMarcaModelo: 701, nombre: 'RIO' },
            { codMarcaModelo: 702, nombre: 'CERATO' },
            { codMarcaModelo: 703, nombre: 'SPORTAGE' },
            { codMarcaModelo: 704, nombre: 'PICANTO' },
            { codMarcaModelo: 800, nombre: 'MAZDA' },
            { codMarcaModelo: 801, nombre: 'MAZDA 3' },
            { codMarcaModelo: 802, nombre: 'MAZDA 6' },
            { codMarcaModelo: 803, nombre: 'CX-5' },
            { codMarcaModelo: 804, nombre: 'CX-30' }
        ];
        
    
        fillSelects();
        
    } catch (error) {
        console.error('Error cargando catálogos:', error);
    }
}


function fillSelects() {
  
    fillSelect('codTipoVhc', tiposVehiculo, 'codTipoVhc', 'nombre');
    fillSelect('filterTipo', tiposVehiculo, 'codTipoVhc', 'nombre');
    
  
    fillSelect('codColor', colores, 'codColor', 'nombre');
    fillSelect('filterColor', colores, 'codColor', 'nombre');
    
   
    fillSelect('codMarcaModelo', marcasModelos, 'codMarcaModelo', 'nombre');
    fillSelect('filterMarca', marcasModelos, 'codMarcaModelo', 'nombre');
}


function fillSelect(selectId, data, valueField, textField) {
    const select = document.getElementById(selectId);
    const currentValue = select.value;
    
  
    const firstOption = select.options[0];
    select.innerHTML = '';
    select.appendChild(firstOption);
    
  
    data.forEach(item => {
        const option = document.createElement('option');
        option.value = item[valueField];
        option.textContent = item[textField];
        select.appendChild(option);
    });
    
   
    select.value = currentValue;
}


async function loadVehiculos() {
    try {
        const response = await fetchAuth(`${API_URL}/vehiculos`);
        
        if (!response.ok) {
            throw new Error('Error al cargar vehículos');
        }
        
        vehiculos = await response.json();
        renderVehiculos(vehiculos);
        
    } catch (error) {
        console.error('Error:', error);
        showNotification('Error al cargar los vehículos', 'error');
    }
}


function renderVehiculos(vehiculosList) {
    const tbody = document.getElementById('vehiculosTableBody');
    
    if (vehiculosList.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="6" style="text-align: center; padding: 40px;">
                    No hay vehículos registrados
                </td>
            </tr>
        `;
        return;
    }
    
    tbody.innerHTML = vehiculosList.map(vehiculo => `
        <tr>
            <td><strong>${vehiculo.nroPlaca}</strong></td>
            <td>${vehiculo.tipoVehiculo || '-'}</td>
            <td>${vehiculo.color || '-'}</td>
            <td>${vehiculo.marcaModelo || '-'}</td>
            <td>
                <span class="badge ${vehiculo.lunasPolarizadas === 'SI' ? 'badge-success' : 'badge-secondary'}">
                    ${vehiculo.lunasPolarizadas || 'NO'}
                </span>
            </td>
            <td class="table-actions">
                <button class="btn btn-sm btn-secondary" onclick="editVehiculo('${vehiculo.nroPlaca}')">
                    Editar
                </button>
                <button class="btn btn-sm btn-danger" onclick="deleteVehiculo('${vehiculo.nroPlaca}')">
                    Eliminar
                </button>
            </td>
        </tr>
    `).join('');
}


document.getElementById('searchInput').addEventListener('input', (e) => {
    const searchTerm = e.target.value.toLowerCase();
    
    const filtered = vehiculos.filter(vehiculo => 
        vehiculo.nroPlaca.toLowerCase().includes(searchTerm) ||
        (vehiculo.tipoVehiculo && vehiculo.tipoVehiculo.toLowerCase().includes(searchTerm)) ||
        (vehiculo.color && vehiculo.color.toLowerCase().includes(searchTerm)) ||
        (vehiculo.marcaModelo && vehiculo.marcaModelo.toLowerCase().includes(searchTerm))
    );
    
    renderVehiculos(filtered);
});


document.getElementById('filterTipo').addEventListener('change', applyFilters);
document.getElementById('filterColor').addEventListener('change', applyFilters);
document.getElementById('filterMarca').addEventListener('change', applyFilters);

function applyFilters() {
    const tipoFilter = document.getElementById('filterTipo').value;
    const colorFilter = document.getElementById('filterColor').value;
    const marcaFilter = document.getElementById('filterMarca').value;
    
    let filtered = vehiculos;
    
    if (tipoFilter) {
        filtered = filtered.filter(v => v.codTipoVhc === tipoFilter);
    }
    
    if (colorFilter) {
        filtered = filtered.filter(v => v.codColor === colorFilter);
    }
    
    if (marcaFilter) {
        filtered = filtered.filter(v => v.codMarcaModelo === marcaFilter);
    }
    
    renderVehiculos(filtered);
}


function showAddVehicleModal() {
    isEditMode = false;
    currentPlaca = null;
    document.getElementById('modalTitle').textContent = 'Agregar Vehículo';
    document.getElementById('vehicleForm').reset();
    document.getElementById('nroPlaca').disabled = false;
    document.getElementById('vehicleModal').classList.add('show');
}


function closeVehicleModal() {
    document.getElementById('vehicleModal').classList.remove('show');
    document.getElementById('vehicleForm').reset();
}


function editVehiculo(placa) {
    const vehiculo = vehiculos.find(v => v.nroPlaca === placa);
    if (!vehiculo) return;
    
    isEditMode = true;
    currentPlaca = placa;
    
    document.getElementById('modalTitle').textContent = 'Editar Vehículo';
    document.getElementById('nroPlaca').value = vehiculo.nroPlaca;
    document.getElementById('nroPlaca').disabled = true;
    document.getElementById('codTipoVhc').value = vehiculo.codTipoVhc || '';
    document.getElementById('codColor').value = vehiculo.codColor || '';
    document.getElementById('codMarcaModelo').value = vehiculo.codMarcaModelo || '';
    document.getElementById('lunasPolarizadas').value = vehiculo.lunasPolarizadas || 'NO';
    
    document.getElementById('vehicleModal').classList.add('show');
}


async function deleteVehiculo(placa) {
    if (!confirm(`¿Está seguro de eliminar el vehículo ${placa}?`)) {
        return;
    }
    
    try {
        const response = await fetchAuth(`${API_URL}/vehiculos/${placa}`, {
            method: 'DELETE'
        });
        
        if (response.ok) {
            showNotification('Vehículo eliminado exitosamente', 'success');
            loadVehiculos();
        } else {
            throw new Error('Error al eliminar vehículo');
        }
        
    } catch (error) {
        console.error('Error:', error);
        showNotification('Error al eliminar el vehículo', 'error');
    }
}


document.getElementById('vehicleForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    if (!validateForm(e.target)) {
        return;
    }
    
    const formData = new FormData(e.target);
    const vehiculoData = {
        nroPlaca: formData.get('nroPlaca').toUpperCase(),
        codTipoVhc: parseInt(formData.get('codTipoVhc')),
        codColor: parseInt(formData.get('codColor')),
        codMarcaModelo: parseInt(formData.get('codMarcaModelo')),
        lunasPolarizadas: formData.get('lunasPolarizadas')
    };

    try {
        const url = isEditMode 
            ? `${API_URL}/vehiculos/${currentPlaca}`
            : `${API_URL}/vehiculos`;
            
        const method = isEditMode ? 'PUT' : 'POST';
        
        const response = await fetchAuth(url, {
            method: method,
            body: JSON.stringify(vehiculoData)
        });
        
        if (response.ok) {
            showNotification(
                isEditMode ? 'Vehículo actualizado exitosamente' : 'Vehículo registrado exitosamente',
                'success'
            );
            closeVehicleModal();
            loadVehiculos();
        } else {
            const error = await response.json();
            throw new Error(error.message || 'Error al guardar vehículo');
        }
        
    } catch (error) {
        console.error('Error:', error);
        showNotification(error.message || 'Error al guardar el vehículo', 'error');
    }
});


document.getElementById('nroPlaca').addEventListener('input', (e) => {
    e.target.value = e.target.value.toUpperCase();
});


document.addEventListener('DOMContentLoaded', () => {
    loadInitialData();
});


const vehiculosStyles = `
<style>
.badge {
    padding: 4px 8px;
    border-radius: 4px;
    font-size: 12px;
    font-weight: 500;
}

.badge-success {
    background-color: #d4edda;
    color: #155724;
}

.badge-secondary {
    background-color: #e2e3e5;
    color: #383d41;
}

.modal-footer {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
    margin-top: 20px;
    padding-top: 20px;
    border-top: 1px solid #e0e0e0;
}

#nroPlaca:disabled {
    background-color: #f5f5f5;
    cursor: not-allowed;
}
</style>
`;

document.head.insertAdjacentHTML('beforeend', vehiculosStyles);
