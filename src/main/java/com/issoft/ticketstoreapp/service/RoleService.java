package com.issoft.ticketstoreapp.service;

import com.issoft.ticketstoreapp.annotation.Audit;
import com.issoft.ticketstoreapp.exception.RoleNotFoundException;
import com.issoft.ticketstoreapp.model.Role;
import com.issoft.ticketstoreapp.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HallService.class);
    private static final String NO_ROLE_MESSAGE = "No role with name [%s] exists";

    private final RoleRepository roleRepository;

//    @Autowired
//    public RoleService(RoleRepository roleRepository) {
//        this.roleRepository = roleRepository;
//    }

    @Audit
    public Role findByName(String roleName) {
        LOGGER.debug("Entering RoleService.findByName()");
        Role foundRole = this.roleRepository
                .findByName(roleName)
                .orElseThrow(() -> new RoleNotFoundException(String.format(NO_ROLE_MESSAGE, roleName)));

        LOGGER.debug("Exiting RoleService.findByName()");
        return foundRole;
    }
}
