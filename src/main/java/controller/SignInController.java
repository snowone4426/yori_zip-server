package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.json.JSONException;
import org.json.JSONObject;
import dao.UserDAO;
import dao.UserObj;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/SignInController")
public class SignInController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public SignInController() {
        super();
    }


	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("si") != null) {
		  switch (request.getParameter("si")) {
		    case "signin" :
		      signIn(request,response);
		      break;
		    default : 
		      PrintWriter out = response.getWriter();
	          out.print("SIGNIN CONTROLLER");
		  }
		} else {
		  PrintWriter out = response.getWriter();
          out.print("SIGNIN CONTROLLER");
		}
	}
	
	public void signIn(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    
	  response.setContentType("application/json");
	  response.setCharacterEncoding("utf-8");
	  PrintWriter out = response.getWriter();
	  HttpSession session = request.getSession();
	  JSONParser json_parser = new JSONParser(request.getReader());
	  JSONObject json_obj = new JSONObject();
	  UserDAO user = new UserDAO();
	  
	  try {
	    Map<String, Object> json_data = json_parser.object();
        String email = (String) json_data.get("email");
        String password = (String) json_data.get("password");
      
        if(user.login(session, email, password)) {
          UserObj user_info = (UserObj) session.getAttribute("user_info");
          json_obj.put("success", true);
          json_obj.put("gender", user_info.getGender());
        } else {
          json_obj.put("success", false);
          json_obj.put("gender", "");
        }
        
      } catch (JSONException e) {
        e.printStackTrace();
      } catch (ParseException e) {
        e.printStackTrace();
      }
	  
	  out.print(json_obj);
    }
	
}
