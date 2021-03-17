package team.software.irbl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.software.irbl.service.project.ProjectService;
import team.software.irbl.util.Err;
import team.software.irbl.util.Res;

@RestController
@RequestMapping("/api/project")
public class ProjectController {
    private ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService){
        this.projectService = projectService;
    }

    @GetMapping("/getIndicatorEvaluation/{projectIndex}")
    public Res getIndicatorEvaluation(@PathVariable Integer projectIndex) throws Err {
        return Res.success(projectService.getIndicatorEvaluation(projectIndex));
    }

}
