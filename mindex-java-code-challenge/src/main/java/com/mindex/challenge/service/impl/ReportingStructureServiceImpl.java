package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public ReportingStructure lookup(String id) {
        LOG.debug("Looking up reporting structure for employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        ReportingStructure reportingStructure = new ReportingStructure();
        reportingStructure.setEmployee(employee);
        reportingStructure.setNumberOfReports(GetNumberOfReports(employee));

        return reportingStructure;
    }

    private int GetNumberOfReports(Employee employee)
    {
        List<Employee> directReports = employee.getDirectReports();
        int numberOfReports = 0;
        if (directReports != null) {
            for (Employee employee2 : directReports) {
                numberOfReports += 1 + GetNumberOfReports(employeeRepository.findByEmployeeId(employee2.getEmployeeId()));
            }
        }
        
        return numberOfReports;
    }
}
