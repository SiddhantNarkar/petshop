package com.petshop.frontend.service;

import com.petshop.frontend.model.Contributor;
import com.petshop.frontend.repository.ContributorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContributorService {

    private final ContributorRepository contributorRepository;

    public ContributorService(ContributorRepository contributorRepository) {
        this.contributorRepository = contributorRepository;
    }

    // ── Auth ──────────────────────────────────────────────────────────────────

    public Contributor authenticate(String username, String password) {
        Optional<Contributor> contributor = contributorRepository.findByUsername(username);
        if (contributor.isPresent() && contributor.get().getPassword().equals(password)) {
            return contributor.get();
        }
        return null;
    }

    /**
     * Registers a brand-new ROLE_USER account.
     * Returns null if the username is already taken.
     */
    public Contributor register(String username, String password, String name) {
        if (contributorRepository.findByUsername(username).isPresent()) {
            return null; // username taken
        }
        Contributor c = new Contributor();
        c.setUsername(username);
        c.setPassword(password);
        c.setName(name);
        c.setRole("Member");
        c.setDescription("Registered user");
        c.setProfilePicUrl("/images/default.png");
        c.setUserRole("ROLE_USER");
        return contributorRepository.save(c);
    }

    // ── Queries ───────────────────────────────────────────────────────────────

    public List<Contributor> getAllContributors() {
        return contributorRepository.findAll();
    }

    /** Returns only ROLE_ADMIN contributors (the team members on the contributors page). */
    public List<Contributor> getAdminContributors() {
        return contributorRepository.findAll().stream()
                .filter(Contributor::isAdmin)
                .sorted((left, right) -> left.getName().compareToIgnoreCase(right.getName()))
                .toList();
    }

    public Optional<Contributor> getContributorById(Long id) {
        return contributorRepository.findById(id);
    }

    // ── Endpoint catalogue (unchanged) ────────────────────────────────────────

    public List<Endpoint> getEndpointsForContributor(String contributorUsername) {
        List<Endpoint> endpoints;
        switch (contributorUsername.toLowerCase()) {
            case "sharvari":
                endpoints = List.of(
                        endpoint("POST", "/api/v1/customers/register", "Create a new customer record for the storefront."),
                        endpoint("POST", "/api/v1/customers/login", "Validate returning customers with login credentials."),
                        endpoint("GET", "/api/v1/customers/{id}", "Fetch a single customer profile by customer id."),
                        endpoint("PUT", "/api/v1/customers/{id}", "Update customer identity and contact information."),
                        endpoint("GET", "/api/v1/customers/{id}/address", "Load the saved address for one customer."),
                        endpoint("POST", "/api/v1/customers/{id}/address", "Attach a fresh address to an existing customer."),
                        endpoint("PUT", "/api/v1/customers/addresses/{id}", "Edit a saved address without recreating it.")
                );
                break;
            case "yatesh":
                endpoints = List.of(
                        endpoint("GET", "/api/v1/inventory/food", "Browse the current pet food inventory catalogue."),
                        endpoint("GET", "/api/v1/inventory/food/{id}", "Inspect one pet food item in detail."),
                        endpoint("POST", "/api/v1/inventory/food", "Register a new food item in stock."),
                        endpoint("PUT", "/api/v1/inventory/food/{id}", "Refresh quantity, pricing, or brand details."),
                        endpoint("GET", "/api/v1/inventory/suppliers", "List every supplier supporting inventory operations."),
                        endpoint("GET", "/api/v1/inventory/suppliers/{id}", "Retrieve a single supplier relationship."),
                        endpoint("POST", "/api/v1/inventory/suppliers", "Add a supplier to the procurement system."),
                        endpoint("PUT", "/api/v1/inventory/suppliers/{id}", "Update supplier contact and business details."),
                        endpoint("GET", "/api/v1/inventory/employees", "View the employees connected to inventory workflows."),
                        endpoint("GET", "/api/v1/inventory/employees/{id}", "Look up a specific employee assignment."),
                        endpoint("POST", "/api/v1/inventory/employees", "Create a new employee inventory record."),
                        endpoint("PUT", "/api/v1/inventory/employees/{id}", "Edit employee role and contact details."),
                        endpoint("POST", "/api/v1/inventory/pets/{petId}/food/{foodId}", "Link a food item to a pet profile."),
                        endpoint("POST", "/api/v1/inventory/pets/{petId}/suppliers/{supplierId}", "Associate a pet with its supplier source."),
                        endpoint("POST", "/api/v1/inventory/pets/{petId}/employees/{employeeId}", "Assign an employee to care for a pet."),
                        endpoint("GET", "/api/v1/inventory/pets/{petId}/food", "See all food mapped to a specific pet."),
                        endpoint("GET", "/api/v1/inventory/pets/{petId}/suppliers", "View supplier history for an individual pet."),
                        endpoint("GET", "/api/v1/inventory/pets/{petId}/employees", "List employees currently attached to a pet.")
                );
                break;
            case "sushmita":
                endpoints = List.of(
                        endpoint("GET", "/api/v1/categories", "Load every pet category available in the catalog."),
                        endpoint("GET", "/api/v1/categories/{id}", "Inspect one pet category by id."),
                        endpoint("POST", "/api/v1/categories", "Create a new category for the pet catalog."),
                        endpoint("PUT", "/api/v1/categories/{id}", "Rename or refine an existing category."),
                        endpoint("GET", "/api/v1/pets", "Browse all pets currently listed for sale."),
                        endpoint("GET", "/api/v1/pets/{id}", "Fetch the core record for a single pet."),
                        endpoint("POST", "/api/v1/pets", "Publish a new pet listing to the shop."),
                        endpoint("PUT", "/api/v1/pets/{id}", "Update a pet listing with fresh details or pricing."),
                        endpoint("GET", "/api/v1/pets/categories/{categoryId}", "Filter pets by a specific category."),
                        endpoint("GET", "/api/v1/pets/{id}/details", "Return a richer pet details payload for the UI.")
                );
                break;
            case "tejas":
                endpoints = List.of(
                        endpoint("GET", "/api/v1/services/grooming", "List every grooming service the store offers."),
                        endpoint("POST", "/api/v1/services/grooming", "Create a brand-new grooming service option."),
                        endpoint("POST", "/api/v1/services/pets/{petId}/grooming/{serviceId}", "Assign a grooming service to a pet."),
                        endpoint("GET", "/api/v1/services/pets/{petId}/grooming", "Review grooming services scheduled for one pet."),
                        endpoint("GET", "/api/v1/services/vaccinations", "Browse the vaccination service catalogue."),
                        endpoint("POST", "/api/v1/services/vaccinations", "Add a new vaccination option for staff."),
                        endpoint("POST", "/api/v1/services/pets/{petId}/vaccinations/{vaccinationId}", "Record a vaccination against a pet."),
                        endpoint("GET", "/api/v1/services/pets/{petId}/vaccinations", "Display vaccination history for a pet.")
                );
                break;
            case "siddhant":
                endpoints = List.of(
                        endpoint("POST", "/api/v1/transactions/orders", "Create a new order transaction for checkout."),
                        endpoint("GET", "/api/v1/transactions/orders/{id}", "Retrieve one order by transaction id."),
                        endpoint("GET", "/api/v1/transactions/customers/{id}/orders", "List every order placed by one customer."),
                        endpoint("GET", "/api/v1/transactions/{id}", "Fetch a transaction summary for audit trails."),
                        endpoint("GET", "/api/v1/transactions/customers/{id}", "Load transaction history for one customer."),
                        endpoint("GET", "/api/v1/transactions", "Browse the full transaction ledger."),
                        endpoint("PUT", "/api/v1/transactions/{id}/status", "Update the lifecycle status of an order.")
                );
                break;
            default:
                endpoints = List.of();
        }
        return endpoints;
    }

    private Endpoint endpoint(String method, String path, String description) {
        return new Endpoint(method, path, description);
    }

    public int getTotalEndpointsForContributor(String contributorUsername) {
        return getEndpointsForContributor(contributorUsername).size();
    }

    public static class Endpoint {
        private final String method;
        private final String path;
        private final String description;

        public Endpoint(String method, String path) {
            this(method, path, "");
        }

        public Endpoint(String method, String path, String description) {
            this.method = method;
            this.path = path;
            this.description = description;
        }

        public String getMethod() { return method; }
        public String getPath()   { return path;   }
        public String getDescription() { return description; }
    }
}
