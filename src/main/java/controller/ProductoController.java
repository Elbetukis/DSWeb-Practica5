package controller;

import dao.ProductoDAO;
import entidades.Producto;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

@ManagedBean
@ViewScoped
public class ProductoController implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Producto producto = new Producto();
    private ProductoDAO productoDAO = new ProductoDAO();
    private List<Producto> listaProductos;

    public void guardarProducto() {
        productoDAO.guardarProducto(producto);
        producto = new Producto(); // Limpiar formulario
    }

    public List<Producto> getListaProductos() {
        if (listaProductos == null) {
            listaProductos = productoDAO.obtenerProductos();
        }
        return listaProductos;
    }

        public boolean isListaProductosVacia() {
        return getListaProductos() == null || getListaProductos().isEmpty();
    }

    // Getters y setters
    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
