/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */


if (!requireAuth()) {
    throw new Error('No autenticado');
}

updateUserInfo();


let currentReportType = null;
let reportData = null;


document.addEventListener('DOMContentLoaded', () => {
    const today = new Date();
    const lastMonth = new Date(today);
    lastMonth.setMonth(lastMonth.getMonth() - 1);
    
    document.getElementById('fechaDesde').value = formatDateForInput(lastMonth);
    document.getElementById('fechaHasta').value = formatDateForInput(today);
    
  
    loadSummaryStats();
});


function formatDateForInput(date) {
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
}


async function loadSummaryStats() {
    try {
        const fechaDesde = document.getElementById('fechaDesde').value;
        const fechaHasta = document.getElementById('fechaHasta').value;
        
      
        const stats = await fetchReportStats(fechaDesde, fechaHasta);
        
        document.getElementById('totalIngresos').textContent = formatCurrency(stats.totalIngresos || 0);
        document.getElementById('totalVehiculos').textContent = stats.totalVehiculos || '0';
        document.getElementById('promedioDiario').textContent = formatCurrency(stats.promedioDiario || 0);
        document.getElementById('tiempoPromedio').textContent = stats.tiempoPromedio || '0h';
        
    } catch (error) {
        console.error('Error cargando estadísticas:', error);
    }
}


async function fetchReportStats(fechaDesde, fechaHasta) {
  
    return {
        totalIngresos: 15750.00,
        totalVehiculos: 1243,
        promedioDiario: 525.00,
        tiempoPromedio: '3.5h'
    };
}


function applyFilters() {
    loadSummaryStats();
    
  
    if (currentReportType) {
        generateReport(currentReportType);
    }
}


async function generateReport(type) {
    currentReportType = type;
    
   
    document.getElementById('reportContent').style.display = 'block';
    
   
    const titles = {
        'ingresos': 'Reporte de Ingresos',
        'ocupacion': 'Reporte de Ocupación',
        'clientes': 'Reporte de Clientes',
        'vehiculos': 'Reporte de Vehículos'
    };
    document.getElementById('reportTitle').textContent = titles[type];
    
  
    document.getElementById('reportData').innerHTML = '<div class="loading">Generando reporte...</div>';
    
    try {
      
        const fechaDesde = document.getElementById('fechaDesde').value;
        const fechaHasta = document.getElementById('fechaHasta').value;
        
     
        let content = '';
        
        switch (type) {
            case 'ingresos':
                content = await generateIngresosReport(fechaDesde, fechaHasta);
                break;
            case 'ocupacion':
                content = await generateOcupacionReport(fechaDesde, fechaHasta);
                break;
            case 'clientes':
                content = await generateClientesReport(fechaDesde, fechaHasta);
                break;
            case 'vehiculos':
                content = await generateVehiculosReport(fechaDesde, fechaHasta);
                break;
        }
        
        document.getElementById('reportData').innerHTML = content;
        
     
        document.getElementById('reportContent').scrollIntoView({ behavior: 'smooth' });
        
    } catch (error) {
        console.error('Error generando reporte:', error);
        document.getElementById('reportData').innerHTML = 
            '<p style="color: red;">Error al generar el reporte. Por favor, intente nuevamente.</p>';
    }
}


