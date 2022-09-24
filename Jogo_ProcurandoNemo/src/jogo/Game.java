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
    private Interacao interagir = new Interacao();
    private Parser parser;
    private Room currentRoom;
    private Room finalRoom;
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
        Room casa, arraia, mar_aberto, tubarao, minas, canion, peixe_abissal, tartaruga, agua_viva, sidney;
      
        // create the rooms
        casa = new Room("na sua casa mas não sabe onde o Nemo está");
        arraia = new Room("na escola onde o Nemo estuda, mas ele tambem não esta aqui");
        mar_aberto = new Room("no mar aberto e encontrou uma peixe chamada Dori! Ela irá te acompanhar agora!");
        tubarao = new Room("onde tubarões assustadores vivem");
        minas = new Room("nas minas, cuid- KABUMMMMMMMM");
        canion = new Room("em um canion escuro e perigoso. Você sente que se tentar voltar para aqui irá se perder, cuidado!");
        peixe_abissal = new Room("com um peixe perigoso que vive no fundo do mar. Não tem escapatoria");
        tartaruga = new Room("em uma corrente maritima com as tartarugas! Você ainda não encontrou o Nemo");
        agua_viva = new Room("junto com as aguas-vivas. Muito cuidado!");
        sidney = new Room("COM O NEMO, PARABENS!");

        
        // initialise room exits
        casa.setExit("north", arraia);

        arraia.setExit("west", mar_aberto);
        arraia.setExit("south", casa);
        arraia.setExit("north", tubarao);

        mar_aberto.setExit("east", arraia);
        mar_aberto.setExit("north", tubarao);
        interagir.setDialogRoom(mar_aberto, "Dori: Vamos seguir em frente! E cuidado com os tubarões, eles são mentirosos.");

        tubarao.setExit("east", minas);
        tubarao.setExit("north", canion);
        interagir.setDialogRoom(tubarao, "Tubarão assustador: Um conselho, vá para a direção EAST pois a direção NORTH é muito perigosa.");;

        canion.setExit("north", agua_viva);
        canion.setExit("west", tartaruga);

        tartaruga.setExit("east", agua_viva);
        tartaruga.setExit("south", peixe_abissal);
        interagir.setDialogRoom(tartaruga, "Tartaruga lider: O Nemo passou por aqui, você tem que seguir a direção NORTH quando passar as aguas-vivas para encontrar ele.");

        agua_viva.setExit("south", peixe_abissal);
        agua_viva.setExit("west", tartaruga);
        agua_viva.setExit("north", sidney);

        interagir.setDeathRoom(peixe_abissal);
        interagir.setDialogRoom(peixe_abissal, "Você morreu pois tentou voltar para o desfiladeiro, se perdeu e foi parar na boca de um peixe perigoso.\n Tenha mais cuidado da proxima vez!");
        
        interagir.setDeathRoom(minas);
        interagir.setDialogRoom(minas, "Você morreu pois acreditou no tubarão e foi para as minas. Tenha mais cuidado da proxima vez!");

        interagir.setDialogRoom(sidney, "PARABENS!!! Você chegou ao final do jogo, deve ter percebido que o caminho até o final foi apenas em uma direção.\n"
                                +   "Esta é a lição tanto do filme quanto deste jogo: siga sempre em frente!");

        finalRoom = sidney;
        currentRoom = casa;  // start game outside
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
            if(interagir.checkDeathRoom(currentRoom) || currentRoom == finalRoom){
                interagir.interact(currentRoom);
                break;
            }
            interagir.interact(currentRoom);
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Obrigado por jogar.  Até mais.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Bem vindo ao Procurando Nemo!");
        System.out.println("Procurando Nemo definitivamente é um jogo de procurar o Nemo.");
        System.out.println("Digite 'help' se precisar de ajuda.");
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

        if(command.isUnknown()) {
            System.out.println("Não entendi...");
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
        // else command not recognised.
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
        
        System.out.println("Seus comandos são:");
        parser.showCommands();
    }

    /** 
     * Try to in to one direction. If there is an exit, enter the new
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
