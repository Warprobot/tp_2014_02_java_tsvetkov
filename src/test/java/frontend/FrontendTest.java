package frontend;

import accountServcie.AccountService;
import dbService.ProductionDataService;
import messageSystem.MessageSystem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import java.util.Random;

/**
 * Created by Andrey
 * 14.03.14.
 */
public class FrontendTest {
    final private static HttpServletRequest REQUEST = mock(HttpServletRequest.class);
    final private static HttpServletResponse RESPONSE = mock(HttpServletResponse.class);
    final private static HttpSession HTTPSESSION = mock(HttpSession.class);

    private static StringWriter stringWriter = new StringWriter();
    private static PrintWriter writer = new PrintWriter(stringWriter);

    final private static String MAIN_PAGE = "/index";
    final private static String USERID_PAGE = "/userid";
    final private static String POST_LOGIN = "/login";
    final private static String POST_REGISTER = "/registerUser";
    final private static String REG_STATUS = "/registrationStatus";
    final private static String SID = "uiykogFHywvczatrhjyukiygjIYUBjrvxtreGHcjvKYL";

    private static MessageSystem messageSystem = new MessageSystem();
    private static ProductionDataService productionDataService = new ProductionDataService();
    private static AccountService accountService = new AccountService(productionDataService, messageSystem);
    private static Frontend frontend = new Frontend(messageSystem);

    final private static int WAIT_TIME = 1000;

    @Before
    public void setUp() throws Exception
    {
        (new Thread(frontend)).start();
        (new Thread(accountService)).start();
    }

    private void prepare(String path) throws IOException
    {
        when(RESPONSE.getWriter()).thenReturn(writer);
        when(REQUEST.getSession()).thenReturn(HTTPSESSION);
        when(REQUEST.getPathInfo()).thenReturn(path);
        when(HTTPSESSION.getId()).thenReturn(SID);
    }

    private void waitAndCheckContent(String page, String check) throws IOException, ServletException
    {
        try {
            Thread.sleep(WAIT_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        prepare(page);
        frontend.doGet(REQUEST, RESPONSE);
        Assert.assertTrue(stringWriter.toString().contains(check));
    }

    @Test
    public void testDoGetUserIdPageSuccess() throws ServletException, IOException
    {
        prepare(USERID_PAGE);
        frontend.doGet(REQUEST, RESPONSE);
        Assert.assertTrue(stringWriter.toString().contains("<p>UserId: "));
    }

    @Test
    public void testDoGetMainPageSuccess() throws ServletException, IOException
    {
        prepare(MAIN_PAGE);
        frontend.doGet(REQUEST, RESPONSE);
        Assert.assertTrue(stringWriter.toString().contains("<a href=\"/register.html\">зарегистрироваться</a>"));
    }

    @Test
    public void testDoGetNonExistentPageFailure() throws ServletException, IOException
    {
        prepare("/This_Page_can_never_exist$%54%(*7ie7xfm0re89ncng8tc7");
        frontend.doGet(REQUEST, RESPONSE);
        Assert.assertTrue(stringWriter.toString().contains("404"));
    }

    @Test
    public void testDoPostLoginSuccess() throws ServletException, IOException
    {
        (new Thread(frontend)).start();
        (new Thread(accountService)).start();
        prepare(POST_LOGIN);
        when(REQUEST.getParameter("login")).thenReturn("1");
        when(REQUEST.getParameter("password")).thenReturn("1");

        frontend.doPost(REQUEST, RESPONSE);
        verify(RESPONSE, atLeastOnce()).sendRedirect("/userid");

        waitAndCheckContent(USERID_PAGE, "id");
    }

    @Test
    public void testDoPostLoginFailure() throws ServletException, IOException
    {
        prepare(POST_LOGIN);
        when(REQUEST.getParameter("login")).thenReturn("1");
        when(REQUEST.getParameter("password")).thenReturn("2");

        frontend.doPost(REQUEST, RESPONSE);
        verify(RESPONSE, atLeastOnce()).sendRedirect("/userid");

        waitAndCheckContent(USERID_PAGE, "Неверное имя пользоватея или пароль");
    }

    @Test
    public void testDoPostRegisterSuccess() throws ServletException, IOException
    {
        Random rnd = new Random();
        String name = "Random_test_user" + rnd.nextLong();

        prepare(POST_REGISTER);
        when(REQUEST.getParameter("login")).thenReturn(name);
        when(REQUEST.getParameter("password")).thenReturn(name);

        frontend.doPost(REQUEST, RESPONSE);
        verify(RESPONSE, atLeastOnce()).sendRedirect("/registrationStatus");

        waitAndCheckContent(REG_STATUS, "");
        verify(RESPONSE, atLeastOnce()).sendRedirect("/");
    }

    @Test
    public void testDoPostRegisterFailure() throws ServletException, IOException
    {
        prepare(POST_REGISTER);
        when(REQUEST.getParameter("login")).thenReturn("1");
        when(REQUEST.getParameter("password")).thenReturn("");

        frontend.doPost(REQUEST, RESPONSE);
        verify(RESPONSE, atLeastOnce()).sendRedirect("/registrationStatus");

        waitAndCheckContent(REG_STATUS, "Некорректные");
    }
}
