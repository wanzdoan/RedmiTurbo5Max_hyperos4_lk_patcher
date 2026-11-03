
uint FUN_ffff0000be086c68(uint param_1,int *param_2)

{
  uint uVar1;
  int iVar2;
  uint *puVar3;
  ulong uVar4;
  uint uVar5;
  uint uVar6;
  uint uVar7;
  uint local_34;
  
  local_34 = 0;
  if (param_2 == (int *)0x0) {
    uVar7 = 6;
  }
  else {
    if (DAT_ffff0000be1b54ec != '\x01') {
      uVar5 = 0;
      uVar6 = 0;
      uVar7 = 0x10400c;
      goto LAB_ffff0000be086d00;
    }
    uVar4 = (ulong)DAT_ffff0000be1b54f0;
    if (DAT_ffff0000be1b54f0 != 0) {
      puVar3 = &DAT_ffff0000be0f3740;
      do {
        if (puVar3[-2] == param_1) {
          if (puVar3[-1] == 0) {
            uVar7 = 0;
            goto LAB_ffff0000be086d2c;
          }
          uVar5 = *puVar3;
          uVar6 = puVar3[1];
          if (uVar6 < uVar5) {
            uVar7 = 0x104008;
          }
          else {
            *param_2 = 0;
            uVar7 = uVar5;
            while (iVar2 = FUN_ffff0000be087408(uVar7,&local_34), iVar2 == 0) {
              uVar7 = uVar7 + 1;
              uVar1 = local_34 - (local_34 >> 1 & 0x55555555);
              uVar1 = (uVar1 & 0x33333333) + (uVar1 >> 2 & 0x33333333);
              *param_2 = *param_2 + ((uVar1 + (uVar1 >> 4) & 0xf0f0f0f) * 0x1010101 >> 0x18);
              if (uVar6 < uVar7) {
                return 0;
              }
            }
            uVar7 = 0x104004;
          }
          goto LAB_ffff0000be086d00;
        }
        puVar3 = puVar3 + 7;
        uVar4 = uVar4 - 1;
      } while (uVar4 != 0);
    }
    uVar7 = 0x4009;
  }
  uVar5 = 0;
  uVar6 = 0;
  uVar7 = uVar7 | 0x100000;
LAB_ffff0000be086d00:
  iVar2 = FUN_ffff0000be0879f0();
  if (iVar2 != 0) {
    FUN_ffff0000be05fd98
              (s___s__fail_to_get_otp_ver__start__ffff0000be0ba857,&DAT_ffff0000be0bb811,uVar5,uVar6
               ,uVar7);
  }
  if (param_2 != (int *)0x0) {
LAB_ffff0000be086d2c:
    *param_2 = 0;
  }
  return uVar7;
}


