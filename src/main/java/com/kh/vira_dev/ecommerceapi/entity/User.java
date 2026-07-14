package com.kh.vira_dev.ecommerceapi.entity;

import com.kh.vira_dev.ecommerceapi.enums.Permission;
import com.kh.vira_dev.ecommerceapi.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tbl_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fullName" , nullable = false , length = 50)
    private String fullName;

    @Column(name = "email" , nullable = false , unique = true , length = 50)
    private String email;

    @Column(name = "password" , nullable = false , length = 255)
    private String password;

    @Column(name = "bio" , length = 255)
    private String bio;

    @Column(name = "reset_opt" , length = 6)
    private String resetOpt;

    @Column(name = "reset_opt_expire_at")
    private Long resetOptExpireAt;

    @Column(name = "phone_number" , length = 20)
    private String phoneNumber;

    @Column(name = "date_of_birth")
    private String dateOfBirth;

    private boolean status = false;

    @Column(name = "roles" , nullable = false , length = 50)
    private Set<Role> roles = new HashSet<>();

    public Set<SimpleGrantedAuthority> getAuthorities() {
        
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        
        roles.forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.name()));
            for (Permission permission : role.getPermissions()) {
                authorities.add(new SimpleGrantedAuthority(permission.name()));
            }
        });
        
        return authorities;
    }

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL , fetch = FetchType.EAGER)
    private RefreshToken refreshToken;

    @OneToMany(mappedBy = "user" , fetch = FetchType.LAZY , cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();

    @OneToOne(mappedBy = "user" , cascade = CascadeType.ALL , fetch = FetchType.EAGER)
    private Cart cart;

    @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    private List<ShippingAddress> shippingAddresses = new ArrayList<>();

}
