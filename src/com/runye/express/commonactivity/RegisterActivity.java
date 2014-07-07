package com.runye.express.commonactivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.runye.express.android.R;
import com.runye.express.utils.ToastUtil;

/**
 * 
 * @ClassName: Register
 * @Description: 注册
 * @author LanJie.Chen
 * @date 2014-7-1 下午3:05:11
 * @version V1.0
 * @Company:山西润叶网络科技有限公司
 */
public class RegisterActivity extends Activity {
	/**
	 * 0 昵称 1 手机 2 邮箱 3 密码 4 确认密码 5 推广号
	 */
	private EditText[] et_registers;
	private String[] et_values;
	/** 单选控件 */
	private RadioGroup rg_sex;
	/** 男 */
	private RadioButton rb_man;
	/** 女 */
	private RadioButton rb_woman;
	/** 保密 */
	private RadioButton rb_secrecy;

	/** 注册按钮 */

	private Button bt_commit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		initUI();
	}

	private void initUI() {
		// 6个et的id
		int[] etID = new int[] { R.id.activity_register_nickname,
				R.id.activity_register_phone, R.id.activity_register_email,
				R.id.activity_register_password,
				R.id.activity_register_password2,
				R.id.activity_register_promotionCode };
		et_registers = new EditText[etID.length];
		et_values = new String[etID.length];
		for (int i = 0; i < etID.length; i++) {
			et_registers[i] = (EditText) findViewById(etID[i]);
			et_values[i] = et_registers[i].getText().toString().trim();
		}

		// RadioGroup
		rg_sex = (RadioGroup) findViewById(R.id.activity_register_radioGroup);
		rb_man = (RadioButton) findViewById(R.id.activity_register_man);
		rb_woman = (RadioButton) findViewById(R.id.activity_register_woman);
		rb_secrecy = (RadioButton) findViewById(R.id.activity_register_secrecy);
		rg_sex.setOnCheckedChangeListener(new MyRadioListener());

		// button
		bt_commit = (Button) findViewById(R.id.activity_register_confirm);
		bt_commit.setOnClickListener(new MyButtonListener());

	}

	/**
	 * 
	 * @Description: 检查输入完整性
	 * @return boolean
	 */
	private boolean checkInput() {
		for (int i = 0; i < et_values.length; i++) {
			if (et_values[i].equals("")) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @Description: 注册方法，联网提交参数
	 * @return void
	 */
	private void doRegister() {
		if (checkInput()) {
			// 联网代码，提交参数
			
			//
		} else {
			ToastUtil.showLongToast(RegisterActivity.this, "还有信息未填写哦！");
		}
	}

	class MyButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.activity_register_confirm:
				// 注册
				doRegister();
				ToastUtil.showLongToast(RegisterActivity.this, check);
				break;

			default:
				break;
			}
		}
	}

	String check;

	class MyRadioListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.activity_register_man:
				check = rb_man.getText().toString();
				break;
			case R.id.activity_register_woman:
				check = rb_woman.getText().toString();
				break;
			case R.id.activity_register_secrecy:
				check = rb_secrecy.getText().toString();
				break;
			default:
				break;
			}
		}
	}
}
