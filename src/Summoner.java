import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

public class Summoner {

	private String name;
	private String key = "RGAPI-91eb14e9-5317-4aaf-9e9d-69434e9862e5";
	private String reg;
	private String bigReg;
	private String apiName;
	private String soloRank;
	private String flexRank;
	private String soloLp;
	private String flexLp;
	private String soloWins;
	private String soloLosses;
	private String flexWins;
	private String flexLosses;
	private String soloWr;
	private String flexWr;
	private String soloPromos;
	private String flexPromos;

	public Summoner(String name, String reg, String apiName) throws NotValidRegionException, NotValidNameException {
		this.apiName = apiName;

		if (reg.equals("EUW")) {
			this.reg = "euw1";
		}else if(reg.equals("EUNE")) {
			this.reg = "eun1";
		}
		else if(reg.equals("BR")) {
			this.reg = "br1";
		}
		else if(reg.equals("JP")) {
			this.reg = "jp1";
		}
		else if(reg.equals("KR")) {
			this.reg = "kr";
		}
		else if(reg.equals("LAN")) {
			this.reg = "la1";
		}
		else if(reg.equals("LAS")) {
			this.reg = "la2";
		}
		else if(reg.equals("NA")) {
			this.reg = "na1";
		}
		else if(reg.equals("OCE")) {
			this.reg = "oc1";
		}
		else if(reg.equals("RU")) {
			this.reg = "ru";
		}
		else if(reg.equals("TUR")) {
			this.reg = "tr1";
		}else {
			throw new NotValidRegionException("Region does not exist: " + reg);
		}

		if(this.reg.equals("euw1")||this.reg.equals("eun1")||this.reg.equals("ru")||this.reg.equals("tr1")) {
			this.bigReg = "europe";
		}else
			if(this.reg.equals("na1")||this.reg.equals("la1")||this.reg.equals("la2")||this.reg.equals("br1")||this.reg.equals("oc1")) {
				this.bigReg = "americas";
			}else
				if(this.reg.equals("jp1")||this.reg.equals("kr")) {
					this.bigReg = "europe";
				}else {
					throw new NotValidRegionException("Region does not exist: " + reg);
				}

		try {
			JSONObject json = JsonReader.readJsonFromUrl("https://"+this.reg+".api.riotgames.com/lol/summoner/v4/summoners/by-name/"+this.apiName+"?api_key="+key);
			this.name = json.getString("name");	
		} 
		catch (JSONException | IOException e) {
			throw new NotValidNameException("Name " +name+"does not exist on "+reg);
		}
		
		String[] rankedData = getRankedData(getSummonerData("id"));
		if (rankedData[0] != null) {
			System.out.println(rankedData[0].split("\\s+")[0]);
			this.soloRank = rankedData[0].split("\\s+")[0]+" "+rankedData[0].split("\\s+")[1];
			this.soloLp = rankedData[0].split("\\s+")[2].split("L")[0];
			}else {
				this.soloRank = "unranked";
				this.soloLp = "unranked";
			}
		
		if (rankedData[1] != null) {
			this.soloPromos = rankedData[1];
			}else {
				this.soloPromos = "Not in promos";
			}
		
		if (rankedData[2] != null) {
			this.soloWins = rankedData[2];
			}else {
				this.soloWins = "0";
			}
		
		if (rankedData[3] != null) {
			this.soloLosses = rankedData[3];
			this.soloWr = ""+Math.round(Double.parseDouble(soloWins)/(Double.parseDouble(soloWins)+Double.parseDouble(soloLosses))*100);
			}else {
				this.soloLosses = "0";
				this.soloWr = "0";
			}
		
		if (rankedData[4] != null) {
			this.flexRank = rankedData[4].split("\\s+")[0]+" "+rankedData[4].split("\\s+")[1];
			this.flexLp = rankedData[4].split("\\s+")[2].split("L")[0];
			}else {
				this.flexRank = "unranked";
				this.flexLp = "unranked";
			}
		
		if (rankedData[5] != null) {
			this.flexPromos = rankedData[5];
			}else {
				this.flexPromos = "Not in promos";
			}
		

		if (rankedData[6] != null) {
			this.flexWins = rankedData[6];
			}else {
				this.flexWins = "0";
			}
		
		if (rankedData[7] != null) {
			this.flexLosses = rankedData[7];
			this.flexWr = ""+Math.round(Double.parseDouble(flexWins)/(Double.parseDouble(flexWins)+Double.parseDouble(flexLosses))*100);
			}else {
				this.flexLosses = "0";
				this.flexWr = "0";
			}

	}

	public String getName() {
		return name;
	}

	public String getReg() {
		return reg;
	}
	
	public String getSoloRank() {
		return soloRank;
	}
	
	public String getflexRank() {
		return flexRank;
	}
	
	public String getSoloLP() {
		return soloLp;
	}
	
	public String getFlexLP() {
		return flexLp;
	}
	
	public String getSoloW() {
		return soloWins;
	}
	
	public String getSoloL() {
		return soloLosses;
	}
	
	public String getFlexW() {
		return flexWins;
	}
	
	public String getFlexL() {
		return flexLosses;
	}
	
	public String getSoloWr() {
		return soloWr;
	}
	
	public String getFlexWr() {
		return flexWr;
	}
	
	public String getSoloPromos() {
		return soloPromos;
	}
	
