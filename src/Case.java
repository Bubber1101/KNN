
public class Case{

    private double[] attributes;
    private String decisionAttribute;

    public Case(String input ){
        String[] inputSplit = input.split(",");
        attributes = new double[inputSplit.length-1];
        for (int i = 0; i <inputSplit.length-1 ; i++) {
            attributes[i] = Double.parseDouble(inputSplit[i]);
        }
        decisionAttribute = inputSplit[inputSplit.length-1];
    }
    public Case(){
        decisionAttribute = "unknown";
    }


    public double[] getAttributes() {
        return attributes;
    }

    public String getDecisionAttribute() {
        return decisionAttribute;
    }

    public double calculateDistanceBetween(double other[]){
        double temp[] = new double[attributes.length];
        for (int i = 0; i < attributes.length; i++) {
            temp[i] = Math.pow(attributes[i] - other[i],2);
        }
        double tempx =0;
        for (double d: temp) {
            tempx += d;
        }
        return Math.sqrt(tempx);
    }

    public void setAttributes(String s) {
        String[] split = s.split(" ");
        double[] arr = new double[split.length];
        for (int i = 0; i <arr.length ; i++) {
            arr[i] = Double.parseDouble(split[i]);
        }
        this.attributes = arr;
    }
}