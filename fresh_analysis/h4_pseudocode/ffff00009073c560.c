
void FUN_ffff00009073c560(undefined8 param_1)

{
  uint uVar1;
  ulong uVar2;
  uint uVar3;
  uint uVar4;
  uint uVar5;
  uint uVar6;
  undefined8 uVar7;
  char *pcVar8;
  long lVar9;
  undefined8 *puVar10;
  long lVar11;
  ulong uVar12;
  ulong uVar13;
  ulong uVar14;
  ulong uVar15;
  
  uVar6 = DAT_ffff00009086e0c4;
  uVar5 = DAT_ffff00009086e0c0;
  uVar4 = DAT_ffff00009086e0bc;
  uVar1 = DAT_ffff00009086e0b8;
  uVar14 = (ulong)DAT_ffff00009086e0bc;
  uVar13 = (ulong)DAT_ffff00009086e0c0;
  uVar2 = (ulong)DAT_ffff00009086e0c4;
  uVar3 = DAT_ffff00009086e0b8 - DAT_ffff00009086e0bc;
  if ((uVar3 == 0) ||
     (DAT_ffff00009086e128 = FUN_ffff00009077d514((ulong)uVar3 << 4), DAT_ffff00009086e128 != 0)) {
    if ((uVar4 == 0) ||
       (DAT_ffff00009086e130 = FUN_ffff00009077d514(uVar14 << 4), DAT_ffff00009086e130 != 0)) {
      if (uVar1 != uVar4) {
        uVar15 = 0;
        uVar14 = uVar13 & 0xfffffffe;
        do {
          uVar7 = FUN_ffff00009077d514(uVar13 << 3);
          *(undefined8 *)(DAT_ffff00009086e128 + uVar15 * 0x10 + 8) = uVar7;
          lVar9 = *(long *)(DAT_ffff00009086e128 + uVar15 * 0x10 + 8);
          if (lVar9 == 0) {
            pcVar8 = s__s__malloc_failed_for_return_sta_ffff0000907d9367;
            goto LAB_ffff00009073c770;
          }
          *(undefined8 *)(DAT_ffff00009086e128 + uVar15 * 0x10) = 0;
          if (uVar5 != 0) {
            uVar12 = 0;
            if (uVar5 != 1) {
              puVar10 = (undefined8 *)(lVar9 + 8);
              uVar12 = uVar14;
              do {
                puVar10[-1] = 0;
                *puVar10 = 0;
                puVar10 = puVar10 + 2;
                uVar12 = uVar12 - 2;
              } while (uVar12 != 0);
              uVar12 = uVar14;
              if (uVar14 == uVar13) goto LAB_ffff00009073c6b8;
            }
            lVar11 = uVar13 - uVar12;
            puVar10 = (undefined8 *)(lVar9 + uVar12 * 8);
            do {
              lVar11 = lVar11 + -1;
              *puVar10 = 0;
              puVar10 = puVar10 + 1;
            } while (lVar11 != 0);
          }
LAB_ffff00009073c6b8:
          uVar1 = (int)uVar15 + 1;
          uVar15 = (ulong)uVar1;
        } while (uVar1 != uVar3);
      }
      if (uVar4 != 0) {
        uVar14 = 0;
        uVar13 = uVar2 & 0xfffffffe;
        do {
          uVar7 = FUN_ffff00009077d514(uVar2 << 3);
          *(undefined8 *)(DAT_ffff00009086e130 + uVar14 * 0x10 + 8) = uVar7;
          lVar9 = *(long *)(DAT_ffff00009086e130 + uVar14 * 0x10 + 8);
          if (lVar9 == 0) {
            pcVar8 = s__s__malloc_failed_for_return_sta_ffff0000907f9e6a;
            goto LAB_ffff00009073c770;
          }
          *(undefined8 *)(DAT_ffff00009086e130 + uVar14 * 0x10) = 0;
          if (uVar6 != 0) {
            uVar15 = 0;
            if (uVar6 != 1) {
              puVar10 = (undefined8 *)(lVar9 + 8);
              uVar15 = uVar13;
              do {
                puVar10[-1] = 0;
                *puVar10 = 0;
                puVar10 = puVar10 + 2;
                uVar15 = uVar15 - 2;
              } while (uVar15 != 0);
              uVar15 = uVar13;
              if (uVar13 == uVar2) goto LAB_ffff00009073c5e8;
            }
            lVar11 = uVar2 - uVar15;
            puVar10 = (undefined8 *)(lVar9 + uVar15 * 8);
            do {
              lVar11 = lVar11 + -1;
              *puVar10 = 0;
              puVar10 = puVar10 + 1;
            } while (lVar11 != 0);
          }
LAB_ffff00009073c5e8:
          uVar3 = (int)uVar14 + 1;
          uVar14 = (ulong)uVar3;
        } while (uVar3 != uVar4);
      }
      if (DAT_ffff00009086e118 == (code *)0x0) {
        return;
      }
                    /* WARNING: Could not recover jumptable at 0xffff00009073c6a0. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
      (*DAT_ffff00009086e118)(param_1);
      return;
    }
    pcVar8 = s__s__malloc_failed_for_big_core_s_ffff0000907cf872;
  }
  else {
    pcVar8 = s__s__malloc_failed_for_little_cor_ffff0000907d2db9;
  }
LAB_ffff00009073c770:
  FUN_ffff00009077e11c(pcVar8,s_dfd_decode_return_stack_ffff0000907ee3c8);
  return;
}


