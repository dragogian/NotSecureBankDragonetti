package com.notsecurebank.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.notsecurebank.util.DBUtil;
import com.notsecurebank.util.ServletUtil;

public class SubscribeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LogManager.getLogger(SubscribeServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.info("doPost");

        String email = request.getParameter("txtEmail");
        if (email == null || !ServletUtil.isValidEmail(email)) {
            LOG.error("An error occured");
            response.sendRedirect("index.jsp");
            return;
        }

        String messageSubscribe = null;
        try {

            String registeredUser = DBUtil.addSubscription(email);
            if (registeredUser == null) {
                messageSubscribe = "Thank you. You are not registered yet. Please <a href='locations.jsp'>search</a> for the Branch Office closest to you and ask them for an account.";
            } else {
                messageSubscribe = "Hello <em>" + registeredUser + "</em>! Please <a href='login.jsp'>sign in</a> to use our advanced banking features.";
            }

        } catch (Exception e) {
            messageSubscribe = "Unexpected error.";
        }

        request.setAttribute("message_subscribe", messageSubscribe);
        RequestDispatcher dispatcher = request.getRequestDispatcher("subscribe.jsp");
        dispatcher.forward(request, response);

    }
}
