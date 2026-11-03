
void FUN_ffff0000be035310(uint param_1,uint param_2)

{
  DAT_ffff0000be121848 = param_1 & 0xff000000;
  FUN_ffff0000be035188(param_1 >> 0x10,param_1 >> 8);
  FUN_ffff0000be035094(param_2 >> 0x10,param_2 >> 8,param_2);
  return;
}


