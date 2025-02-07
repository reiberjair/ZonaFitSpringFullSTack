package gm.zona_fit.gui;

import gm.zona_fit.modelo.Cliente;
import gm.zona_fit.servicio.ClienteServicio;
import gm.zona_fit.servicio.IClienteServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


//@Component
public class ZonaFItForma extends JFrame{
    private JPanel panelPrincipal;
    private JTable clientesTabla;
    private JTextField nombreTexto;
    private JTextField apellidoTetxo;
    private JTextField membresiaTexto;
    private JButton guardarButton;
    private JButton eliminarButton;
    private JButton limpiarButton;
    IClienteServicio clienteServicio;
    private DefaultTableModel tablaModeloClientes;
    private Integer idCliente;

    @Autowired
    public ZonaFItForma(ClienteServicio clienteServicio){
        this.clienteServicio = clienteServicio;
        iniciarForma();
        guardarButton.addActionListener(e -> guardarCliente());
        clientesTabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                cargarClienteSeleccionado();
            }
        });
        eliminarButton.addActionListener(e -> eliminarCliente());
        limpiarButton.addActionListener(e -> limpiarFormulario());
    }

    private void iniciarForma(){

        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900,700);
        setLocationRelativeTo(null);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        //this.tablaModeloClientes = new DefaultTableModel(0, 4);
        this.tablaModeloClientes = new DefaultTableModel(0, 4){
            //Restringimo la edicion de los registros directamente en la tbala
            @Override
                    public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        String[] cabeceros = {"Id", "Nombre", "Apellido", "Membresia"};
        this.tablaModeloClientes.setColumnIdentifiers(cabeceros);
        this.clientesTabla = new JTable(tablaModeloClientes);
        //Restringimos la seleccion de la tabla a un solo registro
        this.clientesTabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //Cargar Listado de cleintes
        listarClientes();
    }

    private void listarClientes(){
        this.tablaModeloClientes.setRowCount(0);
        var clientes = this.clienteServicio.listarClientes();
        clientes.forEach(cliente -> {
            Object[] renglonCLiente = {
//                    cliente.getId(),
//                    cliente.getNombre(),
//                    cliente.getApellido(),
//                    cliente.getMembresia()
            };
            this.tablaModeloClientes.addRow(renglonCLiente);
        });
    }

    private void guardarCliente(){
        if (nombreTexto.getText().equals("")){
            mostrarMensaje("Proporciona un nombre.");
            nombreTexto.requestFocusInWindow();
            return;
        }
        if (membresiaTexto.getText().equals("")){
            mostrarMensaje("Proporciona una membresia");
            membresiaTexto.requestFocusInWindow();
            return;
        }
        //Recuperar los valores del formulario
        var nombre = nombreTexto.getText();
        var apellido = apellidoTetxo.getText();
        var membresia = Integer.parseInt(membresiaTexto.getText());
        //var cliente = new Cliente(this.idCliente, nombre, apellido, membresia);

       // this.clienteServicio.guardarCliente(cliente);// se peude gaurdar y modificar

        if (this.idCliente == null){
            mostrarMensaje("Se agrego un nuevo cliente.");
        }else {
            //mostrarMensaje("Se modifico el cliente" + cliente);
        }
        limpiarFormulario();
        listarClientes();
        mostrarMensaje("Cliente guardado.");

    }

    private void eliminarCliente(){
        var renglon = clientesTabla.getSelectedRow();
        if (renglon != -1){// -1 significa que no selecciono ningun registro
            var id = clientesTabla.getModel().getValueAt(renglon, 0).toString();
            this.idCliente = Integer.parseInt(id);
            var cliente = new Cliente();
            //cliente.setId(this.idCliente);
            clienteServicio.eliminarCliente(cliente);
            limpiarFormulario();
            listarClientes();
            mostrarMensaje("Cliente eliminado correctamente.");
        } else {
            mostrarMensaje("Debe seleccionar un registro.");
        }
    }

    private void cargarClienteSeleccionado(){
        var renglon = clientesTabla.getSelectedRow();
        if (renglon != -1){// -1 significa que no selecciono ningun registro
            var id = clientesTabla.getModel().getValueAt(renglon, 0).toString();
            this.idCliente = Integer.parseInt(id);
            var nombre = clientesTabla.getModel().getValueAt(renglon,1).toString();
            this.nombreTexto.setText(nombre);
            var apellido = clientesTabla.getModel().getValueAt(renglon, 2).toString();
            this.apellidoTetxo.setText(apellido);
            var membresia = clientesTabla.getModel().getValueAt(renglon,3).toString();
            this.membresiaTexto.setText(membresia);
        }
    }

    private void mostrarMensaje(String mensaje){
    JOptionPane.showMessageDialog(this, mensaje);
    }

    private void limpiarFormulario(){
        nombreTexto.setText("");
        apellidoTetxo.setText("");
        membresiaTexto.setText("");
        //Limpiamos el id del cleinte seleccionado
        this.idCliente = null;
        //Deseceleccionamos el registro seleccionado de la tbala
        this.clientesTabla.getSelectionModel().clearSelection();
    }
}
