package ro.allevo.fintpws.util;

import java.text.ParseException;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.RollbackException;
import javax.persistence.TransactionRequiredException;
import javax.ws.rs.core.UriInfo;

import ro.allevo.fintpws.model.BaseEntity;
import ro.allevo.fintpws.model.StatusEntity;
import ro.allevo.fintpws.util.enums.EventsLogger;

public class EntityManagerUtils {
	
	public static void persistEntityToDB(BaseEntity entity, EntityManager entityManager, UriInfo uriInfo) throws RollbackException, ParseException {
		
		StatusEntity statusEntity = null;
		EntityManager eml = null;
		
		try {

			eml = entityManager.getEntityManagerFactory().createEntityManager();
			eml.getTransaction().begin();

			statusEntity = ModelUtils.getStatusEntity(entity, uriInfo, "insert");

			eml.persist(entity);			
			eml.persist(statusEntity);
			
			eml.getTransaction().commit();
			eml.close();
			
		} catch (TransactionRequiredException|IllegalArgumentException|EntityExistsException e) {

			statusEntity.setType(EventsLogger.configUIClassEvents.ERROR.toString());
			
			if(eml.getTransaction().isActive()) {
				eml.persist(statusEntity);
				eml.getTransaction().commit();
				eml.close();
			}

		} catch (RollbackException re) {
			throw re;
		} finally {
			if(eml != null && eml.isOpen()) {
				eml.close();
			}
		}
	}
}
