// ffff0000907cd238  size=520  FUN_ffff0000907cd238

undefined8 FUN_ffff0000907cd238(long param_1,undefined8 param_2,ulong param_3)

{
  ulong uVar1;
  undefined8 uVar2;
  ulong uVar3;
  long lVar4;
  long lVar5;
  undefined4 *puVar6;
  ulong uVar7;
  ulong uVar8;
  ulong uVar9;
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined4 local_68;
  undefined8 local_60;
  long local_58;
  undefined8 uStack_50;
  undefined8 local_48;
  ulong local_40;
  undefined1 *local_38;
  
  local_38 = (undefined1 *)&local_90;
  uVar1 = *(ulong *)(param_1 + 8) - param_3;
  if (*(ulong *)(param_1 + 8) < param_3) {
    return 0;
  }
  if (param_3 <= uVar1) {
    uVar1 = param_3;
  }
  local_58 = 2;
  local_90 = 0;
  uStack_88 = 0;
  local_80 = 0;
  uStack_78 = 0;
  local_70 = 0;
  local_68 = 0;
  local_60 = 1;
  local_48 = 1;
  uStack_50 = param_2;
  FUN_ffff00009077ee50(&local_90,0,0x2c);
  FUN_ffff00009077eb20(&local_90,*(long *)(param_1 + 0x10) + param_3 * 4,uVar1 << 2);
  uVar3 = *(ulong *)(param_1 + 8);
  local_40 = uVar1 + 2;
  uVar1 = uVar3 - param_3;
  if (param_3 <= uVar3 && uVar1 != 0) {
    lVar5 = *(long *)(param_1 + 0x10);
    uVar7 = param_3;
    if (1 < uVar1) {
      uVar8 = uVar1 & 0xfffffffffffffffe;
      uVar7 = uVar8 + param_3;
      lVar4 = lVar5 + param_3 * 4 + 4;
      uVar9 = uVar8;
      do {
        *(undefined8 *)(lVar4 + -4) = 0;
        uVar9 = uVar9 - 2;
        lVar4 = lVar4 + 8;
      } while (uVar9 != 0);
      if (uVar1 == uVar8) goto LAB_ffff0000907cd330;
    }
    lVar4 = uVar3 - uVar7;
    puVar6 = (undefined4 *)(lVar5 + uVar7 * 4);
    do {
      lVar4 = lVar4 + -1;
      *puVar6 = 0;
      puVar6 = puVar6 + 1;
    } while (lVar4 != 0);
  }
LAB_ffff0000907cd330:
  uVar2 = FUN_ffff0000907c846c(&local_48,&local_48,&local_60);
  if ((int)uVar2 != 0) {
    return uVar2;
  }
  uVar2 = FUN_ffff0000907c7de8(param_1,param_1,&local_48);
  if ((int)uVar2 != 0) {
    return uVar2;
  }
  local_40 = *(long *)(param_1 + 8) - param_3;
  if (param_3 <= local_40) {
    local_40 = param_3;
  }
  FUN_ffff00009077ee50(&local_90,0,0x2c);
  FUN_ffff00009077eb20(&local_90,*(long *)(param_1 + 0x10) + param_3 * 4,local_40 << 2);
  uVar3 = *(ulong *)(param_1 + 8);
  local_40 = local_40 + local_58;
  uVar1 = uVar3 - param_3;
  if (param_3 <= uVar3 && uVar1 != 0) {
    lVar5 = *(long *)(param_1 + 0x10);
    if (1 < uVar1) {
      uVar9 = uVar1 & 0xfffffffffffffffe;
      lVar4 = param_3 * 4;
      param_3 = uVar9 + param_3;
      lVar4 = lVar5 + lVar4 + 4;
      uVar7 = uVar9;
      do {
        *(undefined8 *)(lVar4 + -4) = 0;
        uVar7 = uVar7 - 2;
        lVar4 = lVar4 + 8;
      } while (uVar7 != 0);
      if (uVar1 == uVar9) goto LAB_ffff0000907cd404;
    }
    lVar4 = uVar3 - param_3;
    puVar6 = (undefined4 *)(lVar5 + param_3 * 4);
    do {
      lVar4 = lVar4 + -1;
      *puVar6 = 0;
      puVar6 = puVar6 + 1;
    } while (lVar4 != 0);
  }
LAB_ffff0000907cd404:
  uVar2 = FUN_ffff0000907c846c(&local_48,&local_48,&local_60);
  if ((int)uVar2 == 0) {
    uVar2 = FUN_ffff0000907c7de8(param_1,param_1,&local_48);
  }
  return uVar2;
}


