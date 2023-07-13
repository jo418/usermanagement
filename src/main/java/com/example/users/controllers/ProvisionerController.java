package com.example.users.controllers;

import com.example.users.model.Provisioner;
import com.example.users.repositories.ProvisionerRepository;
import com.example.users.secret.JwtGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/organisations/{orgId}/provisioners")
public class ProvisionerController {
    private final ProvisionerRepository provisionerRepository;
    private final JwtGenerator jwtGenerator = new JwtGenerator();

    @Autowired
    public ProvisionerController(ProvisionerRepository provisionerRepository) {
        this.provisionerRepository = provisionerRepository;
    }

    @PostMapping
    public Provisioner createProvisioner(@PathVariable Integer orgId, @RequestBody Provisioner provisioner) {
        // Generate a secret for the provisioner
        Integer provisionerId = provisioner.getExternalId();
        String secret = generateSecret(provisionerId, orgId);
        // Set the generated secret in the provisioner entity
        provisioner.setSecret(secret);
       return provisionerRepository.save(provisioner);
    }

    private String generateSecret(Integer provisionerId, Integer organizationId) {
        // Implement the logic to generate a secret (e.g., using UUID.randomUUID().toString())
        return jwtGenerator.generateJwt(provisionerId, organizationId);
    }
}
