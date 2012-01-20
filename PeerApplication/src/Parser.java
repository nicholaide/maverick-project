
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
import java.util.List;
import org.yaml.snakeyaml.*;

public class Parser {

	/**
	 * @param args
	 * @throws FileNotFoundException
	 */
	
	public void parseYML() throws FileNotFoundException{
		
		 InputStream input = new FileInputStream(new File("src/res/sample.yml"));
		 Yaml yaml = new Yaml();
		 Map<String, Object> object = (Map<String, Object>) yaml.load(input);
		 List<Object> l =(List<Object>)object.get("Configuration");
		 System.out.println(object);
			for (int i = 0; i < l.size(); i++) {
				Map<String, Object> o = (Map<String, Object>)l.get(i);
				System.out.println(o.get("IP"));
			}
		
	}
	


}
