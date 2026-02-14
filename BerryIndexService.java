import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

class BerryIndexService {

    //Defines an array list for the berry index
    private ArrayList<Berry> index;

    public BerryIndexService() {
        //Array list constructor
        this.index = new ArrayList<>();
    }

    //Displays a berry
    public void displayBerry(Berry target) {
        String status = target.getFound() ? "[Found]" : "[Missing]";
        System.out.printf("| %-10s | %-15s | %-50s | %-40s |%n", 
            status,
            target.getName(),
            target.getEffect(),
            target.getBiomes());
    }

    //Displays berries in the berry index with displayBerry() method
    public void displayIndex() {
        //Checks if index is empty
        if (index.isEmpty()) {
            System.out.println("Index is empty.");
        } else {
            //Compare each berry in the index to each other to sort alphabetically
            index.sort((b1, b2) -> b1.getName().compareToIgnoreCase(b2.getName()));

            System.out.println("--- Berry Index ---");
            System.out.println("\n" + "=".repeat(128));
            System.out.printf("| %-10s | %-15s | %-50s | %-40s |%n", "STATUS", "BERRY NAME", "EFFECT", "BIOME(S)");
            System.out.println("-".repeat(128));
            //For each berry in the berry index, display the berry using displayBerry()
            for (Berry b : index) {
                displayBerry(b);
            }
            System.out.println("=".repeat(128) + "\n");
        }
        System.out.println("");
    }

    //Displays berries that are found (true) or missing (false), based on the isFound input
    public void displayFilteredIndex(Boolean isFound) {
        //Checks if index is empty
        if (index.isEmpty()) {
            System.out.println("Index is empty.");
        } 

        //Checks which filter has been selected, true for found and false for missing, and display the appropriate header
        String header = isFound ? "--- Found Berries ---" : "--- Missing Berries ---";
        System.out.println(header);
        
        //Sorts the index alphabetically
        index.sort((b1, b2) -> b1.getName().compareToIgnoreCase(b2.getName()));

        System.out.println("\n" + "=".repeat(128));
        System.out.printf("| %-10s | %-15s | %-50s | %-40s |%n", "STATUS", "BERRY NAME", "EFFECT", "BIOME(S)");
        System.out.println("-".repeat(128));
        //Initiates matchFound as false, until berries that match the selected filter are found
        Boolean matchFound = false;
        //For each berry in the index that matches the selected filter, then display using displayBerry() and set matchFound to true
        for (Berry b : index) {
            if (Objects.equals(b.getFound(), isFound)) {
                displayBerry(b);
                matchFound = true;
            }
        }
        System.out.println("=".repeat(128) + "\n");

        //Display if no match is found
        String searchFilter = isFound ? "found" : "missing";
        if (!matchFound) {
            System.out.println("No berries are " + searchFilter + ".\n");
        }
    }

    //Checks for a valid input for yes or no confirmations
    public Boolean validYNCheck(Scanner input, String confirmMessage) {
        String confirm = "";
        //While the string isn't a y and isn't an n, continue to ask for a confirmation
        while (!confirm.equalsIgnoreCase("Y") && !confirm.equalsIgnoreCase("N")) {
            System.out.println(confirmMessage + " Enter Y/N:");
            confirm = input.nextLine().toUpperCase();

            //If input is invalid, ask user to try again
            if (!confirm.equals("Y") && !confirm.equals("N")) {
                System.out.println("Invalid input. Please enter 'Y' or 'N' only.");
            }
        }

        //Return that the response is valid, y returns true and n returns false
        Boolean confirmed = confirm.equalsIgnoreCase("Y");
        return confirmed;
    }

    //Adds a berry to the index and CSV
    public Berry addBerry(Scanner input) {

        System.out.println("Enter berry name:");
        String name = input.nextLine();

        System.out.println("Enter effect:");
        String effect = input.nextLine();

        System.out.println("Enter biome(s):");
        String biomes = input.nextLine().replaceAll(",+", "/");

        String confirmMessage = "Has this berry been found and planted?";
        Boolean found = validYNCheck(input, confirmMessage);

        //Creates a newBerry from the user's input, then add's it to the berry index and CSV
        Berry newBerry =  new Berry(name, effect, biomes, found);
        index.add(newBerry);
        berryToCSV(newBerry);

        //Returns data from the newBerry for the berryToCSV() 
        return newBerry;
    }

