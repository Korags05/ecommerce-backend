package org.kunal.ECommerce.auth.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    @GetMapping("/profile")
    public Map<String, Object> getUserProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> response = new HashMap<>();

        response.put("status", "success");
        response.put("message", "User profile retrieved successfully");
        response.put("userId", auth.getPrincipal());

        // Get additional user details from authentication
        if (auth.getDetails() instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> details = (Map<String, Object>) auth.getDetails();
            response.put("email", details.get("email"));
            response.put("name", details.get("name"));
        }

        return response;
    }

    @GetMapping("/protected")
    public Map<String, String> getProtectedData() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Map<String, String> response = new HashMap<>();

        response.put("status", "success");
        response.put("message", "This is protected data accessible with valid Firebase token");
        response.put("userId", (String) auth.getPrincipal());

        return response;
    }

    @GetMapping("/dashboard")
    public Map<String, Object> getDashboardData() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> response = new HashMap<>();

        response.put("status", "success");
        response.put("message", "Dashboard data loaded successfully");
        response.put("userId", auth.getPrincipal());
        response.put("data", Map.of(
                "totalUsers", 1250,
                "activeUsers", 856,
                "newSignups", 45
        ));

        return response;
    }
}
