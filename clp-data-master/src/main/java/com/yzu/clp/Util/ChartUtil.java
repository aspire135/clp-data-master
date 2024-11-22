package com.yzu.clp.Util;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map;

import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieToolTipGenerator;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;

/**
 * @Author: JCccc
 * @Date: 2022-6-15 16:53
 * @Description:
 */
public class ChartUtil {

    /**
     * 生成饼状图
     * @param dataset
     * @param filePath
     * @return
     */
    public static String createPieChart( PieDataset dataset,String filePath, String name) throws IOException, FontFormatException {

//        java.awt.Font customFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, ChartUtil.class.getResourceAsStream("/font/SourceHanSansCN-Bold.ttf"));
//        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//        ge.registerFont(customFont);

        JFreeChart chart = ChartFactory.createPieChart(name, dataset, true, true, false);
        // 设置抗锯齿，防止字体显示不清楚
        chart.setTextAntiAlias(false);
        PiePlot plot = (PiePlot) chart.getPlot();
        //边框线为白色
        plot.setOutlinePaint(Color.white);
        //连接线类型为直线
        plot.setLabelLinkStyle(PieLabelLinkStyle.QUAD_CURVE);

        // 使用自定义字体
        //设置Label字体
//        plot.setLabelFont(new java.awt.Font("Source Han Sans CN", java.awt.Font.BOLD, 12));
        plot.setLabelFont(new java.awt.Font("微软雅黑", java.awt.Font.BOLD, 12));
        //设置legend字体
//        chart.getLegend().setItemFont(new java.awt.Font("Source Han Sans CN", java.awt.Font.BOLD, 12));
        chart.getLegend().setItemFont(new java.awt.Font("微软雅黑", java.awt.Font.BOLD, 12));

        // 图片中显示百分比:默认方式
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator(StandardPieToolTipGenerator.DEFAULT_TOOLTIP_FORMAT));
        // 图片中显示百分比:自定义方式，{0} 表示选项， {1} 表示数值， {2} 表示所占比例 ,小数点后两位
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}={1}({2})",
                NumberFormat.getNumberInstance(),
                new DecimalFormat("0.00%")));
        // 图例显示百分比:自定义方式， {0} 表示选项， {1} 表示数值， {2} 表示所占比例
        plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0}={1}({2})"));
        // 指定图片的透明度(0.0-1.0)
        plot.setForegroundAlpha(1.0f);
        // 指定显示的饼图上圆形(false)还椭圆形(true)
        plot.setCircular(true);
        //设置图表背景透明度
        plot.setBackgroundAlpha(0f);
        // 设置背景色为白色
        chart.setBackgroundPaint(Color.WHITE);
        // 设置标注无边框
        chart.getLegend().setFrame(new BlockBorder(Color.WHITE));
        // 标注位于右侧
        chart.getLegend().setPosition(RectangleEdge.RIGHT);
        // 设置图标题的字体
        java.awt.Font font = new java.awt.Font("黑体", java.awt.Font.CENTER_BASELINE, 20);
        TextTitle textTitle = new TextTitle(name);
