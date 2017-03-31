package com.github.wangyi.hetty.test.demo;

import java.util.Date;

public class ClassRoomSupport implements ClassService{

	@Override
	public ClassRoom addClass(ClassRoom classRoom) {

		return classRoom;
	}

	@Override
	public ClassRoom modiftyClass(ClassRoom classRoom) {
		classRoom.setScore(classRoom.getScore()+10L);
		classRoom.setCreate_date(new Date());
		return classRoom;
	}

}
