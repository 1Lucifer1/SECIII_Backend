package team.software.irbl.IntegrationTest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import team.software.irbl.controller.CodeFileController;
import team.software.irbl.controller.ProjectController;
import team.software.irbl.controller.ReportController;
import team.software.irbl.dto.file.File;
import team.software.irbl.dto.file.FileContent;
import team.software.irbl.dto.project.Indicator;
import team.software.irbl.dto.report.Report;
import team.software.irbl.util.Err;
import team.software.irbl.util.Res;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTest {

    @Autowired
    CodeFileController codeFileController;

    @Autowired
    ProjectController projectController;

    @Autowired
    ReportController reportController;

    @Test
    public void readFileTest() throws Exception{
        Res res = codeFileController.readFile(3);
        assertTrue(res.success);
        FileContent fileContent = (FileContent) res.data;
        String content = "/*******************************************************************************\n" +
                " * Copyright (c) 2000, 2004 IBM Corporation and others.\n" +
                " * All rights reserved. This program and the accompanying materials\n" +
                " * are made available under the terms of the Eclipse Public License v1.0\n" +
                " * which accompanies this distribution, and is available at\n" +
                " * http://www.eclipse.org/legal/epl-v10.html\n" +
                " *\n" +
                " * Contributors:\n" +
                " *     IBM Corporation - initial API and implementation\n" +
                " *******************************************************************************/\n" +
                "package org.eclipse.swt.accessibility;\n" +
                "\n" +
                "\n" +
                "/**\n" +
                " * Class ACC contains all the constants used in defining an\n" +
                " * Accessible object.\n" +
                " * \n" +
                " * @since 2.0\n" +
                " */\n" +
                "public class ACC {\n" +
                "\tpublic static final int STATE_NORMAL = 0x00000000;\n" +
                "\tpublic static final int STATE_SELECTED = 0x00000002;\n" +
                "\tpublic static final int STATE_SELECTABLE = 0x00200000;\n" +
                "\tpublic static final int STATE_MULTISELECTABLE = 0x1000000;\n" +
                "\tpublic static final int STATE_FOCUSED = 0x00000004;\n" +
                "\tpublic static final int STATE_FOCUSABLE = 0x00100000;\n" +
                "\tpublic static final int STATE_PRESSED = 0x8;\n" +
                "\tpublic static final int STATE_CHECKED = 0x10;\n" +
                "\tpublic static final int STATE_EXPANDED = 0x200;\n" +
                "\tpublic static final int STATE_COLLAPSED = 0x400;\n" +
                "\tpublic static final int STATE_HOTTRACKED = 0x80;\n" +
                "\tpublic static final int STATE_BUSY = 0x800;\n" +
                "\tpublic static final int STATE_READONLY = 0x40;\n" +
                "\tpublic static final int STATE_INVISIBLE = 0x8000;\n" +
                "\tpublic static final int STATE_OFFSCREEN = 0x10000;\n" +
                "\tpublic static final int STATE_SIZEABLE = 0x20000;\n" +
                "\tpublic static final int STATE_LINKED = 0x400000;\n" +
                "\n" +
                "\tpublic static final int ROLE_CLIENT_AREA = 0xa;\n" +
                "\tpublic static final int ROLE_WINDOW = 0x9;\n" +
                "\tpublic static final int ROLE_MENUBAR = 0x2;\n" +
                "\tpublic static final int ROLE_MENU = 0xb;\n" +
                "\tpublic static final int ROLE_MENUITEM = 0xc;\n" +
                "\tpublic static final int ROLE_SEPARATOR = 0x15;\n" +
                "\tpublic static final int ROLE_TOOLTIP = 0xd;\n" +
                "\tpublic static final int ROLE_SCROLLBAR = 0x3;\n" +
                "\tpublic static final int ROLE_DIALOG = 0x12;\n" +
                "\tpublic static final int ROLE_LABEL = 0x29;\n" +
                "\tpublic static final int ROLE_PUSHBUTTON = 0x2b;\n" +
                "\tpublic static final int ROLE_CHECKBUTTON = 0x2c;\n" +
                "\tpublic static final int ROLE_RADIOBUTTON = 0x2d;\n" +
                "\tpublic static final int ROLE_COMBOBOX = 0x2e;\n" +
                "\tpublic static final int ROLE_TEXT = 0x2a;\n" +
                "\tpublic static final int ROLE_TOOLBAR = 0x16;\n" +
                "\tpublic static final int ROLE_LIST = 0x21;\n" +
                "\tpublic static final int ROLE_LISTITEM = 0x22;\n" +
                "\tpublic static final int ROLE_TABLE = 0x18;\n" +
                "\tpublic static final int ROLE_TABLECELL = 0x1d;\n" +
                "\tpublic static final int ROLE_TABLECOLUMNHEADER = 0x19;\n" +
                "\t/** @deprecated use ROLE_TABLECOLUMNHEADER */\n" +
                "\tpublic static final int ROLE_TABLECOLUMN = ROLE_TABLECOLUMNHEADER;\n" +
                "\tpublic static final int ROLE_TABLEROWHEADER = 0x1a;\n" +
                "\tpublic static final int ROLE_TREE = 0x23;\n" +
                "\tpublic static final int ROLE_TREEITEM = 0x24;\n" +
                "\tpublic static final int ROLE_TABFOLDER = 0x3c;\n" +
                "\tpublic static final int ROLE_TABITEM = 0x25;\n" +
                "\tpublic static final int ROLE_PROGRESSBAR = 0x30;\n" +
                "\tpublic static final int ROLE_SLIDER = 0x33;\n" +
                "\tpublic static final int ROLE_LINK = 0x1e;\n" +
                "\n" +
                "\tpublic static final int CHILDID_SELF = -1;\n" +
                "\tpublic static final int CHILDID_NONE = -2;\n" +
                "\tpublic static final int CHILDID_MULTIPLE = -3;\n" +
                "\t\n" +
                "\tpublic static final int TEXT_INSERT = 0;\n" +
                "\tpublic static final int TEXT_DELETE = 1;\n" +
                "}\n";
        assertEquals(3, fileContent.getFileIndex());
        assertEquals("ACC.java",fileContent.getFileName());
        assertEquals("swt-3.1/src/org/eclipse/swt/accessibility/ACC.java",fileContent.getFilePath());
        assertEquals(content,fileContent.getContent());
        // ==TODO==
        assertEquals(0,fileContent.getSimilarity(),0);
    }

    @Test
    public void getSortedFilesTest() throws Exception{
        Res res =codeFileController.localizationOfBugReport(1);
        assertTrue(res.success);
        List<File> files = (List<File>) res.data;
//        files.forEach(item->System.out.println(item.getFileRank()));
        List<File> expected = new ArrayList<>();
        File file1 = new File();
        file1.setFileIndex(1);
        file1.setFileName("test1.java");
        file1.setFileRank(2);
        file1.setCosineSimilarity(2.1);
        File file2 = new File();
        file2.setFileIndex(2);
        file2.setFileName("test2.java");
        file2.setFileRank(1);
        file2.setCosineSimilarity(1.2);

        expected.add(file2);
        expected.add(file1);

        assertEquals(expected,files);
    }

    @Test
    public void getIndicatorEvaluationTest() throws Err{
        Res res = projectController.getIndicatorEvaluation(2);
        assertTrue(res.success);
        Indicator indicator = (Indicator) res.data;
        assertThat(indicator.getTop1(), greaterThanOrEqualTo(0.071));
        assertThat(indicator.getTop5(), greaterThanOrEqualTo(0.316));
        assertThat(indicator.getTop10(), greaterThanOrEqualTo(0.49));
        assertThat(indicator.getMRR(), greaterThanOrEqualTo(0.20438985978353597));
        assertThat(indicator.getMAP(), greaterThanOrEqualTo(0.18109653077639296));
    }

    @Test
    public void getAllReportsByProjectIndexTest() throws Err {
        Res res = reportController.getAllReportsByProjectIndex(1);
        assertTrue(res.success);
        List<Report> reports = (List<Report>) res.data;
        Report report = reports.get(0);

        assertEquals(1, report.getReportIndex());
        assertEquals(1000,report.getBugId());
        assertEquals("2020-11-12 08:40:00",report.getOpenDate());
        assertEquals("2020-12-12 08:40:00",report.getFixDate());
        assertEquals("test bug report",report.getSummary());
    }
}
