package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;

import dao.UserDAO;
import dao.UserObj;


@WebServlet("/SignUpController")
public class SignUpController extends HttpServlet {
  private static final long serialVersionUID = 1L;
  public SignUpController() {
    super();
  }
  
  public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    if(request.getParameter("su") != null) {
      switch (request.getParameter("su")) {
        case "signup" : 
          signUp(request,response);
          break;
        case "overlapcheck" :
          overlapCheck(request,response);
          break;
        default : 
          PrintWriter out = response.getWriter();
          out.print("SIGNIN CONTROLLER");
      }
    }
  }
  
  public void signUp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    response.setCharacterEncoding("utf-8");
    request.setCharacterEncoding("utf-8");
    PrintWriter out = response.getWriter();
    JSONParser json_parser = new JSONParser(request.getReader());
    UserObj user = new UserObj();
    UserDAO userdao = new UserDAO();
    
    try {
      Map<String, Object> json_data = json_parser.object();
      
      user.setPassword((String)json_data.get("password"));
      user.setEmail((String)json_data.get("email"));
      user.setNickname((String)json_data.get("nickname"));
      user.setProfile((String)json_data.get("profile"));
      user.setGender((String)json_data.get("gender"));
      user.setType((String)json_data.get("type"));
      
      out.print(userdao.signUp(user, (String)json_data.get("question"), (String)json_data.get("answer")));
    } catch (ParseException e) {
      e.printStackTrace();
    }
    
    ;
  }
  
  public void overlapCheck(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    response.setCharacterEncoding("utf-8");
    request.setCharacterEncoding("utf-8");
    PrintWriter out = response.getWriter();
    JSONParser json_parser = new JSONParser(request.getReader());
    UserDAO userdao = new UserDAO();

      try {
        Map<String, Object> json_data =  json_parser.object();
        
        out.print(userdao.overlapCheck((String)json_data.get("column"), (String)json_data.get("value")));
      } catch (ParseException e) {
        e.printStackTrace();
      }
  } 
  
  
}


