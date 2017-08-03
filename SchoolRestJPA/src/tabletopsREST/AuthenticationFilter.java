package tabletopsREST;

import tabletopsPD.Company;
import tabletopsPD.Token;

import java.io.IOException;
import java.security.Principal;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;


import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
  String username ;
  
    public void setUsername(String username) {
      this.username = username;
    }
    
    public String getUsername() {
      return username;
    }
  
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
      
        // Get the HTTP Authorization header from the request
        String authorizationHeader = 
            requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        // Check if the HTTP Authorization header is present and formatted correctly 
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new NotAuthorizedException("Authorization header must be provided");
        }

        // Extract the token from the HTTP Authorization header
        String token = authorizationHeader.substring("Bearer ".length()).trim();

        try {

            // Validate the token
            setUsername(validateToken(token));

        } catch (Exception e) {
            requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED).build());
        }
        
        final SecurityContext currentSecurityContext = requestContext.getSecurityContext();
        requestContext.setSecurityContext(new SecurityContext() {

            @Override
            public Principal getUserPrincipal() {

                return new Principal() {

                    @Override
                    public String getName() {
                        return getUsername();
                    }
                };
            }

            @Override
            public boolean isUserInRole(String role) {
                return true;
            }

            @Override
            public boolean isSecure() {
                return currentSecurityContext.isSecure();
            }

            @Override
            public String getAuthenticationScheme() {
                return "Bearer";
            }
        });
    }

    private String validateToken(String stringToken) throws Exception {
          Token systemToken = Company.findToken(stringToken);
          if (systemToken == null || !systemToken.validate())  throw new Exception();
          return systemToken.getUser().getUserName();
        }
}

