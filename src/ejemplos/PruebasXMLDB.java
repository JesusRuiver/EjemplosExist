package ejemplos;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Node;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;

import org.xmldb.api.base.XMLDBException;

import org.xmldb.api.modules.XMLResource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;

import org.xmldb.api.modules.CollectionManagementService;

import org.xmldb.api.modules.XPathQueryService;

public class PruebasXMLDB {

	public static void main(String[] args) {
//		try {
//			bajardocumento();
//		} catch (TransformerConfigurationException e) {
//
//			e.printStackTrace();
//		} catch (TransformerException e) {
//
//			e.printStackTrace();
//		}

		try {
			verempleados10();
		} catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//	vercolecciones();
	//	verrecursosdelascolecciones();
	}

	public static void verempleados10() throws XMLDBException {
		String driver = "org.exist.xmldb.DatabaseImpl"; // Driver para eXist
		Collection col = null; // Colección
		String URI = "xmldb:exist://localhost:8081/exist/xmlrpc/db/ColeccionPruebas";
		String usu = "admin"; // Usuario
		String usuPwd = "admin"; // Clave
		try {
			Class cl = Class.forName(driver);
			Database database = (Database) cl.newInstance();
			DatabaseManager.registerDatabase(database);

			col = DatabaseManager.getCollection(URI, usu, usuPwd);
			if (col == null)
				System.out.println(" *** LA COLECCION NO EXISTE. ***");
			XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
			ResourceSet result = servicio.query("for $em in /EMPLEADOS/EMP_ROW[DEPT_NO=10] return $em");
			// Recorrer los datos del recurso.
			ResourceIterator i;
			i = result.getIterator();
			if (!i.hasMoreResources())
				System.out.println(" LA CONSULTA NO DEVUELVE NADA.");
			while (i.hasMoreResources()) {
				Resource r = i.nextResource();
				System.out.println((String) r.getContent());
			}
			col.close(); // borramos
		} catch (Exception e) {
			System.out.println("Error al inicializar la BD eXist");
			e.printStackTrace();
		}
	}// FIN verempleados10

	public static void bajardocumento() throws TransformerConfigurationException, TransformerException {
		// Localizar un documento, extraerlo y guardarlo en disco
		String driver = "org.exist.xmldb.DatabaseImpl"; // Driver para eXist
		try {
			Class cl = Class.forName(driver); // Cargar del driver
			Database database = (Database) cl.newInstance(); // Instancia de la
																// BD
			DatabaseManager.registerDatabase(database); // Registro del driver
			String URI = "xmldb:exist://localhost:8080/exist/xmlrpc/db/BDProductosXML";
			String usu = "admin"; // Usuario
			String usuPwd = "root"; // Clave
			Collection col = DatabaseManager.getCollection(URI, usu, usuPwd);
			XMLResource res = (XMLResource) col.getResource("zonas.xml");
			if (res == null) {
				System.out.println("NO EXISTE EL DOCUMENTO");
			} else {
				System.out.println("ID del documento: " + res.getDocumentId());
				// Volcado del documento a un árbol DOM
				Node document = (Node) res.getContentAsDOM();
				Source source = new DOMSource(document);
				// Volcado del documento de memoria a consola
				Transformer transformer = TransformerFactory.newInstance().newTransformer();
				Result console = new StreamResult(System.out);
				transformer.transform(source, console);
				// Volcado del documento a un fichero
				Result fichero = new StreamResult(new java.io.File("./zonas.xml"));
				transformer = TransformerFactory.newInstance().newTransformer();
				transformer.transform(source, fichero);
			}
		} catch (ClassNotFoundException ex) {
			System.out.println(" ERROR EN EL DRIVER. COMPRUEBA CONECTORES.");
		} catch (InstantiationException ex) {
			System.out.println("Error al crear Instancia de la BD (Database) cl.newInstance()");
		} catch (IllegalAccessException ex) {
			System.out.println("Error al crear Instancia de la BD (Database) cl.newInstance()");
		} catch (XMLDBException ex) {
			ex.printStackTrace();
		}
	}

