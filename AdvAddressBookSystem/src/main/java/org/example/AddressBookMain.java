package org.example;
import java.util.Scanner;
public class AddressBookMain {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        AddressBook addressBook = new AddressBook();

        String fileName = "AddressBook.dat";

        while (true) {
            System.out.println("\n===== Address Book =====");
            System.out.println("1. Add Contact");
            System.out.println("2. Display Contacts");
            System.out.println("3. Save Contacts to File");
            System.out.println("4. Read Contacts from File");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice;

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("First Name: ");
                    String firstName = scanner.nextLine();

                    System.out.print("Last Name: ");
                    String lastName = scanner.nextLine();

                    System.out.print("Address: ");
                    String address = scanner.nextLine();

                    System.out.print("City: ");
                    String city = scanner.nextLine();

                    System.out.print("State: ");
                    String state = scanner.nextLine();

                    System.out.print("ZIP: ");
                    String zip = scanner.nextLine();

                    System.out.print("Phone Number: ");
                    String phone = scanner.nextLine();

                    System.out.print("Email: ");
                    String email = scanner.nextLine();

                    Contact contact = new Contact(
                            firstName, lastName, address, city,
                            state, zip, phone, email
                    );

                    addressBook.addContact(contact);
                    System.out.println("Contact added successfully.");
                    break;

                case 2:
                    addressBook.displayContacts();
                    break;

                case 3:
                    addressBook.writeToFile(fileName);
                    break;

                case 4:
                    addressBook.readFromFile(fileName);
                    break;

                case 5:
                    System.out.println("Exiting Address Book.");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
