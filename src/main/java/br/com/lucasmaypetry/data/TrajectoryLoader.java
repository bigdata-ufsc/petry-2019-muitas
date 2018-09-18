package br.com.lucasmaypetry.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import br.com.lucasmaypetry.base.Feature;
import br.com.lucasmaypetry.base.Point;
import br.com.lucasmaypetry.base.Trajectory;
import br.com.lucasmaypetry.base.config.ExperimentConfiguration;
import br.com.lucasmaypetry.base.config.FeatureConfiguration;
import br.com.lucasmaypetry.utils.Logger;
import br.com.lucasmaypetry.utils.Logger.Type;

public class TrajectoryLoader {
	
	private String file;
	
	public TrajectoryLoader(String file) {
		this.file = file;
	}

	public List<Trajectory> load(ExperimentConfiguration config) {
		BufferedReader br = null;
		String line = "";
		String csvSplitBy = ",";

		List<Trajectory> trajectories = new ArrayList<>();

		int tid = -1;
		String label = "";
		List<Point> points = new ArrayList<>();

		try {
			br = new BufferedReader(new FileReader(this.file));
			
			String[] header = br.readLine().split(csvSplitBy);
			Map<String, Integer> fMap = getFeatureIndexMap(header);
			Map<String, FeatureConfiguration> cfgFeatures = config.getFeatures();
			List<String> features = new ArrayList<>(cfgFeatures.keySet());
			
			int tidIdx = fMap.get(config.getTid());
			int labelIdx = fMap.get(config.getLabel());
			
			while ((line = br.readLine()) != null) {
				String[] columns = line.replaceAll("\"", "").split(csvSplitBy);
				int newTid = Integer.parseInt(columns[tidIdx]);

				if (newTid != tid) {
					if (!points.isEmpty()) { // Store trajectory
						trajectories.add(new Trajectory(tid, points, label));
					}

					tid = newTid;
					label = columns[labelIdx].trim();
					points = new ArrayList<>();
				}
				List<Feature> procFeatures = new ArrayList<>();
				
				for(String feature : features) {
					procFeatures.add(getFeature(feature,
												columns[fMap.get(feature)].trim(),
												cfgFeatures.get(feature)));
				}

				points.add(new Point(procFeatures));
			}

			if (!points.isEmpty()) { // Store last trajectory
				trajectories.add(new Trajectory(tid, points, label));
			}
		} catch (IOException e) {
			Logger.log(Type.ERROR, e.getMessage());
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					Logger.log(Type.ERROR, e.getMessage());
					e.printStackTrace();
				}
			}
		}

		return trajectories;
	}
	
	private Feature getFeature(String name, String value, FeatureConfiguration featureCfg) {
		Object castValue = value;
		
		switch(featureCfg.getType()) {
		case CHARACTER:
			castValue = value.charAt(0);
			break;
		case INTEGER:
		case DOUBLE:
			castValue = Double.parseDouble(value);
			break;
		case DATE:
			SimpleDateFormat formatter = new SimpleDateFormat(featureCfg.getFormat(), Locale.ENGLISH);
			try {
				castValue = formatter.parse(value);
			} catch (ParseException e) {
				Logger.log(Type.ERROR, e.getMessage());
				e.printStackTrace();
			}
			break;
		default:
		}
		
		return new Feature(name, featureCfg.getType(), castValue);
	}
	
	private Map<String, Integer> getFeatureIndexMap(String[] features) {
		Map<String, Integer> map = new HashMap<>();
		
		for(int i = 0; i < features.length; i++) {
			map.put(features[i], i);
		}
		
		return map;
	}
	
}
