
/* WARNING: This function may have set the stack pointer */

undefined1  [16]
FUN_ffff0000be01d3c0(undefined8 param_1,undefined8 param_2,undefined8 param_3,undefined8 param_4)

{
  undefined8 unaff_x29;
  undefined8 unaff_x30;
  undefined1 auVar1 [16];
  
  DAT_ffff0000be0f8fd0 = unaff_x29;
  DAT_ffff0000be0f8fd8 = unaff_x30;
  DAT_ffff0000be0f8fe0 = param_3;
  DAT_ffff0000be0f8fe8 = param_4;
  DAT_ffff0000be0f8ff0 = param_1;
  DAT_ffff0000be0f8ff8 = param_2;
  FUN_ffff0000be0034f8();
  auVar1._8_8_ = DAT_ffff0000be0f8ff8;
  auVar1._0_8_ = DAT_ffff0000be0f8ff0;
  return auVar1;
}


