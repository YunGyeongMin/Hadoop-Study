package kr.gudi.hadoop;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/** 설명:
 * https://hadoop.apache.org/docs/r2.9.2/hadoop-mapreduce-client/hadoop-mapreduce-client-core/MapReduceTutorial.html#Example:_WordCount_v1.0
 *
 */
public class App {
    public static void main( String[] args ){
        System.out.println( "Hello World!" );
        
        if(args.length != 2) { //input , output 2개가 아닐경우
        	System.out.println("파라메터 2개 있어야합니다.");
        	System.exit(0); //바로끄기
        }
        
        System.out.println("Hadoop 시스템 동작시작");
        // 하둡 프로그램 정의
        try {
        	Configuration con = new Configuration(); //설정정보 객체 생성
        	FileSystem hdfs = FileSystem.get(con); //파일시스템 객체 가져오기
        	if(hdfs.exists(new Path(args[1]))) { //경로 존재 여부 확인
        		hdfs.delete(new Path(args[1]), true); //경로 삭제 
        	}
			Job job = Job.getInstance(con); //실행 객체 생성
			job.setJarByClass(App.class);//실행 대상 클래스 설정
			job.setMapperClass(JobMap.class); // 맵클래스 설정
			job.setReducerClass(JobReduce.class);//리듀서 클래스 설정
			job.setMapOutputKeyClass(Text.class); //맵 클래스 결과 자료형 설정
			job.setMapOutputValueClass(IntWritable.class);
			job.setOutputKeyClass(Text.class); //리듀서 클래스 결과 자료형 설정
			job.setOutputValueClass(IntWritable.class);
			job.setNumReduceTasks(1); // 리듀서 실행 명령 번호 설정
			FileInputFormat.addInputPath(job, new Path(args[0])); // 입력파일과 출력 파일경로 설정
			FileOutputFormat.setOutputPath(job, new Path(args[1])); 
			System.exit(job.waitForCompletion(true) ? 0 : 1); //하둡실행후 정상종료 :0 실패:1
		} catch (Exception e) {
			e.printStackTrace();
		}
        /*Client가 맵리듀스를 수행하면 Job이 생성되고 잡트래커와 태스크트래커에 잡을 할당합니다.
		할당된 잡은 각각의 데이터노드에서 수행되어 결과를 반환하며, 최종적으로 수행이 완료 됩니다*/

        /*MapReduce 작업을 제출하는 동안 MapReduce 작업이 출력에 쓸 새 출력 디렉토리를 제공해야합니다. 
         그러나 출력 디렉토리가 이미 존재하면 OutputDirectoryAlreadyExist라는 예외가 발생합니다. 따라서 경로 삭제 예외처리 추가 */


    }
}
