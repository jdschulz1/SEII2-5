package tabletopsREST;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import tabletopsPD.Company;
import tabletopsPD.Client;
import tabletopsPD.Event;
import tabletopsPD.Guest;
import tabletopsPD.SeatingArrangement;
import tabletopsPD.User;
import tabletopsDAO.EM;
import tabletopsDAO.EventDAO;
import schoolUT.Log;
import schoolUT.Message;
import tabletopsDAO.CompanyDAO;

@Path("/tabletopservices")
public class TabletopsService {

	ArrayList<Message> messages = new ArrayList<Message>();
	Company company = (Company) (CompanyDAO.listCompany().get(0));
	Log log = new Log();
	
	//Events REST Services
	@GET
	@Path("/events")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Event> getEvents(
		@MatrixParam("client") String idNumber,
		@MatrixParam("date") String date,
	    @DefaultValue("0") @QueryParam("page") String page,
	    @DefaultValue("10") @QueryParam("per_page") String perPage){
			EM.getEM().refresh(company);
			if(idNumber != null && date != null) {
				return company.getEventsForClientAndDate(idNumber, date, Integer.parseInt(page),Integer.parseInt(perPage));
			}
			else if(idNumber != null && date == null) {
				return company.getEventsForClient(idNumber, Integer.parseInt(page),Integer.parseInt(perPage));
			}
			else if(idNumber == null && date != null) {
				return company.getEventsForDate(date, Integer.parseInt(page),Integer.parseInt(perPage));
			}
			else {
				return company.getAllEvents(Integer.parseInt(page),Integer.parseInt(perPage));
			}
	}	
	
	@GET
	@Path("/events/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Event getEvent(@PathParam("id") String id){
      return company.findEventByIdNumber(id);
	}
	
	@POST
	   @Path("/events")
	   @Produces(MediaType.APPLICATION_JSON)
	   @Consumes(MediaType.APPLICATION_JSON)
	   public ArrayList<Message> addEvent(Event event,@Context final HttpServletResponse response) throws IOException{

		  if (event == null) {

			  response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
			  try {
			        response.flushBuffer();
			  }catch(Exception e){}
			  messages.add(new Message("op002","Fail Operation",""));
			  return messages;
		  }
		  else  {
			  
			  ArrayList<Message> errMessages = event.validate();
			  if (errMessages != null) {
				  
				  response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
				  try {
					  response.flushBuffer();
				  }	
				  catch(Exception e){
					  }
				  return errMessages;
			  }
			  EntityTransaction userTransaction = EM.getEM().getTransaction();
		    userTransaction.begin();
			  Boolean result = company.addEvent(event);
			  userTransaction.commit();
			  if(result){
				  messages.add(new Message("op001","Success Operation",""));
				  return messages;
			  }
			  response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
			  try {
				  response.flushBuffer();
			  }	
			  catch(Exception e){
				  }
			  messages.add(new Message("op002","Fail Operation",""));
			  return messages;
		  }
	}
	@POST
	   @Path("/events/{id}/generateSeatingArrangement")
	   @Produces(MediaType.APPLICATION_JSON)
	   @Consumes(MediaType.APPLICATION_JSON)
	   public ArrayList<Message> generateSeatingArrangement(@PathParam("id") String id,@Context final HttpServletResponse response) throws IOException{

		  if (id == null) {

			  response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
			  try {
			        response.flushBuffer();
			  }catch(Exception e){}
			  messages.add(new Message("op002","Fail Operation",""));
			  return messages;
		  }
		  else  {
			  Event event = EventDAO.findEventByIdNumber(id);
			  boolean result = event.calculateSeatingArrangement(new BigDecimal(90));

			  if(result){
				  messages.add(new Message("op001","Success Operation",""));
				  return messages;
			  }
			  response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
			  try {
				  response.flushBuffer();
			  }	
			  catch(Exception e){
				  }
			  messages.add(new Message("op002","Fail Operation",""));
			  return messages;
		  }
	}
	@PUT
	   @Path("/events/{id}")
	   @Produces(MediaType.APPLICATION_JSON)
	   @Consumes(MediaType.APPLICATION_JSON)
	   public ArrayList<Message> updatedEvent(Event event,@PathParam("id") String id, @Context final HttpServletResponse response) throws IOException{
		   Event oldEvent = company.findEventByIdNumber(id);
		   if (oldEvent == null)
			  {
		   		  response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
		   		  try {
			        response.flushBuffer();
		   		  }catch(Exception e){}
				  messages.add(new Message("op002","Fail Operation",""));
				  return messages;
			  }
		  else
			  {
				  ArrayList<Message> errMessages = event.validate();
				  if (errMessages != null) {
					  response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
					  try {
						  response.flushBuffer();
					  }	
					  catch(Exception e){
						  }
					  return errMessages;
				  }
			  }
     EntityTransaction userTransaction = EM.getEM().getTransaction();
     userTransaction.begin();
	      Boolean result = oldEvent.update(event);
	      userTransaction.commit();
	      if(result){
			  messages.add(new Message("op001","Success Operation",""));
			  return messages;
	      }
	      response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
		    try {
		        response.flushBuffer();
		    }catch(Exception e){}
			  messages.add(new Message("op002","Fail Operation",""));
			  return messages;
	   }
	 @DELETE
	   @Path("/events/{id}")
	   @Produces(MediaType.APPLICATION_JSON)
	   public ArrayList<Message> deleteEvent(@PathParam("id") String id, @Context final HttpServletResponse response){
		  Event event = company.findEventByIdNumber(id);
		  if (event == null) {
			  response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			    try {
			        response.flushBuffer();
			    }catch(Exception e){}
				messages.add(new Message("op002","Fail Operation",""));
				return messages;
		  }
		    EntityTransaction userTransaction = EM.getEM().getTransaction();
		    userTransaction.begin();
	      Boolean result = event.delete();
	      userTransaction.commit();
	      if(result){
			  messages.add(new Message("op001","Success Operation",""));
			  return messages;
	      }
	      else {
			  messages.add(new Message("op002","Fail Operation",""));
			  return messages;
	      }
	   }
	 
