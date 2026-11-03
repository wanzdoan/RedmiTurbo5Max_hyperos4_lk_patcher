
void FUN_ffff0000be01afc4(undefined4 param_1)

{
  long lVar1;
  undefined8 uVar2;
  ulong uVar3;
  undefined4 *puVar4;
  long *plVar5;
  uint uVar6;
  long lVar7;
  long *plVar8;
  undefined8 *puVar9;
  
  puVar4 = (undefined4 *)tpidr_el1;
  uVar2 = daif;
  if (((uint)uVar2 >> 7 & 1) == 0) {
    uVar3 = daif;
    daif = uVar3 | 0x80;
  }
  uVar6 = puVar4[0xd];
  puVar4[0xa2] = param_1;
  DAT_ffff0000be10c6e8 = 1;
  puVar4[0xb] = 5;
  if ((uVar6 & 1) == 0) {
    uVar2 = tpidr_el1;
    plVar5 = *(long **)(puVar4 + 0xa8);
    uVar6 = DAT_ffff0000be10cc00;
    while (DAT_ffff0000be10cc00 = uVar6, plVar5 != (long *)(puVar4 + 0xa6)) {
      plVar8 = (long *)plVar5[1];
      *plVar8 = *plVar5;
      *(long **)(*plVar5 + 8) = plVar8;
      *plVar5 = 0;
      plVar5[1] = 0;
      if (plVar5 == (long *)0x18) break;
      lVar1 = (long)(int)*(uint *)(plVar5 + 2);
      uVar6 = 1 << (ulong)(*(uint *)(plVar5 + 2) & 0x1f) | uVar6;
      puVar4[0xaa] = puVar4[0xaa] + -1;
      lVar7 = (&DAT_ffff0000be10c738)[lVar1 * 2];
      *plVar5 = (long)(&DAT_ffff0000be10c730 + lVar1 * 2);
      *(undefined4 *)((long)plVar5 + 0x14) = 1;
      plVar5[1] = lVar7;
      puVar9 = (undefined8 *)(&DAT_ffff0000be10c738)[lVar1 * 2];
      (&DAT_ffff0000be10c738)[lVar1 * 2] = plVar5;
      plVar8 = *(long **)(puVar4 + 0xa8);
      *(undefined4 *)(plVar5 + 6) = 0;
      plVar5[5] = 0;
      *puVar9 = plVar5;
      plVar5 = plVar8;
    }
  }
  else {
    puVar9 = *(undefined8 **)(puVar4 + 4);
    *puVar4 = 0;
    *puVar9 = *(undefined8 *)(puVar4 + 2);
    *(undefined8 **)(*(long *)(puVar4 + 2) + 8) = puVar9;
    *(undefined8 *)(puVar4 + 2) = 0;
    *(undefined8 *)(puVar4 + 4) = 0;
    if (((uVar6 >> 1 & 1) != 0) && (*(long *)(puVar4 + 0x9a) != 0)) {
      FUN_ffff0000be05f318();
      uVar6 = puVar4[0xd] & 0xffffffdf;
      puVar4[0xd] = uVar6;
    }
    if ((uVar6 >> 2 & 1) != 0) {
      FUN_ffff0000be05f318(puVar4);
    }
  }
  FUN_ffff0000be01b0ec();
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000be05ee24(s_somehow_fell_through_thread_exit_ffff0000be0aa24c);
}


