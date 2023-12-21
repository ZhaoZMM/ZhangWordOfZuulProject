/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael KÃ¶lling and David J. Barnes
 * @version 2016.02.29
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
        
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room a1,a2,a3,a4,a5,b1,b2,b3,b4,b5,c1,c2,c3,c4,c5,d1,d2,d3,d4,d5,e1,e2,e3,e4,e5,e6;
      
        // create the rooms
        a1= new Room("far from the exit");
        a2= new Room("far from the exit");
        a3= new Room("far from the exit");
        a4= new Room("far from the exit");
        a5= new Room("far from the exit");
        b1= new Room("far from the exit");
        b2= new Room("far from the exit");
        b3= new Room("closer to the exit");
        b4= new Room("closer to the exit");
        b5= new Room("closer to the exit");
        c1= new Room("far from the exit");
        c2= new Room("closer to the exit");
        c3= new Room("closer to the exit");
        c4= new Room("really close to the exit");
        c5= new Room("really close to the exit");
        d1= new Room("far from the exit");
        d2= new Room("closer to the exit");
        d3= new Room("really close to the exit");
        d4= new Room("really close to the exit");
        d5= new Room("really close to the exit");
        e1= new Room("far from the exit");
        e2= new Room("closer to the exit");
        e3= new Room("really close to the exit");
        e4= new Room("really close to the exit");
        e5= new Room("really close to the exit");
        e6 = new Room("at the exit");
        
        // initialise room exits (north, east, south, west)
        
        a1.setExits(a2,null,null,null);
        a2.setExits(null,b2,a1,null);
        a3.setExits(null,b3,null,null);
        a4.setExits(a5,b4,null,null);
        a5.setExits(null,b5,a4,null);
        b1.setExits(b2,c1,null,null);
        b2.setExits(b3,c2,b1,a2);
        b3.setExits(b4,null,b2,a3);
        b4.setExits(null,null,b3,a4);
        b5.setExits(null,null,null,a5);
        c1.setExits(null,d1,null,b1);
        c2.setExits(null,d2,null,null);
        c3.setExits(c4,d3,null,null);
        c4.setExits(c5, d4, c3, null);
        c5.setExits(null, d5, c4, null);
        d1.setExits(d2, null, null, c1);
        d2.setExits(null, e2, d1, c2);
        d3.setExits(null, e3, null, c3);
        d4.setExits(null, e4, null, c4);
        d5.setExits(null, e5, null, c5);
        e1.setExits(e2, null, null, null);
        e2.setExits(e3, null, e1 ,d2);
        e3.setExits(null, null, e2, d3);
        e4.setExits(null, null, null, d4);
        e5.setExits(e6, null, null, d5);
        

        currentRoom = a3;  // start game outside
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring maze game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println("You are " + currentRoom.getDescription());
        System.out.print("You can go: ");
        if(currentRoom.northExit != null) {
            System.out.print("north ");
        }
        if(currentRoom.eastExit != null) {
            System.out.print("east ");
        }
        if(currentRoom.southExit != null) {
            System.out.print("south ");
        }
        if(currentRoom.westExit != null) {
            System.out.print("west ");
        }
        System.out.println();
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }

        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around throughout the maze");
        System.out.println();
        System.out.println("Your command words are:");
        System.out.println("   go quit help");
    }

    /** 
     * Try to go in one direction. If there is an exit, enter
     * the new room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = null;
        if(direction.equals("north")) {
            nextRoom = currentRoom.northExit;
        }
        if(direction.equals("east")) {
            nextRoom = currentRoom.eastExit;
        }
        if(direction.equals("south")) {
            nextRoom = currentRoom.southExit;
        }
        if(direction.equals("west")) {
            nextRoom = currentRoom.westExit;
        }

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            currentRoom = nextRoom;
            System.out.println("You are " + currentRoom.getDescription());
            System.out.print("Exits: ");
            if(currentRoom.northExit != null) {
                System.out.print("north ");
            }
            if(currentRoom.eastExit != null) {
                System.out.print("east ");
            }
            if(currentRoom.southExit != null) {
                System.out.print("south ");
            }
            if(currentRoom.westExit != null) {
                System.out.print("west ");
            }
            System.out.println();
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
}
