package vino.config;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Configuration {
    
	private static final Logger log = Logger.getLogger(Configuration.class);
	
    private static Configuration configuration;

    private String mode;
    private List<DatabaseConfiguration> database;
    private WineApiConfiguration wineApi;
        
    public static Configuration getInstance() {
        if (configuration == null) {        	
        	try {
        		configuration = new Configuration();
        		configuration.loadFromXMLFile();
        	}
        	catch (IOException e) {
        		log.error("Unable to read config.xml file", e);
        	} catch (JAXBException e) {
        		log.error("Unable to parse config.xml file", e);
			}
        }
        return configuration;
    }
    
    public String getMode() {
		return mode;
	}

	public DatabaseConfiguration getDatabase() {
		if (database != null) {
			for(DatabaseConfiguration each : database) {
				if (each.getMode().equalsIgnoreCase(mode)) return each;
			}			
		}
		return null;
	}
	
	public List<DatabaseConfiguration> getDatabases() {
		return database;
	}

	public WineApiConfiguration getWineApi() {
		return wineApi;
	}
	
	private Configuration() {}
    
    private void loadFromXMLFile() throws IOException, JAXBException {    	
    	String xml = null;
    	
    	try {
    		xml = Files.toString(new File("./WEB-INF/config.xml"), Charsets.UTF_8);
    	}
    	catch (IOException e) {
    		xml = Files.toString(new File("./web/WEB-INF/config.xml"), Charsets.UTF_8);
    	}
    	
    	JAXBContext jaxbContext = JAXBContext.newInstance(Configuration.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        configuration = (Configuration) unmarshaller.unmarshal(new StringReader(xml));
    }
    
    public String toXML() {
    	try {
	        JAXBContext jaxbContext = JAXBContext.newInstance(Configuration.class);
	    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        Marshaller marshaller = jaxbContext.createMarshaller();
	        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	        marshaller.marshal(configuration, baos);
	        return new String(baos.toByteArray());
    	}
    	catch (JAXBException e) {
    		return "Error converting POJO to XML";
    	}
    }
    
    private void generateXSD() throws JAXBException, IOException {
    	// this code was only helpful to produce a bare bones schema file. I had to add validations on top of it
    	JAXBContext jaxbContext = JAXBContext.newInstance(Configuration.class);
    	jaxbContext.generateSchema(new SchemaOutputResolver() {
    	    public Result createOutput(String namespaceURI, String suggestedFileName) throws IOException {
    	        File file = new File("./web/WEB-INF/config.xsd");
    	        StreamResult result = new StreamResult(file);
    	        result.setSystemId(file.toURI().toURL().toString());
    	        return result;
    	    }
    	});
    }
    
    public String toString() {
    	return toXML();
    }
    
    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)    
    public static class DatabaseConfiguration {
    	
    	private String mode;
    	private String driver;
    	private String url;
    	private String username;
    	private String password;
    	
    	@SuppressWarnings("unused")
		private DatabaseConfiguration() {}
    	
    	public DatabaseConfiguration(String mode, String driver, String url, String username, String password) {
    		this.mode = mode;
    		this.driver = driver;
    		this.url = url;
    		this.username = username;
    		this.password = password;
    	}

		public String getMode() {
			return mode;
		}

		public void setMode(String mode) {
			this.mode = mode;
		}
		
		public String getDriver() {
			return driver;
		}

		public void setDriver(String driver) {
			this.driver = driver;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)  
    public static class WineApiConfiguration {
    	
    	private String url;
    	private String key;
    	private long fetchSize = 400;
    	private long fetchOffset = 0;
    	
    	@SuppressWarnings("unused")
		private WineApiConfiguration() {}
    	
    	public WineApiConfiguration(String url, String key, long fetchSize, long fetchOffset) {
    		this.url = url;
    		this.key = key;
    		this.fetchSize = fetchSize;
    		this.fetchOffset = fetchOffset;
    	}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public long getFetchSize() {
			return fetchSize;
		}

		public void setFetchSize(long fetchSize) {
			this.fetchSize = fetchSize;
		}

		public long getFetchOffset() {
			return fetchOffset;
		}

		public void setFetchOffset(long fetchOffset) {
			this.fetchOffset = fetchOffset;
		}
    	
    }
    
}