// File offset entry: 0x84454

undefined1  [16] FUN_02084354(void)

{
  long lVar1;
  bool bVar2;
  char cVar3;
  undefined8 uVar4;
  ulong uVar5;
  ulong uVar6;
  long lVar7;
  uint uVar8;
  uint uVar9;
  ulong uVar10;
  ulong uVar11;
  int iVar12;
  undefined1 auVar13 [16];
  
  uVar4 = func_0x0208ad38();
  if ((int)uVar4 == 0) {
    func_0x02092484(uVar4,0xe1000);
    func_0x0208ac18(1);
  }
  FUN_0208ada0(0x20c433f);
  func_0x0208cc4c();
  FUN_0208ada0(0x20b1ce1);
  iVar12 = -3;
  do {
    uVar5 = func_0x020667f8(8);
    if ((uVar5 & 1) == 0) break;
    func_0x02099e7c();
    func_0x02091d50(5000);
    iVar12 = iVar12 + 1;
  } while (iVar12 != 0);
  cVar3 = func_0x0209a3f4();
  if (cVar3 == '\0') {
    func_0x02099c44(10);
  }
  uVar4 = func_0x0208137c(5000);
  FUN_0208ada0(0x20bcea9,uVar4);
  FUN_0208440c();
  auVar13 = FUN_02084354();
  if (cRam000000000211cf38 == '\0') {
    return auVar13;
  }
  DataSynchronizationBarrier(3,3,0);
  uVar5 = clidr_el1;
  uVar6 = uVar5 >> 0x18 & 7;
  lVar7 = uVar6 * 2;
  if (uVar6 != 0) {
    uVar6 = 0;
    do {
      if (1 < (uVar5 >> (uVar6 + (uVar6 >> 1) & 0x3f) & 7)) {
        csselr_el1 = uVar6;
        InstructionSynchronizationBarrier();
        uVar10 = ccsidr_el1;
        lVar1 = (uVar10 & 7) + 4;
        uVar11 = UnkSytemRegRead(3,0,0,7,2);
        bVar2 = (uVar11 >> 0x14 & 0xf) != 1;
        uVar9 = (uint)(uVar10 >> 3) & 0x1fffff;
        if (bVar2) {
          uVar9 = (uint)(uVar10 >> 3) & 0x3ff;
        }
        uVar11 = (ulong)(uint)(1 << (ulong)((uint)LZCOUNT(uVar9) & 0x1f));
        uVar8 = (uint)(uVar10 >> 0x20) & 0xffffff;
        if (bVar2) {
          uVar8 = (uint)uVar10 >> 0xd & 0x7fff;
        }
        iVar12 = 1 << lVar1;
        DataSynchronizationBarrier(3,3,0);
        uVar10 = (ulong)((uint)uVar6 | uVar9 << (ulong)((uint)LZCOUNT(uVar9) & 0x1f));
        do {
          uVar9 = uVar8 << lVar1;
          do {
            DC_CISW((ulong)((uint)uVar10 | uVar9));
            bVar2 = iVar12 <= (int)uVar9;
            uVar9 = uVar9 - iVar12;
          } while (bVar2);
          bVar2 = (long)uVar11 <= (long)uVar10;
          uVar10 = uVar10 - uVar11;
        } while (bVar2);
      }
      uVar6 = uVar6 + 2;
    } while (lVar7 - uVar6 != 0 && (long)uVar6 <= lVar7);
    csselr_el1 = 0;
    DataSynchronizationBarrier(3,3,0);
    InstructionSynchronizationBarrier();
  }
  return auVar13;
}


