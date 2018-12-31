public class AssignmentData {
    private String assessmentTitle;
    private double markReceived;
    private double maxMark;
    private double percentReceived;
    private double weighting;

    public AssignmentData(String assessmentTitle, double markReceived, double maxMark, double weighting) {
        this.assessmentTitle = assessmentTitle;
        this.markReceived = markReceived;
        this.maxMark = maxMark;
        this.weighting = weighting;
        percentReceived = ((double) markReceived / (double) maxMark) * (double) weighting; // Fix this later
    }

    public String getAssessmentTitle() {
        return assessmentTitle;
    }

    public double getMarkReceived() {
        return markReceived;
    }

    public double getMaxMark() {
        return maxMark;
    }

    public double getPercentReceived() {
        return percentReceived;
    }

    public double getWeighting() {
        return weighting;
    }
}