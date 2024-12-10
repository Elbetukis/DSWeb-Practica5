/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import dao.DetalleVentaDAO;
import entidades.DetalleVenta;
import entidades.Venta;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 *
 * @author Asus
 */
public class ReporteService {

    private DetalleVentaDAO detalleDAO = new DetalleVentaDAO();

    public void generarPDF(String filePath, Venta venta) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Estilo del título
            Font tituloFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph titulo = new Paragraph("Ticket de Venta", tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);

            // Espacio debajo del título
            document.add(new Paragraph(" "));

            // Fecha de venta y datos básicos
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String fechaVenta = dateFormat.format(venta.getFecha());
            Font datosFont = new Font(Font.FontFamily.HELVETICA, 12);

            document.add(new Paragraph("Fecha de Venta: " + fechaVenta, datosFont));
            document.add(new Paragraph("ID de Venta: " + venta.getIdVenta(), datosFont));
            document.add(new Paragraph("Cliente: " + venta.getCliente(), datosFont));
            document.add(new Paragraph("Total: $" + venta.getTotal(), datosFont));
            document.add(new Paragraph(" ")); // Espacio entre secciones

            // Tabla de detalles de la venta
            PdfPTable table = new PdfPTable(4); // 4 columnas: Producto, Cantidad, Precio Unitario, Subtotal
            table.setWidthPercentage(100); // Ancho de la tabla
            table.setSpacingBefore(10f);

            // Encabezados de la tabla
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            table.addCell(new PdfPCell(new Phrase("Producto", headerFont)));
            table.addCell(new PdfPCell(new Phrase("Cantidad", headerFont)));
            table.addCell(new PdfPCell(new Phrase("Precio Unitario", headerFont)));
            table.addCell(new PdfPCell(new Phrase("Subtotal", headerFont)));

            // Contenido de la tabla
            List<DetalleVenta> detalles = detalleDAO.obtenerDetallesPorVentaId(venta.getIdVenta());
            int numeroProducto = 1; // Iniciar contador de productos
            if (detalles != null && !detalles.isEmpty()) {
                for (DetalleVenta detalle : detalles) {
                    table.addCell(new PdfPCell(new Phrase(String.valueOf(numeroProducto++)))); // Usar número en lugar de nombre
                    table.addCell(new PdfPCell(new Phrase(String.valueOf(detalle.getCantidad()))));
                    table.addCell(new PdfPCell(new Phrase("$" + detalle.getPrecio())));
                    table.addCell(new PdfPCell(new Phrase("$" + (detalle.getCantidad() * detalle.getPrecio()))));
                }
            } else {
                PdfPCell emptyCell = new PdfPCell(new Phrase("No hay detalles para esta venta."));
                emptyCell.setColspan(4);
                emptyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(emptyCell);
            }

            document.add(table); // Añadir la tabla al documento

            // Mensaje de agradecimiento
            Font agradecimientoFont = new Font(Font.FontFamily.HELVETICA, 12, Font.ITALIC);
            Paragraph agradecimiento = new Paragraph("\n¡Gracias por su compra!", agradecimientoFont);
            agradecimiento.setAlignment(Element.ALIGN_CENTER);
            document.add(agradecimiento);

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }
}