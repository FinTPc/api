package ro.allevo.fintpws.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang3.StringUtils;

import ro.allevo.fintpws.model.BaseEntity;
import ro.allevo.fintpws.model.StatusEntity;
import ro.allevo.fintpws.util.enums.EventsLogger;

public class ModelUtils {

	public static String ToBoolYN(String value) {
		if (null != value && value.toUpperCase().equals("Y"))
			return "Y";
		
		return "N";
	}
	
	public static StatusEntity getStatusEntity(BaseEntity entity, UriInfo uriInfo, String restOperation) throws ParseException {
		
		StatusEntity statusEntity = new StatusEntity();
		
	    // reflection ------  call method getClassEvent from class that extends BaseEntity
		statusEntity.setClasS(invokeObjectMethod(entity, "getClassEvent"));     
		
		statusEntity.setType(EventsLogger.configUIClassEvents.INFO.toString());
		
		Timestamp time = new Timestamp(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
											.parse(LocalDateTime.now().toString()).getTime());
		statusEntity.setEventdate(time);
		statusEntity.setInsertdate(time);

		//SELECT oid, * FROM fincfg.servicemaps - for events id is 2
		statusEntity.setService(BigDecimal.valueOf(2));

		// http://localhost:8085/fintp_ui/events?fp=header.menuReports&fp=header.menuEvents ---> localhost
		statusEntity.setMachine(uriInfo.getBaseUri().getHost().toString());
		
		// "insert"  entity (ex insert Bank) 		
		statusEntity.setMessage(restOperation + " " + StringUtils.substringBefore(entity.getClass().getSimpleName(), "Entity")); 
		
		// toString per entity
		statusEntity.setAdditionalinfo(invokeObjectMethod(entity, "toString")); 
		
		statusEntity.setCorrelationid("00000000-00000000-00000000");
		
		statusEntity.setSessionid(null);
		statusEntity.setInnerexception(null);

		return statusEntity;
	}
	
	public static String invokeObjectMethod(BaseEntity entity, String methodName) {
		
		Method[] allMethods = entity.getClass().getDeclaredMethods();
		Object result = null;
		
		for (Method classMethod : allMethods) {
			if(classMethod.getName().equalsIgnoreCase(methodName)) {
				try {
					result = classMethod.invoke(entity);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		return result != null? (String)result : "Error invoking entity method"  ;
	}
}