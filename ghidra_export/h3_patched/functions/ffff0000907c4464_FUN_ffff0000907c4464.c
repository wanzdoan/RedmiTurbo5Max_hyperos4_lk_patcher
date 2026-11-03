// ffff0000907c4464  size=524  FUN_ffff0000907c4464

uint FUN_ffff0000907c4464(int param_1,byte *param_2,uint param_3)

{
  byte bVar1;
  uint uVar2;
  ulong uVar3;
  ulong uVar4;
  uint local_10 [4];
  
  local_10[2] = 0;
  local_10[0] = 0;
  local_10[1] = 0;
  if (param_3 == 0) {
LAB_ffff0000907c4600:
    uVar2 = 0;
  }
  else {
    if (param_1 - 1U < 3) {
      if ((param_3 == 10) || (param_3 == 7)) {
        if (((*param_2 - 0x3a < 0xfffffff6) ||
            ((((param_2[1] - 0x3a < 0xfffffff6 || (param_2[2] - 0x3a < 0xfffffff6)) ||
              (param_2[3] - 0x3a < 0xfffffff6)) ||
             ((param_2[4] != 0x2d || (param_2[5] - 0x3a < 0xfffffff6)))))) ||
           (param_2[6] - 0x3a < 0xfffffff6)) goto LAB_ffff0000907c4600;
        local_10[2] = ((uint)param_2[6] + (uint)param_2[5] * 10 +
                      ((uint)param_2[3] +
                       ((uint)param_2[2] + ((uint)param_2[1] + (uint)*param_2 * 10) * 10) * 10 +
                      -0xd050) * 100) - 0x210;
        if (param_3 == 10) {
          if (((param_2[7] != 0x2d) || (param_2[8] - 0x3a < 0xfffffff6)) ||
             (param_2[9] - 0x3a < 0xfffffff6)) goto LAB_ffff0000907c4600;
          local_10[2] = ((uint)param_2[9] + (uint)param_2[8] * 10 + local_10[2] * 100) - 0x210;
        }
      }
      else {
        local_10[2] = 0;
      }
    }
    else {
      if (param_1 != 0) goto LAB_ffff0000907c4600;
      if ((param_3 == 1) && (*param_2 - 0x4d < 0xe)) {
        local_10[2] = 0;
        local_10[1] = 0;
        local_10[0] = *param_2 - 0x47;
      }
      else {
        uVar3 = 0;
        uVar4 = (ulong)param_3;
        do {
          bVar1 = *param_2;
          if (bVar1 == 0x2e) {
            uVar2 = (int)uVar3 + 1;
            uVar3 = (ulong)uVar2;
            if (uVar2 == 3) break;
          }
          else if (bVar1 - 0x3a < 0xfffffff6) goto LAB_ffff0000907c4600;
          uVar4 = uVar4 - 1;
          local_10[uVar3] = ((uint)bVar1 + local_10[uVar3] * 10) - 0x30;
          param_2 = param_2 + 1;
        } while (uVar4 != 0);
      }
      if (0x62 < local_10[0]) {
        local_10[0] = 99;
      }
      if (0x62 < local_10[1]) {
        local_10[1] = 99;
      }
      if (0x62 < local_10[2]) {
        local_10[2] = 99;
      }
      local_10[2] = local_10[0] * 10000 + local_10[1] * 100 + local_10[2];
    }
    uVar2 = local_10[2] / 100;
    if (local_10[2] < 1000000 || param_1 != 1) {
      uVar2 = local_10[2];
    }
  }
  return uVar2;
}


