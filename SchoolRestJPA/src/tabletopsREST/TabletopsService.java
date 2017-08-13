package tabletopsREST;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityTransaction;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import tabletopsDAO.CompanyDAO;
import tabletopsDAO.EM;
import tabletopsDAO.EventDAO;
import tabletopsPD.Client;
import tabletopsPD.Company;
import tabletopsPD.Event;
import tabletopsPD.EventTable;
import tabletopsPD.Guest;
import tabletopsPD.SeatingArrangement;
import tabletopsPD.Token;
import tabletopsPD.User;
import tabletopsUT.Log;
import tabletopsUT.Message;
import tabletopsDAO.EM;
import tabletopsDAO.EventDAO;
import tabletopsDAO.EventTableDAO;
import tabletopsDAO.SeatingArrangementDAO;
import tabletopsDAO.CompanyDAO;
import tabletopsPD.Token;

@Path("/tabletopservices")
public class TabletopsService {

	ArrayList<Message> messages = new ArrayList<Message>();
	Company company = (Company) (CompanyDAO.listCompany().get(0));
	Log log = new Log();

	// Events REST Services
	@Secured()
	@GET
	@Path("/events")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Event> getEvents(@MatrixParam("client") String idNumber, @MatrixParam("date") String date,
			@DefaultValue("0") @QueryParam("page") String page,
			@DefaultValue("100") @QueryParam("per_page") String perPage) {
		EM.getEM().refresh(company);
		if (idNumber != null && date != null) {
			return company.getEventsForClientAndDate(idNumber, date, Integer.parseInt(page), Integer.parseInt(perPage));
		} else if (idNumber != null && date == null) {
			return company.getEventsForClient(idNumber, Integer.parseInt(page), Integer.parseInt(perPage));
		} else if (idNumber == null && date != null) {
			return company.getEventsForDate(date, Integer.parseInt(page), Integer.parseInt(perPage));
		} else {
			return company.getAllEvents(Integer.parseInt(page), Integer.parseInt(perPage));
		}
	}

	@Secured()
	@GET
	@Path("/events/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Event getEvent(@PathParam("id") String id) {
		return company.findEventByIdNumber(id);
	}
	
	@Secured()
	@GET
	@Path("/events/tokens/{token}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Event> getEventsForUser(@PathParam("token") String token) {
		return company.getEventsForUser(token, 0, 100);
	}

	@Secured()
	@POST
	@Path("/events")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Message> addEvent(Event event, @Context final HttpServletResponse response) throws IOException {

		if (event == null) {

			response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
			try {
				response.flushBuffer();
			} catch (Exception e) {
			}
			messages.add(new Message("op002", "Fail Operation", ""));
			return messages;
		} else {

			ArrayList<Message> errMessages = event.validate();
			if (errMessages != null) {

				response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
				try {
					response.flushBuffer();
				} catch (Exception e) {
				}
				return errMessages;
			}
			EntityTransaction userTransaction = EM.getEM().getTransaction();
			userTransaction.begin();
			Boolean result = company.addEvent(event);
			userTransaction.commit();
			if (result) {
				messages.add(new Message("op001", "Success Operation", ""));
				return messages;
			}
			response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
			try {
				response.flushBuffer();
			} catch (Exception e) {
			}
			messages.add(new Message("op002", "Fail Operation", ""));
			return messages;
		}
	}

