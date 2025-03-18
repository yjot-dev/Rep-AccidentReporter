// Instanciar rutas
import { api } from './src/routes/reportRoute.js';

// Instanciar modulos
import express from 'express';
import { createServer } from 'https';
import { readFileSync } from 'fs';
const app = express()

// Configurar puerto
const port = process.env.PORT || 443;
app.set('port', port)

// Usar JSON con un limite de datos para el body request
app.use(express.json({ limit: '20mb' }))
// Usar rutas
app.use('/api', api)

// Lee el certificado y la clave privada 
let credentials;
try {
    const privateKey = readFileSync('./src/certificate/mykey.key');
    const certificate = readFileSync('./src/certificate/mycert.crt');
    credentials = { key: privateKey, cert: certificate };
} catch (error) {
    console.error("Error al leer los certificados: ", error);
    process.exit(1); // Finaliza la aplicación si hay un error con los certificados
}

// Crea el servidor HTTPS 
const httpsServer = createServer(credentials, app);

// Escuchar servicio
httpsServer.listen(app.get('port'), (error) => {
    if (error) {
        console.log(`Sucedió un error: ${error}`);
    }else{
        console.log(`Servidor corriendo en el puerto: ${app.get('port')}`);
    }
});