package ch.zli.iraschle.service;

import ch.zli.iraschle.model.user.ApplicationUserEntity;
import ch.zli.iraschle.model.user.Role;
import ch.zli.iraschle.util.AdministratorManager;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

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
        if (getRoleOfApplicationUser(id) == ADMINISTRATOR) {
            administratorManager.submitAdminRemoval();
        }
        entityManager.remove(applicationUser);
    }

    @Transactional
    public ApplicationUserEntity updateApplicationUser(ApplicationUserEntity applicationUser) {
        addRoleToApplicationUser(applicationUser);
        entityManager.merge(applicationUser);
        return applicationUser;
    }

    @Transactional
    public ApplicationUserEntity changeEmailOfApplicationUser(long id, String newEmail) {
        ApplicationUserEntity applicationUser = getApplicationUser(id);
        applicationUser.setEmail(newEmail);
        entityManager.merge(applicationUser);
        return applicationUser;
    }

    @Transactional
    public ApplicationUserEntity changePasswordOfApplicationUser(long id, String newPassword) {
        ApplicationUserEntity applicationUser = getApplicationUser(id);
        applicationUser.setPassword(newPassword);
        entityManager.merge(applicationUser);
        return applicationUser;
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
