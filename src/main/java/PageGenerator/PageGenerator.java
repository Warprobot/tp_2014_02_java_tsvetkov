package PageGenerator;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

/**
 * Created by warprobot on 29.05.14.
 */
public class PageGenerator {
    private static final String TEMPLATE_DIR = "template";
    private static final Configuration CFG = new Configuration();

    public static String getPage(String templateName, Map<String, Object> data) {
        Writer stream = new StringWriter();
        try {
            CFG.setDirectoryForTemplateLoading(new File(TEMPLATE_DIR));
            CFG.setDefaultEncoding("UTF-8");
            Template template = CFG.getTemplate(templateName);
            template.process(data, stream);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
        return stream.toString();
    }
}
