package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.json.JSONArray;
import org.json.JSONObject;

import dao.RecipeDAO;
import dao.RepleDAO;
import dao.UserDAO;
import dao.UserObj;


@WebServlet("/MyPageController")
public class MyPageController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public MyPageController() {
        super();
    }

	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  if(request.getParameter("mp") != null) {
        switch (request.getParameter("mp")) {
          case "mylist" : 
            getMyRecipeList(request,response);
            break;
          case "myreple" :
            getMyReple(request,response);
            break;
          case "myinfo" :
            getMyInfo(request,response);
          case "updatemyinfo" :
            getMyInfo(request,response);
          default : 
            PrintWriter out = response.getWriter();
            out.print("MYPAGE CONTROLLER");
        }
      } else {
        PrintWriter out = response.getWriter();
        out.print("MYPAGE CONTROLLER");
      }
	}
	
	@SuppressWarnings("unchecked")
  public void getRecipeList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  response.setContentType("application/json"); 
      response.setCharacterEncoding("utf-8");
      request.setCharacterEncoding("utf-8");
      PrintWriter out = response.getWriter();
      RecipeDAO recipe = new RecipeDAO();
      JSONParser json_parser = new JSONParser(request.getReader());
      List<Map<String,String>> recipe_list = null;
      Map<String,String> category = null;
      
      try {
        Map<String, Object> json_data = json_parser.object();
        String limit = (String) json_data.get("limit");
        String offset = (String) json_data.get("offset");
        
        if (json_data.get("category") != null ) {
          category = (Map<String, String>) json_data.get("category");
        }
        
        recipe_list = recipe.getRecipeList(Integer.parseInt(offset), Integer.parseInt(limit), category);
        System.out.println(recipe_list);
        
        JSONArray json_array = new JSONArray();
        
        for (Map<String,String> x :recipe_list) {
          JSONObject json_obj = new JSONObject(x);
          
          json_array.put(json_obj);
        }
        
        out.print(json_array.toString());

      } catch (ParseException e) {
        e.printStackTrace();
      }
      
  }
	
	public void getMyRecipeList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      response.setContentType("application/json"); 
      response.setCharacterEncoding("utf-8");
      request.setCharacterEncoding("utf-8");
      PrintWriter out = response.getWriter();
      RecipeDAO recipe = new RecipeDAO();
      JSONParser json_parser = new JSONParser(request.getReader());
      List<Map<String,Object>> recipe_list = null;
      
      try {
        Map<String, Object> json_data = json_parser.object();
        String limit = (String) json_data.get("limit");
        String offset = (String) json_data.get("offset");
        String user_id = (String) json_data.get("user_id");

        recipe_list = recipe.getMyRecipe(user_id,Integer.parseInt(offset), Integer.parseInt(limit));
        
        JSONArray json_array = new JSONArray();
        
        for (Map<String,Object> x :recipe_list) {
          JSONObject json_obj = new JSONObject(x);
          
          json_array.put(json_obj);
        }
        
        out.print(json_array.toString());

      } catch (ParseException e) {
        e.printStackTrace();
      }
      
  }
	
	public void getMyReple(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  response.setContentType("application/json"); 
      response.setCharacterEncoding("utf-8");
      request.setCharacterEncoding("utf-8");
      PrintWriter out = response.getWriter();
      RepleDAO reple = new RepleDAO();
      JSONParser json_parser = new JSONParser(request.getReader());
      List<Map<String,Object>> reple_list = null;
      
      try {
        Map<String, Object> json_data = json_parser.object();
        String limit = (String) json_data.get("limit");
        String offset = (String) json_data.get("offset");
        String user_id = (String) json_data.get("user_id");
        String recipe_id = (String) json_data.get("recipe_id");
        
        reple_list = reple.getReple(recipe_id, Integer.parseInt(offset), Integer.parseInt(limit), user_id);
        
        JSONArray json_array = new JSONArray();
        
        for (Map<String,Object> x :reple_list) {
          JSONObject json_obj = new JSONObject(x);
          
          json_array.put(json_obj);
        }
        
        
        out.print(json_array.toString());
      } catch (ParseException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      
	}
	
	public void getMyInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  response.setContentType("application/json"); 
      response.setCharacterEncoding("utf-8");
      request.setCharacterEncoding("utf-8");
      PrintWriter out = response.getWriter();
      HttpSession session = request.getSession();
      UserDAO user = new UserDAO();
      Map<String,Object> user_info = user.userInfo(session);
      JSONObject json_obj = new JSONObject(user_info);
      
      out.print(json_obj.toString());
      
	}
	
	public void setMyInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  response.setContentType("application/json"); 
      response.setCharacterEncoding("utf-8");
      request.setCharacterEncoding("utf-8");
      PrintWriter out = response.getWriter();
      UserDAO user = new UserDAO();
      UserObj user_info = new UserObj();
      JSONParser json_parser = new JSONParser(request.getReader());
      
      try {
        Map<String,Object> json_data = json_parser.object();
        user_info.setProfile((String) json_data.get("profile"));
        user_info.setPassword((String) json_data.get("password"));
        user_info.setNickname((String) json_data.get("nickname"));
        user_info.setUser_id((String) json_data.get("user_id"));
        
        out.print(user.userInfoUpdate(user_info));
      } catch (ParseException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
	}

}