	@Secured()
	@PUT
	@Path("/events/{id}/generateSeatingArrangement")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Message> generateSeatingArrangement(@PathParam("id") String id,
			@Context final HttpServletResponse response) throws IOException {

		Event oldEvent = company.findEventByIdNumber(id);
		if (oldEvent == null) {

			response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
			try {
				response.flushBuffer();
			} catch (Exception e) {
			}
			messages.add(new Message("op002", "Fail Operation", ""));
			return messages;
		} else {
			Event event = EventDAO.findEventByIdNumber(id);
			ArrayList<Message> errMessages = event.validate();
			if (errMessages != null) {
				response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
				try {
					response.flushBuffer();
				} catch (Exception e) {
				}
				return errMessages;
			}
			
			//EM.getEM().getTransaction().begin();
			Boolean result = event.calculateSeatingArrangement(new BigDecimal(100));
			//System.out.println(event.bullshit().isValid());
			//EventTable et6;
			//When 13 has blacklist entries for 6, 13 can't move to 6
//			for(EventTable et : event.bullshit().getEventTables()){
//				if(et.getGuests().contains(event.bullshit().findGuestByNumber(6))){
//					et6 = et;
//					System.out.println(event.findGuestByGuestNumber(13).moveToTable(et6));
//					break;
//				}
//			}
			
			//System.out.println(event.bullshit().isValid());
			//EM.getEM().getTransaction().commit();
			EM.getEM().getTransaction().begin();
			SeatingArrangementDAO.addSeatingArrangement(event.bullshit());
			EM.getEM().getTransaction().commit();
			
			for(EventTable et : event.bullshit().getEventTables()){
				EM.getEM().getTransaction().begin();
				EventTableDAO.addEventTable(et);
				EM.getEM().getTransaction().commit();
			}
			EM.getEM().getTransaction().begin();
			result = result && oldEvent.update(event);
			
			EM.getEM().getTransaction().commit();
			
			if (result) {
				messages.add(new Message("op001", "Success Operation", ""));
				
				return messages;
			}
			response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
			try {
				response.flushBuffer();
			} catch (Exception e) {
			}
			messages.add(new Message("op002", "Fail Operation", ""));
			return messages;
		}
	}

	@Secured()
	@PUT
	@Path("/events/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Message> updatedEvent(Event event, @PathParam("id") String id,
			@Context final HttpServletResponse response) throws IOException {
		Event oldEvent = company.findEventByIdNumber(id);
		if (oldEvent == null) {
			response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
			try {
				response.flushBuffer();
			} catch (Exception e) {
			}
			messages.add(new Message("op002", "Fail Operation", ""));
			return messages;
		} else {
			ArrayList<Message> errMessages = event.validate();
			if (errMessages != null) {
				response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
				try {
					response.flushBuffer();
				} catch (Exception e) {
				}
				return errMessages;
			}
		}
		EntityTransaction userTransaction = EM.getEM().getTransaction();
		userTransaction.begin();
		Boolean result = oldEvent.update(event);
		userTransaction.commit();
		if (result) {
			messages.add(new Message("op001", "Success Operation", ""));
			return messages;
		}
		response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
		try {
			response.flushBuffer();
		} catch (Exception e) {
		}
		messages.add(new Message("op002", "Fail Operation", ""));
		return messages;
	}

	@Secured()
	@DELETE
	@Path("/events/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Message> deleteEvent(@PathParam("id") String id, @Context final HttpServletResponse response) {
		Event event = company.findEventByIdNumber(id);
		if (event == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			try {
				response.flushBuffer();
			} catch (Exception e) {
			}
			messages.add(new Message("op002", "Fail Operation", ""));
			return messages;
		}
		EntityTransaction userTransaction = EM.getEM().getTransaction();
		userTransaction.begin();
		Boolean result = event.delete();
		userTransaction.commit();
		if (result) {
			messages.add(new Message("op001", "Success Operation", ""));
			return messages;
		} else {
			messages.add(new Message("op002", "Fail Operation", ""));
			return messages;
		}
	}

	// Client REST Services
	@Secured()
	@GET
	@Path("/clients")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Client> getClients(@DefaultValue("0") @QueryParam("page") String page,
			@DefaultValue("100") @QueryParam("per_page") String perPage) {
		EM.getEM().refresh(company);
		return company.getAllClients(Integer.parseInt(page), Integer.parseInt(perPage));
	}

	@Secured()
	@GET
	@Path("/clients/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Client getClient(@PathParam("id") String id) {
		return company.findClientByIdNumber(id);
	}

