import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.fs.Path;


public class HotCold {
    
    public static class Map extends Mapper<LongWritable,Text,Text,Text>{

        public void map(LongWritable key, Text value, Context context)
        throws IOException,InterruptedException 
        {
            String line = value.toString();
            String[] parts = line.split(",");
            String date = parts[1];
            if (Float.parseFloat(parts[5]) > 40){
            	context.write(new Text(date), new Text("High"));
            }
            else if (Float.parseFloat(parts[6]) < 10){
            	context.write(new Text(date), new Text("Low"));
            }
            else{
            	context.write(new Text(date), new Text("Medium"));
            }
            }
        }
        
    
//    public static class Combine extends Reducer<Text,Text,Text,Text>{
//        public void reduce(Text key, Iterable<Text> values, Context context)
//        throws IOException,InterruptedException 
//        {
//            String temperature = values.
//            }
//            
//            
//        }
//        
//    
//    public static class Reduce extends Reducer<Text,Text,Text,Text>{
//
//        public void reduce(Text key, Text values, Context context)
//        throws IOException,InterruptedException 
//        {
//        	context.write(key, values);
//        }
//        
//        
//    }
//    

    
    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws Exception {
        
        Configuration conf= new Configuration();
        
        Job job = new Job(conf,"HotCold");
        
        job.setJarByClass(HotCold.class);
        job.setMapperClass(Map.class);
//        job.setReducerClass(Reduce.class);
//        job.setCombinerClass(Combine.class);
        
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        
        Path outputPath = new Path(args[1]);
            
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
            
        outputPath.getFileSystem(conf).delete(outputPath);
            
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}