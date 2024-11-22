package com.yzu.clp.DATA_PERFORM.service;

import com.yzu.clp.DATA_PERFORM.dto.HeiMaoSubDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;

public interface FileExportService {

    void exportToCSV( HeiMaoSubDTO heiMaoSubDTO);

    void createBulletinPdf() throws IOException, FontFormatException;
}
