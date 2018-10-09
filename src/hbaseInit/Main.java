package hbaseInit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import org.w3c.dom.*;

import com.opencsv.CSVReader;


/**
* <h1>Hbase DataBase init function</h1>
* <p>The main class to initialize of Hbase configuration, column familly and also datasets<p>
* <p>
* <b>Note:</b> Think to make a specific class for all methods of init of database
*
* @author  Abdelkader Rhouati (arhouati)
* @version 1.0
* @since   2017-05-26
* 
*/

// TODO : refactoring of codes
// TODO : Change project encode to UTF-8 and fixe encoding for Tweeter french election
public class Main {

	static private Properties hbasePropertiesFile;
	static private Document schemaDataBase;
	
	static private HBaseAdmin admin;
	static private Configuration config;
	
	/**
	 * @param args
	 * @throws Exception 
	 * @throws ZooKeeperConnectionException 
	 * @throws MasterNotRunningException 
	 */
	public static void main(String[] args) throws Exception  {
		
		System.setProperty("hadoop.home.dir", "/");	
		
		try {
			 
			hbasePropertiesFile = loadPropetyFile("hbase.properties");
			schemaDataBase = loadXMLFile(  hbasePropertiesFile.getProperty("database.schema") );
		
			System.out.println("# init config :: Standalone HBase without HDFS ");
			config = HBaseConfiguration.create();
			config.set("hbase.zookeeper.property.clientPort", hbasePropertiesFile.getProperty("hbase.zookeeper.property.clientPort"));
	    
			Connection connection = ConnectionFactory.createConnection(config); 
			admin = (HBaseAdmin) connection.getAdmin();
			
			deleteAllTable();
			
			System.out.println("# init config :: create Table & Column ");
			initTableAndColumn(schemaDataBase);
			
			System.out.println("# init config :: initilize  Datasets");
			
			String dataFiles = hbasePropertiesFile.getProperty("database.data");
			String[] files = dataFiles.split(";");
			CSVReader DataFile;
			
			int i = 0;
			for( String file : files){
				System.out.println("# init config :: Import from file  " + file);
				DataFile = new CSVReader(new FileReader( file ));
				i++;
				initData(DataFile, ""+i );
			}
			
			/*String dataFilesPos = hbasePropertiesFile.getProperty("database.data.pos");
			String[] filesPos = dataFilesPos.split(";");
			CSVReader DataFilePos;
			
			int i = 0;
			for( String file : filesPos){
				System.out.println("# init config :: Import from file  " + file);
				DataFilePos = new CSVReader(new FileReader( file ), '|');
				i++;
				initDataPositive(DataFilePos, ""+i );
			}*/
			
			/*String dataFilesNeg = hbasePropertiesFile.getProperty("database.data.neg");
			String[] filesNeg = dataFilesNeg.split(";");
			CSVReader DataFileNeg;
			
			i = 0;
			for( String file : filesNeg){
				System.out.println("# init config :: Import from file  " + file);
				DataFileNeg = new CSVReader(new FileReader( file ), '|');
				i++;
				initDataNegative(DataFileNeg, ""+i );
			}*/
			
			admin.close();
			
		} catch (MasterNotRunningException e) {
			e.printStackTrace();
		} catch (ZooKeeperConnectionException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void deleteAllTable() throws IOException{

		@SuppressWarnings("deprecation")
		String[] tableNames = admin.getTableNames();
		String tableName;
		for (int temp = 0; temp < tableNames.length; temp++) {
			tableName = tableNames[temp];
			admin.disableTable( tableName );
			admin.deleteTable( tableName );	
		}
		
	}
	
	private static void initTableAndColumn( Document schema ) throws IOException{
		
		String namespaceLabel = schema.getDocumentElement().getAttribute("namespace");
		
		NamespaceDescriptor namespaceDescrip = NamespaceDescriptor.create( namespaceLabel ).build();
		
		if( ! Arrays.toString( admin.listNamespaceDescriptors() ).contains( namespaceLabel) )
			admin.createNamespace( namespaceDescrip );
		
		NodeList tables = schema.getElementsByTagName("table");
		
		for (int temp = 0; temp < tables.getLength(); temp++) {

			Node table = tables.item(temp);
					
			if (table.getNodeType() == Node.ELEMENT_NODE) {

				String tableName = table.getAttributes().getNamedItem("name").getTextContent();
		
				// disable and delete all Table
				if( admin.tableExists( TableName.valueOf(namespaceLabel, tableName) )){
					admin.disableTable( TableName.valueOf(namespaceLabel, tableName) );
					admin.deleteTable( TableName.valueOf(namespaceLabel, tableName) );
				}

				//creating table descriptor
			   HTableDescriptor tableDescr = new HTableDescriptor( TableName.valueOf(namespaceLabel, tableName) );

			   NodeList columns = table.getChildNodes();
			    
			   for (int i = 0; i < columns.getLength(); i++) {

				   Node column = columns.item(i);
					
					if (column.getNodeType() == Node.ELEMENT_NODE) {

						String columnName = column.getAttributes().getNamedItem("name").getTextContent();
						
						// Adding column families to table descriptor
						tableDescr.addFamily(new HColumnDescriptor( columnName ));
					
					}			    
			   }
			   
			   // create new table
			    admin.createTable(tableDescr);
			}
		}
	}
	
	private static void initDataPositive(CSVReader DataFile, String prefix) throws Exception {
		
        String[] line;
         
        // Instantiating HTable class for comment (tweets)
        @SuppressWarnings("deprecation")
        HTable table = new HTable(config, Bytes.toBytes("pok:data-positive-new"));

        line = DataFile.readNext();

        int i = 0;


        while ( (line = DataFile.readNext() ) != null ) {

  		   System.out.println(line.length);

     	   if( line.length == 2 ){
     		   
     		   System.out.println("text: " + line[1]);

     		   	// Instantiating Put class
     		   	// accepts a row name.
     		   	Put p = new Put(Bytes.toBytes("row_" +prefix + "_" + i)); 
	                p.addColumn(Bytes.toBytes("Comment"), Bytes.toBytes("Text"), line[1] != "" ? Bytes.toBytes(line[1]) : null);
	            	p.addColumn(Bytes.toBytes("Comment"), Bytes.toBytes("Lang"), Bytes.toBytes("fr") );

	                // Saving the put Instance to the HTable.
	                System.out.println("# init config :: initilize  Datasets :: Adding row_" +prefix + "_" + i);
	                table.put(p);
             
         	}
             
             i++;
       }
         
       // closing HTable
       table.close();
	}
	
	private static void initDataNegative(CSVReader DataFile, String prefix) throws Exception {
		
        String[] line;
         
        // Instantiating HTable class for comment (tweets)
        @SuppressWarnings("deprecation")
        HTable table = new HTable(config, Bytes.toBytes("pok:data-negative-new"));

        line = DataFile.readNext();

        int i = 0;


        while ( (line = DataFile.readNext() ) != null ) {

        	 if( line.length == 2 ){
       		   
       		   System.out.println("text: " + line[1]);

       		   	// Instantiating Put class
       		   	// accepts a row name.
       		   	Put p = new Put(Bytes.toBytes("row_" +prefix + "_" + i)); 
  	                p.addColumn(Bytes.toBytes("Comment"), Bytes.toBytes("Text"), line[1] != "" ? Bytes.toBytes(line[1]) : null);
	            	p.addColumn(Bytes.toBytes("Comment"), Bytes.toBytes("Lang"), Bytes.toBytes("fr") );

  	                // Saving the put Instance to the HTable.
  	                System.out.println("# init config :: initilize  Datasets :: Adding row_" +prefix + "_" + i);
  	                table.put(p);
               
           	}
             
             i++;
       }
         
       // closing HTable
       table.close();
	}
	
	private static void initData(CSVReader DataFile, String prefix) throws Exception {
		
           String[] line;
            
           // Instantiating HTable class for comment (tweets)
           @SuppressWarnings("deprecation")
           HTable table = new HTable(config, Bytes.toBytes("pok:election"));

           line = DataFile.readNext();

           int i = 0;


           while ( (line = DataFile.readNext() ) != null ) {

        	   if( line.length == 22 ){
        		   
        		   System.out.println("text: " + line[1]);

        		   	// Instantiating Put class
        		   	// accepts a row name.
        		   	Put p = new Put(Bytes.toBytes("row_" +prefix + "_" + i)); 
            	
	            	p.addColumn(Bytes.toBytes("Comment"), Bytes.toBytes("Lang"), line[1] != "" ? Bytes.toBytes(line[1]) : null );
	                p.addColumn(Bytes.toBytes("Comment"), Bytes.toBytes("Text"), line[17] != "" ? Bytes.toBytes(line[17]) : null);
	                p.addColumn(Bytes.toBytes("Comment"), Bytes.toBytes("Date"), line[18] != "" ? Bytes.toBytes(line[18]) : null);
	                p.addColumn(Bytes.toBytes("Comment"), Bytes.toBytes("mention_Macron"), line[11] != "" ? Bytes.toBytes(line[11]) : null);
	                p.addColumn(Bytes.toBytes("Comment"), Bytes.toBytes("mention_Fillon"), line[7] != "" ? Bytes.toBytes(line[7]) : null);
	                p.addColumn(Bytes.toBytes("Comment"), Bytes.toBytes("mention_LePen"), line[10] != "" ? Bytes.toBytes(line[10]) : null);
	                p.addColumn(Bytes.toBytes("User"), Bytes.toBytes("Identifiant"), line[19] != "" ? Bytes.toBytes(line[19]) : null);
            	
	                // Saving the put Instance to the HTable.
	                System.out.println("# init config :: initilize  Datasets :: Adding row_" +prefix + "_" + i);
	                table.put(p);
                
            	}
                
                i++;
          }
            
          // closing HTable
          table.close();
	}
	
	private static Properties loadPropetyFile( String file) throws IOException{
		
		Properties propetiesFile = new Properties();
		InputStream inStream = Main.class.getClassLoader().getResourceAsStream( file );
		if( inStream != null){
			propetiesFile.load(inStream);
		}else{
			throw new FileNotFoundException("propety file : " + file  + " not found in the classpath");
		};
		
		return propetiesFile;
		
	}
	
	private static Document loadXMLFile( String file) {
		
		DocumentBuilder dBuilder;
		Document doc = null;
		try {
			
			File fXmlFile = new File( file );
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return doc;
	}

}
