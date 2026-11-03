// ffff00009073d1ac  size=440  FUN_ffff00009073d1ac

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009073d1ac(void)

{
  uint *puVar1;
  undefined4 uVar2;
  uint uVar3;
  int iVar4;
  int iVar5;
  int iVar6;
  uint *puVar7;
  uint uVar8;
  ulong uVar9;
  uint uVar10;
  long lVar11;
  undefined4 *puVar12;
  ulong uVar13;
  ulong uVar14;
  undefined4 *puVar15;
  
  iVar5 = FUN_ffff00009073d364();
  iVar4 = _DAT_ffff00009086e180;
  if (((-1 < iVar5) && (_DAT_ffff00009086e17c != 0)) && (_DAT_ffff00009086e178 == 1)) {
    uVar13 = (ulong)_DAT_ffff00009086e188;
    if (_DAT_ffff00009086e188 != 0) {
      uVar14 = 0;
      puVar15 = (undefined4 *)&DAT_ffff00009086e1d4;
      iVar5 = _DAT_ffff00009086e180 * 1000;
      uVar3 = _DAT_ffff00009086e184 << 1;
      do {
        iVar6 = FUN_ffff00009077f0ac
                          (uVar14 * 0x80 + -0xffff6f791e70,s_debug_ctrl_ao_MMUP_AO_ffff0000907e4bcd)
        ;
        if (iVar6 != 0) {
          if (*(int *)(uVar14 * 0x80 + -0xffff6f791e38) == 1) {
            puVar7 = (uint *)(uVar14 * 0x80 + -0xffff6f791e34);
            if (*puVar7 != 0) {
              uVar9 = 0;
              lVar11 = *(long *)(uVar14 * 0x80 + -0xffff6f791e48);
              puVar12 = puVar15;
              do {
                puVar1 = puVar12 + -1;
                uVar2 = *puVar12;
                uVar9 = uVar9 + 1;
                puVar12 = puVar12 + 2;
                *(undefined4 *)(lVar11 + (ulong)*puVar1) = uVar2;
              } while (uVar9 < *puVar7);
            }
          }
          puVar7 = *(uint **)(uVar14 * 0x80 + -0xffff6f791e48);
          *puVar7 = 0x208;
          *puVar7 = 8;
          uVar10 = uVar3 | 0xffff0008;
          uVar8 = uVar3 | 0xffff000c;
          if (iVar4 != -1) {
            uVar10 = ((uint)(iVar5 * *(int *)(uVar14 * 0x80 + -0xffff6f791e3c)) >> 10) - 1;
            if (0xfffe < uVar10) {
              uVar10 = 0xffff;
            }
            uVar8 = uVar3 | uVar10 << 0x10;
            uVar10 = uVar8 | 8;
            uVar8 = uVar8 | 0xc;
          }
          *puVar7 = uVar10;
          *puVar7 = uVar8;
        }
        uVar14 = uVar14 + 1;
        puVar15 = puVar15 + 0x20;
      } while (uVar14 != uVar13);
    }
    FUN_ffff00009077e11c
              (s__s__Lastbus_kernel_driver_will_d_ffff0000907efc04,s_lastbus_init_ffff0000907cf8d1);
    return;
  }
  return;
}


