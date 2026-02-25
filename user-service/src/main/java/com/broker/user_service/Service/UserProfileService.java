package com.broker.user_service.Service;

import com.broker.user_service.DTOs.UserProfileRequest;
import com.broker.user_service.DTOs.UserProfileResponse;
import com.broker.user_service.events.UserCreatedEvent;
import com.broker.user_service.model.User;
import com.broker.user_service.model.UserPermission;
import com.broker.user_service.model.UserProfile;
import com.broker.user_service.repository.UserPermissionRepository;
import com.broker.user_service.repository.UserProfileRepository;
import com.broker.user_service.repository.UserRepository;
import com.broker.user_service.utils.CurrentUserProvider;
import com.broker.user_service.utils.ProductPermission;
import jakarta.transaction.Transactional;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserProfileService {

    private final UserProfileRepository repository;
    private final UserRepository userRepository;
    private final CurrentUserProvider currentUserProvider;
    private final KafkaTemplate<String, UserCreatedEvent> kafkaTemplate;

    public UserProfileService(UserProfileRepository repository, UserRepository userRepository, CurrentUserProvider currentUserProvider, KafkaTemplate<String, UserCreatedEvent> kafkaTemplate) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.currentUserProvider = currentUserProvider;
        this.kafkaTemplate = kafkaTemplate;
    }

    public UserProfileResponse createProfile(UserProfileRequest request) {

        Long userId = currentUserProvider.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        UserProfile profile = new UserProfile();
        profile.setUser(user);
        profile.setFullName(request.getFullName());
        profile.setDateOfBirth(request.getDateOfBirth());
        profile.setNationality(request.getNationality());
        profile.setPhone(request.getPhone());
        profile.setAddressLine(request.getAddressLine());
        profile.setCity(request.getCity());
        profile.setPostalCode(request.getPostalCode());
        profile.setCountry(request.getCountry());
        profile.setIdNumber(request.getIdNumber());
        profile.setEmploymentStatus(request.getEmploymentStatus());
        profile.setIncomeRange(request.getIncomeRange());
        profile.setInvestmentExperience(request.getInvestmentExperience());
        profile.setRiskTolerance(request.getRiskTolerance());
        profile.setBaseCurrency(request.getBaseCurrency());
        profile.setUserLevel(UserProfile.UserLevel.BASIC);

        repository.save(profile);

        user.setHasPersonalData(true);
        userRepository.save(user);
        UserCreatedEvent event = new UserCreatedEvent(
                user.getId(),
                user.getEmail(),
                profile.getBaseCurrency()
        );
        kafkaTemplate.send("user-created-topic", String.valueOf(user.getId()), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        System.out.println("❌ ERROR enviando evento a Kafka");
                        ex.printStackTrace();
                    } else {
                        System.out.println(
                                "✅ Evento enviado a Kafka | topic="
                                        + result.getRecordMetadata().topic()
                                        + " | partition=" + result.getRecordMetadata().partition()
                                        + " | offset=" + result.getRecordMetadata().offset()
                        );
                    }
                });

        return new UserProfileResponse(profile.getId(),profile.getFullName());
    }

    public String getFullNameSafe() {
        Long userId = currentUserProvider.getUserId();
        return repository.findFullNameByUserId(userId).orElse("");
    }
}
