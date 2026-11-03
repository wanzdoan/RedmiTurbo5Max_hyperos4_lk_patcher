
undefined8 FUN_ffff0000b80630b0(long *param_1,long param_2,long param_3)

{
  undefined8 uVar1;
  
  if (param_1 == (long *)0x0) {
    uVar1 = 0xffffffff;
  }
  else if (param_2 == 0) {
    uVar1 = 0xfffffffe;
  }
  else if ((ulong)param_1[10] < 0x81) {
    uVar1 = FUN_ffff0000b8068314(&DAT_ffff0000b81b7100);
    if ((int)uVar1 == 0) {
      param_1[10] = 0;
      param_1[0xb] = param_3;
      *param_1 = *param_1 + param_3 * 8;
    }
  }
  else {
    uVar1 = 0xfffffffd;
  }
  return uVar1;
}


