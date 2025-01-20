package closet_share.closetshare_platform.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateForm {
    @Size(min = 2, max = 25, message = "이거 왜 안뜸")
    private String userId;

    private String userPassword;

    private String userPassword2;

    private String userName;

    private String userPhoneNumber;

    private Gender gender;
}