package com.petshop.frontend.controller;

import com.petshop.frontend.dto.ContributorListDTO;
import com.petshop.frontend.model.ApiCallLog;
import com.petshop.frontend.model.Contributor;
import com.petshop.frontend.service.ApiCallService;
import com.petshop.frontend.service.ContributorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ContributorController {

    private final ContributorService contributorService;
    private final ApiCallService apiCallService;

    public ContributorController(ContributorService contributorService, ApiCallService apiCallService) {
        this.contributorService = contributorService;
        this.apiCallService = apiCallService;
    }

    @GetMapping("/contributors")
    public String contributors(HttpSession session, Model model) {
        Contributor user = (Contributor) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("contributors", buildList());
        return "contributors";
    }

    @GetMapping("/contributors/{id}")
    public String profile(@PathVariable Long id, HttpSession session, Model model) {
        Contributor user = (Contributor) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        Contributor contributor = contributorService.getContributorById(id).orElse(null);
        if (contributor == null) {
            return "redirect:/contributors";
        }

        List<ContributorService.Endpoint> endpoints =
                contributorService.getEndpointsForContributor(contributor.getUsername());
        List<ApiCallLog> recentActivity = apiCallService.getRecentActivity(id, 5);

        model.addAttribute("contributor", contributor);
        model.addAttribute("endpoints", endpoints);
        model.addAttribute("recentActivity", recentActivity);
        model.addAttribute("isAdmin", user.isAdmin());
        return "profile";
    }

    private List<ContributorListDTO> buildList() {
        List<Contributor> admins = contributorService.getAdminContributors();

        return List.of(
                contributor(resolveId(admins, "sharvari", 1L), "Sharvari Patil", "Backend Developer", "/images/sharvari.jpg", "Customers & Addresses API"),
                contributor(resolveId(admins, "yatesh", 2L), "Yatesh Ahire", "Backend Developer", "/images/yatesh.jpg", "Inventory API"),
                contributor(resolveId(admins, "sushmita", 3L), "Sushmita Damarla", "Backend Developer", "/images/sushmita.jpg", "Pets & Categories API"),
                contributor(resolveId(admins, "tejas", 4L), "Tejas Daphal", "Backend Developer", "/images/tejas.jpg", "Services API"),
                contributor(resolveId(admins, "siddhant", 5L), "Siddhant Narkar", "Backend Developer", "/images/siddhant.jpg", "Transactions API")
        );
    }

    private Long resolveId(List<Contributor> admins, String username, Long fallbackId) {
        return admins.stream()
                .filter(contributor -> username.equalsIgnoreCase(contributor.getUsername()))
                .map(Contributor::getId)
                .findFirst()
                .orElse(fallbackId);
    }

    private ContributorListDTO contributor(Long id,
                                           String name,
                                           String role,
                                           String photoUrl,
                                           String apiArea) {
        return new ContributorListDTO(id, name, role, photoUrl, apiArea);
    }
}
