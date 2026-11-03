
/* WARNING: This function may have set the stack pointer */

undefined1  [16]
FUN_ffff00009071e970(undefined8 param_1,undefined8 param_2,undefined8 param_3,undefined8 param_4)

{
  undefined8 unaff_x29;
  undefined8 unaff_x30;
  undefined1 auVar1 [16];
  
  DAT_ffff00009083cfd0 = unaff_x29;
  DAT_ffff00009083cfd8 = unaff_x30;
  DAT_ffff00009083cfe0 = param_3;
  DAT_ffff00009083cfe8 = param_4;
  DAT_ffff00009083cff0 = param_1;
  DAT_ffff00009083cff8 = param_2;
  FUN_ffff00009071e968();
  auVar1._8_8_ = DAT_ffff00009083cff8;
  auVar1._0_8_ = DAT_ffff00009083cff0;
  return auVar1;
}


