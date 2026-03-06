/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */



if (!requireAuth()) {
   
    throw new Error('No autenticado');
}


updateUserInfo();


let dashboardData = null;
let refreshInterval = null;


async function loadDashboardData() {
    try {
        const response = await fetchAuth(`${API_URL}/dashboard/stats`);
        
        if (!response.ok) {
            throw new Error('Error al cargar datos del dashboard');
        }
        
        dashboardData = await response.json();
        updateDashboard(dashboardData);
        updateLastUpdateTime();
        
    } catch (error) {
        console.error('Error:', error);
        showNotification('Error al cargar los datos del dashboard', 'error');
    }
}


function updateDashboard(data) {

    document.getElementById('totalEspacios').textContent = data.totalEntradas || '0';
    document.getElementById('espaciosDisponibles').textContent = 
        (data.totalEntradas - data.vehiculosActivos) || '0';
    document.getElementById('espaciosOcupados').textContent = data.vehiculosActivos || '0';
    document.getElementById('ingresosDia').textContent = formatCurrency(data.ingresosDia || 0);
    
  
    updateRecentEntries();
    
   
    updateLevelOccupation(data.entradasPorNivel);
}


async function updateRecentEntries() {
    try {
        const response = await fetchAuth(`${API_URL}/entradas/activas`);
        const entries = await response.json();
        
        const entriesContainer = document.getElementById('recentEntries');
        
        if (entries.length === 0) {
            entriesContainer.innerHTML = '<p class="no-data">No hay entradas recientes</p>';
            return;
        }
        
    
        const recentEntries = entries.slice(0, 5);
        
        entriesContainer.innerHTML = recentEntries.map(entry => `
            <div class="entry-item">
                <div class="entry-info">
                    <span class="plate">${entry.nroPlaca || 'Sin placa'}</span>
                    <span class="location">Nivel ${entry.nivel || '-'}</span>
                </div>
                <div class="entry-details">
                    <span class="time">${formatTime(entry.horaIngreso)}</span>
                    <span class="zone">${entry.zona || 'Sin zona'}</span>
                </div>
            </div>
        `).join('');
        
    } catch (error) {
        console.error('Error cargando entradas recientes:', error);
    }
}


function updateLevelOccupation(levelData) {
    const levelContainer = document.getElementById('levelOccupation');
    
   
    const defaultLevels = [
        { name: 'Sótano 1', count: 85, total: 100, color: '#ff9800' },
        { name: 'Piso Bajo', count: 92, total: 100, color: '#4caf50' },
        { name: 'Piso 1', count: 78, total: 100, color: '#ffc107' },
        { name: 'Piso 2', count: 65, total: 100, color: '#2196f3' },
        { name: 'Piso 3', count: 45, total: 100, color: '#9c27b0' }
    ];
    
  
    if (levelData && Object.keys(levelData).length > 0) {
        const levels = Object.entries(levelData).map(([level, count], index) => {
            const colors = ['#ff9800', '#4caf50', '#ffc107', '#2196f3', '#9c27b0'];
            return {
                name: level,
                count: count,
                total: 100, 
                color: colors[index % colors.length]
            };
        });
        
        renderLevelOccupation(levels);
    } else {
        renderLevelOccupation(defaultLevels);
    }
}


function renderLevelOccupation(levels) {
    const levelContainer = document.getElementById('levelOccupation');
    
    levelContainer.innerHTML = levels.map(level => {
        const percentage = Math.round((level.count / level.total) * 100);
        
        return `
            <div class="level-item">
                <div class="level-info">
                    <span class="level-name">${level.name}</span>
                    <span class="level-percentage">${percentage}%</span>
                </div>
                <div class="level-bar">
                    <div class="level-progress" style="width: ${percentage}%; background: ${level.color};"></div>
                </div>
            </div>
        `;
    }).join('');
}


function formatTime(timeString) {
    if (!timeString) return '--:--';
    

    if (Array.isArray(timeString)) {
        const [hour, minute] = timeString;
        return `${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}`;
    }
    

    const time = new Date(`2000-01-01T${timeString}`);
    return time.toLocaleTimeString('es-PE', {
        hour: '2-digit',
        minute: '2-digit'
    });
}


function updateLastUpdateTime() {
    const now = new Date();
    const formatted = now.toLocaleDateString('es-PE', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    });
    
    document.getElementById('lastUpdate').textContent = formatted;
}


function startAutoRefresh() {
    refreshInterval = setInterval(() => {
        loadDashboardData();
    }, 30000);
}


function stopAutoRefresh() {
    if (refreshInterval) {
        clearInterval(refreshInterval);
        refreshInterval = null;
    }
}


document.addEventListener('DOMContentLoaded', () => {
    loadDashboardData();
    startAutoRefresh();
});


window.addEventListener('beforeunload', () => {
    stopAutoRefresh();
});


const dashboardStyles = `
<style>
.no-data {
    text-align: center;
    color: #999;
    padding: 40px 20px;
    font-style: italic;
}

.entry-item {
    animation: slideIn 0.3s ease;
}

@keyframes slideIn {
    from {
        opacity: 0;
        transform: translateX(-20px);
    }
    to {
        opacity: 1;
        transform: translateX(0);
    }
}

.level-progress {
    position: relative;
    overflow: hidden;
}

.level-progress::after {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    bottom: 0;
    right: 0;
    background: linear-gradient(
        90deg,
        rgba(255, 255, 255, 0) 0%,
        rgba(255, 255, 255, 0.2) 50%,
        rgba(255, 255, 255, 0) 100%
    );
    animation: shimmer 2s infinite;
}

@keyframes shimmer {
    0% {
        transform: translateX(-100%);
    }
    100% {
        transform: translateX(100%);
    }
}
</style>
`;

document.head.insertAdjacentHTML('beforeend', dashboardStyles);

