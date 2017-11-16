package popkovanton.improvetest.validator;


import java.util.Arrays;
import java.util.List;

public class PhoneValidator {

    public PhoneValidator() {
    }

    public String validate (String phone) {
        StringBuffer sb = new StringBuffer(phone);
        if(phone.length() == 11) {
            sb.insert(9," ");
            sb.insert(7," ");
            sb.insert(4,") ");
            sb.insert(1," (");
            sb.insert(0,"+");
            return sb.toString();
        } else if(phone.length() == 12) {
            sb.insert(10," ");
            sb.insert(8," ");
            sb.insert(5,") ");
            sb.insert(2," (");
            sb.insert(0,"+");
            return sb.toString();
        } else return  sb.toString();

    }
}
