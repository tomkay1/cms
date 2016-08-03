package com.huotu.hotcms.servlet;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(
        name = "PreviewServlet",
        urlPatterns = {"/preview"}
)
public class PreviewServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        ServletOutputStream out = resp.getOutputStream();
        if (req.getParameter("browse") != null) {
            out.write("widget browse".getBytes());
        } else if (req.getParameter("editor") != null) {
            out.write("widget editor".getBytes());
        }
        out.flush();
        out.close();
    }

}
