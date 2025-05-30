package designpattern.behavioral.command;

/**
 * @author wangy
 * @version 1.0
 * @date 2024/3/29 / 10:42
 * 命令接口
 */
public interface CinemaCommand {

    void execute();

    void undo();
}
