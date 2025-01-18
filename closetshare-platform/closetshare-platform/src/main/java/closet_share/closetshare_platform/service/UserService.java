package closet_share.closetshare_platform.service;

import closet_share.closetshare_platform.domain.Borrow;
import closet_share.closetshare_platform.domain.InterestedProducts;
import closet_share.closetshare_platform.domain.Item;
import closet_share.closetshare_platform.domain.User;
import closet_share.closetshare_platform.model.Gender;
import closet_share.closetshare_platform.model.Role;
import closet_share.closetshare_platform.model.UserDTO;
import closet_share.closetshare_platform.repos.BorrowRepository;
import closet_share.closetshare_platform.repos.InterestedProductsRepository;
import closet_share.closetshare_platform.repos.ItemRepository;
import closet_share.closetshare_platform.repos.UserRepository;
import closet_share.closetshare_platform.util.NotFoundException;
import closet_share.closetshare_platform.util.ReferencedWarning;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BorrowRepository borrowRepository;
    private final InterestedProductsRepository interestedProductsRepository;

    public List<UserDTO> findAll() {
        final List<User> users = userRepository.findAll(Sort.by("seqId"));
        return users.stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    public UserDTO get(final Long seqId) {
        return userRepository.findById(seqId)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UserDTO create(String userPhoneNumber, String userPassword, String userName,
                          String userId, String address, String subAddress, Role role, Gender gender) {
        UserDTO userDTO = new UserDTO();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    }

    public Long create(final UserDTO userDTO) {
        final User user = new User();
        mapToEntity(userDTO, user);
        return userRepository.save(user).getSeqId();
    }

    public void update(final Long seqId, final UserDTO userDTO) {
        final User user = userRepository.findById(seqId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userDTO, user);
        userRepository.save(user);
    }

    public void delete(final Long seqId) {
        userRepository.deleteById(seqId);
    }

    private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setSeqId(user.getSeqId());
        userDTO.setUserId(user.getUserId());
        userDTO.setUserPassword(user.getUserPassword());
        userDTO.setRole(user.getRole());
        userDTO.setUserName(user.getUserName());
        userDTO.setUserPhoneNumber(user.getUserPhoneNumber());
        userDTO.setAddress(user.getAddress());
        userDTO.setSubAddress(user.getSubAddress());
        userDTO.setGender(user.getGender());
        return userDTO;
    }

    private User mapToEntity(final UserDTO userDTO, final User user) {
        user.setUserId(userDTO.getUserId());
        user.setUserPassword(userDTO.getUserPassword());
        user.setRole(userDTO.getRole());
        user.setUserName(userDTO.getUserName());
        user.setUserPhoneNumber(userDTO.getUserPhoneNumber());
        user.setAddress(userDTO.getAddress());
        user.setSubAddress(userDTO.getSubAddress());
        user.setGender(userDTO.getGender());
        return user;
    }

    public boolean userIdExists(final String userId) {
        return userRepository.existsByUserIdIgnoreCase(userId);
    }

    public boolean userPhoneNumberExists(final String userPhoneNumber) {
        return userRepository.existsByUserPhoneNumber(userPhoneNumber);
    }

    public ReferencedWarning getReferencedWarning(final Long seqId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final User user = userRepository.findById(seqId)
                .orElseThrow(NotFoundException::new);
        final Item userIdItem = itemRepository.findFirstByUserId(user);
        if (userIdItem != null) {
            referencedWarning.setKey("user.item.userId.referenced");
            referencedWarning.addParam(userIdItem.getSeqId());
            return referencedWarning;
        }
        final Borrow userIdBorrow = borrowRepository.findFirstByUserId(user);
        if (userIdBorrow != null) {
            referencedWarning.setKey("user.borrow.userId.referenced");
            referencedWarning.addParam(userIdBorrow.getSeqId());
            return referencedWarning;
        }
        final InterestedProducts userIdInterestedProducts = interestedProductsRepository.findFirstByUserId(user);
        if (userIdInterestedProducts != null) {
            referencedWarning.setKey("user.interestedProducts.userId.referenced");
            referencedWarning.addParam(userIdInterestedProducts.getSeqId());
            return referencedWarning;
        }
        return null;
    }

}
