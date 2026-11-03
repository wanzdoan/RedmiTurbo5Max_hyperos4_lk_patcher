
void FUN_ffff00009073d1ac(void)

{
  undefined4 uVar1;
  uint uVar2;
  int iVar3;
  int iVar4;
  int iVar5;
  uint *puVar6;
  uint uVar7;
  ulong uVar8;
  uint uVar9;
  long lVar10;
  undefined4 *puVar11;
  ulong uVar12;
  ulong uVar13;
  undefined4 *puVar14;
  
  iVar4 = FUN_ffff00009073d364();
  iVar3 = DAT_ffff00009086e180;
  if (((-1 < iVar4) && (DAT_ffff00009086e17c != 0)) && (DAT_ffff00009086e178 == 1)) {
    uVar12 = (ulong)DAT_ffff00009086e188;
    if (DAT_ffff00009086e188 != 0) {
      uVar13 = 0;
      puVar14 = &DAT_ffff00009086e1d4;
      iVar4 = DAT_ffff00009086e180 * 1000;
      uVar2 = DAT_ffff00009086e184 << 1;
      do {
        iVar5 = FUN_ffff00009077f0ac
                          (&DAT_ffff00009086e190 + uVar13 * 0x80,
                           s_debug_ctrl_ao_MMUP_AO_ffff0000907e4bcd);
        if (iVar5 != 0) {
          if ((&DAT_ffff00009086e1c8)[uVar13 * 0x20] == 1) {
            if ((&DAT_ffff00009086e1cc)[uVar13 * 0x20] != 0) {
              uVar8 = 0;
              lVar10 = (&DAT_ffff00009086e1b8)[uVar13 * 0x10];
              puVar11 = puVar14;
              do {
                puVar6 = puVar11 + -1;
                uVar1 = *puVar11;
                uVar8 = uVar8 + 1;
                puVar11 = puVar11 + 2;
                *(undefined4 *)(lVar10 + (ulong)*puVar6) = uVar1;
              } while (uVar8 < (uint)(&DAT_ffff00009086e1cc)[uVar13 * 0x20]);
            }
          }
          puVar6 = (uint *)(&DAT_ffff00009086e1b8)[uVar13 * 0x10];
          *puVar6 = 0x208;
          *puVar6 = 8;
          uVar9 = uVar2 | 0xffff0008;
          uVar7 = uVar2 | 0xffff000c;
          if (iVar3 != -1) {
            uVar9 = ((uint)(iVar4 * (&DAT_ffff00009086e1c4)[uVar13 * 0x20]) >> 10) - 1;
            if (0xfffe < uVar9) {
              uVar9 = 0xffff;
            }
            uVar7 = uVar2 | uVar9 << 0x10;
            uVar9 = uVar7 | 8;
            uVar7 = uVar7 | 0xc;
          }
          *puVar6 = uVar9;
          *puVar6 = uVar7;
        }
        uVar13 = uVar13 + 1;
        puVar14 = puVar14 + 0x20;
      } while (uVar13 != uVar12);
    }
    FUN_ffff00009077e11c
              (s__s__Lastbus_kernel_driver_will_d_ffff0000907efc04,s_lastbus_init_ffff0000907cf8d1);
    return;
  }
  return;
}


