package com.huotu.hotcms.admin.controller.common;


import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Servlet implementation class FileUploadServlet
 */
//上传文件被保存的路径
@MultipartConfig(
        location="E:\\eclipse\\workspace\\TestAceEditor\\FileUpload"
)
@WebServlet(urlPatterns={"/fileUpload"})

public class test extends HttpServlet {
    private static final long serialVersionUID = 1L;
    //private static final Log log = LogFactory.getLog(UploadFileAction.class);
    //private String fileNameExtractorRegex = "filename=\".+\"";



    //获得指定文件的内容
    private String getFileContent(String fileName){
        File file = new File(fileName);
        BufferedReader reader = null;
        String ans = "";
        try{
            reader = new BufferedReader(new FileReader(file));
            String tmpString = null;
            //一行一行的读取文件里面的内容
            while((tmpString = reader.readLine()) != null){
                ans += tmpString + "\n";//保存在ans里面
            }
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            if(reader != null)
            {
                try{
                    reader.close();
                }catch(IOException e1){
                    e1.printStackTrace();
                }
            }
        }
        //返回获得的文件内容
        return ans;
    }
    //获得上传文件的文件名
    private String getFilename(Part part){



        return "";
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        request.getRequestDispatcher("fileUpload.jsp").forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        //set the encoding
        request.setCharacterEncoding("UTF-8");

        //请求（form中为file类型的输入框的名字为filename）发送过来时，使用getPart()方法取得Part实现对象
        Part part = request.getPart("filename");
        String fileName = getFilename(part);


        if(fileName != null && !fileName.isEmpty()){
            part.write(fileName);
        }

        //从指定文件路径获得文件内容
        String filePath = "E:\\eclipse\\workspace\\TestAceEditor\\FileUpload" + "\\" + fileName;
        String fileContent = getFileContent(filePath);

        //write to browser
//        response.setContentType("text/html");
//        PrintWriter writer = response.getWriter();
//        writer.print("<br/>Uploaded file name: "+ fileName);
//        writer.print("<br/>Size: "+part.getSize());
//
//        String author = request.getParameter("author");
//        writer.print("<br/>Author: "+author);
        //设置属性，在jsp文件中可以使用EL获得fileContent对象里的内容
        request.setAttribute("fileContent", fileContent);
        request.getRequestDispatcher("fileUpload.jsp").forward(request, response);

    }

}