package bullish.store.service.auth;

import bullish.store.communication.auth.AuthenticateRequest;
import bullish.store.communication.auth.AuthenticateResponse;
import bullish.store.communication.auth.AuthRegisterRequest;

public interface AuthService {

    AuthenticateResponse register(AuthRegisterRequest request);
    AuthenticateResponse authenticate(AuthenticateRequest request);

}
