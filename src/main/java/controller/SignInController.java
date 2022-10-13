package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.json.JSONArray;
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
	  response.setHeader("Access-Control-Allow-Methods","POST, GET, OPTIONS, DELETE");
      response.setHeader("Access-Control-Max-Age", "3600");
      response.setHeader("Access-Control-Allow-Headers", "Content-Type, x-requested-with");
      response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
	  response.setContentType("application/json");
      response.setCharacterEncoding("utf-8");
		if(request.getParameter("si") != null) {
		  switch (request.getParameter("si")) {
		    case "signin" :
		      signIn(request,response);
		      break;
		    case "islogin" :
		      isLogin(request,response);
		      break;
		    case "passwordsearch" : 
		      passwordSearch(request,response);
		    case "question" : 
		      question(request,response);
		    default : 
		      PrintWriter out = response.getWriter();
	          out.print("SIGNIN CONTROLLER");
		  }
		} else {
		  PrintWriter out = response.getWriter();
          out.print("SIGNIN CONTROLLER");
		}
	}
	
	private void signIn(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
	
	private void isLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      PrintWriter out = response.getWriter();
      HttpSession session = request.getSession();
      JSONObject json_obj = new JSONObject();
      UserDAO user = new UserDAO();
      String gender = null;
      try {
        if ((gender = user.isLogin(session)) != null) {
          json_obj.put("gender", gender);
          json_obj.put("success", true);
        } else {
          json_obj.put("success", false);
        }
        
        out.print(json_obj.toString());
        
      } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
	}
	
	private void passwordSearch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  request.setCharacterEncoding("utf-8");
      PrintWriter out = response.getWriter();
      JSONParser jsonparser = new JSONParser(request.getReader());
      JSONObject json_obj = new JSONObject();
      UserDAO userdao = new UserDAO();
      
      try {
        Map<String,Object> json_data = jsonparser.object();
        String email = (String) json_data.get("email");
        String answer = (String) json_data.get("answer");
        
        String password = userdao.passwordFind(email, answer);
        
        if (password != null) {
          json_obj.put("password", password);
          json_obj.put("success", true);
        } else {
          json_obj.put("success", false);
        }
        
        out.print(json_obj);
        
      } catch (ParseException | JSONException e) {
        e.printStackTrace();
      }
	}
	
	private void question(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  request.setCharacterEncoding("utf-8");
      PrintWriter out = response.getWriter();
      UserDAO userdao = new UserDAO();
      JSONArray json_arr = new JSONArray(userdao.question());
      
      out.print(json_arr.toString());
	}
	
}
