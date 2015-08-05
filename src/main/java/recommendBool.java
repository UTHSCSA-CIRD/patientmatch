import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.GenericBooleanPrefDataModel;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import sun.net.www.content.text.Generic;

import java.io.*;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import static java.lang.System.out;
import java.util.*;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

/**
 * Created by mahout on 7/14/15.
 */


public class recommendBool {
    /**
     * command line arguments:
     * pcodes: csv file containing a patient_num and a code on each line, currently hardcoded to patients_nomods.csv
     * patients: file containing a unique set of patient_num's representing the disease group
     */
    public static void main(String[] args) throws Exception {


        String splitBy = ",";
        BufferedReader br = new BufferedReader(new FileReader("allpatients.csv"));
        String line;
        PrintWriter writer = new PrintWriter("data2.csv", "UTF-8");
        while ((line = br.readLine()) != null) {
            if (line.isEmpty()) {
                continue;
            }
            String[] b = line.split(splitBy);
            writer.println(b[0] + "," + b[1].hashCode());
        }
        writer.close();
        br.close();


        ArrayList<Long> sick = new ArrayList<Long>();
        Scanner scan = new Scanner(new File("sick_patients.txt"));
        while (scan.hasNext()) {
            sick.add(scan.nextLong());
        }
        scan.close();


        // int[] sickids = SOMETHINGTHATREADSTHE_PATIENTS_FILEINTOANARRAY;

        DataModel model = new GenericBooleanPrefDataModel(
                GenericBooleanPrefDataModel.toDataMap(
                        new FileDataModel(new File("data2.csv"))));
        UserSimilarity similarity = new LogLikelihoodSimilarity(model);
        //UserSimilarity similarity = new TanimotoCoefficientSimilarity(model);
        //Gets a neighborhood of 1, calculates similarity based on the model
        UserNeighborhood neighborhood = new NearestNUserNeighborhood(1, similarity, model);
        // hopefully the below can be an int array
        // somehow have ii created out of sickids[]
        //LongPrimitiveIterator ii = model.getUserIDs();
        int i = 1;
        ArrayList<Long> errors = new ArrayList<Long>();
        writer = new PrintWriter("LogLikelihood.csv", "UTF-8");
        PrintWriter err = new PrintWriter("Error.csv", "UTF-8");
        writer.println("row_no,patient_id,neighbor_id,score");
        long[] myHood;


        for (Long temp : sick) {
            //out.println(temp);
            myHood = neighborhood.getUserNeighborhood(temp);
            out.println(temp + " " +myHood.length);
            for (Long control : myHood) {
                out.println("\t" + control + " " + similarity.userSimilarity(temp, control));
            }
            for (Long control : myHood) {
                if (sick.contains(control)) {
                    continue;
                }
                writer.println(temp + "," + control + "," + similarity.userSimilarity(temp, control));
                break;
            }
        }
        writer.close();
    }
}
                //for(int temp=0; temp < sick.size(); temp++){
                /*   LongPrimitiveIterator ii = model.getUserIDs();
                   while(ii.hasNext()) {
                       Long userid = ii.next();
                       //use this as the parameter
                       //sick.contains(userid);
                       out.println(sick.get(temp) + ":" + userid);
                       //Computes a "neighborhood" of users like a given user.
                       myHood = neighborhood.getUserNeighborhood(userid);   //ID of user for which a neighborhood will be computed
                       // do whatever is appropriate to exclude the values in sickids[] from myHood[]
                       if(sick.get(temp) != userid) {
                            try {
                                //usersimilarity define a notion of similarity between two users.
                                writer.println(i + "," + userid + "," + myHood[0] + "," + similarity.userSimilarity(userid, myHood[0]));
                                //break;
                            } catch (IndexOutOfBoundsException e) {
                                errors.add(userid);
                                writer.println(i + "," + userid);
                            }
                       }
                       else {
                           out.println("These are the same ids " + sick.get(temp));
                       }
                   }

                   //i++;

                  // out.println(i);
                  */
              // }
                //err.close();

               // out.println(errors.get(0));



/*
                    Recommender recommender= new GenericUserBasedRecommender(model, neighborhood, similarity);

        List<RecommendedItem> recommendations = recommender.recommend(1, 1);
        */
/*
        for (RecommendedItem recommendation : recommendations) {
            out.println(model.getNumUsers());
            out.println(similarity);
            out.println(recommendation);
        }
        */
/*
    }
}
*/