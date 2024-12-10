/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entidades.Venta;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;
import services.HibernateUtil;

/**
 *
 * @author Asus
 */
public class VentaDAO {
    public void guardarVenta(Venta venta) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(venta);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    // Método para obtener todas las ventas
    public List<Venta> obtenerVentas() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Venta", Venta.class).list();
        }
    }

    public Venta obtenerVentaPorId(Long idVenta) {
        Transaction transaction = null;
        Venta venta = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            venta = session.get(Venta.class, idVenta);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return venta;
    }
}