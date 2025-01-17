package closet_share.closetshare_platform.model;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import closet_share.closetshare_platform.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import org.springframework.web.servlet.HandlerMapping;


/**
 * Validate that the userPhoneNumber value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = UserUserPhoneNumberUnique.UserUserPhoneNumberUniqueValidator.class
)
public @interface UserUserPhoneNumberUnique {

    String message() default "{Exists.user.userPhoneNumber}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class UserUserPhoneNumberUniqueValidator implements ConstraintValidator<UserUserPhoneNumberUnique, Integer> {

        private final UserService userService;
        private final HttpServletRequest request;

        public UserUserPhoneNumberUniqueValidator(final UserService userService,
                final HttpServletRequest request) {
            this.userService = userService;
            this.request = request;
        }

        @Override
        public boolean isValid(final Integer value, final ConstraintValidatorContext cvContext) {
            if (value == null) {
                // no value present
                return true;
            }
            @SuppressWarnings("unchecked") final Map<String, String> pathVariables =
                    ((Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
            final String currentId = pathVariables.get("seqId");
            if (currentId != null && value.equals(userService.get(Long.parseLong(currentId)).getUserPhoneNumber())) {
                // value hasn't changed
                return true;
            }
            return !userService.userPhoneNumberExists(value);
        }

    }

}
