package vn.com.cardoctor.garage_service.enums;

import java.util.HashMap;
import java.util.Map;

public enum EmployeeType {
    TECHNICIAN(0),
    TECHNICIAN_LEAD(1);
    private final Integer code;
    private static Map<Integer, EmployeeType> employeeTypeMap;


    EmployeeType(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static EmployeeType getEmployeeType(Integer code) {
        if (employeeTypeMap == null) {
            employeeTypeMap = new HashMap<>();
            employeeTypeMap.put(0, EmployeeType.TECHNICIAN);
            employeeTypeMap.put(1, EmployeeType.TECHNICIAN_LEAD);
        }
        return employeeTypeMap.get(code);
    }
}