//        textTitle.setFont(new java.awt.Font("Source Han Sans CN", java.awt.Font.CENTER_BASELINE, 20));
        textTitle.setFont(font);
        chart.setTitle(textTitle);
        //图片存放位置
        try {
            FileOutputStream outputStream = new FileOutputStream(filePath);
            ChartUtilities.writeChartAsJPEG(outputStream, 1.0f, chart, 800, 450, null);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 生成柱状图
     * @param dataset
     * @param filePath
     */
    public static void createBarChart( CategoryDataset dataset,String filePath,String name) throws IOException, FontFormatException {

//        java.awt.Font customFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, ChartUtil.class.getResourceAsStream("/font/SourceHanSansCN-Bold.ttf"));
//        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//        ge.registerFont(customFont);

        final JFreeChart chart = ChartFactory.createBarChart(
                name,
                "分类", // 目录轴的显示标签
                "同比变化(%)", // 数值轴的显示标签
                dataset, // 数据集
                PlotOrientation.VERTICAL, // 图表方向
                true, // 是否显示图例，对于简单的柱状图必须为false
                true, // 是否生成提示工具
                false); // 是否生成url链接
        final CategoryPlot categoryplot = (CategoryPlot) chart.getPlot();
        final NumberAxis numberaxis = (NumberAxis) categoryplot.getRangeAxis();
        final CategoryAxis domainAxis = categoryplot.getDomainAxis();
        // 设置背景透明度
        categoryplot.setBackgroundAlpha(0f);
        /*------设置X轴坐标上的文字-----------*/
        domainAxis.setTickLabelFont(new Font("sans-serif", Font.PLAIN, 11));
//        domainAxis.setTickLabelFont(new java.awt.Font("Source Han Sans CN", java.awt.Font.CENTER_BASELINE, 11));
        /*------设置X轴的标题文字------------*/
        domainAxis.setLabelFont(new Font("宋体", Font.PLAIN, 12));
//        domainAxis.setLabelFont(new java.awt.Font("Source Han Sans CN", java.awt.Font.CENTER_BASELINE, 12));
        /*------设置Y轴坐标上的文字-----------*/
        numberaxis.setTickLabelFont(new Font("sans-serif", Font.PLAIN, 12));
//        numberaxis.setTickLabelFont(new java.awt.Font("Source Han Sans CN", java.awt.Font.CENTER_BASELINE, 12));
        /*------设置Y轴的标题文字------------*/
        numberaxis.setLabelFont(new Font("黑体", Font.PLAIN, 12));
//        numberaxis.setLabelFont(new java.awt.Font("Source Han Sans CN", java.awt.Font.CENTER_BASELINE, 12));
        /*------这句代码解决了底部汉字乱码的问题-----------*/
        chart.getLegend().setItemFont(new Font("宋体", Font.PLAIN, 12));
//        chart.getLegend().setItemFont(new java.awt.Font("Source Han Sans CN", java.awt.Font.CENTER_BASELINE, 12));
        /******* 这句代码解决了标题汉字乱码的问题 ********/
        chart.getTitle().setFont(new Font("宋体", Font.PLAIN, 20));
//        chart.getTitle().setFont(new java.awt.Font("Source Han Sans CN", java.awt.Font.CENTER_BASELINE, 20));
        try {
            FileOutputStream out = new FileOutputStream(filePath);
            ChartUtilities.writeChartAsJPEG(out, 1.0f, chart, 800, 450, null);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 生成折线图
     * @param ds
     * @param filePath
     */
    public static void createLineChart(DefaultCategoryDataset ds, String filePath) {
        try {
            // 创建柱状图.标题,X坐标,Y坐标,数据集合,orientation,是否显示legend,是否显示tooltip,是否使用url链接
            JFreeChart chart = ChartFactory.createLineChart("XX次数趋势图", "", "次数", ds, PlotOrientation.VERTICAL,false, true, true);
            chart.setBackgroundPaint(Color.WHITE);
            Font font = new Font("宋体", Font.BOLD, 12);
            chart.getTitle().setFont(font);
            chart.setBackgroundPaint(Color.WHITE);
            // 配置字体（解决中文乱码的通用方法）
            // X轴
            Font xfont = new Font("仿宋", Font.BOLD, 12);
            // Y轴
            Font yfont = new Font("宋体", Font.BOLD, 12);
            // 图片标题
            Font titleFont = new Font("宋体", Font.BOLD, 12);
            CategoryPlot categoryPlot = chart.getCategoryPlot();
            categoryPlot.getDomainAxis().setLabelFont(xfont);
            categoryPlot.getDomainAxis().setLabelFont(xfont);
            categoryPlot.getRangeAxis().setLabelFont(yfont);
            chart.getTitle().setFont(titleFont);
            categoryPlot.setBackgroundPaint(Color.WHITE);
            // x轴 // 分类轴网格是否可见
            categoryPlot.setDomainGridlinesVisible(true);
            // y轴 //数据轴网格是否可见
            categoryPlot.setRangeGridlinesVisible(true);
            // 设置网格竖线颜色
            categoryPlot.setDomainGridlinePaint(Color.LIGHT_GRAY);
            // 设置网格横线颜色
            categoryPlot.setRangeGridlinePaint(Color.LIGHT_GRAY);
            // 没有数据时显示的文字说明
            categoryPlot.setNoDataMessage("没有数据显示");
            // 设置曲线图与xy轴的距离
            categoryPlot.setAxisOffset(new RectangleInsets(0d, 0d, 0d, 0d));
            // 设置面板字体
            Font labelFont = new Font("SansSerif", Font.TRUETYPE_FONT, 12);
            // 取得Y轴
            NumberAxis rangeAxis = (NumberAxis) categoryPlot.getRangeAxis();
            rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            rangeAxis.setAutoRangeIncludesZero(true);
            rangeAxis.setUpperMargin(0.20);
            rangeAxis.setLabelAngle(Math.PI / 2.0);
            // 取得X轴
            CategoryAxis categoryAxis = (CategoryAxis) categoryPlot.getDomainAxis();
            // 设置X轴坐标上的文字
            categoryAxis.setTickLabelFont(labelFont);
            // 设置X轴的标题文字
            categoryAxis.setLabelFont(labelFont);
            // 横轴上的 Lable 45度倾斜
            categoryAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
            // 设置距离图片左端距离
            categoryAxis.setLowerMargin(0.0);
            // 设置距离图片右端距离
            categoryAxis.setUpperMargin(0.0);
            // 获得renderer
            LineAndShapeRenderer lineandshaperenderer = (LineAndShapeRenderer) categoryPlot.getRenderer();
            // 是否显示折点
            lineandshaperenderer.setBaseShapesVisible(true);
            // 是否显示折线
            lineandshaperenderer.setBaseLinesVisible(true);
            // series 点（即数据点）间有连线可见 显示折点数据
            lineandshaperenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
            lineandshaperenderer.setBaseItemLabelsVisible(true);
            ChartUtilities.saveChartAsJPEG(new File(filePath), chart, 1207, 500);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
