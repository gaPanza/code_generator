package screens;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang3.text.WordUtils;

public class ProcessRequests {
	static File txtToWrite = new File(System.getProperty("user.home") + "\\IPNET"
			+ ("\\ipnetCode" + new Date().toString().trim().replaceAll(" ", "_").replaceAll(":", "_") + ".txt"));
	static BufferedWriter bw = null;
	public static void generateCode(HashMap<String, String> map, ArrayList<String> fields) throws IOException {
		StringBuffer buffer = new StringBuffer();
		ArrayList<String> fieldsReverse = new ArrayList<String>();
		FileWriter fw = new FileWriter(txtToWrite);
		bw = new BufferedWriter(fw);
		txtToWrite.createNewFile();
		
		generateReportToExport(fields, buffer);
		buffer = new StringBuffer();
		findAllForMonthReport(map, fields, buffer, fieldsReverse);
		dto(fieldsReverse);
		bw.flush();
		
	}

	private static void dto(ArrayList<String> fieldsReverse) {
		StringBuffer buffer;
		String thisnewOne;
		buffer = new StringBuffer();
		
		for(String x : fieldsReverse){
			thisnewOne = "public String " + x + ";\n";
			buffer.append(thisnewOne);
		}
		// Gerar o DTO
		buffer.append("\n\n");
		thisnewOne = "dataCell = dataRow.createCell(dataRow.getLastCellNum());\ndataCell.setCellValue(reportScheduleDTO.";
		for(String x : fieldsReverse){
			x = Character.toUpperCase(x.charAt(0)) + x.substring(1);
			String aux = "get"+ x+"());\n";
			String created = thisnewOne +aux;
			buffer.append(created);
		}
		System.out.println(buffer.toString());
		writeToTxt(buffer);
	}

	private static void findAllForMonthReport(HashMap<String, String> map, ArrayList<String> fields,
		StringBuffer buffer, ArrayList<String> fieldsReverse) {
		String thisnewOne;
		thisnewOne = "String querySql = \" SELECT DISTINCT \" \n + ";
		buffer.append(thisnewOne);

		for (int i = 0; i < map.size(); i++) {
			
			String loseYourself = "\"\\\""; //  "\"
			String anotherOne = map.get(fields.get(i));
			String ending = "\\\"";
			String as = " AS ";
			String ending2 = "\" , \" \n + ";
			String anotherTwo = formatMyField(anotherOne);
			
			
			if(i+1 == map.size()){
				if(anotherOne.equals("locationCheckIn")){
					buffer.append("\"ST_Y(\"locationCheckIn\")||','||ST_X(\"locationCheckIn\") AS locationCheckInText\" \n + ");
					fieldsReverse.add(anotherOne);
					continue;
				}
				buffer.append(loseYourself);
				buffer.append(anotherOne);
				
				if(!anotherOne.equals(anotherTwo)){
					buffer.append(ending);
					buffer.append(as);
					buffer.append(anotherTwo);
					ending2 =  "\\\" \n + ";
					buffer.append(ending2);
					fieldsReverse.add(anotherTwo);
				}else{
					ending = "\\\" \n + ";
					buffer.append(ending);
					fieldsReverse.add(anotherOne);
				}
			}else{
				if(anotherOne.equals("locationCheckIn")){
					buffer.append("\"ST_Y(\"locationCheckIn\")||','||ST_X(\"locationCheckIn\") AS locationCheckInText\", \" \n + ");
					fieldsReverse.add(anotherOne);
					continue;
				}
				buffer.append(loseYourself);
				buffer.append(anotherOne);
				buffer.append(ending);
				if(!anotherOne.equals(anotherTwo)){
					buffer.append(as);
					buffer.append(anotherTwo);
					buffer.append(ending2);
					fieldsReverse.add(anotherTwo);
					
				}else{
					buffer.append(ending2);
					fieldsReverse.add(anotherOne);
				}
			}
		}
		
		thisnewOne = "\"FROM vw_month_report_mt WHERE schedule_time BETWEEN ? AND ?\";\n\n";
		
		buffer.append(thisnewOne);
		
		thisnewOne ="Session currentSession = hibernate.getSessionFactory().getCurrentSession(); \n	Query query = currentSession.createSQLQuery(querySql)\n";
		buffer.append(thisnewOne);
		for (String x : fieldsReverse){
			thisnewOne = ".addScalar(\"";
			thisnewOne = thisnewOne + x + "\", StandardBasicTypes.TEXT)\n";
			buffer.append(thisnewOne);
		}
		buffer.append(";");
		buffer.append("\n\n");
		
		buffer.append("query.setParameter(0, new Date(start.getTime()));\nquery.setParameter(1, new Date(end.getTime()));\nquery.setFirstResult(offset);\nquery.setMaxResults(limit);\nquery.setResultTransformer(Transformers.aliasToBean(ReportScheduleDTO.class));\nreturn query.list();");
		buffer.append("\n\n");
		System.out.println(buffer.toString());
		writeToTxt(buffer);
	}

	private static String formatMyField(String field){
		String retorno;
		
		if(Character.isDigit(field.charAt(0))){
			retorno = field.substring(1,field.length());
			return formatMyField(retorno);
		}
		if(Character.toLowerCase(field.charAt(0)) == '.'){
			retorno = field.replace(".", "");
			return formatMyField(retorno);
		}
		if(field.contains(" ")){
			 retorno = WordUtils.capitalizeFully(field);
			 retorno = retorno.replaceAll(" ", "");
			 retorno = Character.toLowerCase(retorno.charAt(0)) + retorno.substring(1);
			 //Retorna uma string nos padrões Java Beans
			 return formatMyField(retorno);
		}
		return field;
	}
	
	private static void generateReportToExport(ArrayList<String> fields, StringBuffer buffer) {
		// Gera o generateReportToExport
		String thisnewOne = "String[] headers = { ";
		buffer.append(thisnewOne);

		for (int i = 0; i < fields.size(); i++) {
			String againANewOne = "\""; // Colocar Aspas numa String
			buffer.append(againANewOne); // Abriu aspas
			buffer.append(fields.get(i)); // Inseriu Campo
			if (i + 1 == fields.size()) {
				againANewOne = "\""; // Inseriu uma aspas final e uma virgula
				buffer.append(againANewOne);
				break;
			}
			againANewOne = "\","; // Inseriu uma aspas final e uma virgula
			buffer.append(againANewOne);
		}

		thisnewOne = "};";
		buffer.append(thisnewOne);
		System.out.println(buffer.toString());
		writeToTxt(buffer);
	}

	private static void writeToTxt(StringBuffer buffer) {
		try {
			String updatedText = buffer.toString();
			if(updatedText.contains("\n"))
				updatedText = updatedText.replaceAll("\n", System.lineSeparator());
			bw.write(updatedText);
		} catch (IOException e) {
			e.printStackTrace();
			
		}

	}

}
