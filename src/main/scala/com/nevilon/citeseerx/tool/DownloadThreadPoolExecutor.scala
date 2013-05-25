package com.nevilon.citeseerx.tool

import java.util.concurrent.{ThreadPoolExecutor, BlockingQueue, TimeUnit}


class DownloadThreadPoolExecutor(corePoolSize: Int, maximumPoolSize: Int, keepAliveTime: Long,
                                 unit: TimeUnit, workQueue: BlockingQueue[Runnable])
  extends ThreadPoolExecutor(corePoolSize: Int, maximumPoolSize: Int, keepAliveTime: Long,
    unit: TimeUnit, workQueue: BlockingQueue[Runnable]) {


  protected override def beforeExecute(t: Thread, r: Runnable) {
    super.beforeExecute(t, r)
  }

  protected override def afterExecute(r: Runnable, t: Throwable) {
    super.afterExecute(r, t)
  }
}
