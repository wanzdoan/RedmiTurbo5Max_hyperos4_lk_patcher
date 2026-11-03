// ffff000090751c04  size=1040  FUN_ffff000090751c04

undefined8 FUN_ffff000090751c04(int param_1)

{
  long *plVar1;
  ulong uVar2;
  int iVar3;
  ulong uVar4;
  ulong uVar5;
  uint uVar6;
  long lVar8;
  long *plVar9;
  ulong uVar10;
  ulong uVar11;
  long lVar12;
  long lVar13;
  ulong uVar7;
  
  if ((DAT_ffff000090879374 & 1) == 0) {
    if (param_1 - 0x2bU < 2) {
      FUN_ffff0000907548b0(param_1);
    }
    DAT_ffff000090879374 = 1;
  }
  uVar6 = 0;
  if (param_1 != 0x2b) {
    uVar6 = (uint)(param_1 != 0x2d);
  }
  uVar7 = (ulong)uVar6;
  uVar2 = 1;
  if (param_1 != 0x2b) {
    uVar2 = 2;
  }
  lVar12 = uVar2 - uVar7;
  plVar1 = &DAT_ffff00009080cd50 + uVar7;
  uVar10 = uVar7;
  do {
    iVar3 = *(int *)(&DAT_ffff0000908781d8 + uVar10 * 0xbd8);
    FUN_ffff00009074cb5c(param_1);
    uVar4 = uVar7;
    if (iVar3 == 0) {
      do {
        uVar5 = uVar4 + 1;
        *(uint *)(*(long *)(s_0123456789abcdef_ffff000090804a48 + uVar4 * 8 + 0x10) + 0x1d0) =
             *(uint *)(*(long *)(s_0123456789abcdef_ffff000090804a48 + uVar4 * 8 + 0x10) + 0x1d0) &
             0xfffffffe;
        uVar4 = uVar5;
        uVar11 = uVar7;
      } while (uVar2 != uVar5);
      do {
        lVar13 = *(long *)(s_0123456789abcdef_ffff000090804a48 + uVar11 * 8 + 0x10);
        *(uint *)(lVar13 + 0x1d4) = *(uint *)(lVar13 + 0x1d4) & 0xfffffffe;
        FUN_ffff00009077cbd8(1000);
        uVar11 = uVar11 + 1;
        *(uint *)(lVar13 + 0x1d4) = *(uint *)(lVar13 + 0x1d4) & 0xfffffffd;
        *(uint *)(lVar13 + 0x1d4) = *(uint *)(lVar13 + 0x1d4) | 2;
        uVar4 = uVar7;
      } while (uVar2 != uVar11);
      do {
        lVar13 = *(long *)(s_0123456789abcdef_ffff000090804a48 + uVar4 * 8 + 0x10);
        *(uint *)(lVar13 + 0x1d0) = *(uint *)(lVar13 + 0x1d0) & 0xfffffffd;
        *(uint *)(lVar13 + 0x1d0) = *(uint *)(lVar13 + 0x1d0) | 2;
        FUN_ffff00009077cbd8(1000);
        uVar4 = uVar4 + 1;
        uVar5 = uVar7;
      } while (uVar2 != uVar4);
      do {
        plVar9 = plVar1;
        lVar13 = lVar12;
        if (*(int *)(&DAT_ffff000090878738 + uVar5 * 0xbd8) == 0) {
          do {
            lVar8 = *plVar9;
            lVar13 = lVar13 + -1;
            *(uint *)(lVar8 + 0x30) = *(uint *)(lVar8 + 0x30) & 0xfffffffe;
            *(uint *)(lVar8 + 0x28) = *(uint *)(lVar8 + 0x28) | 2;
            *(uint *)(lVar8 + 0x28) = *(uint *)(lVar8 + 0x28) & 0xfffffffe;
            *(uint *)(lVar8 + 0x25c) = *(uint *)(lVar8 + 0x25c) | 1;
            *(uint *)(lVar8 + 0x45c) = *(uint *)(lVar8 + 0x45c) | 1;
            *(uint *)(lVar8 + 0x15c) = *(uint *)(lVar8 + 0x15c) | 1;
            *(uint *)(lVar8 + 0x55c) = *(uint *)(lVar8 + 0x55c) | 1;
            *(uint *)(lVar8 + 0x35c) = *(uint *)(lVar8 + 0x35c) | 1;
            *(undefined4 *)(lVar8 + 4) = 0x3fff0180;
            *(undefined4 *)(lVar8 + 4) = 0x3fff0100;
            plVar9 = plVar9 + 1;
          } while (lVar13 != 0);
        }
        else {
          do {
            lVar8 = *plVar9;
            lVar13 = lVar13 + -1;
            *(uint *)(lVar8 + 0x30) = *(uint *)(lVar8 + 0x30) & 0xfffffffe;
            *(uint *)(lVar8 + 0x28) = *(uint *)(lVar8 + 0x28) | 2;
            *(uint *)(lVar8 + 0x28) = *(uint *)(lVar8 + 0x28) & 0xfffffffe;
            *(undefined4 *)(lVar8 + 4) = 0x3fff0080;
            *(undefined4 *)(lVar8 + 4) = 0x3fff0000;
            plVar9 = plVar9 + 1;
          } while (lVar13 != 0);
        }
        uVar5 = uVar5 + 1;
      } while (uVar5 != uVar2);
    }
    else {
      do {
        lVar13 = uVar4 * 8;
        uVar4 = uVar4 + 1;
        *(uint *)(*(long *)(s_0123456789abcdef_ffff000090804a48 + lVar13 + 0x10) + 0x1d0) =
             *(uint *)(*(long *)(s_0123456789abcdef_ffff000090804a48 + lVar13 + 0x10) + 0x1d0) &
             0xfffffffe;
        uVar5 = uVar7;
      } while (uVar2 != uVar4);
      do {
        lVar13 = *(long *)(s_0123456789abcdef_ffff000090804a48 + uVar5 * 8 + 0x10);
        *(uint *)(lVar13 + 0x1d4) = *(uint *)(lVar13 + 0x1d4) & 0xfffffffe;
        FUN_ffff00009077cbd8(1000);
        uVar5 = uVar5 + 1;
        *(uint *)(lVar13 + 0x1d4) = *(uint *)(lVar13 + 0x1d4) & 0xfffffffd;
        *(uint *)(lVar13 + 0x1d4) = *(uint *)(lVar13 + 0x1d4) | 2;
        uVar4 = uVar7;
      } while (uVar2 != uVar5);
      do {
        lVar13 = *(long *)(s_0123456789abcdef_ffff000090804a48 + uVar4 * 8 + 0x10);
        *(uint *)(lVar13 + 0x1d0) = *(uint *)(lVar13 + 0x1d0) & 0xfffffffd;
        *(uint *)(lVar13 + 0x1d0) = *(uint *)(lVar13 + 0x1d0) | 2;
        FUN_ffff00009077cbd8(1000);
        uVar4 = uVar4 + 1;
        uVar5 = uVar7;
      } while (uVar2 != uVar4);
      do {
        plVar9 = plVar1;
        lVar13 = lVar12;
        if (*(int *)(&DAT_ffff000090878738 + uVar5 * 0xbd8) == 0) {
          do {
            lVar8 = *plVar9;
            lVar13 = lVar13 + -1;
            *(uint *)(lVar8 + 0x30) = *(uint *)(lVar8 + 0x30) & 0xfffffffe;
            *(uint *)(lVar8 + 0x28) = *(uint *)(lVar8 + 0x28) | 2;
            *(uint *)(lVar8 + 0x28) = *(uint *)(lVar8 + 0x28) & 0xfffffffe;
            *(uint *)(lVar8 + 0x25c) = *(uint *)(lVar8 + 0x25c) | 1;
            *(uint *)(lVar8 + 0x45c) = *(uint *)(lVar8 + 0x45c) | 1;
            *(uint *)(lVar8 + 0x15c) = *(uint *)(lVar8 + 0x15c) | 1;
            *(uint *)(lVar8 + 0x55c) = *(uint *)(lVar8 + 0x55c) | 1;
            *(uint *)(lVar8 + 0x35c) = *(uint *)(lVar8 + 0x35c) | 1;
            *(undefined4 *)(lVar8 + 4) = 0x3fff0180;
            *(undefined4 *)(lVar8 + 4) = 0x3fff0100;
            plVar9 = plVar9 + 1;
          } while (lVar13 != 0);
        }
        else {
          do {
            lVar8 = *plVar9;
            lVar13 = lVar13 + -1;
            *(uint *)(lVar8 + 0x30) = *(uint *)(lVar8 + 0x30) & 0xfffffffe;
            *(uint *)(lVar8 + 0x28) = *(uint *)(lVar8 + 0x28) | 2;
            *(uint *)(lVar8 + 0x28) = *(uint *)(lVar8 + 0x28) & 0xfffffffe;
            *(undefined4 *)(lVar8 + 4) = 0x3fff0080;
            *(undefined4 *)(lVar8 + 4) = 0x3fff0000;
            plVar9 = plVar9 + 1;
          } while (lVar13 != 0);
        }
        uVar5 = uVar5 + 1;
      } while (uVar5 != uVar2);
    }
    uVar10 = uVar10 + 1;
  } while (uVar10 != uVar2);
  return 0;
}


