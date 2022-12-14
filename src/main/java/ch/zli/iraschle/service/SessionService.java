package ch.zli.iraschle.service;

import ch.zli.iraschle.model.session.Credentials;
import ch.zli.iraschle.model.user.ApplicationUserEntity;
import ch.zli.iraschle.util.JwtFactory;
import ch.zli.iraschle.util.WebApplicationExceptionFactory;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

import static ch.zli.iraschle.util.PasswortHashing.hashPassword;
import static ch.zli.iraschle.util.WebApplicationExceptionFactory.INVALID_CREDENTIALS;

@ApplicationScoped
public class SessionService {
    @Inject
    ApplicationUserService applicationUserService;

    public String sign_up(ApplicationUserEntity applicationUser) {
        Optional<ApplicationUserEntity> applicationUserWithEmail = applicationUserService.getApplicationUserWithEmail(applicationUser.getEmail());
        if (applicationUserWithEmail.isPresent()) {
            throw WebApplicationExceptionFactory.DUPLICATED_EMAIL;
        }
        applicationUserService.createApplicationUser(applicationUser);
        return JwtFactory.createJwt(applicationUser.getEmail(), applicationUser.getRole().name());
    }

    public String sign_in(Credentials credentials) {
        Optional<ApplicationUserEntity> optionalApplicationUser = applicationUserService.getApplicationUserWithCredentials(credentials.getEmail(), hashPassword(credentials.getPassword()));
        if (optionalApplicationUser.isEmpty()) {
            throw INVALID_CREDENTIALS;
        }
        ApplicationUserEntity applicationUser = optionalApplicationUser.get();
        return JwtFactory.createJwt(applicationUser.getEmail(), applicationUser.getRole().name());
    }
}
