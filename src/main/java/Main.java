import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws ParserConfigurationException, TransformerException, IOException, SAXException {
        String[] textCsv = "1, John, Smith, USA, 25".split(",");
        String[] textCsv2 = "2, Inav, Petrov, RU, 23".split(",");
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        //создание файла csv с текстом
        try (CSVWriter writer = new CSVWriter(new FileWriter("data.csv", true))) {
            writer.writeNext(textCsv);
            writer.writeNext(textCsv2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parse parse = new Parse();
        //получаем список сотрудников из файла csv
        List<Employee> list = parse.parseCSV(columnMapping, fileName);
        //преобразуем csv в json
        String json = parse.listToJson(list);
        //записываем файл json
        parse.writeString(json, "data.json");

        //Задание 2
        //создание xml файла
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Element root = document.createElement("staff");
        document.appendChild(root);

        Element employee = document.createElement("employee");
        root.appendChild(employee);
        Element id = document.createElement("id");
        id.appendChild(document.createTextNode("1"));
        employee.appendChild(id);
        Element firstName = document.createElement("firstName");
        firstName.appendChild(document.createTextNode("John"));
        employee.appendChild(firstName);
        Element lastName = document.createElement("lastName");
        lastName.appendChild(document.createTextNode("Smith"));
        employee.appendChild(lastName);
        Element country = document.createElement("country");
        country.appendChild(document.createTextNode("USA"));
        employee.appendChild(country);
        Element age = document.createElement("age");
        age.appendChild(document.createTextNode("25"));
        employee.appendChild(age);

        Element employee2 = document.createElement("employee");
        root.appendChild(employee2);
        Element id2 = document.createElement("id");
        id2.appendChild(document.createTextNode("2"));
        employee2.appendChild(id2);
        Element firstName2 = document.createElement("firstName");
        firstName2.appendChild(document.createTextNode("Inav"));
        employee2.appendChild(firstName2);
        Element lastName2 = document.createElement("lastName");
        lastName2.appendChild(document.createTextNode("Petrov"));
        employee2.appendChild(lastName2);
        Element country2 = document.createElement("country");
        country2.appendChild(document.createTextNode("RU"));
        employee2.appendChild(country2);
        Element age2 = document.createElement("age");
        age2.appendChild(document.createTextNode("23"));
        employee2.appendChild(age2);


        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File("data.xml"));
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.transform(domSource, streamResult);

        //Получение списка сотрудников
        List<Employee> list2 = parse.parseXML("data.xml");
        //Преобразуем в json
        String xmlJson = parse.listToJson(list2);
        //записываем файл json
        parse.writeString(xmlJson, "data2.json");

    }
}
