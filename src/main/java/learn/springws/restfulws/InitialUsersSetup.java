package learn.springws.restfulws;

import learn.springws.restfulws.data.entity.AuthorityEntity;
import learn.springws.restfulws.data.entity.RoleEntity;
import learn.springws.restfulws.data.entity.UserEntity;
import learn.springws.restfulws.data.repository.AuthorityRepository;
import learn.springws.restfulws.data.repository.RoleRepository;
import learn.springws.restfulws.data.repository.UserRepository;
import learn.springws.restfulws.shared.Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
public class InitialUsersSetup {

    private final AuthorityRepository authorityRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Utils utils;

    @Value("${publicIdLength}")
    private int userPublicIdLength;

    public InitialUsersSetup(AuthorityRepository authorityRepository, RoleRepository roleRepository,
                             UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, Utils utils) {
        this.authorityRepository = authorityRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.utils = utils;
    }

    @EventListener
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (userRepository.count() == 0) {
            createInitialUsers();
        }
    }

    private void createInitialUsers() {
        AuthorityEntity read = createAuthority("READ_AUTHORITY");
        AuthorityEntity write = createAuthority("WRITE_AUTHORITY");
        AuthorityEntity delete = createAuthority("DELETE_AUTHORITY");
        RoleEntity roleUser = createRole("ROLE_USER", List.of(read, write));
        RoleEntity roleAdmin = createRole("ROLE_ADMIN", List.of(read, write, delete));
        createUser("admin", "admin", "admin@example.org", "admin", roleAdmin);
        System.out.println(">>>>>>>>>>>>>>>>>> InitialUsersSetup::onApplicationEvent: admin user created");
    }

    private void createUser(String firstName, String lastName, String email, String password, RoleEntity role) {
        UserEntity entity = new UserEntity();
        entity.setFirstName(firstName);
        entity.setLastName(lastName);
        entity.setEmail(email);
        entity.setUserId(utils.generateUserId(userPublicIdLength));
        entity.setEncryptedPassword(bCryptPasswordEncoder.encode(password));
        entity.setRoles(List.of(role));
        userRepository.save(entity);
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
