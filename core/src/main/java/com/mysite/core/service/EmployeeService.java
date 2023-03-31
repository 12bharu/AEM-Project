package com.mysite.core.service;

import java.util.List;

import com.mysite.core.models.Employee;
public interface EmployeeService {
 public List<Employee> getEmployees();
 public Employee getEmployeeByUsername(String username);
public Employee getEmployee(String username, String password);
}