	   	//Client REST Services
		@GET
		@Path("/clients")
		@Produces(MediaType.APPLICATION_JSON)
		public List<Client> getClients(
		    @DefaultValue("0") @QueryParam("page") String page,
		    @DefaultValue("10") @QueryParam("per_page") String perPage){
				EM.getEM().refresh(company);
				return company.getAllClients(Integer.parseInt(page),Integer.parseInt(perPage));
		}	
		
		@GET
		@Path("/clients/{id}")
		@Produces(MediaType.APPLICATION_JSON)
		public Client getClient(@PathParam("id") String id){
	      return company.findClientByIdNumber(id);
		}
		
		@POST
	   @Path("/clients")
	   @Produces(MediaType.APPLICATION_JSON)
	   @Consumes(MediaType.APPLICATION_JSON)
	   public ArrayList<Message> addClient(Client client,@Context final HttpServletResponse response) throws IOException{

		  if (client == null) {

			  response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
			  try {
			        response.flushBuffer();
			  }catch(Exception e){}
			  messages.add(new Message("op002","Fail Operation",""));
			  return messages;
		  }
		  else  {
			  
			  ArrayList<Message> errMessages = client.validate();
			  if (errMessages != null) {
				  
				  response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
				  try {
					  response.flushBuffer();
				  }	
				  catch(Exception e){
					  }
				  return errMessages;
			  }
			  EntityTransaction userTransaction = EM.getEM().getTransaction();
			  userTransaction.begin();
			  Boolean result = company.addClient(client);
			  userTransaction.commit();
			  if(result){
				  messages.add(new Message("op001","Success Operation",""));
				  return messages;
			  }
			  response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
			  try {
				  response.flushBuffer();
			  }	
			  catch(Exception e){
				  }
			  messages.add(new Message("op002","Fail Operation",""));
			  return messages;
		  }
		}
		@PUT
		   @Path("/clients/{id}")
		   @Produces(MediaType.APPLICATION_JSON)
		   @Consumes(MediaType.APPLICATION_JSON)
		   public ArrayList<Message> updatedClient(Client client,@PathParam("id") String id, @Context final HttpServletResponse response) throws IOException{
			   Client oldClient = company.findClientByIdNumber(id);
			   if (oldClient == null)
				  {
			   		  response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
			   		  try {
				        response.flushBuffer();
			   		  }catch(Exception e){}
					  messages.add(new Message("op002","Fail Operation",""));
					  return messages;
				  }
			  else
				  {
					  ArrayList<Message> errMessages = client.validate();
					  if (errMessages != null) {
						  response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
						  try {
							  response.flushBuffer();
						  }	
						  catch(Exception e){
							  }
						  return errMessages;
					  }
				  }
	     EntityTransaction userTransaction = EM.getEM().getTransaction();
	     userTransaction.begin();
		      Boolean result = oldClient.update(client);
		      userTransaction.commit();
		      if(result){
				  messages.add(new Message("op001","Success Operation",""));
				  return messages;
		      }
		      response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
			    try {
			        response.flushBuffer();
			    }catch(Exception e){}
				  messages.add(new Message("op002","Fail Operation",""));
				  return messages;
		   }
		 @DELETE
		   @Path("/clients/{id}")
		   @Produces(MediaType.APPLICATION_JSON)
		   public ArrayList<Message> deleteClient(@PathParam("id") String id, @Context final HttpServletResponse response){
			  Client client = company.findClientByIdNumber(id);
			  if (client == null) {
				  response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				    try {
				        response.flushBuffer();
				    }catch(Exception e){}
					messages.add(new Message("op002","Fail Operation",""));
					return messages;
			  }
			    EntityTransaction userTransaction = EM.getEM().getTransaction();
			    userTransaction.begin();
		      Boolean result = client.delete();
		      userTransaction.commit();
		      if(result){
				  messages.add(new Message("op001","Success Operation",""));
				  return messages;
		      }
		      else {
				  messages.add(new Message("op002","Fail Operation",""));
				  return messages;
		      }
		   }

//		   @OPTIONS
//		   @Path("/clients")
//		   @Produces(MediaType.APPLICATION_JSON)
//		   public String getSupportedOperations(){
//		      return "{ {'POST' : { 'description' : 'add a client'}} {'GET' : {'description' : 'get a client'}}}";
//		   }
//	   
		 //Guest REST Services		
			@GET
			@Path("/events/{id}/guests")
			@Produces(MediaType.APPLICATION_JSON)
			public List<Guest> getGuestsForEvent(
				@PathParam("id") String id,
			    @DefaultValue("0") @QueryParam("page") String page,
			    @DefaultValue("10") @QueryParam("per_page") String perPage){
					EM.getEM().refresh(company);
					
					return company.getGuestsForEvent(id, Integer.parseInt(page),Integer.parseInt(perPage));
			}
			
