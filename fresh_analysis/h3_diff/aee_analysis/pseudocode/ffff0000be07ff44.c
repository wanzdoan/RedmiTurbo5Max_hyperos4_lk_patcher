
undefined8 FUN_ffff0000be07ff44(long *param_1,long param_2,long param_3)

{
  undefined8 uVar1;
  
  if (param_1 == (long *)0x0) {
    uVar1 = 0xffffffff;
  }
  else if (param_2 == 0) {
    uVar1 = 0xfffffffe;
  }
  else if ((ulong)param_1[9] < 0x41) {
    uVar1 = FUN_ffff0000be085ac4(&DAT_ffff0000be1b5340,param_2,(long)(int)param_3);
    if ((int)uVar1 == 0) {
      param_1[9] = 0;
      param_1[10] = param_3;
      *param_1 = *param_1 + (long)((int)param_3 << 3);
    }
  }
  else {
    uVar1 = 0xfffffffd;
  }
  return uVar1;
}