async function generateIngresosReport(fechaDesde, fechaHasta) {

    const data = {
        totalPeriodo: 15750.00,
        diasOperacion: 30,
        promedioDiario: 525.00,
        mejorDia: { fecha: '2024-06-15', monto: 892.50 },
        peorDia: { fecha: '2024-06-22', monto: 125.00 },
        ingresosPorTipo: [
            { tipo: 'Automóvil', monto: 10500.00, porcentaje: 66.7 },
            { tipo: 'Motocicleta', monto: 2250.00, porcentaje: 14.3 },
            { tipo: 'Camioneta', monto: 3000.00, porcentaje: 19.0 }
        ]
    };
    
    return `
        <div class="report-section">
            <h3>Resumen del Período</h3>
            <p><strong>Desde:</strong> ${formatDate(fechaDesde)} <strong>Hasta:</strong> ${formatDate(fechaHasta)}</p>
            
            <div class="summary-stats">
                <div class="stat-box">
                    <h4>Total del Período</h4>
                    <div class="value">${formatCurrency(data.totalPeriodo)}</div>
                </div>
                <div class="stat-box">
                    <h4>Días de Operación</h4>
                    <div class="value">${data.diasOperacion}</div>
                </div>
                <div class="stat-box">
                    <h4>Promedio Diario</h4>
                    <div class="value">${formatCurrency(data.promedioDiario)}</div>
                </div>
            </div>
            
            <h3>Mejores y Peores Días</h3>
            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 20px;">
                <div class="card" style="background: #d4edda;">
                    <h4>Mejor Día</h4>
                    <p><strong>Fecha:</strong> ${formatDate(data.mejorDia.fecha)}</p>
                    <p><strong>Ingresos:</strong> ${formatCurrency(data.mejorDia.monto)}</p>
                </div>
                <div class="card" style="background: #f8d7da;">
                    <h4>Peor Día</h4>
                    <p><strong>Fecha:</strong> ${formatDate(data.peorDia.fecha)}</p>
                    <p><strong>Ingresos:</strong> ${formatCurrency(data.peorDia.monto)}</p>
                </div>
            </div>
            
            <h3>Ingresos por Tipo de Vehículo</h3>
            <table>
                <thead>
                    <tr>
                        <th>Tipo de Vehículo</th>
                        <th>Monto Total</th>
                        <th>Porcentaje</th>
                    </tr>
                </thead>
                <tbody>
                    ${data.ingresosPorTipo.map(item => `
                        <tr>
                            <td>${item.tipo}</td>
                            <td>${formatCurrency(item.monto)}</td>
                            <td>
                                <div style="display: flex; align-items: center;">
                                    <div style="width: 100px; height: 20px; background: #e0e0e0; border-radius: 10px; margin-right: 10px;">
                                        <div style="width: ${item.porcentaje}%; height: 100%; background: #5b6cd4; border-radius: 10px;"></div>
                                    </div>
                                    ${item.porcentaje}%
                                </div>
                            </td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        </div>
    `;
}


async function generateOcupacionReport(fechaDesde, fechaHasta) {
    const data = {
        promedioOcupacion: 72.5,
        horasPico: ['10:00 - 11:00', '14:00 - 15:00', '18:00 - 19:00'],
        ocupacionPorNivel: [
            { nivel: 'Sótano 1', promedio: 85, max: 95 },
            { nivel: 'Planta Baja', promedio: 90, max: 98 },
            { nivel: 'Nivel 1', promedio: 75, max: 88 },
            { nivel: 'Nivel 2', promedio: 60, max: 75 },
            { nivel: 'Nivel 3', promedio: 45, max: 60 }
        ]
    };
    
    return `
        <div class="report-section">
            <h3>Análisis de Ocupación</h3>
            <p><strong>Período:</strong> ${formatDate(fechaDesde)} al ${formatDate(fechaHasta)}</p>
            
            <div class="summary-stats">
                <div class="stat-box">
                    <h4>Ocupación Promedio</h4>
                    <div class="value">${data.promedioOcupacion}%</div>
                </div>
            </div>
            
            <h3>Horas Pico</h3>
            <ul>
                ${data.horasPico.map(hora => `<li>${hora}</li>`).join('')}
            </ul>
            
            <h3>Ocupación por Nivel</h3>
            <table>
                <thead>
                    <tr>
                        <th>Nivel</th>
                        <th>Ocupación Promedio</th>
                        <th>Ocupación Máxima</th>
                        <th>Visualización</th>
                    </tr>
                </thead>
                <tbody>
                    ${data.ocupacionPorNivel.map(nivel => `
                        <tr>
                            <td>${nivel.nivel}</td>
                            <td>${nivel.promedio}%</td>
                            <td>${nivel.max}%</td>
                            <td>
                                <div style="width: 200px; height: 20px; background: #e0e0e0; border-radius: 10px;">
                                    <div style="width: ${nivel.promedio}%; height: 100%; background: ${getColorByPercentage(nivel.promedio)}; border-radius: 10px;"></div>
                                </div>
                            </td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        </div>
    `;
}


async function generateClientesReport(fechaDesde, fechaHasta) {
    const data = {
        totalClientes: 425,
        nuevosClientes: 67,
        clientesFrecuentes: [
            { nombre: 'Juan Pérez', visitas: 28, totalPagado: 420.00 },
            { nombre: 'María García', visitas: 25, totalPagado: 375.00 },
            { nombre: 'Carlos López', visitas: 22, totalPagado: 330.00 },
            { nombre: 'Ana Martínez', visitas: 20, totalPagado: 300.00 },
            { nombre: 'Pedro Rodríguez', visitas: 18, totalPagado: 270.00 }
        ]
    };
    
    return `
        <div class="report-section">
            <h3>Análisis de Clientes</h3>
            <p><strong>Período:</strong> ${formatDate(fechaDesde)} al ${formatDate(fechaHasta)}</p>
            
            <div class="summary-stats">
                <div class="stat-box">
                    <h4>Total Clientes</h4>
                    <div class="value">${data.totalClientes}</div>
                </div>
                <div class="stat-box">
                    <h4>Nuevos Clientes</h4>
                    <div class="value">${data.nuevosClientes}</div>
                </div>
            </div>
            
            <h3>Top 5 Clientes Frecuentes</h3>
            <table>
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Cliente</th>
                        <th>Visitas</th>
                        <th>Total Pagado</th>
                    </tr>
                </thead>
                <tbody>
                    ${data.clientesFrecuentes.map((cliente, index) => `
                        <tr>
                            <td>${index + 1}</td>
                            <td>${cliente.nombre}</td>
                            <td>${cliente.visitas}</td>
                            <td>${formatCurrency(cliente.totalPagado)}</td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        </div>
    `;
}


async function generateVehiculosReport(fechaDesde, fechaHasta) {
    const data = {
        totalVehiculos: 892,
        vehiculosPorTipo: [
            { tipo: 'Automóvil', cantidad: 654, porcentaje: 73.3 },
            { tipo: 'Motocicleta', cantidad: 142, porcentaje: 15.9 },
            { tipo: 'Camioneta', cantidad: 96, porcentaje: 10.8 }
        ],
        marcasPopulares: [
            { marca: 'Toyota', cantidad: 156 },
            { marca: 'Nissan', cantidad: 134 },
            { marca: 'Honda', cantidad: 98 },
            { marca: 'Mazda', cantidad: 87 },
            { marca: 'Hyundai', cantidad: 76 }
        ]
    };
    
    return `
        <div class="report-section">
            <h3>Análisis de Vehículos</h3>
            <p><strong>Período:</strong> ${formatDate(fechaDesde)} al ${formatDate(fechaHasta)}</p>
            
            <div class="summary-stats">
                <div class="stat-box">
                    <h4>Total Vehículos</h4>
                    <div class="value">${data.totalVehiculos}</div>
                </div>
            </div>
            
            <h3>Distribución por Tipo</h3>
            <table>
                <thead>
                    <tr>
                        <th>Tipo de Vehículo</th>
                        <th>Cantidad</th>
                        <th>Porcentaje</th>
                    </tr>
                </thead>
                <tbody>
                    ${data.vehiculosPorTipo.map(item => `
                        <tr>
                            <td>${item.tipo}</td>
                            <td>${item.cantidad}</td>
                            <td>${item.porcentaje}%</td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
            
            <h3>Marcas Más Populares</h3>
            <table>
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Marca</th>
                        <th>Cantidad</th>
                    </tr>
                </thead>
                <tbody>
                    ${data.marcasPopulares.map((marca, index) => `
                        <tr>
                            <td>${index + 1}</td>
                            <td>${marca.marca}</td>
                            <td>${marca.cantidad}</td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        </div>
    `;
}


function getColorByPercentage(percentage) {
    if (percentage >= 90) return '#f44336';
    if (percentage >= 75) return '#ff9800';
    if (percentage >= 50) return '#ffc107';
    return '#4caf50';
}


function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString('es-PE', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric'
    });
}


async function exportReport(format) {
    const tipo = currentReportType;
    const desde = document.getElementById('fechaDesde').value;
    const hasta = document.getElementById('fechaHasta').value;

    try {
        const response = await fetchAuth(`${API_URL}/reportes/exportar?formato=${format}&tipo=${tipo}&desde=${desde}&hasta=${hasta}`, {
            method: 'GET'
        });

        if (!response.ok) {
            throw new Error('No se pudo exportar el reporte');
        }

        const blob = await response.blob();
        const url = window.URL.createObjectURL(blob);

        const a = document.createElement('a');
        a.href = url;
        a.download = `reporte_${tipo}_${desde}_a_${hasta}.${format}`;
        a.click();
        window.URL.revokeObjectURL(url);

        showNotification(`Reporte exportado exitosamente`, 'success');
    } catch (error) {
        console.error('Error exportando reporte:', error);
        showNotification('Error al exportar el reporte', 'error');
    }
}

function printReport() {
    window.print();
}


const reportStyles = `
<style>
.report-section {
    margin-top: 20px;
}

.report-section h3 {
    color: #333;
    margin-top: 30px;
    margin-bottom: 15px;
}

.report-section table {
    margin-top: 20px;
}

@media print {
    .sidebar,
    .main-header,
    .report-filters,
    .report-cards,
    .export-buttons {
        display: none !important;
    }
    
    .main-content {
        margin-left: 0 !important;
    }
    
    .content-wrapper {
        padding: 20px !important;
    }
}
</style>
`;

document.head.insertAdjacentHTML('beforeend', reportStyles);

