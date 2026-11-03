// ffff000090723c80  size=288  FUN_ffff000090723c80

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090723c80(void)

{
  long lVar1;
  byte bVar2;
  byte bVar3;
  byte bVar4;
  byte bVar5;
  long lVar6;
  ulong uVar7;
  undefined1 *puVar8;
  
  uVar7 = (ulong)_DAT_ffff000090860888;
  if ((int)_DAT_ffff000090860888 < 1) {
    _DAT_ffff0000908613d0 = 0;
    _DAT_ffff0000908613d4 = 0;
  }
  else {
    _DAT_ffff0000908613d0 = 0;
    _DAT_ffff0000908613d4 = 0;
    puVar8 = &DAT_ffff000090860720;
    lVar6 = 0;
    do {
      bVar2 = puVar8[0x14];
      bVar3 = puVar8[0x15];
      _DAT_ffff0000908613d0 = _DAT_ffff0000908613d0 + 8;
      bVar4 = puVar8[0x16];
      lVar1 = lVar6 + 0xc0;
      bVar5 = puVar8[0x17];
      *(uint *)(&DAT_ffff0000908608a0 + lVar6) = (uint)bVar2;
      *(uint *)(&DAT_ffff0000908608b8 + lVar6) = (uint)bVar3;
      *(uint *)(lVar6 + -0xffff6f79f700) = (uint)bVar2;
      *(uint *)(lVar6 + -0xffff6f79f6e8) = (uint)bVar3;
      *(undefined1 **)(lVar6 + -0xffff6f79f770) = puVar8;
      *(undefined1 **)(lVar6 + -0xffff6f79f758) = puVar8;
      *(undefined1 **)(lVar6 + -0xffff6f79f740) = puVar8;
      _DAT_ffff0000908613d4 =
           _DAT_ffff0000908613d4 + ((uint)bVar3 + (uint)bVar2 + (uint)bVar4 + (uint)bVar5) * 2;
      *(undefined1 **)(lVar6 + -0xffff6f79f728) = puVar8;
      *(undefined1 **)(lVar6 + -0xffff6f79f710) = puVar8;
      *(undefined1 **)(lVar6 + -0xffff6f79f6f8) = puVar8;
      *(undefined1 **)(lVar6 + -0xffff6f79f6e0) = puVar8;
      *(undefined1 **)(lVar6 + -0xffff6f79f6c8) = puVar8;
      puVar8 = puVar8 + 0x18;
      *(undefined1 *)(lVar6 + -0xffff6f79f764) = 0;
      *(undefined4 *)(lVar6 + -0xffff6f79f768) = 0;
      *(undefined1 *)(lVar6 + -0xffff6f79f74c) = 0;
      *(undefined4 *)(lVar6 + -0xffff6f79f750) = 1;
      *(undefined4 *)(lVar6 + -0xffff6f79f738) = 2;
      *(undefined1 *)(lVar6 + -0xffff6f79f734) = 0;
      *(uint *)(lVar6 + -0xffff6f79f730) = (uint)bVar4;
      *(undefined1 *)(lVar6 + -0xffff6f79f71c) = 0;
      *(uint *)(lVar6 + -0xffff6f79f718) = (uint)bVar5;
      *(undefined4 *)(lVar6 + -0xffff6f79f720) = 3;
      *(undefined1 *)(lVar6 + -0xffff6f79f704) = 1;
      *(undefined4 *)(lVar6 + -0xffff6f79f708) = 0;
      *(undefined1 *)(lVar6 + -0xffff6f79f6ec) = 1;
      *(undefined4 *)(lVar6 + -0xffff6f79f6f0) = 1;
      *(undefined4 *)(lVar6 + -0xffff6f79f6d8) = 2;
      *(undefined1 *)(lVar6 + -0xffff6f79f6d4) = 1;
      *(uint *)(lVar6 + -0xffff6f79f6d0) = (uint)bVar4;
      *(undefined1 *)(lVar6 + -0xffff6f79f6bc) = 1;
      *(uint *)(lVar6 + -0xffff6f79f6b8) = (uint)bVar5;
      *(undefined4 *)(lVar6 + -0xffff6f79f6c0) = 3;
      lVar6 = lVar1;
    } while (uVar7 * 0xc0 - lVar1 != 0);
  }
  return;
}


