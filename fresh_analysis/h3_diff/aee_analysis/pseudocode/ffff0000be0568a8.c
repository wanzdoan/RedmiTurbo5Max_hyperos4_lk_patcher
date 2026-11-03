
uint FUN_ffff0000be0568a8(ulong param_1,undefined4 *param_2,uint param_3)

{
  undefined4 *puVar1;
  uint uVar2;
  uint uVar3;
  int iVar4;
  int iVar5;
  int iVar6;
  uint uVar7;
  long lVar8;
  undefined4 *puVar9;
  uint local_54;
  
  uVar7 = *(uint *)(param_1 + 4);
  uVar3 = FUN_ffff0000be0553b4();
  if ((int)uVar3 < 0) {
    return uVar3;
  }
  iVar4 = FUN_ffff0000be055e80(param_1);
  uVar3 = (*(uint *)(param_1 + 0x14) & 0xff00ff00) >> 8 |
          (*(uint *)(param_1 + 0x14) & 0xff00ff) << 8;
  uVar3 = uVar3 >> 0x10 | uVar3 << 0x10;
  if (uVar3 < 0x11) {
    if (uVar3 != 0x10) {
      return 0xfffffff6;
    }
    local_54 = 0;
    do {
      iVar5 = FUN_ffff0000be055660(param_1,local_54,&local_54);
    } while (iVar5 != 9);
    if ((int)local_54 < 0) {
      return local_54;
    }
  }
  else {
    uVar3 = (*(uint *)(param_1 + 0x24) & 0xff00ff00) >> 8 |
            (*(uint *)(param_1 + 0x24) & 0xff00ff) << 8;
    local_54 = uVar3 >> 0x10 | uVar3 << 0x10;
  }
  uVar3 = local_54;
  iVar5 = iVar4 * 0x10 + 0x10;
  iVar6 = FUN_ffff0000be056a6c(param_1,iVar5,local_54);
  if (iVar6 == 0) {
    uVar7 = FUN_ffff0000be055af8(param_1,param_2,param_3);
    if (uVar7 == 0) {
      uVar3 = (param_3 & 0xff00ff00) >> 8 | (param_3 & 0xff00ff) << 8;
      uVar2 = (local_54 & 0xff00ff00) >> 8 | (local_54 & 0xff00ff) << 8;
      param_2[5] = 0x11000000;
      param_2[1] = uVar3 >> 0x10 | uVar3 << 0x10;
      param_2[9] = uVar2 >> 0x10 | uVar2 << 0x10;
    }
  }
  else {
    uVar2 = (*(uint *)(param_1 + 0x20) & 0xff00ff00) >> 8 |
            (*(uint *)(param_1 + 0x20) & 0xff00ff) << 8;
    iVar4 = iVar4 * 0x10 + uVar3 + (uVar2 >> 0x10 | uVar2 << 0x10) + 0x38;
    if (((int)param_3 < iVar4) ||
       (((lVar8 = (long)iVar4, puVar9 = param_2, param_1 < (ulong)((long)param_2 + lVar8) &&
         (uVar7 = (uVar7 & 0xff00ff00) >> 8 | (uVar7 & 0xff00ff) << 8,
         puVar1 = (undefined4 *)(param_1 + (uVar7 >> 0x10 | uVar7 << 0x10)), param_2 < puVar1)) &&
        (puVar9 = puVar1,
        (ulong)((long)param_2 + (long)(int)param_3) < (ulong)((long)puVar1 + lVar8))))) {
      uVar7 = 0xfffffffd;
    }
    else {
      FUN_ffff0000be056ad8(param_1,puVar9,iVar5,uVar3);
      FUN_ffff0000be0608c8(param_2,puVar9,lVar8);
      uVar3 = (param_3 & 0xff00ff00) >> 8 | (param_3 & 0xff00ff) << 8;
      uVar7 = 0;
      *param_2 = 0xedfe0dd0;
      param_2[1] = uVar3 >> 0x10 | uVar3 << 0x10;
      *(undefined8 *)(param_2 + 5) = 0x1000000011000000;
      param_2[7] = *(undefined4 *)(param_1 + 0x1c);
    }
  }
  return uVar7;
}


