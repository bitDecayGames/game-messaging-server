package util

import java.io._
import java.util.zip._

import scala.collection.JavaConversions._

object Unzip {

  val BUFSIZE = 4096
  val buffer = new Array[Byte](BUFSIZE)

  def apply(source: String, targetFolder: String) = {
    val targetFolderFile = new File(targetFolder)
    deleteRecursively(targetFolderFile)
    targetFolderFile.mkdir
    val zipFile = new ZipFile(source)

    unzipAllFile(zipFile.entries.toList, getZipEntryInputStream(zipFile)_, targetFolderFile)
  }

  private def getZipEntryInputStream(zipFile: ZipFile)(entry: ZipEntry) = zipFile.getInputStream(entry)

  private def unzipAllFile(entryList: List[ZipEntry], inputGetter: (ZipEntry) => InputStream, targetFolder: File): Boolean = {

    entryList match {
      case entry :: entries =>
        println(s"Zip Entry: ${entry.getName}")
        val entryFile = new File(targetFolder, entry.getName)
        if (!entryFile.getParentFile.exists) entryFile.getParentFile.mkdirs
        saveFile(inputGetter(entry), new FileOutputStream(entryFile))

        unzipAllFile(entries, inputGetter, targetFolder)
      case _ =>
        true
    }

  }

  private def saveFile(fis: InputStream, fos: OutputStream) = {
    writeToFile(bufferReader(fis)_, fos)
    fis.close
    fos.close
  }

  private def bufferReader(fis: InputStream)(buffer: Array[Byte]) = (fis.read(buffer), buffer)

  private def writeToFile(reader: (Array[Byte]) => Tuple2[Int, Array[Byte]], fos: OutputStream): Boolean = {
    val (length, data) = reader(buffer)
    if (length >= 0) {
      fos.write(data, 0, length)
      writeToFile(reader, fos)
    } else
      true
  }

  private def deleteRecursively(file: File): Unit = {
    if (file.isDirectory)
      file.listFiles.foreach(deleteRecursively)
    if (file.exists && !file.delete)
      throw new RuntimeException(s"Unable to delete ${file.getAbsolutePath}")
  }
}