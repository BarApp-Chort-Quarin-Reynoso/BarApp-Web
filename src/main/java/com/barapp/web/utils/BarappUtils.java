package com.barapp.web.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class BarappUtils {

    /**
     * Obtiene el tipo MIME de una imagen almacenada en Firebase Storage a partir de la URL proporcionada.
     *
     * <p>Este método extrae la extensión del archivo de la URL dada y la convierte en un tipo MIME agregando "image/"
     * como prefijo. Si la extensión es "jpg", se convierte a "jpeg". Si la URL no contiene una extensión de archivo,
     * se devuelve un tipo MIME predeterminado.</p>
     *
     * @param url La URL de la imagen almacenada en Firebase Storage. Debe ser una URL válida y bien formada.
     * @param defaultMimeType El tipo MIME predeterminado a devolver si la URL no contiene una extensión.
     * @return El tipo MIME de la imagen si la extensión está presente en la URL; de lo contrario, el tipo MIME predeterminado.
     * @throws RuntimeException Si la URL es mal formada o no puede ser procesada.
     */
    public static String getMimeTypeFromStorageImage(String url, String defaultMimeType) {
        try {
            String urlPath = new URL(url).getPath();

            // Verifica si la URL contiene un punto
            if (urlPath.lastIndexOf('.') != -1) {
                // Obtiene la extensión del archivo desde el último punto hasta el final de la cadena
                String extension = urlPath.substring(urlPath.lastIndexOf('.') + 1);
                // Maneja el caso específico de "jpg"
                if (extension.equals("jpg")) {
                    return "image/jpeg";
                }
                // Devuelve el tipo MIME prefijando "image/" a la extensión
                return "image/" + extension;
            }
            // Si no hay punto, devuelve un tipo MIME predeterminado
            return defaultMimeType;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Convierte un {@link InputStream} a un arreglo de bytes.
     *
     * <p>Este método lee todos los datos de un {@link InputStream} y los escribe en un {@link ByteArrayOutputStream},
     * que luego se convierte en un arreglo de bytes.</p>
     *
     * @param inputStream El {@link InputStream} que contiene los datos a convertir. No debe ser null.
     * @return Un arreglo de bytes que contiene todos los datos leídos del {@link InputStream}.
     * @throws IOException Si ocurre un error al leer del {@link InputStream}.
     */
    public static byte[] convertInputStreamToByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[40960];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        return outputStream.toByteArray();
    }
}
