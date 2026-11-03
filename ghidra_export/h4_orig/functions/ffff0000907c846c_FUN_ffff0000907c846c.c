// ffff0000907c846c  size=568  FUN_ffff0000907c846c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000907c846c(ulong *param_1,ulong *param_2,ulong *param_3)

{
  int iVar1;
  ulong uVar2;
  ulong uVar3;
  long lVar4;
  long lVar5;
  ulong uVar6;
  long lVar7;
  ulong local_80;
  long local_78;
  long local_70;
  ulong local_68;
  long local_60;
  long local_58;
  
  local_60 = 0;
  local_58 = 0;
  local_78 = 0;
  local_70 = 0;
  local_68 = (ulong)CONCAT12(_DAT_ffff00009090e620 != 0,1);
  local_80 = (ulong)CONCAT12(_DAT_ffff00009090e620 != 0,1);
  if (param_1 == param_2) {
    param_2 = &local_68;
    iVar1 = FUN_ffff0000907c7260(&local_68,param_1);
    if (iVar1 != 0) goto joined_r0xffff0000907c861c;
  }
  if ((param_1 == param_3) &&
     (iVar1 = FUN_ffff0000907c7260(&local_80,param_1), param_3 = &local_80, iVar1 != 0))
  goto joined_r0xffff0000907c861c;
  uVar6 = param_2[1];
  do {
    uVar3 = uVar6;
    if (uVar3 == 0) break;
    uVar6 = uVar3 - 1;
  } while (*(int *)(param_2[2] + uVar3 * 4 + -4) == 0);
  lVar4 = param_3[1] * 4;
  lVar7 = -param_3[1];
  do {
    lVar5 = lVar7;
    lVar4 = lVar4 + -4;
    if (lVar5 == 0) break;
    lVar7 = lVar5 + 1;
  } while (*(int *)(param_3[2] + lVar4) == 0);
  uVar6 = uVar3 - lVar5;
  if (uVar6 < 0x2711) {
    if (param_1[1] < uVar6) {
      uVar2 = FUN_ffff00009077d5a8(uVar6,4);
      if (uVar2 == 0) goto LAB_ffff0000907c853c;
      if (param_1[2] != 0) {
        FUN_ffff00009077eb20(uVar2,param_1[2],param_1[1] << 2);
        FUN_ffff0000907cca9c(param_1[2],param_1[1] << 2);
        thunk_FUN_ffff0000907c314c(param_1[2]);
      }
      param_1[1] = uVar6;
      param_1[2] = uVar2;
    }
    iVar1 = FUN_ffff0000907c7528(param_1,0);
    if (iVar1 == 0) {
      if (lVar5 != 0) {
        lVar7 = 0;
        do {
          FUN_ffff0000907c86a4
                    (uVar3,param_2[2],param_1[2] + lVar4,*(undefined4 *)(param_3[2] + lVar4));
          lVar4 = lVar4 + -4;
          lVar7 = lVar7 + 1;
        } while (-lVar7 != lVar5);
      }
      iVar1 = 0;
      *(short *)param_1 = (short)*param_3 * (short)*param_2;
    }
  }
  else {
LAB_ffff0000907c853c:
    iVar1 = -0x10;
  }
joined_r0xffff0000907c861c:
  if (local_70 != 0) {
    FUN_ffff0000907cca9c(local_70,local_78 << 2);
    thunk_FUN_ffff0000907c314c(local_70);
  }
  local_78 = 0;
  local_70 = 0;
  local_80 = CONCAT62(local_80._2_6_,1);
  if (local_58 != 0) {
    FUN_ffff0000907cca9c(local_58,local_60 << 2);
    thunk_FUN_ffff0000907c314c(local_58);
  }
  return iVar1;
}