	public static void vercolecciones() {
		String driver = "org.exist.xmldb.DatabaseImpl";
		try {
			Class cl = Class.forName(driver);
			Database database = (Database) cl.newInstance();
			DatabaseManager.registerDatabase(database);
			String URI = "xmldb:exist://localhost:8080/exist/xmlrpc/db/";
			String usu = "admin";
			String usuPwd = "root";
			Collection col = DatabaseManager.getCollection(URI, usu, usuPwd);
			System.out.println("Número de colecciones: " + col.getChildCollectionCount());
			String[] colecciones = col.listChildCollections();
			for (int j = 0; j < colecciones.length; j++) {
				System.out.println(colecciones[j]);
				// Collection colecc = col.getChildCollection(colecciones[j]);
			}
		} catch (ClassNotFoundException ex) {
			System.out.println(" ERROR EN EL DRIVER. COMPRUEBA CONECTORES.");
		} catch (IllegalAccessException ex) {
			System.out.println("Error al crear Instancia de la BD (Database) cl.newInstance()");
		} catch (XMLDBException ex) {
			Logger.getLogger(PruebasXMLDB.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			Logger.getLogger(PruebasXMLDB.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static void verrecursosdelascolecciones() {
		String driver = "org.exist.xmldb.DatabaseImpl";
		try {
			Class cl = Class.forName(driver);
			Database database = (Database) cl.newInstance();
			DatabaseManager.registerDatabase(database);
			String URI = "xmldb:exist://localhost:8080/exist/xmlrpc/db/";
			String usu = "admin";
			String usuPwd = "root";
			Collection col = DatabaseManager.getCollection(URI, usu, usuPwd);
			System.out.println("Número de colecciones: " + col.getChildCollectionCount());
			String[] colecciones = col.listChildCollections();
			for (int j = 0; j < colecciones.length; j++) {
				System.out.println("-------------------------- ");
				System.out.println(colecciones[j]);
				Collection colecc = col.getChildCollection(colecciones[j]);
				String[] lista = colecc.listResources();
				for (int i = 0; i < lista.length; i++) {
					Resource res = (Resource) colecc.getResource(lista[i]);
					System.out.println("ID del documento: " + res.getId());
					System.out.println("Contenido del documento:\n " + res.getContent());
				}
			}
		} catch (ClassNotFoundException ex) {
			System.out.println(" ERROR EN EL DRIVER. COMPRUEBA CONECTORES.");
		} catch (IllegalAccessException ex) {
			System.out.println("Error al crear Instancia de la BD (Database) cl.newInstance()");
		} catch (XMLDBException ex) {
			ex.printStackTrace();
		} catch (InstantiationException ex) {
			ex.printStackTrace();
		}
	}

	public static void prueba1() {
		String driver = "org.exist.xmldb.DatabaseImpl"; // Driver para eXist
		try {
			Class cl = Class.forName(driver); // Cargar del driver
			Database database = (Database) cl.newInstance(); // Instancia de la
																// BD
			DatabaseManager.registerDatabase(database); // Registro del driver

			String URI = "xmldb:exist://localhost:8080/exist/xmlrpc/db/BDProductosXML";
			String usu = "admin"; // Usuario
			String usuPwd = "root"; // Clave
			Collection col = DatabaseManager.getCollection(URI, usu, usuPwd);

			XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
			ResourceSet result = servicio.query("for $p in /productos/produc return $p");
			ResourceIterator i; // se utiliza para recorrer un set de recursos
			i = result.getIterator();
			if (!i.hasMoreResources()) {
				System.out.println(" LA CONSULTA NO DEVUELVE NADA.");
			}
			while (i.hasMoreResources()) {
				Resource r = i.nextResource();
				System.out.println("Elemento: " + (String) r.getContent());
			}

		} catch (ClassNotFoundException ex) {
			System.out.println(" ERROR EN EL DRIVER. COMPRUEBA CONECTORES.");
		} catch (InstantiationException ex) {
			System.out.println("Error al crear Instancia de la BD (Database) cl.newInstance()");
		} catch (IllegalAccessException ex) {
			System.out.println("Error al crear Instancia de la BD (Database) cl.newInstance()");
		} catch (XMLDBException ex) {
			Logger.getLogger(PruebasXMLDB.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	public static void actualizaproductos() {
		String driver = "org.exist.xmldb.DatabaseImpl"; // Driver para eXist
		try {
			Class cl = Class.forName(driver); // Cargar del driver
			Database database = (Database) cl.newInstance(); // Instancia de la
																// BD
			DatabaseManager.registerDatabase(database); // Registro del driver

			String URI = "xmldb:exist://localhost:8080/exist/xmlrpc/db/BDProductosXML";
			String usu = "admin"; // Usuario
			String usuPwd = "root"; // Clave
			Collection col = DatabaseManager.getCollection(URI, usu, usuPwd);

			XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
			String consulta = " for $p in collection('/db/BDProductosXML')/productos/produc "
					+ " let $st := $p/stock_actual " + " return update value $p/stock_actual " + " with $st+10 ";
			ResourceSet result = servicio.query(consulta);
			ResourceIterator i; // se utiliza para recorrer un set de recursos
			i = result.getIterator();
			if (!i.hasMoreResources()) {
				System.out.println(" LA CONSULTA NO DEVUELVE NADA.");
			}
			while (i.hasMoreResources()) {
				Resource r = i.nextResource();
				System.out.println("Elemento: " + (String) r.getContent());
			}

		} catch (ClassNotFoundException ex) {
			System.out.println(" ERROR EN EL DRIVER. COMPRUEBA CONECTORES.");
		} catch (InstantiationException ex) {
			System.out.println("Error al crear Instancia de la BD (Database) cl.newInstance()");
		} catch (IllegalAccessException ex) {
			System.out.println("Error al crear Instancia de la BD (Database) cl.newInstance()");
		} catch (XMLDBException ex) {
			Logger.getLogger(PruebasXMLDB.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	public static void prueba2() throws TransformerConfigurationException, TransformerException {
		// Localizar un documento, extraerlo y guardarlo en disco
		String driver = "org.exist.xmldb.DatabaseImpl"; // Driver para eXist
		try {
			Class cl = Class.forName(driver); // Cargar del driver
			Database database = (Database) cl.newInstance(); // Instancia de la
																// BD
			DatabaseManager.registerDatabase(database); // Registro del driver
			String URI = "xmldb:exist://localhost:8080/exist/xmlrpc/db/BDProductosXML";
			String usu = "admin"; // Usuario
			String usuPwd = "root"; // Clave
			Collection col = DatabaseManager.getCollection(URI, usu, usuPwd);
			XMLResource res = (XMLResource) col.getResource("zonas.xml");
			if (res == null) {
				System.out.println("NO EXISTE EL DOCUMENTO");
			} else {
				System.out.println("ID del documento: " + res.getDocumentId());
				// Volcado del documento a un arbol DOM
				Node document = res.getContentAsDOM();
				Source source = new DOMSource(document);
				Transformer transformer = TransformerFactory.newInstance().newTransformer();
				// Volcado del documento de memoria a consola
				Result console = new StreamResult(System.out);
				transformer.transform(source, console);
				// Volcado del documento a un fichero
				Result fichero = new StreamResult(new java.io.File("./zonas.xml"));
				transformer = TransformerFactory.newInstance().newTransformer();
				transformer.transform(source, fichero);
			}
		} catch (ClassNotFoundException ex) {
			System.out.println(" ERROR EN EL DRIVER. COMPRUEBA CONECTORES.");
		} catch (InstantiationException ex) {
			System.out.println("Error al crear Instancia de la BD (Database) cl.newInstance()");
		} catch (IllegalAccessException ex) {
			System.out.println("Error al crear Instancia de la BD (Database) cl.newInstance()");
		} catch (XMLDBException ex) {
			Logger.getLogger(PruebasXMLDB.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static void crearcoleccysubirarchivo(String colecc) throws XMLDBException {
		String driver = "org.exist.xmldb.DatabaseImpl";
		Collection col = null;
		String URI = "xmldb:exist://localhost:8080/exist/xmlrpc/db";
		String usu = "admin";
		String usuPwd = "root";
		try {
			Class cl = Class.forName(driver);
			Database database = (Database) cl.newInstance();
			DatabaseManager.registerDatabase(database);
			col = DatabaseManager.getCollection(URI, usu, usuPwd);
			if (col != null) {
				// CREAR COLECCION dentro de col,
				CollectionManagementService mgtService = (CollectionManagementService) col
						.getService("CollectionManagementService", "1.0");
				mgtService.createCollection(colecc);
				System.out.println(" *** COLECCION CREADA: " + colecc);
			}
			// Nos posicionamos en la nueva colección y a?adimos el archivo
			// Si es un ficheo binario ponemos BinaryResource
			URI = "xmldb:exist://localhost:8080/exist/xmlrpc/db/" + colecc;
			col = DatabaseManager.getCollection(URI, usu, usuPwd);
			File archivo = new File("NUEVAS_ZONAS.xml");
			if (!archivo.canRead()) {
				System.out.println("ERROR AL LEER EL FICHERO");
			} else {
				Resource nuevoRecurso = col.createResource(archivo.getName(), "XMLResource");
				nuevoRecurso.setContent(archivo);
				col.storeResource(nuevoRecurso);
				System.out.println("FICHERO A?ADIDO");
			}
		} catch (Exception e) {
			System.out.println("Error al inicializar la BD eXist");
		}
	}

	public static void borrarcoleccion(String colecc) throws XMLDBException {
		String driver = "org.exist.xmldb.DatabaseImpl";
		Collection col = null;
		String URI = "xmldb:exist://localhost:8080/exist/xmlrpc/db";
		String usu = "admin";
		String usuPwd = "root";
		try {
			Class cl = Class.forName(driver);
			Database database = (Database) cl.newInstance();
			DatabaseManager.registerDatabase(database);
			col = DatabaseManager.getCollection(URI, usu, usuPwd);
			CollectionManagementService mgtService = (CollectionManagementService) col
					.getService("CollectionManagementService", "1.0");
			mgtService.removeCollection(colecc);
			System.out.println(" *** COLECCION BORRADA. ***");
		} catch (Exception e) {
			System.out.println("Error al inicializar la BD eXist");
		}
	}

	public static void borrarfichero(String colecc, String fichero) throws XMLDBException {
		String driver = "org.exist.xmldb.DatabaseImpl";
		Collection col = null;
		String URI = "xmldb:exist://localhost:8080/exist/xmlrpc/db/" + colecc;
		String usu = "admin";
		String usuPwd = "root";
		try {
			Class cl = Class.forName(driver);
			Database database = (Database) cl.newInstance();
			DatabaseManager.registerDatabase(database);
			col = DatabaseManager.getCollection(URI, usu, usuPwd);
			// borrar un xml de la colección
			String ff = "'" + fichero + "'";
			Resource recursoParaBorrar = col.getResource(fichero);
			col.removeResource(recursoParaBorrar);
			System.out.println("FICHERO BORRADO.");
		} catch (NullPointerException e) {
			System.out.println("No se puede borrar. No se encuentra.");
		} catch (ClassNotFoundException ex) {
			System.out.println("ERROR DRIVER.");
		} catch (InstantiationException ex) {
			System.out.println("ERROR AL CREAR LA INSTANCIA.");
		} catch (IllegalAccessException ex) {
			System.out.println("ERROR AL CREAR LA INSTANCIA.");
		}
	}

	public static void ejecutarconsultafichero(String fichero) {
		String driver = "org.exist.xmldb.DatabaseImpl"; // Driver para eXist
		try {
			Class cl = Class.forName(driver); // Cargar del driver
			Database database = (Database) cl.newInstance(); // Instancia de la
																// BD
			DatabaseManager.registerDatabase(database); // Registro del driver
			String URI = "xmldb:exist://localhost:8080/exist/xmlrpc/db/BDProductosXML";
			String usu = "admin"; // Usuario
			String usuPwd = "root"; // Clave
			Collection col = DatabaseManager.getCollection(URI, usu, usuPwd);
			System.out.println("Convirtiendo el fichero a cadena...");
			BufferedReader entrada = new BufferedReader(new FileReader(fichero));
			String linea = null;
			StringBuilder stringBuilder = new StringBuilder();
			String salto = System.getProperty("line.separator"); // es el salto
																	// de línea
																	// \n

			while ((linea = entrada.readLine()) != null) {
				stringBuilder.append(linea);
				stringBuilder.append(salto);
			}
			String consulta = stringBuilder.toString();
			// Ejecutar consulta
			System.out.println("Consulta: " + consulta);
			XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
			ResourceSet result = servicio.query(consulta);
			ResourceIterator i; // se utiliza para recorrer un set de recursos
			i = result.getIterator();
			if (!i.hasMoreResources()) {
				System.out.println(" LA CONSULTA NO DEVUELVE NADA.");
			}
			while (i.hasMoreResources()) {
				Resource r = i.nextResource();
				System.out.println("Elemento: " + (String) r.getContent());
			}

		} catch (ClassNotFoundException ex) {
			System.out.println("ERROR EN EL DRIVER.");
		} catch (InstantiationException ex) {
			System.out.println("ERROR AL CREAR LA INSTANCIA.");
		} catch (IllegalAccessException ex) {
			System.out.println("ERROR AL CREAR LA INSTANCIA.");
		} catch (XMLDBException ex) {
			System.out.println("ERROR AL OPERAR CON EXIST.");
		} catch (FileNotFoundException ex) {
			System.out.println("El fichero no se localiza: " + fichero);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
