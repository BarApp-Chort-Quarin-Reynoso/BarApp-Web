package com.barapp.web.views.components.charts;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.*;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.chart.builder.ToolbarBuilder;
import com.github.appreciated.apexcharts.config.legend.Position;
import com.github.appreciated.apexcharts.config.plotoptions.builder.BarBuilder;
import com.github.appreciated.apexcharts.config.xaxis.TickPlacement;
import com.github.appreciated.apexcharts.config.xaxis.builder.TitleBuilder;
import com.github.appreciated.apexcharts.config.xaxis.title.builder.StyleBuilder;
import com.github.appreciated.apexcharts.config.yaxis.builder.AxisTicksBuilder;
import com.github.appreciated.apexcharts.config.yaxis.builder.LabelsBuilder;
import com.github.appreciated.apexcharts.helper.Series;

import java.util.ArrayList;
import java.util.List;

public class BarappCharts {
    private static ApexChartsBuilder getBaseBuilder() {
        return new ApexChartsBuilder()
                .withDataLabels(DataLabelsBuilder.get()
                        .withEnabled(false).build())
                .withStroke(StrokeBuilder.get()
                        .withShow(true)
                        .withWidth(2.0)
                        .withColors("transparent")
                        .build())
                .withFill(FillBuilder.get()
                        .withOpacity(1.0).build());
    }

    public static ApexCharts getPieChart() {
        ApexChartsBuilder builder = getBaseBuilder();

        builder
                .withChart(ChartBuilder.get()
                        .withType(Type.DONUT)
                        .withWidth("100%")
                        .withHeight("100%")
                        .build())
                .withLabels("A", "B", "C", "D", "E")
                .withDataLabels(DataLabelsBuilder.get()
                        .build())
                .withTitle(TitleSubtitleBuilder.get()
                        .withText("Basic Chart")
                        .withAlign(com.github.appreciated.apexcharts.config.subtitle.Align.CENTER)
                        .withMargin(25d)
                        .build())
                .withLegend(LegendBuilder.get()
                        .withPosition(Position.BOTTOM)
                        .build())
                .withSeries(25.0, 15.0, 44.0, 55.0, 41.0);

        return builder.build();
    }

    public static ApexCharts getBarChart(String[] labels, Double[] data, String title) {
        ApexChartsBuilder builder = getBaseBuilder();

        builder
                .withChart(ChartBuilder.get()
                        .withToolbar(ToolbarBuilder.get()
                                .withShow(false)
                                .build())
                        .withType(Type.BAR)
                        .withWidth("500px")
                        .withHeight("300px")
                        .build())
                .withPlotOptions(PlotOptionsBuilder.get()
                        .withBar(BarBuilder.get()
                                .withDistributed(true)
                                .withHorizontal(false)
                                .withColumnWidth("55%")
                                .build())
                        .build())
                .withXaxis(XAxisBuilder.get()
                        .withCategories(labels)
                        .withLabels(com.github.appreciated.apexcharts.config.xaxis.builder.LabelsBuilder.get()
                                .withRotate(-45.0)
                                .withRotateAlways(true)
                                .build())
                        .withTickPlacement(TickPlacement.BETWEEN)
                        .withTitle(TitleBuilder.get()
                                .withText(title)
                                .withStyle(StyleBuilder.get()
                                        .withCssClass("estadisticas-titulo")
                                        .build())
                                .withOffsetY(-10.0)
                                .build())
                        .build())
                .withYaxis(YAxisBuilder.get()
                        .withMax(1.0)
                        .withMin(0.0)
                        .withTickAmount(4.0)
                        .withLabels(LabelsBuilder.get()
                                .withFormatter(getJsFormatterLabelsEjeY()).build())
                        .build())
                .withLegend(LegendBuilder.get()
                        .withShow(false)
                        .build())
                .withSeries(getSeries(data));

        return builder.build();
    }

    private static String getJsFormatterLabelsEjeY() {
        return "(value) => {" +
                "    value = Number(parseFloat(value)) * 100;" +
                "    const valueStr = `%${value.toLocaleString('de-DE')}`;" +
                "    return valueStr;" +
                "}";
    }

    @SuppressWarnings("unchecked")
    private static Series<Double>[] getSeries(Double[] data) {
        List<Series<Double>> series = new ArrayList<>();
        series.add(new Series<>("A", data));
        return series.toArray(new Series[0]);
    }
}
