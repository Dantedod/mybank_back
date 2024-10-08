package com.example.my_bank_backend.domain.user;

import com.example.my_bank_backend.domain.account.Account;
import com.example.my_bank_backend.dto.RegisterRequestDto;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String cpf;
    private String birthdate;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private Account account;

    public User(RegisterRequestDto registerRequestDto){
        this.name = registerRequestDto.name(); 
        this.email = registerRequestDto.email();
        this.password = registerRequestDto.password(); 
        this.phone = registerRequestDto.phone();
        this.cpf = registerRequestDto.cpf();
        this.birthdate = registerRequestDto.birthdate();
    }
}
