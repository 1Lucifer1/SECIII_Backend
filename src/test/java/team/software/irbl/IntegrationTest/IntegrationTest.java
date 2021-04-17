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
        Res res = codeFileController.readFile(4);
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
        assertEquals(4, fileContent.getFileIndex());
        assertEquals("ACC.java",fileContent.getFileName());
        assertEquals("src/org/eclipse/swt/accessibility/ACC.java",fileContent.getFilePath());
        assertEquals(content,fileContent.getContent());
        // ==TODO==
        assertEquals(0,fileContent.getSimilarity(),0);
    }

    @Test
    public void getSortedFilesTest() throws Exception{
        Res res = codeFileController.localizationOfBugReport(1);
        assertTrue(res.success);
        List<File> files = (List<File>) res.data;
        assertEquals(484, files.size());
        int cnt = 0;
        for (File file : files) {
            ++cnt;
            assertTrue(file.getCosineSimilarity() >= 0 && file.getCosineSimilarity() <= 1);
            assertEquals(cnt, file.getFileRank());
        }
    }

    @Test
    public void getIndicatorEvaluationTest() throws Err{
        Res res = projectController.getIndicatorEvaluation(2);
        assertTrue(res.success);
        Indicator indicator = (Indicator) res.data;
        assertEquals(Integer.valueOf(2), indicator.getProjectIndex());
        assertTrue(indicator.getTop1() >= 0 && indicator.getTop1() <= 1);
        assertTrue(indicator.getTop5() >= 0 && indicator.getTop5() <= 1);
        assertTrue(indicator.getTop10() >= 0 && indicator.getTop10() <= 1);
        assertTrue(indicator.getMAP() >= 0 && indicator.getMAP() <= 1);
        assertTrue(indicator.getMRR() >= 0 && indicator.getMRR() <= 1);
        assertTrue(indicator.getTop1() < indicator.getTop5());
        assertTrue(indicator.getTop5() < indicator.getTop10());
    }

    @Test
    public void getAllReportsByProjectIndexTest() throws Err {
        Res res = reportController.getAllReportsByProjectIndex(2);
        assertTrue(res.success);
        List<Report> reports = (List<Report>) res.data;
        assertEquals(98, reports.size());
        for (Report report : reports) {
            assertNotNull(report.getFixDate());
            assertNotNull(report.getOpenDate());
            assertNotNull(report.getSummary());
        }
    }
}