	@Secured()
	@POST
	@Path("/clients")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Message> addClient(Client client, @Context final HttpServletResponse response) throws IOException {

		if (client == null) {

			response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
			try {
				response.flushBuffer();
			} catch (Exception e) {
			}
			messages.add(new Message("op002", "Fail Operation", ""));
			return messages;
		} else {

			ArrayList<Message> errMessages = client.validate();
			if (errMessages != null) {

				response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
				try {
					response.flushBuffer();
				} catch (Exception e) {
				}
				return errMessages;
			}
			EntityTransaction userTransaction = EM.getEM().getTransaction();
			userTransaction.begin();
			Boolean result = company.addClient(client);
			userTransaction.commit();
			if (result) {
				messages.add(new Message("op001", "Success Operation", ""));
				return messages;
			}
			response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
			try {
				response.flushBuffer();
			} catch (Exception e) {
			}
			messages.add(new Message("op002", "Fail Operation", ""));
			return messages;
		}
	}

	@Secured()
	@PUT
	@Path("/clients/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Message> updatedClient(Client client, @PathParam("id") String id,
			@Context final HttpServletResponse response) throws IOException {
		Client oldClient = company.findClientByIdNumber(id);
		if (oldClient == null) {
			response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
			try {
				response.flushBuffer();
			} catch (Exception e) {
			}
			messages.add(new Message("op002", "Fail Operation", ""));
			return messages;
		} else {
			ArrayList<Message> errMessages = client.validate();
			if (errMessages != null) {
				response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
				try {
					response.flushBuffer();
				} catch (Exception e) {
				}
				return errMessages;
			}
		}
		EntityTransaction userTransaction = EM.getEM().getTransaction();
		userTransaction.begin();
		Boolean result = oldClient.update(client);
		userTransaction.commit();
		if (result) {
			messages.add(new Message("op001", "Success Operation", ""));
			return messages;
		}
		response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
		try {
			response.flushBuffer();
		} catch (Exception e) {
		}
		messages.add(new Message("op002", "Fail Operation", ""));
		return messages;
	}

	@Secured()
	@DELETE
	@Path("/clients/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Message> deleteClient(@PathParam("id") String id, @Context final HttpServletResponse response) {
		Client client = company.findClientByIdNumber(id);
		if (client == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			try {
				response.flushBuffer();
			} catch (Exception e) {
			}
			messages.add(new Message("op002", "Fail Operation", ""));
			return messages;
		}
		EntityTransaction userTransaction = EM.getEM().getTransaction();
		userTransaction.begin();
		Boolean result = client.delete();
		userTransaction.commit();
		if (result) {
			messages.add(new Message("op001", "Success Operation", ""));
			return messages;
		} else {
			messages.add(new Message("op002", "Fail Operation", ""));
			return messages;
		}
	}

	
	// Guest REST Services
	@Secured()
	@GET
	@Path("/events/{id}/guests")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Guest> getGuestsForEvent(@PathParam("id") String id, @DefaultValue("0") @QueryParam("page") String page,
			@DefaultValue("100") @QueryParam("per_page") String perPage) {
		EM.getEM().refresh(company);

		return company.getGuestsForEvent(id, Integer.parseInt(page), Integer.parseInt(perPage));
	}

	@Secured()
	@GET
	@Path("/tables/{id}/guests")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Guest> getGuestsForTable(@PathParam("id") String id, @DefaultValue("0") @QueryParam("page") String page,
			@DefaultValue("100") @QueryParam("per_page") String perPage) {
		EM.getEM().refresh(company);
		return company.getGuestsForTable(id, Integer.parseInt(page), Integer.parseInt(perPage));
	}

	@Secured()
	@GET
	@Path("/guests/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Guest getGuest(@PathParam("id") String id) {
		return company.findGuestByIdNumber(id);
	}

