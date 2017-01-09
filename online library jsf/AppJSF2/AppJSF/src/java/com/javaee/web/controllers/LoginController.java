
package com.javaee.web.controllers;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class LoginController implements Serializable {

    public LoginController() {
    }
    
    public String login(){
    return "books";
    }
    
}
