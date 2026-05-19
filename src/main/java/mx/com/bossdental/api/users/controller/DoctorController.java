package mx.com.bossdental.api.users.controller;

import lombok.RequiredArgsConstructor;
import mx.com.bossdental.api.users.dto.DoctorOptionResponse;
import mx.com.bossdental.api.users.service.DoctorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping("/doctors/active")
    public List<DoctorOptionResponse> findActiveDoctorOptions(
            @RequestParam(required = false) Long branchId
    ) {
        return doctorService.findActiveDoctorOptions(branchId);
    }
}