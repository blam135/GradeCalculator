public class AssignmentData {
    private String assessmentTitle;
    private int markReceived;
    private int maxMark;
    private double percentReceived;
    private int weighting;

    public AssignmentData(String assessmentTitle, int markReceived, int maxMark, int weighting) {
        this.assessmentTitle = assessmentTitle;
        this.markReceived = markReceived;
        this.maxMark = maxMark;
        this.weighting = weighting;
        percentReceived = ((double) markReceived / (double) maxMark) * (double) weighting;
    }

    public String getAssessmentTitle() {
        return assessmentTitle;
    }

    public int getMarkReceived() {
        return markReceived;
    }

    public int getMaxMark() {
        return maxMark;
    }

    public double getPercentReceived() {
        return percentReceived;
    }

    public int getWeighting() {
        return weighting;
    }
}