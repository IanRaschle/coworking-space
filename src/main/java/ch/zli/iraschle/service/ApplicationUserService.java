package ch.zli.iraschle.service;

import ch.zli.iraschle.model.user.ApplicationUserEntity;
import ch.zli.iraschle.model.user.Role;
import ch.zli.iraschle.util.AdministratorManager;
import ch.zli.iraschle.util.WebApplicationExceptionFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static ch.zli.iraschle.model.user.Role.ADMINISTRATOR;
import static ch.zli.iraschle.model.user.Role.MEMBER;

@ApplicationScoped
public class ApplicationUserService {
    @Inject
    EntityManager entityManager;

    @Inject
    AdministratorManager administratorManager;

    public List<ApplicationUserEntity> getApplicationUsers() {
        var query = entityManager.createQuery("FROM ApplicationUserEntity ", ApplicationUserEntity.class);
        return query.getResultList();
    }

    public ApplicationUserEntity getApplicationUser(long id) {
        return entityManager.find(ApplicationUserEntity.class, id);
    }

    public Optional<ApplicationUserEntity> getApplicationUserWithCredentials(String email, String password) {
        return entityManager
                .createQuery("SELECT u FROM ApplicationUserEntity u WHERE u.email = :email AND u.password = :password", ApplicationUserEntity.class)
                .setParameter("email", email)
                .setParameter("password", password)
                .getResultStream()
                .findFirst();
    }

    public Optional<ApplicationUserEntity> getApplicationUserWithEmail(String email) {
        //TODO error handling
        return entityManager
                .createQuery("SELECT u FROM ApplicationUserEntity u WHERE u.email = :email", ApplicationUserEntity.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst();
    }

    @Transactional
    public ApplicationUserEntity createApplicationUser(ApplicationUserEntity applicationUser) {
        if (!administratorManager.adminExists()) {
            applicationUser.setRole(ADMINISTRATOR);
            administratorManager.submitAdminCreation();
        } else {
            applicationUser.setRole(MEMBER);
        }
        entityManager.persist(applicationUser);
        return applicationUser;
    }

    @Transactional
    public void deleteApplicationUser(Long id) {
        ApplicationUserEntity applicationUser = getApplicationUser(id);
        if (applicationUser == null) {
            throw WebApplicationExceptionFactory.NO_USER_WITH_ID;
        }
        if (getRoleOfApplicationUser(id) == ADMINISTRATOR) {
            administratorManager.submitAdminRemoval();
        }
        entityManager
                .createQuery("DELETE FROM BookingEntity b WHERE b.applicationUser.id = :id")
                .setParameter("id", id)
                .executeUpdate();
        entityManager
                .createQuery("DELETE FROM ApplicationUserEntity u WHERE u.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    @Transactional
    public ApplicationUserEntity updateApplicationUser(ApplicationUserEntity applicationUser) {
        ApplicationUserEntity oldApplicationUser = getApplicationUser(applicationUser.getId());
        if (oldApplicationUser == null) {
            throw WebApplicationExceptionFactory.NO_USER_WITH_ID;
        }
        addRoleToApplicationUser(applicationUser);
        return entityManager.merge(applicationUser);
    }

    @Transactional
    public void changeEmailOfApplicationUser(String email, String newEmail) {
        Optional<ApplicationUserEntity> applicationUserWithEmail = getApplicationUserWithEmail(email);
        if (applicationUserWithEmail.isEmpty()) {
            throw WebApplicationExceptionFactory.NO_USER_WITH_EMAIL;
        }
        ApplicationUserEntity applicationUser = applicationUserWithEmail.get();
        applicationUser.setEmail(newEmail);
        entityManager.merge(applicationUser);
    }

    @Transactional
    public void changePasswordOfApplicationUser(String email, String newPassword) {
        Optional<ApplicationUserEntity> applicationUserWithEmail = getApplicationUserWithEmail(email);
        if (applicationUserWithEmail.isEmpty()) {
            throw WebApplicationExceptionFactory.NO_USER_WITH_EMAIL;
        }
        ApplicationUserEntity applicationUser = applicationUserWithEmail.get();
        applicationUser.setPassword(newPassword);
        entityManager.merge(applicationUser);
    }

    private void addRoleToApplicationUser(ApplicationUserEntity applicationUser) {
        Role role = getRoleOfApplicationUser(applicationUser.getId());
        applicationUser.setRole(role);
    }

    private Role getRoleOfApplicationUser(long id) {
        ApplicationUserEntity applicationUser = getApplicationUser(id);
        return applicationUser.getRole();
    }
}