    //Updates a berry's information
    public void updateBerry(Scanner input) {
        //Initiates complete as false, for the while loop to continue until true
        Boolean complete = false;
        //While complete is false, ask user for a berry to update
        while (!complete) {

            System.out.println("Which berry would you like to update? Enter berry name: ");
            String name = input.nextLine();
            //Initiates the berry target as null until it is found in the index
            Berry target = null;
            //For each berry in the index, find the name that matches the user's input and break out of the loop as soon as it's found
            for (Berry b : index) {
                if (b.getName().equalsIgnoreCase(name)) {
                    target = b;
                    break;
                }
            }

            //If the target is found and isn't null, then confirm that the correct berry was found
            if (target != null) {
                System.out.println("\n" + "=".repeat(128));
                System.out.printf("| %-10s | %-15s | %-50s | %-40s |%n", "STATUS", "BERRY NAME", "EFFECT", "BIOME(S)");
                System.out.println("-".repeat(128));
                displayBerry(target);
                System.out.println("=".repeat(128));
                String confirmMessage = "Is this the correct berry?";
                Boolean confirm = validYNCheck(input, confirmMessage);
                //If the user confirm's that the correct berry was found, then ask which characteristic the user would like to edit/update
                if (confirm) {
                    //Initiates running as true, to allow a user to continue updating the same berry
                    Boolean running = true;
                    //While running the update service on the same berry is true, then find the characteristic case chosen and update it using the respective Berry class method
                    while (running) {

                        System.out.println("""
                            Which would you like to update?
                            1. Name
                            2. Effect
                            3. Biomes
                            4. Found Status
                            Enter the number for your desired selection: 
                            """);
                        String choice = input.nextLine();

                        switch (choice) {
                            case "1" -> {
                                System.out.println("Current Name: " + target.getName());
                                System.out.println("Enter new name: ");
                                target.setName(input.nextLine());
                            }
                            case "2" -> {
                                System.out.println("Current Effect: " + target.getEffect());
                                System.out.println("Enter new effect: ");
                                target.setEffect(input.nextLine());
                            }
                            case "3" -> {
                                System.out.println("Current Biome(s): " + target.getBiomes());
                                System.out.println("Enter new biome(s): ");
                                String newBiomes = input.nextLine();
                                newBiomes = newBiomes.replaceAll(",", "/");
                                target.setBiomes(newBiomes);
                            }
                            case "4" -> {
                                String status = target.getFound() ? "Found" : "Missing";
                                System.out.println("Current Status: " + status);
                                System.out.println("Has berry been found and planted? Enter Y/N: ");
                                target.setFound(input.nextLine());
                            }
                            default -> System.out.println("Invalid input. Try again.");
                        }

                        //Confirms if the user would like to continue updating the same berry
                        String exitRunningMessage = "Would you like to keep updating " + target.getName() + "?";
                        //Checks for a valid Y or N response to the exitRunningMessage
                        Boolean exitRunningConfirm = validYNCheck(input, exitRunningMessage);
                        //If the user doesn't want to keep editing the same berry, then update the CSV and exit the while loop by declaring running as false
                        if (!exitRunningConfirm) {
                            updateCSV();
                            System.out.println(target.getName() + " has been updated.\n");
                            running = false;
                        }
                    }  
                } else {
                    //If the user says the correct berry wasn't found, then continue out of this loop
                    continue;
                }
            } else {
                System.out.println("Berry doesn't exist.\n");
            }

            //Checks if a user would like to update another berry
            String exitMessage = "Would you like to update another berry?";
            //Checks for a valid Y or N response to the exitMessage
            Boolean exitConfirm = validYNCheck(input, exitMessage);
            //If the user wants to exit, then mark complete as true to leave the while loop
            if (!exitConfirm) {
                System.out.println("Exiting Update Berry Service.\n");
                complete = true;
            } 
        }
    }

