package schoolREST;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import schoolDAO.EM;
import schoolDAO.SchoolDAO;
import schoolPD.School;
import schoolPD.Student;
import schoolUT.Log;
import schoolUT.Message;


	@Path("/studentservice")
	public class StudentService {
		
		ArrayList<Message> messages = new ArrayList<Message>();
		
		School school = (School) (SchoolDAO.listSchool().get(0));
		Log log = new Log();

	   @GET
	   @Path("/students")
	   @Produces(MediaType.APPLICATION_JSON)
	   public List<Student> getStudents(
		    @DefaultValue("0") @QueryParam("page") String page,
		    @DefaultValue("10") @QueryParam("per_page") String perPage){
	     EM.getEM().refresh(school);
		   return school.getAllStudents(Integer.parseInt(page),Integer.parseInt(perPage));
	   }	
	   
	   @GET
	   @Path("/students/{id}")
	   @Produces(MediaType.APPLICATION_JSON)
	   public Student getStudent(@PathParam("id") String id){
	      return school.findStudentByIdNumber(id);
	   }
	   
	   @POST
	   @Path("/students")
	   @Produces(MediaType.APPLICATION_JSON)
	   @Consumes(MediaType.APPLICATION_JSON)
	   public ArrayList<Message> addStudent(Student student,@Context final HttpServletResponse response) throws IOException{

		  if (student == null) {

			  response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
			  try {
			        response.flushBuffer();
			  }catch(Exception e){}
			  messages.add(new Message("op002","Fail Operation",""));
			  return messages;
		  }
		  else  {
			  
			  ArrayList<Message> errMessages = student.validate();
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
			  Boolean result = school.addStudent(student);
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
	   @Path("/students/{id}")
	   @Produces(MediaType.APPLICATION_JSON)
	   @Consumes(MediaType.APPLICATION_JSON)
	   public ArrayList<Message> udpatedStudent(Student student,@PathParam("id") String id, @Context final HttpServletResponse response) throws IOException{
		   Student oldStudent = school.findStudentByIdNumber(id);
		   if (oldStudent == null)
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
				  ArrayList<Message> errMessages = student.validate();
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
	      Boolean result = oldStudent.update(student);
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
	   @Path("/students/{id}")
	   @Produces(MediaType.APPLICATION_JSON)
	   public ArrayList<Message> deleteStudent(@PathParam("id") String id, @Context final HttpServletResponse response){
		  Student student = school.findStudentByIdNumber(id);
		  if (student == null) {
			  response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			    try {
			        response.flushBuffer();
			    }catch(Exception e){}
				messages.add(new Message("op002","Fail Operation",""));
				return messages;
		  }
		    EntityTransaction userTransaction = EM.getEM().getTransaction();
		    userTransaction.begin();
	      Boolean result = student.delete();
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

	   @OPTIONS
	   @Path("/students")
	   @Produces(MediaType.APPLICATION_JSON)
	   public String getSupportedOperations(){
	      return "{ {'POST' : { 'description' : 'add a student'}} {'GET' : {'description' : 'get a students'}}}";
	   }
}	
	   
