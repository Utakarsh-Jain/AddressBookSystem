package com.addressbook;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AddressBook {
    private final String name;
    private final List<Contact> contacts = new ArrayList<>();

    public AddressBook(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    public List<Contact> getContacts() {
        return new ArrayList<>(contacts);
    }

    public boolean addContact(Contact contact) {
        boolean duplicate = contacts.stream().anyMatch(existing -> existing.equals(contact));
        if (duplicate) {
            System.out.println("Duplicate contact: a person with this name already exists in this Address Book.");
            return false;
        }
        contacts.add(contact);
        System.out.println("Contact added successfully to '" + name + "'.");
        return true;
    }

    public void addMultipleContacts(List<Contact> newContacts) {
        int added = 0;
        for (Contact contact : newContacts) {
            if (contacts.stream().noneMatch(existing -> existing.equals(contact))) {
                contacts.add(contact);
                added++;
            } else {
                System.out.println("Skipped duplicate: " + contact.getFirstName() + " " + contact.getLastName());
            }
        }
        System.out.println(added + " contact(s) added to '" + name + "'.");
    }

    public Optional<Contact> findContact(String firstName, String lastName) {
        return contacts.stream()
                .filter(c -> c.getFirstName().equalsIgnoreCase(firstName.trim())
                        && c.getLastName().equalsIgnoreCase(lastName.trim()))
                .findFirst();
    }

    public boolean editContact(String firstName, String lastName, Contact updated) {
        Optional<Contact> match = findContact(firstName, lastName);
        if (!match.isPresent()) return false;
        Contact existing = match.get();
        boolean nameTaken = contacts.stream()
                .filter(c -> c != existing)
                .anyMatch(c -> c.equals(updated));
        if (nameTaken) {
            System.out.println("Cannot update: another contact with that name already exists.");
            return false;
        }
        existing.updateDetails(updated.getFirstName(), updated.getLastName(), updated.getAddress(),
                updated.getCity(), updated.getState(), updated.getZip(),
                updated.getPhoneNumber(), updated.getEmail());
        return true;
    }

    public boolean deleteContact(String firstName, String lastName) {
        Optional<Contact> match = findContact(firstName, lastName);
        return match.isPresent() && contacts.remove(match.get());
    }

    public void displayContacts() {
        if (contacts.isEmpty()) {
            System.out.println("Address Book '" + name + "' is empty.");
            return;
        }
        contacts.forEach(contact -> {
            System.out.println("------------------------------");
            System.out.println(contact);
        });
    }

    public List<Contact> findByCity(String city) {
        return contacts.stream().filter(c -> c.getCity().equalsIgnoreCase(city.trim()))
                .collect(Collectors.toList());
    }

    public List<Contact> findByState(String state) {
        return contacts.stream().filter(c -> c.getState().equalsIgnoreCase(state.trim()))
                .collect(Collectors.toList());
    }

    public long countByCity(String city) { return findByCity(city).size(); }
    public long countByState(String state) { return findByState(state).size(); }

    public List<Contact> sortedByName() {
        return contacts.stream()
                .sorted(Comparator.comparing(Contact::getFirstName, String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(Contact::getLastName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    public List<Contact> sortedByCity() {
        return contacts.stream().sorted(Comparator.comparing(Contact::getCity, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    public List<Contact> sortedByState() {
        return contacts.stream().sorted(Comparator.comparing(Contact::getState, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    public List<Contact> sortedByZip() {
        return contacts.stream().sorted(Comparator.comparing(Contact::getZip, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    public void replaceContacts(List<Contact> loadedContacts) {
        contacts.clear();
        addMultipleContacts(loadedContacts);
    }
}
