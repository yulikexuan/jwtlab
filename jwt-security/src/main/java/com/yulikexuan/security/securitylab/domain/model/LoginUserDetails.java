//: com.yulikexuan.utils.securitylab.domain.model.LoginUserDetails.java


package com.yulikexuan.security.securitylab.domain.model;


import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;


@Data
@Builder @AllArgsConstructor
public class LoginUserDetails implements UserDetails {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String token;

    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}///:~