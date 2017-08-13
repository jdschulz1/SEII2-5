package tabletopsDAO;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import tabletopsPD.Event;
import tabletopsPD.Token;
import tabletopsPD.User;

public class UserDAO {

	public static void saveUser(User user) {
		EM.getEM().persist(user);
	}

	public static void addUser(User user) {
		EM.getEM().persist(user);
	}

	public static List<User> listUser() {
		TypedQuery<User> query = EM.getEM().createQuery("SELECT user FROM user user", User.class);
		return query.getResultList();
	}

	public static User findUserByUserName(String username) {
		String qString = "SELECT user FROM user user  WHERE user.userName ='" + username + "'";
		Query query = EM.getEM().createQuery(qString);
		User user = (User) query.getSingleResult();
		return user;
	}

	public static User findUserByToken(String tokenStr) {
		String qString = "SELECT token FROM token token  WHERE token.token ='" + tokenStr + "'";
		Query query = EM.getEM().createQuery(qString);
		Token token = (Token) query.getSingleResult();
		User user = token.getUser();
		return user;
	}

	public static List<User> getAllUsersForEvent(Event event) {
		TypedQuery<User> query = EM.getEM().createQuery("SELECT user FROM user user", User.class);
		return query.getResultList();
	}

	public static User findUserById(long id) {
		User user = EM.getEM().find(User.class, new Long(id));
		return user;
	}

	public static User findUserByIdNumber(String idNumber) {
		String qString = "SELECT user FROM user user WHERE user.userId =" + idNumber;
		Query query = EM.getEM().createQuery(qString);
		User user = (User) query.getSingleResult();
		return user;
	}

	public static List<User> getAllUsers(int page, int pageSize) {
		TypedQuery<User> query = EM.getEM().createQuery("SELECT user FROM user user", User.class);
		return query.setFirstResult(page * pageSize).setMaxResults(pageSize).getResultList();
	}

	public static void removeUser(User user) {
		EM.getEM().remove(user);
	}
}