    //Adds a berry to the berries.csv file
    public void berryToCSV(Berry berry) {
        //Sets the path to the file
        Path path = Paths.get("berries.csv");
        //Write the string in this format to be saved in the CSV: string, string, string, boolean and new line
        String lines = String.format("%s,%s,%s,%b%n", 
            berry.getName(), 
            berry.getEffect(), 
            berry.getBiomes(), 
            berry.getFound());

        //Writes the iterative bytes from lines into the berries.csv. Create a new file if it doesn't exist, then add to the file
        try {
            Files.write(path, lines.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException ex) {
            System.out.println("Error writing to file: " + ex.getMessage());
        }
    }

    //Updates the berries.csv file
    public void updateCSV() {
        //Sets the path to the file
        Path path = Paths.get("berries.csv");
        //Creates a string list 
        List<String> allLines = new ArrayList<>();

        //For each berry in the index, add it to the allLines list
        for (Berry b : index) {
            String line = String.format("%s,%s,%s,%b",
            b.getName(),
            b.getEffect(),
            b.getBiomes(),
            b.getFound());
            allLines.add(line);
        }

        //Writes each line/berry in the allLines list into the berries.csv. Create a new file if it doesn't exist, then open the file in write mode to allow truncate to overwrite the file
        try {
            Files.write(path, allLines, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.out.println("Error overwriting CSV: " + e.getMessage());
        }
    }

    //Reads lines from the berries.csv into the berry index
    public void readCSV() {
        //Sets the path to the file
        Path path = Paths.get("berries.csv");
        //If the file doesn't exist, then exit this method 
        if (!Files.exists(path)) {
            return;
        }

        //Reads and adds each line from the berries.csv back into the berry index
        try {
            //Saves each line in the CSV into the lines list
            List<String> lines = Files.readAllLines(path);
            //For each line in the lines list, split the strings by commas, store each data item that has been split into the savedBerry variable, and add the savedBerry into the index
            for (String line : lines) {
                String[] data = line.split(",");
                Berry savedBerry = new Berry(data[0], data[1], data[2], Boolean.valueOf(data[3]));
                index.add(savedBerry);
            }
        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
        }
    }

    //Deletes a berry from the index and update the CSV
    public void deleteBerry(Scanner input) {
        //Initiates complete as false for the while to run, until complete is true
        Boolean complete = false;
        //While complete is false, continue to ask the user for which berry to delete
        while (!complete) {
            
            System.out.println("Which berry would you like to delete? Enter berry name: ");
            String name = input.nextLine();

            //Target initiated as null, until it is defined as the berry the user is searching for
            Berry target = null;
            //For each berry in index, find the name that matches what the user is searching for
            for (Berry b : index) {
                //If the berry's name matches what the user is searching for, then make the target equal to that
                if (b.getName().equalsIgnoreCase(name)) {
                    target = b;
                    break;
                }
            }

            //If the target isn't null and is found, then ask the user for confirmation
            if (target != null) {
                System.out.println("\n" + "=".repeat(128));
                System.out.printf("| %-10s | %-15s | %-50s | %-40s |%n", "STATUS", "BERRY NAME", "EFFECT", "BIOME(S)");
                System.out.println("-".repeat(128));
                displayBerry(target);
                System.out.println("=".repeat(128));
                String confirmMessage = "Is this the correct berry?";
                //Runs a check for a valid Y or N response to the confirmMessage
                Boolean confirm = validYNCheck(input, confirmMessage);
                //If the confirm check was valid and true, then remove the target berry from the index and update the CSV
                if (confirm) {
                        index.remove(target);
                        updateCSV();
                        System.out.println(target.getName() + " has been deleted.\n");
                } else {
                    System.out.println("Deletion cancelled.");
                }
            } else {
                System.out.println("Berry doesn't exist.\n");
            }

            String exitMessage = "Would you like to delete another berry?";
            //Runs a check for a valid Y or N response to the exitMessage
            Boolean exitConfirm = validYNCheck(input, exitMessage);
            //If user doesn't want to continue and returned a no response (false), then make complete true and exit out of the while loop
            if (!exitConfirm) {
                System.out.println("Exiting Delete Berry Service.\n");
                complete = true;
            } 
        }
    }

    //Displays the amount of berries index, found, and missing
    public String displayStats() {
        //Define total as size of the index array list
        int total = index.size();
        //Initiate foundCount and missingCount as zero
        int foundCount = 0;
        int missingCount = 0;
        //For each berry in the index, if getFound is true, then increase the foundCount by one. Else if getFound is false, then increase missingCount by one.
        for (Berry b : index) {
            if (b.getFound()) {
                foundCount++;
            } else {
                missingCount++;
            }
        } 

        //Checks that found and missing equals the total
        if (foundCount + missingCount != total) {
            System.out.println("Calculation error has occurred. Discrepancy between found, missing, and total berries.");
        }

        //Returns the calculation results
        return String.format("%d Total | %d Found | %d Missing", total, foundCount, missingCount);
    }
}