package com.occupancy.api.registration;

import com.occupancy.api.appuser.AppUser;
import com.occupancy.api.appuser.AppUserRole;
import com.occupancy.api.appuser.AppUserService;
import com.occupancy.api.email.EmailSender;
import com.occupancy.api.registration.token.ConfirmationToken;
import com.occupancy.api.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
@Service
@AllArgsConstructor
public class RegistrationService {

    private final AppUserService appUserService;
    private final EmailValidator emailValidator;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailCreator emailCreator = new EmailCreator();
    private final EmailSender emailSender;

    public Map<String, String> register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.
                test(request.getEmail());
        if (!isValidEmail) {
            throw new IllegalStateException("email not valid");
        }
        String token = appUserService.signUpUser(
                new AppUser(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPassword(),
                        AppUserRole.USER
                )
        );
        String link = "http://localhost:8080/registration/confirm?token=" + token;
        emailSender.send(
                request.getEmail(),
                buildEmail(request.getFirstName(), link));


        HashMap<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("message", "Email Confirmation Sent");
        return map;
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));
        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }
        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }
        confirmationTokenService.setConfirmedAt(token);
        appUserService.enableAppUser(
                confirmationToken.getAppUser().getEmail());
        return emailCreator.conformationSuccessesPage();
    }

    private String buildEmail(String name, String link) {
        return emailCreator.buildConfirmationEmail(name,link);
    }
}
