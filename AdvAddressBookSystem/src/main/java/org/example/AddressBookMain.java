package com.addressbook;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class AddressBookMain {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Map<String, AddressBook> ADDRESS_BOOKS = new HashMap<>();
    private static final String DEFAULT_FILE = "addressbook-data.txt";

    public static void main(String[] args) {
        System.out.println("Welcome to Address Book Program");
        while (true) {
            printMenu();
            int choice = readInt("Enter your choice: ");
            switch (choice) {
                case 1: createAddressBook(); break;
                case 2: addContact(); break;
                case 3: addMultipleContacts(); break;
                case 4: displayContacts(); break;
                case 5: editContact(); break;
                case 6: deleteContact(); break;
                case 7: searchAcrossAddressBooks(); break;
                case 8: viewPersonsByCityOrState(); break;
                case 9: countPersonsByCityOrState(); break;
                case 10: sortContactsByName(); break;
                case 11: sortContactsByCityStateOrZip(); break;
                case 12: saveToFile(); break;
                case 13: loadFromFile(); break;
                case 14: listAddressBooks(); break;
                case 15:
                    System.out.println("Exiting Address Book System.");
                    SCANNER.close();
                    return;
                default: System.out.println("Invalid choice. Please choose 1-15.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n========== ADDRESS BOOK MENU ==========");
        System.out.println("1. Create Address Book");
        System.out.println("2. Add Contact");
        System.out.println("3. Add Multiple Contacts");
        System.out.println("4. Display Contacts");
        System.out.println("5. Edit Contact");
        System.out.println("6. Delete Contact");
        System.out.println("7. Search Persons Across Address Books (City/State)");
        System.out.println("8. View Persons by City or State");
        System.out.println("9. Count Persons by City or State");
        System.out.println("10. Sort Contacts by Name");
        System.out.println("11. Sort Contacts by City, State or ZIP");
        System.out.println("12. Save Address Books to File");
        System.out.println("13. Load Address Books from File");
        System.out.println("14. List Address Books");
        System.out.println("15. Exit");
    }

    private static void createAddressBook() {
        String name = readText("Enter new Address Book name: ").trim();
        if (name.isEmpty()) {
            System.out.println("Address Book name cannot be empty.");
        } else if (ADDRESS_BOOKS.containsKey(key(name))) {
            System.out.println("An Address Book with that name already exists.");
        } else {
            ADDRESS_BOOKS.put(key(name), new AddressBook(name));
            System.out.println("Address Book '" + name + "' created successfully.");
        }
    }

    private static void addContact() {
        AddressBook book = chooseAddressBook();
        if (book != null) book.addContact(readContact());
    }

    private static void addMultipleContacts() {
        AddressBook book = chooseAddressBook();
        if (book == null) return;
        int count = readInt("How many contacts do you want to add? ");
        if (count <= 0) {
            System.out.println("Enter a number greater than zero.");
            return;
        }
        List<Contact> contacts = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            System.out.println("\nEnter details for contact " + (i + 1) + ":");
            contacts.add(readContact());
        }
        book.addMultipleContacts(contacts);
    }

    private static void displayContacts() {
        AddressBook book = chooseAddressBook();
        if (book != null) book.displayContacts();
    }

    private static void editContact() {
        AddressBook book = chooseAddressBook();
        if (book == null) return;
        String first = readText("Enter current first name: ");
        String last = readText("Enter current last name: ");
        Contact updated = readContact();
        if (book.editContact(first, last, updated)) System.out.println("Contact updated successfully.");
        else System.out.println("Contact not found or updated name is a duplicate.");
    }

    private static void deleteContact() {
        AddressBook book = chooseAddressBook();
        if (book == null) return;
        String first = readText("Enter first name of contact to delete: ");
        String last = readText("Enter last name of contact to delete: ");
        if (book.deleteContact(first, last)) System.out.println("Contact deleted successfully.");
        else System.out.println("Contact not found.");
    }

    private static void searchAcrossAddressBooks() {
        if (ADDRESS_BOOKS.isEmpty()) { System.out.println("Create an Address Book first."); return; }
        System.out.println("1. Search by City\n2. Search by State");
        int option = readInt("Choose search type: ");
        String value = readText(option == 1 ? "Enter city: " : "Enter state: ");
        List<String> matches = new ArrayList<>();
        for (AddressBook book : ADDRESS_BOOKS.values()) {
            List<Contact> found;
            if (option == 1) found = book.findByCity(value);
            else if (option == 2) found = book.findByState(value);
            else { System.out.println("Invalid search type."); return; }
            for (Contact contact : found) {
                matches.add("Address Book: " + book.getName() + "\n" + contact);
            }
        }
        printResults(matches);
    }

    private static void viewPersonsByCityOrState() {
        if (ADDRESS_BOOKS.isEmpty()) { System.out.println("Create an Address Book first."); return; }
        Map<String, List<String>> cityIndex = buildIndex(true);
        Map<String, List<String>> stateIndex = buildIndex(false);
        System.out.println("1. View by City\n2. View by State");
        int option = readInt("Choose option: ");
        String value = readText(option == 1 ? "Enter city: " : "Enter state: ");
        Map<String, List<String>> index = option == 1 ? cityIndex : stateIndex;
        if (option != 1 && option != 2) { System.out.println("Invalid option."); return; }
        List<String> result = index.get(key(value));
        if (result == null || result.isEmpty()) System.out.println("No contacts found.");
        else result.forEach(System.out::println);
    }

    private static void countPersonsByCityOrState() {
        Map<String, Long> cityCounts = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        Map<String, Long> stateCounts = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        for (AddressBook book : ADDRESS_BOOKS.values()) {
            book.getContacts().forEach(c -> {
                cityCounts.merge(c.getCity(), 1L, Long::sum);
                stateCounts.merge(c.getState(), 1L, Long::sum);
            });
        }
        System.out.println("1. Counts by City\n2. Counts by State");
        int option = readInt("Choose option: ");
        if (option == 1) cityCounts.forEach((k, v) -> System.out.println(k + " : " + v));
        else if (option == 2) stateCounts.forEach((k, v) -> System.out.println(k + " : " + v));
        else System.out.println("Invalid option.");
    }

    private static void sortContactsByName() {
        AddressBook book = chooseAddressBook();
        if (book != null) printContacts(book.sortedByName());
    }

    private static void sortContactsByCityStateOrZip() {
        AddressBook book = chooseAddressBook();
        if (book == null) return;
        System.out.println("1. Sort by City\n2. Sort by State\n3. Sort by ZIP");
        int option = readInt("Choose sorting option: ");
        switch (option) {
            case 1: printContacts(book.sortedByCity()); break;
            case 2: printContacts(book.sortedByState()); break;
            case 3: printContacts(book.sortedByZip()); break;
            default: System.out.println("Invalid sorting option.");
        }
    }

    private static void saveToFile() {
        String filename = readText("Enter file path (blank for " + DEFAULT_FILE + "): ").trim();
        if (filename.isEmpty()) filename = DEFAULT_FILE;
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filename), StandardCharsets.UTF_8)) {
            for (AddressBook book : ADDRESS_BOOKS.values()) {
                writer.write("BOOK|" + encode(book.getName()));
                writer.newLine();
                for (Contact c : book.getContacts()) {
                    writer.write("CONTACT|" + encode(book.getName()) + "|"
                            + encode(c.getFirstName()) + "|" + encode(c.getLastName()) + "|"
                            + encode(c.getAddress()) + "|" + encode(c.getCity()) + "|"
                            + encode(c.getState()) + "|" + encode(c.getZip()) + "|"
                            + encode(c.getPhoneNumber()) + "|" + encode(c.getEmail()));
                    writer.newLine();
                }
            }
            System.out.println("Address Books saved to " + filename);
        } catch (IOException e) {
            System.out.println("Could not save file: " + e.getMessage());
        }
    }

    private static void loadFromFile() {
        String filename = readText("Enter file path (blank for " + DEFAULT_FILE + "): ").trim();
        if (filename.isEmpty()) filename = DEFAULT_FILE;
        Map<String, List<Contact>> loaded = new HashMap<>();
        Map<String, String> originalNames = new HashMap<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filename), StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", -1);
                if (parts.length == 2 && parts[0].equals("BOOK")) {
                    String name = decode(parts[1]);
                    originalNames.put(key(name), name);
                    loaded.putIfAbsent(key(name), new ArrayList<>());
                } else if (parts.length == 10 && parts[0].equals("CONTACT")) {
                    String bookName = decode(parts[1]);
                    originalNames.put(key(bookName), bookName);
                    loaded.computeIfAbsent(key(bookName), k -> new ArrayList<>()).add(
                            new Contact(decode(parts[2]), decode(parts[3]), decode(parts[4]),
                                    decode(parts[5]), decode(parts[6]), decode(parts[7]),
                                    decode(parts[8]), decode(parts[9])));
                }
            }
            ADDRESS_BOOKS.clear();
            for (Map.Entry<String, List<Contact>> entry : loaded.entrySet()) {
                String name = originalNames.get(entry.getKey());
                AddressBook book = new AddressBook(name);
                book.replaceContacts(entry.getValue());
                ADDRESS_BOOKS.put(entry.getKey(), book);
            }
            System.out.println("Address Books loaded from " + filename);
        } catch (IOException e) {
            System.out.println("Could not load file: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("File contains invalid encoded data.");
        }
    }

    private static void listAddressBooks() {
        if (ADDRESS_BOOKS.isEmpty()) { System.out.println("No Address Books created yet."); return; }
        ADDRESS_BOOKS.values().stream().map(AddressBook::getName)
                .sorted(String.CASE_INSENSITIVE_ORDER).forEach(System.out::println);
    }

    private static AddressBook chooseAddressBook() {
        if (ADDRESS_BOOKS.isEmpty()) { System.out.println("No Address Books available. Create one first."); return null; }
        listAddressBooks();
        String name = readText("Enter Address Book name: ").trim();
        AddressBook book = ADDRESS_BOOKS.get(key(name));
        if (book == null) System.out.println("Address Book not found.");
        return book;
    }

    private static Contact readContact() {
        String first = readText("First Name: ");
        String last = readText("Last Name: ");
        String address = readText("Address: ");
        String city = readText("City: ");
        String state = readText("State: ");
        String zip = readText("ZIP: ");
        String phone = readText("Phone Number: ");
        String email = readText("Email: ");
        return new Contact(first, last, address, city, state, zip, phone, email);
    }

    private static Map<String, List<String>> buildIndex(boolean byCity) {
        Map<String, List<String>> index = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        for (AddressBook book : ADDRESS_BOOKS.values()) {
            for (Contact c : book.getContacts()) {
                String location = byCity ? c.getCity() : c.getState();
                String line = "Address Book: " + book.getName() + " | "
                        + c.getFirstName() + " " + c.getLastName() + " | "
                        + c.getCity() + ", " + c.getState();
                index.computeIfAbsent(key(location), k -> new ArrayList<>()).add(line);
            }
        }
        return index;
    }

    private static void printContacts(List<Contact> contacts) {
        if (contacts.isEmpty()) { System.out.println("No contacts found."); return; }
        contacts.forEach(c -> {
            System.out.println("------------------------------");
            System.out.println(c);
        });
    }

    private static void printResults(List<String> results) {
        if (results.isEmpty()) System.out.println("No matching contacts found.");
        else results.forEach(result -> System.out.println("------------------------------\n" + result));
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = SCANNER.nextLine();
            try { return Integer.parseInt(input.trim()); }
            catch (NumberFormatException e) { System.out.println("Please enter a valid whole number."); }
        }
    }

    private static String readText(String prompt) {
        System.out.print(prompt);
        return SCANNER.nextLine();
    }

    private static String key(String value) {
        return value == null ? "" : value.trim().toLowerCase();
    }

    private static String encode(String value) {
        return Base64.getEncoder().encodeToString((value == null ? "" : value).getBytes(StandardCharsets.UTF_8));
    }

    private static String decode(String value) {
        return new String(Base64.getDecoder().decode(value), StandardCharsets.UTF_8);
    }
}
