package com.occupancy.api.appuser;

import com.occupancy.api.facility.Facility;
import com.occupancy.api.organization.OrganizationRepository;
import com.occupancy.api.registration.token.ConfirmationToken;
import com.occupancy.api.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG = "user with email %s not found";
    private final AppUserRepository appUserRepository;
    private final OrganizationRepository organizationRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                String.format(USER_NOT_FOUND_MSG, email)));
    }

    public String signUpUser(AppUser appUser) {
        boolean userExists = appUserRepository
                .findByEmail(appUser.getEmail())
                .isPresent();
        if (userExists) {
            // TODO check of attributes are the same and
            // TODO if email not confirmed send confirmation email.

            boolean enabled = appUserRepository
                    .findByEmail(appUser.getEmail())
                    .get().getEnabled();
            if(!enabled){
                Long userId = appUserRepository.findByEmail(appUser.getEmail()).get().getId();
                confirmationTokenService.deleteByAppUser(appUserRepository.findByEmail(appUser.getEmail()).get());
                appUserRepository.deleteById(userId);
            }else{
                throw new IllegalStateException("email already taken");
            }
        }
        String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);
        appUserRepository.save(appUser);
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );
        confirmationTokenService.saveConfirmationToken(
                confirmationToken);
        //        TODO: SEND EMAIL
        return token;
    }

    public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);
    }

    public Map<String, String> getDetails(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser appUser;
        if (principal instanceof AppUser) {
            appUser = ((AppUser)principal);
            HashMap<String, String> map = new HashMap<>();
            map.put("firstName", appUser.getFirstName());
            map.put("lastName", appUser.getLastName());
            map.put("email", appUser.getEmail());
            map.put("appUserRole",appUser.getAppUserRole().toString());
            return map;
        }else {
            return null;

        }
    }

    @Transactional
    public void setToManager(Long organizationId, Long appUserId){
        if(getCurrentUser().getAppUserRole() == AppUserRole.ADMIN){
            organizationRepository.findById(organizationId)
                    .orElseThrow(
                            () -> new IllegalStateException(
                                    "No organization with that Id exists")
                    );
           AppUser appUser = appUserRepository.findById(appUserId)
                   .orElseThrow(
                           () -> new UsernameNotFoundException(
                           String.format(USER_NOT_FOUND_MSG,appUserId))
                   );
           appUser.setToManager(organizationId);
        }else{
            throw new IllegalStateException(
                    "Only admin are allowed to register users to a company");
        }
    }

    public AppUser getCurrentUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AppUser appUser;
        if (principal instanceof AppUser) {
            appUser = ((AppUser)principal);
            return appUser;
        }else {
            return null;
        }
    }

}
