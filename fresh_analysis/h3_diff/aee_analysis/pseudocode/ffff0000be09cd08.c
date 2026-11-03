
int FUN_ffff0000be09cd08(ulong *param_1,ulong *param_2,ulong *param_3)

{
  uint uVar1;
  uint uVar2;
  ulong uVar3;
  ulong uVar4;
  int iVar5;
  uint *puVar6;
  uint *puVar7;
  ulong uVar8;
  uint *puVar9;
  long lVar10;
  ulong uVar11;
  uint uVar12;
  ulong local_48;
  long local_40;
  long local_38;
  
  uVar8 = param_2[1];
  do {
    uVar3 = uVar8;
    uVar8 = uVar3 - 1;
    if (uVar8 == 0xffffffffffffffff) break;
  } while (*(int *)(param_2[2] + uVar8 * 4) == 0);
  uVar11 = param_3[1];
  do {
    uVar4 = uVar11;
    uVar11 = uVar4 - 1;
    if (uVar11 == 0xffffffffffffffff) goto LAB_ffff0000be09cd84;
  } while (*(int *)(param_3[2] + uVar11 * 4) == 0);
  if (uVar3 <= uVar4) {
    if (uVar4 <= uVar3) {
      do {
        if (uVar8 == 0xffffffffffffffff) goto LAB_ffff0000be09cd84;
        uVar12 = *(uint *)(param_2[2] + uVar8 * 4);
        uVar1 = *(uint *)(param_3[2] + uVar8 * 4);
        if (uVar1 < uVar12) goto LAB_ffff0000be09cd84;
        uVar8 = uVar8 - 1;
      } while (uVar1 <= uVar12);
    }
    return -10;
  }
LAB_ffff0000be09cd84:
  local_40 = 0;
  local_38 = 0;
  local_48 = (ulong)CONCAT12(DAT_ffff0000be1b7e68 != 0,1);
  if (param_1 == param_3) {
    iVar5 = FUN_ffff0000be09bf10(&local_48,param_3);
    if (iVar5 != 0) goto LAB_ffff0000be09ce80;
    param_3 = &local_48;
  }
  if ((param_1 == param_2) || (iVar5 = FUN_ffff0000be09bf10(param_1,param_2), iVar5 == 0)) {
    *(undefined2 *)param_1 = 1;
    uVar8 = param_3[1];
    do {
      uVar3 = uVar8;
      uVar8 = uVar3 - 1;
      if (uVar8 == 0xffffffffffffffff) goto LAB_ffff0000be09ce7c;
    } while (((uint *)param_3[2])[uVar8] == 0);
    uVar12 = 0;
    uVar8 = 0;
    puVar6 = (uint *)param_1[2];
    puVar9 = (uint *)param_3[2];
    do {
      uVar1 = *puVar6;
      uVar8 = uVar8 + 1;
      uVar2 = uVar1 - uVar12;
      *puVar6 = uVar2;
      uVar12 = (uint)(uVar1 < uVar12);
      if (uVar2 < *puVar9) {
        uVar12 = uVar12 + 1;
      }
      puVar7 = puVar6 + 1;
      *puVar6 = uVar2 - *puVar9;
      puVar6 = puVar7;
      puVar9 = puVar9 + 1;
    } while (uVar3 != uVar8);
    if ((uVar12 != 0) && (uVar1 = *puVar7, *puVar7 = uVar1 - uVar12, uVar1 < uVar12)) {
      lVar10 = 4;
      do {
        iVar5 = *(int *)((long)puVar7 + lVar10);
        *(int *)((long)puVar7 + lVar10) = iVar5 + -1;
        lVar10 = lVar10 + 4;
      } while (iVar5 == 0);
    }
LAB_ffff0000be09ce7c:
    iVar5 = 0;
  }
LAB_ffff0000be09ce80:
  if (local_38 != 0) {
    FUN_ffff0000be0a174c(local_38,local_40 << 2);
    thunk_FUN_ffff0000be09937c(local_38);
  }
  return iVar5;
}


