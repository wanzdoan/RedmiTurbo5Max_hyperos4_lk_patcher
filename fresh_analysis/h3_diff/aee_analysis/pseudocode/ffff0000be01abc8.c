
/* WARNING: Removing unreachable block (ram,0xffff0000be01ad54) */

undefined8 FUN_ffff0000be01abc8(undefined4 *param_1)

{
  long lVar1;
  uint uVar2;
  ulong uVar3;
  long *plVar4;
  undefined8 uVar5;
  undefined8 *puVar6;
  uint uVar7;
  long lVar8;
  long *plVar9;
  
  uVar5 = daif;
  uVar7 = (uint)uVar5;
  if ((uVar7 >> 7 & 1) == 0) {
    uVar3 = daif;
    daif = uVar3 | 0x80;
  }
  uVar5 = tpidr_el1;
  plVar4 = *(long **)(param_1 + 0xa8);
  uVar2 = DAT_ffff0000be10cc00;
  while (DAT_ffff0000be10cc00 = uVar2, plVar4 != (long *)(param_1 + 0xa6)) {
    plVar9 = (long *)plVar4[1];
    *plVar9 = *plVar4;
    *(long **)(*plVar4 + 8) = plVar9;
    *plVar4 = 0;
    plVar4[1] = 0;
    if (plVar4 == (long *)0x18) break;
    lVar1 = (long)(int)*(uint *)(plVar4 + 2);
    uVar2 = 1 << (ulong)(*(uint *)(plVar4 + 2) & 0x1f) | uVar2;
    param_1[0xaa] = param_1[0xaa] + -1;
    lVar8 = (&DAT_ffff0000be10c738)[lVar1 * 2];
    *plVar4 = (long)(&DAT_ffff0000be10c730 + lVar1 * 2);
    *(undefined4 *)((long)plVar4 + 0x14) = 1;
    plVar4[1] = lVar8;
    puVar6 = (undefined8 *)(&DAT_ffff0000be10c738)[lVar1 * 2];
    (&DAT_ffff0000be10c738)[lVar1 * 2] = plVar4;
    plVar9 = *(long **)(param_1 + 0xa8);
    *(undefined4 *)(plVar4 + 6) = 0xffffffde;
    plVar4[5] = 0;
    *puVar6 = plVar4;
    plVar4 = plVar9;
  }
  if (param_1[0xb] != 5) {
    DAT_ffff0000be10c6e8 = 0;
    param_1[0xd] = param_1[0xd] | 1;
    if ((uVar7 >> 7 & 1) == 0) {
      uVar3 = daif;
      daif = uVar3 & 0xffffffffffffff7f;
    }
    return 0;
  }
  param_1[0xd] = param_1[0xd] & 0xfffffffe;
  if ((uVar7 >> 7 & 1) == 0) {
    uVar3 = daif;
    daif = uVar3 & 0xffffffffffffff7f;
  }
  uVar5 = daif;
  uVar7 = (uint)uVar5;
  if ((uVar7 >> 7 & 1) == 0) {
    uVar3 = daif;
    daif = uVar3 | 0x80;
    if ((*(byte *)(param_1 + 0xd) & 1) != 0) {
      uVar3 = daif;
      daif = uVar3 & 0xffffffffffffff7f;
      DAT_ffff0000be10c6e8 = 0;
      return 0xffffffde;
    }
  }
  else if ((*(byte *)(param_1 + 0xd) & 1) != 0) {
    DAT_ffff0000be10c6e8 = 0;
    return 0xffffffde;
  }
  DAT_ffff0000be10c6e8 = 1;
  if ((param_1[0xb] == 5) || (uVar5 = FUN_ffff0000be01ade0(param_1 + 0xa4,0), -1 < (int)uVar5)) {
    puVar6 = *(undefined8 **)(param_1 + 4);
    *param_1 = 0;
    DAT_ffff0000be10c6e8 = 0;
    *puVar6 = *(undefined8 *)(param_1 + 2);
    *(undefined8 **)(*(long *)(param_1 + 2) + 8) = puVar6;
    *(undefined8 *)(param_1 + 2) = 0;
    *(undefined8 *)(param_1 + 4) = 0;
    if ((uVar7 >> 7 & 1) == 0) {
      uVar3 = daif;
      daif = uVar3 & 0xffffffffffffff7f;
    }
    uVar7 = param_1[0xd];
    if (((uVar7 >> 1 & 1) != 0) && (*(long *)(param_1 + 0x9a) != 0)) {
      thunk_FUN_ffff0000be09937c();
      uVar7 = param_1[0xd];
    }
    if ((uVar7 >> 2 & 1) != 0) {
      thunk_FUN_ffff0000be09937c(param_1);
    }
    uVar5 = 0;
  }
  else {
    DAT_ffff0000be10c6e8 = 0;
    if ((uVar7 >> 7 & 1) == 0) {
      uVar3 = daif;
      daif = uVar3 & 0xffffffffffffff7f;
    }
  }
  return uVar5;
}


