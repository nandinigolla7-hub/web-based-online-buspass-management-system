package com.example.buspass_2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.buspass_2.model.Pass;

public interface PassRepository extends JpaRepository<Pass, Long> {

}