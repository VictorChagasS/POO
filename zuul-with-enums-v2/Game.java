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
 * @author  Michael Kölling and David J. Barnes
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
        Room east_blue, nami, zoro, buggy,arlong,vila_da_nami,barba_negra,baratie,don_krieg,sanji,usopp,akainu,vila_do_usopp,one_Piece;
      
        // create the rooms
        east_blue = new Room("Inicio da jornada. Você está no mar dos fracos. Há inimigos por perto.","");
        vila_da_nami = new Room("Você chegou na vila da Nami. Ela está desenhando mapas ao leste. Ir pela direção errada pode ter drasticas consequencias.","");
        buggy = new Room("Você encontrou o buggy e perdeu","vilao");
        arlong = new Room("Você encontrou o arlong e perdeu","vilao");
        akainu = new Room("Você encontrou o akainu e perdeu","vilao");
        nami = new Room("Você encontrou a Nami, a navegadora do bando","companheiro");
        barba_negra = new Room("Você encontrou o Barba negra e perdeu","vilao");
        baratie = new Room("Você no restaurante do Sanji, o cozinheiro do bando. Ele está nos fundos. Há inimigos por perto.","");
        zoro = new Room("Você encontrou Zoro, o espadachim do bando,. Há inimigos por perto","companheiro");
        sanji = new Room("Você encontrou Sanji, o cozinheiro do bando","companheiro");
        don_krieg = new Room("Você encontrou o Don Krieg e perdeu","vilao");
        vila_do_usopp = new Room("Você chegou na vila do usopp, continue seguindo para encontrar o tesouro. Mas antes disso , precisará estender sua rota para encontrar mais um companheiro","");
        usopp = new Room("Você encontrou o Usopp, atirador do bando","companheiro");
        one_Piece = new Room("Você encontrou o maior tesouro do mundo. Fim de jogo.", "fim");

        // initialise room exits
        east_blue.setExit("east", vila_da_nami);
        east_blue.setExit("west", buggy);
        east_blue.setExit("north", zoro);

        vila_da_nami.setExit("west", east_blue);
        vila_da_nami.setExit("north", arlong);
        vila_da_nami.setExit("east", nami);
        vila_da_nami.setExit("south", akainu);

        zoro.setExit("west", barba_negra);
        zoro.setExit("east", baratie);
        zoro.setExit("north",vila_do_usopp);
        zoro.setExit("south", east_blue);

        baratie.setExit("south", sanji);
        baratie.setExit("north", don_krieg);
        baratie.setExit("west", zoro);

        vila_do_usopp.setExit("north",one_Piece);
        vila_do_usopp.setExit("east",usopp);
        vila_do_usopp.setExit("south",zoro);

        usopp.setExit("west",vila_do_usopp);

        nami.setExit("west",vila_da_nami);



        currentRoom = east_blue;  // start game outside
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
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type '" + CommandWord.HELP + "' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        switch (commandWord) {
            case UNKNOWN:
                System.out.println("I don't know what you mean...");
                break;

            case HELP:
                printHelp();
                break;

            case GO:
                goRoom(command);
                break;

            case QUIT:
                wantToQuit = quit(command);
                break;
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
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * Try to go in one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
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
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
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
