package designpattern.behavioral.command;

import designpattern.behavioral.command.cmd.AcOnCommand;
import designpattern.behavioral.command.cmd.FamilyCinemaOnCommand;
import designpattern.behavioral.command.rec.*;

/**
 * @author wangy
 * @version 1.0
 * @date 2024/3/29 / 11:48
 */
public class TestCommand {
    public static void main(String[] args) {
//        simpleCommand();
        batchCommand();
    }

    static void batchCommand(){
        AirConditioner ac =  new AirConditioner();
        Light light =  new Light();
        Popcorn popcorn =  new Popcorn();
        Stereo stereo= new Stereo();
        DV dv = new DV();
        Screen screen = new Screen();
        FamilyCinemaOnCommand familyCinemaOnCommand
            = new FamilyCinemaOnCommand(ac,light, dv, popcorn, screen,stereo);

        RemoteControl remoteControl =  new RemoteControl(familyCinemaOnCommand);
        remoteControl.onButtonPress();
        System.out.println("---------------");
        remoteControl.redoCommand();
    }
}

///:~
//AC on.
//Popcorn ready.
//Stereo on.
//Screen down.
//DV on
//Ready to play Schindler's List.
//Light off.
//---------------
//Light on.
//DV off.
//Screen off.
//Stereo off.
//Popcorn off.
//AC off.
//
