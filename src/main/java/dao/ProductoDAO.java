/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entidades.Producto;
import org.hibernate.Session;
import org.hibernate.Transaction;
import services.HibernateUtil;
import java.util.List;

/**
 *
 * @author Asus
 */
public class ProductoDAO {
    public void guardarProducto(Producto producto) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(producto);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    // Método para obtener todos los productos
    public List<Producto> obtenerProductos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Producto", Producto.class).list();
        }
    }

    // Método para obtener el precio de un producto por su ID
    public Double obtenerPrecioPorId(Long idProducto) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Producto producto = session.get(Producto.class, idProducto);
            return (producto != null) ? producto.getPrecio() : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Método para obtener el nombre de un producto por su ID
    public String obtenerNombrePorId(Long idProducto) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Producto producto = session.get(Producto.class, idProducto);
            return (producto != null) ? producto.getDescripcion() : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Método para obtener un producto por su ID
    public Producto obtenerProductoPorId(Long idProducto) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Producto.class, idProducto);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Método para actualizar un producto
    public void actualizarProducto(Producto producto) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(producto);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    // Método para eliminar un producto
    public void eliminarProducto(Long idProducto) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Producto producto = session.get(Producto.class, idProducto);
            if (producto != null) {
                session.delete(producto);
                transaction.commit();
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
