package bullish.store.controller;

import bullish.store.assembler.UserModelAssembler;
import bullish.store.communication.user.User;
import bullish.store.entity.UserEntity;
import bullish.store.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserModelAssembler assembler;

    @GetMapping("/{username}")
    @PreAuthorize("#username == authentication.principal.username or hasAuthority('ADMIN')")
    public EntityModel<User> getUserByUsername(@PathVariable String username) {
        UserEntity userEntity = userService.getByUsername(username);
        User user = User.toDto(userEntity);
        return assembler.toModel(user);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public CollectionModel<EntityModel<User>> getAll() {
        List<UserEntity> entities = userService.getAll();
        List<User> users = User.toDtoList(entities);
        return assembler.toCollectionModel(users);
    }

}
