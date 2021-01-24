package es.studium.Tiendecita;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.util.HashMap;
import java.awt.Desktop;
import java.awt.event.ActionEvent;

public class TicketsConsulta2 extends JFrame {
	String idTicket;
	String fecha;
	BaseDatos bd = new BaseDatos();
	Connection conexion = null;
	double total=0.0;
	double subtotal=0.0;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFecha;
	private JTable tableTicket;
	private JTextField textTotal;
	private JTextField textIdTicket;
	/**
	 * Create the frame.
	 */
	public TicketsConsulta2(String idTicket, String fecha ) {
		this.idTicket=idTicket;
		this.fecha=fecha;
		setTitle("Consulta Ticket");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 700, 516);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblFecha = new JLabel("Fecha:");
		lblFecha.setBounds(437, 50, 56, 16);
		contentPane.add(lblFecha);

		textFecha = new JTextField();
		textFecha.setEditable(false);
		textFecha.setBounds(493, 47, 177, 22);
		contentPane.add(textFecha);
		textFecha.setColumns(10);

		conexion = bd.conectar();
		String[] data1 = bd.consultarTicketsTabla2(conexion, idTicket).split("#");
		//creamos el arreglo de objetos que contendra el
		//contenido de las columnas
		Object[] data = new Object[3];
		// creamos el modelo de Tabla
		DefaultTableModel dtm= new DefaultTableModel();
		// se crea la Tabla con el modelo DefaultTableModel
		tableTicket = new JTable(dtm);
		// insertamos las columnas
		dtm.addColumn("Articulo");
		dtm.addColumn("Cantidad");
		dtm.addColumn("precio");
		// insertamos el contenido de las columnas
		for(int row = 0; row < data1.length;) {
			data[0] = data1[row];
			data[1] = data1[row+1];
			data[2] = data1[row+2];
			dtm.addRow(data);
			row=row+3;
			subtotal= (Double.parseDouble((String) data[1])*Double.parseDouble((String)data[2]));
			total=total+subtotal;
		}

		tableTicket.setBounds(12, 93, 658, 258);
		contentPane.add(tableTicket);

		JLabel lblTotal = new JLabel("Total:");
		lblTotal.setBounds(480, 375, 42, 16);
		contentPane.add(lblTotal);

		textTotal = new JTextField();
		textTotal.setEditable(false);
		textTotal.setBounds(533, 372, 137, 22);
		contentPane.add(textTotal);
		textTotal.setColumns(10);
		textTotal.setText(String.valueOf(total));
		JButton btnAceptar = new JButton("Aceptar");
		btnAceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				//Cierra la aplicacion
				//System.exit(0);
			}
		});
		btnAceptar.setBounds(12, 431, 97, 25);
		contentPane.add(btnAceptar);

		textIdTicket = new JTextField();
		textIdTicket.setEditable(false);
		textIdTicket.setColumns(10);
		textIdTicket.setBounds(74, 47, 177, 22);
		contentPane.add(textIdTicket);

		textFecha.setText(fecha);
		textIdTicket.setText(idTicket);

		JLabel lblIdTicket = new JLabel("N\u00BA Ticket:");
		lblIdTicket.setBounds(12, 47, 68, 16);
		contentPane.add(lblIdTicket);

		JButton btnPDF = new JButton("PDF");
		btnPDF.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imprimirPDF();
			}
		});
		btnPDF.setBounds(573, 431, 97, 25);
		contentPane.add(btnPDF);

		JButton btnImprimir = new JButton("Imprimir");
		btnImprimir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imprimirPDF();
			}
		});
		btnImprimir.setBounds(464, 431, 97, 25);
		contentPane.add(btnImprimir);
		setVisible(true);
	}
	public void imprimirPDF(){
		try 
		{ 
			// Compilar el informe generando fichero jasper
			JasperCompileManager.compileReportToFile("Ticket.jrxml");
			System.out.println("Fichero Ticket.jasper generado CORRECTAMENTE!"); 
			// Objeto para guardar parámetros necesarios para el informe 
			HashMap<String,Object> parametros = new HashMap<String,Object>();
			//Guardamos los parametros del informe
			parametros.put("titulo", "Factura Simplificada");
			parametros.put("nTicket", idTicket);
			parametros.put("fecha", fecha);
			parametros.put("total", total);
			parametros.put("idTicketConsultar", idTicket);
			
			// Cargar el informe compilado 
			JasperReport report = (JasperReport) JRLoader.loadObjectFromFile("Ticket.jasper"); 
			// Conectar a la base de datos para sacar la información 
			Connection conexion2 = bd.conectar();
			// Completar el informe con los datos de la base de datos 
			JasperPrint print = JasperFillManager.fillReport(report, parametros, conexion2);
			//Desconexion de la BD
			bd.desconectar(conexion2);
			// Mostrar el informe en JasperViewer 
			JasperViewer.viewReport(print, false); 
			// Para exportarlo a pdf 
			JasperExportManager.exportReportToPdfFile(print, "Ticket.pdf");
			// Abrir el fichero PDF generado 
			File path = new File ("Ticket.pdf");
			Desktop.getDesktop().open(path);
		} 
		catch (Exception e) 
		{ 
			System.out.println("Error: " + e.toString()); 
		}
	}
}
