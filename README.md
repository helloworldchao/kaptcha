# kaptcha - A kaptcha generation engine.

This repo is the copy of http://code.google.com/p/kaptcha/, fixed some security problems and add more features.

## Usage

```
<dependency>
  <groupId>tech.helloworldchao</groupId>
  <artifactId>kaptcha</artifactId>
  <version>2.3.4</version>
</dependency>
```

### SpringBoot Demo

```
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
        
        // TODO: add save text logic ....
        
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
        
        // TODO: add save text logic ....
        
        // response to client
        String base64Image = producer.createBase64Image(capText);
        
        return base64Image;
    }
}
```