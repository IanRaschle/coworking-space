package ch.zli.iraschle.util;

import ch.zli.iraschle.model.user.Role;
import io.smallrye.jwt.build.Jwt;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;

public class JwtFactory {
    public static String createJwt(String email, Role role) {
        return Jwt.issuer("https://coworking-space.project.ch")
                .upn(email)
                .groups(new HashSet<>(List.of(role.name())))
                .expiresIn(Duration.ofHours(12))
                .sign();
    }
}