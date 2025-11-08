import { Router } from 'express';
const api = Router();
import { seleccionar_reportes, insertar_reporte, actualizar_reporte, eliminar_reporte } from '../controllers/reportController.js';

const resourcePath = '/reports';

api.get(resourcePath, seleccionar_reportes);
api.post(resourcePath, insertar_reporte);
api.put(`${resourcePath}/:id`, actualizar_reporte);
api.delete(`${resourcePath}/:id`, eliminar_reporte);

export { api };