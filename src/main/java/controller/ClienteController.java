/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.ClienteDAO;
import entidades.Cliente;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.List;

/**
 *
 * @author Asus
 */
@ManagedBean
@ViewScoped
public class ClienteController {
    private Cliente cliente = new Cliente();
    private ClienteDAO clienteDAO = new ClienteDAO();
    private List<Cliente> listaClientes;

    public void guardarCliente() {
        clienteDAO.guardarCliente(cliente);
        cliente = new Cliente(); // Limpiar formulario
    }

    public String obteneNombreID(Long id){
        return clienteDAO.obtenerNombrePorId(id);
    }
    
    public List<Cliente> getListaClientes() {
        if (listaClientes == null) {
            listaClientes = clienteDAO.obtenerClientes();
        }
        return listaClientes;
    }

    public boolean isListaClientesVacia() {
        return getListaClientes() == null || getListaClientes().isEmpty();
    }

    // Getters y setters
    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}