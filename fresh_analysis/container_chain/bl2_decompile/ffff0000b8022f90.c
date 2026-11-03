
void FUN_ffff0000b8022f90(undefined8 param_1)

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
  
  uVar6 = DAT_ffff0000b811bb54;
  uVar5 = DAT_ffff0000b811bb50;
  uVar4 = DAT_ffff0000b811bb4c;
  uVar1 = DAT_ffff0000b811bb48;
  uVar14 = (ulong)DAT_ffff0000b811bb4c;
  uVar13 = (ulong)DAT_ffff0000b811bb50;
  uVar2 = (ulong)DAT_ffff0000b811bb54;
  uVar3 = DAT_ffff0000b811bb48 - DAT_ffff0000b811bb4c;
  if ((uVar3 == 0) ||
     (DAT_ffff0000b811bbb8 = FUN_ffff0000b804bccc((ulong)uVar3 << 4), DAT_ffff0000b811bbb8 != 0)) {
    if ((uVar4 == 0) ||
       (DAT_ffff0000b811bbc0 = FUN_ffff0000b804bccc(uVar14 << 4), DAT_ffff0000b811bbc0 != 0)) {
      if (uVar1 != uVar4) {
        uVar15 = 0;
        uVar14 = uVar13 & 0xfffffffe;
        do {
          uVar7 = FUN_ffff0000b804bccc(uVar13 << 3);
          *(undefined8 *)(DAT_ffff0000b811bbb8 + uVar15 * 0x10 + 8) = uVar7;
          lVar9 = *(long *)(DAT_ffff0000b811bbb8 + uVar15 * 0x10 + 8);
          if (lVar9 == 0) {
            pcVar8 = s__s__malloc_failed_for_return_sta_ffff0000b808a9c6;
            goto LAB_ffff0000b80231a0;
          }
          *(undefined8 *)(DAT_ffff0000b811bbb8 + uVar15 * 0x10) = 0;
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
              if (uVar14 == uVar13) goto LAB_ffff0000b80230e8;
            }
            lVar11 = uVar13 - uVar12;
            puVar10 = (undefined8 *)(lVar9 + uVar12 * 8);
            do {
              lVar11 = lVar11 + -1;
              *puVar10 = 0;
              puVar10 = puVar10 + 1;
            } while (lVar11 != 0);
          }
LAB_ffff0000b80230e8:
          uVar1 = (int)uVar15 + 1;
          uVar15 = (ulong)uVar1;
        } while (uVar1 != uVar3);
      }
      if (uVar4 != 0) {
        uVar14 = 0;
        uVar13 = uVar2 & 0xfffffffe;
        do {
          uVar7 = FUN_ffff0000b804bccc(uVar2 << 3);
          *(undefined8 *)(DAT_ffff0000b811bbc0 + uVar14 * 0x10 + 8) = uVar7;
          lVar9 = *(long *)(DAT_ffff0000b811bbc0 + uVar14 * 0x10 + 8);
          if (lVar9 == 0) {
            pcVar8 = s__s__malloc_failed_for_return_sta_ffff0000b809c7a8;
            goto LAB_ffff0000b80231a0;
          }
          *(undefined8 *)(DAT_ffff0000b811bbc0 + uVar14 * 0x10) = 0;
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
              if (uVar13 == uVar2) goto LAB_ffff0000b8023018;
            }
            lVar11 = uVar2 - uVar15;
            puVar10 = (undefined8 *)(lVar9 + uVar15 * 8);
            do {
              lVar11 = lVar11 + -1;
              *puVar10 = 0;
              puVar10 = puVar10 + 1;
            } while (lVar11 != 0);
          }
LAB_ffff0000b8023018:
          uVar3 = (int)uVar14 + 1;
          uVar14 = (ulong)uVar3;
        } while (uVar3 != uVar4);
      }
      if (DAT_ffff0000b811bba8 == (code *)0x0) {
        return;
      }
                    /* WARNING: Could not recover jumptable at 0xffff0000b80230d0. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
      (*DAT_ffff0000b811bba8)(param_1);
      return;
    }
    pcVar8 = s__s__malloc_failed_for_big_core_s_ffff0000b80853af;
  }
  else {
    pcVar8 = s__s__malloc_failed_for_little_cor_ffff0000b808728e;
  }
LAB_ffff0000b80231a0:
  FUN_ffff0000b804c33c(pcVar8,s_dfd_decode_return_stack_ffff0000b80964ca);
  return;
}


