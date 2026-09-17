/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.progassignmentprt1;

import java.util.Scanner;

/**
 *
 * @author Student
 */
public class ProgAssignmentPrt1 {
    
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Login obj= new Login();
        
        System.out.println("Enter your first name");
        String firstName = input.nextLine(); 
        
        System.out.println("Enter last name");
        String lastName = input.nextLine();        
        
        System.out.println("Enter a username (must contain _ and be <= 5 characters):");
        String username = input.nextLine();
        obj.checkUserName(username);
        
        System.out.println("Enter a password (*+ characters, capiyal letter,number, special character):");
         String password = input.nextLine();
        obj.CheckPassword(password);
         
        String results = obj.RegisterUser(username, password, username);
        System.out.println(results);
    }
       
}