	@Secured()
	@GET
	@Path("/guests/{id}/whitelist")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Guest> getGuestWhitelist(@PathParam("id") String id) {
		Guest guest = company.findGuestByIdNumber(id);
		return guest.getWhitelist();
	}

	@Secured()
	@GET
	@Path("/guests/{id}/blacklist")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Guest> getGuestBlacklist(@PathParam("id") String id) {
		Guest guest = company.findGuestByIdNumber(id);
		return guest.getBlacklist();
	}

	@Secured()
	@POST
	@Path("/events/{id}/guests")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Message> addGuest(@PathParam("id") String id, Guest guest,
			@Context final HttpServletResponse response) throws IOException {

		if (guest == null) {

			response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
			try {
				response.flushBuffer();
			} catch (Exception e) {
			}
			messages.add(new Message("op002", "Fail Operation", ""));
			return messages;
		} else {

			ArrayList<Message> errMessages = guest.validate();
			if (errMessages != null) {

				response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
				try {
					response.flushBuffer();
				} catch (Exception e) {
				}
				return errMessages;
			}
			EntityTransaction userTransaction = EM.getEM().getTransaction();
			userTransaction.begin();
			Boolean result = company.findEventByIdNumber(id).addToGuestList(guest);
			userTransaction.commit();
			if (result) {
				messages.add(new Message("op001", "Success Operation", ""));
				return messages;
			}
			response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
			try {
				response.flushBuffer();
			} catch (Exception e) {
			}
			messages.add(new Message("op002", "Fail Operation", ""));
			return messages;
		}
	}

	@Secured()
	@POST
	@Path("/events/{id}/importGuestList")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Message> importGuestList(@PathParam("id") String id, @Context final HttpServletResponse response)
			throws IOException {

		if (id == null) {

			response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
			try {
				response.flushBuffer();
			} catch (Exception e) {
			}
			messages.add(new Message("op002", "Fail Operation", ""));
			return messages;
		} else {
			Event event = EventDAO.findEventByIdNumber(id);
			String filePath = "C:\\Users\\jenni\\mars_workspace\\SEII2-5\\SeatingArrangementSampleData3.csv";
			Boolean result = event.importGuestList(filePath);
			if (!result) {
				messages.add(new Message("op001", "Error Importing Guest List", ""));
				return messages;
			}
			response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
			try {
				response.flushBuffer();
			} catch (Exception e) {
			}
			messages.add(new Message("op002", "Fail Operation", ""));
			return messages;
		}
	}

	@Secured()
	@PUT
	@Path("/guests/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Message> updatedGuest(Guest guest, @PathParam("id") String id,
			@Context final HttpServletResponse response) throws IOException {
		Guest oldGuest = company.findGuestByIdNumber(id);
		if (oldGuest == null) {
			response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
			try {
				response.flushBuffer();
			} catch (Exception e) {
			}
			messages.add(new Message("op002", "Fail Operation", ""));
			return messages;
		} else {
			ArrayList<Message> errMessages = guest.validate();
			if (errMessages != null) {
				response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
				try {
					response.flushBuffer();
				} catch (Exception e) {
				}
				return errMessages;
			}
		}
		EntityTransaction userTransaction = EM.getEM().getTransaction();
		userTransaction.begin();
		Boolean result = oldGuest.update(guest);
		userTransaction.commit();
		if (result) {
			messages.add(new Message("op001", "Success Operation", ""));
			return messages;
		}
		response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
		try {
			response.flushBuffer();
		} catch (Exception e) {
		}
		messages.add(new Message("op002", "Fail Operation", ""));
		return messages;
	}

