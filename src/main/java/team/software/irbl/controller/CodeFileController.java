package team.software.irbl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.software.irbl.service.file.CodeFileService;
import team.software.irbl.util.Res;

/**
 * @Author: CGC
 * @Date:   2021-3-10
 */
@RestController
@RequestMapping("/api/file")
public class CodeFileController {
    private CodeFileService codeFileService;

    @Autowired
    public CodeFileController(CodeFileService codeFileService){
        this.codeFileService = codeFileService;
    }

    @GetMapping("/readFile/{fileIndex}")
    public Res readFile(@PathVariable Integer fileIndex){
        return Res.success(codeFileService.readFile(fileIndex));
    }
}
