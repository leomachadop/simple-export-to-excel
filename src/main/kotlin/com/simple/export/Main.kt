package com.simple.export

import com.simple.export.model.Context
import com.simple.export.processor.Processor
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import kotlinx.cli.required

fun main(args: Array<String>) {

    val parser = ArgParser("Simple export text to excel")
    val input by parser.option(ArgType.String, shortName = "i", description = "Input file").required()
    val output by parser.option(ArgType.String, shortName = "o", description = "Output file name").required()
    val separator by parser.option(ArgType.String, shortName = "s", description = "Separator of file").default(";")
    val withoutData by parser.option(ArgType.String, shortName = "w", description = "Value of empty input file").default("Without data")
    val sheetName by parser.option(ArgType.String, shortName = "n", description = "Name of sheet").default("DATA")
    val locale by parser.option(ArgType.String, shortName = "l", description = "Location of file for numbers").default("pt_BR")
    val encoding by parser.option(ArgType.String, shortName = "e", description = "Encoding of input file").default("Cp1252")
    val debug by parser.option(ArgType.Boolean, shortName = "d", description = "Turn on debug mode").default(true)

    parser.parse(args)

    val context =
        Context(input = input, output = output, separator = separator, debug = debug, sheetName = sheetName, withoutData = withoutData, encoding = encoding, locale = locale)

    if (context.debug) {
        println("Arguments:\n$context")
    }

    try {
        Processor(context).process()
    } catch (e: Exception) {
        if (context.debug) {
            e.printStackTrace()
        }
    }

}