			@GET
			@Path("/tables/{id}/guests")
			@Produces(MediaType.APPLICATION_JSON)
			public List<Guest> getGuestsForTable(
				@PathParam("id") String id,
			    @DefaultValue("0") @QueryParam("page") String page,
			    @DefaultValue("10") @QueryParam("per_page") String perPage){
					EM.getEM().refresh(company);
					return company.getGuestsForTable(id, Integer.parseInt(page),Integer.parseInt(perPage));
			}
			
			@GET
			@Path("/guests/{id}")
			@Produces(MediaType.APPLICATION_JSON)
			public Guest getGuest(@PathParam("id") String id){
		      return company.findGuestByIdNumber(id);
			}
			
			@POST
			   @Path("/events/{id}/guests")
			   @Produces(MediaType.APPLICATION_JSON)
			   @Consumes(MediaType.APPLICATION_JSON)
			   public ArrayList<Message> addGuest(@PathParam("id") String id, Guest guest,@Context final HttpServletResponse response) throws IOException{

				  if (guest == null) {

					  response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
					  try {
					        response.flushBuffer();
					  }catch(Exception e){}
					  messages.add(new Message("op002","Fail Operation",""));
					  return messages;
				  }
				  else  {
					  
					  ArrayList<Message> errMessages = guest.validate();
					  if (errMessages != null) {
						  
						  response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
						  try {
							  response.flushBuffer();
						  }	
						  catch(Exception e){
							  }
						  return errMessages;
					  }
					  EntityTransaction userTransaction = EM.getEM().getTransaction();
					  userTransaction.begin();
					  Boolean result = company.findEventByIdNumber(id).addToGuestList(guest);
					  userTransaction.commit();
					  if(result){
						  messages.add(new Message("op001","Success Operation",""));
						  return messages;
					  }
					  response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
					  try {
						  response.flushBuffer();
					  }	
					  catch(Exception e){
						  }
					  messages.add(new Message("op002","Fail Operation",""));
					  return messages;
				  }
			}
			@PUT
			   @Path("/guests/{id}")
			   @Produces(MediaType.APPLICATION_JSON)
			   @Consumes(MediaType.APPLICATION_JSON)
			   public ArrayList<Message> updatedGuest(Guest guest,@PathParam("id") String id, @Context final HttpServletResponse response) throws IOException{
				   Guest oldGuest = company.findGuestByIdNumber(id);
				   if (oldGuest == null)
					  {
				   		  response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
				   		  try {
					        response.flushBuffer();
				   		  }catch(Exception e){}
						  messages.add(new Message("op002","Fail Operation",""));
						  return messages;
					  }
				  else
					  {
						  ArrayList<Message> errMessages = guest.validate();
						  if (errMessages != null) {
							  response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
							  try {
								  response.flushBuffer();
							  }	
							  catch(Exception e){
								  }
							  return errMessages;
						  }
					  }
		     EntityTransaction userTransaction = EM.getEM().getTransaction();
		     userTransaction.begin();
			      Boolean result = oldGuest.update(guest);
			      userTransaction.commit();
			      if(result){
					  messages.add(new Message("op001","Success Operation",""));
					  return messages;
			      }
			      response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
				    try {
				        response.flushBuffer();
				    }catch(Exception e){}
					  messages.add(new Message("op002","Fail Operation",""));
					  return messages;
			   }
			 @DELETE
			   @Path("/guests/{id}")
			   @Produces(MediaType.APPLICATION_JSON)
			   public ArrayList<Message> deleteGuest(@PathParam("id") String id, @Context final HttpServletResponse response){
				 Guest guest = company.findGuestByIdNumber(id);
				  if (guest == null) {
					  response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					    try {
					        response.flushBuffer();
					    }catch(Exception e){}
						messages.add(new Message("op002","Fail Operation",""));
						return messages;
				  }
				    EntityTransaction userTransaction = EM.getEM().getTransaction();
				    userTransaction.begin();
			      Boolean result = guest.delete();
			      userTransaction.commit();
			      if(result){
					  messages.add(new Message("op001","Success Operation",""));
					  return messages;
			      }
			      else {
					  messages.add(new Message("op002","Fail Operation",""));
					  return messages;
			      }
			   }
//
////			   @OPTIONS
////			   @Path("/guests")
////			   @Produces(MediaType.APPLICATION_JSON)
////			   public String getSupportedOperations(){
////			      return "{ {'POST' : { 'description' : 'add a guest'}} {'GET' : {'description' : 'get a guest'}}}";
////			   }
//			 
			//SeatingArrangement REST Services
				@GET
				@Path("/seatingarrangement")
				@Produces(MediaType.APPLICATION_JSON)
				public List<SeatingArrangement> getSeatingArrangements(
				    @DefaultValue("0") @QueryParam("page") String page,
				    @DefaultValue("10") @QueryParam("per_page") String perPage){
						EM.getEM().refresh(company);
						return company.getAllSeatingArrangements(Integer.parseInt(page),Integer.parseInt(perPage));
				}	
				
