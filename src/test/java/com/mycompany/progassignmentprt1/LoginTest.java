/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.progassignmentprt1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Student
 */
public class LoginTest {
    
    public LoginTest() {
    }
    //username test
    @Test
    public void testUsernameCorrectFormat() {
        Login user = new Login();
        
        boolean result = user.checkUserName("kyl_1");
        assertTrue(result);
    }
    @Test
    public void testUsernameIncorrectlyFormat(){
         Login user = new Login();
         
         boolean result = user.checkUserName("Kyle!!!!!");
         assertFalse(result);
    }
    //password test
    @Test
    public void testpasswordMeetsCriteria(){
         Login user = new Login();
         
         boolean result = user.CheckPassword("Ch&&sec@keg9");
         assertTrue(result);
    }
    @Test
    public void testpasswordDoesntMeetCriteria(){
         Login user = new Login();
         
         boolean result = user.CheckPassword("password");
         assertFalse(result);
    }
    //test cellphone number
    @Test
    public void testCellphoneNumberCorrectlyFormatted(){
        Login user = new Login();
        
        boolean result = user.CheckPhoneNumber("+278966553");
        assertTrue(result);
    }
    @Test
     public void testCellphoneNumberInorrectlyFormatted(){
        Login user = new Login();
        
        boolean result = user.CheckPhoneNumber("08966553");
        assertFalse(result);
    }
     //Login tests
     @Test
     public void testUserLoginSucessfull(){
         Login user = new Login();
         
         user.RegisterUser("kyl_1", "Ch&&sec@keg9", "+278966553");
         boolean result = user.UserLogin("kyl_1","Ch&&sec@keg9", "+278966553");
         assertTrue(result);
     }
     @Test
     public void testUserLoginUnsucessfull(){
         Login user = new Login();
         
         boolean result = user.UserLogin("kyl_1","Ch&&sec@keg9", "+278966553");
         assertTrue(result);
     }
}
