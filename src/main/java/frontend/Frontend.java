package frontend;

import accountServcie.AccountService;
import game.GameMechanics;
import game.Player;
import messageSystem.Address;
import messageSystem.Message;
import messageSystem.MessageSystem;
import messageSystem.Subscriber;
import messageSystem.messages.MsgCheckPassword;
import messageSystem.messages.MsgRegisterUser;
import messageSystem.messages.MsgStartGameSession;
import utils.TimeHelper;
import utils.resource.FrontendRes;
import utils.resource.ResourceFactory;
import PageGenerator.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Andrey on 21.02.14.
 * blah
 */
public class Frontend extends HttpServlet implements Runnable, Subscriber {

    private FrontendRes frontendRes = (FrontendRes) ResourceFactory.getInstance().get("data/frontend.xml");

    private final String REFRESH_PERIOD = frontendRes.getRefresh_period();
    private final int LOG_INTERVAL = frontendRes.getLog_interval();
    private final int DB_WAIT_TIME = frontendRes.getDb_wait_time();

    private final int GAME_CONNECTION_TIMEOUT = 5000;
    private Map<String, Player> players;

    private final MessageSystem messageSystem;
    private final Address address;

    private AtomicInteger handleCount;
    private Map<String, UserSession> sessions;

    public Frontend(MessageSystem ms)
    {
        handleCount = new AtomicInteger();
        sessions = new HashMap<>();
        address = new Address();
        messageSystem = ms;
        messageSystem.registerService(this);
        players = new HashMap<>();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        handleCount.incrementAndGet();
        createUniqueUserSessionIfNotExist(request.getSession());

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        String path = request.getPathInfo();
        switch (path) {
        case "/userid":
            handleGetUserId(request, response);
            break;
        case "/registrationStatus":
            handleGetRegistrationStatus(request, response);
            break;
        case "/game":
            handleGetGame(request, response);
            break;
        case "/index": case "/": case "":
            response.getWriter().println(PageGenerator.getPage("..\\static\\index.html", null));
            break;
        default:
            response.getWriter().println(PageGenerator.getPage("..\\static\\404.html", null));
            break;
        }
    }

    private void handleGetRegistrationStatus(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        Map<String, Object> pageVariables = new HashMap<>();

        Long userId = sessions.get(request.getSession().getId()).getUserId();
        pageVariables.put("refreshPeriod", REFRESH_PERIOD);
        pageVariables.put("serverTime", TimeHelper.getTimeString());
        if(userId == null) {
            pageVariables.put("status", "Ждите ответа от базы");
        } else if(userId == -1) {
            pageVariables.put("status", "Некорректные данные, или такой пользователь уже существует");
        } else {
            pageVariables.put("status", "OK");
            response.sendRedirect("/");
        }
        response.getWriter().println(PageGenerator.getPage("reg.tml", pageVariables));
    }

    private void handleGetGame(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("refreshPeriod", REFRESH_PERIOD);

        UserSession session = sessions.get(request.getSession().getId());

        String playerName = session.getName();
        if(!players.containsKey(playerName) && session.getUserId() > -1) {
            Player player = new Player(session.getSessionId(), session.getName(), session.getUserId());
            player.setLastAction(TimeHelper.getTime());
            players.put(playerName, player);
        } else {
            players.get(playerName).setLastAction(TimeHelper.getTime());
        }

        pageVariables.put("status", "Ожидание игроков");
        if ( players.size() >= 2) {
            Address gm_address = messageSystem.getAddressService().getService(GameMechanics.class);
            Message msg = new MsgStartGameSession(address, gm_address, players);
            messageSystem.sendMessage(msg);

            pageVariables.put("status", "В игре");
        }

        StringBuilder playersInGame = new StringBuilder();
        for(Player p : players.values()) {
            playersInGame.append(p.getName()).append("  ");
        }
        pageVariables.put("players", playersInGame.toString());

        response.getWriter().println(PageGenerator.getPage("game.tml", pageVariables));
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        handleCount.incrementAndGet();
        createUniqueUserSessionIfNotExist(request.getSession());

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        String path = request.getPathInfo();

        switch (path) {
        case "/login":
            handleLogin(request, response);
            break;
        case "/registerUser":
            handleRegistration(request, response);
            break;
        }
    }

    private void createUniqueUserSessionIfNotExist(HttpSession session)
    {
        if(! sessions.containsKey(session.getId())) {
            sessions.put(session.getId(), new UserSession(session.getId()));
        }
    }

    void handleLogin(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String login = request.getParameter("login");
        String pass = request.getParameter("password");
        String sessionId = request.getSession().getId();

        sessions.get(sessionId).setLastAction(TimeHelper.getTime());
        sessions.get(sessionId).setName(login);
        logOut(sessionId);

        Address as_address = messageSystem.getAddressService().getService(AccountService.class);
        Message msg = new MsgCheckPassword(address, as_address, login, pass, sessionId);
        messageSystem.sendMessage(msg);

        response.sendRedirect("/userid");
    }

    void handleRegistration(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String login = request.getParameter("login");
        String pass = request.getParameter("password");
        String sessionId = request.getSession().getId();

        logOut(sessionId);

        Address as_address = messageSystem.getAddressService().getService(AccountService.class);
        Message msg = new MsgRegisterUser(address, as_address, login, pass, sessionId);
        messageSystem.sendMessage(msg);

        response.sendRedirect("/registrationStatus");
    }

    void handleGetUserId(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        Map<String, Object> pageVariables = new HashMap<>();

        String sid = request.getSession().getId();
        Long userId = sessions.get(sid).getUserId();

        pageVariables.put("refreshPeriod", REFRESH_PERIOD);
        pageVariables.put("serverTime", TimeHelper.getTimeString());


        if(userId == null) {
            pageVariables.put("userId", "Ждите авторизации");
        } else if(userId == -1) {
            pageVariables.put("userId", "Неверное имя пользоватея или пароль");
        } else
            pageVariables.put("userId", userId);

        if((TimeHelper.getTime().getTime() - sessions.get(sid).getLastAction().getTime() > DB_WAIT_TIME) && userId==null)
            pageVariables.put("userId", "Нет ответа от базы");


        response.getWriter().println(PageGenerator.getPage("userid.tml", pageVariables));
    }

    private void logOut(String sid)
    {
        if(sessions.containsKey(sid))
            sessions.get(sid).setUserId(null);
    }

    public void run() {
        TimerTask task = new TimerTask() {
            public void run() {
                System.out.print("Handle Count: ");
                System.out.println(handleCount.get());
            }
        };
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(task, 0, LOG_INTERVAL);
        while(true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            messageSystem.execForSubscriber(this);
        }
    }

    public Address getAddress() {
        return address;
    }

    public MessageSystem getMessageSystem() {
        return messageSystem;
    }

    public void updateUserId(String sid, Long uid)
    {
        sessions.get(sid).setUserId(uid);
    }

    public void updateUserInfo(String sid, String name, Long uid)
    {
        sessions.get(sid).setUserId(uid);
        sessions.get(sid).setName(name);
    }

    public void updateStatus(String sid, Long uid)
    {
        sessions.get(sid).setUserId(uid);
    }

}
