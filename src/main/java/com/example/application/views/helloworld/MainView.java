package com.example.application.views.helloworld;

import com.example.deleteme.DiffService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.charts.model.style.SolidColor;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route
public class MainView extends VerticalLayout {

    public MainView() {
        TextField fx = new TextField();
        fx.setClearButtonVisible(true);
        fx.setErrorMessage("Неправильное значение");
        Div div = new Div();
        div.setText("f(x) = ");
        fx.setPrefixComponent(div);
        fx.setValue("2 * sin(x)");
        fx.setLabel("Введите значение f(x)");
        fx.setSizeFull();

        NumberField maxT = new NumberField();
        maxT.setClearButtonVisible(true);
        maxT.setMax(10000);
        maxT.setMin(0.1);
        maxT.setErrorMessage("Неправильное значение");
        maxT.setValue(10.0);
        maxT.setLabel("Введите T");


        NumberField firstX = new NumberField();
        firstX.setClearButtonVisible(true);
        firstX.setMin(0);
        firstX.setMax(1000000);
        firstX.setErrorMessage("Неправильное значение");
        firstX.setValue(0.0);
        firstX.setLabel("Введите  начальную точку");

        NumberField errorRate = new NumberField();
        errorRate.setClearButtonVisible(true);
        errorRate.setMin(0.0000001);
        errorRate.setMax(0.1);
        errorRate.setErrorMessage("Неправильное значение");
        errorRate.setValue(0.00001);
        errorRate.setLabel("Введите значение погрешности");

        Button primaryButton = new Button("Решить");
        primaryButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(fx);


        FormLayout formLayout = new FormLayout();
        formLayout.add(maxT, firstX, errorRate, primaryButton);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("400px", 2));
        new FormLayout.ResponsiveStep("1000px", 3);
        formLayout.setColspan(primaryButton, 2);
        formLayout.setColspan(errorRate, 2);

        add(formLayout);


        DiffService diffService = new DiffService();

        com.vaadin.flow.component.charts.Chart chart = new Chart();
        chart.setWidth("400px");
        chart.setHeight("400px");
        Configuration conf = chart.getConfiguration();
        conf.getChart().setType(ChartType.LINE);
        conf.setTitle("Решение");
        conf.getxAxis().setTitle("Ось х");
        conf.getyAxis().setTitle("Ось y");

        com.vaadin.flow.component.charts.Chart chart1 = new Chart();
        chart.setWidth("400px");
        chart.setHeight("400px");
        Configuration conf1 = chart1.getConfiguration();
        conf1.getChart().setType(ChartType.LINE);
        conf1.getyAxis().setTitle("Ось E");
        conf1.getxAxis().setTitle("Ось N");
        conf1.setTitle("Погрешность");

        primaryButton.addClickListener(buttonClickEvent -> {
            chart.setVisible(false);
            chart1.setVisible(false);

            diffService.runApp(maxT.getValue(), 100, errorRate.getValue(), firstX.getValue(), fx.getValue());

            DataSeries serie1 = new DataSeries();
            for (int i = 0; i < diffService.getListX().size(); i++) {
                serie1.add(new DataSeriesItem(diffService.getListX().get(i), diffService.getListY().get(i)));
            }

            conf.setSeries(serie1);
            PlotOptionsLine serie1Opts = new PlotOptionsLine();
            serie1Opts.setColor(SolidColor.RED);
            serie1.setPlotOptions(serie1Opts);
            conf.setTitle("Решение");
            DataSeries serie2 = new DataSeries();

            for (int i = 0; i < diffService.getListX().size(); i++) {
                diffService.getListN().get(i);
                diffService.getListErrorRate().get(i);
                serie2.add(new DataSeriesItem(i, diffService.getListErrorRate().get(i)));
            }
            conf1.setSeries(serie2);
            serie1Opts.setColor(SolidColor.RED);
            serie1.setPlotOptions(serie1Opts);
            conf1.setTitle("Погрешность");

            chart.setVisible(true);
            chart1.setVisible(true);


            FormLayout formLayout1 = new FormLayout();

            formLayout1.add(chart, chart1);
            add(formLayout1);

            diffService.setListN(null);
            diffService.setListErrorRate(null);
            diffService.setListX(null);
            diffService.setListY(null);
        });


    }
}

