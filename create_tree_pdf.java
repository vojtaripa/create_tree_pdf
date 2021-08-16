import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

/********************************************************************************************************************
 * What does this project do?
 * take a CSV file called trees.csv and converts it to PDF (making a table with the data) to a file called trees.pdf
 ********************************************************************************************************************/
public class create_tree_pdf 
{

    //SOURCE FILE:
    public static final String DATA = "src/main/resources/data/trees.csv";
    
    //DESTINATION FILE:
    public static final String DEST = "results/trees.pdf";

    //MAIN FUNCTION!
    public static void main(String args[]) throws IOException 
    {
        //creating a new file in the desired path
        File file = new File(DEST);
        
        //get the directory the file will be in
        file.getParentFile().mkdirs();
        
        //creates the new PDF in the directory
        new create_tree_pdf().createPdf(DEST);
    }

    //create PDF function (throw error if blank)
    public void createPdf(String dest) throws IOException 
    {
        //Initialize PDF writer
        PdfWriter writer = new PdfWriter(dest);

        //Initialize PDF document
        PdfDocument pdf = new PdfDocument(writer);

        // Initialize document
        Document document = new Document(pdf, PageSize.A4.rotate());
        
        //set document margins
        document.setMargins(10, 10, 10, 10);

        //setting document font
        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        
        //setting doument bold font
        PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        
        //creating a new table and setting the size
        Table table = new Table(UnitValue.createPercentArray(new float[]{4, 1, 3, 4}))
                .useAllAvailableWidth();
        
        //renders each line
        BufferedReader br = new BufferedReader(new FileReader(DATA));
        
        //takes in new line
        String line = br.readLine();
        
        //takes the input and processes them
        process(table, line, bold, true);
        
        //looping through each line
        while ((line = br.readLine()) != null) 
        {
            process(table, line, font, false);
        }
        
        //closing the line reader
        br.close();

        //add table to the document
        document.add(table);

        //Close document
        document.close();
    }

    //process function:
    public void process(Table table, String line, PdfFont font, boolean isHeader) 
    {
        StringTokenizer tokenizer = new StringTokenizer(line, ";");
        while (tokenizer.hasMoreTokens()) 
        {
            if (isHeader) 
            {
                table.addHeaderCell(new Cell().add(new Paragraph(tokenizer.nextToken()).setFont(font)));
            } else 
            {
                table.addCell(new Cell().add(new Paragraph(tokenizer.nextToken()).setFont(font)));
            }
        }
    }
}
