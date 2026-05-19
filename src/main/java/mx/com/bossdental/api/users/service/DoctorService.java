package mx.com.bossdental.api.users.service;

import lombok.RequiredArgsConstructor;
import mx.com.bossdental.api.users.dto.DoctorOptionResponse;
import mx.com.bossdental.api.users.mapper.UserMapper;
import mx.com.bossdental.api.users.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    public List<DoctorOptionResponse> findActiveDoctorOptions(Long branchId) {

        return userRepository.findActiveDoctorOptions(branchId)
                .stream()
                .map(userMapper::toDoctorOptionResponse)
                .toList();
    }
}