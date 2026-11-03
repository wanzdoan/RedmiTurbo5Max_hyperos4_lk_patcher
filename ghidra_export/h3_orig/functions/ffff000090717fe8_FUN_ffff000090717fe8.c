// ffff000090717fe8  size=344  FUN_ffff000090717fe8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090717fe8(void)

{
  long *plVar1;
  uint uVar2;
  uint uVar3;
  uint uVar4;
  long *plVar5;
  long *plVar6;
  long lVar7;
  undefined8 *puVar8;
  long *plVar9;
  long *plVar10;
  
  uVar2 = _DAT_ffff000090843b90;
  plVar6 = (long *)tpidr_el1;
  if (_DAT_ffff000090843b90 == 0) {
    plVar10 = (long *)&DAT_ffff0000908438c0;
  }
  else {
    plVar10 = (long *)&DAT_ffff0000908438c0;
    uVar3 = _DAT_ffff000090843b90;
    do {
      uVar4 = (uint)LZCOUNT(uVar3) ^ 0x1f;
      plVar1 = (long *)((ulong)uVar4 * 0x10 + -0xffff6f7bc940);
      puVar8 = (undefined8 *)((ulong)uVar4 * 0x10 + -0xffff6f7bc938);
      plVar9 = (long *)*puVar8;
      if (plVar9 != plVar1) {
        plVar10 = plVar9 + -3;
        plVar5 = (long *)plVar9[1];
        *plVar5 = *plVar9;
        lVar7 = *plVar9;
        *plVar9 = 0;
        *(long **)(lVar7 + 8) = plVar5;
        plVar9[1] = 0;
        if ((long *)*puVar8 == plVar1) {
          _DAT_ffff000090843b90 = uVar2 & (1 << (ulong)(uVar4 & 0x1f) ^ 0xffffffffU);
        }
        break;
      }
      uVar3 = uVar3 & (1 << (ulong)(uVar4 & 0x1f) ^ 0xffffffffU);
    } while (uVar3 != 0);
  }
  *(undefined4 *)((long)plVar10 + 0x2c) = 2;
  if (plVar10 == plVar6) {
    return;
  }
  if ((int)plVar10[6] < 1) {
    *(undefined4 *)(plVar10 + 6) = 5;
  }
  uVar2 = *(uint *)((long)plVar6 + 0x34) & 0x18;
  if ((*(byte *)((long)plVar10 + 0x34) & 0x18) == 0) {
    if (uVar2 != 0) {
      FUN_ffff00009071872c(0xffff000090843690,10,FUN_ffff000090718204,0);
    }
  }
  else if (uVar2 == 0) {
    FUN_ffff00009071874c(0xffff000090843690);
  }
  tpidr_el1 = plVar10;
  InstructionSynchronizationBarrier();
  if (plVar10[7] != plVar6[7]) {
    FUN_ffff00009077bc38();
  }
  FUN_ffff0000907087a0(plVar6,plVar10);
  return;
}


