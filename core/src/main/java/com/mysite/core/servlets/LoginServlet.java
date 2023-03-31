package com.mysite.core.servlets;

import com.mysite.core.models.Employee;
import java.io.IOException; 
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.commons.io.IOUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Login Servlet",
        "sling.servlet.methods=" + "POST", "sling.servlet.paths=" + "/bin/newlogin" })
public class LoginServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = -6587862240235618977L;

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        // Get the user's entered credentials from the request
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Load the employee data from the JSON file
        ResourceResolver resolver = request.getResourceResolver();
        Resource resource = resolver.getResource("/content/dam/mysite/employees.json");
        InputStream stream = resource.adaptTo(InputStream.class);
        String jsonString = IOUtils.toString(new InputStreamReader(stream));
        Gson gson = new Gson();
        List<Employee> employees = gson.fromJson(jsonString, new TypeToken<List<Employee>>() {}.getType());

        // Find the employee with the matching username and password
        Employee matchedEmployee = null;
        for (Employee employee : employees) {
            if (employee.getUsername().equals(username) && employee.getPassword().equals(password)) {
                matchedEmployee = employee;
                break;
            }
        }

       // If the entered credentials match an employee, redirect to the appropriate home page based on the user's role
        if (matchedEmployee != null) {
           String redirectUrl = "/content/";
            if (matchedEmployee.getRole().equals("user")) {
               redirectUrl += "userhome.html";
            } else if (matchedEmployee.getRole().equals("admin")) {
                redirectUrl += "adminpage.html";
           }
            redirectUrl += "/" + matchedEmployee.getUsername();
            response.sendRedirect(redirectUrl);
        
       } else {
    	   
            // If no matching employee is found, display an error message on the login page
    	   
    	   response.sendRedirect("/content/loginpage0.html?error=Invalid%20username%20or%20password");
//            request.setAttribute("error", "Invalid username or password");
//
//          request.getRequestDispatcher("/content/loginpage0.html").forward(request, response);
       }
    }
        


}
