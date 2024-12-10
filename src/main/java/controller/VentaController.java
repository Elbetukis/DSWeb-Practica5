// Java Controller: VentaController
package controller;

import dao.VentaDAO;
import dao.DetalleVentaDAO;
import dao.ClienteDAO;
import dao.ProductoDAO;
import entidades.Cliente;
import entidades.DetalleVenta;
import entidades.Producto;
import entidades.Venta;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import services.EmailService;
import services.ReporteService;

@ManagedBean
@ViewScoped
public class VentaController implements Serializable {
    private static final long serialVersionUID = 1L;

    private Venta venta;
    private DetalleVenta detalleVenta;
    private VentaDAO ventaDAO = new VentaDAO();
    private ProductoDAO productoDAO = new ProductoDAO();
    private DetalleVentaDAO detalleVentaDAO = new DetalleVentaDAO();
    private ClienteDAO clienteDAO = new ClienteDAO();
    private List<Venta> listaVentas;
    private List<DetalleVenta> listaDetalleVentas;
    private Long idCliente;
    private Double total;
    private ReporteService reporteService = new ReporteService();
    private EmailService emailService = new EmailService();
    
    public VentaController() {
        venta = new Venta();
        detalleVenta = new DetalleVenta();
        listaDetalleVentas = new ArrayList<>();
    }

    public String getProductoID(Long id){
        return productoDAO.obtenerNombrePorId(id);
    }
    public double getTotal(double cantidad, double precio){
        return cantidad * precio;
    }
    
    
    // Método para agregar un producto al detalle de la venta
    public void agregarProducto() {
        System.out.println("agrego producto");
        if (detalleVenta.getProducto() != null && detalleVenta.getCantidad() > 0) {
            DetalleVenta nuevoDetalle = new DetalleVenta();
            nuevoDetalle.setProducto(detalleVenta.getProducto());
            nuevoDetalle.setCantidad(detalleVenta.getCantidad());
            nuevoDetalle.setPrecio( productoDAO.obtenerPrecioPorId(detalleVenta.getProducto()));
            listaDetalleVentas.add(nuevoDetalle);
            detalleVenta = new DetalleVenta(); // Limpia el formulario del detalle
            System.out.println("Producto agregado: " + nuevoDetalle.getProducto() + ", Cantidad: " + nuevoDetalle.getCantidad());
        } else {
            System.out.println("Error: Producto no seleccionado o cantidad incorrecta.");
        }
    }

    // Método para guardar la venta (con detalles)
    public void guardarVenta() {
        System.out.println("Intentando guardar venta");
        if (idCliente != null) {
                venta.setCliente(idCliente);
                venta.setFecha(new Date());
                venta.setTotal(calcularTotalVenta());

                System.out.println("Cliente asignado: " + idCliente);
                System.out.println("Total de la venta: " + total);

                ventaDAO.guardarVenta(venta);

                // Guardar los detalles de la venta
                for (DetalleVenta detalle : listaDetalleVentas) {
                    detalle.setVenta(venta.getIdVenta());
                    System.out.println("DETALLE DE VENTA");
                    System.out.println(detalle.getCantidad());
                    System.out.println(detalle.getPrecio());
                    System.out.println(detalle.getProducto());
                    System.out.println(detalle.getVenta());
                    detalleVentaDAO.guardarDetalleVenta(detalle);
                    
                    
                }
                
        
                // Reiniciar los datos después de guardar
                venta = new Venta();
                listaDetalleVentas.clear();
                System.out.println("Venta guardada correctamente");
            } else {
                System.out.println("Error: Cliente no encontrado.");
            }
    }

    // Método para calcular el total de la venta
    public double calcularTotalVenta() {
        double total = 0;
        for (DetalleVenta detalle : listaDetalleVentas) {
            total += detalle.getCantidad() * detalle.getPrecio();
        }
        return total;
    }

    // Getter y setter para idCliente
    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    // Getters y setters para las otras propiedades
    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public DetalleVenta getDetalleVenta() {
        return detalleVenta;
    }

    public void setDetalleVenta(DetalleVenta detalleVenta) {
        this.detalleVenta = detalleVenta;
    }

    public List<DetalleVenta> getListaDetalleVentas() {
        return listaDetalleVentas;
    }

    public void setListaDetalleVentas(List<DetalleVenta> listaDetalleVentas) {
        this.listaDetalleVentas = listaDetalleVentas;
    }

    public List<Venta> getListaVentas() {
        if (listaVentas == null) {
            listaVentas = ventaDAO.obtenerVentas();
        }
        return listaVentas;
    }

    public void generarPdfVenta(Long idVenta) {
        Venta venta = ventaDAO.obtenerVentaPorId(idVenta);
        if (venta != null) {
            String filePath = "C:/Users/Asus/Documents/pdf-java/venta_" + idVenta + ".pdf";
            reporteService.generarPDF(filePath, venta);
            System.out.println("Se generó el pdf en la ruta: " + filePath);
        } else {
            System.out.println("Error: Venta no encontrada.");
        }
    }

    // Método para enviar el reporte de la venta por correo electrónico
   public void enviarCorreoVenta(Long idVenta, String correoDestino) {
    Venta venta = ventaDAO.obtenerVentaPorId(idVenta);
    if (venta != null) {
        // Generar el PDF primero y luego adjuntarlo al correo
        String filePath = "C:/Users/Asus/Documents/pdf-java/venta_" + idVenta + ".pdf";
        reporteService.generarPDF(filePath, venta);

        // Enviar el correo con el PDF adjunto
        emailService.sendEmail(correoDestino, "Reporte de Venta", "Se adjunta el reporte de la venta", filePath);
        System.out.println("Correo enviado a: " + correoDestino + " con el reporte de la venta: " + filePath);
    } else {
        System.out.println("Error: Venta no encontrada.");
    }
}



}
