package com.xy.hashmaker.algorithm;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import com.xy.hashmaker.utils.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.xy.hashmaker.constants.FilterPatterns.ADJECTIVE_NOTES;
import static com.xy.hashmaker.constants.FilterPatterns.NOUNCE_NOTES;
import static com.xy.hashmaker.constants.FilterPatterns.POSITION_NOTES;
import static com.xy.hashmaker.constants.FilterPatterns.PROPER_NOUNCE_NOTES;
import static com.xy.hashmaker.constants.FilterPatterns.TIME_NOTES;
import static com.xy.hashmaker.constants.FilterPatterns.VERB_NOTES;

/**
 * Created by Xavier Yin on 10/18/16.
 */

public class HashMaker {
    private Logger logger = Logger.getInstance(this.getClass());

    private StopWordsReader stopWordsReader = new StopWordsReader();
    private Set<String> stopWordSet;

    private static final float d = 0.85f;
    private static final int MAX_LOOP = 500;
    private static final float END_CONDITION = 0.0001f;

    public HashMaker() {
        this.stopWordSet = this.stopWordsReader.getStopWords();
    }

    public List<String> makeHashTag(String content) {
        StringBuilder hashTagResults = new StringBuilder();
        List<String> hashTagList = new ArrayList<>();

        Map<String, Float> scoreSet = this.calculateTextRank(this.getWordSet(content));
        List<Map.Entry<String, Float>> sortedScoreList = this.sortHashScore(scoreSet);

        for (Map.Entry<String, Float> entry : sortedScoreList) {
            logger.d("key: " + entry.getKey() + ", value: " + scoreSet.get(entry.getKey()));
            hashTagResults.append("#").append(entry.getKey()).append(": ").append(scoreSet.get(entry.getKey())).append("\n");

            hashTagList.add(entry.getKey());
        }

        return hashTagList;
    }

    private List<Map.Entry<String, Float>> sortHashScore(Map<String, Float> scoreSet) {
        List<Map.Entry<String, Float>> list = new ArrayList<>(scoreSet.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Float>>() {
            @Override
            public int compare(Map.Entry<String, Float> entry1, Map.Entry<String, Float> entry2) {
                return (entry2.getValue()).compareTo((entry1.getValue()));
            }
        });

        return list;
    }

    private Map<String, Float> calculateTextRank(Map<String, Set<String>> wordSet) {
        Map<String, Float> score = new HashMap<>();

        for (int i = 0; i < MAX_LOOP; i++) {
            logger.d("count: " + i);

            Map<String, Float> m = new HashMap<>();
            Map<String, Set<String>> words = wordSet;
            float max_diff = 0;

            for (Map.Entry<String, Set<String>> entry : words.entrySet()) {
                String key = entry.getKey();
                Set<String> value = entry.getValue();
                m.put(key, 1 - d);

                for (String other : value) {
                    int size = words.get(other).size();

                    if (key.equals(other) || size == 0) {
                        continue;
                    }

                    m.put(key, m.get(key) + d / size * (score.get(other) == null ? 0 : score.get(other)));
                }

                max_diff = Math.max(max_diff, Math.abs(m.get(key) - (score.get(key) == null ? 0 : score.get(key))));
            }

            score = m;

            logger.d("max_diff: " + max_diff);

            if (max_diff <= END_CONDITION) {
                break;
            }
        }

        return score;
    }

    private Map<String, Set<String>> getWordSet(String content) {
        final int frame = 5;
        Map<String, Set<String>> words = new HashMap<>();
        List<String> sequences = this.generateSequence(content);

        for (int i = 0; i < sequences.size(); i++) {
            Set<String> wordSeqs;
            String key = sequences.get(i);

            if (words.get(key) == null) {
                wordSeqs = new HashSet<>();
            } else {
                wordSeqs = words.get(key);
            }

            for (int backward = i + 1; backward < i + frame && backward < sequences.size(); backward++) {
                wordSeqs.add(sequences.get(backward));
            }

            for (int frontward = i - 1; frontward > -1 && frontward > i - frame; frontward--) {
                wordSeqs.add(sequences.get(frontward));
            }

            wordSeqs.remove(key);
            words.put(key, wordSeqs);
        }

        return words;
    }

    private List<String> generateSequence(String content) {
        List<String> sequence = new ArrayList<>();

        for (Term term : HanLP.segment(content)) {
            String currentWord = term.word;
            String currentWordPattern = "-" + term.nature.firstChar() + "-";

            logger.d("term: " + currentWord + ", np: " + currentWordPattern);

            if (!this.stopWordSet.contains(currentWord)) {
                if (currentWord.length() >= 2) {
                    if ((NOUNCE_NOTES.indexOf(currentWordPattern) != -1)
                            || (PROPER_NOUNCE_NOTES.indexOf(currentWordPattern) != -1)
                            || (TIME_NOTES.indexOf(currentWordPattern) != -1 && currentWord.length() >= 3)
                            || (POSITION_NOTES.indexOf(currentWordPattern) != -1)
                            || (ADJECTIVE_NOTES.indexOf(currentWordPattern) != -1)
                            || (VERB_NOTES.indexOf(currentWordPattern) != -1)) {
                        sequence.add(currentWord);
                    }
                }
            }
        }

        logger.d("sequence length: " + sequence.size());

        return sequence;
    }
}
