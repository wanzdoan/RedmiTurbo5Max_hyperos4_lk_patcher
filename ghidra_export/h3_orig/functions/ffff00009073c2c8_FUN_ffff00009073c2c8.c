// ffff00009073c2c8  size=600  FUN_ffff00009073c2c8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009073c2c8(undefined8 param_1)

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
  
  uVar6 = _DAT_ffff00009086e02c;
  uVar5 = _DAT_ffff00009086e028;
  uVar4 = _DAT_ffff00009086e024;
  uVar1 = _DAT_ffff00009086e020;
  uVar14 = (ulong)_DAT_ffff00009086e024;
  uVar13 = (ulong)_DAT_ffff00009086e028;
  uVar2 = (ulong)_DAT_ffff00009086e02c;
  uVar3 = _DAT_ffff00009086e020 - _DAT_ffff00009086e024;
  if ((uVar3 == 0) ||
     (_DAT_ffff00009086e090 = FUN_ffff00009077d24c((ulong)uVar3 << 4), _DAT_ffff00009086e090 != 0))
  {
    if ((uVar4 == 0) ||
       (_DAT_ffff00009086e098 = FUN_ffff00009077d24c(uVar14 << 4), _DAT_ffff00009086e098 != 0)) {
      if (uVar1 != uVar4) {
        uVar15 = 0;
        uVar14 = uVar13 & 0xfffffffe;
        do {
          uVar7 = FUN_ffff00009077d24c(uVar13 << 3);
          *(undefined8 *)(_DAT_ffff00009086e090 + uVar15 * 0x10 + 8) = uVar7;
          lVar9 = *(long *)(_DAT_ffff00009086e090 + uVar15 * 0x10 + 8);
          if (lVar9 == 0) {
            pcVar8 = s__s__malloc_failed_for_return_sta_ffff0000907d92d0;
            goto LAB_ffff00009073c4d8;
          }
          *(undefined8 *)(_DAT_ffff00009086e090 + uVar15 * 0x10) = 0;
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
              if (uVar14 == uVar13) goto LAB_ffff00009073c420;
            }
            lVar11 = uVar13 - uVar12;
            puVar10 = (undefined8 *)(lVar9 + uVar12 * 8);
            do {
              lVar11 = lVar11 + -1;
              *puVar10 = 0;
              puVar10 = puVar10 + 1;
            } while (lVar11 != 0);
          }
LAB_ffff00009073c420:
          uVar1 = (int)uVar15 + 1;
          uVar15 = (ulong)uVar1;
        } while (uVar1 != uVar3);
      }
      if (uVar4 != 0) {
        uVar14 = 0;
        uVar13 = uVar2 & 0xfffffffe;
        do {
          uVar7 = FUN_ffff00009077d24c(uVar2 << 3);
          *(undefined8 *)(_DAT_ffff00009086e098 + uVar14 * 0x10 + 8) = uVar7;
          lVar9 = *(long *)(_DAT_ffff00009086e098 + uVar14 * 0x10 + 8);
          if (lVar9 == 0) {
            pcVar8 = s__s__malloc_failed_for_return_sta_ffff0000907f9cce;
            goto LAB_ffff00009073c4d8;
          }
          *(undefined8 *)(_DAT_ffff00009086e098 + uVar14 * 0x10) = 0;
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
              if (uVar13 == uVar2) goto LAB_ffff00009073c350;
            }
            lVar11 = uVar2 - uVar15;
            puVar10 = (undefined8 *)(lVar9 + uVar15 * 8);
            do {
              lVar11 = lVar11 + -1;
              *puVar10 = 0;
              puVar10 = puVar10 + 1;
            } while (lVar11 != 0);
          }
LAB_ffff00009073c350:
          uVar3 = (int)uVar14 + 1;
          uVar14 = (ulong)uVar3;
        } while (uVar3 != uVar4);
      }
      if (_DAT_ffff00009086e080 == (code *)0x0) {
        return;
      }
                    /* WARNING: Could not recover jumptable at 0xffff00009073c408. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
      (*_DAT_ffff00009086e080)(param_1);
      return;
    }
    pcVar8 = s__s__malloc_failed_for_big_core_s_ffff0000907cf853;
  }
  else {
    pcVar8 = s__s__malloc_failed_for_little_cor_ffff0000907d2d7c;
  }
LAB_ffff00009073c4d8:
  FUN_ffff00009077de54(pcVar8,s_dfd_decode_return_stack_ffff0000907ee257);
  return;
}


