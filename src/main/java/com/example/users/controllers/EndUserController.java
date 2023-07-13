package com.example.users.controllers;

import com.example.users.model.EndUser;
import com.example.users.model.IdList;
import com.example.users.model.Provisioner;
import com.example.users.repositories.EndUserRepository;
import com.example.users.repositories.ProvisionerRepository;
import com.example.users.secret.JwtGenerator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/organisations/{orgId}/provisioner/{provId}/users")
public class EndUserController {

    private final EndUserRepository endUserRepository;
    private final ProvisionerRepository provisionerRepository;
    private final JwtGenerator jwtGenerator = new JwtGenerator();

    public EndUserController(EndUserRepository endUserRepository, ProvisionerRepository provisionerRepository) {
        this.endUserRepository = endUserRepository;
        this.provisionerRepository = provisionerRepository;
    }

    @PostMapping
    public ResponseEntity<EndUser> createUser(@PathVariable("orgId") Integer orgId,
                                              @PathVariable("provId") Integer provId,
                                              @RequestHeader("Authorization") String rawToken,
                                              @RequestBody EndUser endUser) {
        /*
         NOTE provId is not needed in here to validate JWT token, because it is already in bearer token, but
         we let it be in here now, because the requirement was that it is present in API endpoint.
         Now we are adding an extra manual check against bearer token providedId.
         Another thing is that we can check, whether our database contains that token.
         We should discuss this issue.
        */
        HttpStatus status = getEndUserByValidating(provId, rawToken);
        if (!HttpStatus.OK.equals(status)) {
            return ResponseEntity.status(status).build();
        }
        // Set the organization ID and other necessary fields
        endUser.setOrganisationId(orgId);
        // Save the end user to the database
        EndUser savedEndUser = endUserRepository.save(endUser);
        // Return the response with the saved end user
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEndUser);
    }

    @PostMapping("/activateall")
    public ResponseEntity<List<EndUser>> activateUsers(@PathVariable("orgId") Integer orgId,
                                                    @PathVariable("provId") Integer provId,
                                                    @RequestHeader("Authorization") String rawToken,
                                                    @RequestBody IdList ids) {
        return activateAll(orgId, provId, rawToken, ids);
    }

    @PostMapping("/activate/{userId}")
    public ResponseEntity<List<EndUser>> activateOneUser(@PathVariable("orgId") Integer orgId,
                                                       @PathVariable("provId") Integer provId,
                                                       @PathVariable("userId") Integer userId,
                                                       @RequestHeader("Authorization") String rawToken) {
        IdList ids = new IdList();
        List<Integer> all = new ArrayList<>();
        all.add(userId);
        ids.setIds(all);
        return activateAll(orgId, provId, rawToken, ids);
    }

    @GetMapping("/allinactive")
    public ResponseEntity<List<EndUser>> getUsers(@PathVariable("orgId") Integer orgId,
                                                  @PathVariable("provId") Integer provId,
                                                  @RequestHeader("Authorization") String rawToken) {
        HttpStatus status = getEndUserByValidating(provId, rawToken);
        if (!HttpStatus.OK.equals(status)) {
            return ResponseEntity.status(status).build();
        }
        List<EndUser> inactiveUsers = endUserRepository.findAll().stream()
                // we assume that organization identifies also the provider.
                .filter(i -> i.getOrganisationId().equals(orgId) && !i.isActive())
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(inactiveUsers);
    }

    @GetMapping("/inactive/{userId}")
    public ResponseEntity<EndUser> getOneInactiveUser(@PathVariable("orgId") Integer orgId,
                                                  @PathVariable("provId") Integer provId,
                                                  @PathVariable("userId") Integer userId,
                                                  @RequestHeader("Authorization") String rawToken) {
        HttpStatus status = getEndUserByValidating(provId, rawToken);
        if (!HttpStatus.OK.equals(status)) {
            return ResponseEntity.status(status).build();
        }
        List<EndUser> inactiveUsers = endUserRepository.findAll().stream()
                // we assume that organization identifies also the provider.
                .filter(i -> i.getOrganisationId().equals(orgId) && !i.isActive() && i.getExternalId().equals(userId))
                .collect(Collectors.toList());
        if (inactiveUsers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(inactiveUsers.get(0));
        }
    }

    private ResponseEntity<List<EndUser>> activateAll(Integer orgId, Integer provId, String rawToken, IdList ids) {
        HttpStatus status = getEndUserByValidating(provId, rawToken);
        if (!HttpStatus.OK.equals(status)) {
            return ResponseEntity.status(status).build();
        }
        final Set<Integer> inactiveUserIds = ids.getIds().stream()
                .collect(Collectors.toSet());
        List<EndUser> inactiveUsersInDatabase = endUserRepository.findAll().stream()
                // we assume that organization identifies also the provider.
                .filter(i -> inactiveUserIds.contains(i.getExternalId()))
                .filter(i -> i.getOrganisationId().equals(orgId) && !i.isActive())
                .collect(Collectors.toList());
        inactiveUsersInDatabase.forEach(i -> i.setActive(true));
        Iterable<? extends EndUser> activatedUsers = inactiveUsersInDatabase;
        List<EndUser> saved = new ArrayList<>(endUserRepository.saveAll(activatedUsers));
        if (saved.isEmpty()) {
            // let us return a joke when no user was activated
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(saved);
        }
    }

    /**
     * Verify the JWT token
     * @param provId
     * @param rawToken is of form <Bearer token>. The string "Bearer " must be parsed away first.
     * @return HttpStatus.OK or HttpStatus.UNAUTHORIZED.
     */
    private HttpStatus getEndUserByValidating(Integer provId, String rawToken) {
        try {
            String token = rawToken.replace("Bearer ", "");
            // We could study claims also manually, but it is really not needed.
            Jws<Claims> claims = jwtGenerator.parseAndValidateJwt(token);
            Integer providerId = (Integer)claims.getBody().get("provId");
            if (provId != providerId) {
                return HttpStatus.UNAUTHORIZED;
            }
            Provisioner foundProvisioner = provisionerRepository.findAll().stream()
                    .filter(provisioner -> provisioner.getExternalId().equals(provId))
                    .findFirst()
                    .orElse(null);
            if (foundProvisioner == null) {
                return HttpStatus.UNAUTHORIZED;
            }
        } catch (Exception e) {
            return HttpStatus.UNAUTHORIZED;
        }
        return HttpStatus.OK;
    }
}
