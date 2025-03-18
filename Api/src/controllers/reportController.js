import pool from '../bd/db.js';

// Verifica si el objeto esta vacio
function isEmptyObject(obj) {
    return !Object.keys(obj).length;
};

// Seleccionar reporte
const seleccionar_reportes = async function(_, res) {
    try {
        // Consulta todos los reportes
        const sql1 = `SELECT * FROM reportes`;
        const reg1 = await pool.query(sql1);

        if (isEmptyObject(reg1)) {
            return res.status(500).send("Error no hay reportes");
        }

        res.status(200).send(reg1[0]);
    } catch (error) {
        console.error("Error al consultar reportes: ", error);
        res.status(500).send("Error del servidor");
    }
};

// Actualizar reporte
const actualizar_reporte = async function(req, res) {
    try {
        const id = req.params.id;
        // Obtiene todos los datos
        const usuarioEditado = req.body;

        // Construir la consulta de actualización
        const sql1 = `UPDATE reportes SET ? WHERE id = ?`;
        const reg1 = await pool.query(sql1, [usuarioEditado, id]);

        res.status(200).send(reg1);
    } catch (error) {
        console.error("Error al actualizar reporte: ", error);
        res.status(500).send("Error del servidor");
    }
};

// Insertar reporte
const insertar_reporte = async function(req, res) {
    try {
        // Obtiene todos los datos
        const usuarioNuevo = req.body;

        // Construir la consulta de inserción
        const sql1 = `INSERT INTO reportes SET ?`;
        const reg1 = await pool.query(sql1, usuarioNuevo);

        res.status(200).send(reg1);
    } catch (error) {
        console.error("Error al insertar reporte: ", error);
        res.status(500).send("Error del servidor");
    }
};

// Eliminar reporte
const eliminar_reporte = async function(req, res) {
    try{
        const id = req.params.id

        // Construir la consulta de eliminación
        const sql = `DELETE FROM reportes WHERE id = ?`;
        const reg = await pool.query(sql, id);

        res.status(200).send(reg);
    } catch (error) {
        console.error("Error al eliminar reporte: ", error);
        res.status(500).send("Error del servidor");
    }
}

export {
    seleccionar_reportes,
    actualizar_reporte,
    insertar_reporte,
    eliminar_reporte
};