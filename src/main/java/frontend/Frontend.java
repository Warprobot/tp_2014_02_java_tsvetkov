package frontend;

import AccountService.AccountService;
import PageGenerator.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;


/**
 * Created by warprobot on 29.05.14.
 */
public class Frontend extends HttpServlet {
    private static final DateFormat FORMATTER = new SimpleDateFormat("HH.mm.ss");
    private AtomicLong userIdGenerator = new AtomicLong();

    public static String getTime() {
        return FORMATTER.format(new Date());
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        Map <String, Object> responseData = new HashMap<>();
        response.setContentType("text/html;charset=utf-8");

        final String requestedURL = request.getPathInfo();

        switch (requestedURL) {
            case "/profile":
                HttpSession session = request.getSession();
                Long userId = (Long) session.getAttribute("userId");
                if (userId == null)
                    response.sendRedirect("/auth");
                responseData.put("serverTime", getTime());
                responseData.put("userID", userId);
                response.getWriter().println(PageGenerator.getPage("profile.tml", responseData));
                break;
            case "/registration":
                responseData.put("error", "");
                response.getWriter().println(PageGenerator.getPage("registration.tml", responseData));
                break;
            case "/auth":
                responseData.put("error", "");
                response.getWriter().println(PageGenerator.getPage("auth.tml", responseData));
                break;
            default:
                response.sendRedirect("/");
                break;
        }
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        final String requestedURL = request.getPathInfo();
        response.setContentType("text/html;charset=utf-8");
        Map<String, Object> responseData = new HashMap<>();

        switch (requestedURL) {
            case "/login":
                String login = request.getParameter("login");
                String password = request.getParameter("password");

                if (AccountService.checkUser(login, password)){
                    HttpSession session = request.getSession();
                    if(!session.isNew()) {
                        session.invalidate();
                        session = request.getSession();
                    }
                    Long userId = (Long) session.getAttribute("userId");
                    if (userId == null) {
                        userId = userIdGenerator.getAndIncrement();
                        session.setAttribute("userId", userId);
                    }
                    response.sendRedirect("/profile");
                } else {
                    responseData.put("error", "Wrong login/password!");
                    response.getWriter().println(PageGenerator.getPage("auth.tml", responseData));
                }
                break;
            case "/registration":
                login = request.getParameter("login");
                password = request.getParameter("password");
                if (login.isEmpty() || password.isEmpty()) {
                    responseData.put("error", "All fields must be filled!");
                    response.getWriter().println(PageGenerator.getPage("registration.tml", responseData));
                } else if (AccountService.isRegistered(login)) {
                    responseData.put("error", "Choose another login!");
                    response.getWriter().println(PageGenerator.getPage("registration.tml", responseData));
                } else {
                    AccountService.addUser(login, password);
                    response.sendRedirect("/");
                }
                break;
            default:
        }

    }


}