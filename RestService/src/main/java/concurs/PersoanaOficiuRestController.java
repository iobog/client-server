package concurs;

import concurs.model.PersoanaOficiu;
import concurs.repository.PersoanaOficiuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/concurs")
public class PersoanaOficiuRestController {
  private final PersoanaOficiuRepository persoanaOficiuRepo;
  private final JwtUtil jwtUtil;

  @Autowired
  public PersoanaOficiuRestController(PersoanaOficiuRepository persoanaOficiuRepo,
                                      JwtUtil jwtUtil) {
    this.persoanaOficiuRepo = persoanaOficiuRepo;
    this.jwtUtil = jwtUtil;
  }

  @PostMapping("/logout")
  public ResponseEntity<?> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return ResponseEntity.status(400).body("Token lipsă sau format greșit");
    }

    String token = authHeader.substring(7);
    try {
      String username = jwtUtil.extractUsername(token);
      return ResponseEntity.ok("Logout reușit pentru utilizatorul: " + username);
    } catch (Exception e) {
      return ResponseEntity.status(400).body("Token invalid: " + e.getMessage());
    }
  }


  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody PersoanaOficiu credentials) {
    PersoanaOficiu foundPersoana = persoanaOficiuRepo.findByUsernameAndPassword(
            credentials.getUsername(),
            credentials.getParola()
    );

    if (foundPersoana == null) {
      return ResponseEntity.status(401).body("Autentificare esuata: credentiale invalide");
    }


    String token = jwtUtil.generateToken(foundPersoana.getUsername());

    return ResponseEntity.ok()
            .header("Authorization", "Bearer " + token)
            .body(new AuthResponse(
                    foundPersoana.getUsername(),
                    foundPersoana.getOras(),
                    token
            ));
  }


  private static class AuthResponse {
    private final String username;
    private final String oras;
    private final String token;

    public AuthResponse(String username, String oras, String token) {
      this.username = username;
      this.oras = oras;
      this.token = token;
    }
    public String getUsername() { return username; }
    public String getOras() { return oras; }
    public String getToken() { return token; }
  }
}