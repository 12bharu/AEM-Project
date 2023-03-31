package com.mysite.core.service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysite.core.models.User;



@Component(service = AuthenticationService.class)
public class AuthenticationService {

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    public String authenticate(String username, String password, SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        String role = null;
        try (ResourceResolver resourceResolver = resourceResolverFactory.getServiceResourceResolver(Collections.singletonMap(ResourceResolverFactory.SUBSERVICE, "authenticationService"))) {
            Resource resource = resourceResolver.getResource("/content/dam/mysite/employees.json");
            if (resource != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                String json = resource.getValueMap().get("jcr:data", String.class);
                List<User> users = objectMapper.readValue(json, new TypeReference<List<User>>() {});
                for (User user : users) {
                    if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                        role = user.getRole();
                        request.getSession().setAttribute("user", user);
                        response.sendRedirect(getHomePageUrl(role));
                        break;
                    }
                }
            }
        }   catch (IOException | org.apache.sling.api.resource.LoginException e) {
            // Handle exception
        }
        return role;
    }

    private String getHomePageUrl(String role) {
        if (role.equals("admin")) {
            return "/content/userhome.html";
        } else if (role.equals("user")) {
            return "/content/userhome.html";
        } else {
            return "/content/loginpage0.html";
        }
    }

}