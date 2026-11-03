
void FUN_ffff0000b8007cb4
               (undefined8 *param_1,undefined8 param_2,undefined8 param_3,undefined8 param_4,
               undefined8 param_5,undefined8 param_6,undefined8 param_7,undefined8 param_8)

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
  undefined1 (*pauVar10) [16];
  undefined8 *puVar11;
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
  undefined8 unaff_x22;
  undefined8 unaff_x23;
  undefined8 unaff_x24;
  undefined8 unaff_x25;
  undefined8 unaff_x26;
  undefined8 unaff_x27;
  undefined8 unaff_x28;
  undefined1 auVar12 [16];
  
  FUN_ffff0000b801c4dc();
  pauVar10 = (undefined1 (*) [16])FUN_ffff0000b801c464();
  puVar11 = (undefined8 *)FUN_ffff0000b801c4a0();
  auVar12 = FUN_ffff0000b801c450();
  if (((auVar12._0_8_ & 1) == 0) && (pauVar10 != (undefined1 (*) [16])0x0)) {
    *pauVar10 = auVar12;
    *(undefined8 *)pauVar10[1] = param_3;
    *(undefined8 *)(pauVar10[1] + 8) = param_4;
    *(undefined8 *)pauVar10[2] = param_5;
    *(undefined8 *)(pauVar10[2] + 8) = param_6;
    *(undefined8 *)pauVar10[3] = param_7;
    *(undefined8 *)(pauVar10[3] + 8) = param_8;
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
    *(undefined8 *)pauVar10[0xb] = unaff_x22;
    *(undefined8 *)(pauVar10[0xb] + 8) = unaff_x23;
    *(undefined8 *)pauVar10[0xc] = unaff_x24;
    *(undefined8 *)(pauVar10[0xc] + 8) = unaff_x25;
    *(undefined8 *)pauVar10[0xd] = unaff_x26;
    *(undefined8 *)(pauVar10[0xd] + 8) = unaff_x27;
    *(undefined8 *)pauVar10[0xe] = unaff_x28;
    *(undefined1 **)(pauVar10[0xe] + 8) = &stack0xffffffffffffffd0;
    *(undefined8 *)pauVar10[0xf] = 0xffff0000b8007ce4;
    *(undefined1 **)(pauVar10[0xf] + 8) = &stack0xffffffffffffffd0;
    uVar4 = currentel;
    uVar2 = spsel;
    uVar3 = daif;
    uVar5 = nzcv;
    *(undefined8 *)pauVar10[0x10] = 0xffff0000b8007d50;
    *(ulong *)(pauVar10[0x10] + 8) = uVar4 | uVar2 | uVar3 | uVar5;
    FUN_ffff0000b8007824(*(undefined8 *)(pauVar10[0xe] + 8),*(undefined8 *)pauVar10[0xf]);
  }
  if (puVar11 != (undefined8 *)0x0) {
    uVar6 = sctlr_el1;
    uVar9 = tcr_el1;
    uVar7 = ttbr0_el1;
    uVar8 = ttbr1_el1;
    uVar1 = sp_el0;
    *puVar11 = uVar6;
    puVar11[1] = uVar9;
    puVar11[2] = uVar7;
    puVar11[3] = uVar8;
    puVar11[4] = uVar1;
    puVar11[5] = &stack0xffffffffffffffd0;
    FUN_ffff0000b804c33c(s_Crash_regs_saved_ffff0000b8086e7d);
  }
  FUN_ffff0000b80075ac(entry,0x2000000);
  FUN_ffff0000b804c33c(s_iframe__p__ffff0000b808a745,param_1);
  FUN_ffff0000b804c33c
            (s_x0_0x_16llx_x1_0x_16llx_x2_0x_16_ffff0000b80a0d40,*param_1,param_1[1],param_1[2],
             param_1[3]);
  FUN_ffff0000b804c33c
            (s_x4_0x_16llx_x5_0x_16llx_x6_0x_16_ffff0000b808db32,param_1[4],param_1[5],param_1[6],
             param_1[7]);
  FUN_ffff0000b804c33c
            (s_x8_0x_16llx_x9_0x_16llx_x10_0x_1_ffff0000b80998ab,param_1[8],param_1[9],param_1[10],
             param_1[0xb]);
  FUN_ffff0000b804c33c
            (s_x12_0x_16llx_x13_0x_16llx_x14_0x_ffff0000b80a0d75,param_1[0xc],param_1[0xd],
             param_1[0xe],param_1[0xf]);
  FUN_ffff0000b804c33c
            (s_x16_0x_16llx_x17_0x_16llx_x18_0x_ffff0000b8086e48,param_1[0x10],param_1[0x11],
             param_1[0x12],param_1[0x13]);
  FUN_ffff0000b804c33c
            (s_x20_0x_16llx_x21_0x_16llx_x22_0x_ffff0000b809d345,param_1[0x14],param_1[0x15],
             param_1[0x16],param_1[0x17]);
  FUN_ffff0000b804c33c
            (s_x24_0x_16llx_x25_0x_16llx_x26_0x_ffff0000b808a751,param_1[0x18],param_1[0x19],
             param_1[0x1a],param_1[0x1b]);
  FUN_ffff0000b804c33c
            (s_x28_0x_16llx_x29_0x_16llx_lr_0x__ffff0000b8085f57,param_1[0x1c],param_1[0x1d],
             param_1[0x1e],param_1[0x1f]);
  FUN_ffff0000b804c33c(s_elr_0x_16llx_ffff0000b808c2d3,param_1[0x20]);
  FUN_ffff0000b804c33c(s_spsr_0x_16llx_ffff0000b809f23f,param_1[0x21]);
  FUN_ffff0000b8007824(param_1[0x1d],param_1[0x20]);
  return;
}


