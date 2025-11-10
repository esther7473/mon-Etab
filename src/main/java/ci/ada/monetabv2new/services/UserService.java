package ci.ada.monetabv2new.services;

import ci.ada.monetabv2new.models.UserAccountEntity;
import ci.ada.monetabv2new.repositories.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserAccountRepository userRepo;

    public UserAccountEntity save(UserAccountEntity userAccount){
        return userRepo.save(userAccount);
    }
    public ResponseEntity<?> getUserById(long userId) {
        try {
            if (userRepo.findById(userId).isPresent()) {
                return new ResponseEntity<>(userRepo.findById(userId).get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e){
            return new ResponseEntity<>("Internal error!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<UserAccountEntity> getAllUsers() {
        return userRepo.findAll();
    }

    public ResponseEntity<?> deleteUser(long userId) {
        if (userRepo.findById(userId).isPresent()) {
            userRepo.deleteById(userId);
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        }

        return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }

}