
void FUN_ffff00009071812c(void)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  long *plVar4;
  long *plVar5;
  long lVar6;
  long *plVar7;
  long *plVar8;
  
  uVar1 = DAT_ffff000090843bc8;
  plVar5 = (long *)tpidr_el1;
  if (DAT_ffff000090843bc8 == 0) {
    plVar8 = (long *)&DAT_ffff0000908438f8;
  }
  else {
    plVar8 = (long *)&DAT_ffff0000908438f8;
    uVar2 = DAT_ffff000090843bc8;
    do {
      uVar3 = (uint)LZCOUNT(uVar2) ^ 0x1f;
      plVar7 = (long *)(&DAT_ffff000090843700)[(ulong)uVar3 * 2];
      if (plVar7 != &DAT_ffff0000908436f8 + (ulong)uVar3 * 2) {
        plVar8 = plVar7 + -3;
        plVar4 = (long *)plVar7[1];
        *plVar4 = *plVar7;
        lVar6 = *plVar7;
        *plVar7 = 0;
        *(long **)(lVar6 + 8) = plVar4;
        plVar7[1] = 0;
        if ((long *)(&DAT_ffff000090843700)[(ulong)uVar3 * 2] ==
            &DAT_ffff0000908436f8 + (ulong)uVar3 * 2) {
          DAT_ffff000090843bc8 = uVar1 & (1 << (ulong)(uVar3 & 0x1f) ^ 0xffffffffU);
        }
        break;
      }
      uVar2 = uVar2 & (1 << (ulong)(uVar3 & 0x1f) ^ 0xffffffffU);
    } while (uVar2 != 0);
  }
  *(undefined4 *)((long)plVar8 + 0x2c) = 2;
  if (plVar8 == plVar5) {
    return;
  }
  if ((int)plVar8[6] < 1) {
    *(undefined4 *)(plVar8 + 6) = 5;
  }
  uVar1 = *(uint *)((long)plVar5 + 0x34) & 0x18;
  if ((*(byte *)((long)plVar8 + 0x34) & 0x18) == 0) {
    if (uVar1 != 0) {
      FUN_ffff000090718870(&DAT_ffff0000908436c8,10,&LAB_ffff000090718348,0);
    }
  }
  else if (uVar1 == 0) {
    FUN_ffff000090718890(&DAT_ffff0000908436c8);
  }
  tpidr_el1 = plVar8;
  InstructionSynchronizationBarrier();
  if (plVar8[7] != plVar5[7]) {
    FUN_ffff00009077bf00();
  }
  FUN_ffff00009070872c(plVar5,plVar8);
  return;
}


