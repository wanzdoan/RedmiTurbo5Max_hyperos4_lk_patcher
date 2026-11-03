// ffff00009070866c  size=164  FUN_ffff00009070866c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009070866c(undefined8 param_1)

{
  undefined8 *puVar1;
  ulong uVar2;
  long lVar3;
  
  uVar2 = cpacr_el1;
  if ((~(uint)uVar2 & 0x300000) != 0) {
    cpacr_el1 = uVar2 | 0x300000;
    InstructionSynchronizationBarrier();
    lVar3 = tpidr_el1;
    if ((lVar3 != 0) &&
       ((puVar1 = (undefined8 *)(lVar3 + 0x58), puVar1 != _DAT_ffff00009083d130 ||
        (*(int *)(lVar3 + 0x260) != 0)))) {
      *(undefined4 *)(lVar3 + 0x260) = 0;
      param_1 = *puVar1;
      fpcr = (ulong)*(uint *)(lVar3 + 600);
      fpsr = (ulong)*(uint *)(lVar3 + 0x25c);
      _DAT_ffff00009083d130 = puVar1;
    }
  }
  return param_1;
}


