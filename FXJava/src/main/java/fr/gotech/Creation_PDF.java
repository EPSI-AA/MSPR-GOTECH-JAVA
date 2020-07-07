package fr.gotech;

import com.google.common.collect.Table;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.TextAlignment;
import fr.gotech.model.Client;
import fr.gotech.model.Equipement;
import fr.gotech.model.Interface;

import java.io.IOException;
import java.util.List;

public class Creation_PDF {

            public static void Creation(String Path, String clientID, Client client, Equipement equipement, List<Interface> interfaces) throws IOException {


                String dest = "/home/advance/EPSI/document.pdf";
                if(!Path.equals("")){
                    dest=Path;
                }
                PdfWriter writer = new PdfWriter(dest);
                PdfDocument pdfDoc = new PdfDocument(writer);
                //PdfPage page = pdfDoc.addNewPage();
                Document document = new Document(pdfDoc);
                PdfFont bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
                Text tclient = new Text("Client : \n").setFont(bold);
                //Text tnom = new Text("Nom :").setFont(bold);
                tclient.setFontSize(24);
                String test = "Nom :" + clientID + "\n Adresse : " + client.getAdresse() + "\n" + "Code Postal : " + client.getCp() + "\n" +
                        "Ville : " + client.getVille() + "\n" + "Téléphone : " + client.getTel() + "\n" + "Email : " + client.getEmail();
                Paragraph paragraphclient = new Paragraph();
                paragraphclient.add(tclient);
                paragraphclient.add(test);
                paragraphclient.setTextAlignment(TextAlignment.CENTER);
                //paragraphclient.setBorder(new SolidBorder(2));
                document.add(paragraphclient);
                ImageData logoData = ImageDataFactory.create("src/main/resources/fr/gotech/css/images/ic_launcher_round.png");
                Image logo = new Image(logoData);
                logo.scaleAbsolute(100,100);
                logo.setFixedPosition(10,730);
                document.add(logo);
                Text tEquipement= new Text("Equipement : \n").setFont(bold);
                tEquipement.setFontSize(24);
                String InfoEquipement = "Hostname : " + equipement.getHostname() + "\n" + "Date d'achat : " + equipement.getAchat() + "\n" + "Etat : " + equipement.getEtat() + "\n"
                        + "Date de fin de garantie : " + equipement.getGarantie() + "\n" + "Date d'installation : " + equipement.getInstall()
                        + "\n" + "Type : " + equipement.getType() + "\n" + "Marque : " + equipement.getMarque() + "\n" + "Modele : " + equipement.getModele() + "\n" + "SN : " + equipement.getSn();
                Paragraph paragraphequipement = new Paragraph();
                //paragraphequipement.setBorder(new SolidBorder(2));
                paragraphequipement.add(tEquipement);
                paragraphequipement.add(InfoEquipement);
                paragraphequipement.setTextAlignment(TextAlignment.CENTER);
                document.add(paragraphequipement);
                if(interfaces.size() > 0) {
                    //Gestion de la liste d'interfaces
                    com.itextpdf.layout.element.Table table = new com.itextpdf.layout.element.Table(interfaces.size());
                    System.out.println(interfaces.size());
                    for (int i = 0; i < interfaces.size(); i++) {
                        Text tInterface = new Text("Interface " + i + " : \n").setFont(bold);
                        tInterface.setFontSize(16);
                        String infoInterface = "Nom : " + interfaces.get(i).getName() + "\nAdressage IP : " + interfaces.get(i).getIp() + interfaces.get(i).getCidr() + "\n" +
                                "Passerelle par défaut : " + interfaces.get(i).getGw() + "\n" + "DNS1 : " + interfaces.get(i).getDns1() + "\n"
                                + "DNS2 : " + interfaces.get(i).getDns2() + "\n";
                        System.out.println(infoInterface);
                        System.out.println(interfaces.get(i).getName());
                        Paragraph paragraphinterface = new Paragraph();
                        paragraphinterface.add(tInterface);
                        paragraphinterface.add(infoInterface);
                        Cell cell = new Cell(1, i);
                        cell.add(paragraphinterface);
                        cell.setBorder(Border.NO_BORDER);
                        table.addCell(cell);
                    }
                    table.setBorder(Border.NO_BORDER);
                    document.add(table);
                }
                //-----------------------------------------
                Image qrCode = QRcode.creaQRCODE();
                //ImageData data = ImageDataFactory.create(imageFile);
                //Image qrCode = new Image(data);
                qrCode.setBorder(new SolidBorder(2));
                qrCode.scaleAbsolute(200,200);
                //qrCode.setFixedPosition(1000,1000);
                qrCode.setMarginLeft(160);
                Paragraph paragraphqr = new Paragraph("");
                paragraphqr.add(qrCode);
                document.add(paragraphqr);
                document.close();
                System.out.println("PDF Created");
                System.out.println(dest);
            }
        }


