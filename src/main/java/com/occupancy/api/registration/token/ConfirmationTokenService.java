package com.occupancy.api.registration.token;

import com.occupancy.api.appuser.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    //saves a new confirmation token to database
    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }

    //finds a confirmation toke from database
    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    //set the confirmation time for the token
    public int setConfirmedAt(String token) {
        return confirmationTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }

    //deletes a confirmation token not used by user
    public void deleteByAppUser(AppUser appUser){
        Long id = confirmationTokenRepository.findByAppUser(appUser).getId();
        confirmationTokenRepository.deleteById(id);
    }
}
