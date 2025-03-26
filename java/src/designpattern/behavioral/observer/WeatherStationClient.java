package designpattern.behavioral.observer;

/**
 * @author wangy
 * @version 1.0
 * @date 2024/3/8 / 17:39
 */
public class WeatherStationClient {
    public static void main(String[] args) {
        WeatherStation client = new WeatherStation();
        client.setData(23.2f, 10.91f, 1.01f);
        NormalBoard normalBoard =  new NormalBoard();        
        client.registerBoard(normalBoard);
        // register a new listener
        StatisticsBoard statisticsBoard =  new StatisticsBoard();
        client.registerBoard(statisticsBoard);
        
        client.notifyBoard();   // 1st notify
        client.unregisterBoard(normalBoard);
        client.setStatus(true);
        client.notifyBoard();   // 2nd notify
    }
}
