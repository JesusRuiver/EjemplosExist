package ejemplos;


import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XPathQueryService;

public class PruebasEmpleYDepart {

    public static final String nombrefichero = "AleatorioEmple.dat";
    static Scanner teclado = new Scanner(System.in);
    static String driver = "org.exist.xmldb.DatabaseImpl"; //Driver para eXist
    static String URI = "xmldb:exist://localhost:8081/exist/xmlrpc/db/ColeccionPruebas"; //URI colección   
    static String usu = "admin"; //Usuario
    static String usuPwd = "admin"; //Clave
    static Collection col = null;

    public static void main(String[] args) {

        int op = 0, n = 0;
        do {
            dibujamenu();
            System.out.println("TECLEA OPERACIÓN: ");
            op = teclado.nextInt();
            switch (op) {
                case 1:
                    listardep();
                    break;
                case 2:
                	//Inserta uno fijo
                    insertardep(21, "El 21","Talavera");
                    break;
                case 3: // consultar un dep
                    System.out.println("Teclea el número de departamento a consultar: ");
                    n = teclado.nextInt();
                    consultardep(n);
                    break;
                case 4: //Modificar
                    System.out.println("Teclea el número de dep a modificar: ");
                    n = teclado.nextInt();
                    modificardep(n);
                    break;
                case 5: // borrar un dep
                    System.out.println("Teclea el número de dep a borrar: ");
                    n = teclado.nextInt();
                    borrarregistrodep(n);
                    break;

            }//cierra Sub menu

        } while (op != 0);

    }//fin main
////////////////////////////////////////////////////////////////////////////////////

    private static void dibujamenu() {
        System.out.println("............................................................\n"
                + ".  1 Listar todos los departamentos. \n"
                + ".  2 Insertar un departamento (inserta 21, 'El 21','Talavera'.  \n"
                + ".  3 Consultar un departamento.\n"
                + ".  4 Modificar un departamento (cambia el nombre por 'NUEVO NOMBRE').\n"
                + ".  5 Borrar un departamento.\n"
                + ".  0 SALIR.\n"
                + "............................................................\n");

    } // fin dibujamenu

    public static Collection conectar() {

        try {
            Class cl = Class.forName(driver); //Cargar del driver 
            Database database = (Database) cl.newInstance(); //Instancia de la BD
            DatabaseManager.registerDatabase(database); //Registro del driver
            col = DatabaseManager.getCollection(URI, usu, usuPwd);
            return col;
        } catch (XMLDBException e) {
            System.out.println("Error al inicializar la BD eXist.");
            //e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Error en el driver.");
            //e.printStackTrace();
        } catch (InstantiationException e) {
            System.out.println("Error al instanciar la BD.");
            //e.printStackTrace();
        } catch (IllegalAccessException e) {
            System.out.println("Error al instanciar la BD.");
            //e.printStackTrace();
        }
        return null;
    }

    private static void listardep() {
        if (conectar() != null) {
            try {
                XPathQueryService servicio;
                servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                ResourceSet result = servicio.query("for $dep in /departamentos/DEP_ROW return $dep");
                // recorrer los datos del recurso.  
                ResourceIterator i;
                i = result.getIterator();
                if (!i.hasMoreResources()) {
                    System.out.println(" LA CONSULTA NO DEVUELVE NADA O ESTÁ MAL ESCRITA");
                }
                while (i.hasMoreResources()) {
                    Resource r = i.nextResource();
                    System.out.println("--------------------------------------------");
                    System.out.println((String) r.getContent());
                }
                col.close();
            } catch (XMLDBException e) {
                System.out.println(" ERROR AL CONSULTAR DOCUMENTO.");
                e.printStackTrace();
            }
        } else {
            System.out.println("Error en la conexión. Comprueba datos.");
        }

    }

    private static void insertardep(int dep, String dnombre, String loc) {
       
        String nuevodep = "<DEP_ROW><DEPT_NO>" + dep + "</DEPT_NO>"
                + "<DNOMBRE>" + dnombre + "</DNOMBRE><LOC>" + loc + "</LOC>"
                + "</DEP_ROW>";

        if (conectar() != null) {
            try {
                XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                System.out.printf("Inserto: %s \n", nuevodep);
                ResourceSet result = servicio.query("update insert " + nuevodep + " into /departamentos");
                col.close(); //borramos 
                System.out.println("Dep insertado.");
            } catch (Exception e) {
                System.out.println("Error al insertar empleado.");
                e.printStackTrace();
            }
        } else {
            System.out.println("Error en la conexión. Comprueba datos.");
        }
    }

    private static void consultardep(int dep) {
        if (comprobardep(dep)) {
            if (conectar() != null) {
                try {
                    XPathQueryService servicio;
                    servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                    ResourceSet result = servicio.query("/departamentos/DEP_ROW[DEPT_NO=" + dep + "]");
                    // recorrer los datos del recurso.  
                    ResourceIterator i;
                    i = result.getIterator();
                    if (!i.hasMoreResources()) {
                        System.out.println(" LA CONSULTA NO DEVUELVE NADA O ESTÁ MAL ESCRITA");
                    } else {
                        Resource r = i.nextResource();
                        System.out.println("--------------------------------------------");
                        System.out.println((String) r.getContent());
                    }
                    col.close();
                } catch (XMLDBException e) {
                    System.out.println(" ERROR AL CONSULTAR DOCUMENTO.");
                    e.printStackTrace();
                }
            } else {
                System.out.println("Error en la conexión. Comprueba datos.");
            }
        } else {
            System.out.println("El departamento NO EXISTE.");
        }

    }

    private static void modificardep(int dep) {

        if (comprobardep(dep)) {

            if (conectar() != null) {
                try {
                    System.out.printf("Actualizo el departamento: %s\n", dep);
                    XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                    ResourceSet result = servicio.query(
                            "update value /departamentos/DEP_ROW[DEPT_NO=" + dep + "]/DNOMBRE with data('NUEVO NOMBRE') ");

                    col.close();
                    System.out.println("Dep actualizado.");
                } catch (Exception e) {
                    System.out.println("Error al actualizar.");
                    e.printStackTrace();
                }
            } else {
                System.out.println("Error en la conexión. Comprueba datos.");
            }
        } else {
            System.out.println("El departamento NO EXISTE.");
        }
    }

    private static void borrarregistrodep(int dep) {
        if (comprobardep(dep)) {
            if (conectar() != null) {
                try {
                    System.out.printf("Borro el departamento: %s\n", dep);
                    XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                    ResourceSet result = servicio.query(
                            "update delete /departamentos/DEP_ROW[DEPT_NO=" + dep + "]");
                    col.close();
                    System.out.println("Dep borrado.");
                } catch (Exception e) {
                    System.out.println("Error al borrar.");
                    e.printStackTrace();
                }
            } else {
                System.out.println("Error en la conexión. Comprueba datos.");
            }
        } else {
            System.out.println("El departamento NO EXISTE.");
        }

    }

    private static boolean comprobardep(int dep) {
        //Devuelve true si el dep existe
        if (conectar() != null) {
            try {
                XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
                ResourceSet result = servicio.query(
                        "/departamentos/DEP_ROW[DEPT_NO=" + dep + "]");
                ResourceIterator i;
                i = result.getIterator();
                col.close();
                if (!i.hasMoreResources()) {
                    return false;
                } else {
                    return true;
                }
            } catch (Exception e) {
                System.out.println("Error al consultar.");
                // e.printStackTrace();
            }
        } else {
            System.out.println("Error en la conexión. Comprueba datos.");
        }

        return false;

    }// comprobardep
}// fin clase

