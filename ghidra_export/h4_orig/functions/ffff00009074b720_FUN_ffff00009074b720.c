// ffff00009074b720  size=40  FUN_ffff00009074b720

undefined8 FUN_ffff00009074b720(uint *param_1,undefined8 param_2,int param_3)

{
  *param_1 = *param_1 & 0xfffffffe | (uint)(param_3 != 0);
  return 0;
}


