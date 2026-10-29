package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AddressBook {

    private final List<Contact> contacts = new ArrayList<>();

    // Add a contact to the address book
    public void addContact(Contact contact) {
        contacts.add(contact);
    }

    // Return all contacts
    public List<Contact> getContacts() {
        return new ArrayList<>(contacts);
    }

    // Write contacts to a file
    public void writeToFile(String fileName) {
        try (ObjectOutputStream output =
                     new ObjectOutputStream(
                             new FileOutputStream(fileName))) {

            output.writeObject(new ArrayList<>(contacts));
            System.out.println("Address book saved successfully.");

        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }

    // Read contacts from a file
    @SuppressWarnings("unchecked")
    public void readFromFile(String fileName) {
        try (ObjectInputStream input =
                     new ObjectInputStream(
                             new FileInputStream(fileName))) {

            Object data = input.readObject();

            if (!(data instanceof ArrayList<?>)) {
                throw new IOException("Invalid address book file.");
            }

            ArrayList<?> savedContacts = (ArrayList<?>) data;

            for (Object item : savedContacts) {
                if (!(item instanceof Contact)) {
                    throw new IOException("Invalid contact data.");
                }
            }

            contacts.clear();

            for (Object item : savedContacts) {
                contacts.add((Contact) item);
            }

            System.out.println("Address book loaded successfully.");

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    // Display all contacts
    public void displayContacts() {
        if (contacts.isEmpty()) {
            System.out.println("No contacts available.");
            return;
        }

        for (Contact contact : contacts) {
            System.out.println("--------------------");
            System.out.println(contact);
        }
    }
}
