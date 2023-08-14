package bullish.store.service.user;

import bullish.store.entity.UserEntity;

import java.util.List;

public interface UserService {

    UserEntity getByUsername(String username);
    List<UserEntity> getAll();

}
