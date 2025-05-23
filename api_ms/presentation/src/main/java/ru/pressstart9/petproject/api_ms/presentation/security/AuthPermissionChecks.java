package ru.pressstart9.petproject.api_ms.presentation.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.pressstart9.petproject.api_ms.service.kafka.RequestProducer;
import ru.pressstart9.petproject.api_ms.service.util.ExtendedUser;
import ru.pressstart9.petproject.common_kafka.UserRole;
import ru.pressstart9.petproject.dto.requests.FriendPairBody;
import ru.pressstart9.petproject.dto.requests.GetRequest;

import java.util.Objects;

@Component("permission")
public class AuthPermissionChecks {
    private final RequestProducer requestProducer;

    public AuthPermissionChecks(RequestProducer requestProducer) {
        this.requestProducer = requestProducer;
    }

    public boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream().anyMatch(authority -> Objects.equals(authority.getAuthority(), UserRole.admin.toString()));
    }

    public boolean isSelf(Long selfId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream().anyMatch(authority ->
                Objects.equals(authority.getAuthority(), UserRole.admin.toString()))) {
            return true;
        }
        if (auth.getAuthorities().stream().noneMatch(authority ->
                Objects.equals(authority.getAuthority(), UserRole.user.toString()))) {
            return false;
        }

        return Objects.equals(selfId, ((ExtendedUser) auth.getPrincipal()).getId());
    }

    public boolean isOwner(Long petId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream().anyMatch(authority ->
                Objects.equals(authority.getAuthority(), UserRole.admin.toString()))) {
            return true;
        }
        if (auth.getAuthorities().stream().noneMatch(authority ->
                Objects.equals(authority.getAuthority(), UserRole.user.toString()))) {
            return false;
        }

        return Objects.equals(((ExtendedUser) auth.getPrincipal()).getId(),
                requestProducer.sendPetRequest(new GetRequest(petId)).getOwnerId());
    }

    public boolean isOwnerOfPair(FriendPairBody request) {
        return isOwner(request.petId);
    }
}
