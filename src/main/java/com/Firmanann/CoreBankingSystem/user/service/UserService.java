package com.Firmanann.CoreBankingSystem.user.service;

import com.Firmanann.CoreBankingSystem.auth.dto.RegisterRequest;
import com.Firmanann.CoreBankingSystem.roles.entity.RoleEntity;
import com.Firmanann.CoreBankingSystem.roles.entity.RolesStatus;
import com.Firmanann.CoreBankingSystem.roles.repository.RoleRepository;
import com.Firmanann.CoreBankingSystem.security.service.PasswordService;
import com.Firmanann.CoreBankingSystem.user.entity.UserEntity;
import com.Firmanann.CoreBankingSystem.user.repository.UserRepository;
import com.Firmanann.CoreBankingSystem.global.exception.BusinessException;
import com.Firmanann.CoreBankingSystem.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    //Inject Class
    private final UserRepository userRepo;
    private final PasswordService passwordService;
    private final RoleRepository roleRepo;

    //To validate existing email
    public void validateEmailAvailability(String email) {
        if (userRepo.existsByEmail(email)) {
            throw new BusinessException(ErrorCode.EMAIL_EXISTS);
        }
    }

    //Create new user
    public UserEntity createUser(RegisterRequest request){

        //1. Validasi ketersediaan email
        validateEmailAvailability(request.getEmail());

        //2. Cari role USER yang sudah ada di DB dan set ke new account
        RoleEntity userRole = roleRepo.findByStatus((RolesStatus.USER))
                .orElseThrow(() -> new BusinessException(ErrorCode.ROLE_NOT_FOUND));

        //2. Hash Password dari input user
        String HashedPassword = passwordService.hashPassword(request.getPassword());

        //3.Membuat objek baru lalu menyimpan data nya melalui repository
        UserEntity newUser = new UserEntity();

        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(HashedPassword);
        newUser.setRole(userRole);

        return userRepo.save(newUser);
    }


}
