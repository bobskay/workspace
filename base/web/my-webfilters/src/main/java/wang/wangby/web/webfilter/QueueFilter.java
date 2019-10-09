package wang.wangby.web.webfilter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import wang.wangby.utils.monitor.MonitorFacade;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class QueueFilter implements WebFilter<String> {
	public static final String TOKE_NNAME="queueToken";
	private Map<String, ArrayBlockingQueue> queueMap;
	

	public QueueFilter(Map<String, Integer> countMap) {
		log.info("开启并发控制");
		this.queueMap=new ConcurrentHashMap<String, ArrayBlockingQueue>();
		countMap.forEach((url,count)->{
			if(count==null||count<=0) {
				log.info("允许最大并发数配置错误:{}-{}",url,count);
				return;
			}
			log.info("允许最大并发数:{} = {}",url,count);
			queueMap.put(url, new ArrayBlockingQueue(count));

			MonitorFacade.addMonior(MonitorFacade.QUEUE_MONITOR, this::getQueueInfo);
		});
		
	}
	
	
	public Map<String,Object> getQueueInfo(){
		Map map=Collections.synchronizedMap(new LinkedHashMap());
		queueMap.forEach((key,value)->{
			QueueInfo info=new QueueInfo();
			info.size=value.size();
			info.remain=value.remainingCapacity();
			map.put(key,info);
		});
		return map;
	}

	@Override
	public String begin(HttpServletRequest request, HttpServletResponse httpResponse) throws Exception {
		String uri = request.getRequestURI();
		BlockingQueue queue = queueMap.get(uri);
		if(queue==null||queue.offer(uri)) {
			return "";
		}
		
		httpResponse.setContentType("text/html;charset=UTF-8");
		httpResponse.getWriter().write("当前队列人数:"+queue.size());
		return null;
	}

	@Override
	public void end(String t, HttpServletRequest request) throws InterruptedException {
		String uri = request.getRequestURI();
		BlockingQueue queue = queueMap.get(uri);
		if(queue!=null) {
			queue.poll();
		}
	}
	
	@Data
	public class QueueInfo{
		private int remain;
		private int size;
		public String toString() {
			return size+"/"+(size+remain);
		}
	}
	

}
