package es.studium.Tiendecita;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * Conexión a la base de datos y métodos para la consulta
 * @author Alvca
 * @since 2021
 * @version 1.0
 */
public class BaseDatos
{
	String fecha;
	String fecha2;
	String BaseDatos="tiendecita";
	String login="ClaseStudium";
	String password="Studium2020;";
	String driver = "com.mysql.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/"+BaseDatos+"?useJDBCCompliantTimezoneShift=true&serverTimezone=UTC&useSSL=false";

	Connection connection = null;
	Statement statement = null;
	Statement statement1 = null;
	Statement statement2 = null;
	ResultSet rs = null;
/**
 * Método para conectar a la base de datos
 * @return connection
 */
	public Connection conectar()
	{
		try
		{
			//Cargar los controladores para el acceso a la BD
			Class.forName(driver);
			//Establecer la conexión con la BD Empresa
			connection = DriverManager.getConnection(url, login, password);
		}
		catch (ClassNotFoundException cnfe)
		{
			System.out.println("Error 1-"+cnfe.getMessage());
		}
		catch (SQLException sqle)
		{
			System.out.println("Error 2-"+sqle.getMessage());
		}
		return connection;
	}
	/**
	 * Método para la desconexión de la base de datos
	 * @param c Parámetro enviado para cerrar la conexión
	 */
	public void desconectar(Connection c)
	{
		try
		{
			if(c!=null)
			{
				c.close();
			}
		}
		catch (SQLException e)
		{
			System.out.println("Error 3-"+e.getMessage());
		}
	}
	/**
	 * Método para dar de alta un artículo.
	 * @param c Parámetro enviado de conexión
	 * @param sentencia Sentencia SQL
	 * @return resultado Nos indica si se ha realizado la sentencia SQL
	 */
	public int altaArticulo(Connection c, String sentencia)
	{
		int resultado = 1;
		try
		{
			//Crear una sentencia
			statement = c.createStatement();
			//Crear un objeto ResultSet para guardar lo obtenido
			//y ejecutar la sentencia SQL
			if((statement.executeUpdate(sentencia))==1)
			{
				resultado = 0;
			}
			else
			{
				resultado = 1;
			}
		}
		catch (SQLException sqle)
		{
			System.out.println("Error altaArticulo-"+sqle.getMessage());
		}
		return (resultado);
	}
	/**
	 * Método para dar de alta un Ticket
	 * @param c Parámetro enviado de conexión
	 * @param sentencia Sentencia SQL
	 */
	public void AltaTicket(Connection c, String sentencia)
	{
		try
		{
			//Crear una sentencia
			statement = c.createStatement();
			//Crear un objeto ResultSet para guardar lo obtenido
			//y ejecutar la sentencia SQL
			statement.executeUpdate(sentencia);
		}
		catch (SQLException sqle)
		{
			System.out.println("Error AltaTicket-"+sqle.getMessage());
		}
	}
	/**
	 * Método para obtener el ultimo ID del ticket
	 * @param c Parámetro enviado de conexión
	 * @param sentencia Sentencia SQL
	 * @return resultado Devuelve el id ultimo
	 */
	public int ultimoIdTicket(Connection c, String sentencia)
	{
		int resultado = 0; // INSERT incorrecto
		try
		{
			//Crear una sentencia
			statement = c.createStatement();
			//Crear un objeto ResultSet para guardar lo obtenido
			//y ejecutar la sentencia SQL
			if((statement.executeUpdate(sentencia))==1)
			{
				String sentenciaConsulta = "SELECT idTicket FROM tickets ORDER BY 1 DESC LIMIT 1";
				ResultSet rs = statement.executeQuery(sentenciaConsulta);
				if(rs.next())
				{
					resultado = rs.getInt("idTicket");
				}
			}
			else
			{
				resultado = 0;
			}
		}
		catch (SQLException sqle)
		{
			System.out.println("Error ultimoIdTicket-"+sqle.getMessage());
		}
		return (resultado);
	}
	/**
	 * Método para mostrar los artículos del Choice
	 * @param c Parámetro enviado de conexión
	 * @return resultado Devuelve los valores de la lista Choice
	 */
	public String consultarArticulosChoice(Connection c)
	{
		String resultado = "";
		try
		{
			String sentencia = "SELECT * FROM articulos";
			//Crear una sentencia
			statement = c.createStatement();
			//Crear un objeto ResultSet para guardar lo obtenido
			//y ejecutar la sentencia SQL
			rs = statement.executeQuery(sentencia);
			while (rs.next())
			{
				resultado = resultado + rs.getInt("idArticulo") + "-" +
						rs.getString("descripcionArticulo") + "-" +
						rs.getString("cantidadArticulo")+ "-" +
						rs.getString("precioArticulo")+"#";
			}
		}
		catch (SQLException sqle)
		{
			System.out.println("Error consultarArticulosChoice-"+sqle.getMessage());
		}
		return (resultado);
	}
	/**
	 * Método para borrar un articulo
	 * @param c Parámetro enviado de conexión
	 * @param idArticulo Parámetro enviado para eliminar articulo
	 * @return resultado Nos indica si se ha realizado la sentencia SQL
	 */
	public int borrarArticulos(Connection c, int idArticulo)
	{
		int resultado = 1;
		try
		{
			String sentencia1 = "DELETE FROM articulostickets WHERE idArticuloFK1 = "+ idArticulo;
			String sentencia2 = "DELETE FROM articulos WHERE idArticulo = "+ idArticulo;
			//Crear una sentencia
			statement1 = c.createStatement();
			statement2 = c.createStatement();
			// Ejecutar la sentencia SQL
			if((statement1.executeUpdate(sentencia1))==1)
			{
				if((statement2.executeUpdate(sentencia2))==1) 
				{
					resultado = 0;
				}
			}
			else
			{
				if((statement2.executeUpdate(sentencia2))==1) 
				{
					resultado = 0;
				}
				else
				{
					resultado = 1;
				}
			}
		}
		catch (SQLException sqle)
		{
			System.out.println("Error borrarArticulos-"+sqle.getMessage());
		}
		return (resultado);
	}
	/**
	 * Método para consultar un articulo
	 * @param c Parámetro enviado de conexión
	 * @param idArticulo Parámetro que nos indica el articulo a buscar
	 * @return Devuelve los artículos que coincidan con la búsqueda
	 */
	public String consultarArticulo(Connection c, int idArticulo)
	{
		String resultado = "";
		try
		{
			String sentencia = "SELECT * FROM articulos WHERE idArticulo="+idArticulo;
			//Crear una sentencia
			statement = c.createStatement();
			//Crear un objeto ResultSet para guardar lo obtenido
			//y ejecutar la sentencia SQL
			rs = statement.executeQuery(sentencia);
			rs.next();
			resultado = resultado + rs.getInt("idArticulo") + "#" +
					rs.getString("descripcionArticulo") + "#" +
					rs.getString("cantidadArticulo")+ "#" +
					rs.getString("precioArticulo")+ "#";

		}
		catch (SQLException sqle)
		{
			
		}
		return (resultado);
	}
	/**
	 * Método para modificar un articulo
	 * @param c Parámetro enviado de conexión
	 * @param sentencia Sentencia sql enviada para ejecutarse
	 * @return devuelve si la sentencia se ha ejecutado o no correctamente
	 */
	public int modificarArticulos(Connection c, String sentencia)
	{
		int resultado = 1;
		try
		{
			//Crear una sentencia
			statement = c.createStatement();
			// Ejecutar la sentencia SQL
			if((statement.executeUpdate(sentencia))==1)
			{
				resultado = 0;
			}
			else
			{
				resultado = 1;
			}
		}
		catch (SQLException sqle)
		{
			System.out.println("Error modificarArticulos-"+sqle.getMessage());
		}
		return (resultado);
	}
	/**
	 * Método para consultar todos los artículos, para ponerlos en una tabla
	 * @param c Parámetro enviado de conexión
	 * @return Devuelve todos los artículos
	 */
	public String consultarArticulosTabla(Connection c)
	{
		String resultado = "";
		try
		{
			String sentencia = "SELECT * FROM articulos";
			//Crear una sentencia
			statement = c.createStatement();
			//Crear un objeto ResultSet para guardar lo obtenido
			//y ejecutar la sentencia SQL
			rs = statement.executeQuery(sentencia);
			while (rs.next())
			{
				resultado = resultado + rs.getInt("idArticulo") + "#" +
						rs.getString("descripcionArticulo") + "#" +
						rs.getString("cantidadArticulo")+ "#" +
						rs.getString("precioArticulo")+"#";
			}
		}
		catch (SQLException sqle)
		{
			System.out.println("Error consultarArticulosTabla-"+sqle.getMessage());
		}
		return (resultado);
	}
	/**
	 * Método para consultar todos los artículos
	 * @param c Parámetro enviado de conexión
	 * @return Devuelve todos los artículos
	 */
	public String consultarArticulos(Connection c)
	{
		String resultado = "";
		try
		{
			String sentencia = "SELECT * FROM articulos";
			//Crear una sentencia
			statement = c.createStatement();
			//Crear un objeto ResultSet para guardar lo obtenido
			//y ejecutar la sentencia SQL
			rs = statement.executeQuery(sentencia);
			while (rs.next())
			{
				resultado = resultado + rs.getInt("idArticulo") + "-" +
						rs.getString("descripcionArticulo") + "-" +
						rs.getString("cantidadArticulo")+ "-" +
						rs.getString("precioArticulo")+"\n";
			}
		}
		catch (SQLException sqle)
		{
			System.out.println("Error consultarArticulos-"+sqle.getMessage());
		}
		return (resultado);
	}
	/**
	 * Método para mostrar todos los tickes que después se pondrán en una tabla
	 * @param c Parámetro enviado de conexión
	 * @return Todos los tickets
	 */
	public String consultarTicketsTabla(Connection c)
	{
		String resultado = "";
		try
		{
			String sentencia = "SELECT * FROM tickets";
			//Crear una sentencia
			statement = c.createStatement();
			//Crear un objeto ResultSet para guardar lo obtenido
			//y ejecutar la sentencia SQL
			rs = statement.executeQuery(sentencia);
			while (rs.next())
			{
				resultado = resultado + rs.getInt("idTicket") + "#" +
						rs.getString("fechaTicket") + "#" +
						rs.getString("totalTicket")+ "#";
			}
		}
		catch (SQLException sqle)
		{
			System.out.println("Error consultarTicketsTabla-"+sqle.getMessage());
		}
		return (resultado);
	}
	/**
	 * Método para comprobar los tickets realizados entre dos fechas
	 * @param c Parámetro enviado de conexión
	 * @param fecha Fecha inicial de búsqueda
	 * @param fecha2 Fecha final de búsqueda
	 * @return Devuelve todos los tickets que se encuentren entre las dos fechas, ambas incluidas
	 */
	public String consultarTicketsTablaFecha(Connection c, String fecha, String fecha2)
	{
		String resultado = "";
		this.fecha=fecha;
		this.fecha2=fecha2;
		try
		{
			String sentencia = "SELECT * FROM tickets where fechaTicket between \""+fecha+"\" and \""+fecha2+"\";";
			System.out.println(sentencia);
			//Crear una sentencia
			statement = c.createStatement();
			//Crear un objeto ResultSet para guardar lo obtenido
			//y ejecutar la sentencia SQL
			rs = statement.executeQuery(sentencia);
			while (rs.next())
			{
				resultado = resultado + rs.getInt("idTicket") + "#" +
						rs.getString("fechaTicket") + "#" +
						rs.getString("totalTicket")+ "#";
			}
			System.out.println(resultado);
		}
		catch (SQLException sqle)
		{
			System.out.println("Error consultarTicketsTabla-"+sqle.getMessage());
		}
		return (resultado);
	}
	/**
	 * Método para generar el choice de los tickets
	 * @param c Parámetro enviado de conexión
	 * @return Devuelve todos los tickets
	 */
	public String consultarTicketsChoice(Connection c)
	{
		String resultado = "";
		try
		{
			String sentencia = "SELECT * FROM tickets";
			//Crear una sentencia
			statement = c.createStatement();
			//Crear un objeto ResultSet para guardar lo obtenido
			//y ejecutar la sentencia SQL
			rs = statement.executeQuery(sentencia);
			while (rs.next())
			{
				resultado = resultado + rs.getInt("idTicket") + " - " +
						rs.getString("fechaTicket") + " - " +
						rs.getString("totalTicket")+"#";
			}
		}
		catch (SQLException sqle)
		{
			System.out.println("Error consultarTicketsChoice-"+sqle.getMessage());
		}
		return (resultado);
	}
	/**
	 * Método para generar las líneas de los artículos asociados a un ticket usado para la visualización de la tabla
	 * @param c Parámetro enviado de conexión
	 * @param idTicketConsultar Parámetro que nos indica el ticket a buscar
	 * @return Nos devuelve el resultado de la sentencia SQL, mostrando las líneas de artículos del ticket seleccionado
	 * */
	public String consultarTicketsTabla2(Connection c, String idTicketConsultar)
	{
		String resultado = "";
		try
		{
			
			String sentencia = "SELECT descripcionArticulo, articulostickets.cantidadArticulo, precioArticulo FROM tiendecita.articulos, tiendecita.articulostickets, tiendecita.tickets where idTicket="+idTicketConsultar+" AND idArticulo=idArticuloFK1 AND idTicket=idTicketFK2";
			//Crear una sentencia
			statement = c.createStatement();
			//Crear un objeto ResultSet para guardar lo obtenido
			//y ejecutar la sentencia SQL
			rs = statement.executeQuery(sentencia);
			while (rs.next())
			{
				resultado = resultado + rs.getString("descripcionArticulo") + "#" +
						rs.getString("articulostickets.cantidadArticulo") + "#" +
						rs.getString("precioArticulo")+"#";
			}
		}
		catch (SQLException sqle)
		{
			System.out.println("Error consultarTicketsTabla2-"+sqle.getMessage());
		}
		return (resultado);
	}
	/**
	 * Método que devuelve un listado de todos los tickets
	 * @param c Parámetro enviado de conexión
	 * @return Nos devuelve el resultado de la sentencia SQL, mostrándonos todos los tickets
	 */
	public String consultarTicketsTablaPDF1(Connection c)
	{
		String resultado = "";
		try
		{
			String sentencia = "SELECT * FROM tickets";
			//Crear una sentencia
			statement = c.createStatement();
			//Crear un objeto ResultSet para guardar lo obtenido
			//y ejecutar la sentencia SQL
			rs = statement.executeQuery(sentencia);
			while (rs.next())
			{
				resultado = resultado + rs.getInt("idTicket") + " - " +
						rs.getString("fechaTicket") + " - " +
						rs.getString("totalTicket")+ "\n";
			}
		}
		catch (SQLException sqle)
		{
			System.out.println("Error consultarTicketsTabla-"+sqle.getMessage());
		}
		return (resultado);
	}
	/**
	 * Método para generar las líneas de los artículos asociados a un ticket usado para el informe.
	 * @param c Parámetro enviado de conexión
	 * @param idTicketConsultar Parámetro que nos indica el ticket a buscar
	 * @return Nos devuelve el resultado de la sentencia SQL, mostrando las líneas de artículos del ticket seleccionado
	 */
	public String consultarTicketsTablaPDF2(Connection c, String idTicketConsultar)
	{
		String resultado = "";
		try
		{
			
			String sentencia = "SELECT descripcionArticulo, articulostickets.cantidadArticulo, precioArticulo FROM tiendecita.articulos, tiendecita.articulostickets, tiendecita.tickets where idTicket="+idTicketConsultar+" AND idArticulo=idArticuloFK1 AND idTicket=idTicketFK2";
			//Crear una sentencia
			statement = c.createStatement();
			//Crear un objeto ResultSet para guardar lo obtenido
			//y ejecutar la sentencia SQL
			rs = statement.executeQuery(sentencia);
			while (rs.next())
			{
				resultado = resultado + rs.getString("descripcionArticulo") + " - " +
						rs.getString("articulostickets.cantidadArticulo") + " - " +
						rs.getString("precioArticulo")+"\n";
			}
		}
		catch (SQLException sqle)
		{
			System.out.println("Error consultarTicketsTabla2-"+sqle.getMessage());
		}
		return (resultado);
	}
}