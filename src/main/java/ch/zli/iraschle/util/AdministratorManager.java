package ch.zli.iraschle.util;

import ch.zli.iraschle.service.ApplicationUserService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class AdministratorManager {
    @Inject
    ApplicationUserService applicationUserService;

    private boolean alreadyChecked = false;
    private boolean adminExists = false;

    public boolean adminExists() {
        if (!alreadyChecked) {
            adminExists = !applicationUserService.getApplicationUsers().isEmpty();
            alreadyChecked = true;
        }
        return adminExists;
    }

    public void submitAdminRemoval() {
        adminExists = false;
    }

    public void submitAdminCreation() {
        adminExists = true;
    }
}
