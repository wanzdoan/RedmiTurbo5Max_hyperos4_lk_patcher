
undefined8 FUN_ffff0000b8061b0c(long *param_1,long param_2,ulong param_3)

{
  undefined8 uVar1;
  long lVar2;
  ulong uVar3;
  
  if (param_1 == (long *)0x0) {
    uVar1 = 0xffffffff;
  }
  else if (param_2 == 0) {
    uVar1 = 0xfffffffe;
  }
  else if ((ulong)param_1[9] < 0x41) {
    param_1[10] = param_3;
    if (param_3 != 0) {
      do {
        lVar2 = param_1[9];
        if ((lVar2 == 0) && (0x3f < param_3)) {
          FUN_ffff0000b805e38c(param_1,param_2);
          uVar3 = param_1[10];
          param_2 = param_2 + uVar3;
          lVar2 = *param_1 + uVar3 * 8;
LAB_ffff0000b8061b8c:
          *param_1 = lVar2;
        }
        else {
          uVar3 = param_3;
          if (0x40U - lVar2 <= param_3) {
            uVar3 = 0x40U - lVar2;
          }
          FUN_ffff0000b804cd40((long)(param_1 + 0xb) + lVar2,param_2,uVar3);
          lVar2 = param_1[9];
          param_2 = param_2 + uVar3;
          param_1[9] = lVar2 + uVar3;
          if (lVar2 + uVar3 == 0x40) {
            param_1[10] = 0x40;
            FUN_ffff0000b805e38c(param_1,param_1 + 0xb);
            param_1[9] = 0;
            lVar2 = *param_1 + 0x200;
            goto LAB_ffff0000b8061b8c;
          }
        }
        param_3 = param_3 - uVar3;
      } while (param_3 != 0);
    }
    uVar1 = 0;
  }
  else {
    uVar1 = 0xfffffffd;
  }
  return uVar1;
}


