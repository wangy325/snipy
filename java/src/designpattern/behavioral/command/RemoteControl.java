package designpattern.behavioral.command;

/**
 * @author wangy
 * @version 1.0
 * @date 2024/3/29 / 10:41
 */
public class RemoteControl {

    private CinemaCommand cinemaCommand;

    public RemoteControl(CinemaCommand cinemaCommand) {
        this.cinemaCommand = cinemaCommand;
    }

    public void onButtonPress(){
        cinemaCommand.execute();
    }

    public void redoCommand(){
        cinemaCommand.undo();
    }
}
