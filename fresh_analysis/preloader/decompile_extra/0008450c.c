// File offset entry: 0x8450c

undefined1  [16] FUN_0208440c(undefined8 param_1,undefined8 param_2)

{
  long lVar1;
  int iVar2;
  ulong uVar3;
  undefined1 auVar4 [16];
  bool bVar5;
  ulong uVar6;
  long lVar7;
  uint uVar8;
  uint uVar9;
  ulong uVar10;
  ulong uVar11;
  undefined1 auVar12 [16];
  
  if (cRam000000000211cf38 != '\0') {
    DataSynchronizationBarrier(3,3,0);
    uVar3 = clidr_el1;
    uVar6 = uVar3 >> 0x18 & 7;
    lVar7 = uVar6 * 2;
    if (uVar6 != 0) {
      uVar6 = 0;
      do {
        if (1 < (uVar3 >> (uVar6 + (uVar6 >> 1) & 0x3f) & 7)) {
          csselr_el1 = uVar6;
          InstructionSynchronizationBarrier();
          uVar10 = ccsidr_el1;
          lVar1 = (uVar10 & 7) + 4;
          uVar11 = UnkSytemRegRead(3,0,0,7,2);
          bVar5 = (uVar11 >> 0x14 & 0xf) != 1;
          uVar9 = (uint)(uVar10 >> 3) & 0x1fffff;
          if (bVar5) {
            uVar9 = (uint)(uVar10 >> 3) & 0x3ff;
          }
          uVar11 = (ulong)(uint)(1 << (ulong)((uint)LZCOUNT(uVar9) & 0x1f));
          uVar8 = (uint)(uVar10 >> 0x20) & 0xffffff;
          if (bVar5) {
            uVar8 = (uint)uVar10 >> 0xd & 0x7fff;
          }
          iVar2 = 1 << lVar1;
          DataSynchronizationBarrier(3,3,0);
          uVar10 = (ulong)((uint)uVar6 | uVar9 << (ulong)((uint)LZCOUNT(uVar9) & 0x1f));
          do {
            uVar9 = uVar8 << lVar1;
            do {
              DC_CISW((ulong)((uint)uVar10 | uVar9));
              bVar5 = iVar2 <= (int)uVar9;
              uVar9 = uVar9 - iVar2;
            } while (bVar5);
            bVar5 = (long)uVar11 <= (long)uVar10;
            uVar10 = uVar10 - uVar11;
          } while (bVar5);
        }
        uVar6 = uVar6 + 2;
      } while (lVar7 - uVar6 != 0 && (long)uVar6 <= lVar7);
      csselr_el1 = 0;
      DataSynchronizationBarrier(3,3,0);
      InstructionSynchronizationBarrier();
    }
    auVar4._8_8_ = param_2;
    auVar4._0_8_ = param_1;
    return auVar4;
  }
  auVar12._8_8_ = param_2;
  auVar12._0_8_ = param_1;
  return auVar12;
}


