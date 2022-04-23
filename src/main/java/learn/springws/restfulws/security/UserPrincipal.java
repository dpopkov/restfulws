package learn.springws.restfulws.security;

import learn.springws.restfulws.data.entity.AuthorityEntity;
import learn.springws.restfulws.data.entity.RoleEntity;
import learn.springws.restfulws.data.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {

    private final String username;
    private final String password;
    private final Collection<GrantedAuthority> authorities;
    private final String publicId;

    public UserPrincipal(UserEntity userEntity) {
        username = userEntity.getEmail();   // email is used as username
        password = userEntity.getEncryptedPassword();
        publicId = userEntity.getUserId();
        authorities = initAuthorities(userEntity.getRoles());
    }

    private Collection<GrantedAuthority> initAuthorities(Collection<RoleEntity> roles) {
        if (roles == null || roles.isEmpty()) {
            return List.of();
        }
        Set<String> authorityNames = new HashSet<>();
        for (RoleEntity role : roles) {
            authorityNames.add(role.getName());
            Collection<AuthorityEntity> roleAuthorities = role.getAuthorities();
            if (roleAuthorities == null || roleAuthorities.isEmpty()) {
                continue;
            }
            roleAuthorities.forEach(authority -> authorityNames.add(authority.getName()));
        }
        return authorityNames.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    public String getPublicId() {
        return publicId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
        return true; // todo: use email verification status
    }
}
