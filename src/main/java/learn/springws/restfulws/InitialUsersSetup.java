package learn.springws.restfulws;

import learn.springws.restfulws.data.entity.AuthorityEntity;
import learn.springws.restfulws.data.entity.RoleEntity;
import learn.springws.restfulws.data.repository.AuthorityRepository;
import learn.springws.restfulws.data.repository.RoleRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
public class InitialUsersSetup {

    private final AuthorityRepository authorityRepository;
    private final RoleRepository roleRepository;

    public InitialUsersSetup(AuthorityRepository authorityRepository, RoleRepository roleRepository) {
        this.authorityRepository = authorityRepository;
        this.roleRepository = roleRepository;
    }

    @EventListener
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println(">>>>>>>>>>>>>>>>>> InitialUsersSetup::onApplicationEvent");
        AuthorityEntity read = createAuthority("READ_AUTHORITY");
        AuthorityEntity write = createAuthority("WRITE_AUTHORITY");
        AuthorityEntity delete = createAuthority("DELETE_AUTHORITY");
        System.out.println(">>>>>>>>>>>>>>>>>> InitialUsersSetup::onApplicationEvent: Authorities created");
        RoleEntity roleUser = createRole("ROLE_USER", List.of(read, write));
        RoleEntity roleAdmin = createRole("ROLE_ADMIN", List.of(read, write, delete));
        System.out.println(">>>>>>>>>>>>>>>>>> InitialUsersSetup::onApplicationEvent: Roles created");
    }

    private AuthorityEntity createAuthority(String name) {
        Optional<AuthorityEntity> byName = authorityRepository.findByName(name);
        if (byName.isEmpty()) {
            AuthorityEntity authority = new AuthorityEntity(name);
            return authorityRepository.save(authority);
        }
        return byName.get();
    }

    private RoleEntity createRole(String name, Collection<AuthorityEntity> authorities) {
        Optional<RoleEntity> byName = roleRepository.findByName(name);
        if (byName.isEmpty()) {
            RoleEntity role = new RoleEntity(name, authorities);
            return roleRepository.save(role);
        }
        return byName.get();
    }
}
