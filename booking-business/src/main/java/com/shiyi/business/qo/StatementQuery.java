package com.shiyi.business.qo;

import com.shiyi.business.domain.BusStatement;
import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;
import java.util.Date;

@Getter
@Setter
public class StatementQuery extends BusStatement {
    private Date startTime;
    private Date endTime;
    public Date getEndTime(){
        Calendar calendar = Calendar.getInstance();
        if(endTime!=null){
            calendar.setTime(this.endTime);
            calendar.add(Calendar.DAY_OF_MONTH,1);
            return calendar.getTime();
        }
        return null;
    }
}
