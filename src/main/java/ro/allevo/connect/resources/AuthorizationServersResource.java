package ro.allevo.connect.resources;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.http.client.utils.URIBuilder;
import org.codehaus.jettison.json.JSONException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import ro.allevo.connect.model.AuthorizationServersEntity;
import ro.allevo.connect.model.TokenBodyEntity;
import ro.allevo.connect.model.TokenEntity;
import ro.allevo.fintpws.resources.BaseResource;
import ro.allevo.fintpws.util.Roles;

public class AuthorizationServersResource extends  BaseResource<AuthorizationServersEntity> {

	public AuthorizationServersResource(UriInfo uriInfo, EntityManager entityManagerConnect, Long id) {
		super(AuthorizationServersEntity.class, uriInfo, entityManagerConnect, id);		
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed(Roles.API_INTERFACE_MODIFY)
	public AuthorizationServersEntity getAuthorizationsServers() {
		return get();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed(Roles.API_INTERFACE_MODIFY)
	public Response updateAuthorizationServer(AuthorizationServersEntity authorizationServersEntity) {
		return put(authorizationServersEntity);
	}

	@DELETE
	@RolesAllowed(Roles.API_INTERFACE_MODIFY)
	public Response deleteAuthorizationServer(AuthorizationServersEntity authorizationServersEntity) {
		authorizationServersEntity.setToken("");
		authorizationServersEntity.setExpirationDate(null);
		return put(authorizationServersEntity);
	}	
	
	@Path("token")
	@GET
	public String getResource(@QueryParam("code") String code, @QueryParam("sha") String sha) throws URISyntaxException, NoSuchAlgorithmException, JSONException, IOException {
		AuthorizationServersEntity entityConnect = get();//query.getSingleResult();
		
		HttpHeaders headers = new HttpHeaders();
	    headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED.toString());
	    headers.add("Accept", MediaType.APPLICATION_JSON.toString());

	    MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<String, String>();
	    requestBody.add("grant_type", "authorization_code");
	    requestBody.add("redirect_uri", "https://google.com");
	    requestBody.add("code", code);
	    requestBody.add("client_id", entityConnect.getClientId());
	    requestBody.add("client_secret", entityConnect.getClientSecret());
	    requestBody.add("code_verifier", "U1C0I6sHBtIHHFzZJmoHpeOfaiNoA5sIURzrnNspkfIhvmRKHfqXZiJcoxHcQvBturMupLBhu6Ym5x52EAFWbUsDg0a6hvhvUeNbf5KbhlJ4Wodm7iqVs6HMybYLapCm");
		
	    HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<MultiValueMap<String, String>>(requestBody, headers);
	  	RestTemplate restTemplate = new RestTemplate();
	  	ResponseEntity<TokenEntity> response = null;
	  	try {
			response = restTemplate.exchange(entityConnect.getAccessTokenUri(), HttpMethod.POST, formEntity, TokenEntity.class);
	  	}catch(Exception e) {
	  		
	  	}
	  	if(response ==null)
	  		return "false";	
	  	entityConnect.setToken(response.getBody().getAccess_token());
	  	updateAuthorizationServer(entityConnect);
	  	return  "true";
	}
	
	private ResponseEntity<TokenEntity> getRestTemplate(AuthorizationServersEntity entityConnect, String code, String sha) throws URISyntaxException, NoSuchAlgorithmException, JSONException {
		RestTemplate restTemplate = new RestTemplate();
		//restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(entityConnect.getClientId(), entityConnect.getClientSecret()));
		URIBuilder url = new URIBuilder(entityConnect.getAccessTokenUri());
				
//		url.addParameter("grant_type", entityConnect.getGrantType());
//		url.addParameter("redirect_uri", "https://google.com"/*entityConnect.getRedirectUri()*/);
//		url.addParameter("code", code);
//		url.addParameter("client_id", entityConnect.getClientId());
//		url.addParameter("client_secret", entityConnect.getClientSecret());
//		url.addParameter("code_verifier", sha);
		URI uri = url.build();
		HttpHeaders headers = getHeaders();
//		String auth = entityConnect.getClientId() + ":" + entityConnect.getClientSecret();
//		headers.add("Authorization",
//				"Basic " + Base64.getUrlEncoder().encodeToString(auth.getBytes(Charset.forName("UTF-8"))));
		
					TokenBodyEntity bodyToken = new TokenBodyEntity();
		bodyToken.setGrant_type(entityConnect.getGrantType());
		bodyToken.setRedirect_uri("https://google.com");
		bodyToken.setCode(code);
		bodyToken.setClient_id(entityConnect.getClientId());		
		bodyToken.setClient_secret(entityConnect.getClientSecret());
		bodyToken.setCode_verifier(sha);
						
		HttpEntity<TokenBodyEntity> request = new HttpEntity<>(bodyToken, headers);
		
		//ResponseEntity<TokenEntity> consentsResponse = restTemplate.exchange(entityConnect.getAccessTokenUri(), HttpMethod.POST, request,TokenEntity.class);
		
		return restTemplate.postForEntity(uri, request, TokenEntity.class);
		
		//return consentsResponse;
	}
	
	private static HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		//headers.add("Content-Type", MediaType.TEXT_PLAIN);
		headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED);
		//headers.add("Content-Type", MediaType.APPLICATION_JSON);
		headers.add("Accept", MediaType.APPLICATION_JSON);
		return headers;
	}

}
