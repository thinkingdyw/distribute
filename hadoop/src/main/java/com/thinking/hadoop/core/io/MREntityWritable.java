package com.thinking.hadoop.core.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import com.thinking.hadoop.core.factory.AppConfig;

public class MREntityWritable implements WritableComparable<MREntity> {
	/**
	 * 标注是哪一个实体类
	 */
	private String alias;
	private MREntity entity;

	public String getAlias() {
		return alias;
	}

	public MREntityWritable() {}

	public MREntityWritable(MREntity entity) {
		this.entity = entity;
		this.alias = entity.getAlias();
	}

	public void write(DataOutput out) throws IOException {
		out.writeUTF(this.entity.serialize());
	}

	public void readFields(DataInput in) throws IOException {
		final Text record = new Text(in.readUTF());
		try {
			this.entity = AppConfig.get(record);
			this.entity.deSerialize(record.toString());
			this.alias = entity.getAlias();
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException("反序列化实体类失败!");
		}
	}

	public int compareTo(MREntity entity) {
		if (null != entity && null != entity) {
			return entity.toString().compareTo(this.entity.toString());
		}else {
			return 0;
		}
	}

	@Override
	public String toString() {
		return this.entity.serialize();
	}
}
