import mysql from 'mysql2/promise';
import dotenv from 'dotenv';

dotenv.config({ path: './src/bd/.env' });

const dataConexion = {
  host: process.env.MYSQL_HOST,
  port: process.env.MYSQL_PORT,
  user: process.env.MYSQL_USER,
  password: process.env.MYSQL_PASSWORD,
  database: process.env.MYSQL_DATABASE,
};

const pool = mysql.createPool(dataConexion)

try {
  const connection = await pool.getConnection();
  console.log('Conexión exitosa');
  connection.release();
} catch (err) {
  if (err.code === 'PROTOCOL_CONNECTION_LOST') {
      console.error('La conexión a la base de datos fue cerrada.');
  }
  if (err.code === 'ER_CON_COUNT_ERROR') {
      console.error('La base de datos ha tenido demasiadas conexiones.');
  }
  if (err.code === 'ECONNREFUSED') {
      console.error('La conexión a la base de datos fue rechazada.');
  } else {
      console.error('Error al conectar a la base de datos:', err);
  }
}

export default pool;