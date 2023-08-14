package bullish.store.controller;

import bullish.store.assembler.UserModelAssembler;
import bullish.store.communication.user.User;
import bullish.store.entity.UserEntity;
import bullish.store.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserController {

    private final UserService userService;
    private final UserModelAssembler assembler;

    @Autowired
    public UserController(UserService userService, UserModelAssembler assembler) {
        this.userService = userService;
        this.assembler = assembler;
    }

    @GetMapping("/{username}")
    @PreAuthorize("#username == authentication.principal.username or hasRole('ADMIN')")
    public EntityModel<User> getUserByUsername(@PathVariable String username) {
        UserEntity userEntity = userService.getByUsername(username);
        User user = User.toDto(userEntity);
        return assembler.toModel(user);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public CollectionModel<EntityModel<User>> getAll() {
        List<UserEntity> entities = userService.getAll();
        List<User> users = User.toDtoList(entities);
        return assembler.toCollectionModel(users);
    }

}
