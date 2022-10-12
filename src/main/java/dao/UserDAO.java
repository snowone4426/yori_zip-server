package dao;

import javax.servlet.http.HttpSession;

import util.DBConnPool;

public class UserDAO extends DBConnPool {
	
	public UserDAO () {
		super();
	}
	
	// 로그인
	public Boolean login (HttpSession session,String email, String password) {
		Boolean result = false;
		String sql = "SELECT * FROM user_info WHERE email='"+ email +"' AND password='" + password + "'";
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			if (rs.next()) {
				UserObj user = new UserObj();
				
				user.setEmail(rs.getString("email"));
				user.setPassword(rs.getString("password"));
				user.setNickname(rs.getString("nickname"));
				user.setProfile(rs.getString("profile"));
				user.setType(rs.getString("type"));
				user.setUser_id(rs.getString("user_id"));
				user.setState(rs.getString("state"));
				user.setGender(rs.getString("gender"));
				session.setAttribute("user_info", user);
				
				result = true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
	        close();
	    }
		
		return result;
	}
	
	// 로그인 여부와 성별
	public String isLogin (HttpSession session) {
		String result = null;
		UserObj user = (UserObj) session.getAttribute("user_info");
		
		if (user != null) {
			result = user.getGender();
		}
		
		return result;
	}
	
	// 비밀번호 찾기
	public String passwordFind (String email, String answer) {
		String result = "";
		String sql = "SELECT * FROM user WHERE email='"+ email +"' AND answer='" + answer + "'";
		
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			if(rs.next()) {
				result = rs.getString("password");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return result;
	}
	
	//중복 여부
	public Boolean overlapCheck (String column, String value) {
		Boolean result = false;
		String sql = "SELECT * FROM user_info WHERE " + column + "='" + value + "'";
		
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			if (rs.next()) {
				result = true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return result;
	}
	
	// 회원가입
	public Boolean signUp (UserObj user, String question, String answer) {
		Boolean result = false;
		String sql = "INSERT INTO user_info(user_id,password,email,nickname,profile,gender,type,question,answer) VALUES ("
				+ "seq_user_id.NEXTVAL,?,?,?,?,?,?,?,?)";
		try {
			psmt = con.prepareStatement(sql);
			psmt.setString(1, user.getPassword());
			psmt.setString(2, user.getEmail());
			psmt.setString(3, user.getNickname());
			psmt.setString(4, user.getProfile());
			psmt.setString(5, user.getGender());
			psmt.setString(6,user.getType());
			psmt.setString(7, question);
			psmt.setString(8, answer);
			
			int num = psmt.executeUpdate();
			
			if (num > 0) {
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
	      close();
		}
		
		return result;
	}
	
	//유저 정보
	public UserObj userInfo (HttpSession session) {
		return (UserObj) session.getAttribute("user_info");
	}
	
	//유저 정보 수정
	public Boolean userInfoUpdate (UserObj u) {
		Boolean result = false;
		String sql = "UPDATE user SET "
				+ "password = '" + u.getPassword() +"', "
				+ "nickname = '" + u.getNickname() + "' "
				+ "WHERE user_id='" + u.getUser_id() + "'";
		
		try {
			stmt = con.createStatement();
			int num = stmt.executeUpdate(sql);
			
			if (num > 0) {
				result = true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return result;
	}
}
