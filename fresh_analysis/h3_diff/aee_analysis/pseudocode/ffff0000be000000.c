
void entry(undefined8 param_1,undefined8 param_2,undefined8 param_3,undefined8 param_4)

{
  undefined8 uVar1;
  bool bVar2;
  ulong uVar3;
  uint uVar4;
  long lVar5;
  ulong uVar6;
  undefined8 *extraout_x9;
  undefined8 *puVar7;
  ulong uVar8;
  ulong uVar9;
  long extraout_x11;
  ulong uVar10;
  ulong extraout_x12;
  undefined8 *puVar11;
  long extraout_x13;
  long extraout_x14;
  undefined8 *extraout_x14_00;
  long extraout_x15;
  long *plVar12;
  ulong uVar13;
  undefined *puVar14;
  code *pcVar15;
  ulong uVar16;
  ulong uVar17;
  undefined1 auVar18 [16];
  
  FUN_ffff0000be01d3bc();
  uVar1 = currentel;
  FUN_ffff0000be01d3c0();
  auVar18 = FUN_ffff0000be007194();
  uVar3 = auVar18._0_8_;
  cpacr_el1 = 0x300000;
  uVar8 = sctlr_el1;
  sctlr_el1 = uVar8 & 0xfffffffffffffffd | 0x101c;
  lVar5 = 0;
  DAT_ffff0000be0d5cd0 = param_3;
  DAT_ffff0000be0d5cd8 = param_4;
  DAT_ffff0000be0d5ce0 = uVar1;
  do {
    DAT_ffff0000be0d5cc8 = auVar18._8_8_;
    DAT_ffff0000be0d5cc0 = auVar18._0_8_;
    (&DAT_ffff0000be0f6000)[lVar5] = 0;
    auVar18._8_8_ = DAT_ffff0000be0d5cc8;
    auVar18._0_8_ = DAT_ffff0000be0d5cc0;
    lVar5 = lVar5 + 1;
  } while (lVar5 != 0x200);
  plVar12 = &DAT_ffff0000be0d5000;
  while( true ) {
    pcVar15 = (code *)*plVar12;
    puVar14 = (undefined *)plVar12[1];
    uVar4 = (uint)plVar12[3];
    uVar8 = plVar12[2];
    if ((uVar4 >> 3 & 1) != 0) {
      pcVar15 = entry;
      *plVar12 = (long)entry;
      plVar12[2] = uVar3;
      uVar8 = uVar3;
    }
    if (uVar8 == 0) break;
    uVar16 = uVar8;
    uVar17 = DAT_ffff0000be0002f8;
    if (((uVar4 >> 1 & 1) == 0) && (uVar17 = DAT_ffff0000be000300, (uVar4 >> 2 & 1) == 0))
    goto LAB_ffff0000be0000ac;
    while( true ) {
      bVar2 = uVar8 < uVar16;
      uVar8 = uVar8 - uVar16;
      do {
      } while (bVar2);
      do {
      } while ((((ulong)puVar14 | (ulong)pcVar15 | uVar16) & 0xfff) != 0);
      uVar13 = (ulong)puVar14 ^ 0xffff000000000000;
      do {
      } while ((uVar13 & 0xffff000000000000) != 0);
      do {
        puVar11 = &DAT_ffff0000be0f6000;
        uVar10 = 0x27;
        uVar9 = uVar13 >> 0x27;
        while (((0x1e < uVar10 ||
                (((uVar13 | (ulong)pcVar15) >> (uVar10 & 0x3f)) << (uVar10 & 0x3f) !=
                 (uVar13 | (ulong)pcVar15))) || (uVar16 >> (uVar10 & 0x3f) == 0))) {
          uVar9 = puVar11[uVar9];
          if (uVar9 == 0) {
            PTR_DAT_ffff0000be0ef240 =
                 (undefined *)
                 (((ulong)(PTR_DAT_ffff0000be0ef240 + 0xfff) & 0xfffffffffffff000) + 0x1000);
            FUN_ffff0000be007328(&PTR_DAT_ffff0000be0ef240,8);
            uVar3 = FUN_ffff0000be007328(extraout_x14 - extraout_x15,0x1000);
            puVar11 = extraout_x14_00;
            do {
              puVar7 = puVar11 + 2;
              *puVar11 = 0;
              puVar11[1] = 0;
              puVar11 = puVar7;
            } while (puVar7 < extraout_x9);
            uVar9 = (ulong)extraout_x14_00 | 3;
            *(ulong *)(extraout_x13 + extraout_x11 * 8) = uVar9;
            uVar10 = extraout_x12;
          }
          do {
          } while ((uVar9 & 3) != 3);
          puVar11 = (undefined8 *)(uVar9 & 0xfffffffffffffffc);
          uVar9 = uVar10 & 0x3f;
          uVar10 = uVar10 - 9;
          uVar9 = (uVar13 & (-1L << uVar9 ^ 0xffffffffffffffffU)) >> (uVar10 & 0x3f);
        }
        uVar6 = uVar17 | 3;
        if (uVar10 != 0xc) {
          uVar6 = uVar17 | 1;
        }
        puVar11[uVar9] = uVar6 | (ulong)pcVar15;
        lVar5 = 1L << (uVar10 & 0x3f);
        uVar13 = uVar13 + lVar5;
        pcVar15 = pcVar15 + lVar5;
        uVar16 = uVar16 - lVar5;
      } while (uVar16 != 0);
      puVar14 = (undefined *)(uVar13 ^ 0xffff000000000000);
      if (uVar8 == 0) break;
LAB_ffff0000be0000ac:
      uVar16 = (long)PTR_entry_ffff0000be000310 - (long)puVar14;
      uVar17 = DAT_ffff0000be000308;
      if (PTR_entry_ffff0000be000310 < puVar14 || uVar16 == 0) {
        uVar16 = (long)PTR_DAT_ffff0000be000320 - (long)puVar14;
        uVar17 = DAT_ffff0000be000318 | 0x4000000000000;
        if (PTR_DAT_ffff0000be000320 < puVar14 ||
            (long)PTR_DAT_ffff0000be000320 - (long)puVar14 == 0) {
          uVar16 = (long)PTR_DAT_ffff0000be000328 - (long)puVar14;
          uVar17 = DAT_ffff0000be000318 | 0x24000000000000;
          if (PTR_DAT_ffff0000be000328 < puVar14 ||
              (long)PTR_DAT_ffff0000be000328 - (long)puVar14 == 0) {
            do {
            } while (PTR_DAT_ffff0000be000330 < puVar14);
            do {
              uVar16 = uVar8;
              uVar17 = DAT_ffff0000be000308;
            } while (uVar8 < (ulong)((long)PTR_DAT_ffff0000be000330 - (long)puVar14));
          }
        }
      }
      else if (uVar8 < uVar16) {
        uVar16 = uVar8;
      }
    }
    plVar12 = plVar12 + 5;
  }
  uVar8 = id_aa64mmfr0_el1;
  uVar8 = uVar8 & 0xf;
  do {
  } while (6 < uVar8);
  if (5 < uVar8) {
    uVar8 = 5;
  }
  DAT_ffff0000be0f7000 = DAT_ffff0000be000340 | uVar8 << 0x20;
  TLBI_VMALLE1IS();
  DataSynchronizationBarrier(3,3,0);
  InstructionSynchronizationBarrier();
  mair_el1 = DAT_ffff0000be000348;
  tcr_el1 = DAT_ffff0000be0f7000 | 0x80;
  InstructionSynchronizationBarrier();
  ttbr0_el1 = 0;
  ttbr1_el1 = 0xffff0000be0f6000;
  InstructionSynchronizationBarrier();
  vbar_el1 = PTR_DAT_ffff0000be000350;
  InstructionSynchronizationBarrier();
  uVar8 = sctlr_el1;
  sctlr_el1 = uVar8 | 1;
  InstructionSynchronizationBarrier();
  vbar_el1 = 0;
  InstructionSynchronizationBarrier();
  TLBI_VMALLE1();
  DataSynchronizationBarrier(3,3,0);
  InstructionSynchronizationBarrier();
  puVar11 = (undefined8 *)PTR_DAT_ffff0000be000360;
  for (lVar5 = (long)PTR_DAT_ffff0000be000368 - (long)PTR_DAT_ffff0000be000360; lVar5 != 0;
      lVar5 = lVar5 + -8) {
    *puVar11 = 0;
    puVar11 = puVar11 + 1;
  }
  FUN_ffff0000be008bd8
            (DAT_ffff0000be0d5cc0,DAT_ffff0000be0d5cc8,DAT_ffff0000be0d5cd0,DAT_ffff0000be0d5cd8);
  do {
                    /* WARNING: Do nothing block with infinite loop */
  } while( true );
}


