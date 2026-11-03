
undefined8 FUN_ffff0000be0195b0(undefined8 *param_1)

{
  uint uVar1;
  ulong uVar2;
  ulong uVar3;
  ulong uVar4;
  ulong uVar5;
  ulong uVar6;
  undefined8 uVar7;
  undefined8 uVar8;
  undefined8 uVar9;
  undefined1 (*pauVar10) [16];
  undefined8 *puVar11;
  undefined8 uVar12;
  undefined8 uVar13;
  undefined8 in_x5;
  undefined8 in_x6;
  undefined8 in_x7;
  undefined8 extraout_x8;
  undefined8 extraout_x9;
  undefined8 extraout_x10;
  undefined8 extraout_x11;
  undefined8 extraout_x12;
  undefined8 extraout_x13;
  undefined8 extraout_x14;
  undefined8 extraout_x15;
  undefined8 extraout_x16;
  undefined8 extraout_x17;
  undefined8 extraout_x18;
  undefined8 unaff_x24;
  undefined8 unaff_x25;
  undefined8 unaff_x26;
  undefined8 unaff_x27;
  undefined8 unaff_x28;
  undefined8 unaff_x29;
  undefined1 auVar14 [16];
  
  uVar2 = UnkSytemRegRead(3,0,0xc,0xc,0);
  uVar1 = (uint)uVar2 & 0x3ff;
  if (uVar1 < 0x3fe) {
    if (uVar1 == 0x2dc) {
      FUN_ffff0000be05fd98(s_iframe__p__ffff0000be0a9e7b,param_1);
      FUN_ffff0000be05fd98
                (s_x0_0x_16llx_x1_0x_16llx_x2_0x_16_ffff0000be0c5e2e,*param_1,param_1[1],param_1[2],
                 param_1[3]);
      FUN_ffff0000be05fd98
                (s_x4_0x_16llx_x5_0x_16llx_x6_0x_16_ffff0000be0ae2ec,param_1[4],param_1[5],
                 param_1[6],param_1[7]);
      FUN_ffff0000be05fd98
                (s_x8_0x_16llx_x9_0x_16llx_x10_0x_1_ffff0000be0bcc4e,param_1[8],param_1[9],
                 param_1[10],param_1[0xb]);
      FUN_ffff0000be05fd98
                (s_x12_0x_16llx_x13_0x_16llx_x14_0x_ffff0000be0c5e63,param_1[0xc],param_1[0xd],
                 param_1[0xe],param_1[0xf]);
      uVar12 = param_1[0x11];
      uVar9 = param_1[0x12];
      uVar13 = param_1[0x13];
      FUN_ffff0000be05fd98(s_x16_0x_16llx_x17_0x_16llx_lr_0x__ffff0000be0b0a35,param_1[0x10]);
      FUN_ffff0000be05fd98(s_elr_0x_16llx_ffff0000be0ac369,param_1[0x14]);
      FUN_ffff0000be05fd98(s_spsr_0x_16llx_ffff0000be0c3b0e,param_1[0x15]);
      FUN_ffff0000be02456c(param_1);
      pauVar10 = (undefined1 (*) [16])FUN_ffff0000be024490();
      puVar11 = (undefined8 *)FUN_ffff0000be0244cc();
      auVar14 = FUN_ffff0000be02447c();
      if (((auVar14._0_8_ & 1) == 0) && (pauVar10 != (undefined1 (*) [16])0x0)) {
        *pauVar10 = auVar14;
        *(undefined8 *)pauVar10[1] = uVar12;
        *(undefined8 *)(pauVar10[1] + 8) = uVar9;
        *(undefined8 *)pauVar10[2] = uVar13;
        *(undefined8 *)(pauVar10[2] + 8) = in_x5;
        *(undefined8 *)pauVar10[3] = in_x6;
        *(undefined8 *)(pauVar10[3] + 8) = in_x7;
        *(undefined8 *)pauVar10[4] = extraout_x8;
        *(undefined8 *)(pauVar10[4] + 8) = extraout_x9;
        *(undefined8 *)pauVar10[5] = extraout_x10;
        *(undefined8 *)(pauVar10[5] + 8) = extraout_x11;
        *(undefined8 *)pauVar10[6] = extraout_x12;
        *(undefined8 *)(pauVar10[6] + 8) = extraout_x13;
        *(undefined8 *)pauVar10[7] = extraout_x14;
        *(undefined8 *)(pauVar10[7] + 8) = extraout_x15;
        *(undefined8 *)pauVar10[8] = extraout_x16;
        *(undefined8 *)(pauVar10[8] + 8) = extraout_x17;
        *(undefined8 *)pauVar10[9] = extraout_x18;
        *(undefined8 **)(pauVar10[9] + 8) = param_1;
        *(undefined8 **)pauVar10[10] = puVar11;
        *(undefined1 (**) [16])(pauVar10[10] + 8) = pauVar10;
        *(ulong *)pauVar10[0xb] = uVar2;
        *(undefined8 *)(pauVar10[0xb] + 8) = 0x2dc;
        *(undefined8 *)pauVar10[0xc] = unaff_x24;
        *(undefined8 *)(pauVar10[0xc] + 8) = unaff_x25;
        *(undefined8 *)pauVar10[0xd] = unaff_x26;
        *(undefined8 *)(pauVar10[0xd] + 8) = unaff_x27;
        *(undefined8 *)pauVar10[0xe] = unaff_x28;
        *(undefined1 **)(pauVar10[0xe] + 8) = &stack0xffffffffffffffc0;
        *(undefined8 *)pauVar10[0xf] = 0xffff0000be01969c;
        *(undefined1 **)(pauVar10[0xf] + 8) = &stack0xffffffffffffffc0;
        uVar5 = currentel;
        uVar3 = spsel;
        uVar4 = daif;
        uVar6 = nzcv;
        *(undefined8 *)pauVar10[0x10] = 0xffff0000be019708;
        *(ulong *)(pauVar10[0x10] + 8) = uVar5 | uVar3 | uVar4 | uVar6;
        FUN_ffff0000be0075a0(*(undefined8 *)(pauVar10[0xe] + 8),*(undefined8 *)pauVar10[0xf]);
      }
      if (puVar11 != (undefined8 *)0x0) {
        uVar12 = sctlr_el1;
        uVar8 = tcr_el1;
        uVar13 = ttbr0_el1;
        uVar7 = ttbr1_el1;
        uVar9 = sp_el0;
        *puVar11 = uVar12;
        puVar11[1] = uVar8;
        puVar11[2] = uVar13;
        puVar11[3] = uVar7;
        puVar11[4] = uVar9;
        puVar11[5] = &stack0xffffffffffffffc0;
        FUN_ffff0000be05fd98(s_Crash_regs_saved_ffff0000be0a552b);
      }
      FUN_ffff0000be007328(entry,0x800000);
      FUN_ffff0000be0075a0(unaff_x29,param_1[0x12]);
    }
    puVar11 = (undefined8 *)(&DAT_ffff0000be1088a8 + (uVar2 & 0x3ff) * 0x10);
    if (0x1f < uVar1) {
      puVar11 = (undefined8 *)(&DAT_ffff0000be108aa8 + (ulong)(uVar1 - 0x20) * 0x10);
    }
    if ((code *)*puVar11 == (code *)0x0) {
      uVar9 = 0;
    }
    else {
      uVar9 = (*(code *)*puVar11)(puVar11[1]);
    }
    UnkSytemRegWrite(3,0,0xc,0xc,1,uVar2 & 0xffffffff);
  }
  else {
    uVar9 = 0;
  }
  return uVar9;
}


