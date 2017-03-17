package net.vatri.inventory;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.fxml.Initializable;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class DashboardController extends BaseController implements Initializable {

    private InventoryDao dao = new ActiveRecordDao();

    @FXML private Label   productsNumber;
    @FXML private Label   groupsNumber;
    @FXML private Label   ordersNumber;

    @FXML private BarChart<String,Number> ordersChart;
    @FXML private CategoryAxis  x;
    @FXML private NumberAxis    y;

    private Map<String,String> stats = dao.getStats();

    public void initialize(URL url, ResourceBundle rb) {

        productsNumber.setText(stats.get("products_number"));
        groupsNumber.setText(stats.get("groups_number"));
        ordersNumber.setText(stats.get("orders_number"));

        _createBarChart();

    }

    private void _createBarChart(){

        Integer month1 = Integer.parseInt( stats.get("orders1") );
        Integer month2 = Integer.parseInt( stats.get("orders2") );
        Integer month3 = Integer.parseInt( stats.get("orders3") );
        Integer month4 = Integer.parseInt( stats.get("orders4") );

        XYChart.Series<String,Number> dataSet1 = new XYChart.Series<String,Number>();
        dataSet1.getData().addAll(
                new XYChart.Data<String,Number>(_getRecentMonthName(3), month1)
                ,new XYChart.Data<String,Number>(_getRecentMonthName(2), month2)
                ,new XYChart.Data<String,Number>(_getRecentMonthName(1), month3)
                ,new XYChart.Data<String,Number>(_getRecentMonthName(0), month4)
        );

        ordersChart.getData().addAll(dataSet1);
    }

    private String _getRecentMonthName(int minusMonths){

        DateTimeFormatter formatter = DateTimeFormat.forPattern("MMM"); // MMM = Jan,Feb...

        return  formatter.print(LocalDate.now().minusMonths(minusMonths) );
    }


} // class