	@Secured()
	@PUT
	@Path("/guests/{owner_id}/blacklist/{member_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Message> addToBlackList(@PathParam("owner_id") String ownerId,
			@PathParam("member_id") String memberId, @Context final HttpServletResponse response) throws IOException {
		Guest ownerGuest = company.findGuestByIdNumber(ownerId);
		Guest memberGuest = company.findGuestByIdNumber(memberId);
		if (ownerGuest == null || memberGuest == null) {
			response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
			try {
				response.flushBuffer();
			} catch (Exception e) {
			}
			messages.add(new Message("op002", "Fail Operation", ""));
			return messages;
		}

		EntityTransaction userTransaction = EM.getEM().getTransaction();
		userTransaction.begin();
		ownerGuest.addToBlackList(memberGuest);
		userTransaction.commit();
		messages.add(new Message("op001", "Success Operation", ""));
		return messages;
	}
	
	@Secured()
	@PUT
	@Path("/guests/{owner_id}/whitelist/{member_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Message> addToWhiteList(@PathParam("owner_id") String ownerId,
			@PathParam("member_id") String memberId, @Context final HttpServletResponse response) throws IOException {
		Guest ownerGuest = company.findGuestByIdNumber(ownerId);
		Guest memberGuest = company.findGuestByIdNumber(memberId);
		if (ownerGuest == null || memberGuest == null) {
			response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
			try {
				response.flushBuffer();
			} catch (Exception e) {
			}
			messages.add(new Message("op002", "Fail Operation", ""));
			return messages;
		}

		EntityTransaction userTransaction = EM.getEM().getTransaction();
		userTransaction.begin();
		ownerGuest.addToWhiteList(memberGuest);
		userTransaction.commit();
		messages.add(new Message("op001", "Success Operation", ""));
		return messages;
	}
	
	@Secured()
	@PUT
	@Path("/guests/{guest_id}/moveToTable/{table_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Message> moveGuest(@PathParam("guest_id") String guestId,
			@PathParam("table_id") String tableId, @Context final HttpServletResponse response) throws IOException {
		Guest guest = company.findGuestByIdNumber(guestId);
		EventTable eventTable = company.findEventTableByIdNumber(tableId);
		if (guest == null || eventTable == null) {
			response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
			try {
				response.flushBuffer();
			} catch (Exception e) {
			}
			messages.add(new Message("op002", "Fail Operation", ""));
			return messages;
		}
		EntityTransaction userTransaction = EM.getEM().getTransaction();
		userTransaction.begin();
		boolean result = guest.moveToTable(eventTable);
		userTransaction.commit();
		if(result == true) {
			messages.add(new Message("op001", "Success Operation", ""));
		}
		else {
			messages.add(new Message("op003", "Cannot Move Guest", ""));
//			response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
			response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
		}

		return messages;
	}

	@Secured()
	@DELETE
	@Path("/guests/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Message> deleteGuest(@PathParam("id") String id, @Context final HttpServletResponse response) {
		Guest guest = company.findGuestByIdNumber(id);
		if (guest == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			try {
				response.flushBuffer();
			} catch (Exception e) {
			}
			messages.add(new Message("op002", "Fail Operation", ""));
			return messages;
		}
		EntityTransaction userTransaction = EM.getEM().getTransaction();
		userTransaction.begin();
		Boolean result = guest.delete();
		userTransaction.commit();
		if (result) {
			messages.add(new Message("op001", "Success Operation", ""));
			return messages;
		} else {
			messages.add(new Message("op002", "Fail Operation", ""));
			return messages;
		}
	}
	
	@Secured()
	@DELETE
	@Path("/guests/{owner_id}/whitelist/{member_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Message> deleteFromWhiteList(@PathParam("owner_id") String ownerId,
			@PathParam("member_id") String memberId, @Context final HttpServletResponse response) throws IOException {
		Guest ownerGuest = company.findGuestByIdNumber(ownerId);
		Guest memberGuest = company.findGuestByIdNumber(memberId);
		if (ownerGuest == null || memberGuest == null) {
			response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
			try {
				response.flushBuffer();
			} catch (Exception e) {
			}
			messages.add(new Message("op002", "Fail Operation", ""));
			return messages;
		}

		EntityTransaction userTransaction = EM.getEM().getTransaction();
		userTransaction.begin();
		ownerGuest.removeFromWhiteList(memberGuest);
		userTransaction.commit();
		messages.add(new Message("op001", "Success Operation", ""));
		return messages;
	}
	
