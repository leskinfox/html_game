package services.template;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import services.statistics.StatisticsService;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

public class TemplateService {
    private static final String HTML_DIR = "templates";

    private static TemplateService templateService;
    private final Configuration cfg;
    private final StatisticsService statistics;

    public TemplateService(StatisticsService statistics) {
        this.statistics = statistics;
        cfg = new Configuration(Configuration.VERSION_2_3_0);
        cfg.setDefaultEncoding("UTF-8");
    }

    public String getPage(String sessionId, String filename, Map<String, String> data) {
        statistics.stopPageTimer(sessionId);
        data.put("statistics", statistics.getInfo(sessionId));
        Writer stream = new StringWriter();
        try {
            Template template = cfg.getTemplate(HTML_DIR + File.separator + filename);
            template.process(data, stream);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
        return stream.toString();
    }
}