	public String getFlexPromos() {
		return flexPromos;
	}
	


	//can get id, puuid, name
	public String getSummonerData(String data)  {
		JSONObject json;
		try {
			json = JsonReader.readJsonFromUrl("https://"+reg+".api.riotgames.com/lol/summoner/v4/summoners/by-name/"+apiName+"?api_key="+key);
			return json.getString(data);
		}  catch (JSONException | IOException e) {
			throw new java.lang.IllegalArgumentException("Most likely gamer not in region:" +reg);
		}

	}	

	// can get summonerLevel, profileIconId
	public int getIntSummonerData(String data) {
		JSONObject json;
		try {
			json = JsonReader.readJsonFromUrl("https://"+reg+".api.riotgames.com/lol/summoner/v4/summoners/by-name/"+apiName+"?api_key="+key);
			return (int) json.get(data);
		} catch (IOException | JSONException e) {
			throw new java.lang.IllegalArgumentException("Most likely gamer not in region:" +reg);
		}
	}

	public String[] getRankedData(String id)throws NotValidRegionException {
		try {
			URL url = new URL("https://"+reg+".api.riotgames.com/lol/league/v4/entries/by-summoner/"+id+"?api_key="+key);
			Scanner sc = new Scanner(url.openStream());
			StringBuffer sb = new StringBuffer();
			while(sc.hasNext()) {
				sb.append(sc.next());
			}
			//Retrieving the String from the String Buffer object
			String stringMatches = sb.toString();
			JSONArray json_arr = new JSONArray(stringMatches);	
			String[] data = new String[8];
			for(int i=0;i<json_arr.length();i++){         // OR iterate
				JSONObject tmp = (JSONObject) json_arr.get(i);
				if(tmp.get("queueType").equals("RANKED_SOLO_5x5")) {
					try {
					data[0] = (String) tmp.get("tier")+" "+(String) tmp.get("rank") +" "+tmp.get("leaguePoints")+"LP";
					data[2] = ""+tmp.get("wins");
					data[3] = ""+tmp.get("losses");
					}catch (Exception e) {
						data[0] = "";
					}
					try {
						data[1] = tmp.getJSONObject("miniSeries").getString("progress");
					}catch(Exception e) {
						data[1] = null;
					}
				}else if(tmp.get("queueType").equals("RANKED_FLEX_SR")){
					try {
					data[4] = (String) tmp.get("tier")+" "+(String) tmp.get("rank")+" "+tmp.get("leaguePoints")+"LP";
					data[6] = ""+tmp.get("wins");
					data[7] = ""+tmp.get("losses");
					}catch(Exception e) {
						data[4] = "";
					}
					try {
						data[5] = tmp.getJSONObject("miniSeries").getString("progress");
					}catch(Exception e) {
						data[5] = null;
					}
				}
			} 
			return data;
		}catch(Exception e) {
			throw new NotValidRegionException("Something went wrong");
		}
	}

	//returns summoners matchIds	
	public LinkedList<String> getMathcIds(String data) throws IOException, JSONException {
		URL url = new URL("https://"+bigReg+".api.riotgames.com/lol/match/v5/matches/by-puuid/"+getSummonerData("puuid")+"/ids?start=0&count=20&api_key="+key);
		Scanner sc = new Scanner(url.openStream());
		StringBuffer sb = new StringBuffer();
		while(sc.hasNext()) {
			sb.append(sc.next());
		}	
		//Retrieving the String from the String Buffer object
		String stringMatches = sb.toString();
		//System.out.println(stringMatches);

		boolean found = false;
		LinkedList<String> ll = new LinkedList<String>();
		StringBuilder stringBuild = new StringBuilder();
		for(int i = 0; i < stringMatches.length();i++) {
			if(stringMatches.charAt(i)=='"'){
				while(!found) {
					i+=1;
					if(stringMatches.charAt(i)=='"') {
						found=true;
					}else {
						stringBuild.append(stringMatches.charAt(i));
					}
				}
				ll.add(stringBuild.toString());
				stringBuild.setLength(0);
				found = false;
			}  
		}
		return ll;		
	}	

	public String toString() {
		try {
			String id = getSummonerData("id");
			String [] data = getRankedData(id);
			String soloqWr = "";
			String flexqWr = "";			
			if(data[0] == null) {
				data[0] = "unranked";
			}
			if(data[4] == null) {
				data[4] = "unranked";
			}

			try {
				soloqWr = Math.round(Double.parseDouble(data[2])/(Integer.parseInt(data[2])+Integer.parseInt(data[3]))*100)+"%wr";
				if(data[1] != null) {
					soloqWr = soloqWr+" Promos: "+data[1];
				}
			}catch (Exception e) {
				soloqWr = "";
			}
			try {
				flexqWr =Math.round(Double.parseDouble(data[6])/(Integer.parseInt(data[6])+Integer.parseInt(data[7]))*100)+"%wr";
				if(data[5] != null) {
					soloqWr = soloqWr+" Promos: "+data[5];
				}
			}catch(Exception e) {
				flexqWr = "";
			}
			return name + " , lvl "+getIntSummonerData("summonerLevel")+" , SoloQ: "+data[0] + " "+soloqWr+", FlexQ: "+data[4] + " "+flexqWr;
		}catch(Exception e){
			throw new java.lang.IllegalArgumentException("Gamer does not exist");
		}
	}

}
