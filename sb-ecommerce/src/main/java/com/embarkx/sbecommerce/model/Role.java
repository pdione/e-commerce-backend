package com.embarkx.sbecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Entity
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @ToString.Exclude
    @Enumerated(EnumType.STRING) // EnumType.STRING : convert enum to string. Persist in database as a String
    @Column(length = 20, name = "role_name")
    private AppRole roleName;

    public Role(AppRole roleName){
        this.roleName = roleName;
    }
}
