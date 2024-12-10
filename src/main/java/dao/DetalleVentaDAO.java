/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import entidades.DetalleVenta;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import services.HibernateUtil;

/**
 *
 * @author Asus
 */
public class DetalleVentaDAO {
    public void guardarDetalleVenta(DetalleVenta detalleVenta) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(detalleVenta);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    // Método para obtener todos los detalles de venta
    public List<DetalleVenta> obtenerDetallesVenta() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from DetalleVenta", DetalleVenta.class).list();
        }
    }
    
    public List<DetalleVenta> obtenerDetallesPorVentaId(Long idVenta) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from DetalleVenta where venta = :idVenta", DetalleVenta.class)
                    .setParameter("idVenta", idVenta)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}