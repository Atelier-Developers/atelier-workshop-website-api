package com.atelier.atelier.repository.user;

import com.atelier.atelier.entity.UserPortalManagment.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
