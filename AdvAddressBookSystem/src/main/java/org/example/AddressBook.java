
package org.example;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class AddressBook {
    private final List<Contact> contacts = new ArrayList<>();
    // Add a new contact
    public void addContact(Contact contact) {
        contacts.add(contact);
        System.out.println("Contact added successfully.");
    }
    // Display all contacts
    public void displayContacts() {
        if (contacts.isEmpty()) {
            System.out.println("Address Book is empty.");
            return;
        }

        for (Contact contact : contacts) {
            System.out.println("--------------------");
            System.out.println(contact);
        }
    }

    // Edit an existing contact using the person's name
    public void editContact(String firstName, Scanner scanner) {
        for (Contact contact : contacts) {
            if (contact.getFirstName().equalsIgnoreCase(firstName)) {

                System.out.println("Contact found. Enter updated details:");

                System.out.print("Enter Last Name: ");
                String lastName = scanner.nextLine();

                System.out.print("Enter Address: ");
                String address = scanner.nextLine();

                System.out.print("Enter City: ");
                String city = scanner.nextLine();

                System.out.print("Enter State: ");
                String state = scanner.nextLine();

                System.out.print("Enter ZIP: ");
                String zip = scanner.nextLine();

                System.out.print("Enter Phone Number: ");
                String phoneNumber = scanner.nextLine();

                System.out.print("Enter Email: ");
                String email = scanner.nextLine();

                contact.updateDetails(
                        lastName, address, city, state, zip,
                        phoneNumber, email
                );

                System.out.println("Contact updated successfully.");
                return;
            }
        }

        System.out.println("Contact not found.");
    }

    public List<Contact> getContacts() {
        return new ArrayList<>(contacts);
    }
}
