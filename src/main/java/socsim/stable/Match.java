package socsim.stable;

import java.time.Instant;
import java.util.Comparator;
import java.util.Random;

import org.apache.commons.math3.distribution.EnumeratedIntegerDistribution;
import org.apache.commons.math3.distribution.IntegerDistribution;
import org.apache.commons.math3.distribution.PoissonDistribution;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Match implements Comparable<Match> {
	@Getter @NonNull private Instant date;
	@Getter @NonNull private Team homeTeam;
	@Getter @NonNull private Team guestTeam;
	@Getter private int homeScore = 0;
	@Getter private int guestScore = 0;
	@Getter private boolean ko = false;
	@Getter private boolean finished = false;

	// Base goal scoring probabilities
	public static final double[] base_probabilities = new double[] { 0.21, 0.3, 0.27, 0.15, 0.05, 0.01, 0.01 };;
	
	public Match(Instant date, Team homeTeam, Team guestTeam, boolean ko) {
		this(date, homeTeam, guestTeam);
		this.ko = ko;
	}
	
	public Match(Instant date, Team homeTeam, Team guestTeam, int homeScore, int guestScore) {
		this(date, homeTeam, guestTeam);
		this.homeScore = homeScore;
		this.guestScore = guestScore;
	}
	
	public void play() {
		String nV = "";

		// TODO Adjust for team elos
		int eloDiff = homeTeam.getElo() - guestTeam.getElo();
		double eloScaleFactor = 0.001 *eloDiff;
		
		IntegerDistribution dist = new PoissonDistribution(2.5 + eloScaleFactor);
		int totalGoals = dist.sample();
		// Original formula - produces high values, because it is applied to each goal.
		// Playing around below, fix eventually ...
//		double expectedScoreElo = 1 / (1 + (Math.pow(10, ((double) homeTeam.getElo() - (double) guestTeam.getElo()) / 400)));

		double expectedScoreElo = 1 / (1 + (Math.pow(10, ((double) homeTeam.getElo() - (double) guestTeam.getElo()) / 1500)));

		while (totalGoals > 0) {
			double rd = new Random().nextDouble();
			if (rd > expectedScoreElo) {
				homeScore++;
			} else {
				guestScore++;
			}
			totalGoals--;
		}
		// TODO Remove hack
		if (ko && homeScore == guestScore) {
			nV = " (n.V.)";
			double rd = new Random().nextDouble();
			if (rd < 0.1) {
				homeScore += 2;
			} else if (rd < 0.5) {
				homeScore += 1;
			} else if (rd < 0.9) {
				guestScore += 1;
			} else {
				guestScore += 2;
			}
			
		}
		System.out.println(toString() + nV);
		finished = true;
	}
	
	@Override
	public String toString() {
		return String.format("  %s    %-20s  -  %-20s    %2d : %2d", date.toString(), homeTeam, guestTeam, homeScore,
				guestScore);
	}
	
	public Team getWinner() {
		
		if (homeScore > guestScore)
			return homeTeam;
		if (guestScore > homeScore)
			return guestTeam;
		return null;
	}
	
	@Override
	public int compareTo(Match o) {
		return Comparator.comparing(Match::getDate).thenComparing(Match::getHomeTeam).thenComparing(Match::getGuestTeam)
				.compare(this, o);
	}

	/**
	 * @param eloFactor Positive numbers increase towards more goals scored,
	 *                  negative values vice versa
	 */
	public IntegerDistribution goalDistribution(int eloFactor) {
		int[] goals = new int[] { 0, 1, 2, 3, 4, 5, 6 };
		double eloScaleFactor = 0.001 * eloFactor;
		double[] probabilities = base_probabilities;
		probabilities[0] *= (1 - eloScaleFactor);
		probabilities[1] *= (1 - eloScaleFactor);
		// 2 constant
		probabilities[3] *= (1 + eloScaleFactor);
		probabilities[4] *= (1 + eloScaleFactor);
		probabilities[5] *= (1 + eloScaleFactor);
		return new EnumeratedIntegerDistribution(goals, probabilities);
	}
}