				@GET
				@Path("/seatingarrangements/{id}")
				@Produces(MediaType.APPLICATION_JSON)
				public SeatingArrangement getSeatingArrangement(@PathParam("id") String id){
			      return company.findSeatingArrangementByIdNumber(id);
				}
				
//				@POST
//				   @Path("events/{id}/seatingarrangements")
//				   @Produces(MediaType.APPLICATION_JSON)
//				   @Consumes(MediaType.APPLICATION_JSON)
//				   public ArrayList<Message> addSeatingArrangement(@PathParam("id") String id, SeatingArrangement sa,@Context final HttpServletResponse response) throws IOException{
//
//					  if (sa == null) {
//
//						  response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
//						  try {
//						        response.flushBuffer();
//						  }catch(Exception e){}
//						  messages.add(new Message("op002","Fail Operation",""));
//						  return messages;
//					  }
//					  else  {
//						  
//						  ArrayList<Message> errMessages = sa.validate();
//						  if (errMessages != null) {
//							  
//							  response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
//							  try {
//								  response.flushBuffer();
//							  }	
//							  catch(Exception e){
//								  }
//							  return errMessages;
//						  }
//						  EntityTransaction userTransaction = EM.getEM().getTransaction();
//						  userTransaction.begin();
//						  Boolean result = company.findEventByIdNumber(id)
//						  userTransaction.commit();
//						  if(result){
//							  messages.add(new Message("op001","Success Operation",""));
//							  return messages;
//						  }
//						  response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
//						  try {
//							  response.flushBuffer();
//						  }	
//						  catch(Exception e){
//							  }
//						  messages.add(new Message("op002","Fail Operation",""));
//						  return messages;
//					  }
//				}
				@PUT
				   @Path("/seatingarrangements/{id}")
				   @Produces(MediaType.APPLICATION_JSON)
				   @Consumes(MediaType.APPLICATION_JSON)
				   public ArrayList<Message> updatedSeatingArrangement(SeatingArrangement sa,@PathParam("id") String id, @Context final HttpServletResponse response) throws IOException{
					   SeatingArrangement oldSeatingArrangement = company.findSeatingArrangementByIdNumber(id);
					   if (oldSeatingArrangement == null)
						  {
					   		  response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
					   		  try {
						        response.flushBuffer();
					   		  }catch(Exception e){}
							  messages.add(new Message("op002","Fail Operation",""));
							  return messages;
						  }
					  else
						  {
							  ArrayList<Message> errMessages = sa.validate();
							  if (errMessages != null) {
								  response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
								  try {
									  response.flushBuffer();
								  }	
								  catch(Exception e){
									  }
								  return errMessages;
							  }
						  }
			     EntityTransaction userTransaction = EM.getEM().getTransaction();
			     userTransaction.begin();
				      Boolean result = oldSeatingArrangement.update(sa);
				      userTransaction.commit();
				      if(result){
						  messages.add(new Message("op001","Success Operation",""));
						  return messages;
				      }
				      response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
					    try {
					        response.flushBuffer();
					    }catch(Exception e){}
						  messages.add(new Message("op002","Fail Operation",""));
						  return messages;
				   }
				 @DELETE
				   @Path("/seatingarrangements/{id}")
				   @Produces(MediaType.APPLICATION_JSON)
				   public ArrayList<Message> deleteSeatingArrangement(@PathParam("id") String id, @Context final HttpServletResponse response){
					 SeatingArrangement sa = company.findSeatingArrangementByIdNumber(id);
					  if (sa == null) {
						  response.setStatus(HttpServletResponse.SC_NOT_FOUND);
						    try {
						        response.flushBuffer();
						    }catch(Exception e){}
							messages.add(new Message("op002","Fail Operation",""));
							return messages;
					  }
					    EntityTransaction userTransaction = EM.getEM().getTransaction();
					    userTransaction.begin();
				      Boolean result = sa.delete();
				      userTransaction.commit();
				      if(result){
						  messages.add(new Message("op001","Success Operation",""));
						  return messages;
				      }
				      else {
						  messages.add(new Message("op002","Fail Operation",""));
						  return messages;
				      }
				   }
			 
