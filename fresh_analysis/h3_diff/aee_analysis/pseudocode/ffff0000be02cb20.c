
void FUN_ffff0000be02cb20(undefined8 param_1)

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
  
  uVar6 = DAT_ffff0000be11838c;
  uVar5 = DAT_ffff0000be118388;
  uVar4 = DAT_ffff0000be118384;
  uVar1 = DAT_ffff0000be118380;
  uVar14 = (ulong)DAT_ffff0000be118384;
  uVar13 = (ulong)DAT_ffff0000be118388;
  uVar2 = (ulong)DAT_ffff0000be11838c;
  uVar3 = DAT_ffff0000be118380 - DAT_ffff0000be118384;
  if ((uVar3 == 0) ||
     (DAT_ffff0000be1183f0 = FUN_ffff0000be05f1bc((ulong)uVar3 << 4), DAT_ffff0000be1183f0 != 0)) {
    if ((uVar4 == 0) ||
       (DAT_ffff0000be1183f8 = FUN_ffff0000be05f1bc(uVar14 << 4), DAT_ffff0000be1183f8 != 0)) {
      if (uVar1 != uVar4) {
        uVar15 = 0;
        uVar14 = uVar13 & 0xfffffffe;
        do {
          uVar7 = FUN_ffff0000be05f1bc(uVar13 << 3);
          *(undefined8 *)(DAT_ffff0000be1183f0 + uVar15 * 0x10 + 8) = uVar7;
          lVar9 = *(long *)(DAT_ffff0000be1183f0 + uVar15 * 0x10 + 8);
          if (lVar9 == 0) {
            pcVar8 = s__s__malloc_failed_for_return_sta_ffff0000be0aa3da;
            goto LAB_ffff0000be02cd30;
          }
          *(undefined8 *)(DAT_ffff0000be1183f0 + uVar15 * 0x10) = 0;
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
              if (uVar14 == uVar13) goto LAB_ffff0000be02cc78;
            }
            lVar11 = uVar13 - uVar12;
            puVar10 = (undefined8 *)(lVar9 + uVar12 * 8);
            do {
              lVar11 = lVar11 + -1;
              *puVar10 = 0;
              puVar10 = puVar10 + 1;
            } while (lVar11 != 0);
          }
LAB_ffff0000be02cc78:
          uVar1 = (int)uVar15 + 1;
          uVar15 = (ulong)uVar1;
        } while (uVar1 != uVar3);
      }
      if (uVar4 != 0) {
        uVar14 = 0;
        uVar13 = uVar2 & 0xfffffffe;
        do {
          uVar7 = FUN_ffff0000be05f1bc(uVar2 << 3);
          *(undefined8 *)(DAT_ffff0000be1183f8 + uVar14 * 0x10 + 8) = uVar7;
          lVar9 = *(long *)(DAT_ffff0000be1183f8 + uVar14 * 0x10 + 8);
          if (lVar9 == 0) {
            pcVar8 = s__s__malloc_failed_for_return_sta_ffff0000be0c0a3c;
            goto LAB_ffff0000be02cd30;
          }
          *(undefined8 *)(DAT_ffff0000be1183f8 + uVar14 * 0x10) = 0;
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
              if (uVar13 == uVar2) goto LAB_ffff0000be02cba8;
            }
            lVar11 = uVar2 - uVar15;
            puVar10 = (undefined8 *)(lVar9 + uVar15 * 8);
            do {
              lVar11 = lVar11 + -1;
              *puVar10 = 0;
              puVar10 = puVar10 + 1;
            } while (lVar11 != 0);
          }
LAB_ffff0000be02cba8:
          uVar3 = (int)uVar14 + 1;
          uVar14 = (ulong)uVar3;
        } while (uVar3 != uVar4);
      }
      if (DAT_ffff0000be1183e0 == (code *)0x0) {
        return;
      }
                    /* WARNING: Could not recover jumptable at 0xffff0000be02cc60. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
      (*DAT_ffff0000be1183e0)(param_1);
      return;
    }
    pcVar8 = s__s__malloc_failed_for_big_core_s_ffff0000be0a34ac;
  }
  else {
    pcVar8 = s__s__malloc_failed_for_little_cor_ffff0000be0a5bc4;
  }
LAB_ffff0000be02cd30:
  FUN_ffff0000be05fd98(pcVar8,s_dfd_decode_return_stack_ffff0000be0b8b1d);
  return;
}


