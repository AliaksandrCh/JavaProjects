
package com.javaee.web.validators;

import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("com.javaee.web.validators.LoginValidator")
public class LoginValidator implements Validator{

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        
        ResourceBundle bundle = ResourceBundle.getBundle("com.javaee.web.nls.messages", FacesContext.getCurrentInstance().getViewRoot().getLocale());
        
        try {
        
        if(value.toString().length()<3){
            throw new IllegalArgumentException(bundle.getString("login_lenght_error"));
        }
        if(!Character.isLetter(value.toString().charAt(0))){
            throw new IllegalArgumentException(bundle.getString("login_letter_error"));
        }
        if (getBusyName().contains(value.toString())){
            throw new IllegalArgumentException(bundle.getString("busy_name"));
        }
        } catch (IllegalArgumentException e) {
            FacesMessage message = new FacesMessage(e.getMessage());
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message); //To change body of generated methods, choose Tools | Templates.
        }  
        
    }
    
    private ArrayList<String> getBusyName(){
    ArrayList<String> busyName = new ArrayList<String>();
    busyName.add("username");
    busyName.add("login");
    return busyName;
    }
}
