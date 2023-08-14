package bullish.store.service.user;

import bullish.store.entity.UserEntity;
import bullish.store.exception.user.UserNotFoundException;
import bullish.store.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.userRepository = repository;
    }

    @Transactional(readOnly = true)
    @Override
    public UserEntity getByUsername(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }

}
