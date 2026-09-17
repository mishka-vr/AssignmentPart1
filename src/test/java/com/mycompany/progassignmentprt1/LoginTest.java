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

    @Test
    public void testSomeMethod() {
        Login user = new Login();
        
        boolean result = user.checkUserName("kyl_1");
        assertTrue(result);
    }
    public void testUsernameIncorrectlyFormat(){
         Login user = new Login();
         
         boolean result = user.checkUserName("Kyle!!!!!");
         assertFalse(result);
    }
}
