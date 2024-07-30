package com.example.transfolio.domain.user.service;

import com.example.transfolio.common.error.ErrorMessage;
import com.example.transfolio.common.error.ErrorObj;
import com.example.transfolio.common.response.ResObj;
import com.example.transfolio.common.utils.JwtUtil;
import com.example.transfolio.domain.user.entity.UserEntity;
import com.example.transfolio.domain.user.entity.UserIntrsEntity;
import com.example.transfolio.domain.user.model.UserDto;
import com.example.transfolio.domain.user.repository.UserIntrsRepository;
import com.example.transfolio.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserSerivce {

    private final UserRepository userRepository;
    private final UserIntrsRepository userIntrsRepository;
    private final PasswordEncoder encoder;

    /**
     * 회원가입
     */
    public JSONObject registerUser(UserDto userDto) {

        if (!userRepository.findByUserId(userDto.getUserId()).isEmpty()) {
            return new ErrorObj(ErrorMessage.DUPLICATION_ID).getObject();
        }

        String userPassword = userDto.getPassword();
        String encodePassword = encoder.encode(userPassword);

        UserEntity build = UserEntity
                .builder()
                .userId(userDto.getUserId())
                .email(userDto.getEmail())
                .password(encodePassword)
                .build();

        userRepository.save(build);

        UserIntrsEntity userIntrs = UserIntrsEntity
                .builder()
                .user(build)
                .intrsLanguage(userDto.getUserIntrsDto().getIntrsLanguage())
                .intrsMajor(userDto.getUserIntrsDto().getIntrsMajor())
                .intrsCorporation(userDto.getUserIntrsDto().getIntrsCorporation())
                .intrsLiterature(userDto.getUserIntrsDto().getIntrsLiterature())
                .build();

        userIntrsRepository.save(userIntrs);

        return new ResObj().getObject();

    }

    /**
     * 로그인
     */
    public JSONObject login(UserDto userDto) {

        List<UserEntity> userList = userRepository.findByUserId(userDto.getUserId());

        boolean isSearchUser = userList.isEmpty();
        if(isSearchUser) {
            return new ErrorObj(ErrorMessage.REQUIRED_ID_PASSWORD).getObject();
        }

        UserEntity user = userList.get(0);

        boolean isPasswordMatches = encoder.matches(userDto.getPassword(), user.getPassword());
        if (!isPasswordMatches) {
            return new ErrorObj(ErrorMessage.REQUIRED_ID_PASSWORD).getObject();
        }

        // 로그인 성공 => Jwt Token 발급
        String secretKey = "my-secret-key-123123";
        long expireTimeMs = 5000000;     // Token 유효 시간 = 60분

        String jwtToken = JwtUtil.createToken(user.getUserId(), secretKey, expireTimeMs);

        return new ResObj(jwtToken).getObject();

    }


}
