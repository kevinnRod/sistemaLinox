
package com.linox.sistemaventas.controllers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.linox.sistemaventas.models.Cliente;
import com.linox.sistemaventas.models.ClienteJuridico;
import com.linox.sistemaventas.models.ClienteNatural;
import com.linox.sistemaventas.models.DetalleVenta;
import com.linox.sistemaventas.models.Empleado;
import com.linox.sistemaventas.models.EmpresaAnfitrion;
import com.linox.sistemaventas.models.Venta;
import com.linox.sistemaventas.services.ClienteService;
import com.linox.sistemaventas.services.EmpleadoService;
import com.linox.sistemaventas.services.EmpresaAnfitrionService;
import com.linox.sistemaventas.services.ProductoService;
import com.linox.sistemaventas.services.VentaService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private EmpresaAnfitrionService empresaAnfitrionService;

    // 3. Mostrar listado
    @GetMapping()
    public String listarVentas(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "8") int size,
            Model model) {

        List<Venta> ventas = ventaService.findAllActiveVentas();

        List<Map<String, Object>> datosVentas = ventas.stream().map(venta -> {
            Map<String, Object> datos = new HashMap<>();
            datos.put("codigo", venta.getCodVenta());
            datos.put("id", venta.getIdVenta());
            datos.put("total", venta.getTotal());
            datos.put("fecha", venta.getFechaV().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            datos.put("empleado", venta.getEmpleado().getNombres());

            Cliente cliente = clienteService.findById(venta.getCliente().getCodCliente())
                    .orElse(null);

            if (cliente instanceof ClienteNatural cn) {
                datos.put("nombre", cn.getPersona().getNombres() + " " + cn.getPersona().getApellidos());
                datos.put("identificacion", cn.getPersona().getDni());
            } else if (cliente instanceof ClienteJuridico cj) {
                datos.put("nombre", cj.getEmpresa().getRazonSocial());
                datos.put("identificacion", cj.getEmpresa().getRuc());
            } else {
                datos.put("nombre", "Cliente no encontrado");
                datos.put("identificacion", "-");
            }

            return datos;
        }).toList();

        // Paginación
        int totalVentas = datosVentas.size();
        int totalPages = (int) Math.ceil((double) totalVentas / size);
        int fromIndex = Math.min((page - 1) * size, totalVentas);
        int toIndex = Math.min(fromIndex + size, totalVentas);
        List<Map<String, Object>> paginaVentas = datosVentas.subList(fromIndex, toIndex);

        model.addAttribute("ventas", paginaVentas);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("size", size);
        model.addAttribute("active_page", "listarventa");

        return "venta/listar";
    }

    // Mostrar formulario de creación
    @GetMapping("/crear")
    public String mostrarFormulario(Model model) {
        Venta venta = new Venta();
        venta.setCodVenta(generarCodigoVenta());
        model.addAttribute("venta", venta);
        model.addAttribute("clientes", clienteService.findAllActivos());
        model.addAttribute("empleados", empleadoService.listarTodos());
        model.addAttribute("productos", productoService.findAllActivos());
        model.addAttribute("active_page", "listarventa");
        return "venta/crear";
    }

    @PostMapping("/guardar")
    public String guardarVenta(@ModelAttribute Venta venta,
            @RequestParam("productoIds") List<Integer> productoIds,
            @RequestParam("cantidades") List<Integer> cantidades,
            @RequestParam("codCliente") String codCliente,
            @RequestParam("empleado.id") Integer empleadoId,
            RedirectAttributes redirectAttrs) {
        Optional<Cliente> optionalCliente = clienteService.findById(codCliente);
        Optional<Empleado> optionalEmpleado = empleadoService.findById(empleadoId);

        if (optionalCliente.isPresent() && optionalEmpleado.isPresent()) {
            Cliente cliente = optionalCliente.get();
            Empleado empleado = optionalEmpleado.get();

            venta.setCliente(cliente);
            venta.setEmpleado(empleado);

            ventaService.guardarVentaConDetalles(venta, productoIds, cantidades);
            return "redirect:/ventas";
        } else {
            redirectAttrs.addFlashAttribute("error", "Cliente o empleado no encontrado.");
            return "redirect:/ventas/crear";
        }

    }

    // Mostrar formulario de edición

    // Actualizar venta
    @PostMapping("/update")
    public String actualizar(@ModelAttribute Venta venta) {
        Optional<Venta> original = ventaService.findVentaById(venta.getIdVenta());
        if (original.isPresent()) {
            venta.setCreatedAt(original.get().getCreatedAt());
            ventaService.saveVenta(venta);
        }
        return "redirect:/ventas";
    }

    // Eliminación lógica
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id) {
        ventaService.softDeleteVenta(id);
        return "redirect:/ventas";
    }

    @GetMapping("/{codigo}")
    public String verDetalleVenta(@PathVariable("codigo") String codigo, Model model) {
        // Buscar la venta por código, con su detalle (productos, cantidades, precios,
        // etc.)
        Optional<Venta> venta = ventaService.findByCodVenta(codigo);

        if (venta.isPresent() == false) {
            // Manejar error o redirigir
            return "redirect:/ventas";
        }
        Cliente cliente = clienteService.findById(venta.get().getCliente().getCodCliente())
                .orElseThrow(() -> new RuntimeException(
                        "Cliente no encontrado con ID: " + venta.get().getCliente().getCodCliente()));
        if (cliente instanceof ClienteNatural cn) {
            model.addAttribute("nombre", cn.getPersona().getNombres() + " " + cn.getPersona().getApellidos());
            model.addAttribute("identificacion", cn.getPersona().getDni());
        } else if (cliente instanceof ClienteJuridico cj) {
            model.addAttribute("nombre", cj.getEmpresa().getRazonSocial());
            model.addAttribute("identificacion", cj.getEmpresa().getRuc());
        }

        model.addAttribute("venta", venta.get());
        model.addAttribute("active_page", "listarventa");
        return "venta/detalle"; // nombre del template para detalle
    }

    @GetMapping("/{codigo}/comprobante")
    public void generarComprobante(@PathVariable("codigo") String codigo, HttpServletResponse response)
            throws Exception {
        Optional<Venta> ventaOpt = ventaService.findByCodVenta(codigo);
        if (!ventaOpt.isPresent()) {
            response.sendRedirect("/ventas");
            return;
        }

        Venta venta = ventaOpt.get();
        Cliente cliente = clienteService.findById(venta.getCliente().getCodCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=comprobante_" + codigo + ".pdf");

        Document document = new Document(PageSize.A4, 40, 40, 40, 40);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        // Fuentes y formatos
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Font subFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        DecimalFormat df = new DecimalFormat("0.00");

        // 1. Obtener empresa anfitriona
        EmpresaAnfitrion empresa = empresaAnfitrionService.getEmpresaUnica()
                .orElseThrow(() -> new RuntimeException("No hay empresa anfitriona configurada"));

        // 2. Logo
        try {
            String logoUrl = empresa.getLogoUrl(); // Ej: /uploads/empresa/imagen.jpg

            // Ruta absoluta al archivo dentro del proyecto
            String relativePath = logoUrl.replaceFirst("/uploads", "uploads"); // quita la primera "/"
            File logoFile = new File(relativePath);

            if (logoFile.exists()) {
                BufferedImage bufferedImage = ImageIO.read(logoFile);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, "jpg", baos);
                baos.flush();
                Image logo = Image.getInstance(baos.toByteArray());
                logo.scaleToFit(80, 80);
                logo.setAlignment(Element.ALIGN_CENTER);
                document.add(logo);
            } else {
                System.err.println("Logo no encontrado: " + logoFile.getAbsolutePath());
            }

        } catch (Exception e) {
            System.err.println("No se pudo cargar el logo: " + e.getMessage());
        }

        // 3. Nombre y datos de la empresa
        Paragraph nombreEmpresa = new Paragraph(empresa.getNombreComercial(),
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16));
        nombreEmpresa.setAlignment(Element.ALIGN_CENTER);
        document.add(nombreEmpresa);

        Paragraph datosEmpresa = new Paragraph(
                "RUC: " + empresa.getRuc() + "\n" +
                        empresa.getDireccion() + "\n" +
                        "Tel: " + empresa.getTelefono() + " - " + empresa.getCorreo(),
                FontFactory.getFont(FontFactory.HELVETICA, 10));
        datosEmpresa.setAlignment(Element.ALIGN_CENTER);
        datosEmpresa.setSpacingAfter(15);
        document.add(datosEmpresa);

        // 3. INFORMACIÓN DE VENTA Y CLIENTE
        PdfPTable info = new PdfPTable(2);
        info.setWidthPercentage(100);
        info.setWidths(new float[] { 3, 3 });
        info.setSpacingBefore(10);
        info.setSpacingAfter(10);

        String nombreCliente = (cliente instanceof ClienteNatural cn)
                ? cn.getPersona().getNombres() + " " + cn.getPersona().getApellidos()
                : ((ClienteJuridico) cliente).getEmpresa().getRazonSocial();

        info.addCell(getCell("Cliente:", boldFont));
        info.addCell(getCell(nombreCliente, normalFont));

        info.addCell(getCell("Código de Venta:", boldFont));
        info.addCell(getCell(venta.getCodVenta(), normalFont));

        info.addCell(getCell("Fecha:", boldFont));
        info.addCell(getCell(venta.getFechaV().toLocalDate().toString(), normalFont));

        info.addCell(getCell("Empleado:", boldFont));
        info.addCell(getCell(venta.getEmpleado().getNombres() + " " + venta.getEmpleado().getApellidos(), normalFont));

        document.add(info);

        // 4. TABLA DE DETALLES
        PdfPTable tabla = new PdfPTable(4);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[] { 4, 1, 2, 2 });

        String[] headers = { "Producto", "Cantidad", "Precio", "Subtotal" };
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, boldFont));
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            tabla.addCell(cell);
        }

        for (DetalleVenta d : venta.getDetallesVenta()) {
            tabla.addCell(getCell(d.getProducto().getNombreProducto(), normalFont, Element.ALIGN_LEFT));
            tabla.addCell(getCell(String.valueOf(d.getCantidad()), normalFont, Element.ALIGN_CENTER));
            tabla.addCell(
                    getCell("S/ " + df.format(d.getProducto().getPrecioUnitario()), normalFont, Element.ALIGN_RIGHT));
            tabla.addCell(getCell("S/ " + df.format(d.getSubtotal()), normalFont, Element.ALIGN_RIGHT));
        }

        document.add(tabla);

        // 5. TOTAL
        PdfPTable totalTable = new PdfPTable(2);
        totalTable.setWidths(new float[] { 6, 2 });
        totalTable.setWidthPercentage(100);
        totalTable.setSpacingBefore(10);

        totalTable.addCell(getCell("TOTAL:", boldFont, Element.ALIGN_RIGHT));
        totalTable.addCell(getCell("S/ " + df.format(venta.getTotal()), boldFont, Element.ALIGN_RIGHT));
        document.add(totalTable);

        // 6. NOTA FINAL
        Paragraph gracias = new Paragraph("\nGracias por su compra. ¡Vuelva pronto!", normalFont);
        gracias.setAlignment(Element.ALIGN_CENTER);
        gracias.setSpacingBefore(20);
        document.add(gracias);

        document.close();
    }

    private PdfPCell getCell(String text, Font font) {
        return getCell(text, font, Element.ALIGN_LEFT);
    }

    private PdfPCell getCell(String text, Font font, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(5);
        cell.setHorizontalAlignment(alignment);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorderWidth(0.5f);
        return cell;
    }

    private String generarCodigoVenta() {
        long total = ventaService.count() + 1;
        return String.format("RRD01-%05d", total);
    }

}
