package ro.allevo.fintpws.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

import javax.persistence.Column;

import org.apache.commons.lang3.reflect.FieldUtils;

import ro.allevo.fintpws.util.annotations.MergeEntity;

public abstract class BaseEntity implements Serializable{
	private static final long serialVersionUID = 1L;

	public void mergeWith(Object source) throws IllegalAccessException {
	    List<Field> columns = FieldUtils.getFieldsListWithAnnotation(this.getClass(), Column.class);
	    columns.addAll(FieldUtils.getFieldsListWithAnnotation(this.getClass(), MergeEntity.class));
	    
	    for(Field field  : columns) {
	    	Object value = FieldUtils.readField(field, source, true);
	    	
	    	//null values not sent in JSON
	    	if (null == field.getAnnotation(javax.persistence.Id.class)) {
		    	/*if (field.getType() == String.class) {
		    		//NULL string to null
		    		boolean isNull = String.valueOf(value).toUpperCase().equals("NULL");
		    		
		    		if (isNull)
		    			FieldUtils.writeField(field, this, null);
		    		else
		    			FieldUtils.writeField(field, this, value);
		    	}
		    	else*/
		    		FieldUtils.writeField(field, this, value);
	    	}
	    		
	    }
	}
}
