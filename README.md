# kaptcha - A kaptcha generation engine.

[![Publish package to the Maven Central Repository](https://github.com/helloworldchao/kaptcha/actions/workflows/publish_maven.yaml/badge.svg)](https://github.com/helloworldchao/kaptcha/actions/workflows/publish_maven.yaml)

This repo is the copy of http://code.google.com/p/kaptcha/, fixed some security problems and add more features.

## Usage

```xml
<dependency>
  <groupId>tech.helloworldchao</groupId>
  <artifactId>kaptcha</artifactId>
  <version>2.3.4</version>
</dependency>
```

### SpringBoot Demo

```java
@Configuration
public class KaptchaConfig {

    @Bean
    public DefaultKaptcha getDefaultKaptcha() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        properties.setProperty("kaptcha.border", "no");
        properties.setProperty("kaptcha.border.color", "105,179,90");
        properties.setProperty("kaptcha.textproducer.font.color", "blue");
        properties.setProperty("kaptcha.image.width", "110");
        properties.setProperty("kaptcha.image.height", "40");
        properties.setProperty("kaptcha.textproducer.font.size", "35");
        properties.setProperty("kaptcha.session.key", "code");
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        properties.setProperty("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);

        return defaultKaptcha;
    }
}
```

```java
@RestController
public class CaptchaController {

    @Autowired
    Producer producer;

    @RequestMapping("/captcha")
    public void getImage(HttpServletResponse response) throws Exception {
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        // generate text
        String capText = producer.createText();
        
        // TODO: save text logic ....
        
        // response to client
        BufferedImage bi = producer.createImage(capText);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
    }

    @RequestMapping("/base64Captcha")
    public String getBase64Image() throws Exception {
        // generate text
        String capText = producer.createText();
        
        // TODO: save text logic ....
        
        // response to client
        String base64Image = producer.createBase64Image(capText);
        
        return base64Image;
    }
}
```