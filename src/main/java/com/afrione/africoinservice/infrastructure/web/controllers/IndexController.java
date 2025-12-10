package com.afrione.africoinservice.infrastructure.web.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by jnya on
 * Sun, 25 May, 2025
 */
@Controller
@RequiredArgsConstructor
public class IndexController {

    @GetMapping(value = {"/home", "", "/"}, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String indexPage() {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "    <head>\n" +
                "        <meta charset=\"UTF-8\" />\n" +
                "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
                "        <title> Africoin Admin Service | Afrione</title>\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        <main>\n" +
                "            <code>\n" +
                "                \n" +
                "                    {\n" +
                "                        <pre>   \"message\": \"Service Available.\"</pre>\n" +
                "                    }\n" +
                "                </pre>\n" +
                "            </code>\n" +
                "        </main>\n" +
                "    </body>\n" +
                "</html>";
    }


}
