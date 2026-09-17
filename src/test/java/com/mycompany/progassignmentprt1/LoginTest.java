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
    public void testSomeMethod() {
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
         assertFalse(result);
    }
    //test cellphone number
    @Test
    public void testCellphoneNumber(){
        Login user = new Login();
        
        boolean result = user.CheckPhoneNumber("+278966553");
        assertTrue(result);
    }
}
