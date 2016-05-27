package com.taihuoniao.fineix.utils;

//public class DialogUtil {
//	public interface DialogCallBack{
//		void excute();
//	}
//	public static void showConfirmDialog(final Activity activity,int titleId,String tips,String rightStr,String leftStr,final DialogCallBack callBack){
//		final Dialog dialog = new Dialog(activity, R.style.dialog);
//		LayoutInflater inflater = LayoutInflater.from(activity);
//		View view = inflater.inflate(R.layout.confirm_dialog_layout,null);
//		TextView leftBtn = (TextView) view.findViewById(R.id.tv_left_btn);
//		TextView rightBtn= (TextView) view.findViewById(R.id.tv_right_btn);
//		TextView content= (TextView) view.findViewById(R.id.tv_content);
//		leftBtn.setText(leftStr);
//		rightBtn.setText(rightStr);
//		content.setText(tips);
//		OnClickListener onClickListener =new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				switch (v.getId()) {
//					case R.id.tv_left_btn:
//						dialog.dismiss();
//						callBack.excute();
//						break;
//					case R.id.tv_right_btn:
//						dialog.dismiss();
//						break;
//					default:
//						break;
//				}
//
//			}
//		};
//		leftBtn.setOnClickListener(onClickListener);
//		rightBtn.setOnClickListener(onClickListener);
//		dialog.setTitle(titleId);
//		LayoutParams lp= new LayoutParams();
//		lp.width=Util.getScreenWidth()-100;
//		dialog.setContentView(view,lp);
//		dialog.show();
//	}
//}
