package ru.pressstart9.petproject.api_ms.presentation.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.pressstart9.petproject.api_ms.service.kafka.RequestProducer;
import ru.pressstart9.petproject.api_ms.service.util.ExtendedUser;
import ru.pressstart9.petproject.commons.UserRole;
import ru.pressstart9.petproject.commons.dto.requests.CreatePetBody;
import ru.pressstart9.petproject.commons.dto.requests.FriendPairBody;
import ru.pressstart9.petproject.commons.dto.requests.GetRequest;
import ru.pressstart9.petproject.commons.dto.requests.RemoveFriendPair;

import java.util.Objects;

@Component("permission")
public class AuthPermissionChecks {
    private final RequestProducer requestProducer;

    public AuthPermissionChecks(RequestProducer requestProducer) {
        this.requestProducer = requestProducer;
    }

    public boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return Boolean.TRUE.equals(checkRole(auth));
    }

    public boolean isSelf(Long selfId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Boolean check = checkRole(auth);
        if (check != null) { return check; }

        return Objects.equals(selfId, ((ExtendedUser) auth.getPrincipal()).getId());
    }

    public boolean isOwner(Long petId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Boolean check = checkRole(auth);
        if (check != null) { return check; }

        Long id = requestProducer.sendPetRequest(new GetRequest(petId)).getOwnerId();
        return Objects.equals(((ExtendedUser) auth.getPrincipal()).getId(), id);
    }

    public boolean isOwnerOfPair(FriendPairBody request) {
        return isOwner(request.petId);
    }

    public boolean isOwnerOfPair(RemoveFriendPair request) {
        return isOwner(request.petId);
    }

    public boolean isOwnerOfCreated(CreatePetBody request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Boolean check = checkRole(auth);
        if (check != null) { return check; }

        return Objects.equals(((ExtendedUser) auth.getPrincipal()).getId(), request.ownerId);
    }

    private Boolean checkRole(Authentication auth) {
        if (auth.getAuthorities().stream().anyMatch(authority ->
                Objects.equals(authority.getAuthority(), UserRole.admin.toString()))) {
            return true;
        }
        if (auth.getAuthorities().stream().noneMatch(authority ->
                Objects.equals(authority.getAuthority(), UserRole.user.toString()))) {
            return false;
        }

        return null;
    }
}
