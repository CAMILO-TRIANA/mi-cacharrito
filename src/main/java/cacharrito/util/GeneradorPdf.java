package cacharrito.util;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import cacharrito.modelo.Alquiler;

public class GeneradorPdf {

	private static final Color VERDE = new Color(14, 59, 46);
	private static final Color AMARILLO = new Color(252, 209, 22);
	private static final Color GRIS = new Color(85, 101, 94);
	private static final Color LINEA = new Color(217, 225, 218);

	// Genera el PDF del alquiler con la informacion solicitada en el enunciado
	public static byte[] generarPdfAlquiler(Alquiler a) {
		try {
			Document documento = new Document(PageSize.A4, 48, 48, 48, 48);
			ByteArrayOutputStream salida = new ByteArrayOutputStream();
			PdfWriter.getInstance(documento, salida);
			documento.open();

			Font marca = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, Font.NORMAL, Color.WHITE);
			Font subtitulo = FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL, AMARILLO);
			Font etiqueta = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, GRIS);
			Font valor = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Font.NORMAL, Color.BLACK);
			Font total = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Font.NORMAL, VERDE);
			Font nota = FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL, GRIS);

			// Encabezado
			PdfPTable cabecera = new PdfPTable(1);
			cabecera.setWidthPercentage(100);
			PdfPCell celda = new PdfPCell();
			celda.setBackgroundColor(VERDE);
			celda.setBorder(Rectangle.NO_BORDER);
			celda.setPadding(18);
			celda.addElement(new Paragraph("Mi Cacharrito", marca));
			celda.addElement(new Paragraph("Comprobante de alquiler", subtitulo));
			cabecera.addCell(celda);
			documento.add(cabecera);
			documento.add(new Paragraph(" "));

			// Datos del alquiler
			PdfPTable datos = new PdfPTable(new float[] { 1f, 2f });
			datos.setWidthPercentage(100);
			long dias = Math.max(1, ChronoUnit.DAYS.between(a.getFechaInicio(), a.getFechaEntregaPactada()));

			fila(datos, "Número de alquiler", a.getNumeroAlquiler(), etiqueta, valor);
			fila(datos, "Estado", a.getEstado(), etiqueta, valor);
			fila(datos, "Usuario", a.getUsuario().getNombreCompleto(), etiqueta, valor);
			fila(datos, "Identificación", a.getUsuario().getNumeroIdentificacion(), etiqueta, valor);
			fila(datos, "Tipo de vehículo", a.getVehiculo().getTipoVehiculo(), etiqueta, valor);
			fila(datos, "Placa", a.getVehiculo().getPlaca(), etiqueta, valor);
			fila(datos, "Color", a.getVehiculo().getColor(), etiqueta, valor);
			fila(datos, "Fecha de inicio", String.valueOf(a.getFechaInicio()), etiqueta, valor);
			fila(datos, "Fecha de entrega", String.valueOf(a.getFechaEntregaPactada()), etiqueta, valor);
			fila(datos, "Duración", dias + (dias == 1 ? " día" : " días"), etiqueta, valor);
			documento.add(datos);

			// Total
			documento.add(new Paragraph(" "));
			NumberFormat pesos = NumberFormat.getIntegerInstance(Locale.forLanguageTag("es-CO"));
			PdfPTable totalTabla = new PdfPTable(new float[] { 1f, 2f });
			totalTabla.setWidthPercentage(100);
			PdfPCell etiquetaTotal = new PdfPCell(new Phrase("Valor del alquiler", etiqueta));
			etiquetaTotal.setBackgroundColor(AMARILLO);
			etiquetaTotal.setBorder(Rectangle.NO_BORDER);
			etiquetaTotal.setPadding(12);
			etiquetaTotal.setVerticalAlignment(Element.ALIGN_MIDDLE);
			PdfPCell valorTotal = new PdfPCell(new Phrase("$ " + pesos.format(a.getValorTotal()) + " COP", total));
			valorTotal.setBackgroundColor(AMARILLO);
			valorTotal.setBorder(Rectangle.NO_BORDER);
			valorTotal.setPadding(12);
			totalTabla.addCell(etiquetaTotal);
			totalTabla.addCell(valorTotal);
			documento.add(totalTabla);

			documento.add(new Paragraph(" "));
			documento.add(new Paragraph(
					"Presenta este comprobante y tu licencia de conducción al reclamar el vehículo. "
							+ "Si lo devuelves después de la fecha de entrega pactada, se cobrará cada día adicional al valor diario.",
					nota));

			documento.close();

			return salida.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException("Error generando el PDF del alquiler", e);
		}
	}

	private static void fila(PdfPTable tabla, String etiqueta, String valor, Font fEtiqueta, Font fValor) {
		PdfPCell c1 = new PdfPCell(new Phrase(etiqueta, fEtiqueta));
		PdfPCell c2 = new PdfPCell(new Phrase(valor == null ? "-" : valor, fValor));
		for (PdfPCell c : new PdfPCell[] { c1, c2 }) {
			c.setBorder(Rectangle.BOTTOM);
			c.setBorderColor(LINEA);
			c.setPadding(8);
		}
		tabla.addCell(c1);
		tabla.addCell(c2);
	}
}
