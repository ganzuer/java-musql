package xyz.ganzuer;

import com.baidu.aip.ocr.AipOcr;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@WebServlet(name = "FileServlet", value = "/file")
public class FileServlet extends HttpServlet{
    public static final String APP_ID = "25642758";
    public static final String API_KEY = "lRapLOg4DbxkwRMPTnI8jNbc";
    public static final String SECRET_KEY = "4eNh94x5GLqUXZIkoEKgG8uKLOq1l3Dw";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=UTF-8");
        String action=req.getParameter("action");
        String ojbPath="";
        boolean online=false;
        String savePath = this.getServletContext().getRealPath("/WEB-INF/upload");
        File folder = new File(savePath);
        if (!folder.exists() && !folder.isDirectory()) {
            folder.mkdir();
        }
        Cookie[] cookies = req.getCookies();
        if(cookies!=null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("name")){
                    String name=cookie.getValue();
                    UserDao userDao=new UserDao();
                    User user = userDao.find(name);
                    if(name.equals(user.getName())){
                        online=true;
                    }else{
                        online=false;
                    }
                }
            }
        }else {
            online=false;
        }
        if (action.equals("upload")&&online){
            try{
                DiskFileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upload = new ServletFileUpload(factory);
                upload.setHeaderEncoding("UTF-8");
                if(!ServletFileUpload.isMultipartContent(req)){
                    return;
                }
                List<FileItem> list = upload.parseRequest(req);
                for(FileItem item : list){
                    if(item.isFormField()){
                        String name = item.getFieldName();
                        String value = item.getString("UTF-8");
                        System.out.println(name + "=" + value);
                    }else {
                        String filename = item.getName();
                        if(filename==null || filename.trim().equals("")){
                            continue;
                        }
                        filename = filename.substring(filename.lastIndexOf("\\")+1);
                        InputStream in = item.getInputStream();
                        ojbPath=savePath + "/" + filename;
                        ojbPath=ojbPath.replaceAll("\\\\", "\\\\\\\\");
                        FileOutputStream out = new FileOutputStream(savePath + "/" + filename);
                        byte buffer[] = new byte[1024];
                        int len = 0;
                        while((len=in.read(buffer))>0){
                            out.write(buffer, 0, len);
                        }
                        in.close();
                        out.close();
                        item.delete();
                        FileDao fileDao=new FileDao();
                        File objFile=new File(ojbPath);
                        xyz.ganzuer.File file=new xyz.ganzuer.File();
                        file.setFileName(filename);
                        file.setFilePath(ojbPath);
                        file.setUploadTime(new Date());
                        file.setFileSize(objFile.length());
                        fileDao.insert(file);
                        resp.sendRedirect("/file?action=show");
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        else if(action.equals("show")) {
            FileDao fileDao=new FileDao();
            ArrayList<xyz.ganzuer.File> files=fileDao.findAll();
            req.setAttribute("online" , !online);
            req.setAttribute("files" , files);
            RequestDispatcher disp =  req.getRequestDispatcher("/index.jsp");
            disp.forward(req,resp);
        }
        else if(action.equals("delete")&&online){
            FileDao fileDao = new FileDao();
            String id=req.getParameter("id");
            fileDao.delete(id);
            resp.sendRedirect("/file?action=show");
        }
        else if(action.equals("download")){
            FileDao filedao = new FileDao();
            String id=req.getParameter("id");
            xyz.ganzuer.File objFile=filedao.find(id);
            String fileName=objFile.getFileName();
            String filePath=objFile.getFilePath();
            try {
                String path=filePath;
                File file = new File(path);
                String filename = fileName;
                InputStream fis = new BufferedInputStream(new FileInputStream(path));
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                fis.close();
                resp.reset();
                resp.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
                resp.addHeader("Content-Length", "" + file.length());
                OutputStream toClient = new BufferedOutputStream(resp.getOutputStream());
                resp.setContentType("application/octet-stream");
                //resp.setCharacterEncoding("UTF-8");
                toClient.write(buffer);
                toClient.flush();
                toClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(action.equals("catch")){
            try{
                DiskFileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upload = new ServletFileUpload(factory);
                upload.setHeaderEncoding("UTF-8");
                if(!ServletFileUpload.isMultipartContent(req)){
                    return;
                }
                List<FileItem> list = upload.parseRequest(req);
                for(FileItem item : list){
                    if(item.isFormField()){
                        String name = item.getFieldName();
                        String value = item.getString("UTF-8");
                        System.out.println(name + "=" + value);
                    }else {
                        String filename = item.getName();
                        if(filename==null || filename.trim().equals("")){
                            continue;
                        }
                        filename = filename.substring(filename.lastIndexOf("\\")+1);
                        InputStream in = item.getInputStream();
                        ojbPath=savePath + "/" + filename;
                        ojbPath=ojbPath.replaceAll("\\\\", "\\\\\\\\");
                        FileOutputStream out = new FileOutputStream(savePath + "/" + filename);
                        byte buffer[] = new byte[1024];
                        int len = 0;
                        while((len=in.read(buffer))>0){
                            out.write(buffer, 0, len);
                        }
                        in.close();
                        out.close();
                        item.delete();

                        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
                        client.setConnectionTimeoutInMillis(2000);
                        client.setSocketTimeoutInMillis(60000);
                        System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");
                        JSONObject res = client.basicGeneral(ojbPath, new HashMap<String, String>());

                        List<Object> list00=new ArrayList<Object>();
                        List<Object> list01=new ArrayList<Object>();
                        list00.add(res.get("words_result"));
                        list01.add(list00.get(0));

                        String mess=list01.toString();
                        mess=mess.substring(12,mess.length()-4);
                        mess=mess.replace("\"},{\"words\":\""," ");

                        String[] arr1 = mess.split(" ");

                        File deleteObj = new File(ojbPath);
                        deleteObj.delete();

                        req.setAttribute("mess" , mess);
                        RequestDispatcher disp =  req.getRequestDispatcher("/file?action=show");
                        disp.forward(req,resp);
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        else {
            req.setAttribute("error" , 102);
            RequestDispatcher disp =  req.getRequestDispatcher("./error.jsp");
            disp.forward(req,resp);
        }
    }
}
