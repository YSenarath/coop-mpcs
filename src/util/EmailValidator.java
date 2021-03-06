package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {
	private static final String EMAIL_PATTERN = 
		"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        
	private Pattern pattern;
	private Matcher matcher;
        private static EmailValidator obj;
        
        public static EmailValidator instance() {
            if (obj == null) obj = new EmailValidator();
            return obj;
        }

	private EmailValidator() {
		pattern = Pattern.compile(EMAIL_PATTERN);
	}
        
	public boolean validate(final String hex) {
		matcher = pattern.matcher(hex);
		return matcher.matches();
	}
}
