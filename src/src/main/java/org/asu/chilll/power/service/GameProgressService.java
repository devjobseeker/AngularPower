package org.asu.chilll.power.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.asu.chilll.power.dataview.GameProgressDataView;
import org.asu.chilll.power.entity.GameProgress;
import org.asu.chilll.power.entity.StudentProfile;
import org.asu.chilll.power.enums.GameStatusType;
import org.asu.chilll.power.repository.GameRepository;
import org.asu.chilll.power.repository.StudentProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameProgressService {
	
	@Autowired
	private GameRepository gameRepo;
	
	@Autowired
	private StudentProfileRepository profileRepo;
	
	private static final int FinalReward = 5;
	
	public void createGameProgress(String childId, String gameId, String grade) {
		GameProgress progress = new GameProgress();
		progress.setChildId(childId);
		progress.setGameId(gameId);
		progress.setGrade(grade);
		progress.setGameStatus(GameStatusType.Start.toString());
		gameRepo.createGameProgress(progress);
	}
	
	public GameProgressDataView fetchGameProgress(String childId, String grade, String gameId) {
		GameProgress dbProgress = gameRepo.fetchGameProgress(childId, grade, gameId);
		GameProgressDataView progress = new GameProgressDataView();
		if(dbProgress != null) {
			progress.setChildId(dbProgress.getChildId());
			progress.setCurrentListIndex(dbProgress.getNextListIndex());
			progress.setCurrentGroupIndex(dbProgress.getCurrentGroupIndex());
			progress.setGameStatus(dbProgress.getGameStatus());
			progress.setRocks(dbProgress.getRocks());
			progress.setCoins(dbProgress.getCoins());
			progress.setRepetitionCount(dbProgress.getRepetitionCount());
			progress.setNumOfBoxes(dbProgress.getNumOfBoxes());
			progress.setTotalCorrectCount(dbProgress.getTotalCorrectCount());
			progress.setTotalTrialCount(dbProgress.getTotalTrialCount());
			if(dbProgress.getConsecutiveTrialResult() != null && !dbProgress.getConsecutiveTrialResult().equals("")) {
				List<Integer> trialResult = new ArrayList<Integer>();
				for(int i = 0; i < dbProgress.getConsecutiveTrialResult().length(); i++) {
					String isCorrect = dbProgress.getConsecutiveTrialResult().charAt(i) + "";
					trialResult.add(Integer.parseInt(isCorrect));
				}
				progress.setConsecutiveTrialResult(trialResult);
			}
		}else {
			progress.setGameStatus(GameStatusType.Start.toString());
		}
		
		return progress;
	}
	
	public void updateGameProgress(GameProgressDataView progress, Optional<Integer> repetitionCount) {
		GameProgress dbProgress = gameRepo.fetchGameProgress(progress.getChildId(), progress.getGrade(), progress.getGameId());
		if(dbProgress == null) {
			dbProgress = new GameProgress();
			dbProgress.setChildId(progress.getChildId());
			dbProgress.setGameId(progress.getGameId());
			dbProgress.setGrade(progress.getGrade());
			dbProgress.setGameStatus(progress.getGameStatus());
			if(repetitionCount.isPresent()) {
				dbProgress.setRepetitionCount(repetitionCount.get());
			}
			gameRepo.createGameProgress(dbProgress);
		}else {
			if(repetitionCount.isPresent()) {
				dbProgress.setRepetitionCount(repetitionCount.get());
			}
			dbProgress.setGameStatus(progress.getGameStatus());
			gameRepo.updateGameProgress(dbProgress);
		}
	}
	
	public void updateNumberUpdateGamesProgress(GameProgressDataView progress) {
		GameProgress dbProgress = gameRepo.fetchGameProgress(progress.getChildId(), progress.getGrade(), progress.getGameId());
		if(dbProgress == null) {
			dbProgress = new GameProgress();
			dbProgress.setChildId(progress.getChildId());
			dbProgress.setGameId(progress.getGameId());
			dbProgress.setGrade(progress.getGrade());
			dbProgress.setGameStatus(progress.getGameStatus());
			dbProgress.setNumOfBoxes(progress.getNumOfBoxes());
			dbProgress.setNextListIndex(progress.getCurrentListIndex() + 1);
			dbProgress.setCurrentGroupIndex(progress.getCurrentGroupIndex());

			gameRepo.createGameProgress(dbProgress);
		}else {
			dbProgress.setGameStatus(progress.getGameStatus());
			dbProgress.setNumOfBoxes(progress.getNumOfBoxes());
			dbProgress.setNextListIndex(progress.getCurrentListIndex() + 1);
			dbProgress.setCurrentGroupIndex(progress.getCurrentGroupIndex());
			gameRepo.updateGameProgress(dbProgress);
		}
	}
	
	public GameProgressDataView updateGameProgressWhenGameOver(String childId, String grade, String gameId, Integer rocks) {
		GameProgress dbProgress = gameRepo.fetchGameProgress(childId, grade, gameId);
		if(dbProgress != null) {
			dbProgress.setGameStatus(GameStatusType.Complete.toString());
			if(dbProgress.getRocks() != null && rocks != null) {
				dbProgress.setRocks(dbProgress.getRocks() + rocks);
			}else if(rocks != null) {
				dbProgress.setRocks(rocks);
			}
			//add constant coins when game over no matter how many coins user earned.
			if(dbProgress.getCoins() == null) {
				dbProgress.setCoins(FinalReward);
			}else {
				dbProgress.setCoins(dbProgress.getCoins() + FinalReward);
			}
			dbProgress = gameRepo.updateGameProgress(dbProgress);
			GameProgressDataView result = new GameProgressDataView();
			result.setCoins(dbProgress.getCoins());
			result.setRocks(dbProgress.getRocks());
			
			StudentProfile profile = profileRepo.fetchStudentProfile(childId, grade);
			//update student profile total coins and rocks.
			if(dbProgress.getCoins() != null || dbProgress.getRocks() != null) {
				
				if(dbProgress.getCoins() != null) {
					profile.setTotalCoins(profile.getTotalCoins() == null ? dbProgress.getCoins() : profile.getTotalCoins() + dbProgress.getCoins());
				}
				if(dbProgress.getRocks() != null) {
					profile.setTotalRocks(profile.getTotalRocks() == null ? dbProgress.getRocks() : profile.getTotalRocks() + dbProgress.getRocks());
				}
				profileRepo.updateTotalCoins(profile);
			}
			
			//check day 1 or day2 game is completed
			List<String> day1Games = new ArrayList<String>();
			day1Games.add("DS"); day1Games.add("VSR"); day1Games.add("LS"); day1Games.add("NUA"); day1Games.add("RDV");
			day1Games.add("PBS"); day1Games.add("VBS");
			
			List<String> day2Games = new ArrayList<String>();
			day2Games.add("DSR"); day2Games.add("VS"); day2Games.add("LSR"); day2Games.add("NUV"); day2Games.add("RDA");
			day2Games.add("NR"); day2Games.add("CMB");
			boolean allGamesComplete = true;
			boolean belongToDay1 = false;
			if(day1Games.contains(gameId)) {
				belongToDay1 = true;
			}
			//check whether all day1 games completed
			List<String> gameIds = belongToDay1 ? day1Games : day2Games;
			List<GameProgressDataView> gameProgressList = fetchListGamesProgress(childId, grade, gameIds);
			for(GameProgressDataView dv: gameProgressList) {
				if(dv.getGameStatus() == null || !dv.getGameStatus().equals(GameStatusType.Complete.toString())) {
					allGamesComplete = false;
					break;
				}
			}
			if(allGamesComplete) {
				if(belongToDay1) {
					profile.setDay1Completed(true);
				}else {
					profile.setDay2Completed(true);
				}
				
				profileRepo.updateProfileDayComplete(profile);
			}
			return result;
		}
		
		return null;
	}
	
	public List<GameProgressDataView> fetchListGamesProgress(String childId, String grade, List<String> gameIds) {
		List<GameProgressDataView> result = new ArrayList<GameProgressDataView>();
		List<GameProgress> progress = gameRepo.fetchListGameProgress(childId, grade, gameIds);
		if(progress != null && progress.size() > 0) {
			for(GameProgress p: progress) {
				GameProgressDataView dv = new GameProgressDataView();
				dv.setChildId(p.getChildId());
				dv.setGameId(p.getGameId());
				dv.setGameStatus(p.getGameStatus());
				result.add(dv);
			}
		}
		return result;
	}
}