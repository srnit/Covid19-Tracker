package com.example.covid19tracker.services;

import com.example.covid19tracker.models.LocationStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.validation.constraints.Email;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class CoronaVirusDataService {
    private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
    List<LocationStats>allstats=new ArrayList<>();

    public List<LocationStats> getAllstats() {
        return allstats;
    }

    @PostConstruct
 @Scheduled(cron = "* * * 1 * *")
 public void fetchVirusData() throws IOException, InterruptedException {
      HttpClient client = HttpClient.newHttpClient();
      HttpRequest request = HttpRequest.newBuilder()
              .uri(URI.create(VIRUS_DATA_URL))
              .build();
      HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
      //System.out.println(httpResponse.body());
      StringReader csvBodyReader = new StringReader(httpResponse.body());
     Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
     List<LocationStats>newstats=new ArrayList<>();
     for (CSVRecord record : records) {
         //System.out.println(record.get("Province/State"))

         LocationStats locationStat = new LocationStats();
         locationStat.setState(record.get("Province/State"));
         locationStat.setCountry(record.get("Country/Region"));
         //System.out.println(record.get(record.size()-1));
         int latestcase=Integer.parseInt(record.get(record.size()-1));
         locationStat.setLatestTotalCases(latestcase);
         int prevdaycases=Integer.parseInt(record.get(record.size()-2));
         locationStat.setDiffFromPrevDay(latestcase-prevdaycases);
         newstats.add(locationStat);
     }
     this.allstats=newstats;

  }
}
