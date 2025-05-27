package org.kunal.ECommerce.auth.Controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.kunal.ECommerce.auth.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/verify")
    public ResponseEntity<Map<String, String>> verifyUser(@RequestHeader("Authorization") String authHeader) {
        Map<String, String> response = new HashMap<>();

        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                logger.warn("Invalid Authorization header received");
                response.put("status", "error");
                response.put("message", "Missing or invalid Authorization header");
                return ResponseEntity.badRequest().body(response);
            }

            String token = authHeader.substring(7);
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);

            logger.info("User verified successfully: {}", decodedToken.getUid());

            // Optionally generate internal JWT token for backend services
            // String jwtToken = jwtUtil.generateToken(decodedToken.getUid(), decodedToken.getEmail());
            // Store or use this JWT as needed for your internal services

            response.put("status", "success");
            response.put("message", "User authenticated successfully");
            return ResponseEntity.ok(response);

        } catch (FirebaseAuthException e) {
            logger.warn("Firebase authentication failed: {}", e.getAuthErrorCode());
            response.put("status", "error");
            response.put("message", "Invalid or expired Firebase token");
            return ResponseEntity.status(401).body(response);
        } catch (Exception e) {
            logger.error("Unexpected error during authentication", e);
            response.put("status", "error");
            response.put("message", "Authentication failed");
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Auth service is running");
        return ResponseEntity.ok(response);
    }
}

