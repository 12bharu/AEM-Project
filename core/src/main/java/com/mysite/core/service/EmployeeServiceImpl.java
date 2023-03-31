package com.mysite.core.service;

import com.mysite.core.models.Employee;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component(service = EmployeeService.class)
@Designate(ocd = EmployeeServiceImpl.Config.class)
public class EmployeeServiceImpl implements EmployeeService  {

    private List<Employee> employees = new ArrayList<>();

    @ObjectClassDefinition(name = "Employee Service Configuration")
    public @interface Config {
        @AttributeDefinition(
                name = "JSON Configuration",
                description = "JSON Configuration File Path",
                type = AttributeType.STRING
        )
        String json_file() default "/content/dam/mysite/employees.json";
    }

    @Activate
    @Modified
    protected void activate(Config config) throws IOException, JSONException {
        loadEmployees(config.json_file());
    }

    private void loadEmployees(String jsonFilePath) throws IOException, JSONException {
        InputStream is = getClass().getResourceAsStream(jsonFilePath);
        String jsonTxt = IOUtils.toString(is, StandardCharsets.UTF_8);

        JSONArray jsonArray = new JSONArray(jsonTxt);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObj = jsonArray.getJSONObject(i);

            int id = jsonObj.getInt("id");
            String username = jsonObj.getString("username");
            String password = jsonObj.getString("password");
            String role = jsonObj.getString("role");

           // Employee employee = new Employee(id, username, password, role);
           // employees.add(employee);
        }
    }

    @Override
	public List<Employee> getEmployees() {
        return employees;
    }

	@Override
	public Employee getEmployeeByUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Employee getEmployee(String username, String password) {
		// TODO Auto-generated method stub
		return null;
	}
}