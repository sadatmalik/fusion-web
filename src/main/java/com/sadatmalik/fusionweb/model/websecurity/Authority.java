package com.sadatmalik.fusionweb.model.websecurity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

/**
 * Authority implements the GrantedAuthority interface providing an
 * implementation of the {@code getAuthority()} method.
 *
 * Note the subtle but significant difference between a Role and a
 * GrantedAuthority in Spring Security:
 *
 * Each GrantedAuthority can be thought of as an individual privilege,
 * the actual name is arbitrary. In this case, the different authority
 * names are maintained within the corresponding RoleEnum enumeration.
 *
 * You can use a GrantedAuthority directly, for example with the use of
 * an expression like hasAuthority(‘ROLE_USER'), thereby restricting
 * access in a fine-grained manner.
 *
 * Each Role can be thought of as a coarse-grained GrantedAuthority that
 * is represented as a String and prefixed with “ROLE“. When using a Role
 * directly, such as through an expression like hasRole(“ADMIN”), we are
 * restricting access in a coarse-grained manner.
 *
 * The core difference between GrantedAuthority and Role is in the
 * semantics of how we attach to how we use them to control security
 * authorisations within the application.
 *
 * An application UserPrincipal will have a List of associated
 * Authorities.
 *
 * @author sadatmalik
 */
@Entity
@Table(name = "authorities")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Authority implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleEnum authority;

    @Override
    public String getAuthority() {
        return authority.name();
    }
}
