package xyz.ganzuer;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=UTF-8");
        String action = req.getParameter("action");
        if(action.equals("login")){
            String password = req.getParameter("password");
            String name = req.getParameter("name");
            UserDao userDao = new UserDao();
            User user = userDao.find(name);
            if(user==null){
                req.setAttribute("error" , 101);
                RequestDispatcher disp =  req.getRequestDispatcher("./error.jsp");
                disp.forward(req,resp);
            }
            String rePassword = user.getPassword();
            if(password.equals(rePassword)){
                Cookie cookie = new Cookie("name",name);
                cookie.setMaxAge(12*60*60);
                resp.addCookie(cookie);
                resp.sendRedirect("/file?action=show");
            }else{
                req.setAttribute("error" , 101);
                RequestDispatcher disp =  req.getRequestDispatcher("./error.jsp");
                disp.forward(req,resp);
            }
        }
        else if(action.equals("leave")){
            Cookie[] cookies = req.getCookies();
            if(cookies!=null){
                for(Cookie cookie : cookies){
                    if(cookie.getName().equals("name")){
                        cookie.setMaxAge(0);
                        cookie.setValue(null);
                        resp.addCookie(cookie);
                    }
                }
            }
            resp.sendRedirect("/file?action=show");
        }else{
            resp.sendRedirect("/file?action=show");
        }
    }
}
