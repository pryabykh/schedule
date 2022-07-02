package com.pryabykh.workspaceservice.repositories;

import com.pryabykh.workspaceservice.models.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
    long countByCreator(long creatorId);
}
