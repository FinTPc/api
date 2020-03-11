package ro.allevo.fintpuiws.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class DatasourceParser {
	@SuppressWarnings("unchecked")
	public static Map<String, String> parseAndFetch(String datasource, EntityManager entityManager) throws JsonParseException, JsonMappingException, IOException {
		
		if (null == datasource || datasource.trim().isEmpty())
			return null;
		
		Map<String, String> values = new HashMap<String, String>();
		
		if (datasource.startsWith("[")) {
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode nodes = mapper.readValue(datasource, ArrayNode.class);
			
			for (JsonNode node : nodes)
				if (node.isArray()) {
					ArrayNode keyvalue = (ArrayNode)node;
					values.put(keyvalue.get(0).asText(), keyvalue.get(1).asText());
				}
				else
					values.put(node.asText(), node.asText());
		}
		else {
			String[] source = datasource.split("\\.");
			
			String schema = null;
			String table = null, value = null;
			
			if (source.length == 2) {
				table = source[0];
				value = source[1];
			}
			else if (source.length == 3) {
				schema = source[0];
				table = source[1];
				value = source[2];
			}
			
			if (null != table) {
				String[] columns;
				
				if (value.startsWith("[") && value.endsWith("]")) {
					value = value.substring(1, value.length() - 1);
					columns = value.split(",");
				}
				else columns = new String[] {value, value};
				
				String query = "select " + columns[0] + ", " + columns[1];
				
				if (null != schema)
					table = schema + "." + table;
				
				query += " from " + table;
				
				Query q = entityManager.createNativeQuery(query);
				List<Object[]> result = q.getResultList();
				
				for (Object[] items : result)
					values.put(items[0].toString(), items[1].toString());
			}
		}
		
		return values;
	}
}
