import { Router } from 'express';
const api = Router();
import { seleccionar_reportes, insertar_reporte, actualizar_reporte, eliminar_reporte } from '../controllers/reportController.js';

api.get('/reports/', seleccionar_reportes);
api.post('/reports/', insertar_reporte);
api.put('/reports/:id', actualizar_reporte);
api.delete('/reports/:id', eliminar_reporte);

export { api };