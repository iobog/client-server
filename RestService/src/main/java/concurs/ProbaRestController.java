package concurs;
import concurs.repository.jdbc.ProbaDataBaseRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import concurs.model.Proba;
import concurs.repository.ProbaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

//
//@CrossOrigin(origins = "*")
//@RestController
//@RequestMapping("/concurs/probe")
//public class ProbaRestController {
//  private final ProbaDataBaseRepository probaRepo;
//
//  @Autowired
//  public ProbaRestController(ProbaDataBaseRepository probaRepo) {
//    this.probaRepo = probaRepo;
//  }
//
//  @GetMapping
//  public List<Proba> getAllProbe() {
//    return StreamSupport
//            .stream(probaRepo.findAll().spliterator(), false)
//            .collect(Collectors.toList());
//  }
//
//  @GetMapping("/{id}")
//  public Proba getProbaById(@PathVariable Integer id) {
//    Optional<Proba> probaOpt = Optional.ofNullable(probaRepo.findOne(id));
//    return probaOpt.orElseThrow(() -> new RuntimeException("Proba not found with id: " + id));
//  }
//
//  @PostMapping
//  public Proba createProba(@RequestBody Proba proba) {
//    return probaRepo.add(proba);
//  }
//
//  @PutMapping("/{id}")
//  public Proba updateProba(@PathVariable Integer id, @RequestBody Proba proba) {
//    Optional<Proba> existingProba = Optional.ofNullable(probaRepo.findOne(id));
//    if (existingProba.isEmpty()) {
//      throw new RuntimeException("Proba not found for update with id: " + id);
//    }
//    proba.setId(id);
//    return probaRepo.update(proba.getId(),proba);
//  }
//
//  @DeleteMapping("/{id}")
//  public void deleteProba(@PathVariable Integer id) {
//    Optional<Proba> probaOpt = Optional.ofNullable(probaRepo.findOne(id));
//    if (probaOpt.isEmpty()) {
//      throw new RuntimeException("Proba not found for deletion with id: " + id);
//    }
//    probaRepo.delete(id);
//  }
//}



@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/concurs/probe")
public class ProbaRestController {
  private final ProbaDataBaseRepository probaRepo;
  private final JwtUtil jwtUtil;

  @Autowired
  public ProbaRestController(ProbaDataBaseRepository probaRepo, JwtUtil jwtUtil) {
    this.probaRepo = probaRepo;
    this.jwtUtil = jwtUtil;
  }


  private void validateAuthHeader(String authHeader) {
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      throw new RuntimeException("Token lipsă sau invalid");
    }
    String token = authHeader.substring(7);
    if (jwtUtil.isTokenExpired(token)) {
      throw new RuntimeException("Token expirat");
    }
  }

  @GetMapping
  public ResponseEntity<?> getAllProbe(@RequestHeader("Authorization") String authHeader) {
    try {
      validateAuthHeader(authHeader); // Verifică token-ul
      List<Proba> probe = StreamSupport
              .stream(probaRepo.findAll().spliterator(), false)
              .collect(Collectors.toList());
      return ResponseEntity.ok(probe);
    } catch (RuntimeException e) {
      return ResponseEntity.status(401).body(e.getMessage());
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getProbaById(
          @PathVariable Integer id,
          @RequestHeader("Authorization") String authHeader
  ) {
    try {
      validateAuthHeader(authHeader);
      Optional<Proba> probaOpt = Optional.ofNullable(probaRepo.findOne(id));
      Proba proba = probaOpt.orElseThrow(() -> new RuntimeException("Proba not found with id: " + id));
      return ResponseEntity.ok(proba);
    } catch (RuntimeException e) {
      return ResponseEntity.status(401).body(e.getMessage());
    }
  }

  @PostMapping
  public ResponseEntity<?> createProba(
          @RequestBody Proba proba,
          @RequestHeader("Authorization") String authHeader
  ) {
    try {
      validateAuthHeader(authHeader); // Verifică token-ul
      Proba createdProba = probaRepo.add(proba);
      return ResponseEntity.ok(createdProba);
    } catch (RuntimeException e) {
      return ResponseEntity.status(401).body(e.getMessage());
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateProba(
          @PathVariable Integer id,
          @RequestBody Proba proba,
          @RequestHeader("Authorization") String authHeader
  ) {
    try {
      validateAuthHeader(authHeader); // Verifică token-ul
      Optional<Proba> existingProba = Optional.ofNullable(probaRepo.findOne(id));
      if (existingProba.isEmpty()) {
        throw new RuntimeException("Proba not found for update with id: " + id);
      }
      proba.setId(id);
      Proba updatedProba = probaRepo.update(proba.getId(), proba);
      return ResponseEntity.ok(updatedProba);
    } catch (RuntimeException e) {
      return ResponseEntity.status(401).body(e.getMessage());
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteProba(
          @PathVariable Integer id,
          @RequestHeader("Authorization") String authHeader
  ) {
    try {
      validateAuthHeader(authHeader); // Verifică token-ul
      Optional<Proba> probaOpt = Optional.ofNullable(probaRepo.findOne(id));
      if (probaOpt.isEmpty()) {
        throw new RuntimeException("Proba not found for deletion with id: " + id);
      }
      probaRepo.delete(id);
      return ResponseEntity.ok().build();
    } catch (RuntimeException e) {
      return ResponseEntity.status(401).body(e.getMessage());
    }
  }




}
