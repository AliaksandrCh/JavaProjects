
package com.javaee.web.controllers;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean
@RequestScoped
public class ExitController implements Serializable {

    public ExitController() {
    }
    
    public String exit() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "exit";
    }
    
    
}