	@Secured()
	@DELETE
	@Path("/guests/{owner_id}/blacklist/{member_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Message> deleteFromBlackList(@PathParam("owner_id") String ownerId,
			@PathParam("member_id") String memberId, @Context final HttpServletResponse response) throws IOException {
		Guest ownerGuest = company.findGuestByIdNumber(ownerId);
		Guest memberGuest = company.findGuestByIdNumber(memberId);
		if (ownerGuest == null || memberGuest == null) {
			response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
			try {
				response.flushBuffer();
			} catch (Exception e) {
			}
			messages.add(new Message("op002", "Fail Operation", ""));
			return messages;
		}

		EntityTransaction userTransaction = EM.getEM().getTransaction();
		userTransaction.begin();
		ownerGuest.removeFromBlackList(memberGuest);
		userTransaction.commit();
		messages.add(new Message("op001", "Success Operation", ""));
		return messages;
	}


	// SeatingArrangement REST Services
	@Secured()
	@GET
	@Path("/seatingarrangement")
	@Produces(MediaType.APPLICATION_JSON)
	public List<SeatingArrangement> getSeatingArrangements(@DefaultValue("0") @QueryParam("page") String page,
			@DefaultValue("100") @QueryParam("per_page") String perPage) {
		EM.getEM().refresh(company);
		return company.getAllSeatingArrangements(Integer.parseInt(page), Integer.parseInt(perPage));
	}

	@Secured()
	@GET
	@Path("/events/{id}/tables")
	@Produces(MediaType.APPLICATION_JSON)
	public SeatingArrangement getSeatingArrangement(@PathParam("id") String id) {
		return company.findSeatingArrangementByIdNumber(id);
	}

	@Secured()
	@PUT
	@Path("/seatingarrangements/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Message> updatedSeatingArrangement(SeatingArrangement sa, @PathParam("id") String id,
			@Context final HttpServletResponse response) throws IOException {
		SeatingArrangement oldSeatingArrangement = company.findSeatingArrangementByIdNumber(id);
		if (oldSeatingArrangement == null) {
			response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
			try {
				response.flushBuffer();
			} catch (Exception e) {
			}
			messages.add(new Message("op002", "Fail Operation", ""));
			return messages;
		} else {
			ArrayList<Message> errMessages = sa.validate();
			if (errMessages != null) {
				response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
				try {
					response.flushBuffer();
				} catch (Exception e) {
				}
				return errMessages;
			}
		}
		EntityTransaction userTransaction = EM.getEM().getTransaction();
		userTransaction.begin();
		Boolean result = oldSeatingArrangement.update(sa);
		userTransaction.commit();
		if (result) {
			messages.add(new Message("op001", "Success Operation", ""));
			return messages;
		}
		response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
		try {
			response.flushBuffer();
		} catch (Exception e) {
		}
		messages.add(new Message("op002", "Fail Operation", ""));
		return messages;
	}

	@Secured()
	@DELETE
	@Path("/seatingarrangements/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Message> deleteSeatingArrangement(@PathParam("id") String id,
			@Context final HttpServletResponse response) {
		SeatingArrangement sa = company.findSeatingArrangementByIdNumber(id);
		if (sa == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			try {
				response.flushBuffer();
			} catch (Exception e) {
			}
			messages.add(new Message("op002", "Fail Operation", ""));
			return messages;
		}
		EntityTransaction userTransaction = EM.getEM().getTransaction();
		userTransaction.begin();
		Boolean result = sa.delete();
		userTransaction.commit();
		if (result) {
			messages.add(new Message("op001", "Success Operation", ""));
			return messages;
		} else {
			messages.add(new Message("op002", "Fail Operation", ""));
			return messages;
		}
	}

