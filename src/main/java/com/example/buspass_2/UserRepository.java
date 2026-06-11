package com.example.buspass_2;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{

    User findByEmailAndPassword(String email, String password);

    List<User> findByApprovalStatus(String approvalStatus);
   // public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByEmail(String email);
    //User findByEmail(String email);
        List<User> findByNameContaining(String name);
        User findByPassNumber(String passNumber);
        //User findByEmail(String email);

       // User findByEmailAndPassword(String email, String password);
    }
