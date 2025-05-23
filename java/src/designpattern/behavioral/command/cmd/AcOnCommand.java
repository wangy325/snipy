package designpattern.behavioral.command.cmd;

import designpattern.behavioral.command.CinemaCommand;
import designpattern.behavioral.command.rec.AirConditioner;

/**
 * @author wangy
 * @version 1.0
 * @date 2024/3/29 / 10:57
 */
public class AcOnCommand implements CinemaCommand {
    private AirConditioner ac;

    public AcOnCommand(AirConditioner ac) {
        this.ac = ac;
    }

    @Override
    public void execute() {
        ac.on();
    }

    @Override
    public void undo() {
        ac.off();
    }
}