	// User REST Services
	@Secured()
	@GET
	@Path("/users")
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> getUsers(@DefaultValue("0") @QueryParam("page") String page,
			@DefaultValue("100") @QueryParam("per_page") String perPage) {
		EM.getEM().refresh(company);
		return company.getAllUsers(Integer.parseInt(page), Integer.parseInt(perPage));
	}

	@Secured()
	@GET
	@Path("/users/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public User getUser(@PathParam("id") String id) {
		return company.findUserByIdNumber(id);
	}

	@Secured()
	@GET
	@Path("/users/{id}/events")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Event> getEventsForUser(@PathParam("id") String id, @DefaultValue("0") @QueryParam("page") String page,
			@DefaultValue("100") @QueryParam("per_page") String perPage) {
		EM.getEM().refresh(company);
		return company.getEventsForUser(id, Integer.parseInt(page), Integer.parseInt(perPage));
	}

	@Secured({"admin"})
	@POST
	@Path("/users")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Message> addUser(User user, @Context final HttpServletResponse response) throws IOException {

		if (user == null) {

			response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
			try {
				response.flushBuffer();
			} catch (Exception e) {
			}
			messages.add(new Message("op002", "Fail Operation", ""));
			return messages;
		} else {

			ArrayList<Message> errMessages = user.validate();
			if (errMessages != null) {

				response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
				try {
					response.flushBuffer();
				} catch (Exception e) {
				}
				return errMessages;
			}
			EntityTransaction userTransaction = EM.getEM().getTransaction();
			userTransaction.begin();
			Boolean result = company.addUser(user);
			userTransaction.commit();
			if (result) {
				messages.add(new Message("op001", "Success Operation", ""));
				return messages;
			}
			response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
			try {
				response.flushBuffer();
			} catch (Exception e) {
			}
			messages.add(new Message("op002", "Fail Operation", ""));
			return messages;
		}
	}

	@Secured({"admin"})
	@PUT
	@Path("/users/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Message> updatedUser(User user, @PathParam("id") String id,
			@Context final HttpServletResponse response) throws IOException {
		User oldUser = company.findUserByIdNumber(id);
		if (oldUser == null) {
			response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
			try {
				response.flushBuffer();
			} catch (Exception e) {
			}
			messages.add(new Message("op002", "Fail Operation", ""));
			return messages;
		} else {
			ArrayList<Message> errMessages = user.validate();
			if (errMessages != null) {
				response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
				try {
					response.flushBuffer();
				} catch (Exception e) {
				}
				return errMessages;
			}
		}
		EntityTransaction userTransaction = EM.getEM().getTransaction();
		userTransaction.begin();
		Boolean result = oldUser.update(user);
		userTransaction.commit();
		if (result) {
			messages.add(new Message("op001", "Success Operation", ""));
			return messages;
		}
		response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
		try {
			response.flushBuffer();
		} catch (Exception e) {
		}
		messages.add(new Message("op002", "Fail Operation", ""));
		return messages;
	}

	@Secured({"admin"})
	@DELETE
	@Path("/users/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Message> deleteUser(@PathParam("id") String id, @Context final HttpServletResponse response) {
		User user = company.findUserByIdNumber(id);
		if (user == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			try {
				response.flushBuffer();
			} catch (Exception e) {
			}
			messages.add(new Message("op002", "Fail Operation", ""));
			return messages;
		}
		EntityTransaction userTransaction = EM.getEM().getTransaction();
		userTransaction.begin();
		Boolean result = user.delete();
		userTransaction.commit();
		if (result) {
			messages.add(new Message("op001", "Success Operation", ""));
			return messages;
		} else {
			messages.add(new Message("op002", "Fail Operation", ""));
			return messages;
		}
	}

