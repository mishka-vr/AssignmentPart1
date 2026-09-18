/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.progassignmentprt1;

/**
 *
 * @author Student
 */
public class Login {
    
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String cellNumber;
    
    private static final int cell_max = 10;
    private static final String cell_regex = "^\\+\\d+$";
    private static final String password_regex = "^(?=.*[A_Z](?=.*[!@#$%^&*(),.?\" :{}|<>_\\-]).{9,}$";
    
public Login (){
    
   }   

//check if the username fits criteria
public boolean checkUserName(String username){
            if (username.contains("_")&& username.length()>=5){
                return true;
            }else{
                return false;
            }              
}
//checking if the user's password suits the cirteria
public boolean CheckPassword(String password){
    if(password.length()<8){
        return false;
    }if(!password.matches(".*[A-Z].*")){
        return false;
    }if(!password.matches(".*[0-9].*")){
        return false;
    }if(!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")){
        return false;
    }
    return true;
}
//check if user's number is a valid cellphone number
public boolean CheckPhoneNumber(String cellNumber){
    return cellNumber.startsWith ("+27")&& cellNumber.length()==10;
}

public String RegisterUser(String username, String password, String cellNumber){
    if(!checkUserName(username)){
        return "Username does not suit the fixed criteria.";
    }else if(!CheckPassword(password)){
        return "Username succesfully created";
    }else if(!CheckPhoneNumber(cellNumber)){
        return "Cellphone number is not a valid number";
    }else{
        return "Your registration was succesfull";
    }   
 }  
//login
 public boolean UserLogin(String username,String password,String cellNumber){
     return this.username.equals(username)&& this.password.equals(password);
}
 public String returnUserLoginStatus(boolean LoginSuscessful, boolean LoginUnsuccesfull){
     if(LoginSuscessful){
         return "Welcome back " +firstName + " it is great to see you";
     }else if(LoginUnsuccesfull){
         return "Username or password incorrect";
     }else{
         return "Login unsucessfull";
     }
 }

}

     



