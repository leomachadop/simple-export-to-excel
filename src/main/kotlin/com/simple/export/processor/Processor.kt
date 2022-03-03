package com.simple.export.processor


import com.simple.export.model.AuxModel
import com.simple.export.model.Context
import org.apache.commons.lang.math.NumberUtils
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.streaming.SXSSFSheet
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import java.text.NumberFormat
import java.util.*


class Processor(private val context: Context) {

    private val numberFormat: NumberFormat = NumberFormat.getNumberInstance(Locale(context.locale))

    @Throws(Exception::class)
    fun process() {

        debug("Process init")

        val workbook: Workbook? = getWorkbook(context.output)
        var fileOutputStream: FileOutputStream? = null
        val sheet: SXSSFSheet = workbook?.createSheet(context.sheetName) as SXSSFSheet
        try {
            Files.newBufferedReader(Paths.get(context.input), Charset.forName(context.encoding)).use { br ->
                val aux = AuxModel()
                var line: String?
                while (br.readLine().also { line = it } != null) {
                    if (line != null) {
                        if (aux.index === 0) {
                            val header = line!!.split(context.separator).toTypedArray()
                            val rowHeader: Row = sheet.createRow(aux.plus())
                            for (i in header.indices) {
                                rowHeader.createCell(i).setCellValue(header[i].trim { it <= ' ' })
                            }
                            aux.header = header
                        } else {
                            val linhaSplit = line!!.split(context.separator).toTypedArray()
                            val currentRow: Row = sheet.createRow(aux.plus())
                            for (i in linhaSplit.indices) {
                                val contentOfLineTrim = linhaSplit[i].trim { it <= ' ' }
                                if (NumberUtils.isDigits(contentOfLineTrim)) {
                                    currentRow.createCell(i).setCellValue(contentOfLineTrim.toDouble())
                                } else if (NumberUtils.isNumber(contentOfLineTrim)) {
                                    currentRow.createCell(i).setCellValue(contentOfLineTrim.toDouble())
                                } else {
                                    try {
                                        currentRow.createCell(i).setCellValue(numberFormat.parse(contentOfLineTrim).toDouble())
                                    } catch (e: Exception) {
                                        currentRow.createCell(i).setCellValue(contentOfLineTrim)
                                    }
                                }
                            }
                        }
                    }
                }
                if (aux.index === 1) {
                    empty(sheet)
                } else {
                    sheet.setAutoFilter(CellRangeAddress(0, 0, 0, aux.header.size - 1))
                    sheet.trackAllColumnsForAutoSizing()
                    for (i in 0 until aux.header.size) {
                        sheet.autoSizeColumn(i)
                        sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 500)
                    }
                    sheet.createFreezePane(0, 1)
                }
                fileOutputStream = FileOutputStream(context.output)
                workbook.write(fileOutputStream)
            }
        } catch (exception: Exception) {
            throw exception
        } finally {
            try {
                fileOutputStream?.close()
                workbook?.close()
            } catch (exception: Exception) {
                throw exception
            }
        }
        debug("Process end")
    }

    private fun empty(sheet: SXSSFSheet) {
        sheet.createRow(0).createCell(0).setCellValue(context.withoutData)
        sheet.autoSizeColumn(0)
    }

    @Throws(Exception::class)
    private fun getWorkbook(path: String): Workbook? {
        val fileXls = File(path)
        val workBook: Workbook = if (fileXls.exists()) {
            WorkbookFactory.create(fileXls)
        } else {
            SXSSFWorkbook(10000)
        }
        return workBook
    }

    private fun debug(t: String) {
        if (context.debug) {
            println("$t")
        }
    }
}