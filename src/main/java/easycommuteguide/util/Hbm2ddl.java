package easycommuteguide.util;

import java.util.HashMap;

import org.hibernate.cfg.Configuration;
import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

/**
 * According to Hibernate documentation, both Configuration and
 * Ejb3Configuration are deprecated and will be removed in Hibernate 5.
 * Hopefully by then SchemaExport can be initialized from persistence.xml
 * instead of hibernate.cfg.xml or hibernate.properties, and if so, we can
 * remove this class and run SchemaExport directly.
 */
@SuppressWarnings("deprecation")
public class Hbm2ddl {

	private static String outputFilename = "easycommuteguide.ddl";

	public static void main(String args[]) {
		
		System.out.println("Generating of " + outputFilename + " !");
		
	    System.out.print("Export DDL to " + outputFilename + " !");
	    
//	    Configuration cfg = (new Ejb3Configuration()).configure( "easycommuteguide", new HashMap<String, Object>()).getHibernateConfiguration();	    
	    Ejb3Configuration ejb3config = new Ejb3Configuration();
	    ejb3config.configure("easycommuteguide", new HashMap<String, Object>());
	    Configuration cfg = ejb3config.getHibernateConfiguration();
	    
	    SchemaExport schemaExport = new SchemaExport(cfg);
	    schemaExport.setOutputFile("./src/main/scripts/" + outputFilename)
            .setDelimiter( ";" )
            .setFormat( true )
            .setHaltOnError( true );

        // . output script to console (and file if outputFile is set): true
        // . export to database: false
        // . only drop the tables: false
        // . only create the tables: true
        schemaExport.execute(true, false, false, true);

        System.out.println(outputFilename + " generated successfully." );
	}
}
