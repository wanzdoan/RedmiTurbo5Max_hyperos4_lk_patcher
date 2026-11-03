
void FUN_ffff0000be020454
               (uint param_1,int param_2,undefined8 param_3,undefined8 param_4,undefined8 param_5,
               undefined8 param_6,undefined8 param_7,undefined8 param_8)

{
  undefined8 uVar1;
  ulong uVar2;
  ulong uVar3;
  ulong uVar4;
  ulong uVar5;
  undefined8 uVar6;
  undefined8 uVar7;
  undefined8 uVar8;
  undefined8 uVar9;
  bool bVar10;
  undefined4 uVar11;
  undefined1 (*pauVar12) [16];
  undefined8 *puVar13;
  undefined8 extraout_x8;
  undefined *puVar14;
  undefined8 extraout_x9;
  undefined **ppuVar15;
  undefined8 extraout_x10;
  undefined8 extraout_x11;
  undefined8 extraout_x12;
  undefined8 extraout_x13;
  undefined8 extraout_x14;
  undefined8 extraout_x15;
  undefined8 extraout_x16;
  undefined8 extraout_x17;
  undefined8 extraout_x18;
  undefined **ppuVar16;
  undefined8 unaff_x23;
  undefined8 unaff_x24;
  undefined8 unaff_x25;
  undefined **ppuVar17;
  undefined8 unaff_x26;
  undefined8 unaff_x27;
  undefined8 unaff_x28;
  undefined1 auVar18 [16];
  
  if (param_2 == 9) {
    pauVar12 = (undefined1 (*) [16])FUN_ffff0000be024490();
    puVar13 = (undefined8 *)FUN_ffff0000be0244cc();
    auVar18 = FUN_ffff0000be02447c();
    if (((auVar18._0_8_ & 1) == 0) && (pauVar12 != (undefined1 (*) [16])0x0)) {
      *pauVar12 = auVar18;
      *(undefined8 *)pauVar12[1] = param_3;
      *(undefined8 *)(pauVar12[1] + 8) = param_4;
      *(undefined8 *)pauVar12[2] = param_5;
      *(undefined8 *)(pauVar12[2] + 8) = param_6;
      *(undefined8 *)pauVar12[3] = param_7;
      *(undefined8 *)(pauVar12[3] + 8) = param_8;
      *(undefined8 *)pauVar12[4] = extraout_x8;
      *(undefined8 *)(pauVar12[4] + 8) = extraout_x9;
      *(undefined8 *)pauVar12[5] = extraout_x10;
      *(undefined8 *)(pauVar12[5] + 8) = extraout_x11;
      *(undefined8 *)pauVar12[6] = extraout_x12;
      *(undefined8 *)(pauVar12[6] + 8) = extraout_x13;
      *(undefined8 *)pauVar12[7] = extraout_x14;
      *(undefined8 *)(pauVar12[7] + 8) = extraout_x15;
      *(undefined8 *)pauVar12[8] = extraout_x16;
      *(undefined8 *)(pauVar12[8] + 8) = extraout_x17;
      *(undefined8 *)pauVar12[9] = extraout_x18;
      *(undefined8 *)(pauVar12[9] + 8) = 9;
      *(ulong *)pauVar12[10] = (ulong)param_1;
      *(undefined8 **)(pauVar12[10] + 8) = puVar13;
      *(undefined1 (**) [16])pauVar12[0xb] = pauVar12;
      *(undefined8 *)(pauVar12[0xb] + 8) = unaff_x23;
      *(undefined8 *)pauVar12[0xc] = unaff_x24;
      *(undefined8 *)(pauVar12[0xc] + 8) = unaff_x25;
      *(undefined8 *)pauVar12[0xd] = unaff_x26;
      *(undefined8 *)(pauVar12[0xd] + 8) = unaff_x27;
      *(undefined8 *)pauVar12[0xe] = unaff_x28;
      *(undefined1 **)(pauVar12[0xe] + 8) = &stack0xffffffffffffffb0;
      *(undefined8 *)pauVar12[0xf] = 0xffff0000be020494;
      *(undefined1 **)(pauVar12[0xf] + 8) = &stack0xffffffffffffffb0;
      uVar4 = currentel;
      uVar2 = spsel;
      uVar3 = daif;
      uVar5 = nzcv;
      *(undefined8 *)pauVar12[0x10] = 0xffff0000be020500;
      *(ulong *)(pauVar12[0x10] + 8) = uVar4 | uVar2 | uVar3 | uVar5;
      FUN_ffff0000be0075a0(*(undefined8 *)(pauVar12[0xe] + 8),*(undefined8 *)pauVar12[0xf]);
    }
    if (puVar13 != (undefined8 *)0x0) {
      uVar6 = sctlr_el1;
      uVar9 = tcr_el1;
      uVar7 = ttbr0_el1;
      uVar8 = ttbr1_el1;
      uVar1 = sp_el0;
      *puVar13 = uVar6;
      puVar13[1] = uVar9;
      puVar13[2] = uVar7;
      puVar13[3] = uVar8;
      puVar13[4] = uVar1;
      puVar13[5] = &stack0xffffffffffffffb0;
      FUN_ffff0000be05fd98(s_Crash_regs_saved_ffff0000be0a552b);
    }
    FUN_ffff0000be007328(entry,0x800000);
  }
  bVar10 = param_1 == 2;
  if (param_1 < 2) {
    if (param_2 != 7) {
      bVar10 = param_2 == 1;
      goto LAB_ffff0000be020564;
    }
LAB_ffff0000be020568:
    FUN_ffff0000be05fd98(s_HALT__action____d__reason____d_ffff0000be0ae6bf,param_1,param_2);
    thunk_FUN_ffff0000be048e48(0);
    FUN_ffff0000be05fd98(s_spin_17ms_ffff0000be0b1be0);
    FUN_ffff0000be05ede4(17000);
    thunk_FUN_ffff0000be0484b4();
  }
  else {
LAB_ffff0000be020564:
    if (bVar10) goto LAB_ffff0000be020568;
  }
  FUN_ffff0000be05fd98(s_HALT__action____d__reason____d_ffff0000be0ae6bf,param_1,param_2);
  uVar1 = daif;
  if (((uint)uVar1 >> 7 & 1) == 0) {
    uVar2 = daif;
    daif = uVar2 | 0x80;
  }
  DAT_ffff0000be0d64e0 = 1;
  ppuVar16 = (undefined **)PTR_LOOP_ffff0000be0d64d8;
  if ((undefined **)PTR_LOOP_ffff0000be0d64d8 != &PTR_LOOP_ffff0000be0d64d0) {
    do {
      ppuVar17 = (undefined **)ppuVar16[1];
      ppuVar15 = ppuVar17;
      if ((code *)ppuVar16[4] != (code *)0x0) {
        (*(code *)ppuVar16[4])(ppuVar16[3],param_1,param_2);
        FUN_ffff0000be05fd98(s__s_done_ffff0000be0c19ae,ppuVar16[2]);
        ppuVar15 = (undefined **)ppuVar16[1];
      }
      *ppuVar15 = *ppuVar16;
      puVar14 = *ppuVar16;
      *ppuVar16 = (undefined *)0x0;
      *(undefined ***)(puVar14 + 8) = ppuVar15;
      ppuVar16[1] = (undefined *)0x0;
      thunk_FUN_ffff0000be09937c(ppuVar16);
      ppuVar16 = ppuVar17;
    } while (ppuVar17 != &PTR_LOOP_ffff0000be0d64d0);
  }
  DAT_ffff0000be0d64e0 = 0;
  if (((uint)uVar1 >> 7 & 1) == 0) {
    uVar2 = daif;
    daif = uVar2 & 0xffffffffffffff7f;
  }
  if (param_1 < 2) {
    if (param_2 == 1) {
      uVar11 = FUN_ffff0000be028bec();
      goto LAB_ffff0000be020690;
    }
    FUN_ffff0000be03580c(param_2);
  }
  else if (param_1 == 2) {
    uVar11 = FUN_ffff0000be028c44(0);
    goto LAB_ffff0000be020690;
  }
  uVar11 = 0;
LAB_ffff0000be020690:
  FUN_ffff0000be05fd98(s_HALT__spinning_forever_____ret___ffff0000be0c069e,uVar11);
  uVar2 = daif;
  daif = uVar2 | 0x80;
  do {
                    /* WARNING: Do nothing block with infinite loop */
  } while( true );
}


