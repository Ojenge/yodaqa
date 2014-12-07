package cz.brmlab.yodaqa.analysis.answer;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.cas.CAS;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.resource.ResourceInitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Score the Answer featuresets within the AnswerHitlistCAS based on
 * their features.
 *
 * This is an aggregate AE that will mainly run an answer scorer; there
 * might be multiple of these using various machine learning models.
 * We also run the AnswerGSHook which dumps the answer feature vectors
 * to be used for model training. */

public class AnswerScoringAE /* XXX: extends AggregateBuilder ? */ {
	final static Logger logger = LoggerFactory.getLogger(AnswerScoringAE.class);

	public static AnalysisEngineDescription createEngineDescription() throws ResourceInitializationException {
		AggregateBuilder builder = new AggregateBuilder();

		builder.add(AnalysisEngineFactory.createEngineDescription(AnswerScoreSimple.class),
				CAS.NAME_DEFAULT_SOFA, "AnswerHitlist");

		/* Compute answer score (estimated probability of being right)
		 * from the various features amassed so far. */
		builder.add(AnalysisEngineFactory.createEngineDescription(AnswerScoreLogistic.class),
				CAS.NAME_DEFAULT_SOFA, "AnswerHitlist");

		builder.add(AnalysisEngineFactory.createEngineDescription(AnswerGSHook.class));

		return builder.createAggregateDescription();
	}
}
