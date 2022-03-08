package com.occupancy.api.appuser;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class AppUser implements UserDetails {

    @SequenceGenerator(
            name = "app_user_sequence",
            sequenceName = "app_user_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "app_user_sequence"
    )
    private Long id;
    private String firstName;
    private String lastName;
    private Long organizationId;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private AppUserRole appUserRole;
    private Boolean locked = false;
    private Boolean enabled = false;
    private long[] favourites;
    private int favouritesSize = 0;

    public AppUser(String email,
                   String password,
                   AppUserRole appUserRole) {
        this.email = email;
        this.password = password;
        this.appUserRole = AppUserRole.ADMIN;
    }

    public AppUser(String firstName,
                   String lastName,
                   String email,
                   String password,
                   AppUserRole appUserRole) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.appUserRole = appUserRole;
        this.favourites= new long[10];
    }

    public boolean addFavourite(Long facilityId){
        if(favouritesSize != 10) {
            favouritesSize++;
            favourites[favouritesSize - 1] = facilityId;
            return true;
        }
        return false;
    }

    public boolean removeFavourites(Long facilityId){
        if(favouritesSize == 0) return false;
        for(int i =0;i < favouritesSize;i++ ){
                if(favourites[i] == facilityId){
                    favouritesSize--;
                    for(int j =i;j < favouritesSize;j++ ){
                        favourites[j] = favourites[j+1];
                    }
                    return true;
                }
        }
        return false;
    }

    public void setToManager(Long companyId){
        this.organizationId = companyId;
        this.appUserRole = AppUserRole.MANAGER;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority(appUserRole.name());
        return Collections.singletonList(authority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

}
