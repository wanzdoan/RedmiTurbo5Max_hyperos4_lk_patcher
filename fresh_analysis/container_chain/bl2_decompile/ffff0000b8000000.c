
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
  long extraout_x13;
  undefined8 *puVar11;
  long extraout_x14;
  undefined8 *extraout_x14_00;
  long extraout_x15;
  code *pcVar12;
  ulong uVar13;
  undefined *puVar14;
  code *pcVar15;
  ulong uVar16;
  ulong uVar17;
  undefined1 auVar18 [16];
  
  FUN_ffff0000b800e424();
  uVar1 = currentel;
  FUN_ffff0000b800e428();
  auVar18 = FUN_ffff0000b8007418();
  uVar3 = auVar18._0_8_;
  cpacr_el1 = 0x300000;
  uVar8 = sctlr_el1;
  sctlr_el1 = uVar8 & 0xfffffffffffffffd | 0x101c;
  lVar5 = 0;
  DAT_ffff0000b80adc80 = param_3;
  DAT_ffff0000b80adc88 = param_4;
  DAT_ffff0000b80adc90 = uVar1;
  do {
    DAT_ffff0000b80adc78 = auVar18._8_8_;
    DAT_ffff0000b80adc70 = auVar18._0_8_;
    (&DAT_ffff0000b8109000)[lVar5] = 0;
    auVar18._8_8_ = DAT_ffff0000b80adc78;
    auVar18._0_8_ = DAT_ffff0000b80adc70;
    lVar5 = lVar5 + 1;
  } while (lVar5 != 0x200);
  pcVar12 = FUN_ffff0000b80ad000;
  while( true ) {
    pcVar15 = *(code **)pcVar12;
    puVar14 = *(undefined **)(pcVar12 + 8);
    uVar4 = (uint)*(long *)(pcVar12 + 0x18);
    uVar8 = *(ulong *)(pcVar12 + 0x10);
    if ((uVar4 >> 3 & 1) != 0) {
      pcVar15 = entry;
      *(code **)pcVar12 = entry;
      *(ulong *)(pcVar12 + 0x10) = uVar3;
      uVar8 = uVar3;
    }
    if (uVar8 == 0) break;
    uVar16 = uVar8;
    uVar17 = DAT_ffff0000b80002f8;
    if (((uVar4 >> 1 & 1) == 0) && (uVar17 = DAT_ffff0000b8000300, (uVar4 >> 2 & 1) == 0))
    goto LAB_ffff0000b80000ac;
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
        uVar10 = 0x27;
        uVar9 = uVar13 >> 0x27;
        puVar11 = &DAT_ffff0000b8109000;
        while (((0x1e < uVar10 ||
                (((uVar13 | (ulong)pcVar15) >> (uVar10 & 0x3f)) << (uVar10 & 0x3f) !=
                 (uVar13 | (ulong)pcVar15))) || (uVar16 >> (uVar10 & 0x3f) == 0))) {
          uVar9 = puVar11[uVar9];
          if (uVar9 == 0) {
            PTR_DAT_ffff0000b8102020 =
                 (undefined *)
                 (((ulong)(PTR_DAT_ffff0000b8102020 + 0xfff) & 0xfffffffffffff000) + 0x1000);
            FUN_ffff0000b80075ac(&PTR_DAT_ffff0000b8102020,8);
            uVar3 = FUN_ffff0000b80075ac(extraout_x14 - extraout_x15,0x1000);
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
LAB_ffff0000b80000ac:
      uVar16 = (long)PTR_entry_ffff0000b8000310 - (long)puVar14;
      uVar17 = DAT_ffff0000b8000308;
      if (PTR_entry_ffff0000b8000310 < puVar14 || uVar16 == 0) {
        uVar16 = (long)PTR_s_mt6379_charger_probe_ffff0000b8000320 - (long)puVar14;
        uVar17 = DAT_ffff0000b8000318 | 0x4000000000000;
        if (PTR_s_mt6379_charger_probe_ffff0000b8000320 < puVar14 ||
            (long)PTR_s_mt6379_charger_probe_ffff0000b8000320 - (long)puVar14 == 0) {
          uVar16 = (long)PTR_FUN_ffff0000b8000328 - (long)puVar14;
          uVar17 = DAT_ffff0000b8000318 | 0x24000000000000;
          if (PTR_FUN_ffff0000b8000328 < puVar14 ||
              (long)PTR_FUN_ffff0000b8000328 - (long)puVar14 == 0) {
            do {
            } while (PTR_DAT_ffff0000b8000330 < puVar14);
            do {
              uVar16 = uVar8;
              uVar17 = DAT_ffff0000b8000308;
            } while (uVar8 < (ulong)((long)PTR_DAT_ffff0000b8000330 - (long)puVar14));
          }
        }
      }
      else if (uVar8 < uVar16) {
        uVar16 = uVar8;
      }
    }
    pcVar12 = pcVar12 + 0x28;
  }
  uVar8 = id_aa64mmfr0_el1;
  uVar8 = uVar8 & 0xf;
  do {
  } while (6 < uVar8);
  if (5 < uVar8) {
    uVar8 = 5;
  }
  DAT_ffff0000b810a000 = DAT_ffff0000b8000340 | uVar8 << 0x20;
  TLBI_VMALLE1IS();
  DataSynchronizationBarrier(3,3,0);
  InstructionSynchronizationBarrier();
  mair_el1 = DAT_ffff0000b8000348;
  tcr_el1 = DAT_ffff0000b810a000 | 0x80;
  InstructionSynchronizationBarrier();
  ttbr0_el1 = 0;
  ttbr1_el1 = 0xffff0000b8109000;
  InstructionSynchronizationBarrier();
  vbar_el1 = PTR_DAT_ffff0000b8000350;
  InstructionSynchronizationBarrier();
  uVar8 = sctlr_el1;
  sctlr_el1 = uVar8 | 1;
  InstructionSynchronizationBarrier();
  vbar_el1 = 0;
  InstructionSynchronizationBarrier();
  TLBI_VMALLE1();
  DataSynchronizationBarrier(3,3,0);
  InstructionSynchronizationBarrier();
  puVar11 = (undefined8 *)PTR_DAT_ffff0000b8000360;
  for (lVar5 = (long)PTR_DAT_ffff0000b8000368 - (long)PTR_DAT_ffff0000b8000360; lVar5 != 0;
      lVar5 = lVar5 + -8) {
    *puVar11 = 0;
    puVar11 = puVar11 + 1;
  }
  FUN_ffff0000b8008e68
            (DAT_ffff0000b80adc70,DAT_ffff0000b80adc78,DAT_ffff0000b80adc80,DAT_ffff0000b80adc88);
  do {
                    /* WARNING: Do nothing block with infinite loop */
  } while( true );
}


