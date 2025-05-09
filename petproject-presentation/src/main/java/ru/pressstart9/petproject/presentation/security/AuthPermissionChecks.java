package ru.pressstart9.petproject.presentation.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.pressstart9.petproject.commons.UserRole;
import ru.pressstart9.petproject.dto.requests.FriendPairBody;
import ru.pressstart9.petproject.service.util.ExtendedUser;
import ru.pressstart9.petproject.service.PetService;

import java.util.Objects;

@Component("permission")
public class AuthPermissionChecks {
    private final PetService petService;

    public AuthPermissionChecks(PetService petService) {
        this.petService = petService;
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

        return Objects.equals(((ExtendedUser) auth.getPrincipal()).getId(), petService.getPetDtoById(petId).getOwnerId());
    }

    public boolean isOwnerOfPair(FriendPairBody request) {
        return isOwner(request.petId);
    }
}
