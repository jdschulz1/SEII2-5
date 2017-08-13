package tabletopsDAO;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import tabletopsPD.Token;

public class TokenDAO {

	public static void saveToken(Token token) {
		EM.getEM().persist(token);
	}

	public static void addToken(Token token) {
		EM.getEM().persist(token);
	}

	public static List<Token> listToken() {
		TypedQuery<Token> query = EM.getEM().createQuery("SELECT token FROM token token", Token.class);
		return query.getResultList();
	}

	public static List<Token> getAllTokens() {
		TypedQuery<Token> query = EM.getEM().createQuery("SELECT token FROM token token", Token.class);
		return query.getResultList();
	}

	public static Token findTokenById(int id) {
		Token token = EM.getEM().find(Token.class, new Integer(id));
		return token;
	}

	public static Token findTokenByToken(String tokenString) {
		String qString = "SELECT token FROM token token  WHERE token.token ='" + tokenString + "'";
		Query query = EM.getEM().createQuery(qString);
		Token token = (Token) query.getSingleResult();
		return token;
	}

	public static List<Token> getAllTokens(int page, int pageSize) {
		TypedQuery<Token> query = EM.getEM().createQuery("SELECT token FROM token token", Token.class);
		return query.setFirstResult(page * pageSize).setMaxResults(pageSize).getResultList();

	}

	public static Token findTokenByIdNumber(String idNumber) {
		String qString = "SELECT token FROM token token WHERE token.tokenID =" + idNumber;
		Query query = EM.getEM().createQuery(qString);
		Token token = (Token) query.getSingleResult();
		return token;
	}

	public static void removeToken(Token token) {
		EM.getEM().remove(token);
	}
}
