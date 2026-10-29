
package org.example;
import java.util.ArrayList;
import java.util.List;
public class AddressBook {
    private final List<Contact> contacts = new ArrayList<>();
    // Add a new contact to the address book
    public void addContact(Contact contact) {
        contacts.add(contact);
        System.out.println("Contact added successfully.");
    }
    // Return all contacts
    public List<Contact> getContacts() {
        return new ArrayList<>(contacts);
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
}
