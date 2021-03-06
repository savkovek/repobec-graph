package de.erichseifert.gral.samples;

import de.erichseifert.gral.data.Column;
import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.data.statistics.Statistics;
import de.erichseifert.gral.graphics.Insets2D;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.ui.InteractivePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

/**
 * Created by user on 07.02.2017.
 */
public class MyTestExample extends ExamplePanel {

    /**
     * Version id for serialization.
     */
    private static final long serialVersionUID = -412699430625953887L;

    private static final int SAMPLE_COUNT = 90;
    /**
     * Instance to generate random data values.
     */
    private static final Random random = new Random();
    private JButton runStopButton;
    private JButton twoGraphButton;
    private Timer updateTimer;
    private Boolean runStopState;
    private DataTable data;
    private int RANGE_VIEW = 50;
    private int BUFFER_MAX = 2100;
    private int BUFFER_VISIBLE = 1000;
    private int value = 0;
    private int x_value = 0;
    private XYPlot plot;
    private InteractivePanel interactivePanel;

    @SuppressWarnings("unchecked")
    public MyTestExample() {
        // Generate 100,000 data points
        data = new DataTable(Integer.class, Integer.class);
        for (int i = 0; i <= SAMPLE_COUNT; i++) {
            data.add(i, i);
            Column col =  data.getColumn(1);
        }
        value = SAMPLE_COUNT;
        x_value = SAMPLE_COUNT;
        /*
        for(Integer i = data.getRowCount(); i > SAMPLE_COUNT/2; i--){
            data.remove(0);
        }
        */
        // Create a new xy-plot

        data.setName("Ololo");
       // data.
        plot = new XYPlot(data);
        plot.setLegendVisible(true);
        plotConfig();



        runStopState = false;
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.X_AXIS));
        runStopButton = new JButton("Stop");
        runStopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (runStopState) {
                    updateTimer.start();
                    runStopButton.setText("Stop");
                } else {
                    updateTimer.stop();
                    runStopButton.setText("Run");
                    Column y_axis= data.getColumn(1);
                    plot.getAxis(XYPlot.AXIS_Y).setRange(
                            y_axis.getStatistics(Statistics.MIN),
                            y_axis.getStatistics(Statistics.MAX)
                    );
                    interactivePanel.repaint();
                }
                runStopState = !runStopState;
                remove(interactivePanel);
                plot = new XYPlot(data);
                plotConfig();

                Column x_axis= data.getColumn(0);
                plot.getAxis(XYPlot.AXIS_X).setRange(
                        x_axis.getStatistics(Statistics.MAX)- BUFFER_VISIBLE,
                        x_axis.getStatistics(Statistics.MAX)+ 50
                );
                interactivePanel = new InteractivePanel(plot);
                add(interactivePanel, BorderLayout.CENTER);
            }
        });
        southPanel.add(runStopButton);

        twoGraphButton = new JButton("add Graph");

        twoGraphButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Column origCol_x = data.getColumn(0);
                Column origCol = data.getColumn(1);
                origCol.get(4);
                DataTable data2 = new DataTable(Integer.class, Integer.class);
                int size = origCol.size();
                for(int i = 0; i <= size-1; i++){
                    data2.add(origCol_x.get(i), ((Integer)(origCol.get(i)))+50);
                }
                plot.add(data2);

                interactivePanel.repaint();
            }
        });


        southPanel.add(twoGraphButton);
        add(southPanel, BorderLayout.SOUTH );


        // Add plot to Swing component
        interactivePanel = new InteractivePanel(plot);

        add(interactivePanel, BorderLayout.CENTER);



        ActionListener taskPerformer = new ActionListener(){

            public void actionPerformed(ActionEvent evt2) {
                int i =0;
                while(i<=5){
                    i++;
                    //value++;
                    x_value = x_value+1;
                    value = new Double(Math.sin(new Double(x_value)/10)*100+ 200).intValue();
                    data.add(x_value , value);

                    while(data.getRowCount() > BUFFER_MAX){
                        data.remove(0);
                    }

                    Column x_axis= data.getColumn(0);
                    plot.getAxis(XYPlot.AXIS_X).setRange(
                            x_axis.getStatistics(Statistics.MAX)- BUFFER_VISIBLE,
                            x_axis.getStatistics(Statistics.MAX) + 50
                    );
                }
                interactivePanel.repaint();
            }

        };

        updateTimer = new Timer(5, taskPerformer);
        updateTimer.start();

    }


    private void plotConfig(){

        // Format plot
        plot.setInsets(new Insets2D.Double(20.0, 40.0, 40.0, 40.0));
        plot.getTitle().setText(getDescription());

        // Format points
        plot.getPointRenderers(data).get(0).setColor(COLOR1);

    }

    @Override
    public String getTitle() {
        return "Scatter plot";
    }

    @Override
    public String getDescription() {
        return String.format("Scatter plot with %d data points", SAMPLE_COUNT);
    }

    public static void main(String[] args) {
        new MyTestExample().showInFrame();
    }


}
