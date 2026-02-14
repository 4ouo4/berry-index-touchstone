//The Minecraft modpack "Cobblemon" allows players to farm various berries for bait seasoning
//Users can choose to add, update, or delete a berry entry
//Entries will be sorted alphabetically and can be filtered by found or missing

import java.util.Scanner;

//Main function, the point of entry
class BerryIndexProgram {

    public static void main(String[] args) {
        //Initiates the BerryIndexService and Scanner for use
        BerryIndexService berryIndexService = new BerryIndexService();
        Scanner input = new Scanner(System.in);

        //Initiates a running variable as true to keep the while loop running, until running becomes false with the exit option
        Boolean running = true;
        //Runs the service that saves data from the CSV into the berry index
        berryIndexService.readCSV();
        //Loop while running is true
        while (running) {
            System.out.println("""
                            If you would like to
                            1. View indexed berries. Enter: 'view_berries'
                            2. View found berries. Enter: 'view_found'
                            3. View missing berries. Enter: 'view_missing'
                            4. Add a new berry. Enter: 'add_berry'
                            5. Update an indexed berry. Enter: 'update_berry'
                            6. Delete an indexed berry. Enter: 'delete_berry'
                            7. Exit and save changes. Enter: 'exit'
                            """);
            String userInput = input.nextLine();

            //Checks if the input is invalid
            if (null == userInput) {
                System.out.println("Invalid input. Try again.\n");
            //Else if there is a valid input, then find the case that matches the user input and run it's respective services
            } else switch (userInput) {
                case "view_berries" -> {
                    berryIndexService.displayIndex();
                    System.out.println(berryIndexService.displayStats());
                    System.out.println("Viewing Indexed Berries\n");
                }
                case "view_found" -> {
                    berryIndexService.displayFilteredIndex(true);
                    System.out.println(berryIndexService.displayStats());
                    System.out.println("Viewing Found Berries\n");
                    }
                case "view_missing" -> {
                    berryIndexService.displayFilteredIndex(false);
                    System.out.println(berryIndexService.displayStats());
                    System.out.println("Viewing Missing Berries\n");
                    }
                case "add_berry" -> {
                    Berry newBerry = berryIndexService.addBerry(input);
                    System.out.println("Berry Indexed: " + newBerry.getName() + "\n");
                }
                case "update_berry" -> berryIndexService.updateBerry(input);
                case "delete_berry" -> berryIndexService.deleteBerry(input);
                case "exit" -> running = false;
                default -> System.out.println("Invalid input. Try again.\n");
            }
        }
    }
}
