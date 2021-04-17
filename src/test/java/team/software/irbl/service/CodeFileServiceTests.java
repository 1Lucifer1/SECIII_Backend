package team.software.irbl.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.Before;
import org.junit.Test;
import team.software.irbl.domain.CodeFile;
import team.software.irbl.domain.Project;
import team.software.irbl.domain.RankRecord;
import team.software.irbl.dto.file.File;
import team.software.irbl.dto.file.FileContent;
import team.software.irbl.mapper.CodeFileMapper;
import team.software.irbl.mapper.ProjectMapper;
import team.software.irbl.mapper.RankRecordMapper;
import team.software.irbl.service.file.CodeFileService;
import team.software.irbl.serviceImpl.file.CodeFileServiceImpl;
import team.software.irbl.util.Err;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CodeFileServiceTests {

    private CodeFileService codeFileService;

    @Before
    public void before(){
        ProjectMapper projectMapper = mock(ProjectMapper.class);
        CodeFileMapper codeFileMapper = mock(CodeFileMapper.class);
        RankRecordMapper rankRecordMapper = mock(RankRecordMapper.class);

        Project project = new Project(2,"swt-3.1",484,98);
        when(projectMapper.selectById(2)).thenReturn(project);

        CodeFile codeFile = new CodeFile();
        codeFile.setFileIndex(4);
        codeFile.setFileName("ACC.java");
        codeFile.setFilePath("src/org/eclipse/swt/accessibility/ACC.java");
        codeFile.setPackageName("org.eclipse.swt.accessibility.ACC.java");
        codeFile.setProjectIndex(2);
        when(codeFileMapper.selectById(4)).thenReturn(codeFile);

        CodeFile codeFile1 = new CodeFile();
        codeFile1.setProjectIndex(1);
        codeFile1.setFileIndex(1);
        codeFile1.setFileName("test1.java");
        CodeFile codeFile2 = new CodeFile();
        codeFile2.setProjectIndex(1);
        codeFile2.setFileIndex(2);
        codeFile2.setFileName("test2.java");
        when(codeFileMapper.selectById(1)).thenReturn(codeFile1);
        when(codeFileMapper.selectById(2)).thenReturn(codeFile2);

        List<RankRecord> rankRecordList = new ArrayList<>();
        RankRecord rankRecord1 = new RankRecord();
        RankRecord rankRecord2 = new RankRecord();
        rankRecord1.setFileIndex(1);
        rankRecord1.setCosineSimilarity(1.2);
        rankRecord1.setFileRank(2);
        rankRecord1.setReportIndex(0);
        rankRecord2.setFileIndex(2);
        rankRecord2.setCosineSimilarity(2.1);
        rankRecord2.setFileRank(1);
        rankRecord2.setReportIndex(0);
        rankRecordList.add(rankRecord2);
        rankRecordList.add(rankRecord1);
        when(rankRecordMapper.selectList(new QueryWrapper<RankRecord>().eq("report_index", 0))).thenReturn(rankRecordList);

        this.codeFileService = new CodeFileServiceImpl(projectMapper,codeFileMapper,rankRecordMapper);
    }

    @Test
    public void readFileTest() throws Err {
        FileContent fileContent = codeFileService.readFile(4);
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
    public void getSortedFilesTest() throws Err {
        List<File> files = codeFileService.getSortedFiles(0);
//        files.forEach(item->System.out.println(item.getFileRank()));
        List<File> expected = new ArrayList<>();
        File file1 = new File();
        file1.setFileIndex(1);
        file1.setFileName("test1.java");
        file1.setFileRank(2);
        file1.setCosineSimilarity(1.2);
        File file2 = new File();
        file2.setFileIndex(2);
        file2.setFileName("test2.java");
        file2.setFileRank(1);
        file2.setCosineSimilarity(2.1);

        assertEquals(expected,files);
    }
}
