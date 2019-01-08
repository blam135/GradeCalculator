public class AssignmentData {
    private String assessmentTitle;
    private double markReceived;
    private double maxMark;
    private double percentReceived;
    private double weighting;

    public AssignmentData(String assessmentTitle, double markReceived, double maxMark, double weighting) {
        // Will assume if the max Mark received is 0, then the weighting is 0
        this.assessmentTitle = assessmentTitle;
        if (maxMark == 0) {
            this.markReceived = 0;
            this.maxMark = 0;
            this.weighting = 0;
            percentReceived = 0;
        } else {
            this.markReceived = markReceived;
            this.maxMark = maxMark;
            this.weighting = weighting;
            percentReceived = (markReceived / maxMark) * weighting;
        }
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