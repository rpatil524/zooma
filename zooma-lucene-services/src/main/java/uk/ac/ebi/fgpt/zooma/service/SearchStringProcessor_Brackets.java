

package uk.ac.ebi.fgpt.zooma.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import uk.ac.ebi.fgpt.zooma.util.SearchStringProcessor;

/** 
 * This class handles the processing of strings with brackets.   
 * @author Jose Iglesias
 * @date 16/08/13         */

public class SearchStringProcessor_Brackets implements SearchStringProcessor{


    @Override
    public float getBoostFactor() {
        return 0.95f;
    }

    
    /**
    * Returns true if the property value contains brackets and they don't belong to a compound.
    * Brackets of compounds mustn't be removed (e.g: 4-(N-nitrosomethylamino)-1-(3-pyridyl)butan-1-one  )
    *
    * Returns false otherwise.   */
    
    @Override
    public boolean canProcess(String searchString, String type) {
        
        if(  searchString.contains("(")   &&   searchString.contains(")")  ){

            //Brackets of compounds mustn't be removed (e.g: 4-(N-nitrosomethylamino)-1-(3-pyridyl)butan-1-one  )

            //Two patterns to identify compounds: 
            String pattern1_compound = ".{0,100}\\(.{1,100}\\)\\S{1,100}.{0,100}";
            String pattern2_compound = ".{0,100}\\S{1,100}\\(.{1,100}\\).{0,100}";

            //Check if string would be a compound..
            if (! (Pattern.matches( pattern1_compound , searchString) || Pattern.matches( pattern2_compound , searchString) ) ){
                
                return true;
            }           
        }
        
        return false; 
    }

    
    
    /** Takes a string and removes the brackets and its content*/
    
    @Override
    public List<String> processSearchString(String searchString) throws IllegalArgumentException {
        
        ArrayList<String> processedStrings = new ArrayList<String>();
        
        String processedString="";

        int pos_ini = searchString.indexOf("(");   
        int pos_fin = searchString.indexOf(")");

        String content = "\\" + searchString.substring(pos_ini, pos_fin)  + "\\)";
                
        content = content.replaceAll("\\+", "\\\\+");
        content = content.replaceAll("\\*", "\\\\*");


        processedString = searchString.replaceAll( content, " ");

        //Avoid irrelevant spaces
        if (processedString.contains("   "))
            processedString = processedString.replace("   ", " ");

        if (processedString.contains("  "))
            processedString = processedString.replace("  ", " ");

        if (processedString.endsWith(" "))
            processedString = processedString.substring(0, processedString.length()-1);

        if (processedString.startsWith(" "))
            processedString = processedString.substring(1, processedString.length());
        
        
        processedStrings.add(processedString);

        return processedStrings;  
    }

    

}