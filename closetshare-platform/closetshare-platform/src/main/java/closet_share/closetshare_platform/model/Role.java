package closet_share.closetshare_platform.model;


import lombok.Getter;

@Getter
public enum Role {

    ADMIN("ROLE_ADMIN"), MEMBER("ROLE_USER");

    Role(String value) {
        this.value = value;
    }

    private String value;
}