	@Context
	SecurityContext securityContext;

	
	// ArrayList<Message> messages = new ArrayList<Message>();

	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response authenticateUser(Map<String, String> user) {

		System.out.println(user);
//		if (user == null) {
//
//			response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
//			try {
//				response.flushBuffer();
//			} catch (Exception e) {
//			}
//			messages.add(new Message("op002", "Fail Operation", ""));
//			return messages;
//		} else {

//			ArrayList<Message> errMessages = client.validate();
//			if (errMessages != null) {
//
//				response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
//				try {
//					response.flushBuffer();
//				} catch (Exception e) {
//				}
//				return errMessages;
//			}
//			EntityTransaction userTransaction = EM.getEM().getTransaction();
//			userTransaction.begin();
//			Boolean result = company.addClient(client);
//			userTransaction.commit();
//			if (result) {
//				messages.add(new Message("op001", "Success Operation", ""));
//				return messages;
//			}
//			response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
//			try {
//				response.flushBuffer();
//			} catch (Exception e) {
//			}
//			messages.add(new Message("op002", "Fail Operation", ""));
//			return messages;
//		}
		
		try {
			// Authenticate the user using the credentials provided
			authenticate(user.get("userName"), user.get("password"));

			// Issue a token for the user
			String token = issueToken(user.get("userName"));

			// Return the token on the response
			return Response.ok(token).build();

		} catch (Exception e) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

	private void authenticate(String username, String password) throws Exception {
		// Authenticate against a database, LDAP, file or whatever
		// Throw an Exception if the credentials are invalid
		User user = Company.findUserByUserName(username);
		if (user == null)
			throw new Exception();
		if (!user.authenticate(password))
			throw new Exception();
	}

	private String issueToken(String username) {
		// Issue a token (can be a random String persisted to a database or a
		// JWT token)
		// The issued token must be associated to a user
		// Return the issued token
		EntityTransaction userTransaction = EM.getEM().getTransaction();
		userTransaction.begin();
		Token token = new Token(Company.findUserByUserName(username));
		token.save();
		userTransaction.commit();
		return token.getToken();
	}
	
	@DELETE
	@Path("/logout/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Message> deleteToken(@PathParam("id") String id, @Context final HttpServletResponse response) {
		Token token = Token.findTokenByIdNumber(id);
		if (token == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			try {
				response.flushBuffer();
			} catch (Exception e) {
			}
			messages.add(new Message("op002", "Fail Operation", ""));
			return messages;
		}
		EntityTransaction userTransaction = EM.getEM().getTransaction();
		userTransaction.begin();
		Boolean result = token.delete();
		userTransaction.commit();
		if (result) {
			messages.add(new Message("op001", "Success Operation", ""));
			return messages;
		} else {
			messages.add(new Message("op002", "Fail Operation", ""));
			return messages;
		}
	}
	
	@Context ServletContext context;
	
	@POST
	@Path("/events/{id}/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile(
		@PathParam("id") String id,
		@FormDataParam("file") InputStream uploadedInputStream,
		@FormDataParam("file") FormDataContentDisposition fileDetail) {
		String realPath = context.getRealPath("/");
		
		String uploadFileLocation = realPath + "/uploads/";// + fileDetail.getFileName();
		new File(uploadFileLocation).mkdirs();	
		System.out.println(uploadFileLocation);
		
		// save it
		writeToFile(uploadedInputStream, uploadFileLocation + fileDetail.getFileName() );

		String output = "File uploaded to : " + uploadFileLocation + fileDetail.getFileName();
		
		Event event = EventDAO.findEventByIdNumber(id);
		Boolean result = event.importGuestList(uploadFileLocation + fileDetail.getFileName());
		
		if(result) {
			return Response.status(200).entity(output).build();
		}
		else {
			return Response.status(Status.NOT_ACCEPTABLE).build();
		}
	}

	// save uploaded file to new location
	private void writeToFile(InputStream uploadedInputStream,
		String uploadedFileLocation) {

		try {
			OutputStream out = new FileOutputStream(new File(
					uploadedFileLocation));
			int read = 0;
			byte[] bytes = new byte[1024];

			out = new FileOutputStream(new File(uploadedFileLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}
}