			//User REST Services
		 	@GET
		 	@Path("/users")
			@Produces(MediaType.APPLICATION_JSON)
			public List<User> getUsers(
			    @DefaultValue("0") @QueryParam("page") String page,
			    @DefaultValue("10") @QueryParam("per_page") String perPage){
					EM.getEM().refresh(company);
					return company.getAllUsers(Integer.parseInt(page),Integer.parseInt(perPage));
		 	}	
				
			@GET
			@Path("/users/{id}")
			@Produces(MediaType.APPLICATION_JSON)
			public User getUser(@PathParam("id") String id){
				return company.findUserByIdNumber(id);
			}
			
			@GET
			@Path("/users/{id}/events")
			@Produces(MediaType.APPLICATION_JSON)
			public List<Event> getEventsForUser(
				@PathParam("id") String id,
			    @DefaultValue("0") @QueryParam("page") String page,
			    @DefaultValue("10") @QueryParam("per_page") String perPage){
					EM.getEM().refresh(company);
					return company.getEventsForUser(id, Integer.parseInt(page),Integer.parseInt(perPage));
			}
				
				@POST
				   @Path("/users")
				   @Produces(MediaType.APPLICATION_JSON)
				   @Consumes(MediaType.APPLICATION_JSON)
				public ArrayList<Message> addUser(User user,@Context final HttpServletResponse response) throws IOException{

					  if (user == null) {

						  response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
						  try {
						        response.flushBuffer();
						  }catch(Exception e){}
						  messages.add(new Message("op002","Fail Operation",""));
						  return messages;
					  }
					  else  {
						  
						  ArrayList<Message> errMessages = user.validate();
						  if (errMessages != null) {
							  
							  response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
							  try {
								  response.flushBuffer();
							  }	
							  catch(Exception e){
								  }
							  return errMessages;
						  }
						  EntityTransaction userTransaction = EM.getEM().getTransaction();
						  userTransaction.begin();
						  Boolean result = company.addUser(user);
						  userTransaction.commit();
						  if(result){
							  messages.add(new Message("op001","Success Operation",""));
							  return messages;
						  }
						  response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
						  try {
							  response.flushBuffer();
						  }	
						  catch(Exception e){
							  }
						  messages.add(new Message("op002","Fail Operation",""));
						  return messages;
					  }
				}
				@PUT
				   @Path("/users/{id}")
				   @Produces(MediaType.APPLICATION_JSON)
				   @Consumes(MediaType.APPLICATION_JSON)
				   public ArrayList<Message> updatedUser(User user,@PathParam("id") String id, @Context final HttpServletResponse response) throws IOException{
					   User oldUser = company.findUserByIdNumber(id);
					   if (oldUser == null)
						  {
					   		  response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
					   		  try {
						        response.flushBuffer();
					   		  }catch(Exception e){}
							  messages.add(new Message("op002","Fail Operation",""));
							  return messages;
						  }
					  else
						  {
							  ArrayList<Message> errMessages = user.validate();
							  if (errMessages != null) {
								  response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
								  try {
									  response.flushBuffer();
								  }	
								  catch(Exception e){
									  }
								  return errMessages;
							  }
						  }
			     EntityTransaction userTransaction = EM.getEM().getTransaction();
			     userTransaction.begin();
				      Boolean result = oldUser.update(user);
				      userTransaction.commit();
				      if(result){
						  messages.add(new Message("op001","Success Operation",""));
						  return messages;
				      }
				      response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
					    try {
					        response.flushBuffer();
					    }catch(Exception e){}
						  messages.add(new Message("op002","Fail Operation",""));
						  return messages;
				   }
				 @DELETE
				   @Path("/users/{id}")
				   @Produces(MediaType.APPLICATION_JSON)
				   public ArrayList<Message> deleteUser(@PathParam("id") String id, @Context final HttpServletResponse response){
					 User user = company.findUserByIdNumber(id);
					  if (user == null) {
						  response.setStatus(HttpServletResponse.SC_NOT_FOUND);
						    try {
						        response.flushBuffer();
						    }catch(Exception e){}
							messages.add(new Message("op002","Fail Operation",""));
							return messages;
					  }
					    EntityTransaction userTransaction = EM.getEM().getTransaction();
					    userTransaction.begin();
				      Boolean result = user.delete();
				      userTransaction.commit();
				      if(result){
						  messages.add(new Message("op001","Success Operation",""));
						  return messages;
				      }
				      else {
						  messages.add(new Message("op002","Fail Operation",""));
						  return messages;
				      }
				   }
}
